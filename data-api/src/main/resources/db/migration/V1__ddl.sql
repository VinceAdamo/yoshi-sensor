CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE TABLE devices
(
     id         UUID DEFAULT uuid_generate_v4() NOT NULL,
     serial_number      VARCHAR(255) NOT NULL,
     name TEXT,
     PRIMARY KEY (id)
);

CREATE TABLE temperature
  (
     id         UUID DEFAULT uuid_generate_v4() NOT NULL,
     value      FLOAT NOT NULL,
     device_id UUID NOT NULL,
     timestamp TIMESTAMPTZ NOT NULL,
     PRIMARY KEY (id),
     CONSTRAINT fk_temperature_to_devices
      FOREIGN KEY(device_id) 
	  REFERENCES devices(id)
  );

CREATE TABLE humidity
  (
     id         UUID DEFAULT uuid_generate_v4() NOT NULL,
     value      FLOAT NOT NULL,
     device_id UUID NOT NULL,
     timestamp TIMESTAMPTZ NOT NULL,
     PRIMARY KEY (id),
     CONSTRAINT fk_temperature_to_devices
      FOREIGN KEY(device_id) 
	  REFERENCES devices(id)
  );