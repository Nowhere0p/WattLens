CREATE TABLE IF NOT EXISTS `user` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `first_name` VARCHAR(255) NOT NULL,
    `last_name` VARCHAR(255) NOT NULL,
    `email` VARCHAR(255) NOT NULL UNIQUE,
    `address` VARCHAR(255) NOT NULL,
    `alerting` BOOLEAN NOT NULL DEFAULT FALSE,
    `energy_alerting_threshold` DOUBLE NOT NULL DEFAULT 0.0
);
