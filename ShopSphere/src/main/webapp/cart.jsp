<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<c:set var="pageTitle" value="Shopping Bag — ShopSphere | Nordic Retail Luxe" />
<%@ include file="/fragments/header.jsp" %>
<%@ include file="/fragments/navbar.jsp" %>

<main class="w-full bg-background">
    <div class="max-w-[1440px] w-full mx-auto px-gutter py-space-lg">
        
        <!-- Breadcrumb & Header Meta -->
        <div class="flex flex-col md:flex-row md:items-center justify-between gap-space-sm mb-space-lg">
            <div class="flex items-center gap-space-xs text-xs font-medium text-on-surface-variant">
                <a class="hover:text-primary transition-colors flex items-center gap-1" href="${pageContext.request.contextPath}/">
                    <span class="material-symbols-outlined text-sm">storefront</span>
                    <span>Store</span>
                </a>
                <span class="material-symbols-outlined text-outline text-[16px]">chevron_right</span>
                <span class="text-on-surface font-semibold">Shopping Bag</span>
            </div>
            <div class="flex items-center gap-space-md text-xs font-medium">
                <span class="flex items-center gap-1 text-on-tertiary-container bg-tertiary-container/10 px-2.5 py-1 rounded-full">
                    <span class="material-symbols-outlined text-sm">verified</span>
                    <span>Verified Direct Merchant</span>
                </span>
                <span class="text-outline">|</span>
                <span class="text-on-surface-variant flex items-center gap-1">
                    <span class="material-symbols-outlined text-sm text-secondary">lock</span>
                    <span>SSL 256-Bit Guarded</span>
                </span>
            </div>
        </div>

        <!-- Feedback Alerts -->
        <c:if test="${not empty sessionScope.cartSuccess}">
            <div class="mb-space-md p-space-md rounded-lg bg-tertiary-container/20 text-on-tertiary-container flex items-center gap-space-sm border border-on-tertiary-container/30">
                <span class="material-symbols-outlined text-on-tertiary-container">check_circle</span>
                <span class="text-xs font-medium">${sessionScope.cartSuccess}</span>
            </div>
            <c:remove var="cartSuccess" scope="session" />
        </c:if>
        <c:if test="${not empty sessionScope.cartError}">
            <div class="mb-space-md p-space-md rounded-lg bg-error-container text-on-error-container flex items-center gap-space-sm border border-error/20">
                <span class="material-symbols-outlined text-error">warning</span>
                <span class="text-xs font-medium">${sessionScope.cartError}</span>
            </div>
            <c:remove var="cartError" scope="session" />
        </c:if>

        <c:choose>
            <c:when test="${not empty cartItems}">
                <!-- Main Grid: 65% Items Table & 35% Sticky Summary -->
                <div class="grid grid-cols-1 lg:grid-cols-12 gap-space-xl items-start">
                    
                    <!-- LEFT 65% COLUMN: Shopping Bag Table -->
                    <div class="lg:col-span-8 flex flex-col gap-space-md">
                        <section class="bg-surface-container-lowest rounded-xl p-space-lg shadow-sm border border-surface-container">
                            <div class="flex items-center justify-between mb-space-md">
                                <div class="flex items-center gap-space-sm">
                                    <span class="w-8 h-8 rounded-lg bg-surface-container flex items-center justify-center text-on-surface">
                                        <span class="material-symbols-outlined text-lg">shopping_basket</span>
                                    </span>
                                    <h2 class="font-headline-md text-lg font-bold text-on-surface tracking-tight">Shopping Bag</h2>
                                    <span class="bg-surface-container-high text-on-surface-variant px-2 py-0.5 rounded-full text-xs font-bold">
                                        ${cartItems.size()} Items
                                    </span>
                                </div>
                                <span class="text-xs text-on-surface-variant hidden sm:inline-block">Dispatch within 24 Hours</span>
                            </div>

                            <!-- Table Container -->
                            <div class="overflow-x-auto">
                                <table class="w-full text-left">
                                    <thead>
                                        <tr class="bg-surface-container-low text-on-surface-variant text-xs font-semibold">
                                            <th class="py-2.5 px-3 rounded-l-lg">Product Curation</th>
                                            <th class="py-2.5 px-2 text-right">Unit Price</th>
                                            <th class="py-2.5 px-2 text-center">Quantity</th>
                                            <th class="py-2.5 px-2 text-right">Subtotal</th>
                                            <th class="py-2.5 px-2 text-center rounded-r-lg">Action</th>
                                        </tr>
                                    </thead>
                                    <tbody class="text-xs text-on-surface divide-y divide-surface-container">
                                        <c:forEach var="item" items="${cartItems}">
                                            <tr class="group hover:bg-surface-container-low/40 transition-colors">
                                                <td class="py-space-md px-3">
                                                    <div class="flex items-center gap-space-md">
                                                        <div class="w-16 h-16 rounded-lg bg-surface-container flex-shrink-0 overflow-hidden relative shadow-sm border border-surface-container">
                                                            <c:set var="cImg" value="${item.productImageUrl}" />
                                                            <c:if test="${not cImg.startsWith('http')}">
                                                                <c:set var="cImg" value="${pageContext.request.contextPath}/${item.productImageUrl}" />
                                                            </c:if>
                                                            <img class="w-full h-full object-cover group-hover:scale-105 transition-transform duration-300" 
                                                                 src="${cImg}" 
                                                                 alt="${item.productName}"
                                                                 onerror="this.onerror=null; this.src='https://placehold.co/100x100/f8f9ff/0b1c30?text=Item';"/>
                                                        </div>
                                                        <div class="flex flex-col min-w-0">
                                                            <a href="${pageContext.request.contextPath}/product-details?id=${item.productId}" 
                                                               class="font-bold text-xs text-on-surface hover:text-secondary transition-colors line-clamp-1">
                                                                ${item.productName}
                                                            </a>
                                                            <span class="inline-flex items-center gap-1 text-on-tertiary-container text-[11px] font-semibold mt-1">
                                                                <span class="material-symbols-outlined text-[13px]">local_shipping</span> White Glove Assembly Free
                                                            </span>
                                                        </div>
                                                    </div>
                                                </td>
                                                <td class="py-space-md px-2 text-right font-semibold whitespace-nowrap">
                                                    ₹${item.getEffectiveUnitPrice()}
                                                </td>
                                                <td class="py-space-md px-2 text-center">
                                                    <form action="${pageContext.request.contextPath}/cart" method="POST" class="inline-flex items-center bg-surface-container rounded-lg p-1 gap-1 border border-outline-variant/30">
                                                        <input type="hidden" name="action" value="update">
                                                        <input type="hidden" name="cartId" value="${item.id}">
                                                        
                                                        <button class="w-6 h-6 rounded flex items-center justify-center bg-surface-container-lowest text-on-surface hover:bg-surface-container-high transition-colors cursor-pointer" 
                                                                type="submit" name="quantity" value="${item.quantity - 1}">
                                                            <span class="material-symbols-outlined text-[14px]">remove</span>
                                                        </button>
                                                        <span class="font-bold text-xs px-1 min-w-[20px] text-center">${item.quantity}</span>
                                                        <button class="w-6 h-6 rounded flex items-center justify-center bg-surface-container-lowest text-on-surface hover:bg-surface-container-high transition-colors cursor-pointer" 
                                                                type="submit" name="quantity" value="${item.quantity + 1}">
                                                            <span class="material-symbols-outlined text-[14px]">add</span>
                                                        </button>
                                                    </form>
                                                </td>
                                                <td class="py-space-md px-2 text-right font-bold font-price-md whitespace-nowrap text-on-surface">
                                                    ₹${item.getSubtotal()}
                                                </td>
                                                <td class="py-space-md px-2 text-center">
                                                    <form action="${pageContext.request.contextPath}/cart" method="POST" class="inline">
                                                        <input type="hidden" name="action" value="remove">
                                                        <input type="hidden" name="cartId" value="${item.id}">
                                                        <button class="p-1.5 rounded-lg text-outline hover:text-error hover:bg-error-container/40 transition-colors cursor-pointer" title="Remove item" type="submit">
                                                            <span class="material-symbols-outlined text-base">delete</span>
                                                        </button>
                                                    </form>
                                                </td>
                                            </tr>
                                        </c:forEach>
                                    </tbody>
                                </table>
                            </div>

                            <!-- Footer Bar of Table -->
                            <div class="mt-space-md pt-space-md border-t border-surface-container flex flex-wrap justify-between items-center gap-space-sm">
                                <a href="${pageContext.request.contextPath}/products" class="inline-flex items-center gap-1.5 text-xs font-semibold text-on-surface hover:text-secondary transition-colors">
                                    <span class="material-symbols-outlined text-sm">arrow_back</span>
                                    <span>Continue Browsing</span>
                                </a>
                                <form action="${pageContext.request.contextPath}/cart" method="POST">
                                    <input type="hidden" name="action" value="clear">
                                    <button type="submit" onclick="return confirm('Clear entire shopping bag?')" 
                                            class="inline-flex items-center gap-1 text-xs text-error hover:underline cursor-pointer">
                                        <span class="material-symbols-outlined text-sm">remove_shopping_cart</span>
                                        <span>Empty Bag</span>
                                    </button>
                                </form>
                            </div>
                        </section>
                    </div>

                    <!-- RIGHT 35% COLUMN: Order Summary -->
                    <div class="lg:col-span-4 flex flex-col gap-space-md sticky top-[160px]">
                        <div class="bg-surface-container-lowest rounded-xl p-space-lg shadow-sm border border-surface-container space-y-space-md">
                            <h3 class="font-headline-sm text-sm font-bold text-on-surface pb-space-xs border-b border-surface-container">
                                Bag Summary
                            </h3>

                            <div class="space-y-2 text-xs">
                                <div class="flex justify-between text-on-surface-variant">
                                    <span>Curated Subtotal</span>
                                    <span class="font-bold text-on-surface">₹${subtotal}</span>
                                </div>
                                <c:if test="${savings > 0}">
                                    <div class="flex justify-between text-on-tertiary-container font-semibold">
                                        <span>Applied Direct Savings</span>
                                        <span>-₹${savings}</span>
                                    </div>
                                </c:if>
                                <div class="flex justify-between text-on-surface-variant">
                                    <span>Logistics &amp; Courier</span>
                                    <span class="text-on-tertiary-container font-bold uppercase tracking-wider">Free (Domestic)</span>
                                </div>
                                <div class="flex justify-between text-on-surface-variant">
                                    <span>Estimated Taxes</span>
                                    <span class="text-outline">Included in Subtotal</span>
                                </div>
                            </div>

                            <div class="h-px bg-surface-container"></div>

                            <div class="flex justify-between items-baseline">
                                <span class="text-xs font-bold uppercase tracking-wider text-on-surface">Total Investment</span>
                                <span class="font-display-hero text-xl font-extrabold text-on-surface font-price-lg">₹${subtotal}</span>
                            </div>

                            <a href="${pageContext.request.contextPath}/checkout" 
                               class="w-full h-12 bg-primary text-on-primary hover:bg-secondary rounded-lg font-semibold text-xs flex items-center justify-center gap-2 shadow-sm transition-all duration-200 text-center">
                                <span>Proceed to Checkout</span>
                                <span class="material-symbols-outlined text-base">arrow_forward</span>
                            </a>

                            <div class="bg-surface-container-low rounded-lg p-2.5 text-[11px] text-on-surface-variant flex items-center gap-2 border border-surface-container">
                                <span class="material-symbols-outlined text-secondary text-base">lock</span>
                                <span>256-Bit SSL Encrypted &amp; Guaranteed Safe Checkout</span>
                            </div>
                        </div>
                    </div>

                </div>
            </c:when>

            <c:otherwise>
                <!-- Empty Bag State -->
                <div class="bg-surface-container-lowest rounded-2xl p-space-xl text-center border border-surface-container shadow-sm my-space-lg">
                    <div class="w-16 h-16 rounded-full bg-surface-container mx-auto flex items-center justify-center text-outline mb-space-md">
                        <span class="material-symbols-outlined text-3xl">shopping_bag</span>
                    </div>
                    <h3 class="text-base font-bold text-on-surface mb-1">Your Shopping Bag is Empty</h3>
                    <p class="text-xs text-on-surface-variant max-w-sm mx-auto mb-space-lg">
                        Discover timeless Scandinavian furniture, ambient lighting, and bespoke everyday objects.
                    </p>
                    <a href="${pageContext.request.contextPath}/products" class="inline-flex items-center gap-2 bg-primary text-on-primary hover:bg-secondary px-5 py-2.5 rounded-lg text-xs font-semibold shadow-sm transition-colors">
                        <span>Explore Curated Catalog</span>
                        <span class="material-symbols-outlined text-sm">arrow_forward</span>
                    </a>
                </div>
            </c:otherwise>
        </c:choose>

    </div>
</main>

<%@ include file="/fragments/footer.jsp" %>
