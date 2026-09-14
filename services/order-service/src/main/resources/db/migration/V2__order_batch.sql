CREATE TABLE order_batch(
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    table_id BIGINT NOT NULL,
    batch_number INT NOT NULL,
    fired_at TIMESTAMP,
    fired_by VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);