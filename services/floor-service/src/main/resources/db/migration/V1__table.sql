CREATE TABLE restaurant_table (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    label VARCHAR(255) NOT NULL,
    capacity INT NOT NULL,
    table_state VARCHAR(20) NOT NULL,
    version BIGINT NOT NULL DEFAULT 0,
    occupied_since DATETIME NULL
)