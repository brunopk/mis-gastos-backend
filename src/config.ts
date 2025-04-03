import dotenv from 'dotenv-flow';

type Config = {
  port: string;
  enableCORS: boolean;
  logLevel: string;
  db: {
    host: string;
    database: string;
    user: string;
    password: string;
  };
};

let config: Config | null = null;

export default function getConfig() {
  if (!config) {
    dotenv.config();

    if (!process.env.PORT) throw new Error('PORT not defined');
    if (!process.env.LOG_LEVEL) throw new Error('LOG_LEVEL not defined');
    if (!process.env.DB_HOST) throw new Error('DB_HOST not defined');
    if (!process.env.DB_DATABASE) throw new Error('DB_DATABASE not defined');
    if (!process.env.DB_USER) throw new Error('DB_USER not defined');
    if (!process.env.DB_PASSWORD) throw new Error('DB_PASSWORD not defined');

    config = {
      port: process.env.PORT!,
      logLevel: process.env.LOG_LEVEL!,
      enableCORS: process.env.NODE_ENV !== 'production',
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
