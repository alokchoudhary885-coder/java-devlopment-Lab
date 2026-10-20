# ShopSphere — Advanced Java Enterprise E-Commerce Management System

> **Rajasthan Technical University (RTU) Advanced Java / Java Enterprise System & Academic Demonstration Suite**

[![Java 21 LTS](https://img.shields.io/badge/Java-21_LTS-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)](https://openjdk.org/projects/jdk/21/)
[![Jakarta EE 10](https://img.shields.io/badge/Jakarta_EE_10-Servlet_6.1-1572B6?style=for-the-badge)](https://jakarta.ee/)
[![Apache Tomcat 11](https://img.shields.io/badge/Apache_Tomcat-11.0-F8DC75?style=for-the-badge&logo=apachetomcat&logoColor=black)](https://tomcat.apache.org/)
[![MySQL 8.4](https://img.shields.io/badge/MySQL-8.4_LTS-4479A1?style=for-the-badge&logo=mysql&logoColor=white)](https://www.mysql.com/)
[![JUnit 5](https://img.shields.io/badge/Tests-30%20Passed%20(100%25)-22c55e?style=for-the-badge&logo=junit5&logoColor=white)](src/test/)
[![Build Status](https://img.shields.io/badge/Build-SUCCESS-success?style=for-the-badge)](pom.xml)

---

## Key Academic Documentation
- 📘 **[PROJECT_REPORT.md](PROJECT_REPORT.md)** — Full 9-chapter academic report detailing the enterprise architecture, N-tier layering, ER relational design, ACID transactions, security model, and complete 34-section syllabus traceability matrix.
- 🎓 **[VIVA_GUIDE.md](VIVA_GUIDE.md)** — Exhaustive 34-question university examiner viva voce defense guide with in-depth technical explanations covering Servlets, JSP, JDBC, JSTL, Sockets, RMI, Applets, Serialization, and OWASP security.

---

## Project Overview

**ShopSphere** is a dual-purpose software system developed in **Java 21 LTS** and **Jakarta EE 10**:
1. **Commercial E-Commerce Platform:** Luxury home furnishing storefront, faceted search & filter catalog, persistent cart, ACID transactional checkout, coupon validation with caps, customer profile, and responsive admin analytics dashboard with dual-bar charts.
2. **RTU Advanced Java Coursework Suite:** Practical, zero-stub implementations demonstrating all 34 syllabus requirements—including Multi-threaded TCP Sockets (`java.net`), Distributed Computing with Java RMI (`java.rmi`), Desktop GUI Administration (`javax.swing`), Deep Object Serialization (`java.io`), Internationalization (`java.util.ResourceBundle`), JNDI Directory Lookup (`javax.naming`), HTTP Web Scraping (`java.net.HttpURLConnection`), and an Applet Lifecycle Simulator (`com.shopsphere.edu.applet`).

---

## 17-Phase Implementation Summary

| Phase | Milestone | Deliverables | Status |
|---|---|---|---|
| **Phase 1** | Scaffolding & Dependencies | Maven POM, Java 21 release flags, Jakarta EE 10, MySQL 8.4 connector, JUnit 5 | Completed |
| **Phase 2** | Database Schema & Seed Data | 12 relational tables, DDL script (`schema_only.sql`), seed data with 88 products (`shopsphere.sql`) | Completed |
| **Phase 3** | Domain Models (POJOs) | 11 Serializable POJOs with `serialVersionUID`, business getters, half-up monetary calculations | Completed |
| **Phase 4** | JDBC DAO Layer | 11 DAOs with 100% parameterized PreparedStatements, query helpers, connection safety | Completed |
| **Phase 5** | Service Layer & ACID Transactions | 5 services, `CheckoutService` atomic transaction with rollback, `CartService` pricing | Completed |
| **Phase 6** | Auth & Session Security | BCrypt password salting (work factor 10), `LoginServlet`, `RegisterServlet`, `AuthFilter` | Completed |
| **Phase 7** | Storefront & Faceted Catalog | Luxury homepage, brand/price/rating facets, sorting, search, mobile drawer | Completed |
| **Phase 8** | Cart, Checkout & Orders | Ajax shopping bag, coupon code validation engine, invoice generator, order history | Completed |
| **Phase 9** | Admin Analytics Dashboard | 10 KPI metric cards, dual-bar revenue chart, split low-stock & recent orders tables | Completed |
| **Phase 10** | Admin CRUD Management | Full administrative controllers & JSPs for Products, Categories, Orders, Users, Coupons | Completed |
| **Phase 11** | Swing Desktop Admin App | Standalone GUI app with `JFrame`, `JTable`, product dialogs, stock manager | Completed |
| **Phase 12** | Multi-threaded TCP Sockets | `InventorySocketServer` on port 8085 with thread pool, interactive `InventorySocketClient` REPL | Completed |
| **Phase 13** | Distributed Computing via Java RMI | `RemoteInventoryService`, `RMIServerRunner` on port 1099, `RMIClientRunner` client stub | Completed |
| **Phase 14** | i18n & Educational Lab JSPs | English/Hindi resource bundles, JSTL Core/Format/Fn/XML/SQL labs, master hub (`/edu/`) | Completed |
| **Phase 15** | Security Hardening | `CSRFTokenUtil` (32-byte tokens), `InputSanitizer` (anti-XSS), OWASP security headers | Completed |
| **Phase 16** | Automated Test Suite | 30 automated JUnit 5 tests covering Security, Models, Sockets, RMI, and Services (100% Pass) | Completed |
| **Phase 17** | Comprehensive Documentation | `PROJECT_REPORT.md`, `VIVA_GUIDE.md`, updated `README.md`, final Git synchronization | Completed |

---

## Technology Stack

| Component | Technology | Version | Purpose |
|---|---|---|---|
| **Language** | Java (OpenJDK) | 21 LTS | Pattern matching, records, virtual thread capability |
| **Web Container** | Jakarta Servlet / JSP | 6.1 / 3.1 | HTTP request handling, session management |
| **Server** | Apache Tomcat | 11.0.x | Production WAR deployment container |
| **Database** | MySQL Server | 8.0 / 8.4 LTS | Relational data persistence with foreign keys |
| **Database Driver** | MySQL Connector/J | 8.4.0 | Native Type-4 binary protocol connectivity |
| **View Templating** | JSTL + EL | 3.0 | Presentation layer logic without Java scriptlets |
| **Design System** | Nordic Retail Luxe | Tailwind + Google Fonts | Scandinavian functionalism, editorial bento grids |
| **Security** | jBCrypt | 0.4 | Salted cryptographic password hashing |
| **Desktop GUI** | Java Swing & AWT | Standard JDK 21 | Native desktop administration suite |
| **Distributed Systems** | Java RMI & Sockets | `java.rmi` / `java.net` | Remote method calls and multi-threaded TCP server |
| **Testing** | JUnit 5 Jupiter | 5.10.2 | Automated unit and integration testing suite |

---

## Nordic Retail Luxe Design System

ShopSphere features a contemporary Scandinavian design system engineered for high-conversion enterprise e-commerce:

- **Aesthetic Principles:** Editorial poise, tactile micro-interactions, solid slate structural lines, and generous negative space rooted in architectural functionalism.
- **Color Architecture:**
  - Canvas Baseline: `#F8F9FF` (Canvas off-white)
  - Focal Surfaces: `#FFFFFF` with hairline borders (`#E2E8F0`)
  - Primary Structural Anchor: `#0B1C30` / `#000000`
  - Interactive Indigo: `#4B41E1`
  - Utility Indicators: In-Stock / Savings (`#009842`), Alerts & Urgency (`#BA1A1A`)
- **Dual Typographic Hierarchy:**
  - **Plus Jakarta Sans:** Geometric rhythm across display titles, commercial prices, and interactive tags.
  - **Inter:** Neutral, high-legibility body copy, tabular forms, and specifications.
- **Iconography:** Google `Material Symbols Outlined` with fine geometric stroke weights.
- **Unified Screen Architecture:**
  1. **Landing Storefront (`index.jsp`):** Asymmetrical 7-column / 5-column hero bento grid, atmosphere category showcase carousel, curatorial metrics ribbon (100% FSC wood, 48hr delivery, 5yr warranty), and dynamic featured products grid.
  2. **Product Catalog (`products.jsp`):** 25% sticky faceted sidebar with min/max price range sliders, category selectors, brand filters, customer ratings, and responsive 3/4-column product cards.
  3. **Product Spotlight PDP (`product-details.jsp`):** 50/50 editorial gallery with micro-zoom, designer attribution note, interactive quantity steppers, live stock indicators, customer review forms, and related curations.
  4. **Account Gateway (`login.jsp`):** 3-column unified authentication portal featuring Customer Login, Client Registration, Wishlist Preview, and 1-click Demo Credentials autofill (`admin@shopsphere.com` / `customer@shopsphere.com`).
  5. **Shopping Bag & Checkout (`cart.jsp` & `checkout.jsp`):** Line-item cart table with quantity adjusters, sticky order summary calculation, coupon code engine, multi-card address selection, and payment methods (UPI QR, Card, Net Banking, COD).
  6. **Admin Operations Suite (`admin/dashboard.jsp`):** Executive telemetry header, 4 primary KPI cards (Gross Revenue, Orders, Active Users, Low Stock Alert), dual-bar monthly performance visualization, and fulfillment health tracker.

---

## Directory Layout

```
ShopSphere/
├── pom.xml                                   # Maven POM with Java 21, Jakarta EE 10, JUnit 5
├── mvnw / mvnw.cmd                           # Maven Wrapper scripts
├── PROJECT_REPORT.md                         # Comprehensive 9-chapter academic project report
├── VIVA_GUIDE.md                             # 34-question examiner viva voce defense handbook
├── README.md                                 # Project documentation
├── database/
│   ├── schema_only.sql                       # 12 clean table DDL statements
│   └── shopsphere.sql                        # Schema + 88 seed products & sample orders
├── src/main/java/com/shopsphere/
│   ├── model/                                # 11 Serializable Domain POJOs
│   ├── dao/                                  # 11 JDBC DAOs with PreparedStatements
│   ├── service/                              # Business layer (Cart, Checkout, Product, User, Order)
│   ├── controller/                           # Storefront Jakarta Servlets
│   │   └── admin/                            # Admin Management Servlets (Dashboard, Products, etc.)
│   ├── filter/                               # CharacterEncodingFilter, AuthFilter, AdminFilter
│   ├── listener/                             # AppLifecycleListener (Context & Session events)
│   ├── util/                                 # DBConnection, PasswordUtil, CSRFTokenUtil, InputSanitizer
│   ├── swing/                                # Java Swing Desktop GUI application
│   ├── network/                              # TCP Socket Server (8085) & Client CLI
│   ├── rmi/                                  # Distributed RMI Registry, Server (1099) & Client
│   └── edu/                                  # Syllabus Educational Demos (i18n, JNDI, URL, Applet)
├── src/main/resources/
│   ├── db.properties                         # Database configuration
│   ├── messages_en.properties                # English localization bundle
│   └── messages_hi.properties                # Hindi localization bundle (UTF-8)
├── src/main/webapp/
│   ├── WEB-INF/web.xml                       # Deployment descriptor & error routing
│   ├── assets/css/ & assets/js/              # Stylesheets, chart renderer, and cart scripts
│   ├── admin/                                # Admin views (dashboard.jsp, products.jsp, etc.)
│   ├── edu/                                  # Educational JSPs (JSTL core, fn, xml, sql, syntax)
│   └── *.jsp                                 # Storefront views (index, catalog, product, cart, checkout)
└── src/test/java/com/shopsphere/
    ├── AuthSecurityTest.java                 # Password hashing, CSRF tokens, XSS sanitizer tests
    ├── ModelAndDAOTest.java                  # Product pricing, deep serialization, SQL schema integrity
    ├── NetworkRMITest.java                   # Sockets, RMI contracts, Applet state machine, i18n
    └── ServiceTest.java                      # Cart subtotals, savings aggregation, coupon ceiling bounds
```

---

## Quick Start & Execution

### 1. Database Setup
```bash
# Log in to MySQL and create schema with seed catalog
mysql -u root -p < database/shopsphere.sql
```
Verify that `src/main/resources/db.properties` contains your local database credentials:
```properties
db.url=jdbc:mysql://localhost:3306/shopsphere?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC
db.user=root
db.password=root
```

### 2. Build & Run Automated Tests
```powershell
# Compile, run all 30 JUnit 5 tests, and package WAR
.\mvnw.cmd package
```
Output:
```text
[INFO] Tests run: 30, Failures: 0, Errors: 0, Skipped: 0
[INFO] Building war: target/ShopSphere.war
[INFO] BUILD SUCCESS
```

### 3. Deploy to Apache Tomcat 11
1. Copy `target/ShopSphere.war` into Tomcat's `webapps/` folder.
2. Start Tomcat (`bin/startup.bat` or `bin/catalina.bat run`).
3. Open your browser:
   - **Storefront:** `http://localhost:8080/ShopSphere/`
   - **Admin Portal:** `http://localhost:8080/ShopSphere/admin/dashboard`
   - **Educational Lab Hub:** `http://localhost:8080/ShopSphere/edu/`

### 4. Default Login Credentials

| Role | Email | Password | Access Privileges |
|---|---|---|---|
| **Administrator** | `admin@shopsphere.com` | `admin123` | Full Admin Dashboard, Catalog CRUD, Order Management |
| **Customer** | `customer@shopsphere.com` | `customer123` | Storefront browsing, cart, checkout, profile orders |

---

## Standalone Syllabus Demonstrations

### 1. Java Swing Desktop Administration
Run the native desktop GUI without Tomcat:
```powershell
.\mvnw.cmd compile exec:java -Dexec.mainClass="com.shopsphere.swing.ShopSphereAdminApp"
```

### 2. Multi-threaded TCP Sockets (Port 8085)
Start the TCP Inventory Server:
```powershell
.\mvnw.cmd compile exec:java -Dexec.mainClass="com.shopsphere.network.InventorySocketServer"
```
In a second terminal, launch the interactive client:
```powershell
.\mvnw.cmd compile exec:java -Dexec.mainClass="com.shopsphere.network.InventorySocketClient"
```

### 3. Distributed Java RMI (Port 1099)
Start the RMI Registry and Service:
```powershell
.\mvnw.cmd compile exec:java -Dexec.mainClass="com.shopsphere.rmi.RMIServerRunner"
```
In a second terminal, execute remote method calls:
```powershell
.\mvnw.cmd compile exec:java -Dexec.mainClass="com.shopsphere.rmi.RMIClientRunner"
```

### 4. Applet Lifecycle Simulation & Viva Guide
```powershell
.\mvnw.cmd compile exec:java -Dexec.mainClass="com.shopsphere.edu.applet.AppletLifecycleSimulator"
```

---

## Verification & Academic Defense Summary

- **Automated Tests:** 30 passed / 0 failed / 0 skipped.
- **MVC Compliance:** Zero scriptlets in views; 100% EL and JSTL tags.
- **Security:** BCrypt work factor 10, anti-CSRF token rotation, HTML entity XSS sanitization, OWASP HTTP headers.
- **Traceability:** Every one of the 34 RTU Advanced Java modules mapped to concrete, executable Java code.
