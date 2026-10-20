<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<!-- ShopSphere Admin Header (Screenshot 5 Faithful) -->
<header class="admin-header">
    <div class="admin-search-box">
        <span class="admin-search-icon">
            <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                <circle cx="11" cy="11" r="8"></circle>
                <line x1="21" y1="21" x2="16.65" y2="16.65"></line>
            </svg>
        </span>
        <input type="text" placeholder="Type to search..." />
    </div>

    <div class="admin-header-actions">
        <button class="header-icon-btn" title="Notifications">
            <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                <path d="M18 8A6 6 0 0 0 6 8c0 7-3 9-3 9h18s-3-2-3-9"></path>
                <path d="M13.73 21a2 2 0 0 1-3.46 0"></path>
            </svg>
            <span class="badge-dot"></span>
        </button>

        <button class="header-icon-btn" title="Messages">
            <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                <path d="M21 15a2 2 0 0 1-2 2H7l-4 4V5a2 2 0 0 1 2-2h14a2 2 0 0 1 2 2z"></path>
            </svg>
        </button>

        <div class="lang-selector">
            <span>En</span>
            <svg width="12" height="12" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                <polyline points="6 9 12 15 18 9"></polyline>
            </svg>
        </div>

        <div class="admin-profile-pill">
            <img src="https://images.unsplash.com/photo-1534528741775-53994a69daeb?w=100&q=80" alt="Super Admin" class="admin-avatar" />
            <div class="admin-profile-info">
                <span class="admin-profile-name">
                    <c:choose>
                        <c:when test="${not empty sessionScope.user.fullName}">
                            <c:out value="${sessionScope.user.fullName}" />
                        </c:when>
                        <c:otherwise>Cyndy Lillibridge</c:otherwise>
                    </c:choose>
                </span>
                <span class="admin-profile-role">Super Admin</span>
            </div>
            <svg width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                <polyline points="6 9 12 15 18 9"></polyline>
            </svg>
        </div>
    </div>
</header>
