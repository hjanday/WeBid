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
  INSERT INTO user_roles (user_id, roles) VALUES ((SELECT id FROM users WHERE username='johndoe'), 'ROLE_USER');

  INSERT INTO users (username, email, password, first_name, last_name, address, postal_code, country, city)
  VALUES ('janedoe', 'jane.doe@example.com', '$2a$10$3T6Fjfd94wugk9K20JKgp.bCStffyb9rcT4D0HWEXgyE.sQ3MDSwy', 'Jane', 'Doe', '789 Pine St', '54321', 'USA', 'Gotham');
  INSERT INTO user_roles (user_id, roles) VALUES ((SELECT id FROM users WHERE username='janedoe'), 'ROLE_USER');

  INSERT INTO users (username, email, password, first_name, last_name, address, postal_code, country, city)
  VALUES ('bobsmith', 'bob.smith@example.com', '$2a$10$.iM97js19E6Ev/HksqTYl.BeXFIpVy7wQ6G3UrWo.7yh5YnmqdLim', 'Bob', 'Smith', '123 Maple Rd', '11111', 'USA', 'Star City');
  INSERT INTO user_roles (user_id, roles) VALUES ((SELECT id FROM users WHERE username='bobsmith'), 'ROLE_USER');

  INSERT INTO users (username, email, password, first_name, last_name, address, postal_code, country, city)
  VALUES ('bobsmith1', 'bob.smith1@example.com', '$2a$10$n93VrOut5nA3nP4kNYNsNeXoDv4F.ni9mNqo5DvatY25Jtm840oi.', 'Bob', 'Smith', '125 Maple Rd', '11221', 'USA', 'Star City');
  INSERT INTO user_roles (user_id, roles) VALUES ((SELECT id FROM users WHERE username='bobsmith1'), 'ROLE_USER');


  -- Insert 2 FORWARD auctions
  INSERT INTO auctions (owner_id, item_name, description, lowest_bid, bid_increment, auction_type, expedited_shipping_cost, current_bid, expedited_shipping, over, start_time, end_time, current_bidderid)
  VALUES 
    (1, 'Vintage Watch', 'A watch!', 100.0, 50, 'FORWARD', 15.0, 500, false, false, NOW(), NOW() + interval '1 day', 2),
    (2, 'Antique Clock', 'An old clock.', 120.0, 40, 'FORWARD', 20.0, 450, false, false, NOW(), NOW() + interval '1 day', 3);

  -- Insert 2 DUTCH auctions
  INSERT INTO auctions (owner_id, item_name, description, lowest_bid, bid_increment, auction_type, expedited_shipping_cost, current_bid, expedited_shipping, over, start_time, end_time, current_bidderid)
  VALUES 
    (3, 'Modern Smartphone', 'Phone!', 200.0, 25, 'DUTCH', 10.0, 250, false, false, NOW(), NOW() + interval '1 day', 4),
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

# Curl Commands

All Curl Commands will have an example so the TA can Copy and Paste into their terminal. Sections in bold indicate the JWT authentication token which will be returned and will need to be used in certain places. Make sure to input the JWT token into the "Bearer" header. You get the JWT token in the response body after logging in.

> Note: These commands are for Windows systems but also run fine on MacOS

## UC 1.1 Sign-up
```bash
curl -X POST "http://localhost:8080/auth/register" -H "Content-Type: application/json" -d "{\"username\":\"someone\",\"email\":\"someone@example.com\",\"password\":\"newSecretPass\",\"firstName\":\"someone\",\"lastName\":\"somebody\",\"address\":\"456 Oak Ave\",\"postalCode\":\"67890\",\"country\":\"USA\",\"city\":\"Metropolis\"}"
```

## UC 1.2 Sign-in
> Note: Copy the JWT token that is returned in the body
```bash
curl -X POST "http://localhost:8080/auth/login" -H "Content-Type: application/json" -d "{\"email\":\"someone@example.com\",\"password\":\"newSecretPass\"}"
```

