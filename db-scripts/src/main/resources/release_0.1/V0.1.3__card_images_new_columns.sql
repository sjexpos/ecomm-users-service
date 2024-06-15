ALTER TABLE card_images
    ADD UNIQUE (image_url);

ALTER TABLE card_images
    ADD COLUMN created_by varchar NULL,
    ADD COLUMN modified_at timestamp NULL,
    ADD COLUMN modified_by varchar NULL,
    ADD COLUMN deleted_at timestamp NULL,
    ADD COLUMN deleted_by varchar NULL;