/**
 * ShopSphere - Main Frontend Client Script
 * Handles Cart counter updates, dynamic add-to-cart, toasts, and UI interactions
 */

document.addEventListener('DOMContentLoaded', function () {
    console.log('[ShopSphere] Frontend script initialized.');
});

/**
 * Adds an item to the shopping cart via asynchronous POST request.
 * Updates the navbar badge and shows a toast confirmation without full page reload.
 *
 * @param {number} productId The ID of the product to add
 * @param {number} quantity The quantity to add (default 1)
 */
function addToCart(productId, quantity = 1) {
    const contextPath = window.location.pathname.substring(0, window.location.pathname.indexOf('/', 1));
    const url = (contextPath ? contextPath : '') + '/cart';

    const formData = new URLSearchParams();
    formData.append('action', 'add');
    formData.append('productId', productId);
    formData.append('quantity', quantity);

    fetch(url, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded',
            'X-Requested-With': 'XMLHttpRequest'
        },
        body: formData.toString()
    })
    .then(response => {
        if (response.redirected) {
            // Unauthenticated user redirected to /login
            window.location.href = response.url;
            return null;
        }
        return response.json();
    })
    .then(data => {
        if (!data) return;

        if (data.success) {
            // Update cart badge in navbar
            const badge = document.getElementById('cartBadge');
            if (badge && data.cartCount !== undefined) {
                badge.textContent = data.cartCount;
            }
            showToast(data.message || 'Product added to cart!');
        } else {
            showToast(data.message || 'Could not add to cart.', 'danger');
        }
    })
    .catch(error => {
        console.error('Error adding to cart:', error);
        // Fallback: regular form submit if JSON endpoint unavailable
        window.location.href = `${contextPath || ''}/cart?action=add&productId=${productId}&quantity=${quantity}`;
    });
}

/**
 * Toggles an item in the user's wishlist.
 *
 * @param {number} productId The product ID
 * @param {HTMLElement} btn The heart button element
 */
function toggleWishlist(productId, btn) {
    const contextPath = window.location.pathname.substring(0, window.location.pathname.indexOf('/', 1));
    const url = (contextPath ? contextPath : '') + '/wishlist';

    const formData = new URLSearchParams();
    formData.append('action', 'toggle');
    formData.append('productId', productId);

    fetch(url, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded',
            'X-Requested-With': 'XMLHttpRequest'
        },
        body: formData.toString()
    })
    .then(response => {
        if (response.redirected) {
            window.location.href = response.url;
            return null;
        }
        return response.json();
    })
    .then(data => {
        if (!data) return;

        if (data.success) {
            if (btn) {
                const icon = btn.querySelector('i');
                if (data.isAdded) {
                    btn.classList.add('active');
                    if (icon) icon.className = 'bi bi-heart-fill text-danger';
                } else {
                    btn.classList.remove('active');
                    if (icon) icon.className = 'bi bi-heart';
                }
            }
            showToast(data.message || 'Wishlist updated!');
        } else {
            showToast(data.message || 'Could not update wishlist.', 'danger');
        }
    })
    .catch(error => {
        console.error('Wishlist error:', error);
        window.location.href = `${contextPath || ''}/wishlist?action=add&productId=${productId}`;
    });
}

/**
 * Displays a lightweight Bootstrap 5 toast notification.
 *
 * @param {string} message The text message to display
 * @param {string} type 'success' (default) or 'danger'
 */
function showToast(message, type = 'success') {
    const toastEl = document.getElementById('actionToast');
    const toastBody = document.getElementById('toastMessage');

    if (!toastEl || !toastBody) {
        alert(message);
        return;
    }

    toastBody.textContent = message;
    if (type === 'danger') {
        toastEl.className = 'toast align-items-center text-white bg-danger border-0';
    } else {
        toastEl.className = 'toast align-items-center text-white bg-dark border-0';
    }

    const toast = new bootstrap.Toast(toastEl, { delay: 3000 });
    toast.show();
}
