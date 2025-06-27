

CREATE USER "wevioo" with password 'Wevioo@2024++';
CREATE DATABASE "pfe_db";
GRANT ALL PRIVILEGES ON DATABASE "pfe_db" to "wevioo";

GRANT ALL ON SCHEMA public TO wevioo;
GRANT CREATE ON SCHEMA public TO wevioo;



CREATE USER debezium WITH REPLICATION PASSWORD 'debezium';
\c pfe_db
GRANT SELECT ON public.fds004t_transaction TO debezium;
CREATE PUBLICATION debezium_publication FOR TABLE public.fds004t_transaction;

