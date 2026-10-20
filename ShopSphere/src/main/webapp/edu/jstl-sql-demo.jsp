<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>JSTL SQL vs Enterprise DAO Pattern — RTU Advanced Java</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
    <style>
        .demo-box { background: #ffffff; border-radius: 10px; border: 1px solid #e2e8f0; padding: 24px; margin-bottom: 24px; }
        .code-box { background: #1e293b; color: #f8fafc; padding: 14px 18px; border-radius: 8px; font-family: Consolas, monospace; font-size: 0.88rem; overflow-x: auto; margin: 10px 0; }
        .comparison-grid { display: grid; grid-template-columns: 1fr 1fr; gap: 20px; margin-top: 16px; }
        .comparison-card { background: #ffffff; border-radius: 8px; padding: 18px; border: 1px solid #cbd5e1; }
        .card-bad { border-top: 4px solid #ef4444; }
        .card-good { border-top: 4px solid #10b981; }
        table.matrix-table { width: 100%; border-collapse: collapse; margin-top: 14px; font-size: 0.85rem; }
        table.matrix-table th, table.matrix-table td { padding: 10px 12px; border: 1px solid #cbd5e1; text-align: left; }
        table.matrix-table th { background: #f1f5f9; }
    </style>
</head>
<body style="background: #f8fafc; padding: 40px 20px;">

    <div style="max-width: 960px; margin: 0 auto;">
        <div style="margin-bottom: 24px;">
            <a href="${pageContext.request.contextPath}/edu/index.jsp" style="color: #2563eb; text-decoration: none; font-weight: 600;">← Back to Educational Demos</a>
            <h1 style="font-size: 1.8rem; color: #0f172a; margin-top: 10px;">JSTL SQL Tags vs Enterprise DAO Architecture</h1>
            <p style="color: #64748b;">RTU Advanced Java Syllabus: In-page database tags (<code>&lt;sql:query&gt;</code>) vs Enterprise N-Tier MVC &amp; DAO Pattern for Viva Voce.</p>
        </div>

        <!-- Section 1: JSTL SQL Syntax -->
        <div class="demo-box">
            <h3 style="color: #0f172a;">1. Traditional JSTL SQL Tags Syntax</h3>
            <p style="color: #475569; font-size: 0.9rem;">Historically, JSTL provided <code>&lt;sql:setDataSource&gt;</code>, <code>&lt;sql:query&gt;</code>, and <code>&lt;sql:update&gt;</code> to query relational databases directly within JSP templates:</p>
            <div class="code-box">
&lt;%-- Legacy In-Page SQL Query in JSP --%&gt;
&lt;sql:setDataSource var="myDb" driver="com.mysql.cj.jdbc.Driver"
                   url="jdbc:mysql://localhost:3306/shopsphere" user="root" password="" /&gt;

&lt;sql:query dataSource="\${myDb}" var="productRows"&gt;
    SELECT id, name, price, stock_quantity FROM products WHERE status = TRUE ORDER BY id DESC LIMIT 5;
&lt;/sql:query&gt;

&lt;c:forEach var="row" items="\${productRows.rows}"&gt;
    Product: \${row.name} - $\${row.price}
&lt;/c:forEach&gt;
            </div>
        </div>

        <!-- Section 2: Architectural Comparison -->
        <div class="demo-box">
            <h3 style="color: #0f172a;">2. Architectural Assessment: Why Enterprise Systems Avoid JSTL SQL</h3>
            
            <div class="comparison-grid">
                <!-- Bad Approach -->
                <div class="comparison-card card-bad">
                    <span style="color: #dc2626; font-weight: 700; font-size: 0.82rem; text-transform: uppercase;">⚠️ Legacy JSTL SQL Approach</span>
                    <h4 style="margin: 8px 0; color: #1e293b;">In-Page JSP Database Execution</h4>
                    <ul style="color: #475569; font-size: 0.85rem; line-height: 1.6; margin-left: 18px;">
                        <li><strong>Violates MVC:</strong> Merges Presentation, Business Logic, and Data Persistence into one file.</li>
                        <li><strong>Security Risk:</strong> Exposes database credentials, table schema, and query structures in view layer.</li>
                        <li><strong>No Unit Testing:</strong> Cannot test database logic without rendering web pages.</li>
                        <li><strong>Poor Performance:</strong> Cannot easily leverage enterprise connection pooling or thread caching.</li>
                    </ul>
                </div>

                <!-- Good Approach -->
                <div class="comparison-card card-good">
                    <span style="color: #16a34a; font-weight: 700; font-size: 0.82rem; text-transform: uppercase;">✅ ShopSphere N-Tier Architecture</span>
                    <h4 style="margin: 8px 0; color: #1e293b;">Separation of Concerns (DAO Pattern)</h4>
                    <ul style="color: #475569; font-size: 0.85rem; line-height: 1.6; margin-left: 18px;">
                        <li><strong>Strict N-Tier:</strong> JSP (View) &rarr; Servlet (Controller) &rarr; Service (Tx) &rarr; DAO &rarr; MySQL.</li>
                        <li><strong>Parameterized PreparedStatement:</strong> 100% protection against SQL injection attacks.</li>
                        <li><strong>Connection Pooling:</strong> Centralized singleton <code>DBConnection</code> manager.</li>
                        <li><strong>Testability:</strong> DAOs and Services can be isolated and verified via JUnit 5 tests.</li>
                    </ul>
                </div>
            </div>

            <!-- Comparison Table for Examiner -->
            <table class="matrix-table">
                <thead>
                    <tr>
                        <th>Architectural Criterion</th>
                        <th>JSTL SQL (&lt;sql:query&gt;)</th>
                        <th>ShopSphere Enterprise DAO Pattern</th>
                    </tr>
                </thead>
                <tbody>
                    <tr>
                        <td><strong>Design Pattern</strong></td>
                        <td>Page Controller / Smart UI anti-pattern</td>
                        <td>Data Access Object (DAO) &amp; MVC Pattern</td>
                    </tr>
                    <tr>
                        <td><strong>SQL Injection Protection</strong></td>
                        <td>Requires manual &lt;sql:param&gt; tags</td>
                        <td>Strict <code>PreparedStatement</code> parameterized queries</td>
                    </tr>
                    <tr>
                        <td><strong>Transaction Management</strong></td>
                        <td>Difficult across multiple operations</td>
                        <td>ACID Transactions (<code>conn.setAutoCommit(false)</code>)</td>
                    </tr>
                    <tr>
                        <td><strong>Maintenance &amp; Scalability</strong></td>
                        <td>High cost: Schema change breaks JSPs</td>
                        <td>Low cost: Only model and DAO classes are modified</td>
                    </tr>
                </tbody>
            </table>
        </div>
    </div>

</body>
</html>
