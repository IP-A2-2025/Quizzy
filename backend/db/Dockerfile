# Use the official PostgreSQL 17 image
FROM postgres:17

# Set environment variables for PostgreSQL
ENV POSTGRES_USER=myuser
ENV POSTGRES_PASSWORD=mypass
ENV POSTGRES_DB=mydb

# Copy the SQL initialization script to the appropriate directory for PostgreSQL
COPY init.sql /docker-entrypoint-initdb.d/init.sql

# Healthcheck configuration to verify if PostgreSQL is ready
HEALTHCHECK --interval=10s --timeout=5s --retries=5 \
  CMD pg_isready -U $POSTGRES_USER -d $POSTGRES_DB -e POSTGRES_USER=$POSTGRES_USER \
            -e POSTGRES_PASSWORD=$POSTGRES_PASSWORD \
            -e POSTGRES_DB=$POSTGRES_DB
