# **Personal Finance Tracker**

## **Project Description**
The Personal Finance Tracker is a web application designed to help users manage their finances by tracking income, expenses, setting budgets, and generating reports. This application supports features like importing financial data through CSV files (e.g., bank statements, credit card reports), categorizing transactions, and setting periodic or one-time financial goals. The app uses **microservices architecture**, with each service focusing on a single responsibility to ensure maintainability and scalability.

---

## **Goal**
The goal of this project is to:
- Provide users with an easy-to-use tool for financial management.
- Enable users to track their spending, set budgets, and view financial insights.
- Implement a robust backend using **Spring Boot** with **microservices** to demonstrate best practices in software architecture.
- Refresh skills in **Java**, **Spring Boot**, **SQL**, and **Frontend Development** with **vanilla JavaScript** and **Bootstrap**.
- Simulate real user interactions during the beta phase by generating dummy data and automating traffic.

---

## **Basic Architecture**
The application follows a **microservices architecture**:
- **User Management Service** – Handles user registration, authentication, and profile management.
- **Transaction Service** – Manages transactions (income and expenses), categories, and payment methods.
- **Budgeting Service** – Allows users to set budgets, track spending against them, and receive alerts.
- **Reporting Service** – Provides financial summaries, category breakdowns, and CSV/PDF export functionality.
- **File Upload Service** – Handles CSV file uploads for importing financial data (bank statements, receipts).
  
Each microservice communicates with others via RESTful APIs. The front-end interacts with these services via API endpoints, ensuring separation of concerns and allowing scalability.

---

## **Tech Stack**
- **Backend**:  
  - **Java** – Core programming language for backend services.
  - **Spring Boot** – For building the microservices-based backend.
  - **Spring Security** – For managing user authentication and session handling.
  - **JPA/Hibernate** – For database interactions.
  
- **Frontend**:  
  - **Vanilla JavaScript** – For implementing dynamic features and asynchronous operations.
  - **Bootstrap** – For styling and responsive design.

- **Database**:  
  - **MySQL** or **MariaDB** – For production database (H2 for local development).
  
- **File Handling**:  
  - **CSV Parser** – For parsing and processing uploaded bank/credit card statements.

- **DevOps**:  
  - **Docker** (for local dev environments, future deployment).
  - **Jenkins** (for CI/CD pipeline – optional for future integration).

---

## **Getting Started**

### **Prerequisites**
1. **Java 11+** (for backend services).
2. **MySQL** (or MariaDB for local dev environment).
3. **Node.js** (for front-end dependencies and development).
4. **Docker** (optional, for local service orchestration).

### **Installation**
#### Clone the repository and set up the backend and frontend:
```bash
git clone <repo_url>
cd personal-finance-tracker
```
#### Backend Setup
Navigate to the backend folder:
```bash
cd backend
```

Build the services:
```bash
mvn clean install
```
Run the Spring Boot application:
```bash
mvn spring-boot:run
```

#### Frontend Setup
Navigate to the frontend folder:
```bash
cd frontend
```

Install dependencies:
```bash
npm install
```

Start the front-end server:
```bash
npm start
```

---

## **Features**

- **User Management:** Register, login, and manage user profile.
- **Transactions:** Add, update, and delete income/expense transactions.
- **Budgets:** Set and track monthly/weekly budgets by category.
- **Reports:** View financial summaries, generate CSV/PDF reports.
- **CSV Upload:** Import bank statements or credit card transactions via CSV files.
- **Gamification:** "Fake Money Challenge" for user engagement.

---

## **Future Enhancements**
Integrate **machine learning** to predict future expenses based on past transactions.

Implement **email/SMS** notifications for budget overrun or transaction reminders.

Add a **mobile app** for accessing financial data on-the-go.

---

## **Contributing**
Feel free to open issues or submit pull requests for any enhancements or bug fixes.
Please follow the project's Git workflow:

1. Fork the repository.
2. Create a new branch for your feature or bug fix.
3. Submit a pull request with a description of the changes.

