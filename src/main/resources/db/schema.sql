CREATE TABLE usuario (
  id UUID PRIMARY KEY,
  nombre VARCHAR(255) NOT NULL,
  correo VARCHAR(255) UNIQUE NOT NULL,
  contrasena VARCHAR(255) NOT NULL,
  created TIMESTAMP NOT NULL,
  modified TIMESTAMP,
  lastLogin TIMESTAMP NOT NULL,
  token UUID NOT NULL,
  isActive BOOLEAN NOT NULL
);

CREATE TABLE telefono (
  id UUID PRIMARY KEY,
  number VARCHAR(255) NOT NULL,
  cityCode VARCHAR(255) NOT NULL,
  countryCode VARCHAR(255) NOT NULL,
  usuarioid UUID,
  FOREIGN KEY (usuarioid) REFERENCES usuario(id)
);