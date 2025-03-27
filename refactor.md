# Steps for microservice refactor

1. Identify Microservices
Based on your codebase, you can split the application into the following microservices
These microservices will be in their own folder with their own main method to run the service as a spring boot app.

User Microservice: Handles user-related operations (e.g., authentication, user profiles).
Auction Microservice: Manages auctions (e.g., creating, updating, deleting auctions).
Bid Microservice: Handles bidding logic and bid-related operations.
Notification Microservice: Sends notifications to users.
JWT Microservice: Manages JWT authentication and token validation.
Auction Microservice: Handles auction logic

-> shared_services: for dto/exception misc logic/code

2. Refactor Dependencies
- Create a parent pom in root dir
-> all other pom's per project inherit the parent and add their own functionality


3. Enable http communication between services
- Use SpringCloud to get HTTP communication with an API Gateway with CloudConfig working (API Gateway and service discovery)