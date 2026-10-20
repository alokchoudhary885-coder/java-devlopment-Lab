<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="utf-8"/>
    <meta content="width=device-width, initial-scale=1.0" name="viewport"/>
    <title>Account Gateway — ShopSphere | Nordic Retail Luxe</title>
    
    <!-- Google Fonts & Material Symbols -->
    <link rel="preconnect" href="https://fonts.googleapis.com"/>
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin=""/>
    <link href="https://fonts.googleapis.com/css2?family=Inter:wght@400;500;600&family=Plus+Jakarta+Sans:wght@600;700;800&display=swap" rel="stylesheet"/>
    <link href="https://fonts.googleapis.com/css2?family=Material+Symbols+Outlined:opsz,wght,FILL,GRAD@20..48,100..700,0..1,-50..200" rel="stylesheet"/>
    
    <script src="https://cdn.tailwindcss.com"></script>
    <script id="tailwind-config">
        tailwind.config = {
            darkMode: "class",
            theme: {
                extend: {
                    colors: {
                        "outline": "#76777d",
                        "primary-container": "#131b2e",
                        "outline-variant": "#c6c6cd",
                        "on-secondary-fixed-variant": "#3323cc",
                        "error-container": "#ffdad6",
                        "surface-container-highest": "#d3e4fe",
                        "on-secondary-fixed": "#0f0069",
                        "on-tertiary": "#ffffff",
                        "on-surface": "#0b1c30",
                        "tertiary-container": "#002109",
                        "surface-container-lowest": "#ffffff",
                        "primary-fixed": "#dae2fd",
                        "on-background": "#0b1c30",
                        "error": "#ba1a1a",
                        "on-tertiary-container": "#009842",
                        "on-surface-variant": "#45464d",
                        "secondary-container": "#645efb",
                        "surface-container-high": "#dce9ff",
                        "on-tertiary-fixed-variant": "#005320",
                        "tertiary-fixed-dim": "#62df7d",
                        "on-secondary": "#ffffff",
                        "on-primary": "#ffffff",
                        "surface-variant": "#d3e4fe",
                        "on-primary-fixed": "#131b2e",
                        "surface-bright": "#f8f9ff",
                        "secondary-fixed": "#e2dfff",
                        "primary-fixed-dim": "#bec6e0",
                        "surface-container-low": "#eff4ff",
                        "secondary-fixed-dim": "#c3c0ff",
                        "on-error": "#ffffff",
                        "tertiary": "#000000",
                        "inverse-surface": "#213145",
                        "on-primary-fixed-variant": "#3f465c",
                        "on-error-container": "#93000a",
                        "secondary": "#4b41e1",
                        "on-primary-container": "#7c839b",
                        "primary": "#000000",
                        "surface-container": "#e5eeff",
                        "inverse-primary": "#bec6e0",
                        "inverse-on-surface": "#eaf1ff",
                        "on-tertiary-fixed": "#002109",
                        "background": "#f8f9ff",
                        "on-secondary-container": "#fffbff",
                        "surface": "#f8f9ff",
                        "surface-dim": "#cbdbf5",
                        "tertiary-fixed": "#7ffc97",
                        "surface-tint": "#565e74"
                    },
                    borderRadius: {
                        "DEFAULT": "0.25rem",
                        "lg": "0.5rem",
                        "xl": "0.75rem",
                        "full": "9999px"
                    },
                    spacing: {
                        "gutter": "1.5rem",
                        "space-md": "1rem",
                        "margin": "3rem",
                        "space-sm": "0.5rem",
                        "margin-tablet": "1.5rem",
                        "gutter-mobile": "0.75rem",
                        "space-xs": "0.25rem",
                        "space-lg": "1.5rem",
                        "space-xl": "2.5rem",
                        "margin-mobile": "1rem"
                    },
                    fontFamily: {
                        "badge-sm": ["Plus Jakarta Sans", "sans-serif"],
                        "display-hero": ["Plus Jakarta Sans", "sans-serif"],
                        "price-lg": ["Plus Jakarta Sans", "sans-serif"],
                        "price-md": ["Plus Jakarta Sans", "sans-serif"],
                        "headline-lg": ["Plus Jakarta Sans", "sans-serif"],
                        "display-hero-mobile": ["Plus Jakarta Sans", "sans-serif"],
                        "label-md": ["Plus Jakarta Sans", "sans-serif"],
                        "body-md": ["Inter", "sans-serif"],
                        "body-lg": ["Inter", "sans-serif"],
                        "headline-sm": ["Plus Jakarta Sans", "sans-serif"],
                        "headline-lg-mobile": ["Plus Jakarta Sans", "sans-serif"],
                        "headline-md": ["Plus Jakarta Sans", "sans-serif"]
                    }
                }
            }
        };
    </script>
    <style>
        @layer base {
            html, body { margin: 0; padding: 0; }
        }
    </style>
