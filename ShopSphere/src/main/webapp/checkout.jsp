<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<c:set var="pageTitle" value="Secure Checkout — ShopSphere | Nordic Retail Luxe" />
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
                <a class="hover:text-primary transition-colors" href="${pageContext.request.contextPath}/cart">Shopping Bag</a>
                <span class="material-symbols-outlined text-outline text-[16px]">chevron_right</span>
                <span class="text-on-surface font-semibold">Secure Checkout</span>
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

        <!-- Error Alerts -->
        <c:if test="${not empty error}">
            <div class="mb-space-md p-space-md rounded-lg bg-error-container text-on-error-container flex items-center gap-space-sm border border-error/20">
                <span class="material-symbols-outlined text-error">warning</span>
                <span class="text-xs font-medium">${error}</span>
            </div>
        </c:if>

        <form action="${pageContext.request.contextPath}/checkout" method="POST" id="checkoutForm">
            <input type="hidden" name="csrfToken" value="${sessionScope.csrfToken}">

            <div class="grid grid-cols-1 lg:grid-cols-12 gap-space-xl items-start">
                
                <!-- LEFT 65% COLUMN: Steps 1, 2, 3 -->
                <div class="lg:col-span-8 flex flex-col gap-space-lg">
                    
                    <!-- STEP 1: Delivery Address -->
                    <section class="bg-surface-container-lowest rounded-xl p-space-lg shadow-sm border border-surface-container">
                        <div class="flex items-center gap-2 mb-space-md pb-space-xs border-b border-surface-container">
                            <span class="w-6 h-6 rounded-full bg-primary text-on-primary flex items-center justify-center text-xs font-bold">1</span>
                            <h2 class="text-sm font-bold text-on-surface">Delivery Address</h2>
                        </div>

                        <c:choose>
                            <c:when test="${not empty addresses}">
                                <div class="flex flex-col gap-space-sm mb-space-md">
                                    <c:forEach var="addr" items="${addresses}" varStatus="loop">
                                        <label class="p-3 border rounded-xl flex items-start gap-3 cursor-pointer transition-colors ${addr.isDefault || loop.first ? 'border-secondary bg-surface-container-low' : 'border-surface-container hover:bg-surface-container-low'}">
                                            <input type="radio" name="addressId" value="${addr.id}" 
                                                   ${addr.isDefault || loop.first ? 'checked' : ''} 
                                                   onchange="toggleNewAddressForm(false)" 
                                                   class="accent-secondary mt-1"/>
                                            <div class="flex-1 text-xs">
                                                <div class="font-bold text-on-surface flex items-center gap-2">
                                                    <span>${sessionScope.user.fullName}</span>
                                                    <c:if test="${addr.isDefault}">
                                                        <span class="bg-secondary text-on-secondary px-2 py-0.2 rounded text-[10px] font-bold">Default</span>
                                                    </c:if>
                                                </div>
                                                <div class="text-on-surface-variant mt-0.5">
                                                    ${addr.street}, ${addr.city}, ${addr.state} — <strong class="text-on-surface">${addr.postalCode}</strong>, ${addr.country}
                                                </div>
                                            </div>
                                        </label>
                                    </c:forEach>
                                </div>
                            </c:when>
                            <c:otherwise>
                                <div class="p-3 bg-surface-container-low rounded-lg text-xs text-on-surface-variant mb-space-md">
                                    Please specify your shipping destination address below.
                                </div>
                            </c:otherwise>
                        </c:choose>

                        <!-- Add New Address Toggle -->
                        <label class="flex items-center gap-2 text-xs font-semibold text-on-surface cursor-pointer select-none">
                            <input type="radio" name="addressId" id="addr_new" value="new" 
                                   ${empty addresses ? 'checked' : ''} onchange="toggleNewAddressForm(true)" class="accent-secondary"/>
                            <span>+ Specify a new delivery address</span>
                        </label>

                        <!-- New Address Fields Form -->
                        <div id="newAddressFields" class="mt-space-md p-space-md bg-surface-container-low rounded-xl border border-surface-container ${not empty addresses ? 'hidden' : 'block'} space-y-space-sm">
                            <div class="space-y-1">
                                <label class="text-[11px] font-semibold text-on-surface">Street Address / House No.</label>
                                <input type="text" name="newStreet" placeholder="e.g. 42 Scandinavian Way, Malviya Nagar" 
                                       class="w-full bg-surface-container-lowest text-xs text-on-surface rounded-lg p-2 border border-outline-variant/30 outline-none"/>
                            </div>
                            <div class="grid grid-cols-1 sm:grid-cols-2 gap-space-sm">
                                <div class="space-y-1">
                                    <label class="text-[11px] font-semibold text-on-surface">City</label>
                                    <input type="text" name="newCity" placeholder="Jaipur" 
                                           class="w-full bg-surface-container-lowest text-xs text-on-surface rounded-lg p-2 border border-outline-variant/30 outline-none"/>
                                </div>
                                <div class="space-y-1">
                                    <label class="text-[11px] font-semibold text-on-surface">State</label>
                                    <input type="text" name="newState" placeholder="Rajasthan" 
                                           class="w-full bg-surface-container-lowest text-xs text-on-surface rounded-lg p-2 border border-outline-variant/30 outline-none"/>
                                </div>
                                <div class="space-y-1">
                                    <label class="text-[11px] font-semibold text-on-surface">Postal PIN Code</label>
                                    <input type="text" name="newPostalCode" placeholder="302017" 
                                           class="w-full bg-surface-container-lowest text-xs text-on-surface rounded-lg p-2 border border-outline-variant/30 outline-none"/>
                                </div>
                                <div class="space-y-1">
                                    <label class="text-[11px] font-semibold text-on-surface">Country</label>
                                    <input type="text" name="newCountry" value="India" 
                                           class="w-full bg-surface-container-lowest text-xs text-on-surface rounded-lg p-2 border border-outline-variant/30 outline-none"/>
                                </div>
                            </div>
                        </div>
                    </section>

                    <!-- STEP 2: Payment Method -->
                    <section class="bg-surface-container-lowest rounded-xl p-space-lg shadow-sm border border-surface-container">
                        <div class="flex items-center gap-2 mb-space-md pb-space-xs border-b border-surface-container">
                            <span class="w-6 h-6 rounded-full bg-primary text-on-primary flex items-center justify-center text-xs font-bold">2</span>
                            <h2 class="text-sm font-bold text-on-surface">Payment Method</h2>
                        </div>

                        <div class="grid grid-cols-1 sm:grid-cols-2 gap-space-sm">
                            
                            <!-- UPI -->
                            <label class="p-3 border rounded-xl flex items-start gap-2.5 cursor-pointer bg-surface-container-low border-secondary">
                                <input type="radio" name="paymentMethod" value="UPI" checked class="accent-secondary mt-1"/>
                                <div class="text-xs">
                                    <div class="font-bold text-on-surface flex items-center gap-1">
                                        <span class="material-symbols-outlined text-secondary text-base">qr_code_scanner</span>
                                        <span>UPI / Instant QR</span>
                                    </div>
                                    <span class="text-[11px] text-on-surface-variant block mt-0.5">GPay, PhonePe, Paytm, BHIM</span>
                                </div>
                            </label>

                            <!-- Card -->
                            <label class="p-3 border rounded-xl flex items-start gap-2.5 cursor-pointer border-surface-container hover:bg-surface-container-low">
                                <input type="radio" name="paymentMethod" value="CARD" class="accent-secondary mt-1"/>
                                <div class="text-xs">
                                    <div class="font-bold text-on-surface flex items-center gap-1">
                                        <span class="material-symbols-outlined text-secondary text-base">credit_card</span>
                                        <span>Credit / Debit Card</span>
                                    </div>
                                    <span class="text-[11px] text-on-surface-variant block mt-0.5">Visa, MasterCard, RuPay</span>
                                </div>
                            </label>

                            <!-- Net Banking -->
                            <label class="p-3 border rounded-xl flex items-start gap-2.5 cursor-pointer border-surface-container hover:bg-surface-container-low">
                                <input type="radio" name="paymentMethod" value="NET_BANKING" class="accent-secondary mt-1"/>
                                <div class="text-xs">
                                    <div class="font-bold text-on-surface flex items-center gap-1">
                                        <span class="material-symbols-outlined text-secondary text-base">account_balance</span>
                                        <span>Net Banking</span>
                                    </div>
                                    <span class="text-[11px] text-on-surface-variant block mt-0.5">All Major Indian Banks</span>
                                </div>
                            </label>

                            <!-- COD -->
                            <label class="p-3 border rounded-xl flex items-start gap-2.5 cursor-pointer border-surface-container hover:bg-surface-container-low">
                                <input type="radio" name="paymentMethod" value="COD" class="accent-secondary mt-1"/>
                                <div class="text-xs">
                                    <div class="font-bold text-on-surface flex items-center gap-1">
                                        <span class="material-symbols-outlined text-secondary text-base">payments</span>
                                        <span>Cash on Delivery</span>
                                    </div>
                                    <span class="text-[11px] text-on-surface-variant block mt-0.5">Pay at Doorstep Arrival</span>
                                </div>
                            </label>

                        </div>
                    </section>

                    <!-- STEP 3: Apply Coupon Code -->
                    <section class="bg-surface-container-lowest rounded-xl p-space-lg shadow-sm border border-surface-container">
                        <div class="flex items-center gap-2 mb-space-md pb-space-xs border-b border-surface-container">
                            <span class="w-6 h-6 rounded-full bg-primary text-on-primary flex items-center justify-center text-xs font-bold">3</span>
                            <h2 class="text-sm font-bold text-on-surface">Curator Coupon Code</h2>
                        </div>

                        <div class="flex gap-2">
                            <input type="text" id="couponInput" name="couponCode" placeholder="Enter coupon code (e.g. LUXE20)" 
                                   value="${couponCode}" style="text-transform: uppercase;" 
                                   class="flex-1 bg-surface-container-low text-xs font-semibold text-on-surface rounded-lg p-2.5 border border-outline-variant/30 outline-none uppercase font-mono"/>
                            <button type="button" onclick="applyCoupon()" class="bg-primary hover:bg-secondary text-on-primary px-4 rounded-lg text-xs font-semibold transition-colors cursor-pointer">
                                Apply
                            </button>
                        </div>

                        <c:if test="${not empty couponMessage}">
                            <div class="text-xs mt-2 font-medium ${discountAmount > 0 ? 'text-on-tertiary-container' : 'text-error'}">
                                ${couponMessage}
                            </div>
                        </c:if>

                        <div class="flex flex-wrap items-center gap-2 mt-3 text-xs text-on-surface-variant">
                            <span class="text-outline">Active promotions:</span>
                            <button type="button" onclick="fillCoupon('LUXE20')" class="bg-surface-container px-2 py-0.5 rounded text-[11px] font-bold text-on-surface hover:bg-surface-container-high transition-colors">LUXE20 (20% Off)</button>
                            <button type="button" onclick="fillCoupon('WELCOME50')" class="bg-surface-container px-2 py-0.5 rounded text-[11px] font-bold text-on-surface hover:bg-surface-container-high transition-colors">WELCOME50 (50% Off)</button>
                        </div>
                    </section>

                </div>

                <!-- RIGHT 35% COLUMN: Review & Place Order -->
                <div class="lg:col-span-4 flex flex-col gap-space-md sticky top-[160px]">
                    <div class="bg-surface-container-lowest rounded-xl p-space-lg shadow-sm border border-surface-container space-y-space-md">
                        
                        <h3 class="font-headline-sm text-sm font-bold text-on-surface pb-space-xs border-b border-surface-container">
                            Review Items (${cartItems.size()})
                        </h3>

                        <!-- Mini Items Scroll List -->
                        <div class="space-y-3 max-h-48 overflow-y-auto pr-1">
                            <c:forEach var="item" items="${cartItems}">
                                <div class="flex items-center gap-3 text-xs">
                                    <div class="w-10 h-10 rounded-lg bg-surface-container flex-shrink-0 overflow-hidden border border-surface-container">
                                        <c:set var="ci" value="${item.productImageUrl}" />
                                        <c:if test="${not ci.startsWith('http')}">
                                            <c:set var="ci" value="${pageContext.request.contextPath}/${item.productImageUrl}" />
                                        </c:if>
                                        <img src="${ci}" alt="${item.productName}" class="w-full h-full object-cover" 
                                             onerror="this.onerror=null; this.src='https://placehold.co/60x60/f8f9ff/0b1c30?text=Item';"/>
                                    </div>
                                    <div class="flex-1 min-w-0">
                                        <span class="font-bold text-on-surface truncate block">${item.productName}</span>
                                        <span class="text-on-surface-variant">${item.quantity} &times; ₹${item.getEffectiveUnitPrice()}</span>
                                    </div>
                                    <span class="font-bold text-on-surface whitespace-nowrap">₹${item.getSubtotal()}</span>
                                </div>
                            </c:forEach>
                        </div>

                        <div class="h-px bg-surface-container"></div>

                        <!-- Calculations -->
                        <div class="space-y-2 text-xs">
                            <div class="flex justify-between text-on-surface-variant">
                                <span>Subtotal</span>
                                <span class="font-bold text-on-surface">₹${subtotal}</span>
                            </div>
                            <c:if test="${discountAmount > 0}">
                                <div class="flex justify-between text-on-tertiary-container font-semibold">
                                    <span>Coupon Discount</span>
                                    <span>-₹${discountAmount}</span>
                                </div>
                            </c:if>
                            <div class="flex justify-between text-on-surface-variant">
                                <span>Logistics Express</span>
                                <span class="text-on-tertiary-container font-bold uppercase">Free</span>
                            </div>
                        </div>

                        <div class="h-px bg-surface-container"></div>

                        <div class="flex justify-between items-baseline">
                            <span class="text-xs font-bold uppercase tracking-wider text-on-surface">Final Payable</span>
                            <span class="font-display-hero text-xl font-extrabold text-on-surface font-price-lg">₹${finalAmount}</span>
                        </div>

                        <button type="submit" 
                                class="w-full h-12 bg-primary text-on-primary hover:bg-secondary rounded-lg font-semibold text-xs flex items-center justify-center gap-2 shadow-sm transition-all duration-200 cursor-pointer">
                            <span class="material-symbols-outlined text-base">lock</span>
                            <span>Complete Order &amp; Pay</span>
                        </button>

                        <div class="bg-surface-container-low rounded-lg p-2.5 text-[11px] text-on-surface-variant flex items-center gap-2 border border-surface-container">
                            <span class="material-symbols-outlined text-secondary text-base">verified_user</span>
                            <span>Direct Encrypted Gateway • 100% Secure Transaction</span>
                        </div>

                    </div>
                </div>

            </div>
        </form>

    </div>
</main>

<%@ include file="/fragments/footer.jsp" %>

<script>
    function toggleNewAddressForm(isNew) {
        const formDiv = document.getElementById('newAddressFields');
        if (isNew) {
            formDiv.classList.remove('hidden');
            formDiv.classList.add('block');
        } else {
            formDiv.classList.add('hidden');
            formDiv.classList.remove('block');
        }
    }

    function fillCoupon(code) {
        document.getElementById('couponInput').value = code;
    }

    function applyCoupon() {
        const code = document.getElementById('couponInput').value.trim();
        const url = new URL(window.location.href);
        url.searchParams.set('coupon', code);
        window.location.href = url.toString();
    }
</script>
