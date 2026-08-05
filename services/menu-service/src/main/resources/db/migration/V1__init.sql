-- V1: baseline, tables added in Week 1

CREATE TABLE menu (
                      id BIGINT AUTO_INCREMENT PRIMARY KEY,
                      name VARCHAR(255) NOT NULL,
                      price DECIMAL(10,2) NOT NULL
);