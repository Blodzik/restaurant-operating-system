CREATE TABLE kitchen_ticket(
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    source_batch_id BIGINT NOT NULL,
    table_label_snapshot VARCHAR(255) NOT NULL,
    batch_number INT NOT NULL,
    fired_at TIMESTAMP,
    waiter_name VARCHAR(255) NOT NULL
);