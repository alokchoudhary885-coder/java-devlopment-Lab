# RTU 5th Sem Advanced Java Lab (5CS4-24) ☕

[![Java Version](https://img.shields.io/badge/Java-21%20LTS-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)](https://openjdk.org/)
[![Jakarta EE](https://img.shields.io/badge/Jakarta%20EE-10-F05032?style=for-the-badge&logo=eclipse-ide&logoColor=white)](https://jakarta.ee/)
[![Apache Tomcat](https://img.shields.io/badge/Apache%20Tomcat-11.0-F8DC75?style=for-the-badge&logo=apache-tomcat&logoColor=black)](https://tomcat.apache.org/)
[![Render](https://img.shields.io/badge/Render-Live%20Demo-46E3B7?style=for-the-badge&logo=render&logoColor=black)](https://shopsphere-qxzf.onrender.com/)
[![License](https://img.shields.io/badge/License-MIT-blue?style=for-the-badge)](LICENSE)

This repository contains the complete practical implementation, source code, laboratory experiments, and a capstone enterprise e-commerce system (**ShopSphere**) prescribed for the **Advanced Java Laboratory** course for the 5th Semester B.Tech in Computer Science and Engineering (CSE) at **Global Institute of Technology (GIT), Jaipur**, affiliated with **Rajasthan Technical University (RTU), Kota**.

---

## 🌐 Live Project Demonstration

* 🚀 **Live Cloud Web Service (Render):** [https://shopsphere-qxzf.onrender.com/](https://shopsphere-qxzf.onrender.com/)
* 🛍️ **E-Commerce Storefront & Catalog:** [https://shopsphere-qxzf.onrender.com/ShopSphere/products](https://shopsphere-qxzf.onrender.com/ShopSphere/products)
* 🎓 **Interactive RTU Concept Demonstrator:** [https://shopsphere-qxzf.onrender.com/ShopSphere/edu/](https://shopsphere-qxzf.onrender.com/ShopSphere/edu/)

---

## 📌 Course Details

| Attribute | Details |
|:---|:---|
| **Course Title** | Advanced Java Lab |
| **Course Code** | **5CS4-24** |
| **Roll number** | **24EGJCS019** |
| **Semester** | V Semester (3rd Year B.Tech) |
| **Branch** | Computer Science & Engineering (CSE) |
| **College / Institute** | **Global Institute of Technology (GIT), Jaipur** |
| **Affiliation** | Rajasthan Technical University (RTU), Kota |
| **Academic Focus** | Swing GUI, JDBC, RMI, Sockets, Servlets, JSP, JSTL & Enterprise MVC |

---

## 📂 Repository Structure

The project is organized into modular laboratory experiments and a complete capstone enterprise application:

```text
java-devlopment-Lab/
├── .gitignore                          # Git rules excluding binaries, target/, .tomcat & secrets
├── README.md                           # Master course documentation (This file)
│
├── experiments/                        # 7 RTU Laboratory Experiments (5CS4-24)
│   ├── README.md                       # Dedicated lab guide & execution instructions
│   ├── 01-student-registration-form/   # Exp 1: Swing GUI form components & event handling
│   ├── 02-jdbc-student-management/     # Exp 2: Relational database connectivity & CRUD operations
│   ├── 03-calculator-mvc/              # Exp 3: Desktop calculator built on MVC architecture
│   ├── 04-rmi-calculator/              # Exp 4: Distributed RMI client-server application
│   ├── 05-client-server-socket/        # Exp 5: TCP/IP socket network communication
│   ├── 06-java-servlets/               # Exp 6: Jakarta Servlets, session management & filters
│   └── 07-jsp-student-evaluation/      # Exp 7: Dynamic JSP pages, Expression Language & JavaBeans
│
└── ShopSphere/                         # Capstone Enterprise Project (Nordic Retail Luxe)
    ├── pom.xml                         # Jakarta EE 10 / Servlet 6.1 / Java 21 build descriptor
    ├── Dockerfile                      # Production container deployment (Tomcat 11 + JDK 21)
    ├── render.yaml                     # Render cloud blueprint specification
    ├── database/
    │   └── shopsphere.sql              # Production schema & 88 curated products with CDN photos
    └── src/
        ├── main/
        │   ├── java/com/shopsphere/    # Controllers, DAOs, Services, Swing, RMI, Sockets
        │   ├── resources/              # db.properties & auto-seeding SQL scripts
        │   └── webapp/                 # JSPs, custom tags, Tailwind CSS & RTU educational portal
        └── test/                       # 31 Automated unit & integration tests (100% pass)
```

---

## 📋 List of Experiments (As per RTU Syllabus)

### 🔹 Module 1: Graphical User Interfaces with Swing
* **Experiment 1:** Study and implementation of Java Swing components (`JLabel`, `JTextField`, `JRadioButton`, `JComboBox`, `JButton`) with event-driven `ActionListener` handling.  
  📁 [`experiments/01-student-registration-form/`](experiments/01-student-registration-form/)
* **Experiment 2:** Implementation of the **Model-View-Controller (MVC)** architectural pattern to separate user interface, mathematical state, and event dispatching.  
  📁 [`experiments/03-calculator-mvc/`](experiments/03-calculator-mvc/)

### 🔹 Module 2: Database Programming & Networking
* **Experiment 3:** Establish relational database connectivity via the `java.sql` package using MySQL JDBC drivers to execute CRUD commands and handle `ResultSet` mappings.  
  📁 [`experiments/02-jdbc-student-management/`](experiments/02-jdbc-student-management/)
* **Experiment 4:** Client-Server Socket programming using the `java.net` package (`ServerSocket`, `Socket`) demonstrating reliable bidirectional data transfer via the TCP protocol.  
  📁 [`experiments/05-client-server-socket/`](experiments/05-client-server-socket/)

### 🔹 Module 3: Distributed Computing
* **Experiment 5:** Development of a distributed enterprise application via **Remote Method Invocation (RMI)** by defining Remote Interfaces, Stubs, Unicast Remote Objects, and binding with the `rmiregistry`.  
  📁 [`experiments/04-rmi-calculator/`](experiments/04-rmi-calculator/)

### 🔹 Module 4: Server-Side Web Technologies
* **Experiment 6:** Implementation of Java Servlets mapping the web container lifecycle, HTTP parameter parsing, session tracking (`HttpSession`), and security filter chains (`AuthFilter`, `RequestLoggingFilter`).  
  📁 [`experiments/06-java-servlets/`](experiments/06-java-servlets/)
* **Experiment 7:** Creation of dynamic JavaServer Pages (JSP) leveraging JSP implicit objects, Scriptlets, Expression Language (EL), and evaluation of student academic marks.  
  📁 [`experiments/07-jsp-student-evaluation/`](experiments/07-jsp-student-evaluation/)

---

## 🛍️ Capstone Enterprise Project: ShopSphere

All 4 syllabus modules scale directly into **[ShopSphere](ShopSphere/)**, a production-grade enterprise e-commerce platform:

* **Swing Desktop Console:** Standalone management GUI (`com.shopsphere.swing`) with live inventory analytics.
* **Network & RMI Microservices:** Background TCP socket inventory daemon and remote inventory RMI service.
* **Curated Catalog:** 88 products across 8 categories with high-resolution Unsplash CDN images and 4-column Bento cards.
* **Enterprise Security:** BCrypt password hashing (`PasswordUtil`), Role-Based Access Control (`AuthFilter`), and CSRF tokens.
* **Dual Database Engine:** Zero-config embedded H2 fallback (MySQL mode) for instant running + external MySQL support via environment variables (`DB_URL`, `DB_USERNAME`, `DB_PASSWORD`).
* **Interactive RTU Portal:** Live browser demonstrations of Applets, JNDI, Serialization, URLConnection, and JSTL Core/XML/SQL tags at `/ShopSphere/edu/`.

---

## 🛠️ Tech Stack & Environment Setup

Ensure you have the following prerequisites installed on your system:

| Technology | Recommended Version | Purpose |
|:---|:---|:---|
| **Java Development Kit** | OpenJDK 21 LTS (or JDK 17+) | Core runtime & compilation |
| **Build Tool** | Apache Maven 3.8+ (wrapper included) | Project lifecycle management |
| **Servlet Container** | Apache Tomcat 11.0 (or Tomcat 10.1) | Jakarta EE web container |
| **Relational Database** | MySQL 8.x (or built-in embedded H2) | Persistent database storage |
| **Styling & Icons** | Tailwind CSS + Google Material Symbols | Nordic Retail Luxe UI |
| **Cloud Hosting** | Docker + Render Web Service | Live cloud deployment |

---

## 🚀 Execution & Setup Guide

### 1. Clone the Repository
```bash
git clone https://github.com/alokchoudhary885-coder/java-devlopment-Lab.git
cd java-devlopment-Lab
```

### 2. Compiling & Running Core Java Experiments (Swing / Sockets / RMI)
```bash
# Experiment 1: Student Registration Form (Swing)
cd experiments/01-student-registration-form
javac StudentRegistration.java
java StudentRegistration

# Experiment 3: Calculator (MVC Architecture)
cd ../03-calculator-mvc
javac *.java
java Main

# Experiment 4: Distributed Calculator (RMI)
cd ../04-rmi-calculator
javac *.java
java server    # Terminal 1
java client    # Terminal 2

# Experiment 5: Client-Server Sockets (TCP)
cd ../05-client-server-socket
javac *.java
java server    # Terminal 1
java client    # Terminal 2
```

### 3. Running ShopSphere Enterprise Project
```bash
# Navigate to ShopSphere directory
cd ShopSphere

# Run test suite (31 automated unit & integration tests)
./mvnw clean test

# Build packaged WAR file
./mvnw clean package

# Localhost URL: http://localhost:8080/ShopSphere/
# Live Cloud URL: https://shopsphere-qxzf.onrender.com/
```

### 4. Running via Docker
```bash
cd ShopSphere
docker build -t shopsphere:latest .
docker run -p 8080:8080 shopsphere:latest
```

---

## 📝 Lab Outcomes

Upon completion of this laboratory curriculum, students will be able to:

1. **Develop Modular Desktop GUIs:** Build responsive, event-driven window applications using Java Swing and MVC architecture.
2. **Execute Reliable Database Transactions:** Connect applications to relational databases with JDBC, PreparedStatement security, and ACID transaction rollbacks.
3. **Orchestrate Distributed Systems:** Implement remote inter-process communication using TCP sockets and Java Remote Method Invocation (RMI).
4. **Deploy Enterprise Web Solutions:** Engineer dynamic, secure web platforms using Jakarta Servlets, JSP Expression Language, JSTL tags, and container filters.
5. **Architect Cloud-Ready Applications:** Package, containerize, and deploy full-stack Java solutions to modern cloud platforms (Render, Docker).

---

## 👤 Author & Attribution

* **Author:** Alok Choudhary ([@alokchoudhary885-coder](https://github.com/alokchoudhary885-coder))
* **College / Institute:** Global Institute of Technology (GIT), Jaipur
* **Affiliating University:** Rajasthan Technical University (RTU), Kota
* **Course:** B.Tech CSE — 5th Semester Advanced Java Lab (5CS4-24)
* **Primary Repository:** [ShopSphere](https://github.com/alokchoudhary885-coder/ShopSphere)
* **Laboratory Repository:** [java-devlopment-Lab](https://github.com/alokchoudhary885-coder/java-devlopment-Lab)
* **Roll Number:** 24EGJCS019
* **License:** [MIT License](LICENSE)
