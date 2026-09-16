CREATE TABLE order_item_modifier(
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    order_item_id BIGINT NOT NULL,
    modifier_name VARCHAR(255) NOT NULL,
    price_delta_snapshot DECIMAL(10, 2) NOT NULL,
    FOREIGN KEY (order_item_id) REFERENCES order_item(id)
);