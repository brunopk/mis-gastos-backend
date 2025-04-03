export const GET_CATEGORIES = 'SELECT * FROM `category`';

export const GET_SUB_CATEGORIES = 'SELECT id, name, category_id as categoryId FROM `subcategory`';

export const GET_GROUPS = 'SELECT id, name, subcategory_id as subcategoryId FROM `group`';

export const GET_ACCOUNTS = 'SELECT * FROM `account`';
