<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<!-- ShopSphere Admin Sidebar (Screenshot 5 Faithful) -->
<aside class="admin-sidebar">
    <div class="admin-sidebar-header">
        <a href="${pageContext.request.contextPath}/admin/dashboard" class="admin-sidebar-brand">
            <svg width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="#00d27a" stroke-width="2.5" stroke-linecap="round" stroke-linejoin="round">
                <path d="M6 2L3 6v14a2 2 0 0 0 2 2h14a2 2 0 0 0 2-2V6l-3-2z"></path>
                <line x1="3" y1="6" x2="21" y2="6"></line>
                <path d="M16 10a4 4 0 0 1-8 0"></path>
            </svg>
            <span>ShopSphere</span>
        </a>
        <button class="sidebar-collapse-btn" title="Toggle Navigation">
            <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                <polyline points="11 17 6 12 11 7"></polyline>
                <polyline points="18 17 13 12 18 7"></polyline>
            </svg>
        </button>
    </div>

    <div class="admin-sidebar-nav">
        <!-- Section: DASHBOARD -->
        <div class="nav-section-title">Dashboard</div>
        <a href="${pageContext.request.contextPath}/admin/dashboard" class="admin-nav-item ${activeNav == 'dashboard' ? 'active' : ''}">
            <div class="nav-link-left">
                <span class="nav-icon">
                    <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                        <rect x="3" y="3" width="7" height="7"></rect>
                        <rect x="14" y="3" width="7" height="7"></rect>
                        <rect x="14" y="14" width="7" height="7"></rect>
                        <rect x="3" y="14" width="7" height="7"></rect>
                    </svg>
                </span>
                <span>Dashboard</span>
            </div>
            <span class="nav-arrow">›</span>
        </a>

        <!-- Section: POINT OF SALE -->
        <div class="nav-section-title">Point of Sale</div>
        <a href="${pageContext.request.contextPath}/admin/dashboard" class="admin-nav-item ${activeNav == 'pos' ? 'active' : ''}">
            <div class="nav-link-left">
                <span class="nav-icon">
                    <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                        <rect x="2" y="3" width="20" height="14" rx="2" ry="2"></rect>
                        <line x1="8" y1="21" x2="16" y2="21"></line>
                        <line x1="12" y1="17" x2="12" y2="21"></line>
                    </svg>
                </span>
                <span>POS</span>
            </div>
            <span class="nav-arrow">›</span>
        </a>

        <!-- Section: ORDER MANAGEMENT -->
        <div class="nav-section-title">Order Management</div>
        <a href="${pageContext.request.contextPath}/admin/orders" class="admin-nav-item ${activeNav == 'orders' ? 'active' : ''}">
            <div class="nav-link-left">
                <span class="nav-icon">
                    <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                        <polyline points="21 8 21 21 3 21 3 8"></polyline>
                        <rect x="1" y="3" width="22" height="5"></rect>
                        <line x1="10" y1="12" x2="14" y2="12"></line>
                    </svg>
                </span>
                <span>Orders</span>
            </div>
            <span class="nav-arrow">›</span>
        </a>

        <!-- Section: ITEM MANAGEMENT -->
        <div class="nav-section-title">Item Management</div>
        <a href="${pageContext.request.contextPath}/admin/products" class="admin-nav-item ${activeNav == 'items' ? 'active' : ''}">
            <div class="nav-link-left">
                <span class="nav-icon">
                    <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                        <path d="M20.59 13.41l-7.17 7.17a2 2 0 0 1-2.83 0L2 12V2h10l8.59 8.59a2 2 0 0 1 0 2.82z"></path>
                        <line x1="7" y1="7" x2="7.01" y2="7"></line>
                    </svg>
                </span>
                <span>Items (Products)</span>
            </div>
            <span class="nav-arrow">›</span>
        </a>
        <a href="${pageContext.request.contextPath}/admin/categories" class="admin-nav-item ${activeNav == 'categories' ? 'active' : ''}">
            <div class="nav-link-left">
                <span class="nav-icon">
                    <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                        <circle cx="12" cy="12" r="3"></circle>
                        <path d="M19.4 15a1.65 1.65 0 0 0 .33 1.82l.06.06a2 2 0 0 1 0 2.83 2 2 0 0 1-2.83 0l-.06-.06a1.65 1.65 0 0 0-1.82-.33 1.65 1.65 0 0 0-1 1.51V21a2 2 0 0 1-2 2 2 2 0 0 1-2-2v-.09A1.65 1.65 0 0 0 9 19.4a1.65 1.65 0 0 0-1.82.33l-.06.06a2 2 0 0 1-2.83 0 2 2 0 0 1 0-2.83l.06-.06a1.65 1.65 0 0 0 .33-1.82 1.65 1.65 0 0 0-1.51-1H3a2 2 0 0 1-2-2 2 2 0 0 1 2-2h.09A1.65 1.65 0 0 0 4.6 9a1.65 1.65 0 0 0-.33-1.82l-.06-.06a2 2 0 0 1 0-2.83 2 2 0 0 1 2.83 0l.06.06a1.65 1.65 0 0 0 1.82.33H9a1.65 1.65 0 0 0 1-1.51V3a2 2 0 0 1 2-2 2 2 0 0 1 2 2v.09a1.65 1.65 0 0 0 1 1.51 1.65 1.65 0 0 0 1.82-.33l.06-.06a2 2 0 0 1 2.83 0 2 2 0 0 1 0 2.83l-.06.06a1.65 1.65 0 0 0-.33 1.82V9a1.65 1.65 0 0 0 1.51 1H21a2 2 0 0 1 2 2 2 2 0 0 1-2 2h-.09a1.65 1.65 0 0 0-1.51 1z"></path>
                    </svg>
                </span>
                <span>Categories</span>
            </div>
            <span class="nav-arrow">›</span>
        </a>

        <!-- Section: MARKETING SECTION -->
        <div class="nav-section-title">Marketing Section</div>
        <a href="${pageContext.request.contextPath}/admin/coupons" class="admin-nav-item ${activeNav == 'coupons' ? 'active' : ''}">
            <div class="nav-link-left">
                <span class="nav-icon">
                    <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                        <path d="M2 9a3 3 0 0 1 0 6v2a2 2 0 0 0 2 2h16a2 2 0 0 0 2-2v-2a3 3 0 0 1 0-6V7a2 2 0 0 0-2-2H4a2 2 0 0 0-2 2z"></path>
                    </svg>
                </span>
                <span>Coupons</span>
            </div>
            <span class="nav-arrow">›</span>
        </a>

        <!-- Section: USER & EMPLOYEE MANAGEMENT -->
        <div class="nav-section-title">User & Role Management</div>
        <a href="${pageContext.request.contextPath}/admin/users" class="admin-nav-item ${activeNav == 'users' ? 'active' : ''}">
            <div class="nav-link-left">
                <span class="nav-icon">
                    <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                        <path d="M17 21v-2a4 4 0 0 0-4-4H5a4 4 0 0 0-4 4v2"></path>
                        <circle cx="9" cy="7" r="4"></circle>
                        <path d="M23 21v-2a4 4 0 0 0-3-3.87"></path>
                        <path d="M16 3.13a4 4 0 0 1 0 7.75"></path>
                    </svg>
                </span>
                <span>Customer Directory</span>
            </div>
            <span class="nav-arrow">›</span>
        </a>

        <!-- Section: STORE NAVIGATION -->
        <div class="nav-section-title">Storefront</div>
        <a href="${pageContext.request.contextPath}/home" class="admin-nav-item" target="_blank">
            <div class="nav-link-left">
                <span class="nav-icon">
                    <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                        <path d="M18 13v6a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2V8a2 2 0 0 1 2-2h6"></path>
                        <polyline points="15 3 21 3 21 9"></polyline>
                        <line x1="10" y1="14" x2="21" y2="3"></line>
                    </svg>
                </span>
                <span>View Live Store ↗</span>
            </div>
        </a>
        <a href="${pageContext.request.contextPath}/logout" class="admin-nav-item">
            <div class="nav-link-left">
                <span class="nav-icon">
                    <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="#ef4444" stroke-width="2">
                        <path d="M9 21H5a2 2 0 0 1-2-2V5a2 2 0 0 1 2-2h4"></path>
                        <polyline points="16 17 21 12 16 7"></polyline>
                        <line x1="21" y1="12" x2="9" y2="12"></line>
                    </svg>
                </span>
                <span style="color: #ef4444;">Sign Out</span>
            </div>
        </a>
    </div>
</aside>
