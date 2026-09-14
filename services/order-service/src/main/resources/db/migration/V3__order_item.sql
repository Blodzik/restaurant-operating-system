CREATE TABLE order_item(
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    batch_id BIGINT NOT NULL,
    guest_id BIGINT NOT NULL,
    menu_item_id BIGINT NOT NULL,
    name_snapshot VARCHAR(255) NOT NULL,
    price_snapshot DECIMAL(10,2) NOT NULL,
    destination_snapshot VARCHAR(50) NOT NULL,
    quantity INT NOT NULL,
    status VARCHAR(50) NOT NULL,
    FOREIGN KEY (batch_id) REFERENCES order_batch(id),
    FOREIGN KEY (guest_id) REFERENCES guest(id)
);