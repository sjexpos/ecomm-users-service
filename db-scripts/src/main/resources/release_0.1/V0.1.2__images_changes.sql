-- CHANGES IN cards TABLE
ALTER TABLE cards DROP COLUMN image_url;
ALTER TABLE cards
    ADD COLUMN card_image_id int NULL;

CREATE TABLE "card_images" (
    "id"      SERIAL PRIMARY KEY,
    "image_url" varchar UNIQUE NOT NULL,
    "status" varchar NOT NULL,
    "created_at" timestamp NOT NULL DEFAULT (now())
);

ALTER TABLE cards
    ADD CONSTRAINT fk_card_images FOREIGN KEY ("card_image_id") REFERENCES "card_images" ("id");

-- CHANGES IN profiles TABLE
ALTER TABLE profiles DROP COLUMN image_url;
ALTER TABLE profiles
    ADD COLUMN profile_image_id int NULL;

CREATE TABLE "profile_images" (
    "id"      SERIAL PRIMARY KEY,
    "image_url" varchar UNIQUE NOT NULL,
    "status" varchar NOT NULL,
    "created_at" timestamp NOT NULL DEFAULT (now())
);

ALTER TABLE profiles
    ADD CONSTRAINT fk_profile_images FOREIGN KEY ("profile_image_id") REFERENCES "profile_images" ("id");

GRANT SELECT, INSERT, UPDATE, DELETE ON ALL TABLES IN SCHEMA public TO users_service;
GRANT USAGE, SELECT ON ALL SEQUENCES IN SCHEMA public TO users_service;