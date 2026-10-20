<%@ tag description="Nordic Retail Luxe Product Card" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ attribute name="product" required="true" type="com.shopsphere.model.Product" %>

<div class="bg-surface-container-lowest rounded-xl p-space-md flex flex-col justify-between shadow-sm hover:shadow-md transition-all duration-300 group border border-surface-container">
    <div>
        <!-- Media Container -->
        <div class="relative w-full aspect-square rounded-lg overflow-hidden bg-surface-container-low mb-space-sm">
            <a href="${pageContext.request.contextPath}/product-details?id=${product.id}">
                <c:set var="imgSrc" value="${product.imageUrl}" />
                <c:if test="${not imgSrc.startsWith('http')}">
                    <c:set var="imgSrc" value="${pageContext.request.contextPath}/${product.imageUrl}" />
                </c:if>
                <img class="w-full h-full object-cover transition-transform duration-500 group-hover:scale-105" 
                     src="${imgSrc}" 
                     alt="${product.name}" 
                     loading="lazy"
                     onerror="this.onerror=null; this.src='https://placehold.co/400x400/f8f9ff/0b1c30?text=${product.brand}';"/>
            </a>
            
            <!-- Quick Wishlist Button -->
            <button aria-label="Add to wishlist" 
                    type="button"
                    onclick="toggleWishlist(${product.id}, this)"
                    class="absolute top-2.5 right-2.5 w-8 h-8 rounded-full bg-surface-container-lowest/90 backdrop-blur-sm text-on-surface hover:text-secondary flex items-center justify-center shadow-sm transition-transform active:scale-90 cursor-pointer">
                <span class="material-symbols-outlined text-base">favorite</span>
            </button>
            
            <!-- Atelier Brand Pill -->
            <div class="absolute bottom-2.5 left-2.5">
                <span class="bg-primary text-on-primary text-[10px] font-bold px-2 py-0.5 rounded-full shadow-xs">
                    ${product.brand}
                </span>
            </div>
        </div>

        <!-- Meta Line: Category & Star Rating -->
        <div class="flex items-center justify-between mb-1">
            <span class="text-[11px] font-bold text-secondary uppercase tracking-wider">
                ${not empty product.categoryName ? product.categoryName : 'Exclusive'}
            </span>
            <div class="flex items-center gap-1 text-[11px] font-bold text-on-surface">
                <span class="material-symbols-outlined text-xs text-amber-500" style="font-variation-settings: 'FILL' 1;">star</span>
                <span>${product.rating != null ? product.rating : '4.8'}</span>
                <span class="text-outline font-normal">(${product.reviewCount != null ? product.reviewCount : '42'})</span>
            </div>
        </div>

        <!-- Title -->
        <h3 class="font-headline-sm text-sm font-bold text-on-surface line-clamp-1 mb-2">
            <a href="${pageContext.request.contextPath}/product-details?id=${product.id}" class="hover:text-secondary transition-colors" title="${product.name}">
                ${product.name}
            </a>
        </h3>

        <!-- Price & Discounts -->
        <div class="flex items-baseline gap-2 mb-space-md">
            <span class="font-price-lg text-base font-bold text-on-surface">₹${product.discountedPrice}</span>
            <c:if test="${product.hasDiscount()}">
                <span class="text-xs text-outline line-through">₹${product.price}</span>
                <span class="bg-surface-container-highest text-on-surface px-1.5 py-0.5 rounded text-[10px] font-bold">
                    ${product.discountPercent}% off
                </span>
            </c:if>
        </div>
    </div>

    <!-- Action Buttons -->
    <div class="grid grid-cols-2 gap-2 mt-auto">
        <button type="button" 
                onclick="addToCart(${product.id}, 1)" 
                ${!product.inStock ? 'disabled' : ''}
                class="w-full inline-flex items-center justify-center gap-1 bg-primary text-on-primary hover:bg-secondary py-2.5 rounded-lg text-xs font-semibold transition-colors duration-200 cursor-pointer disabled:opacity-50">
            <span class="material-symbols-outlined text-sm">shopping_bag</span>
            <span>Add to Bag</span>
        </button>
        <a href="${pageContext.request.contextPath}/checkout?buyNowId=${product.id}&qty=1" 
           class="w-full inline-flex items-center justify-center gap-1 bg-surface-container-high hover:bg-secondary hover:text-on-secondary text-on-surface py-2.5 rounded-lg text-xs font-semibold transition-colors duration-200 text-center ${!product.inStock ? 'pointer-events-none opacity-50' : ''}">
            <span>Buy Now</span>
        </a>
    </div>
</div>
