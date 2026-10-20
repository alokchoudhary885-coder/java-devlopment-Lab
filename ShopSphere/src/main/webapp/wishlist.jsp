<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<c:set var="pageTitle" value="Saved Wishlist — ShopSphere | Nordic Retail Luxe" />
<%@ include file="/fragments/header.jsp" %>
<%@ include file="/fragments/navbar.jsp" %>

<main class="w-full bg-background">
    <div class="max-w-[1440px] w-full mx-auto px-gutter py-space-lg">
        
        <!-- Breadcrumb & Header Meta -->
        <div class="flex items-center gap-space-xs text-xs font-medium text-on-surface-variant mb-space-lg">
            <a class="hover:text-primary transition-colors flex items-center gap-1" href="${pageContext.request.contextPath}/">
                <span class="material-symbols-outlined text-sm">storefront</span>
                <span>Store</span>
            </a>
            <span class="material-symbols-outlined text-outline text-[16px]">chevron_right</span>
            <span class="text-on-surface font-semibold">Saved Curations &amp; Wishlist</span>
        </div>

        <div class="flex flex-col sm:flex-row justify-between sm:items-center gap-space-sm mb-space-lg">
            <div>
                <h1 class="text-2xl font-bold text-on-surface tracking-tight">Saved Wishlist</h1>
                <p class="text-xs text-on-surface-variant">Your personal curations synchronized across devices.</p>
            </div>
            <span class="bg-surface-container-high text-secondary px-3 py-1 rounded-full text-xs font-bold inline-flex items-center gap-1">
                <span class="material-symbols-outlined text-sm">favorite</span>
                <span>${wishlistItems != null ? wishlistItems.size() : (sessionScope.wishlistCount != null ? sessionScope.wishlistCount : 0)} Curations</span>
            </span>
        </div>

        <c:choose>
            <c:when test="${not empty wishlistItems}">
                <div class="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-space-md">
                    <c:forEach var="item" items="${wishlistItems}">
                        <div class="bg-surface-container-lowest rounded-xl p-space-md flex flex-col justify-between shadow-sm hover:shadow-md transition-all duration-300 group border border-surface-container relative">
                            
                            <!-- Delete Button -->
                            <form action="${pageContext.request.contextPath}/wishlist" method="POST" class="absolute top-2.5 right-2.5 z-10">
                                <input type="hidden" name="action" value="remove">
                                <input type="hidden" name="wishlistId" value="${item.id}">
                                <button type="submit" class="w-8 h-8 rounded-full bg-surface-container-lowest/90 backdrop-blur-sm text-outline hover:text-error flex items-center justify-center shadow-sm transition-transform active:scale-90 cursor-pointer" title="Remove from wishlist">
                                    <span class="material-symbols-outlined text-base">close</span>
                                </button>
                            </form>

                            <div>
                                <!-- Product Media -->
                                <div class="relative w-full aspect-square rounded-lg overflow-hidden bg-surface-container-low mb-space-sm">
                                    <a href="${pageContext.request.contextPath}/product-details?id=${item.productId}">
                                        <c:set var="wImg" value="${item.productImageUrl}" />
                                        <c:if test="${not wImg.startsWith('http')}">
                                            <c:set var="wImg" value="${pageContext.request.contextPath}/${item.productImageUrl}" />
                                        </c:if>
                                        <img class="w-full h-full object-cover transition-transform duration-500 group-hover:scale-105" 
                                             src="${wImg}" 
                                             alt="${item.productName}" 
                                             onerror="this.onerror=null; this.src='https://placehold.co/400x400/f8f9ff/0b1c30?text=Curated+Item';"/>
                                    </a>
                                </div>

                                <h3 class="font-headline-sm text-sm font-bold text-on-surface line-clamp-1 mb-1">
                                    <a href="${pageContext.request.contextPath}/product-details?id=${item.productId}" class="hover:text-secondary transition-colors">
                                        ${item.productName}
                                    </a>
                                </h3>

                                <div class="flex items-baseline gap-2 mb-space-md">
                                    <span class="font-price-lg text-base font-bold text-on-surface">₹${item.productPrice}</span>
                                </div>
                            </div>

                            <!-- Move to Cart Form -->
                            <form action="${pageContext.request.contextPath}/wishlist" method="POST" class="mt-auto">
                                <input type="hidden" name="action" value="moveToCart">
                                <input type="hidden" name="wishlistId" value="${item.id}">
                                <input type="hidden" name="productId" value="${item.productId}">
                                <button type="submit" class="w-full inline-flex items-center justify-center gap-1.5 bg-primary text-on-primary hover:bg-secondary py-2.5 rounded-lg text-xs font-semibold transition-colors duration-200 cursor-pointer">
                                    <span class="material-symbols-outlined text-sm">shopping_bag</span>
                                    <span>Move to Bag</span>
                                </button>
                            </form>
                        </div>
                    </c:forEach>
                </div>
            </c:when>

            <c:otherwise>
                <div class="bg-surface-container-lowest rounded-2xl p-space-xl text-center border border-surface-container shadow-sm my-space-lg">
                    <div class="w-16 h-16 rounded-full bg-surface-container mx-auto flex items-center justify-center text-outline mb-space-md">
                        <span class="material-symbols-outlined text-3xl">bookmark_border</span>
                    </div>
                    <h3 class="text-base font-bold text-on-surface mb-1">Your Wishlist is Empty</h3>
                    <p class="text-xs text-on-surface-variant max-w-sm mx-auto mb-space-lg">
                        Browse our bespoke catalog and tap the heart icon on any curation to preserve it here.
                    </p>
                    <a href="${pageContext.request.contextPath}/products" class="inline-flex items-center gap-2 bg-primary text-on-primary hover:bg-secondary px-5 py-2.5 rounded-lg text-xs font-semibold shadow-sm transition-colors">
                        <span>Browse Curations</span>
                        <span class="material-symbols-outlined text-sm">arrow_forward</span>
                    </a>
                </div>
            </c:otherwise>
        </c:choose>

    </div>
</main>

<%@ include file="/fragments/footer.jsp" %>
