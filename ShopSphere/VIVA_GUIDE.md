# ShopSphere — RTU Advanced Java Viva Voce Defense Guide
## 34 In-Depth Technical Questions & Examiner Defense Answers

**Prepared for:** RTU B.Tech (CS/IT) / MCA Advanced Java Examination & Project Defense  
**Project:** ShopSphere Enterprise E-Commerce Platform  
**Architecture:** Java 21 LTS | Jakarta EE 10 | Apache Tomcat 11 | MySQL 8.4  

---

### Q1. How does ShopSphere utilize Object-Oriented Programming (OOP) principles?
**Answer:**
- **Encapsulation:** All 11 domain models (`Product`, `User`, `Order`, `CartItem`, etc.) encapsulate data via private instance fields and expose validated getters and setters. Pricing and discount formulas (e.g. `product.getDiscountedPrice()`) are encapsulated directly inside the entity.
- **Inheritance:** All web controllers extend `jakarta.servlet.http.HttpServlet`. Custom exceptions like `CheckoutException` extend `java.lang.RuntimeException`. The RMI service implementation extends `UnicastRemoteObject`.
- **Polymorphism:** The DAO and Service layers decouple callers from database connection details. Interfaces like `RemoteInventoryService` allow client code to invoke remote server methods polymorphically.
- **Abstraction:** The presentation layer (JSP/EL) is completely abstracted away from SQL queries, interacting solely with Java Beans passed through request scopes.

---

### Q2. What is Java Object Serialization and why did you declare `serialVersionUID`?
**Answer:**
Serialization (`java.io.Serializable`) is the process of converting an in-memory Java object graph into a portable sequence of bytes for network transmission or disk storage.
- All ShopSphere domain models implement `Serializable` so that Tomcat can serialize sessions across server restarts or clustered nodes.
- `serialVersionUID` is a unique 64-bit hash identifier for the class version. If we modify a class without an explicit `serialVersionUID`, the JVM auto-generates one based on class fields; any minor compiler difference or class update results in an `InvalidClassException` during deserialization. Explicitly declaring `private static final long serialVersionUID = 1L;` guarantees version compatibility.

---

### Q3. What is the role of the `transient` keyword in Java Serialization?
**Answer:**
The `transient` modifier instructs Java's serialization mechanism to skip that particular field during serialization (`ObjectOutputStream.writeObject()`).
- In `Product.java`, the field `private transient String categoryName;` is marked transient because category name is dynamically derived from a SQL JOIN at query time and should not be serialized into network payloads or session caches.

---

### Q4. What is the difference between shallow copy and deep copy in Java?
**Answer:**
- A **shallow copy** creates a new object instance but copies reference pointers to child objects. If a nested object is mutated, both the original and copy reflect the mutation.
- A **deep copy** creates a completely independent copy of the parent object and all nested child objects.
- In `SerializationDemo.java`, we demonstrate that serializing an object graph into a `ByteArrayOutputStream` and immediately deserializing it via `ByteArrayInputStream` produces an authentic **deep copy** without referencing original memory addresses.

---

### Q5. What is the difference between Checked and Unchecked Exceptions in Java?
**Answer:**
- **Checked Exceptions** (inherit directly from `java.lang.Exception`) must be explicitly handled via `try-catch` or declared via `throws` at compile time (e.g., `SQLException`, `IOException`, `RemoteException`).
- **Unchecked Exceptions** (inherit from `java.lang.RuntimeException`) represent programming bugs or business rule violations and do not require mandatory compile-time catch blocks.
- In ShopSphere, `CheckoutException` is designed as an unchecked domain exception extending `RuntimeException`, allowing transaction rollbacks without polluting every intermediate method signature.

---

### Q6. What is JDBC and why did ShopSphere use a Type-4 Database Driver?
**Answer:**
JDBC (Java Database Connectivity) is a standard Java API providing database-independent connectivity.
There are 4 driver types:
1. *Type-1 (JDBC-ODBC Bridge):* Obsolete; relies on native ODBC drivers.
2. *Type-2 (Native-API Driver):* Requires client-side C/C++ libraries.
3. *Type-3 (Network-Protocol Driver):* Uses an intermediate middleware server.
4. *Type-4 (Pure Java Driver - Native Protocol):* Converts JDBC calls directly into vendor-specific network binary protocols (e.g. MySQL port 3306). It requires no client-side native libraries, has the highest performance, and is 100% platform-independent. ShopSphere uses `mysql-connector-j 8.4.0`, a pure Type-4 driver.

