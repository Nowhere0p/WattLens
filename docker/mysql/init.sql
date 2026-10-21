-- One database per service (each service owns its schema and Flyway history)
CREATE DATABASE IF NOT EXISTS `home_energy_tracker_user`
    CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE DATABASE IF NOT EXISTS `home_energy_tracker_device`
    CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
