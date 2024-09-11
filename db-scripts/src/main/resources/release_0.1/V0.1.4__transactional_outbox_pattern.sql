
CREATE TABLE "transactional_outbox" (
  "id" SERIAL PRIMARY KEY,
  "aggregate" varchar(10) NOT NULL,
  "message" varchar NOT NULL,
  "is_delivered" boolean NOT NULL DEFAULT 'false',
  "created_date" timestamp NOT NULL,
  "last_modified_date" timestamp NOT NULL
);

GRANT SELECT, INSERT, UPDATE, DELETE ON ALL TABLES IN SCHEMA public TO users_service;
GRANT USAGE, SELECT ON ALL SEQUENCES IN SCHEMA public TO users_service;
