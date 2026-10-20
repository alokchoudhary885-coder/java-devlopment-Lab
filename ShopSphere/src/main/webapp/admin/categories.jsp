<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Category Management — ShopSphere Admin</title>
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
            max-width: 500px;
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
        .form-group { margin-bottom: 14px; }
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
            <div class="admin-page-header">
                <div class="admin-title-area">
                    <h1>Category Management</h1>
                    <div class="admin-breadcrumb">
                        <span>items</span> / <a href="${pageContext.request.contextPath}/admin/categories">categories</a>
                    </div>
                </div>

                <div class="admin-header-controls">
                    <button class="admin-btn admin-btn-primary" onclick="openAddModal()">
                        <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2.5">
                            <line x1="12" y1="5" x2="12" y2="19"></line>
                            <line x1="5" y1="12" x2="19" y2="12"></line>
                        </svg>
                        <span>Add Category</span>
                    </button>
                </div>
            </div>

            <c:if test="${not empty flashMessage}">
                <div class="alert-box alert-${flashType}">
                    <c:out value="${flashMessage}" />
                </div>
            </c:if>

            <div class="admin-card">
                <table class="admin-data-table">
                    <thead>
                        <tr>
                            <th style="width: 60px;">ID</th>
                            <th>Category</th>
                            <th>Description</th>
                            <th>Icon / Image</th>
                            <th>Status</th>
                            <th style="text-align: right;">Actions</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:choose>
                            <c:when test="${empty categories}">
                                <tr>
                                    <td colspan="6" style="text-align: center; padding: 40px; color: #94a3b8;">
                                        No categories found.
                                    </td>
                                </tr>
                            </c:when>
                            <c:otherwise>
                                <c:forEach items="${categories}" var="c">
                                    <tr>
                                        <td><strong>#<c:out value="${c.id}" /></strong></td>
                                        <td><strong><c:out value="${c.name}" /></strong></td>
                                        <td><c:out value="${c.description}" /></td>
                                        <td>
                                            <span style="font-size: 1.2rem; margin-right: 6px;"><c:out value="${c.icon}" /></span>
                                            <c:if test="${not empty c.imageUrl}">
                                                <img src="${c.imageUrl}" alt="" style="width: 28px; height: 28px; border-radius: 4px; object-fit: cover; vertical-align: middle;" />
                                            </c:if>
                                        </td>
                                        <td>
                                            <span class="status-badge ${c.status ? 'status-active' : 'status-inactive'}">
                                                ${c.status ? 'Active' : 'Hidden'}
                                            </span>
                                        </td>
                                        <td style="text-align: right;">
                                            <div style="display: inline-flex; gap: 6px;">
                                                <button type="button" class="admin-btn admin-btn-outline admin-btn-sm"
                                                        onclick="openEditModal(${c.id}, '${c.name}', '${c.description}', '${c.icon}', '${c.imageUrl}', '${c.status}')">
                                                    Edit
                                                </button>
                                                <form action="${pageContext.request.contextPath}/admin/categories" method="post" onsubmit="return confirm('Delete this category? (Will fail if products are linked)');" style="display: inline;">
                                                    <input type="hidden" name="action" value="delete" />
                                                    <input type="hidden" name="id" value="${c.id}" />
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
        </main>
    </div>

    <!-- Category Modal -->
    <div id="categoryModal" class="modal-overlay">
        <div class="modal-content">
            <div class="modal-header">
                <h2 id="modalTitle" style="font-size: 1.15rem; color: #1e293b;">Add Category</h2>
                <button type="button" onclick="closeModal()" style="background: none; border: none; font-size: 1.4rem; cursor: pointer; color: #94a3b8;">&times;</button>
            </div>
            <form id="categoryForm" action="${pageContext.request.contextPath}/admin/categories" method="post">
                <input type="hidden" name="action" id="formAction" value="add" />
                <input type="hidden" name="id" id="categoryId" value="" />

                <div class="form-group">
                    <label class="form-label">Category Name *</label>
                    <input type="text" name="name" id="catName" class="form-input" required placeholder="e.g. Smart Electronics" />
                </div>

                <div class="form-group">
                    <label class="form-label">Description</label>
                    <textarea name="description" id="catDesc" rows="2" class="form-textarea" placeholder="Brief category description..."></textarea>
                </div>

                <div class="form-group">
                    <label class="form-label">Icon (Emoji or class)</label>
                    <input type="text" name="icon" id="catIcon" class="form-input" placeholder="⚡ or 📱" />
                </div>

                <div class="form-group">
                    <label class="form-label">Image URL</label>
                    <input type="url" name="imageUrl" id="catImage" class="form-input" placeholder="https://images.unsplash.com/..." />
                </div>

                <div class="form-group">
                    <label class="form-label">Status</label>
                    <select name="status" id="catStatus" class="form-select">
                        <option value="true">Active (Visible)</option>
                        <option value="false">Hidden</option>
                    </select>
                </div>

                <div style="display: flex; justify-content: flex-end; gap: 10px; margin-top: 20px;">
                    <button type="button" onclick="closeModal()" class="admin-btn admin-btn-outline">Cancel</button>
                    <button type="submit" class="admin-btn admin-btn-primary">Save Category</button>
                </div>
            </form>
        </div>
    </div>

    <script src="${pageContext.request.contextPath}/js/admin.js"></script>
    <script>
        const modal = document.getElementById("categoryModal");
        function openAddModal() {
            document.getElementById("modalTitle").innerText = "Add Category";
            document.getElementById("formAction").value = "add";
            document.getElementById("categoryId").value = "";
            document.getElementById("catName").value = "";
            document.getElementById("catDesc").value = "";
            document.getElementById("catIcon").value = "";
            document.getElementById("catImage").value = "";
            modal.classList.add("open");
        }

        function openEditModal(id, name, desc, icon, img, status) {
            document.getElementById("modalTitle").innerText = "Edit Category #" + id;
            document.getElementById("formAction").value = "edit";
            document.getElementById("categoryId").value = id;
            document.getElementById("catName").value = name;
            document.getElementById("catDesc").value = desc;
            document.getElementById("catIcon").value = icon;
            document.getElementById("catImage").value = img;
            document.getElementById("catStatus").value = status;
            modal.classList.add("open");
        }

        function closeModal() {
            modal.classList.remove("open");
        }
    </script>
</body>
</html>
