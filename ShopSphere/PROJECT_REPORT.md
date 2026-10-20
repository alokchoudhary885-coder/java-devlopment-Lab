# ShopSphere — Advanced Java Enterprise E-Commerce System
## Comprehensive Architectural Specification, Technical Report & Academic Syllabus Traceability

**Academic Course:** RTU Advanced Java / Java Enterprise Programming (CS/IT B.Tech / MCA)  
**Author / Developer:** Alok Choudhary  
**Repository:** [https://github.com/alokchoudhary885-coder/ShopSphere.git](https://github.com/alokchoudhary885-coder/ShopSphere.git)  
**Target Runtime:** Apache Tomcat 11.0.x / Jakarta EE 10 / OpenJDK 21 LTS / MySQL 8.4 LTS  

---

## 1. Executive Summary & Abstract

**ShopSphere** is a high-performance, enterprise-grade e-commerce web platform and comprehensive academic demonstration suite engineered from first principles in **Java 21 LTS** and **Jakarta EE 10**. 

Unlike typical boilerplate applications, ShopSphere serves a dual objective:
1. **Production-Ready Commercial E-Commerce Platform:** Implements end-to-end customer and administrative operations—including faceted catalog navigation, responsive shopping bag, multi-table ACID transactional checkout, coupon validation with discount ceilings, and dynamic administrative KPI analytics with dual-bar canvas charts.
2. **Complete RTU Advanced Java Syllabus Demonstration:** Implements concrete, production-tested source code for every syllabus concept across all 34 modules—including Multi-threaded TCP Sockets (`java.net`), Distributed Computing via Java RMI (`java.rmi`), Desktop GUI Administration (`javax.swing`), Deep Object Serialization (`java.io`), Internationalization (`java.util.ResourceBundle`), JNDI Directory Lookup (`javax.naming`), HTTP Network Scraping (`java.net.HttpURLConnection`), and an Academic Applet Lifecycle Simulator detailing browser execution lifecycles and modern JDK 17 deprecation defense.

---

## 2. Technology Stack & Runtime Specifications

| Tier / Category | Component / Technology | Specification & Version | Purpose |
|---|---|---|---|
| **Programming Language** | Java (OpenJDK) | Java 21 LTS | Core business logic, pattern matching, record-like POJOs |
| **Enterprise Standard** | Jakarta EE | Jakarta EE 10 (Servlet 6.1, JSP 3.1) | Server-side web request lifecycles and MVC-2 container |
| **Application Server** | Apache Tomcat | Version 11.0.x | Production servlet container executing `ShopSphere.war` |
| **Relational Database** | MySQL Server | Version 8.0 / 8.4 LTS | Relational storage engine with foreign keys & UTF-8 mb4 |
| **Database Driver** | MySQL Connector/J | 8.4.0 (Type-4 pure Java) | Native binary protocol socket connectivity |
| **View Template Engine** | JSP + JSTL + EL | JSTL 3.0 (GlassFish impl) | Server-side templating without scriptlet pollution |
| **Frontend Design System** | Nordic Retail Luxe | Tailwind CSS + Google Fonts + Material Symbols | Scandinavian functionalism, editorial bento grids, tactile micro-interactions |
| **Security & Hashing** | jBCrypt | Version 0.4 (Work factor 10) | Salted one-way password cryptographic hashing |
| **Desktop Client** | Java Swing & AWT | Standard Java 21 Foundation | Standalone desktop catalog and inventory manager |
| **Distributed Systems** | Java RMI & TCP Sockets | `java.rmi` + `java.net` | Multi-threaded client-server & remote method invocation |
| **Testing Framework** | JUnit 5 (Jupiter) | Version 5.10.2 | 30 automated test cases with 100% pass rate |
| **Build & Packaging** | Apache Maven | Version 3.9.9 (`mvnw` wrapper) | Clean compilation, surefire test runner, and WAR packager |

---

## 3. Comprehensive 34-Section Syllabus Traceability Matrix

Every single syllabus topic outlined in the RTU Advanced Java Course Guide is fully implemented, verified, and mapped to concrete source code:

| # | Syllabus Requirement | Implementation File / Component | Architectural Role | Verification Status |
|---|---|---|---|---|
| **1** | Java OOP Fundamentals & Encapsulation | `com.shopsphere.model.*` (11 POJOs) | Private fields, standard getters/setters, domain invariants | Verified & Unit Tested |
| **2** | Java Object Serialization (`Serializable`) | `Product.java`, `Category.java`, `User.java` | Implements `Serializable`, defines `serialVersionUID` | Tested (`ModelAndDAOTest`) |
| **3** | Deep Object Serialization Engine | `com.shopsphere.edu.serialization.SerializationDemo` | Deep cloning via `ByteArrayOutputStream` & `ObjectInputStream` | Verified & Demonstrable |
| **4** | Exception Handling Architecture | `CheckoutException.java`, DAO `SQLException` handlers | Custom domain exceptions, checked-to-runtime translations | Verified across checkout |
| **5** | Java Collection Framework | `ArrayList`, `HashMap`, `LinkedHashMap`, `Set` | Sorting, cart items aggregation, brand indexing | Tested across all DAOs |
| **6** | JDBC Type-4 Architecture & Drivers | `com.shopsphere.util.DBConnection` | Pure Java native protocol connection factory | Tested with fallback safety |
| **7** | Safe Database Querying (`PreparedStatement`) | All 11 DAOs (`src/main/java/com/shopsphere/dao/`) | 100% parameterized SQL; zero SQL injection vulnerability | Verified across 50+ queries |
| **8** | ACID Transaction Management | `CheckoutService.processCheckout()` | `conn.setAutoCommit(false)`, `commit()`, and `rollback()` | Verified in Service Layer |
| **9** | Database Metadata Inspection | `DBConnection.java`, `AdminDashboardServlet` | Connection testing, driver versioning & metadata queries | Verified |
| **10** | Database Schema & Relational Modeling | `database/schema_only.sql`, `shopsphere.sql` | 12 normalized tables with foreign keys & cascading rules | Tested (`ModelAndDAOTest`) |
| **11** | Servlet Architecture & Lifecycle | `jakarta.servlet.http.HttpServlet` (All servlets) | `init()`, `service()`, `doGet()`, `doPost()`, `destroy()` | Verified on Tomcat 11 |
| **12** | Declarative `@WebServlet` Mapping | 20+ Servlets across storefront & admin | Clean RESTful URI routing without bloated `web.xml` | Verified |
| **13** | HTTP Request & Response Processing | `HttpServletRequest`, `HttpServletResponse` | Header reading, parameter parsing, content type headers | Verified |
| **14** | Servlet Filter Pipeline | `CharacterEncodingFilter`, `AuthFilter`, `AdminFilter` | Interception, OWASP headers, URL protection | Verified in Phase 15 |
| **15** | Application Lifecycle Listeners | `AppLifecycleListener.java` | `ServletContextListener`, `HttpSessionListener` monitoring | Verified |
| **16** | Session Management & Tracking | `HttpSession`, `request.getSession(true)` | Cart synchronization, user identity, timeout invalidation | Tested (`AuthSecurityTest`) |
| **17** | Cookie Management | `Cookie`, `response.addCookie()`, `request.getCookies()` | Session persistence and client preference tracking | Verified |
| **18** | MVC-2 Enterprise Architecture | Models (`model/`), Views (`webapp/*.jsp`), Controllers | Strict separation of concerns; no scriptlets in views | 100% MVC Compliant |
| **19** | JSP Lifecycle & Directives | `page`, `include`, `taglib` directives | Dynamic compilation to Java class files by Tomcat Jasper | Verified |
| **20** | JSP Expression Language (EL 3.0) | `${product.discountedPrice}`, `${sessionScope.user}` | Null-safe dynamic property access | Verified across 15+ JSPs |
| **21** | JSTL Core Tag Library (`c:`) | `<c:forEach>`, `<c:if>`, `<c:choose>`, `<c:set>` | Clean iterative rendering and conditional logic | Verified (`jstl-core-demo.jsp`) |
| **22** | JSTL Formatting Tag Library (`fmt:`) | `<fmt:formatNumber>`, `<fmt:formatDate>` | Currency formatting (INR ₹) and locale dates | Verified |
| **23** | JSTL Functions Library (`fn:`) | `fn:length()`, `fn:escapeXml()`, `fn:contains()` | String manipulation in JSP templates | Verified (`jstl-fn-demo.jsp`) |
| **24** | JSTL XML Tag Library (`x:`) | `<x:parse>`, `<x:out>`, `<x:forEach>` | Parsing and rendering `products.xml` catalog feeds | Verified (`jstl-xml-demo.jsp`) |
| **25** | JSTL SQL vs Enterprise DAO | `<sql:query>`, `<sql:update>` | Educational contrast: why enterprise DAOs supersede SQL tags | Verified (`jstl-sql-demo.jsp`) |
| **26** | Multi-threaded TCP Sockets (`ServerSocket`) | `com.shopsphere.network.InventorySocketServer` | Dedicated port 8085 listener with `ExecutorService` pool | Verified (`NetworkRMITest`) |
| **27** | Socket Client Application | `com.shopsphere.network.InventorySocketClient` | Interactive REPL client sending commands to server | Verified |
| **28** | Custom TCP Application Protocol | `com.shopsphere.network.SocketProtocol` | Standardized framing (`REQ:`, `RESP:`, `ERR:`, `BYE`) | Tested (`NetworkRMITest`) |
| **29** | Java RMI Remote Interface (`java.rmi.Remote`) | `com.shopsphere.rmi.RemoteInventoryService` | Defines remote stubs throwing `RemoteException` | Tested (`NetworkRMITest`) |
| **30** | Java RMI Server & Registry | `com.shopsphere.rmi.RMIServerRunner` | Port 1099 `LocateRegistry.createRegistry` & `Naming.rebind` | Verified |
| **31** | Java RMI Client Invocation | `com.shopsphere.rmi.RMIClientRunner` | Dynamic lookup `Naming.lookup()` & distributed method calls | Verified |
| **32** | Java Swing Desktop Application | `com.shopsphere.swing.ShopSphereAdminApp` | Native desktop administration UI with Swing tables & dialogs | Verified |
| **33** | Internationalization (i18n) Engine | `messages_en.properties`, `messages_hi.properties` | Dual English & Hindi UTF-8 bundles with currency/dates | Tested (`NetworkRMITest`) |
| **34** | Applet Lifecycle & JDK 17 Removal Defense | `com.shopsphere.edu.applet.AppletLifecycleSimulator` | Simulates `init`, `start`, `paint`, `stop`, `destroy` & viva notes | Tested (`NetworkRMITest`) |

---

## 4. Architectural Layering & N-Tier Enterprise Design

```
+---------------------------------------------------------------------------------------+
|                                    PRESENTATION TIER                                  |
|   Bootstrap 5.3 Responsive Web UI        Java Swing Desktop App       Socket CLI / REPL|
|   (index.jsp, catalog.jsp, cart.jsp)     (AdminMainFrame.java)        (InventoryClient)|
+-------------------------------------------+-------------------------------------------+
                                            |
+-------------------------------------------v-------------------------------------------+
|                              FILTER & INTERCEPTION PIPELINE                           |
|   CharacterEncodingFilter (UTF-8 & OWASP)  |  AuthFilter (Session) |  AdminFilter (Role)|
+-------------------------------------------+-------------------------------------------+
                                            |
+-------------------------------------------v-------------------------------------------+
|                              CONTROLLER / SERVLET LAYER (MVC)                         |
|   Storefront:  HomeServlet, CatalogServlet, ProductServlet, CartServlet, CheckoutServlet|
|   Admin:       AdminDashboardServlet, AdminProductServlet, AdminOrderServlet, etc.     |
+-------------------------------------------+-------------------------------------------+
                                            |
+-------------------------------------------v-------------------------------------------+
|                                     SERVICE LAYER                                     |
|   CartService (Pricing/Savings)           |   CheckoutService (ACID Transactions)     |
|   ProductService (Catalog & Filtering)    |   UserService (Auth & Registration)       |
+-------------------------------------------+-------------------------------------------+
                                            |
+-------------------------------------------v-------------------------------------------+
|                               DATA ACCESS OBJECT (DAO) LAYER                          |
|   ProductDAO | UserDAO | OrderDAO | OrderItemDAO | CartDAO | CouponDAO | PaymentDAO   |
|   CategoryDAO | ReviewDAO | AddressDAO | WishlistDAO                                  |
|   --> 100% Parameterized PreparedStatements; Connection Leak-Proof Try-With-Resources|
+-------------------------------------------+-------------------------------------------+
                                            |
+-------------------------------------------v-------------------------------------------+
|                                    PERSISTENCE TIER                                   |
|   MySQL 8.4 Enterprise Database (12 Normalized Relational Tables with Foreign Keys)   |
+---------------------------------------------------------------------------------------+
```

---

## 5. Relational Database Design (12 Relational Tables)

The database schema (`database/schema_only.sql` and `database/shopsphere.sql`) is designed in **Third Normal Form (3NF)** with strict referential integrity:

1. **`users`**: User identity, full name, unique email, 60-character BCrypt salted password hash, phone number, role (`CUSTOMER`, `ADMIN`), and account status (`ACTIVE`, `INACTIVE`).
2. **`addresses`**: Customer shipping/billing addresses (`street`, `city`, `state`, `postal_code`, `country`, `is_default`), linked via foreign key to `users(id) ON DELETE CASCADE`.
3. **`categories`**: Product taxonomy categorization (`name`, `description`, `icon`, `image_url`, `status`).
4. **`products`**: Catalog inventory items (`category_id`, `name`, `brand`, `description`, `price`, `discount_percent`, `stock_quantity`, `image_url`, `rating`, `review_count`, `status`).
5. **`cart`**: Persistent shopping bag items (`user_id`, `product_id`, `quantity`, `added_at`) with unique composite key `(user_id, product_id)`.
6. **`orders`**: Customer purchase records (`order_number`, `user_id`, `address_id`, `total_amount`, `discount_amount`, `final_amount`, `status`, `payment_status`).
7. **`order_items`**: Line items per order (`order_id`, `product_id`, `quantity`, `unit_price`, `subtotal`).
8. **`payments`**: Payment ledger records (`order_id`, `payment_method`, `amount`, `transaction_id`, `status`).
9. **`coupons`**: Promotional campaign vouchers (`code`, `discount_percent`, `max_discount`, `min_order_amount`, `expiry_date`, `status`).
10. **`coupon_usage`**: Audit tracking table recording which customer redeemed which coupon for which order (`coupon_id`, `user_id`, `order_id`, `used_at`).
11. **`reviews`**: Verified customer ratings (1–5 stars) and comments (`user_id`, `product_id`, `rating`, `comment`), auto-updating aggregate product ratings.
12. **`wishlist`**: Customer saved items for future purchase (`user_id`, `product_id`, `created_at`).

---

## 6. Enterprise Security Architecture

1. **Cryptographic Password Storage**: Plaintext passwords are never stored. Passwords are salted and hashed using **jBCrypt** with 10 log rounds of work factor (`$2a$10$...`).
2. **Cross-Site Scripting (XSS) Mitigation**: All user inputs (reviews, comments, catalog entries, profile details) pass through `InputSanitizer.escapeHtml()` and `InputSanitizer.scrubDangerousPayloads()` to eliminate malicious script execution and HTML entity injections.
3. **Cross-Site Request Forgery (CSRF) Protection**: A 32-byte cryptographically secure pseudo-random token (`SecureRandom`) is bound to the active HTTP session. Tokens are validated on all state-altering POST requests and rotated upon sensitive authentication state changes.
4. **OWASP Hardened HTTP Response Headers**: `CharacterEncodingFilter` injects:
   - `X-Content-Type-Options: nosniff` (Prevents MIME sniffing attacks)
   - `X-Frame-Options: SAMEORIGIN` (Defends against Clickjacking)
   - `X-XSS-Protection: 1; mode=block` (Enables browser cross-site scripting filter)
   - `Referrer-Policy: strict-origin-when-cross-origin` (Safeguards sensitive referral queries)
5. **Role-Based Access Control (RBAC)**: `AdminFilter` intercepts all `/admin/*` endpoints, verifying both session existence and administrative role (`ADMIN`), redirecting unauthorized users to login.

---

## 7. ACID Transactional Integrity in Checkout

The checkout process (`CheckoutService.processCheckout`) coordinates 5 distinct database tables under an atomic database transaction:

```java
Connection conn = DBConnection.getConnection();
try {
    conn.setAutoCommit(false); // Begin ACID Transaction

    // 1. Re-validate cart items and real-time inventory levels
    // 2. Calculate server-side prices (never trusting client parameters)
    // 3. Validate and calculate promotional coupon discount
    // 4. Insert Order record -> obtain generated order_id
    // 5. Insert OrderItem line items for every product
    // 6. Atomically decrement stockQuantity for each purchased item
    // 7. Record payment transaction record
    // 8. Log coupon usage to audit table (if applicable)
    // 9. Clear customer's shopping cart

    conn.commit(); // Atomic Commit
} catch (Exception ex) {
    conn.rollback(); // Complete Rollback on Any Failure
    throw new CheckoutException("Checkout transaction aborted: " + ex.getMessage(), ex);
} finally {
    conn.setAutoCommit(true);
    conn.close();
}
```

---

## 8. Frontend Engineering: Nordic Retail Luxe Design System

ShopSphere implements the **Nordic Retail Luxe** visual design system, combining Scandinavian functionalism, editorial bento grids, and high-conversion e-commerce ergonomics:

1. **Design Tokens & Palette:**
   - **Canvas Foundation:** `#F8F9FF` soft off-white canvas that eliminates eye strain.
   - **Focal Surfaces:** `#FFFFFF` cards with crisp hairline structural borders (`#E2E8F0`).
   - **Primary Structural Tone:** `#0B1C30` deep architectural slate.
   - **Interactive Accent:** `#4B41E1` interactive indigo for focus boundaries, pill badges, and active highlights.
   - **Utility Semantic Cues:** Green (`#009842`) for stock and savings; Amber for countdowns; Red (`#BA1A1A`) for validation alerts.
2. **Dual Typographic Hierarchy:**
   - **Headings & Prices:** Google Fonts `Plus Jakarta Sans` (`display-hero: 44px`, `headline-lg: 32px`, `price-lg: 22px`).
   - **Body & Tabular Data:** Google Fonts `Inter` (`body-lg: 16px`, `body-md: 14px`) for legibility in dense product tables and checkout lists.
3. **Core View Architecture:**
   - **Storefront (`index.jsp`):** Asymmetrical 7-column / 5-column bento hero, atmospheric space showcase (Living Room, Bedroom, Dining, Electronics, Accessories), and curatorial metric ribbon.
   - **Catalog (`products.jsp`):** Sticky faceted sidebar filters with dynamic price range inputs, brand checkboxes, rating filters, and responsive product cards with hover micro-zooms.
   - **Product Detail (`product-details.jsp`):** 50/50 spotlight gallery with micro-zoom preview, designer notes, quantity steppers, live stock indicators, and verified customer review submission forms.
   - **Account Gateway (`login.jsp`):** 3-column unified portal featuring Customer Login, Client Registration, Wishlist Preview, and 1-click Demo Credentials auto-fill.
   - **Cart & Multi-Step Checkout (`cart.jsp`, `checkout.jsp`):** Real-time shopping bag calculations, coupon code validation engine, delivery address selector, and simulated payment methods (UPI QR, Card, Net Banking, COD).
   - **Admin Console (`admin/dashboard.jsp`):** Operations telemetry stream, 4 KPI metric cards, dual-bar performance charts, and fulfillment health tracker.

---

## 9. Automated Test Suite Execution Results

All 30 automated test cases execute cleanly via JUnit 5 and Maven Surefire runner with **zero failures, zero errors, and zero skipped tests**:

```text
[INFO] -------------------------------------------------------
[INFO]  T E S T S
[INFO] -------------------------------------------------------
[INFO] Running com.shopsphere.AuthSecurityTest
[INFO] Tests run: 10, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.513 s -- in com.shopsphere.AuthSecurityTest
[INFO] Running com.shopsphere.ModelAndDAOTest
[INFO] Tests run: 7, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.042 s -- in com.shopsphere.ModelAndDAOTest
[INFO] Running com.shopsphere.NetworkRMITest
[INFO] Tests run: 7, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.032 s -- in com.shopsphere.NetworkRMITest
[INFO] Running com.shopsphere.ServiceTest
[INFO] Tests run: 6, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.011 s -- in com.shopsphere.ServiceTest
[INFO] 
[INFO] Results:
[INFO] 
[INFO] Tests run: 30, Failures: 0, Errors: 0, Skipped: 0
[INFO] 
[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESS
[INFO] ------------------------------------------------------------------------
```

---

## 9. Conclusion

ShopSphere demonstrates how traditional enterprise Java paradigms (Servlets, JSP, JDBC, Sockets, RMI, Swing) seamlessly integrate with modern software engineering practices (Jakarta EE 10, Java 21 LTS, BCrypt cryptography, CSRF protection, OWASP security headers, and automated JUnit testing) to build an academic system that meets university syllabus requirements while delivering production-grade reliability.
