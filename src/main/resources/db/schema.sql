CREATE TABLE users (
  id UUID PRIMARY KEY,
  full_name VARCHAR(255) NOT NULL,
  email VARCHAR(255) UNIQUE NOT NULL,
  password VARCHAR(255) NOT NULL,
  created TIMESTAMP NOT NULL,
  modified TIMESTAMP,
  last_login TIMESTAMP NOT NULL,
  token UUID NOT NULL,
  is_active BOOLEAN NOT NULL
);

CREATE TABLE phones (
  id UUID PRIMARY KEY,
  number VARCHAR(255) NOT NULL,
  city_code VARCHAR(255) NOT NULL,
  country_code VARCHAR(255) NOT NULL,
  user_id UUID, FOREIGN KEY (user_id) REFERENCES users(id)
);