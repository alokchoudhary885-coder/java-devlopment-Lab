<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Product Management — ShopSphere Admin</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/admin.css">
    <style>
        .modal-overlay {
            display: none;
            position: fixed;
            top: 0; left: 0; right: 0; bottom: 0;
            background: rgba(0, 0, 0, 0.5);
            z-index: 2000;
            align-items: center;
            justify-content: center;
        }
        .modal-overlay.open { display: flex; }
        .modal-content {
            background: #ffffff;
            border-radius: 12px;
            width: 100%;
            max-width: 650px;
            max-height: 90vh;
            overflow-y: auto;
            padding: 24px;
            box-shadow: 0 10px 25px rgba(0, 0, 0, 0.2);
        }
        .modal-header {
            display: flex;
            align-items: center;
            justify-content: space-between;
            margin-bottom: 20px;
            border-bottom: 1px solid #e2e8f0;
            padding-bottom: 12px;
        }
        .form-grid {
            display: grid;
            grid-template-columns: 1fr 1fr;
            gap: 16px;
        }
        .form-group-full { grid-column: 1 / -1; }
        .form-label {
            display: block;
            font-size: 0.8rem;
            font-weight: 600;
            color: #475569;
            margin-bottom: 6px;
        }
        .form-input, .form-select, .form-textarea {
            width: 100%;
            padding: 8px 12px;
            border-radius: 6px;
            border: 1px solid #cbd5e1;
            font-size: 0.85rem;
            outline: none;
        }
        .form-input:focus, .form-select:focus, .form-textarea:focus {
            border-color: var(--admin-primary);
        }
        .alert-box {
            padding: 12px 16px;
            border-radius: 8px;
            margin-bottom: 20px;
            font-size: 0.85rem;
            font-weight: 500;
        }
        .alert-success { background: #dcfce7; color: #166534; border: 1px solid #bbf7d0; }
        .alert-danger { background: #fee2e2; color: #991b1b; border: 1px solid #fecaca; }
    </style>
</head>
<body class="admin-body">

    <!-- Dark Sidebar -->
    <jsp:include page="/fragments/admin-sidebar.jsp" />

    <div class="admin-main-wrapper">
        <!-- Top Header -->
        <jsp:include page="/fragments/admin-header.jsp" />

        <main class="admin-content">
            <!-- Header area -->
            <div class="admin-page-header">
                <div class="admin-title-area">
                    <h1>Product Catalog Management</h1>
                    <div class="admin-breadcrumb">
                        <span>items</span> / <a href="${pageContext.request.contextPath}/admin/products">products</a>
                    </div>
                </div>

                <div class="admin-header-controls">
                    <button class="admin-btn admin-btn-primary" onclick="openAddModal()">
                        <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2.5">
                            <line x1="12" y1="5" x2="12" y2="19"></line>
                            <line x1="5" y1="12" x2="19" y2="12"></line>
                        </svg>
                        <span>Add New Product</span>
                    </button>
                </div>
            </div>

            <!-- Flash Alert -->
            <c:if test="${not empty flashMessage}">
                <div class="alert-box alert-${flashType}">
                    <c:out value="${flashMessage}" />
                </div>
            </c:if>

            <!-- Filters & Search Toolbar -->
            <div class="admin-card" style="padding: 16px 20px; margin-bottom: 20px;">
                <form action="${pageContext.request.contextPath}/admin/products" method="get" style="display: flex; gap: 12px; flex-wrap: wrap;">
                    <input type="text" name="keyword" value="${param.keyword}" placeholder="Search product name or brand..." class="form-input" style="max-width: 300px;" />
                    <select name="categoryId" class="form-select" style="max-width: 220px;" onchange="this.form.submit()">
                        <option value="">All Categories</option>
                        <c:forEach items="${categories}" var="cat">
                            <option value="${cat.id}" ${param.categoryId == cat.id ? 'selected' : ''}>
                                <c:out value="${cat.name}" />
                            </option>
                        </c:forEach>
                    </select>
                    <button type="submit" class="admin-btn admin-btn-outline">Search</button>
                    <c:if test="${not empty param.keyword || not empty param.categoryId}">
                        <a href="${pageContext.request.contextPath}/admin/products" class="admin-btn admin-btn-outline" style="color: #64748b;">Reset</a>
                    </c:if>
                </form>
            </div>

            <!-- Products Data Table -->
            <div class="admin-card">
                <div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 16px;">
                    <h3 style="font-size: 1rem; color: #1e293b;">Catalog Items (${products != null ? products.size() : 0})</h3>
                </div>

                <div style="overflow-x: auto;">
                    <table class="admin-data-table">
                        <thead>
                            <tr>
                                <th style="width: 50px;">ID</th>
                                <th>Product Details</th>
                                <th>Category</th>
                                <th>Price</th>
                                <th>Discount</th>
                                <th>Stock</th>
                                <th>Status</th>
                                <th style="text-align: right;">Actions</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:choose>
                                <c:when test="${empty products}">
                                    <tr>
                                        <td colspan="8" style="text-align: center; padding: 40px; color: #94a3b8;">
                                            No products found matching criteria.
                                        </td>
                                    </tr>
                                </c:when>
                                <c:otherwise>
                                    <c:forEach items="${products}" var="p">
                                        <tr>
                                            <td><strong>#<c:out value="${p.id}" /></strong></td>
                                            <td>
                                                <div class="table-product-cell">
                                                    <img src="${p.imageUrl}" alt="${p.name}" class="table-product-thumb" onerror="this.src='https://images.unsplash.com/photo-1523275335684-37898b6baf30?w=100&q=80'" />
                                                    <div>
                                                        <div class="table-product-title" title="${p.name}"><c:out value="${p.name}" /></div>
                                                        <span style="font-size: 0.75rem; color: #64748b;"><c:out value="${p.brand}" /></span>
                                                    </div>
                                                </div>
                                            </td>
                                            <td><c:out value="${p.categoryName}" /></td>
                                            <td><strong>$<fmt:formatNumber value="${p.price}" pattern="#,##0.00" /></strong></td>
                                            <td>
                                                <c:choose>
                                                    <c:when test="${p.discountPercent > 0}">
                                                        <span style="color: #dc2626; font-weight: 600;">-<fmt:formatNumber value="${p.discountPercent}" pattern="#,##0" />%</span>
                                                    </c:when>
                                                    <c:otherwise><span style="color: #94a3b8;">—</span></c:otherwise>
                                                </c:choose>
                                            </td>
                                            <td>
                                                <form action="${pageContext.request.contextPath}/admin/products" method="post" style="display: inline-flex; align-items: center; gap: 6px;">
                                                    <input type="hidden" name="action" value="updateStock" />
                                                    <input type="hidden" name="id" value="${p.id}" />
                                                    <input type="number" name="stockQuantity" value="${p.stockQuantity}" min="0" style="width: 60px; padding: 3px 6px; border: 1px solid #cbd5e1; border-radius: 4px; font-size: 0.8rem;" />
                                                    <button type="submit" class="admin-btn admin-btn-outline admin-btn-sm" title="Save Stock">✓</button>
                                                </form>
                                                <c:choose>
                                                    <c:when test="${p.stockQuantity <= 0}">
                                                        <span class="status-badge status-cancelled" style="margin-left: 4px;">Out of Stock</span>
                                                    </c:when>
                                                    <c:when test="${p.stockQuantity < 10}">
                                                        <span class="status-badge status-pending" style="margin-left: 4px;">Low Stock</span>
                                                    </c:when>
                                                </c:choose>
                                            </td>
                                            <td>
                                                <span class="status-badge ${p.status ? 'status-active' : 'status-inactive'}">
                                                    ${p.status ? 'Active' : 'Draft'}
                                                </span>
                                            </td>
                                            <td style="text-align: right;">
                                                <div style="display: inline-flex; gap: 6px;">
                                                    <button type="button" class="admin-btn admin-btn-outline admin-btn-sm" 
                                                            onclick="openEditModal(${p.id}, '${p.name}', '${p.brand}', ${p.categoryId}, '${p.price}', '${p.discountPercent}', ${p.stockQuantity}, '${p.imageUrl}', '${p.status}')">
                                                        Edit
                                                    </button>
                                                    <form action="${pageContext.request.contextPath}/admin/products" method="post" onsubmit="return confirm('Are you sure you want to delete this product?');" style="display: inline;">
                                                        <input type="hidden" name="action" value="delete" />
                                                        <input type="hidden" name="id" value="${p.id}" />
                                                        <button type="submit" class="admin-btn admin-btn-danger admin-btn-sm">Delete</button>
                                                    </form>
                                                </div>
                                            </td>
                                        </tr>
                                    </c:forEach>
                                </c:otherwise>
                            </c:choose>
                        </tbody>
                    </table>
                </div>
            </div>
        </main>
    </div>

    <!-- Product Add/Edit Modal -->
    <div id="productModal" class="modal-overlay">
        <div class="modal-content">
            <div class="modal-header">
                <h2 id="modalTitle" style="font-size: 1.15rem; color: #1e293b;">Add New Product</h2>
                <button type="button" onclick="closeModal()" style="background: none; border: none; font-size: 1.4rem; cursor: pointer; color: #94a3b8;">&times;</button>
            </div>
            <form id="productForm" action="${pageContext.request.contextPath}/admin/products" method="post">
                <input type="hidden" name="action" id="formAction" value="add" />
                <input type="hidden" name="id" id="productId" value="" />

                <div class="form-grid">
                    <div class="form-group-full">
                        <label class="form-label">Product Name *</label>
                        <input type="text" name="name" id="productName" class="form-input" required placeholder="e.g. Apple Watch Series 8" />
                    </div>

                    <div>
                        <label class="form-label">Category *</label>
                        <select name="categoryId" id="productCategory" class="form-select" required>
                            <c:forEach items="${categories}" var="c">
                                <option value="${c.id}"><c:out value="${c.name}" /></option>
                            </c:forEach>
                        </select>
                    </div>

                    <div>
                        <label class="form-label">Brand</label>
                        <input type="text" name="brand" id="productBrand" class="form-input" placeholder="e.g. Apple, Sony, JBL" />
                    </div>

                    <div>
                        <label class="form-label">Price ($) *</label>
                        <input type="number" step="0.01" name="price" id="productPrice" class="form-input" required placeholder="299.99" />
                    </div>

                    <div>
                        <label class="form-label">Discount (%)</label>
                        <input type="number" step="0.01" name="discountPercent" id="productDiscount" class="form-input" placeholder="0.00" value="0.00" />
                    </div>

                    <div>
                        <label class="form-label">Stock Quantity *</label>
                        <input type="number" name="stockQuantity" id="productStock" class="form-input" required placeholder="50" value="50" />
                    </div>

                    <div>
                        <label class="form-label">Status</label>
                        <select name="status" id="productStatus" class="form-select">
                            <option value="true">Active (Published)</option>
                            <option value="false">Inactive (Draft)</option>
                        </select>
                    </div>

                    <div class="form-group-full">
                        <label class="form-label">Image URL</label>
                        <input type="url" name="imageUrl" id="productImage" class="form-input" placeholder="https://images.unsplash.com/photo-..." />
                    </div>

                    <div class="form-group-full">
                        <label class="form-label">Description</label>
                        <textarea name="description" id="productDesc" rows="3" class="form-textarea" placeholder="Product details, specs, and features..."></textarea>
                    </div>
                </div>

                <div style="display: flex; justify-content: flex-end; gap: 10px; margin-top: 24px;">
                    <button type="button" onclick="closeModal()" class="admin-btn admin-btn-outline">Cancel</button>
                    <button type="submit" class="admin-btn admin-btn-primary">Save Product</button>
                </div>
            </form>
        </div>
    </div>

    <script src="${pageContext.request.contextPath}/js/admin.js"></script>
    <script>
        const modal = document.getElementById("productModal");
        function openAddModal() {
            document.getElementById("modalTitle").innerText = "Add New Product";
            document.getElementById("formAction").value = "add";
            document.getElementById("productId").value = "";
            document.getElementById("productName").value = "";
            document.getElementById("productBrand").value = "";
            document.getElementById("productPrice").value = "";
            document.getElementById("productDiscount").value = "0.00";
            document.getElementById("productStock").value = "50";
            document.getElementById("productImage").value = "";
            document.getElementById("productDesc").value = "";
            modal.classList.add("open");
        }

        function openEditModal(id, name, brand, catId, price, discount, stock, img, status) {
            document.getElementById("modalTitle").innerText = "Edit Product #" + id;
            document.getElementById("formAction").value = "edit";
            document.getElementById("productId").value = id;
            document.getElementById("productName").value = name;
            document.getElementById("productBrand").value = brand;
            document.getElementById("productCategory").value = catId;
            document.getElementById("productPrice").value = price;
            document.getElementById("productDiscount").value = discount;
            document.getElementById("productStock").value = stock;
            document.getElementById("productImage").value = img;
            document.getElementById("productStatus").value = status;
            modal.classList.add("open");
        }

        function closeModal() {
            modal.classList.remove("open");
        }
    </script>
</body>
</html>
