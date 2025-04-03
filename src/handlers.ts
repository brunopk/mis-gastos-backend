import { RequestHandler } from 'express';
import pool from './db/pool';
import { GET_ACCOUNTS, GET_CATEGORIES, GET_GROUPS, GET_SUB_CATEGORIES } from './db/query';

export const getCategories: RequestHandler = async (_, res) => {
  const conn = await pool.getConnection();

  const [results] = await conn.execute(GET_CATEGORIES);

  conn.release();
  res.json(results);
};

export const getSubCategories: RequestHandler = async (_, res) => {
  const conn = await pool.getConnection();
  const [results] = await conn.execute(GET_SUB_CATEGORIES);
  conn.release();
  res.json(results);
};

export const getGroups: RequestHandler = async (_, res) => {
  const conn = await pool.getConnection();
  const [results] = await conn.execute(GET_GROUPS);
  conn.release();
  res.json(results);
};

export const getAccounts: RequestHandler = async (_, res) => {
  const conn = await pool.getConnection();
  const [results] = await conn.execute(GET_ACCOUNTS);
  conn.release();
  res.json(results);
};
