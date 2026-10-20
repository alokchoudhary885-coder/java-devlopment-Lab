<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>RTU Advanced Java — Academic Educational Demos | ShopSphere</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
    <style>
        .edu-container { max-width: 1000px; margin: 40px auto; padding: 0 20px; }
        .edu-header { text-align: center; margin-bottom: 35px; }
        .edu-header h1 { font-size: 2rem; color: #1e293b; margin-bottom: 8px; }
        .edu-header p { color: #64748b; font-size: 1rem; }
        .edu-grid { display: grid; grid-template-columns: repeat(auto-fit, minmax(280px, 1fr)); gap: 20px; }
        .edu-card { background: #ffffff; border-radius: 12px; padding: 24px; border: 1px solid #e2e8f0; transition: transform 0.2s, box-shadow 0.2s; text-decoration: none; color: inherit; display: flex; flex-direction: column; justify-content: space-between; }
        .edu-card:hover { transform: translateY(-3px); box-shadow: 0 8px 20px rgba(0,0,0,0.06); border-color: #3b82f6; }
        .edu-badge { display: inline-block; padding: 4px 10px; border-radius: 20px; font-size: 0.75rem; font-weight: 700; text-transform: uppercase; margin-bottom: 12px; }
        .badge-jsp { background: #fee2e2; color: #991b1b; }
        .badge-core { background: #e0f2fe; color: #0369a1; }
        .badge-fn { background: #fef3c7; color: #92400e; }
        .badge-xml { background: #f3e8ff; color: #7e22ce; }
        .badge-sql { background: #dcfce7; color: #166534; }
        .edu-card h3 { font-size: 1.15rem; color: #0f172a; margin-bottom: 8px; }
        .edu-card p { font-size: 0.88rem; color: #64748b; line-height: 1.4; margin-bottom: 16px; }
        .edu-btn { display: inline-flex; align-items: center; gap: 6px; font-size: 0.85rem; font-weight: 600; color: #2563eb; }
    </style>
</head>
<body style="background-color: #f8fafc;">

    <div class="edu-container">
        <div class="edu-header">
            <span style="font-size: 0.8rem; font-weight: 700; color: #2563eb; letter-spacing: 0.08em; text-transform: uppercase;">RTU Advanced Java Coursework</span>
            <h1>Academic Demonstration Suite</h1>
            <p>Interactive educational portals covering JSP Traditional Syntax, JSTL Core, Functions, XML Parsing, and SQL vs DAO Architecture for University Examination &amp; Viva Voce.</p>
        </div>

        <div class="edu-grid">
            <!-- Demo 1: Traditional JSP Syntax -->
            <a href="${pageContext.request.contextPath}/edu/jsp-syntax-demo.jsp" class="edu-card">
                <div>
                    <span class="edu-badge badge-jsp">Unit: JSP Syntax</span>
                    <h3>Traditional JSP Syntax Demo</h3>
                    <p>Demonstrates JSP Declarations (&lt;%! %&gt;), Scriptlets (&lt;% %&gt;), Expressions (&lt;%= %&gt;), Directives, and Hidden Comments (&lt;%-- --%&gt;).</p>
                </div>
                <span class="edu-btn">Launch Demo →</span>
            </a>

            <!-- Demo 2: JSTL Core Tags -->
            <a href="${pageContext.request.contextPath}/edu/jstl-core-demo.jsp" class="edu-card">
                <div>
                    <span class="edu-badge badge-core">Unit: JSTL Core</span>
                    <h3>JSTL Core Tags Demo</h3>
                    <p>Demonstrates &lt;c:forEach&gt;, &lt;c:if&gt;, &lt;c:choose&gt;, &lt;c:when&gt;, &lt;c:otherwise&gt;, &lt;c:set&gt;, and &lt;c:catch&gt; exception handling.</p>
                </div>
                <span class="edu-btn">Launch Demo →</span>
            </a>

            <!-- Demo 3: JSTL Functions -->
            <a href="${pageContext.request.contextPath}/edu/jstl-fn-demo.jsp" class="edu-card">
                <div>
                    <span class="edu-badge badge-fn">Unit: JSTL Functions</span>
                    <h3>JSTL Functions (fn:) Demo</h3>
                    <p>Demonstrates string manipulation: fn:length, fn:toUpperCase, fn:toLowerCase, fn:substring, fn:contains, and XSS sanitization via fn:escapeXml.</p>
                </div>
                <span class="edu-btn">Launch Demo →</span>
            </a>

            <!-- Demo 4: JSTL XML Tags -->
            <a href="${pageContext.request.contextPath}/edu/jstl-xml-demo.jsp" class="edu-card">
                <div>
                    <span class="edu-badge badge-xml">Unit: JSTL XML</span>
                    <h3>JSTL XML Processing Demo</h3>
                    <p>Parses external product catalog feed (products.xml) using &lt;x:parse&gt;, &lt;x:out&gt;, and XPath expressions (&lt;x:forEach select="..."&gt;).</p>
                </div>
                <span class="edu-btn">Launch Demo →</span>
            </a>

            <!-- Demo 5: JSTL SQL vs DAO -->
            <a href="${pageContext.request.contextPath}/edu/jstl-sql-demo.jsp" class="edu-card">
                <div>
                    <span class="edu-badge badge-sql">Unit: Database Comparison</span>
                    <h3>JSTL SQL vs Enterprise DAO</h3>
                    <p>Demonstrates legacy in-page &lt;sql:query&gt; tags and provides a detailed architectural comparison against modern N-Tier JDBC DAO patterns.</p>
                </div>
                <span class="edu-btn">Launch Demo →</span>
            </a>
        </div>

        <div style="margin-top: 40px; text-align: center; display: flex; justify-content: center; gap: 16px;">
            <a href="${pageContext.request.contextPath}/home" style="color: #2563eb; text-decoration: none; font-weight: 600; font-size: 0.9rem;">← Back to Storefront Home</a>
            <span style="color: #cbd5e1;">|</span>
            <a href="${pageContext.request.contextPath}/admin/dashboard" style="color: #2563eb; text-decoration: none; font-weight: 600; font-size: 0.9rem;">Go to Admin Dashboard →</a>
        </div>
    </div>

</body>
</html>
