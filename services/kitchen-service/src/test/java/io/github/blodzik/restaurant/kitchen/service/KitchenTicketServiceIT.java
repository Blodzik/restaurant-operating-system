package io.github.blodzik.restaurant.kitchen.service;

import io.github.blodzik.restaurant.kitchen.config.RabbitMQConfig;
import io.github.blodzik.restaurant.kitchen.entity.KitchenTicketItem;
import io.github.blodzik.restaurant.kitchen.entity.TicketStatus;
import io.github.blodzik.restaurant.kitchen.repository.KitchenTicketItemRepository;
import io.github.blodzik.restaurant.kitchen.repository.KitchenTicketRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.containers.RabbitMQContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;
import java.util.Map;
import java.time.Duration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;

@SpringBootTest
@Testcontainers
public class KitchenTicketServiceIT {
    @Container
    @ServiceConnection
    static RabbitMQContainer rabbit = new RabbitMQContainer("rabbitmq:3.13-management");

    @Container
    @ServiceConnection
    static MySQLContainer<?> mysql = new MySQLContainer<>("mysql:8.4");

    @Autowired
    RabbitTemplate rabbitTemplate;

    @Autowired
    KitchenTicketRepository ticketRepository;

    @Autowired
    KitchenTicketItemRepository itemRepository;

    @AfterEach
    void cleanUp() {
        ticketRepository.deleteAll();
    }

    @Test
    void shouldSaveOnlyKitchenItems_whenBatchFired() {
        Map<String, Object> event = Map.of(
                "batchId", 1,
                "tableSnapshot", "Table 5",
                "batchNumber", 1,
                "firedAt", "2026-10-09T12:00:00",
                "waiterName", "Anna",
                "items", List.of(
                        Map.of("sourceOrderItemId", 10, "nameSnapshot", "Burger",
                                "quantity", 2, "destinationSnapshot", "KITCHEN"),
                        Map.of("sourceOrderItemId", 11, "nameSnapshot", "Cola",
                                "quantity", 1, "destinationSnapshot", "BAR")
                )
        );

        rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE_NAME, RabbitMQConfig.ROUTING_KEY, event);

        await().atMost(Duration.ofSeconds(10)).untilAsserted(() ->
                assertThat(ticketRepository.count()).isEqualTo(1));

        List<KitchenTicketItem> items = itemRepository.findAll();
        assertThat(items).hasSize(1);
        assertThat(items.get(0).getNameSnapshot()).isEqualTo("Burger");
        assertThat(items.get(0).getQuantity()).isEqualTo(2);
        assertThat(items.get(0).getStatus()).isEqualTo(TicketStatus.PENDING);
    }

    @Test
    void shouldNotCreateTicket_whenBatchHasOnlyDrinks() throws Exception {
        Map<String, Object> event = Map.of(
                "batchId", 2,
                "tableSnapshot", "Table 7",
                "batchNumber", 1,
                "firedAt", "2026-10-09T12:05:00",
                "waiterName", "Piotr",
                "items", List.of(
                        Map.of("sourceOrderItemId", 20, "nameSnapshot", "Beer",
                                "quantity", 3, "destinationSnapshot", "BAR")
                )
        );

        rabbitTemplate.convertAndSend("", RabbitMQConfig.QUEUE_NAME, event);

        Thread.sleep(2000);
        assertThat(ticketRepository.count()).isZero();
    }
}
