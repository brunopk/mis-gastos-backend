DROP DATABASE IF EXISTS `misgastos`;

CREATE DATABASE `misgastos`;

USE `misgastos`;

CREATE TABLE `income_type` (
    `id` INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
	`name` VARCHAR(256)
);

CREATE TABLE `account` (
	`id` INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
	`name` VARCHAR(256)
);

CREATE TABLE `category` (
    `id` INT UNSIGNED AUTO_INCREMENT PRIMARY KEY, 
	`name` VARCHAR(256)
);

CREATE TABLE `subcategory` (
    `id` INT UNSIGNED AUTO_INCREMENT PRIMARY KEY, 
	`name` VARCHAR(256),
    `category_id` INT UNSIGNED NOT NULL,
    CONSTRAINT `fk_subcategory_category` FOREIGN KEY `subcategory`(`category_id`) REFERENCES `category`(`id`)
);

CREATE TABLE `group` (
    `id` INT UNSIGNED AUTO_INCREMENT PRIMARY KEY, 
	`name` VARCHAR(256),
	`category_id` INT UNSIGNED NOT NULL,
    `subcategory_id` INT UNSIGNED NOT NULL,
    CONSTRAINT `uq_id_subcategory_id_category_id` UNIQUE (`id`, `subcategory_id`, `category_id`),
    CONSTRAINT `fk_group_category` FOREIGN KEY `group`(`category_id`) REFERENCES `subcategory`(`category_id`),
    CONSTRAINT `fk_group_subcategory` FOREIGN KEY `group`(`subcategory_id`) REFERENCES `subcategory`(`id`)
);

CREATE TABLE `category_account` (
	`id` INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
	`category_id` INT UNSIGNED NOT NULL,
	`account_id` INT UNSIGNED NOT NULL,
	CONSTRAINT `fk_category_account_category`
	    FOREIGN KEY `category_account`(`category_id`)
	    REFERENCES `category`(`id`),
	CONSTRAINT `fk_category_account_account`
        FOREIGN KEY `category_account`(`account_id`)
        REFERENCES `account`(`id`)
);

CREATE TABLE `subcategory_account` (
	`id` INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
	`subcategory_id` INT UNSIGNED NOT NULL,
	`account_id` INT UNSIGNED NOT NULL,
	CONSTRAINT `fk_subcategory_account_subcategory`
	    FOREIGN KEY `subcategory_account`(`subcategory_id`)
	    REFERENCES `subcategory`(`id`),
	CONSTRAINT `fk_subcategory_account_account`
        FOREIGN KEY `category_account`(`account_id`)
        REFERENCES `account`(`id`)
);

CREATE TABLE `group_account` (
	`id` INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
	`group_id` INT UNSIGNED NOT NULL,
	`account_id` INT UNSIGNED NOT NULL,
	CONSTRAINT `fk_group_account_group`
	    FOREIGN KEY `group_account`(`group_id`)
	    REFERENCES `group`(`id`),
	CONSTRAINT `fk_group_account_account`
        FOREIGN KEY `group_account`(`account_id`)
        REFERENCES `account`(`id`)
);

CREATE TABLE `income_type_account` (
	`id` INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
	`income_type_id` INT UNSIGNED NOT NULL,
	`account_id` INT UNSIGNED NOT NULL,
	CONSTRAINT `fk_income_type_account_income_type`
	    FOREIGN KEY `income_type_account`(`income_type_id`)
	    REFERENCES `income_type`(`id`),
	CONSTRAINT `fk_income_type_account_account`
        FOREIGN KEY `income_type_account`(`account_id`)
        REFERENCES `account`(`id`)
);

CREATE TABLE `scheduled_task_config` (
	`id` INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
	`task_type` ENUM('Automatic', 'Manual') NOT NULL,
    `send_task` TINYINT(1) NOT NULL,
    `send_mail` TINYINT(1) NOT NULL,
    `mail_subject` TEXT,
    `mail_body` TEXT,
    `category_id` INT UNSIGNED,
    `subcategory_id` INT UNSIGNED,
    `group_id` INT UNSIGNED,
    `account_id` INT UNSIGNED NOT NULL,
    `spend_value` BIGINT UNSIGNED NOT NULL,
    `spend_description` TEXT,
    `cron_expression` VARCHAR(64) NOT NULL,
    `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    `updated_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT `fk_task_config_group`
        FOREIGN KEY `task_config`(`group_id`, `subcategory_id`, `category_id`)
        REFERENCES `group`(`id`, `subcategory_id`, `category_id`),
    CONSTRAINT `fk_task_config_account`
        FOREIGN KEY `task_config`(`account_id`)
        REFERENCES `account`(`id`)
);

CREATE TABLE `task` (
	`id` BIGINT UNSIGNED AUTO_INCREMENT NOT NULL PRIMARY KEY,
	`google_task_id` VARCHAR(128),
	`completed` TINYINT(1) NOT NULL DEFAULT 0,
    `spend_value` BIGINT UNSIGNED NOT NULL,
    `config_id` INT UNSIGNED NOT NULL,
    `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    `updated_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT `fk_task_scheduled_task_config`
        FOREIGN KEY `task`(`config_id`)
        REFERENCES `scheduled_task_config`(`id`)
);

CREATE TABLE `spend` (
	`id` BIGINT UNSIGNED AUTO_INCREMENT NOT NULL PRIMARY KEY,
	`date` TIMESTAMP NOT NULL,
    `category_id` INT UNSIGNED,
    `subcategory_id` INT UNSIGNED,
    `group_id` INT UNSIGNED,
    `account_id` INT UNSIGNED NOT NULL,
    `description` TEXT,
    `task_id` BIGINT UNSIGNED,
    `value` BIGINT UNSIGNED NOT NULL,
    `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT `fk_spend_account` 
        FOREIGN KEY `spend`(`account_id`) 
        REFERENCES `account`(`id`),
    CONSTRAINT `fk_spend_group` 
    	FOREIGN KEY `spend`(`group_id`, `subcategory_id`, `category_id`) 
    	REFERENCES `group`(`id`, `subcategory_id`, `category_id`),
    CONSTRAINT `fk_spend_task`
        FOREIGN KEY `spend`(`task_id`)
        REFERENCES `task`(`id`)
);

CREATE TABLE `income` (
	`id` BIGINT UNSIGNED AUTO_INCREMENT NOT NULL PRIMARY KEY,
	`date` TIMESTAMP NOT NULL,
	`income_type_id` INT UNSIGNED NOT NULL,
    `account_id` INT UNSIGNED NOT NULL,
    `spend_id` BIGINT UNSIGNED,
    `description` TEXT,
    `value` BIGINT UNSIGNED NOT NULL,
    `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT `fk_income_income_type`
        FOREIGN KEY `income`(`income_type_id`)
        REFERENCES `income_type`(`id`),
    CONSTRAINT `fk_income_account`
        FOREIGN KEY `income`(`account_id`)
        REFERENCES `account`(`id`),
    CONSTRAINT `fk_income_spend`
        FOREIGN KEY `income`(`spend_id`)
        REFERENCES `spend`(`id`)
);
