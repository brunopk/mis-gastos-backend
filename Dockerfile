ARG BUILD_FROM
FROM $BUILD_FROM

ARG DB_USER=your_user
ARG DB_PASS=your_password
ARG DB_URL=jdbc:mariadb://localhost:3306/db_name

# Install requirements for add-on
RUN apk add --no-cache openjdk21-jre

WORKDIR /app

# Copy data for add-on
COPY run.sh .
COPY mis-gastos-backend.jar .
RUN chmod a+x run.sh

ENV SPRING_PROFILES_ACTIVE=prod
ENV DB_URL=${DB_URL}
ENV DB_USER=${DB_USER}
ENV DB_PASS=${DB_PASS}

CMD [ "./run.sh" ]