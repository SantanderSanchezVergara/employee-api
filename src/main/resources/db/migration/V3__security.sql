CREATE TABLE users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(20) NOT NULL
);

INSERT INTO users (username, password, role)
VALUES ('admin', '$2a$10$YnADC4154Z6g9m3oUgAaVOpZyjo9hl1zHZ4E/g4ZSEPASeFesNY8S', 'ADMIN');


CREATE INDEX users_username ON users(username);