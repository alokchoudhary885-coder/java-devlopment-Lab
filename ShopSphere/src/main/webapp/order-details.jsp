<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<c:set var="pageTitle" value="Invoice ${order.orderNumber} — ShopSphere | Nordic Retail Luxe" />
<%@ include file="/fragments/header.jsp" %>
<%@ include file="/fragments/navbar.jsp" %>

<main class="w-full bg-background">
    <div class="max-w-[1440px] w-full mx-auto px-gutter py-space-lg">
        
        <!-- Breadcrumb & Header Meta -->
        <div class="flex items-center gap-space-xs text-xs font-medium text-on-surface-variant mb-space-lg d-print-none">
            <a class="hover:text-primary transition-colors flex items-center gap-1" href="${pageContext.request.contextPath}/">
                <span class="material-symbols-outlined text-sm">storefront</span>
                <span>Store</span>
            </a>
            <span class="material-symbols-outlined text-outline text-[16px]">chevron_right</span>
            <a class="hover:text-primary transition-colors" href="${pageContext.request.contextPath}/orders">Orders</a>
            <span class="material-symbols-outlined text-outline text-[16px]">chevron_right</span>
            <span class="text-on-surface font-semibold font-mono">${order.orderNumber}</span>
        </div>

        <!-- Header Actions -->
        <div class="flex flex-col sm:flex-row justify-between sm:items-center gap-space-sm mb-space-lg">
            <div>
                <div class="flex items-center gap-2 mb-1">
                    <h1 class="text-2xl font-bold text-on-surface tracking-tight font-mono">${order.orderNumber}</h1>
                    <span class="px-2.5 py-0.5 rounded-full text-xs font-bold uppercase tracking-wider ${order.status eq 'DELIVERED' ? 'bg-tertiary-container/20 text-on-tertiary-container' : 'bg-surface-container text-secondary'}">
                        ${order.status}
                    </span>
                    <span class="px-2 py-0.5 rounded text-xs font-bold font-mono ${order.paymentStatus eq 'PAID' ? 'bg-primary text-on-primary' : 'bg-surface-container-high text-on-surface'}">
                        ${order.paymentStatus}
                    </span>
                </div>
                <p class="text-xs text-on-surface-variant">Archived on ${order.createdAt}</p>
            </div>
            
            <div class="flex items-center gap-2 d-print-none">
                <button type="button" onclick="window.print()" 
                        class="inline-flex items-center gap-1.5 bg-surface-container-lowest hover:bg-surface-container-low border border-surface-container text-on-surface px-4 py-2 rounded-lg text-xs font-semibold shadow-sm transition-colors cursor-pointer">
                    <span class="material-symbols-outlined text-base">print</span>
                    <span>Print Invoice</span>
                </button>
                <a href="${pageContext.request.contextPath}/orders" 
                   class="inline-flex items-center gap-1 bg-surface-container-high hover:bg-secondary hover:text-on-secondary px-4 py-2 rounded-lg text-xs font-semibold text-on-surface transition-colors">
                    <span class="material-symbols-outlined text-base">arrow_back</span>
                    <span>Back to Orders</span>
                </a>
            </div>
        </div>

        <div class="grid grid-cols-1 lg:grid-cols-12 gap-space-xl items-start">
            
            <!-- Left 65%: Ordered Items Table -->
            <div class="lg:col-span-8 bg-surface-container-lowest rounded-xl p-space-lg shadow-sm border border-surface-container space-y-space-md">
                <div class="flex items-center justify-between pb-space-xs border-b border-surface-container">
                    <h2 class="text-sm font-bold text-on-surface">Curated Line Items (${order.orderItems.size()})</h2>
                </div>

                <div class="overflow-x-auto">
                    <table class="w-full text-left text-xs">
                        <thead>
                            <tr class="bg-surface-container-low text-on-surface-variant font-semibold">
                                <th class="p-2.5 rounded-l">Item Description</th>
                                <th class="p-2.5 text-center">Quantity</th>
                                <th class="p-2.5 text-right">Unit Price</th>
                                <th class="p-2.5 text-right rounded-r">Total</th>
                            </tr>
                        </thead>
                        <tbody class="divide-y divide-surface-container">
                            <c:forEach var="item" items="${order.orderItems}">
                                <tr>
                                    <td class="p-2.5">
                                        <div class="flex items-center gap-2.5">
                                            <div class="w-12 h-12 rounded-lg bg-surface-container flex-shrink-0 overflow-hidden border border-surface-container">
                                                <c:set var="oi" value="${item.productImageUrl}" />
                                                <c:if test="${not oi.startsWith('http')}">
                                                    <c:set var="oi" value="${pageContext.request.contextPath}/${item.productImageUrl}" />
                                                </c:if>
                                                <img src="${oi}" alt="${item.productName}" class="w-full h-full object-cover" 
                                                     onerror="this.onerror=null; this.src='https://placehold.co/80x80/f8f9ff/0b1c30?text=Item';"/>
                                            </div>
                                            <a href="${pageContext.request.contextPath}/product-details?id=${item.productId}" class="font-bold text-on-surface hover:text-secondary transition-colors">
                                                ${item.productName}
                                            </a>
                                        </div>
                                    </td>
                                    <td class="p-2.5 text-center font-bold">${item.quantity}</td>
                                    <td class="p-2.5 text-right font-mono text-outline">₹${item.unitPrice}</td>
                                    <td class="p-2.5 text-right font-mono font-bold text-on-surface">₹${item.subtotal}</td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </div>
            </div>

            <!-- Right 35%: Shipping Address & Financial Calculation -->
            <div class="lg:col-span-4 flex flex-col gap-space-md">
                
                <!-- Destination Address Card -->
                <div class="bg-surface-container-lowest rounded-xl p-space-lg shadow-sm border border-surface-container space-y-space-sm text-xs">
                    <div class="flex items-center gap-1.5 font-bold text-on-surface pb-space-xs border-b border-surface-container">
                        <span class="material-symbols-outlined text-secondary text-base">pin_drop</span>
                        <span>Delivery Address</span>
                    </div>
                    <c:choose>
                        <c:when test="${not empty order.address}">
                            <div class="space-y-0.5 text-on-surface-variant">
                                <div class="font-bold text-on-surface">${sessionScope.user != null ? sessionScope.user.fullName : 'Valued Collector'}</div>
                                <div>${order.address.street}</div>
                                <div>${order.address.city}, ${order.address.state} — <strong class="text-on-surface">${order.address.postalCode}</strong></div>
                                <div>${order.address.country}</div>
                            </div>
                        </c:when>
                        <c:otherwise>
                            <div class="text-on-surface-variant">Standard Domestic Verified Delivery</div>
                        </c:otherwise>
                    </c:choose>
                </div>

                <!-- Financial Calculation -->
                <div class="bg-surface-container-lowest rounded-xl p-space-lg shadow-sm border border-surface-container space-y-space-md text-xs">
                    <div class="font-bold text-on-surface pb-space-xs border-b border-surface-container">
                        Payment &amp; Calculation
                    </div>

                    <div class="space-y-2">
                        <div class="flex justify-between text-on-surface-variant">
                            <span>Subtotal</span>
                            <span class="font-bold text-on-surface">₹${order.totalAmount}</span>
                        </div>
                        <c:if test="${order.discountAmount > 0}">
                            <div class="flex justify-between text-on-tertiary-container font-semibold">
                                <span>Applied Coupon</span>
                                <span>-₹${order.discountAmount}</span>
                            </div>
                        </c:if>
                        <div class="flex justify-between text-on-surface-variant">
                            <span>Logistics</span>
                            <span class="text-on-tertiary-container font-bold uppercase">Free</span>
                        </div>
                    </div>

                    <div class="h-px bg-surface-container"></div>

                    <div class="flex justify-between items-baseline">
                        <span class="font-bold uppercase tracking-wider text-on-surface">Total Billed</span>
                        <span class="font-price-lg text-lg font-bold text-on-surface">₹${order.finalAmount}</span>
                    </div>

                    <div class="bg-surface-container-low rounded-lg p-2.5 text-[11px] text-on-surface-variant flex items-center gap-2 border border-surface-container">
                        <span class="material-symbols-outlined text-secondary text-base">verified</span>
                        <span>Verified Transaction by ShopSphere Vault</span>
                    </div>
                </div>

            </div>

        </div>

    </div>
</main>

<%@ include file="/fragments/footer.jsp" %>
