import dotenv from 'dotenv-flow';
import loggerFactory from './logging';

type Config = {
  port: string;
  db: {
    host: string;
    database: string;
    user: string;
    password: string;
  };
};

let config: Config | null = null;

const logger = loggerFactory('express');

export default function getConfig() {
  if (!config) {
    logger.info('Loading configurations from environment variables');

    dotenv.config();

    if (!process.env.DB_HOST) throw new Error('DB_HOST not defined');
    if (!process.env.DB_DATABASE) throw new Error('DB_DATABASE not defined');
    if (!process.env.DB_USER) throw new Error('DB_USER not defined');
    if (!process.env.DB_PASSWORD) throw new Error('DB_PASSWORD not defined');

    config = {
      port: process.env.PORT!,
      db: {
        host: process.env.DB_HOST,
        database: process.env.DB_DATABASE,
        user: process.env.DB_USER,
        password: process.env.DB_PASSWORD
      }
    };
  }

  return config;
}
