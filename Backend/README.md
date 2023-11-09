## 🚩 Backend System

- [Introduction](#Introduction)
- [User Login and Registration](#User-Login-and-Registration)
- [Product Management](#Product-Management)
- [FastAPI Installation](#FastAPI-Installation)
- [Ktor Backend](#Ktor-Backend)
---

## Introduction

The backend system integrating the application smoothly employs two distinct technologies, strategically chosen based on the specific requirements of each microservice.

## User Login and Registration

For the meticulous management of user login and registration, we have harnessed the prowess of Ktor in conjunction with PostgreSQL.

Ktor, an adept framework, facilitates efficient development in Kotlin, characterized by its concise syntax. Concurrently, PostgreSQL stands as a stalwart relational database, imbued with ACID support, rendering it an exemplary choice for applications necessitating secure and consistent transactions. The amalgamation of Ktor and PostgreSQL bestows upon our project a robust and scalable developmental environment, especially tailored for web applications.

## Product Management

In the realm of handling products within the store, our preference gravitates towards the symbiosis of FastAPI and MongoDB.

FastAPI harmoniously coalesces with MongoDB, rendering it a judicious choice for product management within the application store. The expeditious development capabilities afforded by FastAPI in the Python ecosystem, coupled with its support for high-performance RESTful APIs, synergize seamlessly with the flexibility of MongoDB in handling semi-structured data. This dynamic duo facilitates agile adaptation to changes in product details, sans the constraints of rigid schemas. The collaborative integration of FastAPI and MongoDB begets an environment characterized by agility and scalability, propelling the dynamic management of product information within the store.

## FastAPI Installation

Here's how to use and install FastAPI for the backend of this app:

1. Install virtualenv if not already installed: `pip install virtualenv`
2. Create a virtual environment: `python3 -m venv myenv`
3. Activate the virtual environment: `source myenv/bin/activate` or select the interpreter with F1 -> Select Interpreter -> myenv
4. Install the libraries used listed in requirements.txt: `pip install -r requirements.txt`
5. Create a .env file and input the two variables with the database credentials:
   ```
   DB_USER='ecommerceitb'
   DB_PASS='ecommerceitb'
   ```
6. Start the API: `uvicorn main:app --reload`

## Ktor Backend

And for the backend using Ktor:

1. Download the project folder.
2. Open it with IntelliJ IDEA.
3. Run the Kotlin application.
