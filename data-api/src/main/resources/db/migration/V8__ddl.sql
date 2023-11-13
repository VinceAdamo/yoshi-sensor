ALTER TABLE devices DROP COLUMN user_id;

CREATE TABLE user_devices
  (
     id         UUID DEFAULT uuid_generate_v4() NOT NULL,
     device_id UUID NOT NULL,
     user_id UUID NOT NULL,
     PRIMARY KEY (id),
     CONSTRAINT fk_user_devices_to_devices
      FOREIGN KEY(device_id) 
	  REFERENCES devices(id),
     CONSTRAINT fk_user_devices_to_users
      FOREIGN KEY(user_id) 
	  REFERENCES users(id)
  );