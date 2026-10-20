<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<c:set var="pageTitle" value="Order Confirmed — ShopSphere | Nordic Retail Luxe" />
<%@ include file="/fragments/header.jsp" %>
<%@ include file="/fragments/navbar.jsp" %>

<main class="w-full bg-background py-space-xl">
    <div class="max-w-[760px] mx-auto px-gutter">
        
        <div class="bg-surface-container-lowest rounded-2xl p-space-xl shadow-sm border border-surface-container text-center space-y-space-lg">
            
            <!-- Check Icon -->
            <div class="w-20 h-20 rounded-full bg-surface-container text-on-tertiary-container mx-auto flex items-center justify-center">
                <span class="material-symbols-outlined text-4xl" style="font-variation-settings: 'FILL' 1;">check_circle</span>
            </div>

            <div class="space-y-1">
                <h1 class="text-2xl sm:text-3xl font-bold text-on-surface">Thank You for Your Order!</h1>
                <p class="text-xs sm:text-sm text-on-surface-variant max-w-md mx-auto">
                    Your acquisition has been confirmed and placed into our Scandinavian white-glove fulfillment pipeline.
                </p>
            </div>

            <!-- Order Reference Card -->
            <div class="bg-surface-container-low rounded-xl p-space-md border border-surface-container text-left grid grid-cols-2 gap-space-md text-xs">
                <div>
                    <span class="text-outline text-[11px] block">Order Identifier</span>
                    <span class="font-bold text-on-surface font-mono">${lastPlacedOrder != null ? lastPlacedOrder.orderNumber : 'ORD-20261019-CONFIRMED'}</span>
                </div>
                <div>
                    <span class="text-outline text-[11px] block">Payment Telemetry</span>
                    <span class="inline-block mt-0.5 px-2 py-0.5 rounded text-[10px] font-bold ${lastPlacedOrder != null && lastPlacedOrder.paymentStatus eq 'PAID' ? 'bg-tertiary-container/20 text-on-tertiary-container' : 'bg-surface-container text-secondary'}">
                        ${lastPlacedOrder != null ? lastPlacedOrder.paymentStatus : 'PAID (Verified)'}
                    </span>
                </div>
                <div>
                    <span class="text-outline text-[11px] block">Fulfillment Status</span>
                    <span class="inline-block mt-0.5 px-2 py-0.5 rounded text-[10px] font-bold bg-primary text-on-primary">
                        ${lastPlacedOrder != null ? lastPlacedOrder.status : 'PROCESSING'}
                    </span>
                </div>
                <div>
                    <span class="text-outline text-[11px] block">Total Amount</span>
                    <span class="font-bold text-on-surface font-price-lg text-sm">
                        ₹${lastPlacedOrder != null ? lastPlacedOrder.finalAmount : '0.00'}
                    </span>
                </div>
            </div>

            <!-- Actions -->
            <div class="flex flex-col sm:flex-row justify-center gap-space-sm pt-space-xs">
                <a href="${pageContext.request.contextPath}/orders" 
                   class="inline-flex items-center justify-center gap-1.5 bg-primary text-on-primary hover:bg-secondary px-5 py-2.5 rounded-lg text-xs font-semibold shadow-sm transition-colors">
                    <span class="material-symbols-outlined text-base">receipt_long</span>
                    <span>View Order Archives</span>
                </a>
                <a href="${pageContext.request.contextPath}/products" 
                   class="inline-flex items-center justify-center gap-1.5 bg-surface-container-low hover:bg-surface-container text-on-surface px-5 py-2.5 rounded-lg text-xs font-semibold transition-colors border border-surface-container">
                    <span>Continue Browsing</span>
                    <span class="material-symbols-outlined text-base">arrow_forward</span>
                </a>
            </div>

        </div>

    </div>
</main>

<%@ include file="/fragments/footer.jsp" %>
