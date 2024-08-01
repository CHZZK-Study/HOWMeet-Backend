CREATE TABLE IF NOT EXISTS `member`
(
    `id`            BIGINT AUTO_INCREMENT NOT NULL,
    `nickname`      VARCHAR(50)           NULL,
    `profile_image` VARCHAR(255)          NULL,
    `social_id`     VARCHAR(50)           NULL,
    `disable`       TINYINT  DEFAULT 0,
    `created_at`    datetime DEFAULT NOW(),
    `updated_at`    datetime              NULL,
    PRIMARY KEY (`id`)
);

CREATE TABLE IF NOT EXISTS `room`
(
    `id`          BIGINT AUTO_INCREMENT NOT NULL,
    `name`        VARCHAR(50)           NULL,
    `description` TEXT                  NULL,
    `disable`     TINYINT  DEFAULT 0,
    `created_at`  datetime DEFAULT NOW(),
    `updated_at`  datetime              NULL,
    PRIMARY KEY (`id`)
);

CREATE TABLE IF NOT EXISTS `room_member`
(
    `id`         BIGINT AUTO_INCREMENT NOT NULL,
    `member_id`  BIGINT                NOT NULL,
    `room_id`    BIGINT                NOT NULL,
    `is_leader`  BOOLEAN               NULL,
    `disable`    TINYINT  DEFAULT 0,
    `created_at` datetime DEFAULT NOW(),
    `updated_at` datetime              NULL,
    PRIMARY KEY (`id`)
);

CREATE TABLE IF NOT EXISTS `member_schedule`
(
    `id`         BIGINT AUTO_INCREMENT NOT NULL,
    `room_id`    BIGINT                NOT NULL,
    `start_date` TIMESTAMP             NULL,
    `end_date`   TIMESTAMP             NULL,
    `disable`    TINYINT  DEFAULT 0,
    `created_at` datetime DEFAULT NOW(),
    `updated_at` datetime              NULL,
    PRIMARY KEY (`id`)
);

CREATE TABLE IF NOT EXISTS `member_schedule_record`
(
    `id`                 BIGINT AUTO_INCREMENT NOT NULL,
    `member_id`          BIGINT                NOT NULL,
    `member_schedule_id` BIGINT                NOT NULL,
    `room_id`            BIGINT                NOT NULL,
    `start_date`         TIMESTAMP             NULL,
    `end_date`           TIMESTAMP             NULL,
    `disable`            TINYINT  DEFAULT 0,
    `created_at`         datetime DEFAULT NOW(),
    `updated_at`         datetime              NULL,
    PRIMARY KEY (`id`)
);

TRUNCATE TABLE `member`;
TRUNCATE TABLE `room`;
TRUNCATE TABLE `room_member`;
TRUNCATE TABLE `member_schedule`;
TRUNCATE TABLE `member_schedule_record`;