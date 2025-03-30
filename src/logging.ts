import winston from 'winston';

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

function defaultLoggerFactory(label: Label) {
  return winston.createLogger({
    level: 'debug',
    transports: [
      new winston.transports.Console({
        format: winston.format.combine(winston.format.colorize(), defaultAlignColorsAndTime(label))
      })
    ]
  });
}

function loggerFactory(label: Label) {
  return defaultLoggerFactory(label);
}

export default loggerFactory;
