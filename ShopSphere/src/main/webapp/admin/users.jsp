<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Customer Directory — ShopSphere Admin</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/admin.css">
    <style>
        .filter-tabs { display: flex; gap: 8px; margin-bottom: 20px; }
        .filter-tab {
            padding: 8px 16px; border-radius: 8px; font-size: 0.82rem; font-weight: 600;
            text-decoration: none; color: #64748b; background: #ffffff; border: 1px solid var(--admin-border);
        }
        .filter-tab.active { background-color: var(--admin-primary); color: #ffffff; border-color: var(--admin-primary); }
        .alert-box {
            padding: 12px 16px; border-radius: 8px; margin-bottom: 20px; font-size: 0.85rem; font-weight: 500;
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
                    <h1>Customer Directory & Access Control</h1>
                    <div class="admin-breadcrumb">
                        <span>users</span> / <a href="${pageContext.request.contextPath}/admin/users">directory</a>
                    </div>
                </div>
            </div>

            <!-- Flash Alert -->
            <c:if test="${not empty flashMessage}">
                <div class="alert-box alert-${flashType}">
                    <c:out value="${flashMessage}" />
                </div>
            </c:if>

            <!-- Role Tabs -->
            <div class="filter-tabs">
                <a href="${pageContext.request.contextPath}/admin/users" class="filter-tab ${roleFilter == 'ALL' ? 'active' : ''}">All Accounts</a>
                <a href="${pageContext.request.contextPath}/admin/users?role=CUSTOMER" class="filter-tab ${roleFilter == 'CUSTOMER' ? 'active' : ''}">Customers</a>
                <a href="${pageContext.request.contextPath}/admin/users?role=ADMIN" class="filter-tab ${roleFilter == 'ADMIN' ? 'active' : ''}">Administrators</a>
            </div>

            <!-- Users Table -->
            <div class="admin-card">
                <table class="admin-data-table">
                    <thead>
                        <tr>
                            <th style="width: 60px;">ID</th>
                            <th>Name</th>
                            <th>Email Address</th>
                            <th>Phone</th>
                            <th>Role</th>
                            <th>Status</th>
                            <th>Registered At</th>
                            <th style="text-align: right;">Action</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:choose>
                            <c:when test="${empty users}">
                                <tr>
                                    <td colspan="8" style="text-align: center; padding: 40px; color: #94a3b8;">
                                        No registered user accounts found.
                                    </td>
                                </tr>
                            </c:when>
                            <c:otherwise>
                                <c:forEach items="${users}" var="u">
                                    <tr>
                                        <td><strong>#<c:out value="${u.id}" /></strong></td>
                                        <td>
                                            <div style="font-weight: 600; color: #1e293b;"><c:out value="${u.fullName}" /></div>
                                        </td>
                                        <td><c:out value="${u.email}" /></td>
                                        <td><c:out value="${not empty u.phone ? u.phone : '—'}" /></td>
                                        <td>
                                            <span class="status-badge ${u.role == 'ADMIN' ? 'status-processing' : 'status-inactive'}">
                                                <c:out value="${u.role}" />
                                            </span>
                                        </td>
                                        <td>
                                            <span class="status-badge ${u.status == 'ACTIVE' ? 'status-active' : 'status-cancelled'}">
                                                <c:out value="${u.status}" />
                                            </span>
                                        </td>
                                        <td>
                                            <fmt:formatDate value="${u.createdAt}" pattern="MMM dd, yyyy" />
                                        </td>
                                        <td style="text-align: right;">
                                            <c:if test="${u.id != sessionScope.user.id}">
                                                <form action="${pageContext.request.contextPath}/admin/users" method="post" style="display: inline;">
                                                    <input type="hidden" name="action" value="toggleStatus" />
                                                    <input type="hidden" name="userId" value="${u.id}" />
                                                    <input type="hidden" name="status" value="${u.status == 'ACTIVE' ? 'SUSPENDED' : 'ACTIVE'}" />
                                                    <button type="submit" class="admin-btn ${u.status == 'ACTIVE' ? 'admin-btn-danger' : 'admin-btn-success'} admin-btn-sm">
                                                        ${u.status == 'ACTIVE' ? 'Suspend' : 'Activate'}
                                                    </button>
                                                </form>
                                            </c:if>
                                            <c:if test="${u.id == sessionScope.user.id}">
                                                <span style="font-size: 0.75rem; color: #94a3b8; font-weight: 600;">(You)</span>
                                            </c:if>
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

    <script src="${pageContext.request.contextPath}/js/admin.js"></script>
</body>
</html>
