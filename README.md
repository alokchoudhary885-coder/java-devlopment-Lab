# Advanced Java Development Laboratory & ShopSphere Enterprise System

[![Java Version](https://img.shields.io/badge/Java-21%20LTS-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)](https://openjdk.org/)
[![Jakarta EE](https://img.shields.io/badge/Jakarta%20EE-10-F05032?style=for-the-badge&logo=eclipse-ide&logoColor=white)](https://jakarta.ee/)
[![Apache Tomcat](https://img.shields.io/badge/Apache%20Tomcat-11.0-F8DC75?style=for-the-badge&logo=apache-tomcat&logoColor=black)](https://tomcat.apache.org/)
[![Docker](https://img.shields.io/badge/Docker-Ready-2496ED?style=for-the-badge&logo=docker&logoColor=white)](https://www.docker.com/)
[![Render](https://img.shields.io/badge/Render-Cloud%20Deploy-46E3B7?style=for-the-badge&logo=render&logoColor=black)](https://render.com/)
[![License](https://img.shields.io/badge/License-MIT-blue?style=for-the-badge)](LICENSE)

An exhaustive, end-to-end repository bringing together **Foundational Advanced Java Laboratory Experiments** and a production-grade **Capstone Enterprise E-Commerce Platform (ShopSphere)**.

---

## 📑 Table of Contents

1. [Architecture & Overview](#-architecture--overview)
2. [Curriculum & Traceability Matrix](#-curriculum--traceability-matrix)
3. [Repository Directory Structure](#-repository-directory-structure)
4. [Part I: Academic Laboratory Experiments](#-part-i-academic-laboratory-experiments)
   - [Exp 01: Student Registration Form (Swing GUI)](#experiment-01-student-registration-form)
   - [Exp 02: Student Management System (JDBC & MySQL)](#experiment-02-student-management-system)
   - [Exp 03: Calculator (MVC Architecture)](#experiment-03-calculator-mvc-architecture)
   - [Exp 04: Distributed Calculator (Java RMI)](#experiment-04-distributed-calculator-java-rmi)
   - [Exp 05: Client-Server Socket Communication (TCP/IP)](#experiment-05-client-server-socket-communication)
   - [Exp 06: Java Servlets, Filters & Session Tracking](#experiment-06-java-servlets-filters--sessions)
   - [Exp 07: Student Marks Evaluation System (JSP & JavaBeans)](#experiment-07-student-marks-evaluation-system)
5. [Part II: ShopSphere Capstone Enterprise E-Commerce](#-part-ii-shopsphere-capstone-enterprise-e-commerce)
   - [System Architecture & Design Pattern](#system-architecture--design-pattern)
   - [Key Modules & Capabilities](#key-modules--capabilities)
   - [Dual Database Architecture (MySQL + Embedded H2)](#dual-database-architecture)
   - [Cloud & Containerization (Docker + Render)](#cloud--containerization)
6. [Prerequisites & Environment Setup](#-prerequisites--environment-setup)
7. [Quick Start & Execution Guide](#-quick-start--execution-guide)
8. [Author & Attribution](#-author--attribution)

---

## 🏛️ Architecture & Overview

This repository demonstrates how academic computing concepts learned in laboratory coursework directly scale into high-concurrency, secure, real-world enterprise software systems:

```
[Laboratory Foundations]                      [Enterprise Scale Implementation]
┌──────────────────────────────────────┐      ┌─────────────────────────────────────────────────────────┐
│ 01. Swing GUI / Event Handling       │ ───> │ ShopSphere Desktop Admin Console (Swing + Live Charts) │
│ 02. JDBC CRUD & PreparedStatements   │ ───> │ Connection Pooling, DAO Pattern & ACID Rollback Engine │
│ 03. Model-View-Controller (MVC)      │ ───> │ Front Controller Architecture (Servlets, JSPs, Models) │
│ 04. Remote Method Invocation (RMI)   │ ───> │ Microservice Inventory RPC Daemon                      │
│ 05. Socket Network Programming       │ ───> │ Multi-threaded TCP Real-Time Inventory Socket Server   │
│ 06. Servlets, Filters, Sessions      │ ───> │ Authentication Filter, CSRF Tokens, Request Loggers    │
│ 07. JSP, Custom Tags, Expression Lang│ ───> │ Nordic Retail Luxe Storefront, JSTL & Bento Cards      │
└──────────────────────────────────────┘      └─────────────────────────────────────────────────────────┘
```

---

## 🧭 Curriculum & Traceability Matrix

Every syllabus concept is mirrored between its isolated laboratory experiment and its production implementation in ShopSphere:

| Academic Concept | Laboratory Experiment (`experiments/`) | Enterprise Scale Implementation (`ShopSphere/`) | Verification Test / Evidence |
|:---|:---|:---|:---|
| **Swing & AWT Desktop UI** | [`01-student-registration-form/`](experiments/01-student-registration-form/) | `com.shopsphere.swing.view.AdminDashboardFrame`<br>`com.shopsphere.swing.view.ProductManagementPanel` | Tested via `SwingAdminController` & Swing GUI bootstrap |
| **Database Connectivity (JDBC)** | [`02-jdbc-student-management/`](experiments/02-jdbc-student-management/) | `com.shopsphere.util.DBConnection`<br>`com.shopsphere.dao.*` (Product, User, Order, Cart) | `ModelAndDAOTest`, `H2DatabaseTest` |
| **MVC Design Pattern** | [`03-calculator-mvc/`](experiments/03-calculator-mvc/) | `com.shopsphere.model.*` (Model)<br>`com.shopsphere.controller.*` (Controller)<br>`src/main/webapp/*.jsp` (View) | `ServiceTest`, End-to-end HTTP request dispatching |
| **Distributed Systems (RMI)** | [`04-rmi-calculator/`](experiments/04-rmi-calculator/) | `com.shopsphere.rmi.remote.InventoryRemoteService`<br>`com.shopsphere.rmi.server.InventoryRMIServer`<br>`com.shopsphere.rmi.client.InventoryRMIClient` | `NetworkRMITest.testRMILookupAndInventoryCheck` |
| **Network Sockets (TCP/IP)** | [`05-client-server-socket/`](experiments/05-client-server-socket/) | `com.shopsphere.network.server.InventorySocketServer`<br>`com.shopsphere.network.client.InventorySocketClient` | `NetworkRMITest.testSocketInventoryQuery` |
| **Servlets & Request Handling** | [`06-java-servlets/`](experiments/06-java-servlets/) | `com.shopsphere.controller.customer.*`<br>`com.shopsphere.controller.admin.*` | `AuthSecurityTest`, HTTP 200 servlet route verification |
| **Web Filters & Security** | [`06-java-servlets/`](experiments/06-java-servlets/) | `com.shopsphere.filter.AuthFilter`<br>`com.shopsphere.filter.LoggingFilter`<br>`com.shopsphere.util.PasswordUtil` (BCrypt) | `AuthSecurityTest.testPasswordHashingAndVerification` |
| **JSP, JSTL & JavaBeans** | [`07-jsp-student-evaluation/`](experiments/07-jsp-student-evaluation/) | `src/main/webapp/index.jsp`<br>`src/main/webapp/products.jsp`<br>`src/main/webapp/WEB-INF/tags/productCard.tag`<br>`src/main/webapp/edu/*.jsp` | `productCard.tag` rendering, JSTL Core/XML/SQL test suites |

---

## 📂 Repository Directory Structure

```text
java-devlopment-Lab/
├── .gitignore                          # Excludes targets, compiled classes, binaries, logs & secrets
├── README.md                           # Master Project & Laboratory Documentation
├── experiments/                        # 7 Complete Academic Laboratory Experiments
│   ├── README.md                       # Dedicated lab guide & execution instructions
│   ├── 01-student-registration-form/   # AWT/Swing Student Registration Form
│   ├── 02-jdbc-student-management/     # JDBC + MySQL Student Management System
│   ├── 03-calculator-mvc/              # Calculator with Model-View-Controller pattern
│   ├── 04-rmi-calculator/              # Distributed Calculator using Java RMI
│   ├── 05-client-server-socket/        # TCP Client-Server Socket Communication
│   ├── 06-java-servlets/               # Jakarta Servlets, Filters, Sessions & Listeners
│   └── 07-jsp-student-evaluation/      # JSP Student Grade Evaluator & JavaBeans
└── ShopSphere/                         # Full-Scale Capstone Enterprise E-Commerce
    ├── pom.xml                         # Jakarta EE 10 / Servlet 6.1 / Java 21 Build File
    ├── Dockerfile                      # Production Multi-Stage Container (Tomcat 11 + JDK 21)
    ├── render.yaml                     # Render Cloud Web Service Blueprint Specification
    ├── README.md                       # Dedicated ShopSphere Documentation
    ├── database/
    │   └── shopsphere.sql              # Production SQL Schema & 88 Seed Products with CDN Photos
    └── src/
        ├── main/
        │   ├── java/com/shopsphere/
        │   │   ├── controller/         # Customer & Admin MVC Servlets
        │   │   ├── dao/                # Data Access Objects (User, Product, Order, Cart, etc.)
        │   │   ├── edu/                # RTU Educational Modules (Applet, I18N, JNDI, Serialization)
        │   │   ├── filter/             # Security, Authentication & Request Logging Filters
        │   │   ├── listener/           # ServletContext & Session Lifecycle Listeners
        │   │   ├── model/              # Domain Models (JavaBeans with full encapsulation)
        │   │   ├── network/            # TCP Inventory Socket Server & Client
        │   │   ├── rmi/                # Distributed Inventory Remote Method Invocation
        │   │   ├── service/            # Transactional Business Logic (Cart, Checkout, Orders)
        │   │   ├── swing/              # Standalone Desktop Admin Management Application
        │   │   └── util/               # Connection Manager, BCrypt Security, CSRF Tokens
        │   ├── resources/
        │   │   ├── db.properties       # Database Configuration Template
        │   │   └── shopsphere.sql      # Embedded Auto-Seeding Resource Script
        │   └── webapp/
        │       ├── index.jsp           # Nordic Luxe Storefront Landing Page
        │       ├── products.jsp        # Paginated Catalog with 4-Column Bento Cards
        │       ├── product-details.jsp # Product Details Gallery, Reviews & Recommendations
        │       ├── cart.jsp            # Interactive Shopping Cart
        │       ├── checkout.jsp        # Multi-Step Checkout with Coupon Validation
        │       ├── orders.jsp          # Customer Order History & Invoice Generation
        │       ├── login.jsp           # Modern Authentication Portal
        │       ├── edu/                # Interactive RTU Concept Demonstrator Portal
        │       └── WEB-INF/
        │           ├── web.xml         # Servlet & Filter Deployment Descriptor
        │           └── tags/           # Reusable JSP Custom Component Tags
        └── test/
            └── java/com/shopsphere/    # 31 Comprehensive Unit & Integration Tests
```

---

## 🔬 Part I: Academic Laboratory Experiments

### Experiment 01: Student Registration Form
* **Path:** [`experiments/01-student-registration-form/`](experiments/01-student-registration-form/)
* **Concepts:** Java AWT, Swing Containers, Layout Managers, Event-Driven Programming (`ActionListener`).
* **Source:** `StudentRegistration.java`
* **Features:** Collects Roll No, Name, Gender (Radio Buttons), Course (Dropdown Box), Phone, and Address with Submit and Reset actions.
* **Run:**
  ```bash
  cd experiments/01-student-registration-form
  javac StudentRegistration.java
  java StudentRegistration
  ```

---

### Experiment 02: Student Management System
* **Path:** [`experiments/02-jdbc-student-management/`](experiments/02-jdbc-student-management/)
* **Concepts:** Java Database Connectivity (JDBC), MySQL Connector Driver, `PreparedStatement`, `Statement`, `ResultSet`.
* **Sources:** `src/StudentRegistrationSystem.java`, `src/App.java`, `schema.sql`
* **Features:** Full graphical CRUD operations connected to a relational database. Users can Add, View, Update, Delete, and Search student records.
* **Run:**
  ```bash
  cd experiments/02-jdbc-student-management
  javac -cp "lib/mysql-connector-j-26.7.0.jar;src" src/App.java -d bin
  java -cp "bin;lib/mysql-connector-j-26.7.0.jar" App
  ```

---

### Experiment 03: Calculator (MVC Architecture)
* **Path:** [`experiments/03-calculator-mvc/`](experiments/03-calculator-mvc/)
* **Concepts:** Model-View-Controller (MVC) architectural pattern, separation of concerns.
* **Sources:**
  * `CalculatorModel.java`: Computational engine.
  * `CalculatorView.java`: Swing user interface layout.
  * `CalculatorController.java`: Event listener decoupling UI from calculations.
  * `Main.java`: Dependency injection and application launcher.
* **Run:**
  ```bash
  cd experiments/03-calculator-mvc
  javac *.java
  java Main
  ```

---

### Experiment 04: Distributed Calculator (Java RMI)
* **Path:** [`experiments/04-rmi-calculator/`](experiments/04-rmi-calculator/)
* **Concepts:** Remote Method Invocation (RMI), `Remote` interfaces, `UnicastRemoteObject`, `Naming` registry lookup.
* **Sources:** `calculator.java`, `calculatorimpl.java`, `server.java`, `client.java`
* **Features:** Client invokes mathematical calculations over network stubs executed on the server JVM.
* **Run:**
  ```bash
  cd experiments/04-rmi-calculator
  javac *.java
  # Terminal 1:
  java server
  # Terminal 2:
  java client
  ```

---

### Experiment 05: Client-Server Socket Communication
* **Path:** [`experiments/05-client-server-socket/`](experiments/05-client-server-socket/)
* **Concepts:** Berkeley TCP/IP sockets, `ServerSocket`, `Socket`, `InputStreamReader`, `PrintWriter`.
* **Sources:** `server.java`, `client.java`
* **Features:** Full-duplex network conversation between terminal endpoints over port 5000.
* **Run:**
  ```bash
  cd experiments/05-client-server-socket
  javac *.java
  # Terminal 1:
  java server
  # Terminal 2:
  java client
  ```

---

### Experiment 06: Java Servlets, Filters & Sessions
* **Path:** [`experiments/06-java-servlets/`](experiments/06-java-servlets/)
* **Concepts:** Jakarta EE Servlets, `HttpServlet`, `HttpSession` tracking, `Filter` chaining, `ServletContextListener`.
* **Sources:** `HelloServlet.java`, `LoginServlet.java`, `LogoutServlet.java`, `HomeServlet.java`, `AuthFilter.java`, `RequestLoggingFilter.java`, `AppListener.java`.
* **Run:**
  ```bash
  cd experiments/06-java-servlets
  mvn clean package
  ```

---

### Experiment 07: Student Marks Evaluation System
* **Path:** [`experiments/07-jsp-student-evaluation/`](experiments/07-jsp-student-evaluation/)
* **Concepts:** JavaServer Pages (JSP), Expression Language (EL), JSP Declarations, Scriptlets, and Expression tags.
* **Sources:** `src/main/webapp/student.jsp`, `src/main/webapp/result.jsp`, `src/main/java/com/servlet/HelloServlet.java`.
* **Features:** Dynamic computation of cumulative marks, percentage, and grade assignment with color-coded status badges.
* **Run:**
  ```bash
  cd experiments/07-jsp-student-evaluation
  mvn clean package
  ```

---

## 🛍️ Part II: ShopSphere Capstone Enterprise E-Commerce

**ShopSphere** is a modern, high-performance enterprise e-commerce platform developed with **Java 21 LTS**, **Jakarta EE 10 (Servlet 6.1)**, **Apache Tomcat 11**, and **Tailwind CSS (Nordic Retail Luxe)**.

### System Architecture & Design Pattern
* **Front Controller Pattern:** Requests route through clean REST-like endpoints managed by domain-specific controllers (`/products`, `/cart`, `/checkout`, `/orders`, `/admin/*`).
* **DAO & Service Layer:** Strict separation between transactional business rules (`CartService`, `CheckoutService`, `OrderService`, `ProductService`) and persistence logic (`ProductDAO`, `UserDAO`, `OrderDAO`, `CouponDAO`).
* **ACID Transactions:** Full database transaction management with explicit commit and automatic rollback in `CheckoutService` to guarantee data integrity across inventory decrements, payment creation, and order persistence.

### Key Modules & Capabilities
1. **Curated Product Catalog:**
   * **88 Real-World Products** spanning 8 distinct categories (Home & Living, Accessories, Healthcare, Electronic, Phone & iPads, Health & Beauty, Sports & Outdoor, Toys & Games).
   * **Curated High-Resolution Unsplash CDN Photos** accurately matching each item (Apple MacBooks, iPhones, Sony WH-1000XM5, Nike shoes, Nordic oak furniture, etc.).
   * 4-Column Bento Card grid with Atelier brand badges, dynamic star ratings, and real-time stock indicators.
2. **Authentication & Security:**
   * Enterprise-grade **BCrypt Password Hashing** with automatic salt generation (`PasswordUtil`).
   * Role-Based Access Control (`CUSTOMER` vs. `ADMIN`) enforced through `AuthFilter`.
   * CSRF protection via cryptographically strong session-bound tokens.
3. **Cart, Coupons & Checkout Engine:**
   * Server-side price validation (client prices are never trusted).
   * Coupon discount validator (`WELCOME50`, `SAVE20`, `FREESHIP`, `FESTIVE100`).
   * Atomic inventory deduction with out-of-stock guard conditions.
4. **Desktop Admin Application (Swing):**
   * Standalone desktop client (`AdminDashboardFrame`) providing operational metrics, inventory editing, and live product catalog management.
5. **Interactive RTU Educational Portal (`/edu`):**
   * Built-in interactive browser demo showcasing Applet lifecycles, JNDI lookups, Object Serialization, URLConnection, JSTL Core, XML parsing, and SQL tags.

### Dual Database Architecture
ShopSphere features an intelligent, zero-configuration database fallback system:
* **Production Mode (MySQL):** Automatically connects to MySQL when available on port 3306 or via environment overrides (`DB_URL`, `DB_USERNAME`, `DB_PASSWORD`).
* **Zero-Config Fallback (Embedded H2 in MySQL Compatibility Mode):** If no external MySQL server is reachable, `DBConnection` automatically boots an embedded H2 database located at `~/.shopsphere/shopsphere_db`, auto-executes `shopsphere.sql`, and seeds all tables and 88 products with zero manual setup required.

### Cloud & Containerization
* **Multi-Stage Dockerfile:** Compiles the application with OpenJDK 21 and bundles it into `tomcat:11.0-jdk21-temurin`.
* **Dynamic Port Binding:** Automatically detects Render's dynamic `$PORT` environment variable and binds Tomcat to it.
* **Blueprint:** Configured via `render.yaml` for instantaneous one-click cloud deployment.

---

## 💻 Prerequisites & Environment Setup

* **Java Development Kit (JDK):** Version 17 or 21 LTS (`java -version`)
* **Build Tool:** Apache Maven 3.8+ (Maven Wrapper `./mvnw` is included in the project)
* **Web Container (Optional for standalone testing):** Apache Tomcat 10.1 or 11.0
* **Database (Optional):** MySQL 8.0+ (Embedded H2 activates automatically if MySQL is absent)

---

## 🚀 Quick Start & Execution Guide

### 1. Running Any Laboratory Experiment
```bash
# Navigate to the target experiment
cd experiments/01-student-registration-form

# Compile and run
javac StudentRegistration.java
java StudentRegistration
```

### 2. Building & Running ShopSphere Locally
```bash
# Navigate to ShopSphere directory
cd ShopSphere

# Run test suite (31 automated unit & integration tests)
./mvnw clean test

# Package WAR file
./mvnw clean package

# Deploy target/ShopSphere.war to Tomcat 11 webapps directory
# Access in your browser:
# Storefront: http://localhost:8080/ShopSphere/
# Catalog:    http://localhost:8080/ShopSphere/products
# RTU Portal: http://localhost:8080/ShopSphere/edu/
```

### 3. Running ShopSphere via Docker
```bash
cd ShopSphere
docker build -t shopsphere:latest .
docker run -p 8080:8080 shopsphere:latest
```

---

## 👤 Author & Attribution

* **Author:** Alok Choudhary ([@alokchoudhary885-coder](https://github.com/alokchoudhary885-coder))
* **Primary Repository:** [ShopSphere](https://github.com/alokchoudhary885-coder/ShopSphere)
* **Laboratory Repository:** [java-devlopment-Lab](https://github.com/alokchoudhary885-coder/java-devlopment-Lab)
* **Curriculum:** Advanced Java Enterprise Programming (RTU Syllabus)
* **License:** MIT License
