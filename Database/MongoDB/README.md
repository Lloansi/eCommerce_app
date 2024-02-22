## 🛒 MongoDB Database

- [Introduction](#Introduction-MongoDB)
- [User Information Table](#Product-Collection)

---

## Introduction MongoDB

MongoDB's document-based model allows for flexible schema design. In an e-commerce context, where product specifications and attributes can vary widely, MongoDB's ability to handle semi-structured data is advantageous. This flexibility accommodates changes in product attributes and specifications without the need for schema alterations, offering agility in product management.

## Product Collection

The `product` collection stores all the data of our shop products.

`id`: String - Unique identifier for the product.

`name`: String - Name of the product.

`image`: String - Filename or path to the product image.

`category`: String - Category to which the product belongs.

`price`: Float - Price of the product.

`location`: String - Location where the product is available or stored.


`specs`: Object - Specifications of the product:
  - `cpu`: String - CPU specifications.
  - `ram`: String - RAM specifications.
  - `storage`: String - Storage specifications.
  - `ddos_protect`: Boolean - Indicates whether DDoS protection is enabled for the product.
  
`stock`: Integer - Quantity of the product available in stock
  
`times_bought`: Integer - Number of times the product has been purchased.


This structure delineates the various attributes and nested objects comprising the document representing a product in the MongoDB database.