USE `misgastos`;

DROP TABLE IF EXISTS `spend`;

DROP TABLE IF EXISTS `group`;

DROP TABLE IF EXISTS `sub_category`;

DROP TABLE IF EXISTS `category`;

DROP TABLE IF EXISTS `account`;

CREATE TABLE `account` (
	`name` VARCHAR(256) NOT NULL PRIMARY KEY
);

CREATE TABLE `category` (
	`name` VARCHAR(256) NOT NULL PRIMARY KEY
);

CREATE TABLE `sub_category` (
    `name` VARCHAR(256) NOT NULL,
    `category` VARCHAR(256) NOT NULL,
    CONSTRAINT PRIMARY KEY (`name`, `category`),
    CONSTRAINT `fk_category_name` FOREIGN KEY `sub_category`(`category`) REFERENCES `category`(`name`)
);

CREATE TABLE `group` (
    `name` VARCHAR(256) NOT NULL,
    `category` VARCHAR(256) NOT NULL,
    `sub_category` VARCHAR(256) NOT NULL,
    CONSTRAINT PRIMARY KEY (`name`, `category`, `sub_category`),
    CONSTRAINT `fk_sub_category_category_name` 
    	FOREIGN KEY `group`(`category`, `sub_category`) REFERENCES `sub_category`(`category`, `name`)
);

CREATE TABLE `spend` (
	`id` BIGINT UNSIGNED AUTO_INCREMENT NOT NULL,
	`date` DATE NOT NULL,
    `category` VARCHAR(256) NOT NULL,
    `sub_category` VARCHAR(256) NOT NULL,
    `group` VARCHAR(256) NOT NULL,
    `account` VARCHAR(256) NOT NULL,
    `value` BIGINT UNSIGNED NOT NULL,
    `description` TEXT,
    `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP, 
    CONSTRAINT PRIMARY KEY (`id`),
    CONSTRAINT `fk_spend_group_name` FOREIGN KEY `spend`(`category`, `sub_category`, `group`) REFERENCES `group`(`category`, `sub_category`, `name`),
    CONSTRAINT `fk_spend_account_name` FOREIGN KEY `spend`(`account`) REFERENCES `account`(`name`)
);

