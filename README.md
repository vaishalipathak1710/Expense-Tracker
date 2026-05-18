# Smart Expense Tracker

A full-stack Expense Tracker application built using Android and Spring Boot with JWT authentication, CRUD operations, and expense analytics visualization.

---

## Features

* User Registration & Login
* JWT-based Authentication & Authorization
* Auto Login & Logout using SharedPreferences
* Add, Update, Delete & View Expenses
* RecyclerView-based Expense Dashboard
* Expense Analytics using Pie Charts
* REST API Integration using Retrofit
* Layered Backend Architecture
* Exception Handling & DTO Pattern

---

## Tech Stack

### Android Frontend

* Java
* Android SDK
* Retrofit
* RecyclerView
* MPAndroidChart
* SharedPreferences

### Spring Boot Backend

* Java
* Spring Boot
* Spring Security
* JWT Authentication
* Spring Data JPA
* Hibernate
* REST APIs

### Database

* MySQL

### Tools

* Git
* GitHub
* Postman
* Maven

---

## Project Architecture

Android App
↓
Retrofit API Layer
↓
Spring Boot Controllers
↓
Service Layer
↓
Repository Layer
↓
MySQL Database

---

## Backend Features

* JWT Token Generation & Validation
* Secure REST APIs
* CRUD APIs for Expense Management
* DTO-based Request/Response Handling
* Global Exception Handling
* Pagination Support
* Layered Architecture

---

## Android Features

* Login & Authentication Screens
* Dashboard with RecyclerView
* Expense Analytics Dashboard
* Add/Edit/Delete Expense Screens
* Persistent User Session
* Dynamic API Integration

---

## API Endpoints

### Authentication APIs

* POST `/auth/register`
* POST `/auth/login`

### Expense APIs

* GET `/api/expenses`
* POST `/api/expenses`
* PUT `/api/expenses/{id}`
* DELETE `/api/expenses/{id}`

---

## Setup Instructions

### Backend Setup

1. Configure MySQL database in `application.properties`
2. Run Spring Boot application:

```bash
mvn spring-boot:run
```

Backend runs on:

```text
http://localhost:8081
```

---

### Android Setup

1. Open project in Android Studio
2. Start Android Emulator
3. Run application

Make sure backend is running before launching Android application.

---

## Future Improvements

* Expense Filtering & Search
* Monthly Expense Reports
* Cloud Deployment
* Dark Mode Support
* Budget Tracking
* Notification Reminders

---

## Author

Vaishali Pathak
