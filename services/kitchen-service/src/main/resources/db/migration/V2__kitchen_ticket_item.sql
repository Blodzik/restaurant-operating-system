CREATE TABLE kitchen_ticket_item(
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    ticket_id BIGINT NOT NULL,
    source_order_item_id BIGINT NOT NULL,
    name_snapshot VARCHAR(255) NOT NULL,
    quantity INT NOT NULL,
    status VARCHAR(50) NOT NULL,
    CONSTRAINT fk_kitchen_ticket FOREIGN KEY (ticket_id) REFERENCES kitchen_ticket(id) ON DELETE CASCADE
);