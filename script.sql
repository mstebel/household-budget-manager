CREATE DATABASE household_budget CHARACTER SET utfmb4 COLLATE utf8mb4_polish_ci;
CREATE TABLE transaction (
id BIGINT PRIMARY KEY AUTO_INCREMENT,
type ENUM('earning', 'expense') NOT NULL, 
description VARCHAR(250) NOT NULL, 
amount DOUBLE NOT NULL,
date DATE
);

