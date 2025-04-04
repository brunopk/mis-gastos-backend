DROP DATABASE IF EXISTS `misgastos`;

CREATE DATABASE `misgastos`;

USE `misgastos`;

DROP TABLE IF EXISTS `spend`;

DROP TABLE IF EXISTS `group`;

DROP TABLE IF EXISTS `subcategory`;

DROP TABLE IF EXISTS `category`;

DROP TABLE IF EXISTS `account`;

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

CREATE TABLE `spend` (
	`id` BIGINT UNSIGNED AUTO_INCREMENT NOT NULL,
	`date` DATE NOT NULL,
    `category_id` INT UNSIGNED,
    `subcategory_id` INT UNSIGNED,
    `group_id` INT UNSIGNED,
    `account_id` INT UNSIGNED NOT NULL,
    `description` TEXT,
    `value` BIGINT UNSIGNED NOT NULL,
    `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP, 
    CONSTRAINT PRIMARY KEY (`id`),
    CONSTRAINT `fk_spend_account` 
        FOREIGN KEY `spend`(`account_id`) 
        REFERENCES `account`(`id`),
    CONSTRAINT `fk_spend_group` 
    	FOREIGN KEY `spend`(`group_id`, `subcategory_id`, `category_id`) 
    	REFERENCES `group`(`id`, `subcategory_id`, `category_id`)
);

