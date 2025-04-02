# WeBid

WeBid is an online eCommerce auction application that allows users to register and create both forward and dutch auctions.

## 1. What you need
- Spring Boot
- PostgreSQL
- Docker
- Maven (3.9.9+)
- Java (23+)

## 2. PostgreSQL Installation and Setup
You can skip this step and go to step 4 if you have or want to use docker.

You need to have the latest version of PostgreSQL installed on your machine in order to run this application.
[Download](https://www.postgresql.org/download/) the latest version of PostgreSQL.

After downloading and installing, run pgAdmin4. You will be prompted to set a master password for the root user "postgres". When connecting to the server on postgres, use the same password.
Create a database with the name "webid" in pgAdmin4. 

## 3. Running the application
Go to step 4 and follow the steps there if you already have docker installed (or wish to just run off docker) as downloading postgresql won't be necessary. 
Before running the application, add your postgres username and password (username should be "postgres" and the password should be the same from earlier) into a ```.env``` file.
Run maven clean and install.
Then run the application. If your database is properly connected then your database should be populated with all of the tables.
Go back to pgAdmin4, right click on the table and click on "Query tool". Copy and paste this SQL script below to create some auctions and users (there is an sql file you can download with the script as well). 

```
-- Insert sample users + plus admin into the users table
INSERT INTO users (username, password, email, first_name, last_name) VALUES ('admin', '$2a$10$BL1ZjjzDB/x.0p40ccExSupSPkoaVCC7Jzj8UuscREN0z4HOzGU9i', 'admin@gmail.com', 'admin', 'admin');
INSERT INTO user_roles (user_id, roles) VALUES ((SELECT id FROM users WHERE username='admin'), 'ROLE_ADMIN');

INSERT INTO users (username, email, password, first_name, last_name, address, postal_code, country, city)
VALUES ('johndoe', 'john.doe@example.com', '$2a$10$wEuiH0Do4Rk//wTXW8m7iuCnDPBzD0ACR5bludbF7i5H0I0PL6DYW', 'John', 'Doe', '456 Oak Ave', '67890', 'USA', 'Metropolis');

INSERT INTO users (username, email, password, first_name, last_name, address, postal_code, country, city)
VALUES ('janedoe', 'jane.doe@example.com', '$2a$10$3T6Fjfd94wugk9K20JKgp.bCStffyb9rcT4D0HWEXgyE.sQ3MDSwy', 'Jane', 'Doe', '789 Pine St', '54321', 'USA', 'Gotham');

INSERT INTO users (username, email, password, first_name, last_name, address, postal_code, country, city)
VALUES ('bobsmith', 'bob.smith@example.com', '$2a$10$.iM97js19E6Ev/HksqTYl.BeXFIpVy7wQ6G3UrWo.7yh5YnmqdLim', 'Bob', 'Smith', '123 Maple Rd', '11111', 'USA', 'Star City');

INSERT INTO users (username, email, password, first_name, last_name, address, postal_code, country, city)
VALUES ('bobsmith1', 'bob.smith1@example.com', '$2a$10$n93VrOut5nA3nP4kNYNsNeXoDv4F.ni9mNqo5DvatY25Jtm840oi.', 'Bob', 'Smith', '125 Maple Rd', '11221', 'USA', 'Star City');

-- Insert 2 FORWARD auctions
INSERT INTO auctions (owner_id, item_name, description, lowest_bid, bid_increment, auction_type, expedited_shipping_cost, current_bid, expedited_shipping, over, start_time, end_time, current_bidderid)
VALUES 
  (1, 'Vintage Watch', 'A watch!', 100.0, 50, 'FORWARD', 15.0, 500, false, false, NOW(), NOW() + interval '1 day', 0),
  (2, 'Antique Clock', 'An old clock.', 120.0, 40, 'FORWARD', 20.0, 450, false, false, NOW(), NOW() + interval '1 day', 0);

-- Insert 2 DUTCH auctions
INSERT INTO auctions (owner_id, item_name, description, lowest_bid, bid_increment, auction_type, expedited_shipping_cost, current_bid, expedited_shipping, over, start_time, end_time, current_bidderid)
VALUES 
  (3, 'Modern Smartphone', 'Phone!', 200.0, 25, 'DUTCH', 10.0, 250, false, false, NOW(), NOW() + interval '1 day', 0),
  (4, 'Luxury Car', 'Fast Car!', 5000.0, 500, 'DUTCH', 50.0, 5500, false, false, NOW(), NOW() + interval '1 day', 5);

```

Log-in information for Users:
  | Email  | Password |
  | ------------- | ------------- |
  | admin@gmail.com  | admin123  |
  | john.doe@example.com  | newSecretPass  |
  | jane.doe@example.com  | securePass123  |
  | bob.smith@example.com  | passWord321  |
  | bob.smith1@example.com  | passWord3211  |

## 3. Docker
Make sure you have docker desktop installed ([Download](https://docs.docker.com/desktop/)). 
After installing docker run the following commands below to setup the docker images and containers.

Build image:
```docker build -t webid-app .```

Start containers:
```docker-compose up -d```

When you see the containers are running, you can then access your [Localhost](http://localhost:8080/).

If you want to check the webid application console:
```docker logs -f webid-app-1```

To access the postgresql terminal:
```docker exec -it webid-db-1 psql -U control -d webid```
Then you can insert the sql script from step 3 to populate the database.

When you are done you can run:
```docker-compose down```

## 4. Running the Commands
We implemented JWT authentication in this project so when you run the commands you must pass in your current JWT token into the ```Bearer``` header.
You can get this token from the response body after successfully logging in. Copy and paste it into the header for each command.
Admin password is "admin123".

