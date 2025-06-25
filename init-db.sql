

CREATE USER "wevioo" with password 'Wevioo@2024++';
CREATE DATABASE "pfe_db";
GRANT ALL PRIVILEGES ON DATABASE "pfe_db" to "wevioo";
GRANT ALL ON SCHEMA public TO wevioo;
GRANT CREATE ON SCHEMA public TO wevioo;