<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Coupons & Promotions — ShopSphere Admin</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/admin.css">
    <style>
        .modal-overlay {
            display: none; position: fixed; top: 0; left: 0; right: 0; bottom: 0;
            background: rgba(0, 0, 0, 0.5); z-index: 2000; align-items: center; justify-content: center;
        }
        .modal-overlay.open { display: flex; }
        .modal-content {
            background: #ffffff; border-radius: 12px; width: 100%; max-width: 500px;
            padding: 24px; box-shadow: 0 10px 25px rgba(0, 0, 0, 0.2);
        }
        .modal-header {
            display: flex; align-items: center; justify-content: space-between;
            margin-bottom: 20px; border-bottom: 1px solid #e2e8f0; padding-bottom: 12px;
        }
        .form-group { margin-bottom: 14px; }
        .form-label {
            display: block; font-size: 0.8rem; font-weight: 600; color: #475569; margin-bottom: 6px;
        }
        .form-input, .form-select {
            width: 100%; padding: 8px 12px; border-radius: 6px; border: 1px solid #cbd5e1; font-size: 0.85rem; outline: none;
        }
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
                    <h1>Promotional Coupon Engine</h1>
                    <div class="admin-breadcrumb">
                        <span>marketing</span> / <a href="${pageContext.request.contextPath}/admin/coupons">coupons</a>
                    </div>
                </div>

                <div class="admin-header-controls">
                    <button class="admin-btn admin-btn-primary" onclick="openAddModal()">
                        <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2.5">
                            <line x1="12" y1="5" x2="12" y2="19"></line>
                            <line x1="5" y1="12" x2="19" y2="12"></line>
                        </svg>
                        <span>Create Promo Coupon</span>
                    </button>
                </div>
            </div>

            <!-- Flash Alert -->
            <c:if test="${not empty flashMessage}">
                <div class="alert-box alert-${flashType}">
                    <c:out value="${flashMessage}" />
                </div>
            </c:if>

            <div class="admin-card">
                <table class="admin-data-table">
                    <thead>
                        <tr>
                            <th style="width: 50px;">ID</th>
                            <th>Promo Code</th>
                            <th>Discount (%)</th>
                            <th>Max Cap</th>
                            <th>Min Spend</th>
                            <th>Expiry Date</th>
                            <th>Status</th>
                            <th style="text-align: right;">Action</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:choose>
                            <c:when test="${empty coupons}">
                                <tr>
                                    <td colspan="8" style="text-align: center; padding: 40px; color: #94a3b8;">
                                        No promotional coupons configured yet.
                                    </td>
                                </tr>
                            </c:when>
                            <c:otherwise>
                                <c:forEach items="${coupons}" var="c">
                                    <tr>
                                        <td><strong>#<c:out value="${c.id}" /></strong></td>
                                        <td>
                                            <span style="font-family: monospace; font-size: 0.95rem; font-weight: 700; background: #e0f2fe; color: #0369a1; padding: 3px 8px; border-radius: 4px;">
                                                <c:out value="${c.code}" />
                                            </span>
                                        </td>
                                        <td>
                                            <strong style="color: #16a34a;"><fmt:formatNumber value="${c.discountPercent}" pattern="#,##0" />% OFF</strong>
                                        </td>
                                        <td>$<fmt:formatNumber value="${c.maxDiscount}" pattern="#,##0.00" /></td>
                                        <td>$<fmt:formatNumber value="${c.minOrderAmount}" pattern="#,##0.00" /></td>
                                        <td><c:out value="${c.expiryDate}" /></td>
                                        <td>
                                            <span class="status-badge ${c.status ? 'status-active' : 'status-inactive'}">
                                                ${c.status ? 'Active' : 'Disabled'}
                                            </span>
                                        </td>
                                        <td style="text-align: right;">
                                            <form action="${pageContext.request.contextPath}/admin/coupons" method="post" onsubmit="return confirm('Delete coupon ${c.code}?');" style="display: inline;">
                                                <input type="hidden" name="action" value="delete" />
                                                <input type="hidden" name="id" value="${c.id}" />
                                                <button type="submit" class="admin-btn admin-btn-danger admin-btn-sm">Delete</button>
                                            </form>
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

    <!-- Create Coupon Modal -->
    <div id="couponModal" class="modal-overlay">
        <div class="modal-content">
            <div class="modal-header">
                <h2 style="font-size: 1.15rem; color: #1e293b;">Create Promotional Coupon</h2>
                <button type="button" onclick="closeModal()" style="background: none; border: none; font-size: 1.4rem; cursor: pointer; color: #94a3b8;">&times;</button>
            </div>
            <form action="${pageContext.request.contextPath}/admin/coupons" method="post">
                <input type="hidden" name="action" value="add" />

                <div class="form-group">
                    <label class="form-label">Coupon Code *</label>
                    <input type="text" name="code" class="form-input" required placeholder="e.g. FESTIVE30" style="text-transform: uppercase;" />
                </div>

                <div class="form-group">
                    <label class="form-label">Discount Percentage (1-100%) *</label>
                    <input type="number" step="0.01" min="1" max="100" name="discountPercent" class="form-input" required placeholder="20.00" />
                </div>

                <div class="form-group">
                    <label class="form-label">Maximum Discount Cap ($) *</label>
                    <input type="number" step="0.01" min="1" name="maxDiscount" class="form-input" required placeholder="100.00" value="100.00" />
                </div>

                <div class="form-group">
                    <label class="form-label">Minimum Order Spend ($) *</label>
                    <input type="number" step="0.01" min="0" name="minOrderAmount" class="form-input" required placeholder="50.00" value="50.00" />
                </div>

                <div class="form-group">
                    <label class="form-label">Expiry Date *</label>
                    <input type="date" name="expiryDate" class="form-input" required value="2027-12-31" />
                </div>

                <div class="form-group">
                    <label class="form-label">Status</label>
                    <select name="status" class="form-select">
                        <option value="true">Active Immediately</option>
                        <option value="false">Inactive / Draft</option>
                    </select>
                </div>

                <div style="display: flex; justify-content: flex-end; gap: 10px; margin-top: 20px;">
                    <button type="button" onclick="closeModal()" class="admin-btn admin-btn-outline">Cancel</button>
                    <button type="submit" class="admin-btn admin-btn-primary">Create Coupon</button>
                </div>
            </form>
        </div>
    </div>

    <script src="${pageContext.request.contextPath}/js/admin.js"></script>
    <script>
        const modal = document.getElementById("couponModal");
        function openAddModal() { modal.classList.add("open"); }
        function closeModal() { modal.classList.remove("open"); }
    </script>
</body>
</html>