---

### Q7. How does `PreparedStatement` prevent SQL Injection attacks?
**Answer:**
A standard `Statement` concatenates raw user strings into the SQL query string before sending it to the database compiler, allowing attackers to inject SQL syntax (e.g. `' OR '1'='1`).
In contrast, `PreparedStatement`:
1. Sends the SQL query template with placeholders (`?`) to the database engine first for pre-compilation and query plan generation.
2. Sends the parameter values separately via binary protocol. The database engine strictly treats parameter inputs as literal values, never as executable SQL commands.
Every query across all 11 ShopSphere DAOs strictly uses `PreparedStatement`.

---

### Q8. How does ShopSphere handle ACID Transactions during Checkout?
**Answer:**
In `CheckoutService.processCheckout()`, multi-table operations (order creation, order items insertion, inventory decrement, coupon usage audit, and payment creation) must succeed or fail as a single atomic unit.
We enforce ACID compliance by:
1. Disabling autocommit: `conn.setAutoCommit(false);`
2. Executing all updates across orders, items, products, and coupons using the *same* database connection.
3. Calling `conn.commit();` only after all operations succeed.
4. Catching any error and issuing `conn.rollback();` to restore the database to its exact prior state.
5. In the `finally` block, restoring autocommit to `true` and closing the connection.

---

### Q9. What is the difference between Statement, PreparedStatement, and CallableStatement?
**Answer:**
- **Statement:** Used for general-purpose DDL or static queries without parameters; no precompilation.
- **PreparedStatement:** Pre-compiled parameterized query; highly optimized for repeated execution; prevents SQL injection.
- **CallableStatement:** Used to execute database stored procedures and functions with IN, OUT, and INOUT parameters.

---

### Q10. What is Connection Pooling and why is it essential in Enterprise Applications?
**Answer:**
Opening a physical database connection requires TCP handshakes, authentication, and memory allocation, which is computationally expensive (typically 50–200ms per connection).
A **Connection Pool** pre-allocates a pool of active database connections that worker threads borrow and return. In ShopSphere, `DBConnection` manages connection lifecycle and leverages Tomcat's connection management to eliminate connection creation latency.

---

### Q11. Explain the Servlet Lifecycle and its core methods.
**Answer:**
A Servlet has three primary lifecycle phases managed entirely by the Servlet Container (Apache Tomcat):
1. **Initialization (`init(ServletConfig config)`):** Executed exactly once when the servlet is first requested (or at server startup if `load-on-startup` is configured). Used for one-time resource initialization.
2. **Service (`service(ServletRequest req, ServletResponse res)`):** Executed once per client HTTP request in a dedicated container worker thread. In `HttpServlet`, the `service()` method examines the HTTP verb and dispatches to `doGet()`, `doPost()`, `doPut()`, or `doDelete()`.
3. **Destruction (`destroy()`):** Executed once when the servlet is taken out of service or when Tomcat shuts down. Used to release database connections, background threads, and file handles.

---

### Q12. How does `@WebServlet` annotation simplify web application configuration?
**Answer:**
Prior to Servlet 3.0, every servlet required verbose XML mapping in `WEB-INF/web.xml` (`<servlet>` and `<servlet-mapping>`).
`@WebServlet(name = "CartServlet", urlPatterns = {"/cart", "/cart/*"})` enables declarative configuration directly above the Java class. Tomcat inspects bytecode annotations at deployment time and registers routes dynamically, eliminating XML configuration bloat.

---

### Q13. What is a Servlet Filter and how is it used in ShopSphere?
**Answer:**
A Servlet Filter (`jakarta.servlet.Filter`) intercepts incoming requests before they reach target servlets and outgoing responses before they reach the client browser.
ShopSphere utilizes a 3-tier filter pipeline:
1. `CharacterEncodingFilter`: Enforces UTF-8 request/response character encoding and injects OWASP security headers (`X-Content-Type-Options`, `X-Frame-Options`, `X-XSS-Protection`).
2. `AuthFilter`: Intercepts protected user routes (`/checkout`, `/orders`, `/profile`) and verifies if an active session exists; redirects unauthenticated users to `/login`.
3. `AdminFilter`: Intercepts administrative routes (`/admin/*`) and verifies that the session user has `role == "ADMIN"`.

---

