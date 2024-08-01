CREATE TABLE IF NOT EXISTS `member`
(
    `id`            INT          NOT NULL,
    `nickname`      VARCHAR(50)  NULL,
    `profile_image` VARCHAR(255) NULL,
    `social_id`     VARCHAR(50)  NULL,
    `disable`       TINYINT  DEFAULT 0,
    `created_at`    datetime DEFAULT NOW(),
    `updated_at`    datetime     NULL,
    PRIMARY KEY (`id`)
);

CREATE TABLE IF NOT EXISTS `room`
(
    `id`          INT         NOT NULL,
    `name`        VARCHAR(50) NULL,
    `description` TEXT        NULL,
    `disable`     TINYINT  DEFAULT 0,
    `created_at`  datetime DEFAULT NOW(),
    `updated_at`  datetime    NULL,
    PRIMARY KEY (`id`)
);

CREATE TABLE IF NOT EXISTS `room_member`
(
    `id`         INT      NOT NULL,
    `member_id`  INT      NOT NULL,
    `room_id`    INT      NOT NULL,
    `is_leader`  BOOLEAN  NULL,
    `disable`    TINYINT  DEFAULT 0,
    `created_at` datetime DEFAULT NOW(),
    `updated_at` datetime NULL,
    PRIMARY KEY (`id`)
);

CREATE TABLE IF NOT EXISTS `member_schedule`
(
    `id`         INT       NOT NULL,
    `room_id`    INT       NOT NULL,
    `start_date` TIMESTAMP NULL,
    `end_date`   TIMESTAMP NULL,
    `disable`    TINYINT  DEFAULT 0,
    `created_at` datetime DEFAULT NOW(),
    `updated_at` datetime  NULL,
    PRIMARY KEY (`id`)
);

CREATE TABLE IF NOT EXISTS `member_schedule_record`
(
    `id`                 INT       NOT NULL,
    `member_id`          INT       NOT NULL,
    `member_schedule_id` INT       NOT NULL,
    `room_id`            INT       NOT NULL,
    `start_date`         TIMESTAMP NULL,
    `end_date`           TIMESTAMP NULL,
    `disable`            TINYINT  DEFAULT 0,
    `created_at`         datetime DEFAULT NOW(),
    `updated_at`         datetime  NULL,
    PRIMARY KEY (`id`)
);

TRUNCATE TABLE `member`;
TRUNCATE TABLE `room`;
TRUNCATE TABLE `room_member`;
TRUNCATE TABLE `member_schedule`;
TRUNCATE TABLE `member_schedule_record`;