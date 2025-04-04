export const GET_CATEGORIES = 'SELECT * FROM `category`';

export const GET_SUBCATEGORIES = 'SELECT id, name, category_id as categoryId FROM `subcategory`';

export const GET_GROUPS = 'SELECT id, name, subcategory_id as subcategoryId FROM `group`';

export const GET_ACCOUNTS = 'SELECT * FROM `account`';

export const GET_SPENDS = `SELECT 
  id, 
  date, 
  category_id as categoryId, 
  subcategory_id as subcategoryId, 
  group_id as groupId, 
  account_id as accountId, 
  description, 
  value 
FROM \`spend\``
