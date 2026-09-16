package io.github.blodzik.restaurant.order.controller;

import io.github.blodzik.restaurant.order.dto.request.AddItemRequest;
import io.github.blodzik.restaurant.order.entity.Guest;
import io.github.blodzik.restaurant.order.repository.GuestRepository;
import io.github.blodzik.restaurant.order.repository.OrderBatchRepository;
import io.github.blodzik.restaurant.order.repository.OrderItemRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import tools.jackson.databind.ObjectMapper;

import java.math.BigDecimal;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
class OrderControllerIT {

    @Container
    static MySQLContainer<?> mysql = new MySQLContainer<>("mysql:8.4");

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", mysql::getJdbcUrl);
        registry.add("spring.datasource.username", mysql::getUsername);
        registry.add("spring.datasource.password", mysql::getPassword);
    }

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private GuestRepository guestRepository;

    @Autowired
    private OrderBatchRepository batchRepository;

    @Autowired
    private OrderItemRepository itemRepository;

    @BeforeEach
    void setUp() {
        itemRepository.deleteAll();
        batchRepository.deleteAll();
        guestRepository.deleteAll();
    }

    @Test
    void shouldExecuteFullWaiterHappyPath() throws Exception {
        Guest guest = new Guest();
        guest.setTableId(4L);
        guest.setLabel("Seat 1");
        guest.setActive(true);
        guest = guestRepository.save(guest);

        AddItemRequest request = new AddItemRequest(
                101L, "Classic Burger", new BigDecimal("15.00"), "KITCHEN", 1
        );

        mockMvc.perform(post("/tables/4/guests/" + guest.getId() + "/items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nameSnapshot").value("Classic Burger"))
                .andExpect(jsonPath("$.status").value("PENDING"));

        mockMvc.perform(post("/tables/4/batches/fire")
                        .param("waiterName", "Alex"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firedBy").value("Alex"))
                .andExpect(jsonPath("$.firedAt").isNotEmpty());

        mockMvc.perform(get("/tables/4/tab"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].status").value("QUEUED"));
    }
}