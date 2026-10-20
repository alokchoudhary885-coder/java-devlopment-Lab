<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Order Management — ShopSphere Admin</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/admin.css">
    <style>
        .filter-tabs {
            display: flex;
            gap: 8px;
            margin-bottom: 20px;
            flex-wrap: wrap;
        }
        .filter-tab {
            padding: 8px 16px;
            border-radius: 8px;
            font-size: 0.82rem;
            font-weight: 600;
            text-decoration: none;
            color: #64748b;
            background: #ffffff;
            border: 1px solid var(--admin-border);
            display: inline-flex;
            align-items: center;
            gap: 6px;
            transition: all 0.2s ease;
        }
        .filter-tab:hover {
            border-color: var(--admin-primary);
            color: var(--admin-primary);
        }
        .filter-tab.active {
            background-color: var(--admin-primary);
            color: #ffffff;
            border-color: var(--admin-primary);
        }
        .tab-badge {
            background: rgba(0, 0, 0, 0.08);
            padding: 2px 6px;
            border-radius: 10px;
            font-size: 0.72rem;
        }
        .filter-tab.active .tab-badge {
            background: rgba(255, 255, 255, 0.25);
            color: #ffffff;
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
                    <h1>Order Fulfillment & Status Workflow</h1>
                    <div class="admin-breadcrumb">
                        <span>orders</span> / <a href="${pageContext.request.contextPath}/admin/orders">orders</a>
                    </div>
                </div>

                <div class="admin-header-controls">
                    <span style="font-size: 0.88rem; font-weight: 600; color: #1e293b;">
                        Total Revenue: <strong style="color: #16a34a;">$<fmt:formatNumber value="${totalRevenue}" pattern="#,##0.00" /></strong>
                    </span>
                </div>
            </div>

            <!-- Flash Alert -->
            <c:if test="${not empty flashMessage}">
                <div class="alert-box alert-${flashType}">
                    <c:out value="${flashMessage}" />
                </div>
            </c:if>

            <!-- Status Filter Tabs -->
            <div class="filter-tabs">
                <a href="${pageContext.request.contextPath}/admin/orders" class="filter-tab ${statusFilter == 'ALL' ? 'active' : ''}">
                    All Orders <span class="tab-badge">${totalCount}</span>
                </a>
                <a href="${pageContext.request.contextPath}/admin/orders?status=PENDING" class="filter-tab ${statusFilter == 'PENDING' ? 'active' : ''}">
                    Pending <span class="tab-badge">${pendingCount}</span>
                </a>
                <a href="${pageContext.request.contextPath}/admin/orders?status=PROCESSING" class="filter-tab ${statusFilter == 'PROCESSING' ? 'active' : ''}">
                    Processing <span class="tab-badge">${processingCount}</span>
                </a>
                <a href="${pageContext.request.contextPath}/admin/orders?status=SHIPPED" class="filter-tab ${statusFilter == 'SHIPPED' ? 'active' : ''}">
                    Ready for Delivery (Shipped) <span class="tab-badge">${shippedCount}</span>
                </a>
                <a href="${pageContext.request.contextPath}/admin/orders?status=DELIVERED" class="filter-tab ${statusFilter == 'DELIVERED' ? 'active' : ''}">
                    Delivered <span class="tab-badge">${deliveredCount}</span>
                </a>
                <a href="${pageContext.request.contextPath}/admin/orders?status=CANCELLED" class="filter-tab ${statusFilter == 'CANCELLED' ? 'active' : ''}">
                    Cancelled <span class="tab-badge">${cancelledCount}</span>
                </a>
            </div>

            <!-- Orders Table -->
            <div class="admin-card">
                <table class="admin-data-table">
                    <thead>
                        <tr>
                            <th>Order Reference</th>
                            <th>Customer</th>
                            <th>Date</th>
                            <th>Amount</th>
                            <th>Payment</th>
                            <th>Fulfillment Status</th>
                            <th style="text-align: right;">Action</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:choose>
                            <c:when test="${empty orders}">
                                <tr>
                                    <td colspan="7" style="text-align: center; padding: 40px; color: #94a3b8;">
                                        No orders found in this category.
                                    </td>
                                </tr>
                            </c:when>
                            <c:otherwise>
                                <c:forEach items="${orders}" var="o">
                                    <tr>
                                        <td>
                                            <a href="${pageContext.request.contextPath}/order-details?id=${o.id}" target="_blank" style="font-weight: 700; color: var(--admin-primary); text-decoration: none;">
                                                <c:out value="${o.orderNumber}" />
                                            </a>
                                        </td>
                                        <td>
                                            <div style="font-weight: 600; color: #1e293b;"><c:out value="${o.customerName != null ? o.customerName : 'Customer #' += o.userId}" /></div>
                                            <div style="font-size: 0.75rem; color: #64748b;"><c:out value="${o.customerEmail}" /></div>
                                        </td>
                                        <td>
                                            <fmt:formatDate value="${o.createdAt}" pattern="MMM dd, yyyy HH:mm" />
                                        </td>
                                        <td>
                                            <strong>$<fmt:formatNumber value="${o.finalAmount}" pattern="#,##0.00" /></strong>
                                        </td>
                                        <td>
                                            <form action="${pageContext.request.contextPath}/admin/orders" method="post" style="display: inline-flex; align-items: center; gap: 4px;">
                                                <input type="hidden" name="action" value="updatePayment" />
                                                <input type="hidden" name="orderId" value="${o.id}" />
                                                <select name="paymentStatus" onchange="this.form.submit()" style="padding: 3px 6px; border-radius: 4px; font-size: 0.75rem; border: 1px solid #cbd5e1; font-weight: 600;">
                                                    <option value="PAID" ${o.paymentStatus == 'PAID' ? 'selected' : ''}>PAID</option>
                                                    <option value="UNPAID" ${o.paymentStatus == 'UNPAID' ? 'selected' : ''}>UNPAID</option>
                                                    <option value="REFUNDED" ${o.paymentStatus == 'REFUNDED' ? 'selected' : ''}>REFUNDED</option>
                                                </select>
                                            </form>
                                        </td>
                                        <td>
                                            <form action="${pageContext.request.contextPath}/admin/orders" method="post" style="display: inline-flex; align-items: center; gap: 4px;">
                                                <input type="hidden" name="action" value="updateStatus" />
                                                <input type="hidden" name="orderId" value="${o.id}" />
                                                <select name="status" onchange="this.form.submit()" style="padding: 3px 6px; border-radius: 4px; font-size: 0.75rem; border: 1px solid #cbd5e1; font-weight: 600;">
                                                    <option value="PENDING" ${o.status == 'PENDING' ? 'selected' : ''}>PENDING</option>
                                                    <option value="PROCESSING" ${o.status == 'PROCESSING' ? 'selected' : ''}>PROCESSING</option>
                                                    <option value="SHIPPED" ${o.status == 'SHIPPED' ? 'selected' : ''}>SHIPPED</option>
                                                    <option value="DELIVERED" ${o.status == 'DELIVERED' ? 'selected' : ''}>DELIVERED</option>
                                                    <option value="CANCELLED" ${o.status == 'CANCELLED' ? 'selected' : ''}>CANCELLED</option>
                                                </select>
                                            </form>
                                        </td>
                                        <td style="text-align: right;">
                                            <a href="${pageContext.request.contextPath}/order-details?id=${o.id}" class="admin-btn admin-btn-outline admin-btn-sm" target="_blank">
                                                Invoice ↗
                                            </a>
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