### Q14. What are Servlet Listeners and how does ShopSphere use them?
**Answer:**
Listeners (`ServletContextListener`, `HttpSessionListener`, `ServletRequestListener`) allow an application to respond to lifecycle state changes using the Observer design pattern.
- In ShopSphere, `AppLifecycleListener` implements `ServletContextListener` (to log application startup, initialize DB drivers, and release resources on shutdown) and `HttpSessionListener` (to track concurrent active user sessions and clean up temporary cart reservations upon session expiration).

---

### Q15. Explain the 4 Session Tracking mechanisms in Java Web Applications.
**Answer:**
Because HTTP is a stateless protocol, session tracking is required to associate subsequent requests with a specific client:
1. **Cookies:** Small text key-value pairs stored in the client browser (e.g. `JSESSIONID`).
2. **HttpSession API:** Server-side memory storage identifying clients via a unique Session ID.
3. **URL Rewriting:** Appending `;jsessionid=XYZ` to the URL when client browsers disable cookies.
4. **Hidden Form Fields:** Embedding `<input type="hidden" name="sessionToken" value="...">` in HTML forms.
ShopSphere utilizes `HttpSession` with cookie-backed session tokens and CSRF token binding.

---

### Q16. How does ShopSphere protect against Session Fixation attacks?
**Answer:**
In a session fixation attack, an attacker tricks a user into authenticating with a predetermined session ID. Once logged in, the attacker uses that same session ID to hijack the account.
In ShopSphere, `LoginServlet` invokes `session.invalidate()` and `request.getSession(true)` upon successful credentials verification, creating a fresh session ID and rotating the CSRF token via `CSRFTokenUtil.rotateToken(session)`.

---

### Q17. What is Cross-Site Request Forgery (CSRF) and how is it mitigated?
**Answer:**
CSRF occurs when a malicious website tricks an authenticated user's browser into submitting unauthorized commands to a trusted application where the user is currently logged in.
ShopSphere stops CSRF by:
1. Generating a cryptographically random 32-byte URL-safe Base64 token (`CSRFTokenUtil`) bound to the user's session.
2. Embedding this token in every state-altering form (`<input type="hidden" name="csrfToken" value="...">`) and AJAX request header (`X-CSRF-Token`).
3. Validating that the submitted token matches the server-side session token before executing any mutation.

---

### Q18. What is Cross-Site Scripting (XSS) and how does `InputSanitizer` prevent it?
**Answer:**
XSS involves injecting malicious JavaScript payloads into web pages viewed by other users.
- **Reflected XSS:** The script is reflected immediately off a web server in an error message or search query.
- **Stored XSS:** The script is permanently stored in the database (e.g. inside a product review or user address).
ShopSphere implements `InputSanitizer.escapeHtml()`, converting `<`, `>`, `&`, `"`, and `'` into HTML character entities (`&lt;`, `&gt;`, `&amp;`), preventing browsers from interpreting user strings as executable DOM nodes.

---

### Q19. Explain MVC-2 Architecture as implemented in ShopSphere.
**Answer:**
MVC-2 (Model-View-Controller) enforces clean separation of concerns:
1. **Model:** POJOs (`Product`, `Order`, `User`) and Service/DAO classes representing business data and database access.
2. **View:** JSPs rendering dynamic HTML using JSTL and EL without any embedded Java scriptlets.
3. **Controller:** Jakarta Servlets (`CatalogServlet`, `CartServlet`) intercepting HTTP requests, calling business services, populating request attributes (`request.setAttribute("products", list)`), and dispatching to the view via `RequestDispatcher.forward()`.

---

### Q20. Explain the JSP Lifecycle and how Jasper compiles JSPs.
**Answer:**
When a JSP is first requested, Tomcat's Jasper engine executes a 6-phase lifecycle:
1. **Translation:** The JSP file is parsed and converted into a pure Java Servlet source file (`.java`).
2. **Compilation:** The `.java` file is compiled into bytecode (`.class`) by the Java compiler.
3. **Loading:** The compiled servlet class is loaded into JVM memory.
4. **Instantiation:** An instance of the generated servlet is created.
5. **Initialization (`_jspInit()`):** Initializes the page.
6. **Execution (`_jspService()`):** Handles client requests and writes HTML to response stream.
7. **Destruction (`_jspDestroy()`):** Releases resources upon undeployment.

---

### Q21. Why are Java Scriptlets (`<% ... %>`) considered an anti-pattern in modern JSP?
**Answer:**
Scriptlets mix Java business logic with HTML layout, leading to:
- Code duplication and unmaintainable monolithic files.
- Lack of compile-time syntax verification by standard IDE linters.
- High risk of XSS vulnerabilities and unhandled runtime exceptions.
- Violation of MVC separation of concerns.
ShopSphere completely replaces scriptlets with Expression Language (`${...}`) and Standard Tag Libraries (`<c:forEach>`, `<c:if>`).

