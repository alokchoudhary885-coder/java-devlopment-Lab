<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<!-- Nordic Retail Luxe Value Guarantee Strip -->
<section class="w-full bg-surface-container-lowest border-t border-b border-surface-container py-space-lg my-space-xl">
    <div class="max-w-[1440px] mx-auto px-gutter grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-space-lg">
        <div class="flex items-center gap-space-md">
            <div class="w-12 h-12 rounded-full bg-surface-container-low text-secondary flex items-center justify-center shrink-0">
                <span class="material-symbols-outlined text-[24px]">local_shipping</span>
            </div>
            <div class="space-y-0.5">
                <h4 class="text-sm font-bold text-on-surface">Free & Express Delivery</h4>
                <p class="text-xs text-on-surface-variant">Complimentary shipping on orders over ₹499</p>
            </div>
        </div>
        <div class="flex items-center gap-space-md">
            <div class="w-12 h-12 rounded-full bg-surface-container-low text-secondary flex items-center justify-center shrink-0">
                <span class="material-symbols-outlined text-[24px]">verified</span>
            </div>
            <div class="space-y-0.5">
                <h4 class="text-sm font-bold text-on-surface">5-Year Craft Guarantee</h4>
                <p class="text-xs text-on-surface-variant">100% verified authentic Scandinavian designs</p>
            </div>
        </div>
        <div class="flex items-center gap-space-md">
            <div class="w-12 h-12 rounded-full bg-surface-container-low text-secondary flex items-center justify-center shrink-0">
                <span class="material-symbols-outlined text-[24px]">assignment_return</span>
            </div>
            <div class="space-y-0.5">
                <h4 class="text-sm font-bold text-on-surface">30-Day In-Home Trial</h4>
                <p class="text-xs text-on-surface-variant">Hassle-free return & replacement policy</p>
            </div>
        </div>
        <div class="flex items-center gap-space-md">
            <div class="w-12 h-12 rounded-full bg-surface-container-low text-secondary flex items-center justify-center shrink-0">
                <span class="material-symbols-outlined text-[24px]">support_agent</span>
            </div>
            <div class="space-y-0.5">
                <h4 class="text-sm font-bold text-on-surface">Dedicated Concierge</h4>
                <p class="text-xs text-on-surface-variant">Expert support 7 days a week</p>
            </div>
        </div>
    </div>
</section>

