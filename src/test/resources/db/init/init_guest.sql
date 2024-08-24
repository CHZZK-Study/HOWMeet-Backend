CREATE TABLE IF NOT EXISTS `guest`
(
    `id`                BIGINT AUTO_INCREMENT NOT NULL,
    `guest_schedule_id` BIGINT                NOT NULL,
    `nickname`          VARCHAR(255)          NULL,
    `password`          VARCHAR(255)          NULL,
    `disable`           TINYINT  DEFAULT 0,
    `created_at`        datetime DEFAULT NOW(),
    `updated_at`        datetime              NULL,
    PRIMARY KEY (`id`)
);

CREATE TABLE IF NOT EXISTS `guest_schedule`
(
    `id`         BIGINT AUTO_INCREMENT NOT NULL,
    `name`       VARCHAR(255)          NOT NULL,
    `start_date` TIMESTAMP             NOT NULL,
    `end_date`   TIMESTAMP             NOT NULL,
    `disable`    TINYINT  DEFAULT 0,
    `created_at` datetime DEFAULT NOW(),
    `updated_at` datetime              NULL,
    PRIMARY KEY (`id`)
);

CREATE TABLE IF NOT EXISTS `guest_schedule_record`
(
    `id`                BIGINT AUTO_INCREMENT NOT NULL,
    `guest_id`          BIGINT                NOT NULL,
    `guest_schedule_id` BIGINT                NOT NULL,
    `start_date`        TIMESTAMP             NOT NULL,
    `end_date`          TIMESTAMP             NOT NULL,
    `disable`           TINYINT  DEFAULT 0,
    `created_at`        datetime DEFAULT NOW(),
    `updated_at`        datetime              NULL,
    PRIMARY KEY (`id`)
);

TRUNCATE TABLE `guest`;
TRUNCATE TABLE `guest_schedule`;
TRUNCATE TABLE `guest_schedule_record`;