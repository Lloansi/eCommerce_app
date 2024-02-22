CREATE DATABASE ecommerceapp;
\c ecommerceapp
CREATE TABLE IF NOT EXISTS user_info (
  userID SERIAL PRIMARY KEY,
  userImage VARCHAR(255),
  userEmail VARCHAR(255) UNIQUE NOT NULL,
  userValidated BOOLEAN NOT NULL,
  userPass VARCHAR(255) NOT NULL,
  userSalt VARCHAR(255) NOT NULL
);

CREATE TYPE order_state AS ENUM ('COMPLETED', 'ONGOING', 'CANCELLED');

CREATE TABLE IF NOT EXISTS orders (
  orderID SERIAL PRIMARY KEY,
  userID SERIAL REFERENCES user_info(userID),
  productList VARCHAR[] NOT NULL,
  totalPrice NUMERIC NOT NULL,
  orderDate VARCHAR(255) NOT NULL,
  order_state order_state NOT NULL
);

CREATE TABLE IF NOT EXISTS carts (
  cartID SERIAL PRIMARY KEY,
  userID SERIAL REFERENCES user_info(userID),
  productList VARCHAR[] NOT NULL
);


CREATE SEQUENCE userID_seq
START 1
INCREMENT 1;

CREATE SEQUENCE orderID_seq
START 1
INCREMENT 1;

CREATE SEQUENCE cartID_seq
START 1
INCREMENT 1;