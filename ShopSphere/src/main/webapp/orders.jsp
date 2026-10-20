<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<c:set var="pageTitle" value="My Orders — ShopSphere | Nordic Retail Luxe" />
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
            <span class="text-on-surface font-semibold">Orders &amp; Shipments</span>
        </div>

        <div class="flex flex-col sm:flex-row justify-between sm:items-center gap-space-sm mb-space-lg">
            <div>
                <h1 class="text-2xl font-bold text-on-surface tracking-tight">Order Archives</h1>
                <p class="text-xs text-on-surface-variant">Real-time dispatch telemetry and verifiable invoices.</p>
            </div>
            <span class="bg-surface-container-high text-on-surface px-3 py-1 rounded-full text-xs font-bold inline-flex items-center gap-1 border border-surface-container">
                <span class="material-symbols-outlined text-sm text-secondary">inventory_2</span>
                <span>${orders != null ? orders.size() : 0} Orders</span>
            </span>
        </div>

        <c:choose>
            <c:when test="${not empty orders}">
                <div class="space-y-space-md">
                    <c:forEach var="order" items="${orders}">
                        <div class="bg-surface-container-lowest rounded-xl p-space-md md:p-space-lg shadow-sm border border-surface-container space-y-space-md">
                            
                            <div class="flex flex-col md:flex-row justify-between md:items-center gap-space-sm pb-space-sm border-b border-surface-container">
                                <div>
                                    <div class="flex items-center gap-space-sm mb-1">
                                        <span class="font-bold text-sm text-on-surface font-mono">${order.orderNumber}</span>
                                        <span class="px-2.5 py-0.5 rounded-full text-[10px] font-bold uppercase tracking-wider ${order.status eq 'DELIVERED' ? 'bg-tertiary-container/20 text-on-tertiary-container' : 'bg-surface-container text-secondary'}">
                                            ${order.status}
                                        </span>
                                        <span class="px-2 py-0.5 rounded text-[10px] font-bold font-mono ${order.paymentStatus eq 'PAID' ? 'bg-primary text-on-primary' : 'bg-surface-container-high text-on-surface'}">
                                            ${order.paymentStatus}
                                        </span>
                                    </div>
                                    <span class="text-xs text-outline">Executed on ${order.createdAt}</span>
                                </div>

                                <div class="md:text-right">
                                    <span class="text-[11px] text-outline block">Order Total</span>
                                    <span class="font-price-lg text-lg font-bold text-on-surface">₹${order.finalAmount}</span>
                                </div>
                            </div>

                            <div class="flex flex-col sm:flex-row justify-between sm:items-center gap-space-sm text-xs">
                                <div class="flex items-center gap-1.5 text-on-surface-variant">
                                    <span class="material-symbols-outlined text-secondary text-base">local_shipping</span>
                                    <span>White-Glove Express Fulfillment</span>
                                </div>
                                <a href="${pageContext.request.contextPath}/order-details?id=${order.id}" 
                                   class="inline-flex items-center gap-1 bg-surface-container-low hover:bg-secondary hover:text-on-secondary px-3.5 py-1.5 rounded-lg text-xs font-semibold text-on-surface transition-colors">
                                    <span>View Invoice</span>
                                    <span class="material-symbols-outlined text-sm">chevron_right</span>
                                </a>
                            </div>

                        </div>
                    </c:forEach>
                </div>
            </c:when>

            <c:otherwise>
                <div class="bg-surface-container-lowest rounded-2xl p-space-xl text-center border border-surface-container shadow-sm my-space-lg">
                    <div class="w-16 h-16 rounded-full bg-surface-container mx-auto flex items-center justify-center text-outline mb-space-md">
                        <span class="material-symbols-outlined text-3xl">receipt_long</span>
                    </div>
                    <h3 class="text-base font-bold text-on-surface mb-1">No Orders Found</h3>
                    <p class="text-xs text-on-surface-variant max-w-sm mx-auto mb-space-lg">
                        You have not placed any orders yet. Discover timeless items in our catalog.
                    </p>
                    <a href="${pageContext.request.contextPath}/products" class="inline-flex items-center gap-2 bg-primary text-on-primary hover:bg-secondary px-5 py-2.5 rounded-lg text-xs font-semibold shadow-sm transition-colors">
                        <span>Explore Catalog</span>
                        <span class="material-symbols-outlined text-sm">arrow_forward</span>
                    </a>
                </div>
            </c:otherwise>
        </c:choose>

    </div>
</main>

<%@ include file="/fragments/footer.jsp" %>
