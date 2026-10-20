<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.Date, java.time.LocalDateTime, java.time.format.DateTimeFormatter" %>

<%-- RTU Advanced Java Educational Demo: Traditional JSP Syntax --%>
<%! 
    // 1. JSP DECLARATION: Variables and methods become class-level members of generated servlet!
    private int globalHitCounter = 0;

    public int calculateDiscountedPrice(int originalPrice, int discountPercent) {
        return originalPrice - (originalPrice * discountPercent / 100);
    }
%>

<%
    // 2. JSP SCRIPTLET: Code executed inside _jspService() method for every request!
    synchronized(this) {
        globalHitCounter++;
    }
    String clientIp = request.getRemoteAddr();
    String currentTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    int samplePrice = 1200;
    int sampleDiscount = 20;
    int finalPrice = calculateDiscountedPrice(samplePrice, sampleDiscount);
%>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>JSP Syntax Demonstration — RTU Advanced Java</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
    <style>
        .demo-box { background: #ffffff; border-radius: 10px; border: 1px solid #e2e8f0; padding: 24px; margin-bottom: 24px; }
        .code-box { background: #1e293b; color: #f8fafc; padding: 14px 18px; border-radius: 8px; font-family: Consolas, monospace; font-size: 0.88rem; overflow-x: auto; margin: 10px 0; }
        .highlight { color: #38bdf8; font-weight: bold; }
        .keyword { color: #f43f5e; font-weight: bold; }
        .output-box { background: #ecfdf5; border-left: 4px solid #10b981; padding: 12px 16px; border-radius: 4px; font-weight: 600; color: #065f46; margin-top: 10px; }
    </style>
</head>
<body style="background: #f8fafc; padding: 40px 20px;">

    <div style="max-width: 900px; margin: 0 auto;">
        <div style="margin-bottom: 24px;">
            <a href="${pageContext.request.contextPath}/edu/index.jsp" style="color: #2563eb; text-decoration: none; font-weight: 600;">← Back to Educational Demos</a>
            <h1 style="font-size: 1.8rem; color: #0f172a; margin-top: 10px;">Traditional JSP Syntax Demonstration</h1>
            <p style="color: #64748b;">RTU Advanced Java Syllabus: Declarations, Scriptlets, Expressions, Directives, and Comments.</p>
        </div>

        <!-- Section 1: Declarations -->
        <div class="demo-box">
            <h3 style="color: #0f172a;">1. JSP Declaration: <code>&lt;%! ... %&gt;</code></h3>
            <p style="color: #475569; font-size: 0.9rem;">Defines instance variables and methods placed directly in the generated Servlet class body (outside the <code>_jspService()</code> method).</p>
            <div class="code-box">
&lt;%! 
    <span class="keyword">private int</span> globalHitCounter = 0;
    <span class="keyword">public int</span> calculateDiscountedPrice(<span class="keyword">int</span> originalPrice, <span class="keyword">int</span> discountPercent) {
        <span class="keyword">return</span> originalPrice - (originalPrice * discountPercent / 100);
    }
%&gt;
            </div>
            <div class="output-box">
                Method invocation result: calculateDiscountedPrice(1200, 20) = <strong>$<%= calculateDiscountedPrice(1200, 20) %></strong>
            </div>
        </div>

        <!-- Section 2: Scriptlets -->
        <div class="demo-box">
            <h3 style="color: #0f172a;">2. JSP Scriptlet: <code>&lt;% ... %&gt;</code></h3>
            <p style="color: #475569; font-size: 0.9rem;">Inserts executable Java statements directly into the servlet's <code>_jspService()</code> request handling method.</p>
            <div class="code-box">
&lt;% 
    <span class="keyword">synchronized</span>(<span class="keyword">this</span>) { globalHitCounter++; }
    String clientIp = request.getRemoteAddr();
    String currentTime = LocalDateTime.now().format(...);
%&gt;
            </div>
            <div class="output-box">
                Client Remote Address: <strong><%= clientIp %></strong> | Executed at: <strong><%= currentTime %></strong>
            </div>
        </div>

        <!-- Section 3: Expressions -->
        <div class="demo-box">
            <h3 style="color: #0f172a;">3. JSP Expression: <code>&lt;%= ... %&gt;</code></h3>
            <p style="color: #475569; font-size: 0.9rem;">Evaluates a Java expression, converts the result to String, and prints directly to the output stream via <code>out.print(...)</code>. Notice: No semicolon is allowed at the end of an expression!</p>
            <div class="code-box">
Global Page Hit Counter: &lt;%= globalHitCounter %&gt;
Original Price: &lt;%= samplePrice %&gt; | Final Price after &lt;%= sampleDiscount %&gt;% discount: &lt;%= finalPrice %&gt;
            </div>
            <div class="output-box">
                Global Cumulative Page Hit Counter: <strong><%= globalHitCounter %></strong> visits.<br>
                Price Calculation: $<%= samplePrice %> discounted by <%= sampleDiscount %>% = <strong>$<%= finalPrice %></strong>
            </div>
        </div>

        <!-- Section 4: Comments -->
        <div class="demo-box">
            <h3 style="color: #0f172a;">4. JSP Comments vs HTML Comments</h3>
            <p style="color: #475569; font-size: 0.9rem;">JSP comments <code>&lt;%-- ... --%&gt;</code> are removed during servlet compilation and never reach the client. HTML comments <code>&lt;!-- ... --&gt;</code> are sent over HTTP and visible in "View Source".</p>
            <div class="code-box">
&lt;%-- This is a hidden server-side JSP comment --%&gt;
&lt;!-- This is a client-visible HTML comment --&gt;
            </div>
            <div class="output-box" style="background: #eff6ff; border-color: #3b82f6; color: #1e40af;">
                Check page source (Ctrl+U): The JSP comment above will not appear anywhere in the HTML markup!
            </div>
        </div>
    </div>

</body>
</html>
