import express, { NextFunction, Request, RequestHandler, Response } from 'express';
import { getAccounts, getCategories, getGroups, getSpends, getSubCategories } from './handlers';

const app = express();

const asyncHandler =
  (func: RequestHandler) => (req: Request, res: Response, next: NextFunction) => {
    Promise.resolve(func(req, res, next)).catch(next);
  };

app.get('/categories', asyncHandler(getCategories));

app.get('/subcategories', asyncHandler(getSubCategories));

app.get('/groups', asyncHandler(getGroups));

app.get('/accounts', asyncHandler(getAccounts));

app.get('/spends', asyncHandler(getSpends));

export default app;
