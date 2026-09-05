# Skill Exchange Marketplace

A REST API for a campus-based skill/service marketplace — students can list skills they offer (tutoring, coding help, music lessons, repairs, etc.) and other students can request them. Built as a backend-focused portfolio project demonstrating REST API design, JWT authentication, relational data modeling, and business logic validation.

## Overview

Think of it as a mini Fiverr/TaskRabbit scoped to a college campus. The core flow:
1. A user creates a **listing** for a skill they offer
2. Another user sends a **request** for that listing
3. The listing owner **accepts** or **rejects** the request
4. Once accepted, the work happens, and the owner marks it **completed**
5. The requester leaves a **review** with a 1–5 rating
6. Anyone can check a user's **average rating** based on their completed work

## Tech Stack

- **Java 17+**
- **Spring Boot** (REST API)
- **Spring Data JPA** (Hibernate ORM)
- **Spring Security** with **JWT** authentication
- **MySQL 8**
- **Maven**
- **Postman** (API testing)

## Database Schema

**Users**
| Column | Type | Notes |
|---|---|---|
| id | Integer | Primary key |
| name | String | Required |
| email | String | Required, unique |
| password | String | Required, hashed (BCrypt) |
| branch | String | |
| year | Integer | |
| active | Boolean | Soft-delete flag |

**Listings**
| Column | Type | Notes |
|---|---|---|
| id | Integer | Primary key |
| user_id | FK → Users | Listing owner |
| title | String | Required |
| description | Text | |
| category | String | |
| price_or_free | String | |

**Requests**
| Column | Type | Notes |
|---|---|---|
| id | Integer | Primary key |
| listing_id | FK → Listings | |
| requester_id | FK → Users | |
| status | Enum | PENDING, ACCEPTED, REJECTED, COMPLETED |
| created_at | Timestamp | |

**Reviews**
| Column | Type | Notes |
|---|---|---|
| id | Integer | Primary key |
| request_id | FK → Requests | One review per request |
| rating | Integer | 1–5 |
| comment | Text | |
| created_at | Timestamp | |

## Business Rules

1. A user cannot request their own listing
2. Request status flow: `PENDING → ACCEPTED or REJECTED → (if ACCEPTED) → COMPLETED`
3. A request can only move to `COMPLETED` if it was previously `ACCEPTED`
4. A review can only be submitted after a request is `COMPLETED`
5. Rating must be between 1 and 5
6. A user's average rating = average of ratings from reviews on requests where they were the **listing owner**
7. Deleting a user performs a **soft delete** (marks them inactive) rather than removing the row — preserves data integrity for related listings/requests/reviews
8. Passwords are hashed with BCrypt and never exposed in API responses

## API Endpoints

All endpoints except `/users/register` and `/users/login` require a JWT Bearer token in the `Authorization` header, obtained from `/users/login`.

| Method | Endpoint | Description | Auth Required |
|---|---|---|---|
| POST | `/users/register` | Register a new user | No |
| POST | `/users/login` | Login, returns JWT token | No |
| DELETE | `/users/{id}` | Soft-delete a user | Yes |
| GET | `/users/{id}/rating` | Get a user's average rating | No |
| POST | `/listings` | Create a new listing | Yes |
| GET | `/listings` | Get all listings | No |
| GET | `/listings?category={category}` | Filter listings by category | No |
| POST | `/requests` | Create a request for a listing | Yes |
| PUT | `/requests/{id}/accept` | Accept a pending request | Yes |
| PUT | `/requests/{id}/reject` | Reject a pending request | Yes |
| PUT | `/requests/{id}/complete` | Mark an accepted request as completed | Yes |
| POST | `/reviews` | Submit a review for a completed request | Yes |

### Example — Register
```http
POST /users/register
Content-Type: application/json

{
  "name": "Siva",
  "email": "siva@gmail.com",
  "password": "password123",
  "branch": "CSE",
  "year": 3
}
```

### Example — Login
```http
POST /users/login
Content-Type: application/json

{
  "email": "siva@gmail.com",
  "password": "password123"
}
```
Response:
```json
{
  "token": "eyJhbGciOiJIUzUxMiJ9..."
}
```

### Example — Create Listing
```http
POST /listings
Authorization: Bearer <token>
Content-Type: application/json

{
  "userId": 1,
  "title": "Guitar Lessons for Beginners",
  "description": "Weekly 1-hour sessions covering chords and basic songs",
  "category": "music",
  "priceOrFree": "Free"
}
```

## Setup Instructions

### Prerequisites
- Java 17+
- Maven
- MySQL 8

### Steps

1. Clone the repository
```bash
   git clone https://github.com/sivaprakasan85/skill-exchange-marketplace.git
   cd skill-exchange-marketplace
```

2. Create the MySQL database
```sql
   CREATE DATABASE skillexchange_db;
```

3. Configure your database credentials
    - Copy `src/main/resources/application.properties.example` to `src/main/resources/application.properties`
    - Fill in your own MySQL username and password

4. Run the application
```bash
   mvn spring-boot:run
```
The API will start on `http://localhost:8080`

5. Import the Postman collection
    - Open Postman
    - Import `postman/SkillExchange.postman_collection.json`
    - Use the saved requests to test each endpoint

## Test Results

All 22 planned test cases were manually verified via Postman:

| TC ID | Module | Description | Result |
|---|---|---|---|
| TC01 | User Registration | Register with valid details | ✅ Pass |
| TC02 | User Registration | Register with duplicate email | ✅ Pass |
| TC03 | User Registration | Register with missing required field | ✅ Pass |
| TC04 | Listings | Create a new skill listing | ✅ Pass |
| TC05 | Listings | Create listing with invalid user_id | ✅ Pass |
| TC06 | Listings | Fetch all listings | ✅ Pass |
| TC07 | Listings | Filter listings by category | ✅ Pass |
| TC08 | Listings | Filter by non-existent category | ✅ Pass |
| TC09 | Requests | Create a request for a listing | ✅ Pass |
| TC10 | Requests | User tries to request their own listing | ✅ Pass |
| TC11 | Requests | Accept a pending request | ✅ Pass |
| TC12 | Requests | Accept an already-accepted request | ✅ Pass |
| TC13 | Requests | Reject a pending request | ✅ Pass |
| TC14 | Requests | Mark an accepted request as completed | ✅ Pass |
| TC15 | Requests | Try completing a request that's still PENDING | ✅ Pass |
| TC16 | Reviews | Submit review after request completion | ✅ Pass |
| TC17 | Reviews | Submit review before completion | ✅ Pass |
| TC18 | Reviews | Submit rating outside valid range | ✅ Pass |
| TC19 | Ratings | Get average rating for a user | ✅ Pass |
| TC20 | Ratings | Get rating for user with zero reviews | ✅ Pass |
| TC21 | Security | Access protected endpoint without login/token | ✅ Pass |
| TC22 | Database Integrity | Delete a user who has active listings | ✅ Pass (soft delete) |

## Security Notes

- Passwords are hashed using BCrypt before storage
- JWT tokens expire after 24 hours
- Protected endpoints require a valid Bearer token
- User deletion is a soft delete (sets `active = false`) to preserve referential integrity with listings, requests, and reviews
- Password field is write-only in JSON — accepted on input, never exposed in API responses

## Author

**Siva Prakasan** — 3rd Year CSE Student
GitHub: [@sivaprakasan85](https://github.com/sivaprakasan85)

## License

This project is licensed under the MIT License — see the [LICENSE](LICENSE) file for details.