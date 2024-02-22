## 🛒 PostgreSQL Database

- [Introduction](#Introduction-PostgreSQL)
- [User Information Table](#User-Information-Table)
- [Orders Table](#Orders-Table)
- [Carts Table](#Carts-Table)
---

## Introduction PostgreSQL

The PostgreSQL database for our e-commerce application is meticulously crafted to ensure efficient data storage and retrieval, facilitating seamless operation of the backend system.

## User Information Table

The `user_info` table serves as a repository for storing user-related information, essential for authentication and personalized user experiences.

```sql
CREATE TABLE IF NOT EXISTS user_info (
  userID SERIAL PRIMARY KEY,
  userImage VARCHAR(255),
  userEmail VARCHAR(255) UNIQUE NOT NULL,
  userValidated BOOLEAN NOT NULL,
  userPass VARCHAR(255) NOT NULL,
  userSalt VARCHAR(255) NOT NULL
);
```

- `userID`: Unique identifier for each user.
- `userImage`: Path to the user's profile image.
- `userEmail`: Unique email address associated with the user's account.
- `userValidated`: Flag indicating whether the user's account has been validated.
- `userPass`: Hashed password of the user.
- `userSalt`: Salt value used for password hashing.

## Orders Table

The `orders` table facilitates the management of customer orders, capturing crucial details of each transaction.

```sql
CREATE TABLE IF NOT EXISTS orders (
  orderID SERIAL PRIMARY KEY,
  userID SERIAL REFERENCES user_info(userID),
  productList VARCHAR[] NOT NULL,
  totalPrice NUMERIC NOT NULL,
  orderDate VARCHAR(255) NOT NULL,
  order_state order_state NOT NULL
);
```

- `orderID`: Unique identifier for each order.
- `userID`: Foreign key referencing the `userID` in the `user_info` table.
- `productList`: Array storing the IDs of products included in the order.
- `totalPrice`: Total cost of the order.
- `orderDate`: Date and time when the order was placed.
- `order_state`: Enumerated type indicating the state of the order (`COMPLETED`, `ONGOING`, `CANCELLED`).

## Carts Table

The `carts` table facilitates temporary storage of items added to the user's shopping cart before checkout.

```sql
CREATE TABLE IF NOT EXISTS carts (
  cartID SERIAL PRIMARY KEY,
  userID SERIAL REFERENCES user_info(userID),
  productList VARCHAR[] NOT NULL
);
```

- `cartID`: Unique identifier for each cart entry.
- `userID`: Foreign key referencing the `userID` in the `user_info` table.
- `productList`: Array storing the IDs of products added to the cart.

The creation of sequences (`userID_seq`, `orderID_seq`, `cartID_seq`) ensures the automatic generation of unique identifiers for respective tables, ensuring data integrity and consistency.

This database schema forms the backbone of our e-commerce application, facilitating efficient user management, order processing, and cart functionality.