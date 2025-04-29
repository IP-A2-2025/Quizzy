FROM postgres:17
COPY init.sql /docker-entrypoint-initdb.d/init.sql

HEALTHCHECK --interval=10s --timeout=5s --retries=5 \
  CMD pg_isready -U myuser -d mydb
