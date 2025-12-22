### AlgoTracker – DSA Practice & Progress Tracker (Spring Boot + JWT + MySQL)

AlgoTracker is a secure DSA practice & progress tracker built with **Spring Boot**, **Spring Security (JWT)**, and **MySQL**.
Users can register, log in, and manage their own DSA problems.
Each user only sees their own data, and under the hood the project demonstrates custom data structure
implementations (ArrayList, Stack, Queue, LinkedList) in real APIs.

# 📌 Core Features

1 Custom implementation of core Data Structures (e.g., LinkedList)

2 REST APIs to perform and track operations

3 Layered architecture (Controller → Service → Data Structure)

4 Clean separation of concerns

5 Maven-based Spring Boot project

6 Ready for future enhancements like dashboards & documentation


# 🛠️ Tech Stack

Java
Spring Boot
Spring Web (REST APIs)
Maven
Git & GitHub

# 📂 Project Structure
algotracker
│
├── src/main/java/com/anuj/algotracker
│   ├── controller        # REST controllers
│   ├── datastructure     # Custom data structure implementations
│   ├── dto               # data transfer objects
│   ├── exception         # Global and custom exception handling 
│   ├── model             # Entity - contains JPA entity classes representing DB tables  
│   ├── repository        # Data access layer - contains JPA repository responsible for db operation
│   ├── security          # Application security configuration
│   ├── service           # Business logic
│   └── AlgotrackerApplication.java   # Application entry point
│
├── src/main/resources
│
├── pom.xml               # Maven configuration
├── mvnw / mvnw.cmd       # Maven wrapper
├── .gitignore
└── README.md

# How to Run the Project
1. Clone the Repository
   git clone https://github.com/Anuj-Kumar-1952/algotracker.git
2. Navigate to the project directory
   cd algotracker
3. Run the Application 
   ./mvnw spring-boot:run

## ▶️ Quick Demo Flow

1. Register a user – `/api/auth/register`
2. Login – `/api/auth/login` → get JWT
3. Authorize in Swagger using JWT
4. Create problems – `/api/problems`
5. Explore:
   - Recommendations
   - Practice queue
   - Recently solved problems
   - Dashboard summary

# 🌐 API Usage
Once the application starts, the server runs on:
  http://localhost:8080/
  Use Postman or any REST client to interact with the APIs.
  
## 📸 API Testing (Postman)
Below is an example of testing a secured dashboard API using JWT authentication:
![Dashboard API Postman Screenshot](screenshots/postman-dashboard-api.png)

# API Documentation (Swagger / OpenAPI)

This project uses **Swagger (springdoc-openapi)** to provide interactive API documentation.

### 🔗 Swagger UI
Once the application is running, access Swagger at:

http://localhost:8080/swagger-ui/index.html

**$ % 🔐 JWT Authentication in Swagger % $**
1. Call **POST /api/auth/login**
2. Copy the JWT token from the response
3. Click the 🔒 **Authorize** button in Swagger
4. Paste token as: Bearer <your_token>
5. Now you can access all secured APIs directly from Swagger UI
  
# **$ Project Progress $**
### ✅ Phase 1 – Core Setup
- Spring Boot project setup
- MySQL integration
- Problem CRUD APIs

### ✅ Phase 2 – Security
- User registration & login
- JWT-based authentication
- Spring Security configuration

### ✅ Phase 3 – Multi-user Support
- User-specific data isolation
- Secure access to problems

### ✅ Phase 4 – Custom Data Structures
- Custom MyArrayList for recommendations
- Custom Stack for reversed history
- Custom Circular Queue for practice flow
- Custom LinkedList for recently solved problems

### ✅ Phase 5 – Advanced & Polish
- Dashboard summary API
- Input validation using Jakarta Validation
- Global exception handling
- Swagger / OpenAPI documentation with JWT support