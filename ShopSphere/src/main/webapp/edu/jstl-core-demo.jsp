<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ page import="java.util.Arrays, java.util.List" %>

<%
    // Prepare sample list for JSTL core iteration
    List<String> productList = Arrays.asList("Smart Watch Series 8", "Wireless Noise-Cancelling Headphones", "Ultra Thin Laptop M3", "Mechanical Gaming Keyboard");
    request.setAttribute("items", productList);
%>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>JSTL Core Tags Demonstration — RTU Advanced Java</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
    <style>
        .demo-box { background: #ffffff; border-radius: 10px; border: 1px solid #e2e8f0; padding: 24px; margin-bottom: 24px; }
        .code-box { background: #1e293b; color: #f8fafc; padding: 14px 18px; border-radius: 8px; font-family: Consolas, monospace; font-size: 0.88rem; overflow-x: auto; margin: 10px 0; }
        .output-box { background: #ecfdf5; border-left: 4px solid #10b981; padding: 12px 16px; border-radius: 4px; font-weight: 500; color: #065f46; margin-top: 10px; }
        table.demo-table { width: 100%; border-collapse: collapse; margin-top: 10px; }
        table.demo-table th, table.demo-table td { padding: 8px 12px; border: 1px solid #cbd5e1; font-size: 0.88rem; }
        table.demo-table th { background: #f1f5f9; text-align: left; }
    </style>
</head>
<body style="background: #f8fafc; padding: 40px 20px;">

    <div style="max-width: 900px; margin: 0 auto;">
        <div style="margin-bottom: 24px;">
            <a href="${pageContext.request.contextPath}/edu/index.jsp" style="color: #2563eb; text-decoration: none; font-weight: 600;">← Back to Educational Demos</a>
            <h1 style="font-size: 1.8rem; color: #0f172a; margin-top: 10px;">JSTL Core Tags Demonstration</h1>
            <p style="color: #64748b;">RTU Advanced Java Syllabus: <code>&lt;c:set&gt;</code>, <code>&lt;c:out&gt;</code>, <code>&lt;c:if&gt;</code>, <code>&lt;c:choose&gt;</code>, <code>&lt;c:forEach&gt;</code>, and <code>&lt;c:catch&gt;</code>.</p>
        </div>

        <!-- 1. c:set and c:out -->
        <div class="demo-box">
            <h3 style="color: #0f172a;">1. Variable Assignment &amp; Safe Output: <code>&lt;c:set&gt;</code> &amp; <code>&lt;c:out&gt;</code></h3>
            <c:set var="cartTotal" value="${1850.00}" scope="request" />
            <c:set var="userRole" value="ADMIN" scope="request" />
            <div class="code-box">
&lt;c:set var="cartTotal" value="\${1850.00}" scope="request" /&gt;
&lt;c:set var="userRole" value="ADMIN" scope="request" /&gt;
&lt;c:out value="\${cartTotal}" /&gt;
            </div>
            <div class="output-box">
                Assigned Cart Total: <strong>$<c:out value="${cartTotal}" /></strong> | Assigned Role: <strong><c:out value="${userRole}" /></strong>
            </div>
        </div>

        <!-- 2. c:if -->
        <div class="demo-box">
            <h3 style="color: #0f172a;">2. Conditional Branching: <code>&lt;c:if&gt;</code></h3>
            <div class="code-box">
&lt;c:if test="\${cartTotal &gt;= 1000.0}"&gt;
    Eligible for Free Express Shipping!
&lt;/c:if&gt;
            </div>
            <div class="output-box">
                <c:if test="${cartTotal >= 1000.0}">
                    Status: Customer qualifies for <strong>Free Express Shipping</strong> (Order &ge; $1,000)!
                </c:if>
            </div>
        </div>

        <!-- 3. c:choose, c:when, c:otherwise -->
        <div class="demo-box">
            <h3 style="color: #0f172a;">3. Multi-way Switch/Case: <code>&lt;c:choose&gt;</code>, <code>&lt;c:when&gt;</code>, <code>&lt;c:otherwise&gt;</code></h3>
            <div class="code-box">
&lt;c:choose&gt;
    &lt;c:when test="\${userRole == 'ADMIN'}"&gt;Admin Full Permissions&lt;/c:when&gt;
    &lt;c:when test="\${userRole == 'CUSTOMER'}"&gt;Customer Browsing&lt;/c:when&gt;
    &lt;c:otherwise&gt;Guest User&lt;/c:otherwise&gt;
&lt;/c:choose&gt;
            </div>
            <div class="output-box">
                <c:choose>
                    <c:when test="${userRole == 'ADMIN'}">
                        🛡️ Authorization Level: <strong>Full Administrator Dashboard Access</strong>
                    </c:when>
                    <c:when test="${userRole == 'CUSTOMER'}">
                        👤 Authorization Level: <strong>Customer Shopping Access</strong>
                    </c:when>
                    <c:otherwise>
                        🌐 Authorization Level: <strong>Anonymous Guest</strong>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>

        <!-- 4. c:forEach with varStatus -->
        <div class="demo-box">
            <h3 style="color: #0f172a;">4. Collection Iteration: <code>&lt;c:forEach&gt;</code> with <code>varStatus</code></h3>
            <p style="color: #475569; font-size: 0.9rem;">Provides index (0-based), count (1-based), first flag, and last flag.</p>
            <table class="demo-table">
                <thead>
                    <tr>
                        <th>Count</th>
                        <th>Index</th>
                        <th>Product Title</th>
                        <th>First Item?</th>
                        <th>Last Item?</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach items="${items}" var="prodName" varStatus="vs">
                        <tr style="${vs.first ? 'background-color: #f0fdf4;' : ''}">
                            <td><strong>#${vs.count}</strong></td>
                            <td>${vs.index}</td>
                            <td><strong><c:out value="${prodName}" /></strong></td>
                            <td>${vs.first ? 'Yes (First)' : 'No'}</td>
                            <td>${vs.last ? 'Yes (Last)' : 'No'}</td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </div>

        <!-- 5. c:catch -->
        <div class="demo-box">
            <h3 style="color: #0f172a;">5. Exception Handling: <code>&lt;c:catch&gt;</code></h3>
            <div class="code-box">
&lt;c:catch var="calcError"&gt;
    &lt;% int badVal = 10 / 0; %&gt;
&lt;/c:catch&gt;
            </div>
            <c:catch var="calcError">
                <% int badVal = 10 / 0; %>
            </c:catch>
            <div class="output-box" style="background: #fef2f2; border-color: #ef4444; color: #991b1b;">
                Caught expected exception safely: <strong><c:out value="${calcError.message}" /></strong> (<c:out value="${calcError['class'].name}" />)
            </div>
        </div>
    </div>

</body>
</html>