---

### Q22. What are the 5 standard JSTL tag libraries?
**Answer:**
1. **Core (`c:`):** Iteration, conditionals, URL creation (`<c:forEach>`, `<c:if>`, `<c:choose>`, `<c:url>`).
2. **Formatting (`fmt:`):** Number, currency, and date/time formatting with i18n support (`<fmt:formatNumber>`, `<fmt:formatDate>`).
3. **Functions (`fn:`):** String manipulation functions (`fn:length()`, `fn:contains()`, `fn:toLowerCase()`).
4. **XML (`x:`):** XML parsing and XPath transformations (`<x:parse>`, `<x:out>`).
5. **SQL (`sql:`):** Direct database queries from JSPs (demonstrated in `webapp/edu/jstl-sql-demo.jsp` to explain why enterprise applications instead delegate persistence to DAOs).

---

### Q23. How does Java Socket Programming work and how is multi-threading implemented?
**Answer:**
Java Socket networking (`java.net`) enables low-level bidirectional TCP communication between two endpoints.
- `ServerSocket(port)` listens on a specified port.
- When `serverSocket.accept()` receives an incoming connection, it returns a client `Socket`.
- If handled synchronously, the server could only serve one client at a time.
- In `InventorySocketServer.java`, when a connection is accepted, it is immediately submitted to an `ExecutorService` thread pool. Dedicated worker threads execute `ClientHandler`, parsing commands via `SocketProtocol` while the main server loop immediately returns to listening for new connections.

---

### Q24. What is Java Remote Method Invocation (RMI)?
**Answer:**
Java RMI (`java.rmi`) enables a Java object on one JVM to invoke methods on a Java object running in a completely different JVM (potentially on a remote host across a network).
Key components:
1. **Remote Interface (`RemoteInventoryService`):** Extends `java.rmi.Remote` and declares methods that throw `java.rmi.RemoteException`.
2. **Remote Object (`RemoteInventoryServiceImpl`):** Extends `java.rmi.server.UnicastRemoteObject` and implements the remote interface.
3. **RMI Registry:** A lookup directory service running on port 1099 (`LocateRegistry.createRegistry(1099)`).
4. **Client Stub:** A client-side dynamic proxy that marshals parameters over the wire to the server skeleton.

---

### Q25. What is the difference between TCP Sockets and Java RMI?
**Answer:**
| Feature | TCP Sockets (`java.net`) | Java RMI (`java.rmi`) |
|---|---|---|
| **Abstraction Level** | Low-level byte and stream transport | High-level distributed object invocation |
| **Protocol** | Custom application protocol (e.g. `GET_STOCK 101`) | Native Java Remote Method Protocol (JRMP) |
| **Data Format** | Raw text, JSON, or byte frames | Serialized Java object graphs |
| **Coupling** | Language-agnostic (can talk to Python/Node) | Java-to-Java JVM tightly coupled |
| **Use Case** | IoT, game networking, microservices | Enterprise Java distributed clustering |

---

### Q26. How does Internationalization (i18n) work in Java?
**Answer:**
Internationalization adapts software to various languages and regional conventions without changing the underlying codebase.
- **`Locale`:** Represents a specific geographical, cultural, or linguistic region (e.g., `Locale.ENGLISH`, `Locale.of("hi", "IN")`).
- **`ResourceBundle`:** Loads locale-specific property files (`messages_en.properties`, `messages_hi.properties`) from the classpath.
- When `ResourceBundle.getBundle("messages", locale)` is requested, the JVM dynamically resolves the appropriate properties file and fetches localized UI strings and currency formatting symbols.

---

### Q27. Why were Java Applets deprecated in Java 9 and completely removed in Java 17?
**Answer:**
Applets were originally designed in 1995 to run interactive Java code inside web browser windows via NPAPI (Netscape Plugin Application Programming Interface).
They were removed because:
1. **Security Vulnerabilities:** Applet browser sandbox escapes became frequent attack vectors for zero-day exploits.
2. **NPAPI Deprecation:** Major web browsers (Chrome, Firefox, Edge, Safari) permanently dropped support for NPAPI browser plugins between 2015 and 2017.
3. **Modern Web Standards:** Advances in HTML5, CSS3, JavaScript, Canvas, WebGL, and WebAssembly made browser plugins obsolete.
4. **JEP 289 & JEP 398:** Formally deprecated in JDK 9 and permanently removed in JDK 17. ShopSphere provides `AppletLifecycleSimulator.java` to demonstrate the historical 5-stage lifecycle for academic viva defense.

