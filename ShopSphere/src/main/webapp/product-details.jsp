<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<c:set var="pageTitle" value="${product.name} — ShopSphere | Nordic Retail Luxe" />
<%@ include file="/fragments/header.jsp" %>
<%@ include file="/fragments/navbar.jsp" %>

<main class="w-full bg-background">

    <!-- Ambient Subtle Glow -->
    <div class="relative w-full overflow-hidden">
        <div class="max-w-[1440px] mx-auto px-gutter py-space-md">
            
            <!-- Breadcrumbs Bar -->
            <nav aria-label="Breadcrumb" class="flex items-center gap-space-xs text-xs font-medium text-on-surface-variant mb-space-lg">
                <a class="hover:text-on-surface transition-colors" href="${pageContext.request.contextPath}/">Home</a>
                <span class="text-outline-variant">/</span>
                <a class="hover:text-on-surface transition-colors" href="${pageContext.request.contextPath}/products">Catalog</a>
                <span class="text-outline-variant">/</span>
                <a class="hover:text-on-surface transition-colors" href="${pageContext.request.contextPath}/products?category=${product.categoryId}">
                    ${product.categoryName != null ? product.categoryName : 'Curations'}
                </a>
                <span class="text-outline-variant">/</span>
                <span class="text-on-surface font-semibold truncate max-w-xs md:max-w-md">${product.name}</span>
            </nav>

            <!-- Feedback Alerts -->
            <c:if test="${not empty sessionScope.reviewSuccess}">
                <div class="mb-space-md p-space-md rounded-lg bg-tertiary-container/20 text-on-tertiary-container flex items-center gap-space-sm border border-on-tertiary-container/30">
                    <span class="material-symbols-outlined text-on-tertiary-container">check_circle</span>
                    <span class="text-xs font-medium">${sessionScope.reviewSuccess}</span>
                </div>
                <c:remove var="reviewSuccess" scope="session" />
            </c:if>
            <c:if test="${not empty sessionScope.reviewError}">
                <div class="mb-space-md p-space-md rounded-lg bg-error-container text-on-error-container flex items-center gap-space-sm border border-error/20">
                    <span class="material-symbols-outlined text-error">warning</span>
                    <span class="text-xs font-medium">${sessionScope.reviewError}</span>
                </div>
                <c:remove var="reviewError" scope="session" />
            </c:if>

            <!-- Main Product Spotlight Grid: 50% Left / 50% Right -->
            <section class="grid grid-cols-1 lg:grid-cols-12 gap-space-xl items-start">
                
                <!-- LEFT: Editorial Gallery (6 cols) -->
                <div class="lg:col-span-6 flex flex-col gap-space-md">
                    <!-- Main Frame -->
                    <div class="relative w-full aspect-[4/3] sm:aspect-[5/4] bg-surface-container-low rounded-2xl overflow-hidden shadow-sm border border-surface-container group">
                        <c:set var="detailImg" value="${product.imageUrl}" />
                        <c:if test="${not detailImg.startsWith('http')}">
                            <c:set var="detailImg" value="${pageContext.request.contextPath}/${product.imageUrl}" />
                        </c:if>
                        <img id="mainProductImage" 
                             class="w-full h-full object-cover transition-transform duration-500 ease-out group-hover:scale-105" 
                             src="${detailImg}" 
                             alt="${product.name}"
                             onerror="this.onerror=null; this.src='https://placehold.co/600x600/f8f9ff/0b1c30?text=${product.brand}';"/>
                        
                        <!-- Hover Pill -->
                        <div class="absolute top-4 right-4 bg-surface-container-lowest/90 backdrop-blur-md px-3 py-1.5 rounded-full shadow-sm flex items-center gap-1.5 pointer-events-none">
                            <span class="material-symbols-outlined text-xs text-on-surface">zoom_in</span>
                            <span class="text-[10px] font-bold tracking-wide uppercase text-on-surface">Hover to inspect</span>
                        </div>

                        <!-- Badge -->
                        <div class="absolute bottom-4 left-4 bg-primary/85 text-on-primary backdrop-blur-sm px-3.5 py-1 rounded-full text-[10px] font-bold tracking-wider uppercase">
                            ${product.brand} Bespoke
                        </div>
                    </div>

                    <!-- Designer / Material Note Pill -->
                    <div class="p-space-md bg-surface-container-low rounded-xl flex items-center gap-space-md shadow-sm border border-surface-container">
                        <div class="w-10 h-10 rounded-full bg-surface-container-highest flex items-center justify-center text-secondary shrink-0">
                            <span class="material-symbols-outlined text-xl">architecture</span>
                        </div>
                        <div class="flex flex-col">
                            <span class="text-xs font-bold text-on-surface">Handcrafted Scandinavian Architecture</span>
                            <span class="text-xs text-on-surface-variant">Sustainable materials, kiln-dried precision joinery, and durable finishes.</span>
                        </div>
                    </div>
                </div>

                <!-- RIGHT: Product Information & Purchasing Suite (6 cols) -->
                <div class="lg:col-span-6 flex flex-col gap-space-md lg:pl-space-md">
                    
                    <!-- Atelier Brand Label -->
                    <div class="flex items-center justify-between">
                        <span class="text-xs uppercase tracking-[0.2em] font-bold text-secondary">
                            ${product.brand} Atelier
                        </span>
                        <c:choose>
                            <c:when test="${product.isInStock()}">
                                <span class="bg-tertiary-container/20 text-on-tertiary-container px-2.5 py-1 rounded-full text-[11px] font-bold">
                                    In Stock (${product.stockQuantity} available)
                                </span>
                            </c:when>
                            <c:otherwise>
                                <span class="bg-error-container text-on-error-container px-2.5 py-1 rounded-full text-[11px] font-bold">
                                    Depleted / Out of Stock
                                </span>
                            </c:otherwise>
                        </c:choose>
                    </div>

                    <!-- Product Title -->
                    <h1 class="text-2xl sm:text-3xl font-bold text-on-surface leading-tight">
                        ${product.name}
                    </h1>

                    <!-- Rating Block -->
                    <div class="flex items-center gap-space-sm pb-space-xs">
                        <div class="flex items-center text-amber-500">
                            <span class="material-symbols-outlined text-base" style="font-variation-settings: 'FILL' 1;">star</span>
                            <span class="material-symbols-outlined text-base" style="font-variation-settings: 'FILL' 1;">star</span>
                            <span class="material-symbols-outlined text-base" style="font-variation-settings: 'FILL' 1;">star</span>
                            <span class="material-symbols-outlined text-base" style="font-variation-settings: 'FILL' 1;">star</span>
                            <span class="material-symbols-outlined text-base" style="font-variation-settings: 'FILL' 1;">star</span>
                        </div>
                        <span class="text-xs font-bold text-on-surface">${product.rating != null ? product.rating : '4.8'}</span>
                        <span class="text-outline-variant">•</span>
                        <a class="text-xs text-secondary underline hover:text-on-surface transition-colors" href="#customer-reviews">
                            ${product.reviewCount != null ? product.reviewCount : '42'} Verified Client Reviews
                        </a>
                        <span class="text-outline-variant">•</span>
                        <span class="text-xs text-on-surface-variant font-medium">98% Recommend</span>
                    </div>

                    <!-- Price & Savings Card -->
                    <div class="p-space-md bg-surface-container-lowest rounded-xl shadow-sm border border-surface-container flex flex-col sm:flex-row sm:items-baseline gap-space-md justify-between">
                        <div class="flex items-baseline gap-space-sm flex-wrap">
                            <span class="text-2xl sm:text-3xl font-extrabold text-on-surface">₹${product.getDiscountedPrice()}</span>
                            <c:if test="${product.hasDiscount()}">
                                <span class="text-base text-outline line-through">₹${product.price}</span>
                            </c:if>
                        </div>
                        <c:if test="${product.hasDiscount()}">
                            <div class="inline-flex items-center px-3 py-1 bg-tertiary-fixed text-on-tertiary-container rounded-full text-xs font-bold shadow-xs">
                                Save ₹${product.getSavingsAmount()} (${product.discountPercent}% OFF)
                            </div>
                        </c:if>
                    </div>

                    <!-- Brief Description -->
                    <p class="text-xs sm:text-sm text-on-surface-variant leading-relaxed">
                        ${product.description}
                    </p>

                    <!-- Quantity Stepper & Action Buttons -->
                    <div class="space-y-space-md pt-space-xs">
                        <div class="flex items-center gap-space-md">
                            <label class="text-xs font-bold text-on-surface">Quantity:</label>
                            <div class="inline-flex items-center bg-surface-container rounded-lg p-1 gap-2 border border-outline-variant/30">
                                <button type="button" onclick="decrementQty()" 
                                        class="w-7 h-7 rounded flex items-center justify-center bg-surface-container-lowest text-on-surface hover:bg-surface-container-high transition-colors cursor-pointer">
                                    <span class="material-symbols-outlined text-sm">remove</span>
                                </button>
                                <input type="number" id="detailQty" value="1" min="1" max="${product.stockQuantity > 0 ? product.stockQuantity : 10}" 
                                       class="font-bold text-xs px-1 min-w-[28px] text-center bg-transparent border-0 outline-none"/>
                                <button type="button" onclick="incrementQty(${product.stockQuantity > 0 ? product.stockQuantity : 10})" 
                                        class="w-7 h-7 rounded flex items-center justify-center bg-surface-container-lowest text-on-surface hover:bg-surface-container-high transition-colors cursor-pointer">
                                    <span class="material-symbols-outlined text-sm">add</span>
                                </button>
                            </div>
                        </div>

                        <div class="grid grid-cols-1 sm:grid-cols-2 gap-space-md">
                            <button type="button" 
                                    onclick="addToCart(${product.id}, document.getElementById('detailQty').value)"
                                    ${!product.isInStock() ? 'disabled' : ''}
                                    class="w-full h-12 bg-primary text-on-primary hover:bg-secondary rounded-lg font-semibold text-xs flex items-center justify-center gap-2 shadow-sm transition-all duration-200 cursor-pointer disabled:opacity-50">
                                <span class="material-symbols-outlined text-base">shopping_bag</span>
                                <span>Add to Bag</span>
                            </button>
                            <a id="buyNowBtn" 
                               href="${pageContext.request.contextPath}/checkout?buyNowId=${product.id}&qty=1"
                               class="w-full h-12 bg-secondary text-on-secondary hover:bg-secondary-container rounded-lg font-semibold text-xs flex items-center justify-center gap-2 shadow-sm transition-all duration-200 text-center ${!product.isInStock() ? 'pointer-events-none opacity-50' : ''}">
                                <span class="material-symbols-outlined text-base">bolt</span>
                                <span>Instant Buy Now</span>
                            </a>
                        </div>
                    </div>

                    <!-- Trust Strip -->
                    <div class="grid grid-cols-3 gap-space-sm border-t border-surface-container pt-space-md text-xs text-on-surface-variant">
                        <div class="flex items-center gap-1.5">
                            <span class="material-symbols-outlined text-secondary text-lg">local_shipping</span>
                            <span>Free express delivery</span>
                        </div>
                        <div class="flex items-center gap-1.5">
                            <span class="material-symbols-outlined text-secondary text-lg">verified</span>
                            <span>5-Year craft warranty</span>
                        </div>
                        <div class="flex items-center gap-1.5">
                            <span class="material-symbols-outlined text-secondary text-lg">autorenew</span>
                            <span>30-Day home trial</span>
                        </div>
                    </div>

                </div>

            </section>

            <!-- Customer Reviews Section -->
            <section id="customer-reviews" class="mt-space-xl bg-surface-container-lowest rounded-2xl p-space-lg md:p-space-xl shadow-sm border border-surface-container">
                <div class="flex flex-col md:flex-row md:items-center justify-between mb-space-lg pb-space-sm border-b border-surface-container gap-space-sm">
                    <div>
                        <h2 class="text-xl font-bold text-on-surface">Client Feedback &amp; Ratings</h2>
                        <p class="text-xs text-on-surface-variant">Verified evaluations from curated interior spaces</p>
                    </div>
                    <div class="flex items-center gap-2">
                        <span class="material-symbols-outlined text-amber-500 text-xl" style="font-variation-settings: 'FILL' 1;">star</span>
                        <span class="text-sm font-bold text-on-surface">${product.rating != null ? product.rating : '4.8'} out of 5</span>
                    </div>
                </div>

                <div class="grid grid-cols-1 lg:grid-cols-12 gap-space-xl">
                    
                    <!-- Reviews List (7 cols) -->
                    <div class="lg:col-span-7 flex flex-col gap-space-md">
                        <c:choose>
                            <c:when test="${not empty reviews}">
                                <c:forEach var="rev" items="${reviews}">
                                    <div class="p-space-md bg-surface-container-low rounded-xl border border-surface-container">
                                        <div class="flex justify-between items-center mb-1">
                                            <span class="text-xs font-bold text-on-surface">${rev.userName != null ? rev.userName : 'Verified Collector'}</span>
                                            <div class="flex items-center text-amber-500 text-xs">
                                                <c:forEach begin="1" end="${rev.rating}">
                                                    <span class="material-symbols-outlined text-xs" style="font-variation-settings: 'FILL' 1;">star</span>
                                                </c:forEach>
                                            </div>
                                        </div>
                                        <p class="text-xs text-on-surface-variant leading-relaxed mb-1">${rev.comment}</p>
                                        <span class="text-[10px] text-outline font-mono">${rev.createdAt}</span>
                                    </div>
                                </c:forEach>
                            </c:when>
                            <c:otherwise>
                                <div class="p-space-lg bg-surface-container-low rounded-xl text-center border border-surface-container">
                                    <span class="material-symbols-outlined text-3xl text-outline mb-1">rate_review</span>
                                    <p class="text-xs text-on-surface-variant">No client reviews recorded yet. Share your experience below.</p>
                                </div>
                            </c:otherwise>
                        </c:choose>
                    </div>

                    <!-- Write a Review Form (5 cols) -->
                    <div class="lg:col-span-5 bg-surface-container-low rounded-xl p-space-lg border border-surface-container">
                        <h3 class="text-sm font-bold text-on-surface mb-space-sm">Submit Evaluation</h3>
                        
                        <form action="${pageContext.request.contextPath}/reviews" method="POST" class="space-y-space-md">
                            <input type="hidden" name="productId" value="${product.id}">
                            
                            <div class="space-y-1">
                                <label class="text-xs font-semibold text-on-surface">Your Rating Score</label>
                                <select name="rating" class="w-full bg-surface-container-lowest text-xs font-semibold text-on-surface rounded-lg p-2 border border-outline-variant/30 outline-none" required>
                                    <option value="5">★★★★★ 5 Stars (Exquisite)</option>
                                    <option value="4">★★★★☆ 4 Stars (Very Good)</option>
                                    <option value="3">★★★☆☆ 3 Stars (Satisfactory)</option>
                                    <option value="2">★★☆☆☆ 2 Stars (Needs Refinement)</option>
                                    <option value="1">★☆☆☆☆ 1 Star (Unsatisfactory)</option>
                                </select>
                            </div>

                            <div class="space-y-1">
                                <label class="text-xs font-semibold text-on-surface">Feedback Comment</label>
                                <textarea name="comment" rows="3" placeholder="Describe craftsmanship, texture, and durability..." 
                                          class="w-full bg-surface-container-lowest text-xs text-on-surface rounded-lg p-2 border border-outline-variant/30 outline-none focus:border-secondary" required></textarea>
                            </div>

                            <c:choose>
                                <c:when test="${not empty sessionScope.user}">
                                    <button type="submit" class="w-full bg-primary hover:bg-secondary text-on-primary py-2.5 rounded-lg text-xs font-semibold shadow-sm transition-colors cursor-pointer">
                                        Post Review
                                    </button>
                                </c:when>
                                <c:otherwise>
                                    <a href="${pageContext.request.contextPath}/login" class="w-full inline-block text-center bg-surface-container-high hover:bg-secondary hover:text-on-secondary text-on-surface py-2.5 rounded-lg text-xs font-semibold transition-colors">
                                        Sign In to Review
                                    </a>
                                </c:otherwise>
                            </c:choose>
                        </form>
                    </div>

                </div>
            </section>

            <!-- Related Products Section -->
            <c:if test="${not empty relatedProducts}">
                <section class="mt-space-xl mb-space-xl">
                    <h2 class="text-xl font-bold text-on-surface mb-space-md">Complementary Curations</h2>
                    <div class="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-space-md">
                        <c:forEach var="rel" items="${relatedProducts}">
                            <tags:productCard product="${rel}" />
                        </c:forEach>
                    </div>
                </section>
            </c:if>

        </div>
    </div>

</main>

<%@ include file="/fragments/footer.jsp" %>

<script>
    function incrementQty(max) {
        const input = document.getElementById('detailQty');
        let val = parseInt(input.value) || 1;
        if (val < max) {
            input.value = val + 1;
            updateBuyNowHref(val + 1);
        }
    }

    function decrementQty() {
        const input = document.getElementById('detailQty');
        let val = parseInt(input.value) || 1;
        if (val > 1) {
            input.value = val - 1;
            updateBuyNowHref(val - 1);
        }
    }

    function updateBuyNowHref(qty) {
        const btn = document.getElementById('buyNowBtn');
        if (btn) {
            btn.href = '${pageContext.request.contextPath}/checkout?buyNowId=${product.id}&qty=' + qty;
        }
    }

    document.getElementById('detailQty').addEventListener('change', function() {
        let val = parseInt(this.value) || 1;
        updateBuyNowHref(val);
    });
</script>
