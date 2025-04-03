import morgan from 'morgan';
import winston, { Logger } from 'winston';
import getConfig from './config';

type Label = 'express' | null;

function defaultAlignColorsAndTime(label: Label) {
  return winston.format.combine(
    winston.format.label({
      label: `[${label}]`
    }),
    winston.format.timestamp({
      format: 'YYYY-MM-DD HH:mm:ssZ'
    }),
    winston.format.printf(
      (info) =>
        `${info.label} [${info.timestamp}] [${info.level}] ${info.message} ${info.stack ? '\n' + info.stack : ''}`
    )
  );
}

function defaultLoggerFactory(logLevel: string, label: Label) {
  return winston.createLogger({
    level: logLevel,
    transports: [
      new winston.transports.Console({
        format: winston.format.combine(winston.format.colorize(), defaultAlignColorsAndTime(label))
      })
    ]
  });
}

export function loggerFactory(label: Label) {
  const config = getConfig();
  return defaultLoggerFactory(config.logLevel, label);
}

export function morganMiddleware(logger: Logger) {
  return morgan(logger.level == 'debug' ? 'combined' : 'short', {
    stream: {
      write: (message) => {
        const statusMatch = message.match(/\s(\d{3})\s/);
        const status = statusMatch ? parseInt(statusMatch[1], 10) : 200;

        // Determine log level based on status code
        const logLevel: 'debug' | 'info' | 'warn' | 'error' =
          status >= 500
            ? 'error'
            : status >= 400
              ? 'warn'
              : status >= 300
                ? 'warn'
                : status >= 200
                  ? 'info'
                  : 'info';

        logger.log(logLevel, message.trim());
      }
    }
  });
}
