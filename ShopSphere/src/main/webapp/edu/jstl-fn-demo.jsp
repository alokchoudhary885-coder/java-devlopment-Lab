<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>

<%
    request.setAttribute("sampleTitle", "ShopSphere: Redefine Comfort with Timeless Elegance");
    request.setAttribute("couponCode", "diwali2026mega");
    request.setAttribute("unsafeUserComment", "<script>alert('XSS Exploit Attack!');</script>Awesome product, highly recommended!");
%>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>JSTL Functions Demonstration — RTU Advanced Java</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
    <style>
        .demo-box { background: #ffffff; border-radius: 10px; border: 1px solid #e2e8f0; padding: 24px; margin-bottom: 24px; }
        .code-box { background: #1e293b; color: #f8fafc; padding: 14px 18px; border-radius: 8px; font-family: Consolas, monospace; font-size: 0.88rem; overflow-x: auto; margin: 10px 0; }
        .output-box { background: #ecfdf5; border-left: 4px solid #10b981; padding: 12px 16px; border-radius: 4px; font-weight: 500; color: #065f46; margin-top: 10px; }
    </style>
</head>
<body style="background: #f8fafc; padding: 40px 20px;">

    <div style="max-width: 900px; margin: 0 auto;">
        <div style="margin-bottom: 24px;">
            <a href="${pageContext.request.contextPath}/edu/index.jsp" style="color: #2563eb; text-decoration: none; font-weight: 600;">← Back to Educational Demos</a>
            <h1 style="font-size: 1.8rem; color: #0f172a; margin-top: 10px;">JSTL Functions (fn:) Demonstration</h1>
            <p style="color: #64748b;">RTU Advanced Java Syllabus: String transformations, substring slicing, pattern matching, and XSS sanitization via <code>fn:escapeXml</code>.</p>
        </div>

        <!-- 1. fn:length -->
        <div class="demo-box">
            <h3 style="color: #0f172a;">1. String Length: <code>fn:length()</code></h3>
            <div class="code-box">
Original: "\${sampleTitle}"
Length: \${fn:length(sampleTitle)} characters
            </div>
            <div class="output-box">
                String length of "${sampleTitle}" = <strong>${fn:length(sampleTitle)}</strong> characters.
            </div>
        </div>

        <!-- 2. Case Transformation -->
        <div class="demo-box">
            <h3 style="color: #0f172a;">2. Case Conversion: <code>fn:toUpperCase()</code> &amp; <code>fn:toLowerCase()</code></h3>
            <div class="code-box">
Promo Code Lower: "\${couponCode}"
Promo Code Upper: \${fn:toUpperCase(couponCode)}
            </div>
            <div class="output-box">
                Original input: <code>${couponCode}</code><br>
                Normalized for DB: <strong>${fn:toUpperCase(couponCode)}</strong>
            </div>
        </div>

        <!-- 3. Substring -->
        <div class="demo-box">
            <h3 style="color: #0f172a;">3. Substring Extraction: <code>fn:substring()</code></h3>
            <div class="code-box">
Extract first 10 chars: \${fn:substring(sampleTitle, 0, 10)}
            </div>
            <div class="output-box">
                Preview slice (0 to 10): <strong>"${fn:substring(sampleTitle, 0, 10)}"</strong>
            </div>
        </div>

        <!-- 4. Pattern Search -->
        <div class="demo-box">
            <h3 style="color: #0f172a;">4. Pattern Matching: <code>fn:contains()</code> &amp; <code>fn:startsWith()</code></h3>
            <div class="code-box">
Contains "Comfort"? \${fn:contains(sampleTitle, 'Comfort')}
Starts with "Shop"? \${fn:startsWith(sampleTitle, 'Shop')}
            </div>
            <div class="output-box">
                Contains 'Comfort': <strong>${fn:contains(sampleTitle, 'Comfort')}</strong><br>
                Starts with 'Shop': <strong>${fn:startsWith(sampleTitle, 'Shop')}</strong>
            </div>
        </div>

        <!-- 5. String Replacement -->
        <div class="demo-box">
            <h3 style="color: #0f172a;">5. Substring Replacement: <code>fn:replace()</code></h3>
            <div class="code-box">
Replace '2026' with '2027': \${fn:replace(couponCode, '2026', '2027')}
            </div>
            <div class="output-box">
                Updated code: <strong>${fn:replace(couponCode, '2026', '2027')}</strong>
            </div>
        </div>

        <!-- 6. XSS Sanitization -->
        <div class="demo-box">
            <h3 style="color: #0f172a;">6. Security &amp; XSS Prevention: <code>fn:escapeXml()</code></h3>
            <p style="color: #475569; font-size: 0.9rem;">Neutralizes cross-site scripting (XSS) attacks by converting <code>&lt;</code>, <code>&gt;</code>, <code>&amp;</code>, and <code>"</code> into HTML character entities.</p>
            <div class="code-box">
Raw payload: &lt;script&gt;alert('XSS Exploit Attack!');&lt;/script&gt;Awesome product!
Escaped: \${fn:escapeXml(unsafeUserComment)}
            </div>
            <div class="output-box" style="background: #eff6ff; border-color: #3b82f6; color: #1e40af;">
                Safe Rendered Text: <strong>${fn:escapeXml(unsafeUserComment)}</strong>
            </div>
        </div>
    </div>

</body>
</html>
