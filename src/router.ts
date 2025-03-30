import express from 'express';
import { getCategories, getSubCategories, getGroups, getAccounts } from './handlers';

const app = express();

app.get('/categories', getCategories);

app.get('/subcategories', getSubCategories);

app.get('/groups', getGroups);

app.get('/accounts', getAccounts);

export default app;