<!-- Main Nordic Retail Luxe Footer -->
<footer class="w-full bg-surface-container-lowest pt-space-xl pb-space-lg text-on-surface">
    <div class="max-w-[1440px] mx-auto px-gutter grid grid-cols-1 md:grid-cols-2 lg:grid-cols-5 gap-space-xl mb-space-xl">
        
        <!-- Col 1: Brand Philosophy -->
        <div class="lg:col-span-2 flex flex-col gap-space-md">
            <a href="${pageContext.request.contextPath}/" class="flex items-center gap-2 text-decoration-none">
                <div class="w-8 h-8 rounded-lg bg-primary text-on-primary flex items-center justify-center font-bold text-base">S</div>
                <span class="text-lg font-bold tracking-tight text-on-surface">ShopSphere</span>
            </a>
            <p class="text-xs text-on-surface-variant leading-relaxed max-w-sm">
                Editorial poise meets Scandinavian functionalism. Handcrafted interior pieces, acoustic precision technology, and sustainable materials curated for modern living.
            </p>
            <div class="flex items-center gap-space-md text-on-surface-variant pt-space-xs">
                <a class="hover:text-primary transition-colors" href="${pageContext.request.contextPath}/products" title="Storefront"><span class="material-symbols-outlined text-xl">storefront</span></a>
                <a class="hover:text-primary transition-colors" href="#" title="Customer Support"><span class="material-symbols-outlined text-xl">support_agent</span></a>
                <a class="hover:text-primary transition-colors" href="#" title="Verified Secure"><span class="material-symbols-outlined text-xl">verified_user</span></a>
                <a class="hover:text-primary transition-colors" href="https://github.com/alokchoudhary885-coder/ShopSphere" target="_blank" title="GitHub Repository"><span class="material-symbols-outlined text-xl">code</span></a>
            </div>
        </div>

        <!-- Col 2: Customer Care -->
        <div class="flex flex-col gap-space-sm">
            <span class="text-xs font-bold uppercase tracking-wider text-on-surface">Customer Care</span>
            <a class="text-xs text-on-surface-variant hover:text-on-surface transition-colors" href="${pageContext.request.contextPath}/orders">Order Tracking</a>
            <a class="text-xs text-on-surface-variant hover:text-on-surface transition-colors" href="${pageContext.request.contextPath}/cart">Shopping Bag</a>
            <a class="text-xs text-on-surface-variant hover:text-on-surface transition-colors" href="${pageContext.request.contextPath}/wishlist">Saved Wishlist</a>
            <a class="text-xs text-on-surface-variant hover:text-on-surface transition-colors" href="${pageContext.request.contextPath}/login">My Account</a>
        </div>

        <!-- Col 3: Atmospheric Categories -->
        <div class="flex flex-col gap-space-sm">
            <span class="text-xs font-bold uppercase tracking-wider text-on-surface">Collections</span>
            <a class="text-xs text-on-surface-variant hover:text-on-surface transition-colors" href="${pageContext.request.contextPath}/products?category=1">Living Room</a>
            <a class="text-xs text-on-surface-variant hover:text-on-surface transition-colors" href="${pageContext.request.contextPath}/products?category=2">Accessories</a>
            <a class="text-xs text-on-surface-variant hover:text-on-surface transition-colors" href="${pageContext.request.contextPath}/products?category=4">Electronics</a>
            <a class="text-xs text-on-surface-variant hover:text-on-surface transition-colors" href="${pageContext.request.contextPath}/products?category=3">Healthcare</a>
        </div>

        <!-- Col 4: RTU Educational Demonstrations -->
        <div class="flex flex-col gap-space-sm">
            <span class="text-xs font-bold uppercase tracking-wider text-on-surface">RTU Enterprise Specs</span>
            <a class="text-xs text-on-surface-variant hover:text-on-surface transition-colors" href="${pageContext.request.contextPath}/edu/jsp-syntax-demo.jsp">JSP Declarations &amp; Scriptlets</a>
            <a class="text-xs text-on-surface-variant hover:text-on-surface transition-colors" href="${pageContext.request.contextPath}/edu/jstl-core-demo.jsp">JSTL Core Tags &amp; EL</a>
            <a class="text-xs text-on-surface-variant hover:text-on-surface transition-colors" href="${pageContext.request.contextPath}/edu/jstl-xml-demo.jsp">JSTL XML Feed Parser</a>
            <a class="text-xs text-on-surface-variant hover:text-on-surface transition-colors" href="${pageContext.request.contextPath}/edu/jstl-sql-demo.jsp">JSTL SQL vs DAO Comparison</a>
        </div>
    </div>

    <!-- Bottom Copyright -->
    <div class="max-w-[1440px] mx-auto px-gutter pt-space-md border-t border-surface-container flex flex-col md:flex-row items-center justify-between text-xs text-on-surface-variant gap-space-sm">
        <div>&copy; 2026 ShopSphere — RTU Advanced Java Enterprise System. Nordic Retail Luxe edition.</div>
        <div class="flex items-center gap-space-md">
            <span>Currency: INR (₹)</span>
            <span class="text-outline">|</span>
            <span>Security: AES-256 / SHA-256</span>
        </div>
    </div>
</footer>

<!-- Toast Container for Notifications -->
<div id="toastNotification" class="fixed bottom-5 right-5 z-50 transform translate-y-20 opacity-0 transition-all duration-300 pointer-events-none">
    <div class="bg-primary text-on-primary px-4 py-3 rounded-xl shadow-xl flex items-center gap-3">
        <span class="material-symbols-outlined text-secondary text-lg" id="toastIcon">check_circle</span>
        <span class="text-xs font-medium" id="toastText">Action completed successfully</span>
    </div>
</div>

<!-- Interactive Global Scripts -->
<script>
    function showToast(message, isError = false) {
        const toast = document.getElementById('toastNotification');
        const text = document.getElementById('toastText');
        const icon = document.getElementById('toastIcon');
        if (!toast) return;

        text.textContent = message;
        icon.textContent = isError ? 'error' : 'check_circle';
        icon.className = isError ? 'material-symbols-outlined text-error text-lg' : 'material-symbols-outlined text-secondary text-lg';

        toast.classList.remove('translate-y-20', 'opacity-0', 'pointer-events-none');
        setTimeout(() => {
            toast.classList.add('translate-y-20', 'opacity-0', 'pointer-events-none');
        }, 3200);
    }

    function addToCart(productId, quantity = 1) {
        fetch('${pageContext.request.contextPath}/cart', {
            method: 'POST',
            headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
            body: 'action=add&productId=' + encodeURIComponent(productId) + '&quantity=' + encodeURIComponent(quantity)
        })
        .then(response => {
            if (response.ok) {
                showToast('Item added to your shopping bag');
                const badge = document.getElementById('cartBadge');
                if (badge) {
                    const current = parseInt(badge.textContent.trim()) || 0;
                    badge.textContent = current + quantity;
                }
            } else {
                showToast('Please sign in to add items to bag', true);
            }
        })
        .catch(err => {
            showToast('Item added to your bag');
        });
    }

    function toggleWishlist(productId, btn) {
        showToast('Wishlist updated');
        const icon = btn.querySelector('.material-symbols-outlined');
        if (icon) {
            icon.classList.toggle('text-secondary');
        }
    }
</script>
</body>
</html>
