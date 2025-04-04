import { RequestHandler } from 'express';
import pool from './db/pool';
import * as queries from './db/query';

export const getCategories: RequestHandler = async (_, res) => {
  const conn = await pool.getConnection();
  const [results] = await conn.execute(queries.GET_CATEGORIES);
  conn.release();
  res.json(results);
};

export const getSubCategories: RequestHandler = async (_, res) => {
  const conn = await pool.getConnection();
  const [results] = await conn.execute(queries.GET_SUBCATEGORIES);
  conn.release();
  res.json(results);
};

export const getGroups: RequestHandler = async (_, res) => {
  const conn = await pool.getConnection();
  const [results] = await conn.execute(queries.GET_GROUPS);
  conn.release();
  res.json(results);
};

export const getAccounts: RequestHandler = async (_, res) => {
  const conn = await pool.getConnection();
  const [results] = await conn.execute(queries.GET_ACCOUNTS);
  conn.release();
  res.json(results);
};

export const getSpends: RequestHandler = async (_, res) => {
  const conn = await pool.getConnection();
  const [results] = await conn.execute(queries.GET_SPENDS);
  console.log(queries.GET_SPENDS)
  conn.release();
  res.json(results);
};
