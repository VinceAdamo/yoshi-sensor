CREATE TABLE users
(
     id         UUID DEFAULT uuid_generate_v4() NOT NULL,
     email      VARCHAR(255) NOT NULL,
     password_hash   VARCHAR(255) NOT NULL,
     PRIMARY KEY (id)
);

ALTER TABLE devices
  ADD user_id UUID CONSTRAINT fk_device_to_user REFERENCES users(id);