<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<!-- Nordic Retail Luxe Main Navbar -->
<header class="sticky top-0 z-50 bg-surface-container-lowest/95 backdrop-blur-md shadow-[0_1px_8px_rgba(0,0,0,0.04)] border-b border-surface-container">
    <div class="max-w-[1440px] mx-auto px-gutter h-16 flex items-center justify-between gap-space-lg">
        
        <!-- Brand Logo -->
        <a href="${pageContext.request.contextPath}/" class="flex items-center gap-2.5 text-decoration-none group">
            <div class="w-9 h-9 rounded-lg bg-primary text-on-primary flex items-center justify-center font-bold text-lg tracking-wider group-hover:bg-secondary transition-colors">
                S
            </div>
            <span class="font-headline-md text-xl font-bold tracking-tight text-on-surface">ShopSphere</span>
        </a>

        <!-- Search Bar with Department Selector -->
        <form action="${pageContext.request.contextPath}/products" method="GET" class="hidden lg:flex flex-1 max-w-xl items-center bg-surface-container-low rounded-lg px-space-sm py-1.5 gap-space-sm border border-outline-variant/30 focus-within:border-secondary transition-colors">
            <select name="category" class="bg-transparent text-xs font-semibold text-on-surface-variant outline-none cursor-pointer pr-space-xs">
                <option value="">All Departments</option>
                <c:forEach var="cat" items="${applicationScope.activeCategories != null ? applicationScope.activeCategories : categories}">
                    <option value="${cat.id}" ${param.category == cat.id ? 'selected' : ''}>${cat.name}</option>
                </c:forEach>
            </select>
            <div class="w-px h-5 bg-outline-variant/60"></div>
            <div class="flex flex-1 items-center gap-space-xs">
                <span class="material-symbols-outlined text-outline text-lg">search</span>
                <input class="w-full bg-transparent text-sm placeholder:text-outline focus:outline-none text-on-surface" 
                       name="q" value="${param.q}" placeholder="Search bespoke furniture, ambient lighting, acoustics..." type="text"/>
            </div>
            <button type="submit" class="hidden"></button>
        </form>

        <!-- Right Action Icons: Wishlist, Shopping Bag, Account -->
        <div class="flex items-center gap-space-md">
            
            <!-- Wishlist -->
            <a class="relative p-2 rounded-lg text-on-surface-variant hover:text-on-surface hover:bg-surface-container transition-colors" 
               href="${pageContext.request.contextPath}/wishlist" title="Saved Wishlist">
                <span class="material-symbols-outlined text-2xl">favorite</span>
                <c:if test="${sessionScope.wishlistCount > 0}">
                    <span class="absolute top-1 right-1 w-4 h-4 bg-secondary text-on-secondary rounded-full flex items-center justify-center text-[10px] font-bold">
                        ${sessionScope.wishlistCount}
                    </span>
                </c:if>
            </a>

            <!-- Shopping Bag -->
            <a class="relative p-2 rounded-lg text-on-surface-variant hover:text-on-surface hover:bg-surface-container transition-colors" 
               href="${pageContext.request.contextPath}/cart" title="Shopping Bag">
                <span class="material-symbols-outlined text-2xl">shopping_bag</span>
                <span class="absolute top-1 right-1 w-4 h-4 bg-primary text-on-primary rounded-full flex items-center justify-center text-[10px] font-bold" id="cartBadge">
                    ${sessionScope.cartCount != null ? sessionScope.cartCount : '0'}
                </span>
            </a>

            <!-- User Profile / Auth Actions -->
            <div class="flex items-center pl-space-xs">
                <c:choose>
                    <c:when test="${not empty sessionScope.user}">
                        <div class="relative group/user">
                            <button type="button" class="flex items-center gap-2 p-1.5 rounded-lg hover:bg-surface-container transition-colors cursor-pointer">
                                <div class="w-8 h-8 rounded-full bg-primary-container text-on-primary-container flex items-center justify-center font-bold text-xs uppercase">
                                    ${sessionScope.user.fullName.substring(0, 1)}
                                </div>
                                <span class="hidden sm:inline text-xs font-semibold text-on-surface max-w-[100px] truncate">
                                    ${sessionScope.user.fullName}
                                </span>
                                <span class="material-symbols-outlined text-sm text-outline">expand_more</span>
                            </button>
                            <!-- User Dropdown Menu -->
                            <div class="absolute right-0 mt-1 w-52 bg-surface-container-lowest rounded-xl shadow-lg border border-surface-container py-2 opacity-0 invisible group-hover/user:opacity-100 group-hover/user:visible transition-all duration-200 z-50">
                                <div class="px-3 py-2 border-b border-surface-container">
                                    <div class="font-bold text-xs text-on-surface truncate">${sessionScope.user.fullName}</div>
                                    <div class="text-[11px] text-outline truncate">${sessionScope.user.email}</div>
                                    <span class="inline-block mt-1 px-2 py-0.5 rounded text-[10px] font-bold ${sessionScope.user.role eq 'ADMIN' ? 'bg-error-container text-on-error-container' : 'bg-surface-container text-secondary'}">
                                        ${sessionScope.user.role}
                                    </span>
                                </div>
                                <c:if test="${sessionScope.user.role eq 'ADMIN'}">
                                    <a href="${pageContext.request.contextPath}/admin/dashboard" class="flex items-center gap-2 px-3 py-2 text-xs font-semibold text-secondary hover:bg-surface-container-low transition-colors">
                                        <span class="material-symbols-outlined text-sm">dashboard</span> Admin Console
                                    </a>
                                </c:if>
                                <a href="${pageContext.request.contextPath}/orders" class="flex items-center gap-2 px-3 py-2 text-xs text-on-surface-variant hover:text-on-surface hover:bg-surface-container-low transition-colors">
                                    <span class="material-symbols-outlined text-sm">receipt_long</span> My Orders
                                </a>
                                <a href="${pageContext.request.contextPath}/wishlist" class="flex items-center gap-2 px-3 py-2 text-xs text-on-surface-variant hover:text-on-surface hover:bg-surface-container-low transition-colors">
                                    <span class="material-symbols-outlined text-sm">bookmark</span> Saved Wishlist
                                </a>
                                <div class="h-px bg-surface-container my-1"></div>
                                <a href="${pageContext.request.contextPath}/logout" class="flex items-center gap-2 px-3 py-2 text-xs font-semibold text-error hover:bg-error-container/20 transition-colors">
                                    <span class="material-symbols-outlined text-sm">logout</span> Sign Out
                                </a>
                            </div>
                        </div>
                    </c:when>
                    <c:otherwise>
                        <a href="${pageContext.request.contextPath}/login" class="flex items-center gap-1.5 bg-primary text-on-primary hover:bg-secondary px-3.5 py-2 rounded-lg text-xs font-semibold shadow-sm transition-colors">
                            <span class="material-symbols-outlined text-base">login</span>
                            <span>Sign In</span>
                        </a>
                    </c:otherwise>
                </c:choose>
            </div>

        </div>
    </div>

    <!-- Category Strip -->
    <nav class="bg-surface-container-lowest border-t border-surface-container">
        <div class="max-w-[1440px] mx-auto px-gutter h-11 flex items-center gap-space-lg overflow-x-auto text-xs font-medium">
            <a class="px-3 py-1 rounded-lg ${pageContext.request.requestURI.endsWith('/index.jsp') or pageContext.request.servletPath eq '/' ? 'text-on-surface font-semibold bg-surface-container-high' : 'text-on-surface-variant hover:text-on-surface'} transition-colors whitespace-nowrap" 
               href="${pageContext.request.contextPath}/">Home</a>
            <a class="px-3 py-1 rounded-lg ${empty param.category and pageContext.request.servletPath eq '/products' ? 'text-on-surface font-semibold bg-surface-container-high' : 'text-on-surface-variant hover:text-on-surface'} transition-colors whitespace-nowrap" 
               href="${pageContext.request.contextPath}/products">All Curations</a>
            <a class="px-3 py-1 rounded-lg ${param.category eq '1' ? 'text-on-surface font-semibold bg-surface-container-high' : 'text-on-surface-variant hover:text-on-surface'} transition-colors whitespace-nowrap" 
               href="${pageContext.request.contextPath}/products?category=1">Living Room</a>
            <a class="px-3 py-1 rounded-lg ${param.category eq '2' ? 'text-on-surface font-semibold bg-surface-container-high' : 'text-on-surface-variant hover:text-on-surface'} transition-colors whitespace-nowrap" 
               href="${pageContext.request.contextPath}/products?category=2">Accessories</a>
            <a class="px-3 py-1 rounded-lg ${param.category eq '4' ? 'text-on-surface font-semibold bg-surface-container-high' : 'text-on-surface-variant hover:text-on-surface'} transition-colors whitespace-nowrap" 
               href="${pageContext.request.contextPath}/products?category=4">Electronics</a>
            <a class="px-3 py-1 rounded-lg text-on-surface-variant hover:text-on-surface transition-colors whitespace-nowrap" 
               href="${pageContext.request.contextPath}/products?category=3">Healthcare</a>
            
            <div class="ml-auto flex items-center">
                <a class="text-secondary hover:text-on-surface text-xs font-semibold px-3 py-1.5 transition-colors flex items-center gap-1 whitespace-nowrap" 
                   href="${pageContext.request.contextPath}/admin/dashboard">
                    <span class="material-symbols-outlined text-sm">tune</span>
                    <span>Admin Portal</span>
                </a>
            </div>
        </div>
    </nav>
</header>
