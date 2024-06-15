CREATE TABLE "users" (
    "id"          SERIAL PRIMARY KEY,
    "email"       varchar   NOT NULL,
    "password"    varchar,
    "created_at"  timestamp NOT NULL DEFAULT (now()),
    "created_by"  varchar,
    "modified_at" timestamp,
    "modified_by" varchar,
    "deleted_at"  timestamp,
    "deleted_by"  varchar
);

CREATE TABLE "profiles" (
    "id"              SERIAL PRIMARY KEY,
    "user_id"         int       NOT NULL,
    "profile_type_id" int       NOT NULL,
    "first_name"      varchar,
    "last_name"       varchar,
    "image_url"       varchar,
    "gender_id"       int,
    "phone"           varchar,
    "card_id"         int,
    "zip_code_id"     int,
    "dispensary_id"   int,
    "brand_id"        int,
    "enabled"         boolean,
    "verified"        boolean,
    "created_at"      timestamp NOT NULL DEFAULT (now()),
    "created_by"      varchar,
    "modified_at"     timestamp,
    "modified_by"     varchar,
    "deleted_at"      timestamp,
    "deleted_by"      varchar
);

CREATE TABLE "profile_types" (
    "id"      SERIAL PRIMARY KEY,
    "profile" varchar NOT NULL
);

CREATE TABLE "genders" (
    "id"     SERIAL PRIMARY KEY,
    "gender" varchar NOT NULL
);

CREATE TABLE "cards" (
    "id"         SERIAL PRIMARY KEY,
    "mmj_card"   boolean,
    "id_card"    boolean,
    "image_url"  varchar,
    "created_at" timestamp NOT NULL DEFAULT (now())
);

CREATE TABLE "states" (
    "id"         SERIAL PRIMARY KEY,
    "name"       varchar UNIQUE NOT NULL,
    "country_id" int            NOT NULL,
    "short_name" varchar        NOT NULL
);

CREATE TABLE "countries" (
    "id"         SERIAL PRIMARY KEY,
    "name"       varchar UNIQUE NOT NULL,
    "short_name" varchar        NOT NULL
);

CREATE TABLE "zip_codes" (
    "id"       SERIAL PRIMARY KEY,
    "code"     int UNIQUE NOT NULL,
    "city"     varchar    NOT NULL,
    "state_id" int        NOT NULL,
    "tax"      numeric    NOT NULL
);

CREATE TABLE "fav_user_products" (
    "id"         SERIAL PRIMARY KEY,
    "user_id"    int NOT NULL,
    "product_id" int NOT NULL
);

CREATE TABLE "fav_user_brands" (
    "id"       SERIAL PRIMARY KEY,
    "user_id"  int NOT NULL,
    "brand_id" int NOT NULL
);

CREATE TABLE "configs" (
    "id"    SERIAL PRIMARY KEY,
    "key"   varchar NOT NULL,
    "value" varchar NOT NULL
);

ALTER TABLE "profiles"
    ADD CONSTRAINT fk_profiles_users FOREIGN KEY ("user_id") REFERENCES "users" ("id");

ALTER TABLE "profiles"
    ADD CONSTRAINT fk_profiles_profile_types FOREIGN KEY ("profile_type_id") REFERENCES "profile_types" ("id");

ALTER TABLE "profiles"
    ADD CONSTRAINT fk_profiles_genders FOREIGN KEY ("gender_id") REFERENCES "genders" ("id");

ALTER TABLE "profiles"
    ADD CONSTRAINT fk_profiles_cards FOREIGN KEY ("card_id") REFERENCES "cards" ("id");

ALTER TABLE "profiles"
    ADD CONSTRAINT fk_profiles_zip_codes FOREIGN KEY ("zip_code_id") REFERENCES "zip_codes" ("id");

ALTER TABLE "states"
    ADD CONSTRAINT fk_states_countries FOREIGN KEY ("country_id") REFERENCES "countries" ("id");

ALTER TABLE "zip_codes"
    ADD CONSTRAINT fk_zip_codes_states FOREIGN KEY ("state_id") REFERENCES "states" ("id");

ALTER TABLE "fav_user_products"
    ADD CONSTRAINT fk_fav_user_products_users FOREIGN KEY ("user_id") REFERENCES "users" ("id");

ALTER TABLE "fav_user_brands"
    ADD CONSTRAINT fk_fav_user_brands_users FOREIGN KEY ("user_id") REFERENCES "users" ("id");

GRANT ALL PRIVILEGES ON DATABASE ${flyway:database} TO users_service;
GRANT SELECT, INSERT, UPDATE, DELETE ON ALL TABLES IN SCHEMA public TO users_service;
GRANT USAGE, SELECT ON ALL SEQUENCES IN SCHEMA public TO users_service;
