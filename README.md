# Java Mini Compiler (with Spring Boot & React)

This is a **Mini Java Code Analyzer** built as a part of a Compiler Design course project. It performs **lexical** and **syntax analysis** using **ANTLR with Java 8 grammar**. The application uses a **Spring Boot backend** and a **React.js frontend** for a modern, scalable, and interactive user experience.

---

## ✨ Features

- Lexical analysis with support for Java keywords, identifiers, literals (including floating-point).
- Syntax analysis with parse tree construction and detailed syntax error handling.
- Scoped symbol table with class, method, and variable information.
- React-based frontend with visual representation of tokens, parse tree, and errors.

---

## 🚀 Project Tech Stack

| Layer        | Tech Used               |
| ------------ | ----------------------- |
| **Backend**  | Spring Boot (Java)      |
| **Frontend** | React.js (Vite)         |
| **Parser**   | ANTLR with Java8.g4     |

---

## 📂 Folder Structure

* `/cdproject` => Spring Boot Backend (Java)
* `/cd-frontend` => React Frontend (JavaScript/TypeScript)
---


## 🐳 Running with Docker & Docker Compose

### 1. Build Docker Images

#### Frontend:
```bash
    cd cd-frontend
    docker build -t compiler/frontend:1.0.0 ./
```

#### Backend:
```bash
    cd cdproject
    docker build -t compiler/backend:1.0.0 ./
```

### 2. Start Services with Docker Compose

From the project root (where `docker-compose.yml` is located):
```bash
    docker compose up -d
```
* The backend will be available at: http://localhost:8085
* The frontend will be available at: http://localhost:5500

### 3. Stop Services

To stop and remove containers:
```bash
    docker compose down
```
---
