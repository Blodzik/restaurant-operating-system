CREATE TABLE category (
      id BIGINT AUTO_INCREMENT PRIMARY KEY,
      name VARCHAR(255) NOT NULL,
      display_order INT,
      active BOOLEAN NOT NULL DEFAULT TRUE
);