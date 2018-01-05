
DO
$body$
BEGIN
    CREATE ROLE newnut WITH LOGIN PASSWORD 'newnut' SUPERUSER;
EXCEPTION WHEN others THEN
    RAISE NOTICE 'newnut role exists, not re-creating';
END
$body$;

DO
$body$
BEGIN
    CREATE DATABASE newnut;
EXCEPTION WHEN others THEN
    RAISE NOTICE 'newnut database exists, not re-creating';
END
$body$;

GRANT ALL PRIVILEGES ON DATABASE newnut TO newnut;