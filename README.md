# 🧠 Java Mini Compiler (with Spring Boot & React)

This is a **Mini Java Code Analyzer** built as a part of a Compiler Design course project. It performs **lexical** and **syntax analysis** using **ANTLR with Java 8 grammar**. The application uses a **Spring Boot backend** and a **React.js frontend** for a modern, scalable, and interactive user experience.

---

## ✨ Features

- ✅ Lexical analysis with support for Java keywords, identifiers, literals (including floating-point).
- ✅ Syntax analysis with parse tree construction and detailed syntax error handling.
- ✅ Scoped symbol table with class, method, and variable information.
- ✅ React-based frontend with visual representation of tokens, parse tree, and errors.

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


## 🛠 How to Run the Project

### 1. Backend (Spring Boot - `/cdproject`)

#### Requirements:
- Java 17+
- Gradle

#### Steps:
```bash
cd cdproject
# Ensure your Java version matches the one in build.gradle
./gradlew bootRun
```
The backend will start on: http://localhost:8080

### 2.Frontend (React - `/cd-frontend`)

#### Requirements:
- Node
- npm

#### Steps:
```bash
cd cd-frontend
npm install
npm run dev

```
The frontend will start on: http://localhost:5500 