## UC 2.1 Item Search
```bash
curl -X GET "http://localhost:8080/api/auctions/search?itemName=Vintage" -H "Authorization: Bearer <JWT_TOKEN>"
```

## UC 2.2 Display Auctioned Items
Same as 2.1 as the display is showing the output of 2.1

## UC 2.3 Item Selection
```bash
curl -X GET "http://localhost:8080/api/auctions/4" -H "Authorization: Bearer <JWT_TOKEN>"
```

## UC 3.1 Forward Bidding
```bash
curl -X POST "http://localhost:8080/api/bid/1?bidAmount=600" -H "Authorization: Bearer <JWT_TOKEN>"
```

## UC 3.2 Dutch Buying
```bash
curl -X PUT "http://localhost:8080/api/auctions/complete/3" -H "Authorization: Bearer <JWT_TOKEN>"
```

## UC 4 + 5 Payment (Auction Over)
> Note: For auctionId 1 (forward auction), requires values: over = true and endTime to be set before current time
> Edit in pgAdmin4: "Select * from auctions;" and update accordingly
```bash
curl -X POST "http://localhost:8080/api/payments/1/5/pay" -H "Authorization: Bearer <JWT_TOKEN>"
```

## UC 6 Receipt & Shipping
### Set shipping expedition
Note: this api call requires the user to be the current winner/highest bidder of the auction to work
```bash
curl -X PUT "http://localhost:8080/api/auctions/4?expShip=true" -H "Authorization: Bearer <JWT_TOKEN>"
```


### Get payment information (receipt)
```bash
curl -X GET "http://localhost:8080/api/payments/1" -H "Authorization: Bearer <JWT_TOKEN>"
```

## UC 7.1 Create Auction - Forward
```bash
curl -X POST "http://localhost:8080/api/auctions/create" -H "Content-Type: application/json" -H "Authorization: Bearer <JWT_TOKEN>" -d "{\"itemName\":\"Vintage Screen\",\"description\":\"A rare screen\",\"lowestBid\":100.0,\"bidIncrement\":5.0,\"auctionType\":\"FORWARD\",\"expeditedShippingCost\":15.0,\"expeditedShipping\":true}"
```

## UC 7.2 Create Auction - Dutch
```bash
curl -X POST "http://localhost:8080/api/auctions/create" -H "Content-Type: application/json" -H "Authorization: Bearer <JWT_TOKEN>" -d "{\"itemName\":\"Tonka Truck\",\"description\":\"Tonka.\",\"lowestBid\":100.0,\"bidIncrement\":50,\"auctionType\":\"DUTCH\",\"expeditedShippingCost\":15.0,\"currentBid\":500,\"expeditedShipping\":true}"
```

## UC 8 Decrement Dutch
> Note: When creating a dutch auction, user sets decrement amount
> This command decrements by that amount
> The number refers to the auction id, and requires the user to be the owner of the dutch auction.
```bash
curl -X PUT "http://localhost:8080/api/auctions/dutch/6" -H "Authorization: Bearer <JWT_TOKEN>"
```

### Get User Notifications
```bash
curl -X GET "http://localhost:8080/api/notification" -H "Authorization: Bearer <JWT_TOKEN>"
```

### Notify Completed Auction
```bash
curl -X POST "http://localhost:8080/api/notification/completed/1" -H "Authorization: Bearer <JWT_TOKEN>"
```

### Create a payment for an auction
```bash
> the first value is the auction id and the 2nd value is the days needed to ship
curl -X POST "http://localhost:8080/api/payment/1/10/pay" -H "Authorization: Bearer <JWT_TOKEN>"
```


## Important Notes
1. Replace `<JWT_TOKEN>` with the actual JWT token received after login
2. All endpoints except `/auth/register` and `/auth/login` require authentication
3. Some endpoints may require specific roles (admin, user)
4. Dates should be in ISO 8601 format
5. Prices should be numeric values 

