# Advanced Java Development Laboratory Experiments

A comprehensive collection of 7 academic laboratory experiments covering core and advanced Java enterprise programming concepts.

---

## 📚 Experiment Catalog

| Exp # | Experiment Title | Primary Technologies | Directory |
|:---:|:---|:---|:---|
| **01** | **Student Registration Form** | Java AWT, Swing, Event Handling | [`01-student-registration-form/`](01-student-registration-form/) |
| **02** | **Student Management System** | JDBC, MySQL Driver, PreparedStatement, SQL DDL | [`02-jdbc-student-management/`](02-jdbc-student-management/) |
| **03** | **Scientific & Standard Calculator** | Model-View-Controller (MVC) Pattern, Swing | [`03-calculator-mvc/`](03-calculator-mvc/) |
| **04** | **Distributed Calculator Service** | Java RMI (Remote Method Invocation), RMI Registry | [`04-rmi-calculator/`](04-rmi-calculator/) |
| **05** | **Bidirectional Client-Server Communication** | TCP Sockets (`ServerSocket`, `Socket`), I/O Streams | [`05-client-server-socket/`](05-client-server-socket/) |
| **06** | **Enterprise Web Servlets & Security Filters** | Jakarta Servlet 6.0, `HttpSession`, `Filter`, `Listener` | [`06-java-servlets/`](06-java-servlets/) |
| **07** | **Student Marks Evaluation System** | JavaServer Pages (JSP), JavaBeans, JSTL/EL | [`07-jsp-student-evaluation/`](07-jsp-student-evaluation/) |

---

## 🛠️ Experiment Summaries & Execution Instructions

### 01. Student Registration Form (Swing GUI)
* **Objective:** Design an interactive desktop user interface for capturing student registration details with complete event-driven form handling.
* **Key Components:**
  * `StudentRegistration.java`: `JFrame` implementation utilizing `JLabel`, `JTextField`, `JRadioButton`, `JComboBox`, `JTextArea`, `JButton`, and `ActionListener`.
* **Compile & Run:**
  ```bash
  cd 01-student-registration-form
  javac StudentRegistration.java
  java StudentRegistration
  ```

---

### 02. Student Management System (JDBC & MySQL)
* **Objective:** Perform full CRUD (Create, Read, Update, Delete) database operations from a Java desktop application.
* **Key Components:**
  * `schema.sql`: Database schema definition for table `students`.
  * `src/StudentRegistrationSystem.java`: Swing interface coupled with JDBC statements and `ResultSet` table binding.
  * `src/App.java`: Driver bootstrap class.
* **Compile & Run:**
  ```bash
  cd 02-jdbc-student-management
  javac -cp "lib/mysql-connector-j-26.7.0.jar;src" src/App.java -d bin
  java -cp "bin;lib/mysql-connector-j-26.7.0.jar" App
  ```

---

### 03. Calculator (MVC Architecture)
* **Objective:** Implement modular software architecture by separating presentation, business logic, and request routing according to the Model-View-Controller design pattern.
* **Key Components:**
  * `CalculatorModel.java`: Encapsulates arithmetic mathematical calculations.
  * `CalculatorView.java`: Renders the calculator display and numpad buttons.
  * `CalculatorController.java`: Listens to user interactions and directs state changes between Model and View.
  * `Main.java`: Instantiates and wires the MVC triad.
* **Compile & Run:**
  ```bash
  cd 03-calculator-mvc
  javac *.java
  java Main
  ```

---

### 04. Distributed Calculator (Java RMI)
* **Objective:** Enable distributed inter-process computation where client code invokes methods on an object living in a remote Java Virtual Machine.
* **Key Components:**
  * `calculator.java`: Remote interface extending `java.rmi.Remote`.
  * `calculatorimpl.java`: Remote object implementation extending `UnicastRemoteObject`.
  * `server.java`: Starts the RMI registry and exports the remote calculator stub.
  * `client.java`: Performs JNDI/RMI lookup (`Naming.lookup`) and triggers remote execution.
* **Compile & Run:**
  ```bash
  cd 04-rmi-calculator
  javac *.java

  # Terminal 1: Start Server
  java server

  # Terminal 2: Run Client
  java client
  ```

---

### 05. Client-Server Socket Communication (TCP/IP)
* **Objective:** Build reliable, connection-oriented network communication across processes using standard Berkeley TCP socket streams.
* **Key Components:**
  * `server.java`: Initializes `ServerSocket(5000)`, accepts client connections, and echoes messages.
  * `client.java`: Connects via `Socket("localhost", 5000)` and streams messages over `BufferedReader` and `PrintWriter`.
* **Compile & Run:**
  ```bash
  cd 05-client-server-socket
  javac *.java

  # Terminal 1: Start Socket Server
  java server

  # Terminal 2: Connect Socket Client
  java client
  ```

---

### 06. Java Servlets, Filters & Session Management
* **Objective:** Construct a multi-tier HTTP web application handling stateful client sessions, authentication guards, and request lifecycle logging.
* **Key Components:**
  * `HelloServlet.java`: Handles HTTP GET/POST and generates dynamic HTML responses.
  * `LoginServlet.java` & `LogoutServlet.java`: Manages session establishment and invalidation (`request.getSession()`).
  * `HomeServlet.java`: Protected dashboard rendering user session data.
  * `AuthFilter.java`: Intercepts protected URI paths and enforces authentication checks.
  * `RequestLoggingFilter.java`: Logs request headers, method, and response execution duration.
  * `AppListener.java`: `ServletContextListener` initializing application-wide scope parameters.
* **Compile & Run:**
  ```bash
  cd 06-java-servlets
  mvn clean package
  # Deploy target/*.war to Apache Tomcat 10/11 webapps directory
  ```

---

### 07. Student Evaluation System (JSP & JavaBeans)
* **Objective:** Leverage JavaServer Pages and Expression Language to separate presentation markup from server-side computational evaluation.
* **Key Components:**
  * `student.jsp`: Interactive HTML/JSP input form collecting marks across multiple academic subjects.
  * `result.jsp`: Computes total marks, percentage, and assigns letter grades with dynamic CSS status indicators.
  * `HelloServlet.java`: Controller servlet demonstrating request dispatching to JSP views.
* **Compile & Run:**
  ```bash
  cd 07-jsp-student-evaluation
  mvn clean package
  # Deploy target/*.war to Apache Tomcat 10/11 webapps directory
  ```

---

## 🔗 Bridge to Capstone Project
All 7 foundational concepts explored in these standalone laboratory experiments are engineered at enterprise scale inside **[ShopSphere](../ShopSphere/)**:
* **Swing GUI (Exp 1, 3)** $\rightarrow$ Desktop Admin Management Console (`com.shopsphere.swing.view.AdminDashboardFrame`)
* **JDBC & SQL (Exp 2)** $\rightarrow$ Connection Pooling, DAO Architecture, ACID Transactions with rollback (`DBConnection`, `ProductDAO`, `UserDAO`, `OrderDAO`)
* **RMI & Sockets (Exp 4, 5)** $\rightarrow$ Microservice Inventory RPC & Socket daemons (`com.shopsphere.rmi`, `com.shopsphere.network`)
* **Servlets, Filters, JSPs (Exp 6, 7)** $\rightarrow$ Full MVC Web Application with CSRF Protection, Session management, JSTL custom tags, and responsive storefront (`com.shopsphere.controller`, `src/main/webapp`).
