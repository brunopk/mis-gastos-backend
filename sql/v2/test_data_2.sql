/* Before invoking, set the corresponding database in the USE statement */

USE `misgastos_v2`;

INSERT INTO `category`(name) VALUES ('Services'), ('Health care'), ('Entertainment');

INSERT INTO `subcategory`(category_id, name) VALUES (1, 'Basic'), (1, 'Internet'), (2, 'Medicine');

INSERT INTO `group`(subcategory_id, name) VALUES (1, 'Gas'), (1, 'Electricity');

INSERT INTO `account`(name) VALUES ('My bank 1'), ('My bank 2'), ('Cash');

INSERT INTO `income_type`(name) VALUES ('Reimbursement'), ('Salary');

INSERT INTO `income_type_account`(`income_type_id`, `account_id`) VALUES (2, 1);

INSERT INTO `category_account`(`category_id`, `account_id`) VALUES (2, 2),  (2, 3), (3, 1);

INSERT INTO `subcategory_account`(`subcategory_id`, `account_id`) VALUES (3, 3);

INSERT INTO `group_account`(`group_id`, `account_id`) VALUES (2, 3);

INSERT INTO `task_config`(
    `task_name`,
    `task_type`,
    `class_name`,
    `cron_expression`,
    `create_google_task`,
    `google_task_title`,
    `google_task_description`,
    `send_mail`,
    `category_id`,
    `account_id`,
    `spend_value`
) VALUES (
    'test_task_1',
    'AUTOMATIC',
    'RecurrentSpendTask',
    '0 * * * * *',
    0,
    NULL,
    NULL,
    0,
    1,
    1,
    10
);

INSERT INTO `spend`(
    date,
    category_id,
    subcategory_id,
    group_id,
    account_id,
    description,
    value
) VALUES (
    DATE(DATE_SUB(NOW(), INTERVAL 3 MONTH)),
    1,
    1,
    1,
    1,
    'Test 1',
    10
), (
    DATE(DATE_SUB(NOW(), INTERVAL 2 MONTH)),
    1,
    1,
    1,
    1,
    'Test 2',
    10
), (
    DATE(DATE_SUB(NOW(), INTERVAL 1 MONTH)),
    1,
    1,
    1,
    1,
    'Test 3',
    10
  ), (
    DATE(NOW()),
    1,
    1,
    1,
    1,
    'Test 4',
    10
 ),(
    DATE(DATE_SUB(NOW(), INTERVAL 3 MONTH)),
    2,
    NULL,
    NULL,
    1,
    'Test 5',
    10
), (
    DATE(DATE_SUB(NOW(), INTERVAL 2 MONTH)),
    2,
    NULL,
    NULL,
    1,
    'Test 6',
    10
 ), (
    DATE(DATE_SUB(NOW(), INTERVAL 1 MONTH)),
    2,
    NULL,
    NULL,
    1,
    'Test 7',
    10
 ), (
    DATE(NOW()),
    2,
    NULL,
    NULL,
    1,
    'Test 8',
    10
 ), (
    DATE(DATE_SUB(NOW(), INTERVAL 3 MONTH)),
    2,
    3,
    NULL,
    1,
    'Test 9',
    10
), (
    DATE(DATE_SUB(NOW(), INTERVAL 2 MONTH)),
    2,
    3,
    NULL,
    1,
    'Test 10',
    10
 ), (
    DATE(DATE_SUB(NOW(), INTERVAL 1 MONTH)),
    2,
    3,
    NULL,
    1,
    'Test 11',
    10
), (
    DATE(NOW()),
    2,
    3,
    NULL,
    1,
    'Test 12',
    10
 );

INSERT INTO `income`(
    date,
    income_type_id,
    account_id,
    spend_id,
    description,
    value
) VALUES (
    DATE(DATE_SUB(NOW(), INTERVAL 2 MONTH)),
    1,
    1,
    1,
    'Test 1',
    1
), (
    DATE(DATE_SUB(NOW(), INTERVAL 3 MONTH)),
    2,
    1,
    NULL,
    'Test 1',
    100
), (
   DATE(DATE_SUB(NOW(), INTERVAL 2 MONTH)),
   2,
   1,
   NULL,
   'Test 2',
   100
), (
   DATE(DATE_SUB(NOW(), INTERVAL 1 MONTH)),
   2,
   1,
   NULL,
   'Test 3',
   100
), (
   DATE(NOW()),
   2,
   1,
   NULL,
   'Test 4',
   100
)