</head>
<body class="bg-background font-body-md text-on-surface antialiased">

    <!-- Top Announcement Strip -->
    <header class="fixed top-0 left-0 right-0 z-50 bg-surface-container-lowest/95 backdrop-blur-md shadow-[0_1px_8px_rgba(0,0,0,0.04)]">
        <div class="bg-primary text-on-primary py-space-xs px-gutter">
            <div class="max-w-[1440px] mx-auto flex items-center justify-between text-xs font-semibold">
                <div class="flex items-center gap-space-sm">
                    <span class="bg-surface-container-highest text-on-surface px-space-sm py-0.5 rounded-full text-[11px] font-bold">Free Shipping</span>
                    <span>HOT: Get 50% Off on Selected Items | <a class="underline hover:text-surface-variant transition-colors" href="${pageContext.request.contextPath}/products">Shop Now</a></span>
                </div>
                <div class="hidden md:flex items-center gap-space-md text-xs">
                    <span>Helpline: +91 1800-SPHERE</span>
                    <span class="text-outline">|</span>
                    <a class="hover:text-surface-variant transition-colors" href="${pageContext.request.contextPath}/wishlist">Wishlist Preview</a>
                </div>
            </div>
        </div>

        <!-- Main Navigation Bar -->
        <div class="max-w-[1440px] mx-auto px-gutter h-16 flex items-center justify-between gap-space-lg">
            <a href="${pageContext.request.contextPath}/" class="flex items-center gap-2 text-decoration-none">
                <div class="w-9 h-9 rounded-lg bg-primary text-on-primary flex items-center justify-center font-bold text-lg tracking-wider">
                    S
                </div>
                <span class="font-headline-md text-xl font-bold tracking-tight text-on-surface">ShopSphere</span>
            </a>

            <!-- Search Bar -->
            <form action="${pageContext.request.contextPath}/products" method="GET" class="hidden lg:flex flex-1 max-w-xl items-center bg-surface-container-low rounded-lg px-space-sm py-1.5 gap-space-sm">
                <select name="category" class="bg-transparent text-xs font-medium text-on-surface-variant outline-none cursor-pointer pr-space-xs">
                    <option value="">All Departments</option>
                    <option value="1">Living Room</option>
                    <option value="2">Accessories</option>
                    <option value="4">Electronics</option>
                </select>
                <div class="w-px h-5 bg-outline-variant"></div>
                <div class="flex flex-1 items-center gap-space-xs">
                    <span class="material-symbols-outlined text-outline text-lg">search</span>
                    <input class="w-full bg-transparent text-sm placeholder:text-outline focus:outline-none text-on-surface" name="q" placeholder="Search bespoke furniture, ambient lighting..." type="text"/>
                </div>
            </form>

            <!-- Quick Action Icons -->
            <div class="flex items-center gap-space-md">
                <a class="relative p-2 rounded-lg text-on-surface-variant hover:text-on-surface hover:bg-surface-container transition-colors" href="${pageContext.request.contextPath}/wishlist" title="Wishlist">
                    <span class="material-symbols-outlined text-2xl">favorite</span>
                    <c:if test="${sessionScope.wishlistCount > 0}">
                        <span class="absolute top-1 right-1 w-4 h-4 bg-secondary text-on-secondary rounded-full flex items-center justify-center text-[10px] font-bold">${sessionScope.wishlistCount}</span>
                    </c:if>
                </a>
                <a class="relative p-2 rounded-lg text-on-surface-variant hover:text-on-surface hover:bg-surface-container transition-colors" href="${pageContext.request.contextPath}/cart" title="Shopping Bag">
                    <span class="material-symbols-outlined text-2xl">shopping_bag</span>
                    <span class="absolute top-1 right-1 w-4 h-4 bg-primary text-on-primary rounded-full flex items-center justify-center text-[10px] font-bold">${sessionScope.cartCount != null ? sessionScope.cartCount : '0'}</span>
                </a>
                <div class="flex items-center gap-space-sm pl-space-xs">
                    <c:choose>
                        <c:when test="${not empty sessionScope.user}">
                            <div class="flex items-center gap-2">
                                <span class="text-xs font-semibold text-on-surface">${sessionScope.user.fullName}</span>
                                <a href="${pageContext.request.contextPath}/logout" class="text-xs text-error hover:underline">Logout</a>
                            </div>
                        </c:when>
                        <c:otherwise>
                            <a class="flex items-center gap-1 text-on-surface hover:text-secondary text-xs font-semibold transition-colors" href="${pageContext.request.contextPath}/login">
                                <span class="material-symbols-outlined text-xl">account_circle</span>
                                <span>Sign In</span>
                            </a>
                        </c:otherwise>
                    </c:choose>
                </div>
            </div>
        </div>

        <!-- Subnav Category Links -->
        <nav class="bg-surface-container-lowest shadow-[0_1px_4px_rgba(0,0,0,0.02)]">
            <div class="max-w-[1440px] mx-auto px-gutter h-11 flex items-center gap-space-lg overflow-x-auto">
                <a class="text-on-surface-variant hover:text-on-surface text-xs font-medium px-3 py-1.5 transition-colors whitespace-nowrap" href="${pageContext.request.contextPath}/">Home</a>
                <a class="text-on-surface-variant hover:text-on-surface text-xs font-medium px-3 py-1.5 transition-colors whitespace-nowrap" href="${pageContext.request.contextPath}/products">Catalog</a>
                <a class="text-on-surface-variant hover:text-on-surface text-xs font-medium px-3 py-1.5 transition-colors whitespace-nowrap" href="${pageContext.request.contextPath}/products?category=1">Living Room</a>
                <a class="text-on-surface-variant hover:text-on-surface text-xs font-medium px-3 py-1.5 transition-colors whitespace-nowrap" href="${pageContext.request.contextPath}/products?category=2">Accessories</a>
                <a class="text-on-surface-variant hover:text-on-surface text-xs font-medium px-3 py-1.5 transition-colors whitespace-nowrap" href="${pageContext.request.contextPath}/products?category=4">Electronics</a>
                <div class="ml-auto flex items-center">
                    <a class="text-secondary hover:text-on-surface text-xs font-semibold px-3 py-1.5 transition-colors flex items-center gap-1 whitespace-nowrap" href="${pageContext.request.contextPath}/admin/dashboard">
                        <span class="material-symbols-outlined text-sm">dashboard</span>
                        <span>Admin Portal</span>
                    </a>
                </div>
            </div>
        </nav>
    </header>

    <!-- Main Container -->
    <main class="w-full pt-[143px] bg-background">
        <div class="max-w-[1440px] w-full mx-auto px-gutter py-space-xl">
            
            <!-- Breadcrumbs -->
            <div class="flex items-center gap-space-xs text-on-surface-variant text-xs font-medium mb-space-lg">
                <a class="hover:text-on-surface transition-colors" href="${pageContext.request.contextPath}/">Home</a>
                <span class="material-symbols-outlined text-[16px]">chevron_right</span>
                <span class="text-on-surface font-semibold">Account Gateway</span>
            </div>

            <!-- Flash Error / Success Notifications -->
            <c:if test="${not empty error}">
                <div class="mb-space-md p-space-md rounded-lg bg-error-container text-on-error-container flex items-center gap-space-sm border border-error/20">
                    <span class="material-symbols-outlined text-error">warning</span>
                    <span class="text-sm font-medium">${error}</span>
                </div>
            </c:if>
            <c:if test="${not empty sessionScope.error}">
                <div class="mb-space-md p-space-md rounded-lg bg-error-container text-on-error-container flex items-center gap-space-sm border border-error/20">
                    <span class="material-symbols-outlined text-error">shield_lock</span>
                    <span class="text-sm font-medium">${sessionScope.error}</span>
                </div>
                <c:remove var="error" scope="session" />
            </c:if>
            <c:if test="${not empty sessionScope.loginNotice}">
                <div class="mb-space-md p-space-md rounded-lg bg-surface-container-high text-on-surface flex items-center gap-space-sm border border-secondary/20">
                    <span class="material-symbols-outlined text-secondary">info</span>
                    <span class="text-sm font-medium">${sessionScope.loginNotice}</span>
                </div>
                <c:remove var="loginNotice" scope="session" />
            </c:if>
            <c:if test="${not empty sessionScope.success}">
                <div class="mb-space-md p-space-md rounded-lg bg-tertiary-container/20 text-on-tertiary-container flex items-center gap-space-sm border border-on-tertiary-container/30">
                    <span class="material-symbols-outlined text-on-tertiary-container">check_circle</span>
                    <span class="text-sm font-medium">${sessionScope.success}</span>
                </div>
                <c:remove var="success" scope="session" />
            </c:if>

            <div class="grid grid-cols-1 lg:grid-cols-12 gap-space-lg items-start">
                
                <!-- COLUMN 1: Log In -->
                <section class="lg:col-span-4 bg-surface-container-lowest rounded-xl p-space-lg shadow-sm flex flex-col justify-between border border-surface-container">
                    <div class="space-y-space-md">
                        <div class="space-y-space-xs">
                            <span class="text-[11px] font-bold uppercase tracking-wider text-secondary">Existing Customer</span>
                            <h1 class="text-2xl font-bold text-on-surface">Log In</h1>
                            <p class="text-sm text-on-surface-variant">Welcome back. Enter your verified credentials to access your curations.</p>
                        </div>

                        <form action="${pageContext.request.contextPath}/login" method="POST" class="space-y-space-md pt-space-xs">
                            <input type="hidden" name="csrfToken" value="${sessionScope.csrfToken}">
                            
                            <div class="space-y-space-xs">
                                <label class="text-xs font-semibold text-on-surface block" for="login-email">Email Address</label>
                                <div class="relative bg-surface-container-low rounded-lg p-space-xs flex items-center border border-outline-variant/40 focus-within:border-secondary transition-colors">
                                    <span class="material-symbols-outlined text-outline px-space-xs text-[20px]">alternate_email</span>
                                    <input class="w-full bg-transparent text-sm text-on-surface placeholder:text-outline focus:outline-none px-space-xs" 
                                           id="login-email" name="email" placeholder="name@example.com" type="email" value="${email != null ? email : 'customer@shopsphere.com'}" required autocomplete="email"/>
                                </div>
                            </div>

                            <div class="space-y-space-xs">
                                <div class="flex items-center justify-between">
                                    <label class="text-xs font-semibold text-on-surface block" for="login-password">Password</label>
                                    <a class="text-xs text-secondary hover:underline transition-all" href="#">Forgot password?</a>
                                </div>
                                <div class="relative bg-surface-container-low rounded-lg p-space-xs flex items-center border border-outline-variant/40 focus-within:border-secondary transition-colors">
                                    <span class="material-symbols-outlined text-outline px-space-xs text-[20px]">lock</span>
                                    <input class="w-full bg-transparent text-sm text-on-surface placeholder:text-outline focus:outline-none px-space-xs" 
                                           id="login-password" name="password" type="password" value="customer123" required autocomplete="current-password"/>
                                    <button aria-label="Toggle password visibility" class="p-1 text-outline hover:text-on-surface transition-colors" 
                                            onclick="const p = document.getElementById('login-password'); const icon = this.querySelector('span'); p.type = p.type === 'password' ? 'text' : 'password'; icon.textContent = p.type === 'password' ? 'visibility' : 'visibility_off';" type="button">
                                        <span class="material-symbols-outlined text-[20px]">visibility</span>
                                    </button>
                                </div>
                            </div>

                            <button class="w-full h-12 bg-primary text-on-primary hover:bg-secondary rounded-lg font-semibold text-sm flex items-center justify-center gap-space-xs shadow-sm transition-all duration-200 cursor-pointer" type="submit">
                                <span>Sign In</span>
                                <span class="material-symbols-outlined text-[18px]">arrow_forward</span>
                            </button>
                        </form>
                    </div>

                    <!-- Demo Credentials Quick Selector -->
                    <div class="mt-space-lg bg-surface-container-low rounded-lg p-space-md space-y-space-xs border border-surface-container">
                        <div class="flex items-center gap-space-xs text-on-surface font-semibold text-xs">
                            <span class="material-symbols-outlined text-secondary text-[18px]">key</span>
                            <span>Demo Credentials (Click to fill)</span>
                        </div>
                        <div class="text-[11px] space-y-1.5 text-on-surface-variant font-mono">
                            <button type="button" onclick="fillCreds('admin@shopsphere.com', 'admin123')" 
                                    class="w-full text-left flex justify-between items-center bg-surface-container-lowest p-2 rounded hover:bg-surface-container-high transition-colors cursor-pointer border border-outline-variant/30">
                                <span class="font-bold text-secondary">Admin:</span>
                                <span class="text-on-surface">admin@shopsphere.com / admin123</span>
                            </button>
                            <button type="button" onclick="fillCreds('customer@shopsphere.com', 'customer123')" 
                                    class="w-full text-left flex justify-between items-center bg-surface-container-lowest p-2 rounded hover:bg-surface-container-high transition-colors cursor-pointer border border-outline-variant/30">
                                <span class="font-bold text-on-tertiary-container">Customer:</span>
                                <span class="text-on-surface">customer@shopsphere.com / customer123</span>
                            </button>
                        </div>
                    </div>
                </section>

                <!-- COLUMN 2: Register -->
                <section class="lg:col-span-5 bg-surface-container-lowest rounded-xl p-space-lg shadow-sm border border-surface-container">
                    <div class="space-y-space-xs mb-space-md">
                        <span class="text-[11px] font-bold uppercase tracking-wider text-secondary">New Client</span>
                        <h2 class="text-2xl font-bold text-on-surface">Register</h2>
                        <p class="text-sm text-on-surface-variant">Join ShopSphere to synchronize selections and experience bespoke Scandinavian living.</p>
                    </div>

                    <form action="${pageContext.request.contextPath}/register" method="POST" class="space-y-space-md">
                        <input type="hidden" name="csrfToken" value="${sessionScope.csrfToken}">

                        <div class="space-y-space-xs">
                            <label class="text-xs font-semibold text-on-surface block" for="register-name">Full Name</label>
                            <div class="bg-surface-container-low rounded-lg p-space-xs flex items-center border border-outline-variant/40 focus-within:border-secondary transition-colors">
                                <span class="material-symbols-outlined text-outline px-space-xs text-[20px]">person</span>
                                <input class="w-full bg-transparent text-sm text-on-surface placeholder:text-outline focus:outline-none px-space-xs" 
                                       id="register-name" name="fullName" placeholder="John Doe" type="text" value="${fullName}" required autocomplete="name"/>
                            </div>
                        </div>

                        <div class="space-y-space-xs">
                            <label class="text-xs font-semibold text-on-surface block" for="register-email">Email Address</label>
                            <div class="bg-surface-container-low rounded-lg p-space-xs flex items-center border border-outline-variant/40 focus-within:border-secondary transition-colors">
                                <span class="material-symbols-outlined text-outline px-space-xs text-[20px]">mail</span>
                                <input class="w-full bg-transparent text-sm text-on-surface placeholder:text-outline focus:outline-none px-space-xs" 
                                       id="register-email" name="email" placeholder="john.doe@example.com" type="email" value="${email}" required autocomplete="email"/>
                            </div>
                        </div>

                        <div class="space-y-space-xs">
                            <label class="text-xs font-semibold text-on-surface block" for="register-phone">Mobile Number <span class="text-outline font-normal">(Optional)</span></label>
                            <div class="flex gap-space-xs">
                                <div class="bg-surface-container-low rounded-lg px-space-sm flex items-center border border-outline-variant/40">
                                    <span class="text-xs font-semibold text-on-surface">IN +91</span>
                                </div>
                                <div class="bg-surface-container-low rounded-lg p-space-xs flex items-center flex-1 border border-outline-variant/40 focus-within:border-secondary transition-colors">
                                    <span class="material-symbols-outlined text-outline px-space-xs text-[20px]">phone</span>
                                    <input class="w-full bg-transparent text-sm text-on-surface placeholder:text-outline focus:outline-none px-space-xs" 
                                           id="register-phone" name="phone" placeholder="98765 43210" type="tel" value="${phone}" autocomplete="tel"/>
                                </div>
                            </div>
                        </div>

                        <div class="space-y-space-xs">
                            <label class="text-xs font-semibold text-on-surface block" for="reg-password">Password</label>
                            <div class="bg-surface-container-low rounded-lg p-space-xs flex items-center border border-outline-variant/40 focus-within:border-secondary transition-colors">
                                <span class="material-symbols-outlined text-outline px-space-xs text-[20px]">key</span>
                                <input class="w-full bg-transparent text-sm text-on-surface placeholder:text-outline focus:outline-none px-space-xs" 
                                       id="reg-password" name="password" type="password" placeholder="Minimum 6 characters" required autocomplete="new-password" oninput="checkPasswordMatch()"/>
                                <span id="pass-indicator" class="material-symbols-outlined text-outline px-space-xs text-[20px]">lock</span>
                            </div>
                            <p class="text-[11px] text-outline">Minimum 6 characters required</p>
                        </div>

                        <div class="space-y-space-xs">
                            <label class="text-xs font-semibold text-on-surface block" for="reg-confirm">Confirm Password</label>
                            <div class="bg-surface-container-low rounded-lg p-space-xs flex items-center border border-outline-variant/40 focus-within:border-secondary transition-colors">
                                <span class="material-symbols-outlined text-outline px-space-xs text-[20px]">lock_reset</span>
                                <input class="w-full bg-transparent text-sm text-on-surface placeholder:text-outline focus:outline-none px-space-xs" 
                                       id="reg-confirm" name="confirmPassword" type="password" placeholder="Repeat your password" required autocomplete="new-password" oninput="checkPasswordMatch()"/>
                                <div id="match-badge" class="hidden items-center gap-1 bg-surface-container px-2 py-0.5 rounded-full mr-1">
                                    <span class="material-symbols-outlined text-on-tertiary-container text-[16px]">check_circle</span>
                                    <span class="text-[11px] font-bold text-on-tertiary-container">Matched</span>
                                </div>
                            </div>
                        </div>

                        <button class="w-full h-12 bg-primary text-on-primary hover:bg-secondary rounded-lg font-semibold text-sm flex items-center justify-center gap-space-xs shadow-sm transition-all duration-200 cursor-pointer mt-space-md" type="submit">
                            <span>Create Account</span>
                            <span class="material-symbols-outlined text-[18px]">person_add</span>
                        </button>
                    </form>
                </section>

                <!-- COLUMN 3: Wishlist Preview Panel -->
                <section class="lg:col-span-3 bg-surface-container-low rounded-xl p-space-lg shadow-sm flex flex-col justify-between space-y-space-md border border-surface-container">
                    <div>
                        <div class="flex items-center justify-between mb-space-xs">
                            <div class="flex items-center gap-space-xs">
                                <span class="material-symbols-outlined text-secondary text-[22px]">bookmark_heart</span>
                                <h2 class="text-lg font-bold text-on-surface">Curated Wishlist</h2>
                            </div>
                            <span class="bg-surface-container text-secondary text-xs px-2 py-0.5 rounded-full font-bold">2 Items</span>
                        </div>
                        <p class="text-sm text-on-surface-variant mb-space-md">Register or sign in to save your personal wishlist across devices.</p>

                        <div class="space-y-space-sm">
                            <!-- Item 1 -->
                            <div class="bg-surface-container-lowest rounded-lg p-space-sm flex items-center gap-space-sm shadow-sm group border border-outline-variant/30">
                                <img class="w-14 h-14 rounded-lg object-cover bg-surface-container-high shrink-0 group-hover:scale-105 transition-transform duration-300" 
                                     alt="Minimalist luxury black full-grain leather wallet" 
                                     src="https://lh3.googleusercontent.com/aida-public/AB6AXuDOMqWGIK738U-euyvKzKyH31C0MBqhTAyxK2M2wGXXppJDLc2bdCeRGytUo_-mjK_WCOYknF6yg_JMqB0GYvi-EUIr2aPk7IGpzVK04CUDjbR7hqsOzPGvhISVtTVZqLazkC5b9U6j4LZUtYpx-Q3TIGpGrv883C81mNmC46zWEBiMM2e5xTOF9aOjcZsGii1iYzAaRUTnVkJ3ZCs0anojBdn9S7C0ysIunpjTgp1rK3CzehI3BrmE"/>
                                <div class="flex-1 min-w-0">
                                    <span class="text-[10px] uppercase tracking-wide text-outline block font-bold">Leather Goods</span>
                                    <h3 class="text-xs font-bold text-on-surface truncate">Oscar Wallet</h3>
                                    <div class="flex items-center gap-1">
                                        <span class="text-[11px] text-on-surface-variant">Black</span>
                                        <span class="text-outline">·</span>
                                        <span class="text-xs font-bold text-on-surface">₹2,499.00</span>
                                    </div>
                                </div>
                                <a href="${pageContext.request.contextPath}/products" class="bg-surface-container-high hover:bg-primary hover:text-on-primary text-on-surface text-xs font-semibold px-2.5 py-1.5 rounded transition-colors shrink-0">
                                    View
                                </a>
                            </div>

                            <!-- Item 2 -->
                            <div class="bg-surface-container-lowest rounded-lg p-space-sm flex items-center gap-space-sm shadow-sm group border border-outline-variant/30">
                                <img class="w-14 h-14 rounded-lg object-cover bg-surface-container-high shrink-0 group-hover:scale-105 transition-transform duration-300" 
                                     alt="High-end matte black over-ear wireless noise cancelling headphones" 
                                     src="https://lh3.googleusercontent.com/aida-public/AB6AXuC3TUZiAer8wGtMlPree9SE1AhJRq1G2I6-_6OTwxzWZzt91SoDD7eKFPVHiJrng4ZWPWV7iL51e90wZp4EhMXgYF4Yn0ctjB-754O1kk3O6KGrkWt9C39oF8efqTvPkkffREaZV5Pe-h7WCmM33k4Nx-NLHuB8I_FWYrsH1P7DZ9Dv1vsO-LiWAjr4uUxrwRmBIJy49ys54gzBC9S-OIJQ7LPjc-1ZLFvBUP_yclha-QmHjc9nYqe4"/>
                                <div class="flex-1 min-w-0">
                                    <span class="text-[10px] uppercase tracking-wide text-outline block font-bold">Acoustics</span>
                                    <h3 class="text-xs font-bold text-on-surface truncate">JBL Live 660NC</h3>
                                    <div class="flex items-center gap-1">
                                        <span class="text-[11px] text-on-surface-variant">Wireless</span>
                                        <span class="text-outline">·</span>
                                        <span class="text-xs font-bold text-on-surface">₹6,499.00</span>
                                    </div>
                                </div>
                                <a href="${pageContext.request.contextPath}/products" class="bg-surface-container-high hover:bg-primary hover:text-on-primary text-on-surface text-xs font-semibold px-2.5 py-1.5 rounded transition-colors shrink-0">
                                    View
                                </a>
                            </div>
                        </div>
                    </div>

                    <div class="bg-surface-container-highest/80 rounded-lg p-space-sm flex items-start gap-space-sm">
                        <span class="material-symbols-outlined text-secondary text-[20px] shrink-0 mt-0.5">info</span>
                        <p class="text-xs text-on-surface-variant">
                            Please log in or create an account to manage your active wishlist items.
                        </p>
                    </div>
                </section>

            </div>

            <!-- Value Guarantees Strip -->
            <div class="mt-space-xl bg-surface-container-lowest rounded-xl p-space-lg shadow-sm border border-surface-container">
                <div class="grid grid-cols-1 md:grid-cols-3 gap-space-lg">
                    <div class="flex items-center gap-space-md">
                        <div class="w-12 h-12 rounded-full bg-surface-container-low text-secondary flex items-center justify-center shrink-0">
                            <span class="material-symbols-outlined text-[24px]">local_shipping</span>
                        </div>
                        <div class="space-y-0.5">
                            <h4 class="text-sm font-bold text-on-surface">Free & Express Shipping</h4>
                            <p class="text-xs text-on-surface-variant">On all domestic orders over ₹499</p>
                        </div>
                    </div>
                    <div class="flex items-center gap-space-md">
                        <div class="w-12 h-12 rounded-full bg-surface-container-low text-secondary flex items-center justify-center shrink-0">
                            <span class="material-symbols-outlined text-[24px]">verified_user</span>
                        </div>
                        <div class="space-y-0.5">
                            <h4 class="text-sm font-bold text-on-surface">Genuine Warranty</h4>
                            <p class="text-xs text-on-surface-variant">100% verified original products</p>
                        </div>
                    </div>
                    <div class="flex items-center gap-space-md">
                        <div class="w-12 h-12 rounded-full bg-surface-container-low text-secondary flex items-center justify-center shrink-0">
                            <span class="material-symbols-outlined text-[24px]">assignment_return</span>
                        </div>
                        <div class="space-y-0.5">
                            <h4 class="text-sm font-bold text-on-surface">Hassle-Free Returns</h4>
                            <p class="text-xs text-on-surface-variant">7-day replacement guarantee</p>
                        </div>
                    </div>
                </div>
            </div>

        </div>
    </main>

    <!-- Footer -->
    <footer class="w-full bg-surface-container-lowest shadow-[0_-1px_8px_rgba(0,0,0,0.03)] mt-space-xl border-t border-surface-container">
        <div class="max-w-[1440px] mx-auto px-gutter py-space-xl grid grid-cols-1 md:grid-cols-2 lg:grid-cols-5 gap-space-xl">
            <div class="lg:col-span-2 flex flex-col gap-space-md">
                <div class="flex items-center gap-2">
                    <div class="w-7 h-7 rounded-md bg-primary text-on-primary flex items-center justify-center font-bold text-sm">S</div>
                    <span class="text-lg font-bold tracking-tight text-on-surface">ShopSphere</span>
                </div>
                <p class="text-xs text-on-surface-variant max-w-sm">
                    RTU Advanced Java Enterprise E-Commerce System featuring Scandinavian Nordic Luxe functionalism. Built with Jakarta EE 10, Servlets, JDBC, MVC, and automated tests.
                </p>
                <div class="flex items-center gap-space-md text-on-surface-variant pt-space-xs">
                    <a class="hover:text-primary transition-colors" href="${pageContext.request.contextPath}/products" title="Store"><span class="material-symbols-outlined text-xl">storefront</span></a>
                    <a class="hover:text-primary transition-colors" href="#" title="Customer Support"><span class="material-symbols-outlined text-xl">support_agent</span></a>
                    <a class="hover:text-primary transition-colors" href="#" title="Verified Secure"><span class="material-symbols-outlined text-xl">verified_user</span></a>
                </div>
            </div>
            <div class="flex flex-col gap-space-sm">
                <span class="text-xs font-bold uppercase tracking-wider text-on-surface">Customer Care</span>
                <a class="text-xs text-on-surface-variant hover:text-on-surface transition-colors" href="${pageContext.request.contextPath}/orders">Order Tracking</a>
                <a class="text-xs text-on-surface-variant hover:text-on-surface transition-colors" href="${pageContext.request.contextPath}/cart">Shopping Bag</a>
                <a class="text-xs text-on-surface-variant hover:text-on-surface transition-colors" href="${pageContext.request.contextPath}/wishlist">Saved Wishlist</a>
            </div>
            <div class="flex flex-col gap-space-sm">
                <span class="text-xs font-bold uppercase tracking-wider text-on-surface">Categories</span>
                <a class="text-xs text-on-surface-variant hover:text-on-surface transition-colors" href="${pageContext.request.contextPath}/products?category=1">Living Room</a>
                <a class="text-xs text-on-surface-variant hover:text-on-surface transition-colors" href="${pageContext.request.contextPath}/products?category=2">Accessories</a>
                <a class="text-xs text-on-surface-variant hover:text-on-surface transition-colors" href="${pageContext.request.contextPath}/products?category=4">Electronics</a>
            </div>
            <div class="flex flex-col gap-space-sm">
                <span class="text-xs font-bold uppercase tracking-wider text-on-surface">RTU Demos</span>
                <a class="text-xs text-on-surface-variant hover:text-on-surface transition-colors" href="${pageContext.request.contextPath}/edu/jsp-syntax-demo.jsp">JSP Declarations</a>
                <a class="text-xs text-on-surface-variant hover:text-on-surface transition-colors" href="${pageContext.request.contextPath}/edu/jstl-core-demo.jsp">JSTL Core Tags</a>
                <a class="text-xs text-on-surface-variant hover:text-on-surface transition-colors" href="${pageContext.request.contextPath}/edu/jstl-xml-demo.jsp">XML Feed Parser</a>
            </div>
        </div>
        <div class="max-w-[1440px] mx-auto px-gutter py-space-md border-t border-surface-container flex flex-col md:flex-row items-center justify-between text-xs text-on-surface-variant gap-space-sm">
            <div>&copy; 2026 ShopSphere — RTU Advanced Java Enterprise Project. All rights reserved.</div>
            <div class="flex items-center gap-space-md">
                <span>Currency: INR (₹)</span>
                <span>Region: India (Express)</span>
            </div>
        </div>
    </footer>

    <!-- Interactive Scripts -->
    <script>
        function fillCreds(email, pass) {
            document.getElementById('login-email').value = email;
            document.getElementById('login-password').value = pass;
        }

        function checkPasswordMatch() {
            const p1 = document.getElementById('reg-password').value;
            const p2 = document.getElementById('reg-confirm').value;
            const badge = document.getElementById('match-badge');
            const indicator = document.getElementById('pass-indicator');

            if (p1.length >= 6) {
                indicator.classList.remove('text-outline');
                indicator.classList.add('text-on-tertiary-container');
                indicator.textContent = 'check_circle';
            } else {
                indicator.classList.remove('text-on-tertiary-container');
                indicator.classList.add('text-outline');
                indicator.textContent = 'lock';
            }

            if (p2.length > 0 && p1 === p2) {
                badge.classList.remove('hidden');
                badge.classList.add('flex');
            } else {
                badge.classList.add('hidden');
                badge.classList.remove('flex');
            }
        }
    </script>
</body>
</html>
