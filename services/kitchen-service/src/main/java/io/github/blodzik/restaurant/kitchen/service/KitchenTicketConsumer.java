package io.github.blodzik.restaurant.kitchen.service;

import io.github.blodzik.restaurant.kitchen.entity.KitchenTicket;
import io.github.blodzik.restaurant.kitchen.entity.KitchenTicketItem;
import io.github.blodzik.restaurant.kitchen.entity.TicketStatus;
import io.github.blodzik.restaurant.kitchen.event.BatchFireEvent;
import io.github.blodzik.restaurant.kitchen.repository.KitchenTicketRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class KitchenTicketConsumer {
    private final KitchenTicketRepository kitchenTicketRepository;

    @Transactional
    @RabbitListener(queues = "restaurant.orders.queue")
    public void consumeBatchFireEvent(BatchFireEvent event) {
        List<KitchenTicketItem> kitchenItems = event.items().stream()
                .filter(itemDTO -> "KITCHEN".equalsIgnoreCase(itemDTO.destinationSnapshot()))
                .map(itemDTO -> {
                    KitchenTicketItem ticket = new KitchenTicketItem();
                    ticket.setSourceOrderItemId(itemDTO.sourceOrderItemId());
                    ticket.setNameSnapshot(itemDTO.nameSnapshot());
                    ticket.setQuantity(itemDTO.quantity());
                    ticket.setStatus(TicketStatus.PENDING);
                    return ticket;
                })
                .toList();

        if(kitchenItems.isEmpty()) {
            return;
        }

        KitchenTicket ticket = new KitchenTicket();
        ticket.setSourceBatchId(event.batchId());
        ticket.setTableLabelSnapshot(event.tableSnapshot());
        ticket.setBatchNumber(event.batchNumber());
        ticket.setFiredAt(event.firedAt());
        ticket.setWaiterName(event.waiterName());

        kitchenItems.forEach(item -> item.setTicket(ticket));
        ticket.setItems(kitchenItems);

        kitchenTicketRepository.save(ticket);
    }
}
