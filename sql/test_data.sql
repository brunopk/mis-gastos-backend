USE `misgastos`;

SET FOREIGN_KEY_CHECKS = 0;

TRUNCATE TABLE `spend`;

TRUNCATE TABLE `account`;

TRUNCATE TABLE `group`; 

TRUNCATE TABLE `subcategory`;

TRUNCATE TABLE `category`;

SET FOREIGN_KEY_CHECKS = 1;

INSERT INTO `category`(name) VALUES ('Services'), ('Health care');

INSERT INTO `subcategory`(category_id, name) VALUES (1, 'Basic'), (1, 'Internet'), (2, 'Medicine');

INSERT INTO `group`(category_id, subcategory_id, name) VALUES (1, 1, 'Gas'), (1, 1, 'Electricity');

INSERT INTO `account`(name) VALUES ('My bank');

/**
 * Note that this tuple won't be inserted because it fails '' constraint : 
 * 
 * 	('2025-03-29', 1, 2, 1, 1, 10, 'Test 2')
 * 
 **/

INSERT INTO `spend`(
	date, 
	category_id, 
	subcategory_id, 
	group_id, 
	account_id, 
	value, 
	description
) VALUES 
	('2025-03-29', 1, 1, 1, 1, 10, 'Test 1'),
	('2025-03-29', 2, NULL, NULL, 1, 10, 'Test 2'),
	('2025-03-29', 2, 2, NULL, 1, 10, 'Test 3');
