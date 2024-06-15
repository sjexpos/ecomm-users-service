
GRANT ALL PRIVILEGES ON DATABASE ${tests.db.database} TO ${tests.db.username};
GRANT SELECT, INSERT, UPDATE, DELETE ON ALL TABLES IN SCHEMA public TO ${tests.db.username};

GRANT ALL ON ALL SEQUENCES IN SCHEMA public TO ${tests.db.username};
