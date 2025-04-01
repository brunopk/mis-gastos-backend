import { RequestHandler } from 'express';
import pool from './db/pool';
import { GET_ACCOUNTS, GET_CATEGORIES, GET_GROUPS, GET_SUB_CATEGORIES } from './db/query';
import loggerFactory from './logging';

const logger = loggerFactory('express');

export const getCategories: RequestHandler = async (_, res) => {
  const conn = await pool.getConnection();
  const [results] = await conn.execute(GET_CATEGORIES);
  res.json(results);
};

export const getSubCategories: RequestHandler = async (_, res) => {
  const conn = await pool.getConnection();
  const [results] = await conn.execute(GET_SUB_CATEGORIES);
  res.json(results);
};

export const getGroups: RequestHandler = async (_, res) => {
  try {
    const conn = await pool.getConnection();
    const [results] = await conn.execute(GET_GROUPS);
    res.json(results);
  } catch (err) {
    logger.error('Error obtaining groups: ', err);
    res.json(err);
    res.status(500);
  }
};

export const getAccounts: RequestHandler = async (_, res) => {
  try {
    const conn = await pool.getConnection();
    const [results] = await conn.execute(GET_ACCOUNTS);
    res.json(results);
  } catch (err) {
    logger.error('Error obtaining accounts: ', err);
    res.json(err);
    res.status(500);
  }
};