---

### Q28. What is the Java Applet Lifecycle order?
**Answer:**
The 5 standard lifecycle states are:
1. `init()`: Called once when the applet is loaded into browser memory to initialize UI components and parse `<param>` tags.
2. `start()`: Called immediately after `init()` and each time the user navigates back to the applet page; used to start animation threads.
3. `paint(Graphics g)`: Called whenever the applet's display area needs repainting.
4. `stop()`: Called when the user navigates away from the web page or minimizes the browser tab; used to suspend CPU-heavy loops.
5. `destroy()`: Called once when the browser window is closed or applet is flushed from browser cache; used for final resource cleanup.

---

### Q29. How does Java Swing differ from AWT?
**Answer:**
- **AWT (Abstract Window Toolkit):** Heavyweight components; relies on native operating system GUI widgets; visual appearance differs across Windows/Linux/Mac.
- **Swing (`javax.swing`):** Lightweight components; 100% written in pure Java; drawn directly on screen surfaces with pluggable look-and-feel; includes advanced UI widgets like `JTable`, `JTree`, and tabbed panes.
- ShopSphere's `ShopSphereAdminApp` uses Swing's `JFrame`, `JTable`, and `DefaultTableModel` to provide a native desktop administration dashboard.

---

### Q30. What is JNDI and how is it used in Java Enterprise applications?
**Answer:**
JNDI (Java Naming and Directory Interface) is an API that allows Java applications to look up data and objects via standard name bindings (e.g. `java:comp/env/jdbc/ShopSphereDB`).
In `JndiLookupDemo.java`, we demonstrate how an `InitialContext` binds and discovers configuration objects and DataSource references decoupled from physical configuration files.

---

### Q31. What is the difference between `GET` and `POST` HTTP methods?
**Answer:**
- **`GET`:** Idempotent and safe; query parameters are visible in the URL query string; limited payload size (~2KB); browser caches GET requests; used in ShopSphere for catalog browsing, search, and product details.
- **`POST`:** Non-idempotent; parameters sent in the HTTP request body; no strict size limitation; never cached; used in ShopSphere for login, user registration, adding to cart, and checkout submission.

---

### Q32. How does ShopSphere guarantee that client-submitted prices are never trusted?
**Answer:**
A common e-commerce vulnerability is parameter tampering where a user modifies an HTML form or AJAX payload to change a price from ₹1000 to ₹1.
In ShopSphere:
- The cart and checkout servlets *only accept* `productId` and `quantity` from the client.
- The `CartService` and `CheckoutService` query the database directly using `ProductDAO.findById(productId)` to retrieve the true catalog price and discount percentage.
- All subtotals, taxes, and final payment amounts are computed server-side using `BigDecimal` with `RoundingMode.HALF_UP`.

---

### Q33. Why is `BigDecimal` used for currency instead of `float` or `double`?
**Answer:**
`float` and `double` are binary floating-point types representing numbers according to IEEE 754. They cannot accurately represent base-10 decimal fractions (for example, `0.1 + 0.2 = 0.30000000000000004`), resulting in cumulative rounding discrepancies in financial systems.
`BigDecimal` provides exact arbitrary-precision signed decimal arithmetic, making it mandatory for all currency computations in enterprise software.

---

### Q34. How was the automated test suite structured and verified?
**Answer:**
The test suite utilizes JUnit 5 Jupiter and Maven Surefire runner across 4 dedicated suites:
1. `AuthSecurityTest`: Verifies BCrypt password salting/verification, HTML entity XSS escaping, dangerous script stripping, email normalization, safe numeric parsing, and CSRF token generation/rotation/validation.
2. `ModelAndDAOTest`: Verifies product discount algorithms, savings calculations, out-of-stock states, deep object serialization for domain models, and SQL schema file integrity.
3. `NetworkRMITest`: Verifies TCP socket protocol constants, command framing parsers, RMI Remote interface contracts, RemoteException declarations, Applet state machine transitions, and English/Hindi i18n bundles.
4. `ServiceTest`: Verifies multi-item cart subtotals, savings aggregation, half-up fractional rounding, and coupon business logic bounds (max discount ceilings and minimum qualifying order thresholds).
All 30 automated test cases pass with 100% success rate (`BUILD SUCCESS`).
