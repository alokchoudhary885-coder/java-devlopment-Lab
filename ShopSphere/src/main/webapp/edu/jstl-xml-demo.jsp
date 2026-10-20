<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="x" uri="jakarta.tags.xml" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>JSTL XML Processing Demonstration — RTU Advanced Java</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
    <style>
        .demo-box { background: #ffffff; border-radius: 10px; border: 1px solid #e2e8f0; padding: 24px; margin-bottom: 24px; }
        .code-box { background: #1e293b; color: #f8fafc; padding: 14px 18px; border-radius: 8px; font-family: Consolas, monospace; font-size: 0.88rem; overflow-x: auto; margin: 10px 0; }
        .output-box { background: #ecfdf5; border-left: 4px solid #10b981; padding: 12px 16px; border-radius: 4px; font-weight: 500; color: #065f46; margin-top: 10px; }
        table.demo-table { width: 100%; border-collapse: collapse; margin-top: 12px; }
        table.demo-table th, table.demo-table td { padding: 10px 14px; border: 1px solid #cbd5e1; font-size: 0.88rem; }
        table.demo-table th { background: #f1f5f9; text-align: left; }
    </style>
</head>
<body style="background: #f8fafc; padding: 40px 20px;">

    <div style="max-width: 900px; margin: 0 auto;">
        <div style="margin-bottom: 24px;">
            <a href="${pageContext.request.contextPath}/edu/index.jsp" style="color: #2563eb; text-decoration: none; font-weight: 600;">← Back to Educational Demos</a>
            <h1 style="font-size: 1.8rem; color: #0f172a; margin-top: 10px;">JSTL XML Processing Demonstration</h1>
            <p style="color: #64748b;">RTU Advanced Java Syllabus: <code>&lt;x:parse&gt;</code>, <code>&lt;x:out&gt;</code>, <code>&lt;x:forEach&gt;</code>, and XPath expressions over external XML product catalog feeds.</p>
        </div>

        <!-- XML Feed Data & Parse -->
        <c:set var="xmlRawContent">
<catalog>
    <product id="101" category="Electronics">
        <name>Apple Watch Series 8</name>
        <brand>Apple</brand>
        <price>399.00</price>
        <rating>4.9</rating>
        <stock>45</stock>
    </product>
    <product id="102" category="Audio">
        <name>JBL Live Over Ear Wireless Headphone</name>
        <brand>JBL</brand>
        <price>129.99</price>
        <rating>4.7</rating>
        <stock>80</stock>
    </product>
    <product id="103" category="Computing">
        <name>Apple MacBook Pro 14 M3</name>
        <brand>Apple</brand>
        <price>1999.00</price>
        <rating>5.0</rating>
        <stock>18</stock>
    </product>
    <product id="104" category="Accessories">
        <name>Ergonomic Leather Desk Pad</name>
        <brand>Satechi</brand>
        <price>39.50</price>
        <rating>4.6</rating>
        <stock>120</stock>
    </product>
</catalog>
        </c:set>
        <x:parse xml="${xmlRawContent}" var="catalogDoc" />

        <!-- 1. XPath Singular Node Selection -->
        <div class="demo-box">
            <h3 style="color: #0f172a;">1. XPath Single Element Lookup: <code>&lt;x:out select="..."&gt;</code></h3>
            <p style="color: #475569; font-size: 0.9rem;">Extracts the title of the very first product in the XML catalog using the XPath expression <code>$catalogDoc/catalog/product[1]/name</code>.</p>
            <div class="code-box">
&lt;c:import url="/edu/products.xml" var="xmlRawContent" /&gt;
&lt;x:parse xml="\${xmlRawContent}" var="catalogDoc" /&gt;
First Product: &lt;x:out select="$catalogDoc/catalog/product[1]/name" /&gt;
            </div>
            <div class="output-box">
                First Item in XML Catalog: <strong><x:out select="$catalogDoc/catalog/product[1]/name" /></strong> 
                (Brand: <strong><x:out select="$catalogDoc/catalog/product[1]/brand" /></strong>, 
                Price: $<strong><x:out select="$catalogDoc/catalog/product[1]/price" /></strong>)
            </div>
        </div>

        <!-- 2. XPath Collection Iteration -->
        <div class="demo-box">
            <h3 style="color: #0f172a;">2. XPath Collection Loop: <code>&lt;x:forEach select="..."&gt;</code></h3>
            <p style="color: #475569; font-size: 0.9rem;">Iterates through all <code>&lt;product&gt;</code> nodes in the XML document and renders an HTML catalog table.</p>
            <table class="demo-table">
                <thead>
                    <tr>
                        <th>XML ID</th>
                        <th>Category</th>
                        <th>Product Title</th>
                        <th>Brand</th>
                        <th>Price</th>
                        <th>Rating</th>
                        <th>In Stock</th>
                    </tr>
                </thead>
                <tbody>
                    <x:forEach select="$catalogDoc/catalog/product" var="item">
                        <tr>
                            <td>#<strong><x:out select="@id" /></strong></td>
                            <td><span style="background: #e0f2fe; color: #0369a1; padding: 2px 8px; border-radius: 4px; font-size: 0.75rem; font-weight: 600;"><x:out select="@category" /></span></td>
                            <td><strong><x:out select="name" /></strong></td>
                            <td><x:out select="brand" /></td>
                            <td>$<x:out select="price" /></td>
                            <td>★ <x:out select="rating" /></td>
                            <td><x:out select="stock" /> units</td>
                        </tr>
                    </x:forEach>
                </tbody>
            </table>
        </div>

        <!-- 3. XPath Filtering -->
        <div class="demo-box">
            <h3 style="color: #0f172a;">3. Conditional XPath Filtering</h3>
            <p style="color: #475569; font-size: 0.9rem;">Filtering only premium items with Price &gt; $500 using XPath predicate: <code>$catalogDoc/catalog/product[price &gt; 500]</code>.</p>
            <div class="output-box" style="background: #fdf4ff; border-color: #c084fc; color: #6b21a8;">
                Premium Tier Products (Price &gt; $500):
                <ul style="margin-top: 8px; margin-left: 20px;">
                    <x:forEach select="$catalogDoc/catalog/product[price > 500]" var="p">
                        <li><strong><x:out select="name" /></strong> — $<x:out select="price" /></li>
                    </x:forEach>
                </ul>
            </div>
        </div>
    </div>

</body>
</html>
