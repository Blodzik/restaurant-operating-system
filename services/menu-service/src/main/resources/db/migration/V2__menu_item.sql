CREATE TABLE menu_item (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    category_id BIGINT NOT NULL,
    name VARCHAR(255) NOT NULL,
    description VARCHAR(255),
    base_price DECIMAL(10, 2) NOT NULL,
    active BOOLEAN NOT NULL DEFAULT TRUE,
    destination VARCHAR(20) NOT NULL,
    track_stock BOOLEAN NOT NULL DEFAULT FALSE,
    stock_count INT,
    CONSTRAINT fk_menu_item_category FOREIGN KEY (category_id) REFERENCES category(id)
);