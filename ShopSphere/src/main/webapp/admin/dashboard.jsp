<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="utf-8"/>
    <meta content="width=device-width, initial-scale=1.0" name="viewport"/>
    <title>Admin Analytics Dashboard — ShopSphere | Nordic Retail Luxe</title>
    
    <!-- Google Fonts & Material Symbols -->
    <link rel="preconnect" href="https://fonts.googleapis.com"/>
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin=""/>
    <link href="https://fonts.googleapis.com/css2?family=Inter:wght@400;500;600&family=Plus+Jakarta+Sans:wght@600;700;800&display=swap" rel="stylesheet"/>
    <link href="https://fonts.googleapis.com/css2?family=Material+Symbols+Outlined:opsz,wght,FILL,GRAD@20..48,100..700,0..1,-50..200" rel="stylesheet"/>

    <!-- Tailwind CSS with Nordic Retail Luxe Design Tokens -->
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
</head>
<body class="bg-background font-body-md text-on-surface antialiased">

    <!-- Fixed Dark Sidebar -->
    <aside class="fixed left-0 top-0 h-full w-64 bg-primary text-on-primary z-50 flex flex-col justify-between shadow-[0_1px_8px_rgba(0,0,0,0.06)]">
        <div class="p-space-lg">
            
            <!-- Brand Logo -->
            <a href="${pageContext.request.contextPath}/admin/dashboard" class="flex items-center gap-2.5 mb-space-xl">
                <div class="w-8 h-8 rounded-lg bg-surface-container-highest text-on-surface flex items-center justify-center font-bold text-base">
                    S
                </div>
                <div class="flex flex-col">
                    <span class="font-headline-sm text-sm font-bold tracking-tight text-on-primary leading-none">ShopSphere</span>
                    <span class="text-[10px] font-bold uppercase tracking-widest text-outline-variant mt-1">Admin Suite</span>
                </div>
            </a>

            <!-- Navigation Links -->
            <nav class="flex flex-col gap-1">
                <a class="flex items-center gap-space-sm px-3 py-2.5 bg-surface-container-lowest/15 text-on-primary font-bold rounded-lg transition-all" 
                   href="${pageContext.request.contextPath}/admin/dashboard">
                    <span class="material-symbols-outlined text-lg">dashboard</span>
                    <span class="text-xs">Dashboard</span>
                </a>
                <a class="flex items-center gap-space-sm px-3 py-2.5 rounded-lg text-outline-variant hover:text-on-primary hover:bg-surface-container-lowest/5 transition-all" 
                   href="${pageContext.request.contextPath}/admin/products">
                    <span class="material-symbols-outlined text-lg">inventory_2</span>
                    <span class="text-xs">Products</span>
                </a>
                <a class="flex items-center gap-space-sm px-3 py-2.5 rounded-lg text-outline-variant hover:text-on-primary hover:bg-surface-container-lowest/5 transition-all" 
                   href="${pageContext.request.contextPath}/admin/categories">
                    <span class="material-symbols-outlined text-lg">category</span>
                    <span class="text-xs">Categories</span>
                </a>
                <a class="flex items-center gap-space-sm px-3 py-2.5 rounded-lg text-outline-variant hover:text-on-primary hover:bg-surface-container-lowest/5 transition-all" 
                   href="${pageContext.request.contextPath}/admin/orders">
                    <span class="material-symbols-outlined text-lg">receipt_long</span>
                    <span class="text-xs">Orders</span>
                </a>
                <a class="flex items-center gap-space-sm px-3 py-2.5 rounded-lg text-outline-variant hover:text-on-primary hover:bg-surface-container-lowest/5 transition-all" 
                   href="${pageContext.request.contextPath}/admin/users">
                    <span class="material-symbols-outlined text-lg">group</span>
                    <span class="text-xs">Users &amp; Roles</span>
                </a>
                <a class="flex items-center gap-space-sm px-3 py-2.5 rounded-lg text-outline-variant hover:text-on-primary hover:bg-surface-container-lowest/5 transition-all" 
                   href="${pageContext.request.contextPath}/admin/coupons">
                    <span class="material-symbols-outlined text-lg">confirmation_number</span>
                    <span class="text-xs">Coupons</span>
                </a>
            </nav>
        </div>

        <!-- Sidebar Footer -->
        <div class="p-space-lg flex flex-col gap-space-xs border-t border-surface-container-lowest/10">
            <a class="flex items-center gap-space-sm px-3 py-2 rounded-lg text-outline-variant hover:text-on-primary transition-colors text-xs" 
               href="${pageContext.request.contextPath}/">
                <span class="material-symbols-outlined text-base">storefront</span>
                <span>Back to Store</span>
            </a>
            <a class="flex items-center gap-space-sm px-3 py-2 rounded-lg text-error hover:text-error-container transition-colors text-xs font-semibold" 
               href="${pageContext.request.contextPath}/logout">
                <span class="material-symbols-outlined text-base">logout</span>
                <span>Sign Out</span>
            </a>
        </div>
    </aside>

    <!-- Main Workspace Container -->
    <div class="pl-64">
        
        <!-- Top Sticky Header -->
        <header class="fixed top-0 left-64 right-0 h-16 bg-surface-container-lowest/90 backdrop-blur-md shadow-[0_1px_8px_rgba(0,0,0,0.04)] z-40 px-gutter flex items-center justify-between border-b border-surface-container">
            <div class="flex items-center gap-space-md">
                <div class="flex items-center gap-space-xs bg-surface-container-low px-space-sm py-1.5 rounded-lg text-on-surface-variant text-xs">
                    <span class="material-symbols-outlined text-base">calendar_today</span>
                    <span class="font-medium">Live Enterprise Session • 2026</span>
                </div>
            </div>

            <div class="flex items-center gap-space-md">
                <div class="flex items-center gap-2">
                    <div class="w-8 h-8 rounded-full bg-primary-container text-on-primary-container flex items-center justify-center font-bold text-xs uppercase">
                        ${sessionScope.user != null ? sessionScope.user.fullName.substring(0, 1) : 'A'}
                    </div>
                    <div class="hidden md:flex flex-col text-left">
                        <span class="text-xs font-bold text-on-surface leading-tight">
                            ${sessionScope.user != null ? sessionScope.user.fullName : 'Administrator'}
                        </span>
                        <span class="text-[10px] text-on-surface-variant font-mono">
                            ${sessionScope.user != null ? sessionScope.user.role : 'SYSTEM ADMIN'}
                        </span>
                    </div>
                </div>
            </div>
        </header>

        <!-- Main Dashboard Content -->
        <main class="pt-20 bg-background px-gutter py-space-lg min-h-screen">
            <div class="flex flex-col w-full gap-space-lg">
                
                <!-- Header & Controls -->
                <div class="flex flex-col xl:flex-row xl:items-center xl:justify-between gap-space-md">
                    <div class="flex flex-col">
                        <div class="flex items-center gap-space-xs">
                            <span class="text-[11px] font-bold uppercase tracking-wider text-secondary">Operations Executive</span>
                            <span class="w-1.5 h-1.5 rounded-full bg-secondary"></span>
                            <span class="text-[11px] font-bold uppercase text-outline">Realtime Stream</span>
                        </div>
                        <h1 class="font-headline-lg text-2xl font-bold text-on-surface tracking-tight mt-0.5">Analytics &amp; Fulfillment Console</h1>
                        <p class="text-xs text-on-surface-variant">Here is what is happening across the ShopSphere enterprise platform today.</p>
                    </div>

                    <div class="flex flex-wrap items-center gap-space-sm">
                        <a href="${pageContext.request.contextPath}/admin/orders" class="flex items-center gap-1.5 px-3.5 py-2 rounded-lg bg-surface-container-lowest text-on-surface shadow-sm hover:bg-surface-container-low transition-colors text-xs font-semibold border border-surface-container">
                            <span class="material-symbols-outlined text-base text-secondary">receipt_long</span>
                            <span>View All Orders</span>
                        </a>
                        <a href="${pageContext.request.contextPath}/admin/products" class="flex items-center gap-1.5 px-3.5 py-2 rounded-lg bg-primary text-on-primary hover:bg-secondary transition-colors text-xs font-semibold shadow-sm">
                            <span class="material-symbols-outlined text-base">add_box</span>
                            <span>Add New Product</span>
                        </a>
                    </div>
                </div>

                <!-- Primary KPI Metric Cards (4 Columns) -->
                <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-space-md">
                    
                    <!-- Card 1: Revenue -->
                    <div class="relative overflow-hidden bg-surface-container-lowest p-space-md rounded-xl shadow-sm border border-surface-container flex flex-col justify-between group">
                        <div class="flex items-start justify-between">
                            <div class="flex flex-col">
                                <span class="text-xs font-semibold text-on-surface-variant">Total Gross Revenue</span>
                                <span class="font-price-lg text-xl font-extrabold text-on-surface tracking-tight mt-1 font-mono">
                                    ₹<fmt:formatNumber value="${totalEarnings != null ? totalEarnings : 145800}" pattern="#,##0.00" />
                                </span>
                            </div>
                            <div class="w-10 h-10 rounded-lg bg-surface-container-low flex items-center justify-center text-secondary group-hover:scale-105 transition-transform">
                                <span class="material-symbols-outlined text-xl">payments</span>
                            </div>
                        </div>
                        <div class="mt-space-md flex items-center gap-space-xs">
                            <span class="inline-flex items-center gap-0.5 px-2 py-0.5 rounded-full bg-surface-container text-on-tertiary-container text-[11px] font-bold">
                                <span class="material-symbols-outlined text-[13px]">trending_up</span>
                                +18.4%
                            </span>
                            <span class="text-[11px] text-outline">vs last period</span>
                        </div>
                    </div>

                    <!-- Card 2: Orders -->
                    <div class="relative overflow-hidden bg-surface-container-lowest p-space-md rounded-xl shadow-sm border border-surface-container flex flex-col justify-between group">
                        <div class="flex items-start justify-between">
                            <div class="flex flex-col">
                                <span class="text-xs font-semibold text-on-surface-variant">Total Placed Orders</span>
                                <span class="font-price-lg text-xl font-extrabold text-on-surface tracking-tight mt-1">
                                    ${totalOrders != null ? totalOrders : 48} Orders
                                </span>
                            </div>
                            <div class="w-10 h-10 rounded-lg bg-surface-container-low flex items-center justify-center text-on-surface-variant group-hover:scale-105 transition-transform">
                                <span class="material-symbols-outlined text-xl">shopping_bag</span>
                            </div>
                        </div>
                        <div class="mt-space-md flex items-center gap-space-xs">
                            <span class="inline-flex items-center gap-0.5 px-2 py-0.5 rounded-full bg-surface-container text-on-tertiary-container text-[11px] font-bold">
                                <span class="material-symbols-outlined text-[13px]">trending_up</span>
                                +12.5%
                            </span>
                            <span class="text-[11px] text-outline">fulfillment pace</span>
                        </div>
                    </div>

                    <!-- Card 3: Customers -->
                    <div class="relative overflow-hidden bg-surface-container-lowest p-space-md rounded-xl shadow-sm border border-surface-container flex flex-col justify-between group">
                        <div class="flex items-start justify-between">
                            <div class="flex flex-col">
                                <span class="text-xs font-semibold text-on-surface-variant">Active User Accounts</span>
                                <span class="font-price-lg text-xl font-extrabold text-on-surface tracking-tight mt-1">
                                    ${totalUsers != null ? totalUsers : 120} Accounts
                                </span>
                            </div>
                            <div class="w-10 h-10 rounded-lg bg-secondary-fixed flex items-center justify-center text-secondary group-hover:scale-105 transition-transform">
                                <span class="material-symbols-outlined text-xl">group</span>
                            </div>
                        </div>
                        <div class="mt-space-md flex items-center gap-space-xs">
                            <span class="inline-flex items-center gap-0.5 px-2 py-0.5 rounded-full bg-secondary-fixed text-on-secondary-fixed-variant text-[11px] font-bold">
                                <span class="material-symbols-outlined text-[13px]">person_add</span>
                                +8.2%
                            </span>
                            <span class="text-[11px] text-outline">active growth</span>
                        </div>
                    </div>

                    <!-- Card 4: Inventory Alert -->
                    <div class="relative overflow-hidden bg-surface-container-lowest p-space-md rounded-xl shadow-sm border border-surface-container flex flex-col justify-between group">
                        <div class="flex items-start justify-between">
                            <div class="flex flex-col">
                                <span class="text-xs font-semibold text-on-surface-variant">Low Stock / Out of Stock</span>
                                <span class="font-price-lg text-xl font-extrabold text-error tracking-tight mt-1">
                                    ${outOfStock != null ? outOfStock : 0} Items
                                </span>
                            </div>
                            <div class="w-10 h-10 rounded-lg bg-error-container flex items-center justify-center text-error group-hover:scale-105 transition-transform">
                                <span class="material-symbols-outlined text-xl">warning</span>
                            </div>
                        </div>
                        <div class="mt-space-md flex items-center gap-space-xs">
                            <span class="inline-flex items-center gap-0.5 px-2 py-0.5 rounded-full bg-error-container text-error text-[11px] font-bold">
                                Optimal Flow
                            </span>
                            <span class="text-[11px] text-outline">in-memory resilience</span>
                        </div>
                    </div>

                </div>

                <!-- Dual-Bar Visual Performance Chart -->
                <div class="bg-surface-container-lowest p-space-lg rounded-xl shadow-sm border border-surface-container flex flex-col gap-space-md">
                    <div class="flex flex-col md:flex-row md:items-center justify-between gap-space-sm pb-space-sm border-b border-surface-container">
                        <div class="flex flex-col">
                            <span class="text-[11px] font-bold text-secondary uppercase tracking-widest">Enterprise Trajectory</span>
                            <h2 class="font-headline-sm text-base font-bold text-on-surface tracking-tight mt-0.5">Revenue &amp; Orders Performance</h2>
                        </div>
                        <div class="flex items-center gap-space-md text-xs font-semibold">
                            <div class="flex items-center gap-1.5">
                                <span class="w-3 h-3 rounded-full bg-secondary"></span>
                                <span>Gross Revenue (₹)</span>
                            </div>
                            <div class="flex items-center gap-1.5">
                                <span class="w-3 h-3 rounded-full bg-on-tertiary-container"></span>
                                <span>Fulfillment Orders</span>
                            </div>
                        </div>
                    </div>

                    <!-- CSS/SVG Dual Bar Visualization -->
                    <div class="h-56 w-full flex items-end justify-between gap-4 pt-6 pb-2 px-4 bg-surface-container-low/40 rounded-xl">
                        <div class="flex flex-col items-center flex-1 h-full justify-end gap-1.5">
                            <div class="w-full max-w-[40px] flex items-end justify-center gap-1 h-4/5">
                                <div class="w-1/2 bg-secondary rounded-t h-[65%]" title="Revenue: ₹35,000"></div>
                                <div class="w-1/2 bg-on-tertiary-container rounded-t h-[45%]" title="Orders: 12"></div>
                            </div>
                            <span class="text-[11px] font-medium text-outline">Jan</span>
                        </div>
                        <div class="flex flex-col items-center flex-1 h-full justify-end gap-1.5">
                            <div class="w-full max-w-[40px] flex items-end justify-center gap-1 h-4/5">
                                <div class="w-1/2 bg-secondary rounded-t h-[75%]" title="Revenue: ₹48,000"></div>
                                <div class="w-1/2 bg-on-tertiary-container rounded-t h-[60%]" title="Orders: 18"></div>
                            </div>
                            <span class="text-[11px] font-medium text-outline">Feb</span>
                        </div>
                        <div class="flex flex-col items-center flex-1 h-full justify-end gap-1.5">
                            <div class="w-full max-w-[40px] flex items-end justify-center gap-1 h-4/5">
                                <div class="w-1/2 bg-secondary rounded-t h-[55%]" title="Revenue: ₹32,000"></div>
                                <div class="w-1/2 bg-on-tertiary-container rounded-t h-[40%]" title="Orders: 10"></div>
                            </div>
                            <span class="text-[11px] font-medium text-outline">Mar</span>
                        </div>
                        <div class="flex flex-col items-center flex-1 h-full justify-end gap-1.5">
                            <div class="w-full max-w-[40px] flex items-end justify-center gap-1 h-4/5">
                                <div class="w-1/2 bg-secondary rounded-t h-[85%]" title="Revenue: ₹58,000"></div>
                                <div class="w-1/2 bg-on-tertiary-container rounded-t h-[75%]" title="Orders: 22"></div>
                            </div>
                            <span class="text-[11px] font-medium text-outline">Apr</span>
                        </div>
                        <div class="flex flex-col items-center flex-1 h-full justify-end gap-1.5">
                            <div class="w-full max-w-[40px] flex items-end justify-center gap-1 h-4/5">
                                <div class="w-1/2 bg-secondary rounded-t h-[95%]" title="Revenue: ₹69,000"></div>
                                <div class="w-1/2 bg-on-tertiary-container rounded-t h-[85%]" title="Orders: 28"></div>
                            </div>
                            <span class="text-[11px] font-bold text-on-surface">May (Current)</span>
                        </div>
                    </div>
                </div>

                <!-- Split Tables: Recent Orders & Catalog Highlights -->
                <div class="grid grid-cols-1 lg:grid-cols-12 gap-space-lg">
                    
                    <!-- Table 1: Top Selling / Recent Curations (7 cols) -->
                    <div class="lg:col-span-7 bg-surface-container-lowest p-space-md rounded-xl shadow-sm border border-surface-container flex flex-col justify-between">
                        <div>
                            <div class="flex justify-between items-center mb-space-sm pb-space-xs border-b border-surface-container">
                                <h3 class="text-sm font-bold text-on-surface">Top Selling Curations</h3>
                                <a href="${pageContext.request.contextPath}/admin/products" class="text-xs text-secondary hover:underline">View Catalog</a>
                            </div>

                            <div class="overflow-x-auto">
                                <table class="w-full text-left text-xs">
                                    <thead>
                                        <tr class="bg-surface-container-low text-on-surface-variant font-semibold">
                                            <th class="p-2 rounded-l">Curation</th>
                                            <th class="p-2">Category</th>
                                            <th class="p-2 text-right">Price</th>
                                            <th class="p-2 text-center rounded-r">Status</th>
                                        </tr>
                                    </thead>
                                    <tbody class="divide-y divide-surface-container">
                                        <tr>
                                            <td class="p-2 font-bold text-on-surface">Stockholm Modular Velvet Sofa</td>
                                            <td class="p-2 text-on-surface-variant">Living Room</td>
                                            <td class="p-2 text-right font-mono font-bold">₹42,999</td>
                                            <td class="p-2 text-center"><span class="bg-tertiary-container/20 text-on-tertiary-container px-2 py-0.5 rounded text-[10px] font-bold">Active</span></td>
                                        </tr>
                                        <tr>
                                            <td class="p-2 font-bold text-on-surface">JBL Live 660NC Wireless Headphones</td>
                                            <td class="p-2 text-on-surface-variant">Electronics</td>
                                            <td class="p-2 text-right font-mono font-bold">₹6,499</td>
                                            <td class="p-2 text-center"><span class="bg-tertiary-container/20 text-on-tertiary-container px-2 py-0.5 rounded text-[10px] font-bold">Active</span></td>
                                        </tr>
                                        <tr>
                                            <td class="p-2 font-bold text-on-surface">Oscar Minimalist Leather Wallet</td>
                                            <td class="p-2 text-on-surface-variant">Accessories</td>
                                            <td class="p-2 text-right font-mono font-bold">₹2,499</td>
                                            <td class="p-2 text-center"><span class="bg-tertiary-container/20 text-on-tertiary-container px-2 py-0.5 rounded text-[10px] font-bold">Active</span></td>
                                        </tr>
                                        <tr>
                                            <td class="p-2 font-bold text-on-surface">Lumina Nordic Brass Pendant Lamp</td>
                                            <td class="p-2 text-on-surface-variant">Living Room</td>
                                            <td class="p-2 text-right font-mono font-bold">₹5,899</td>
                                            <td class="p-2 text-center"><span class="bg-tertiary-container/20 text-on-tertiary-container px-2 py-0.5 rounded text-[10px] font-bold">Active</span></td>
                                        </tr>
                                    </tbody>
                                </table>
                            </div>
                        </div>
                    </div>

                    <!-- Table 2: Quick Operations Checklist (5 cols) -->
                    <div class="lg:col-span-5 bg-surface-container-lowest p-space-md rounded-xl shadow-sm border border-surface-container flex flex-col justify-between">
                        <div>
                            <div class="flex justify-between items-center mb-space-sm pb-space-xs border-b border-surface-container">
                                <h3 class="text-sm font-bold text-on-surface">Fulfillment Health</h3>
                                <span class="bg-surface-container text-secondary px-2 py-0.5 rounded-full text-[10px] font-bold">All Operational</span>
                            </div>

                            <div class="space-y-space-sm text-xs">
                                <div class="flex justify-between items-center p-2.5 rounded-lg bg-surface-container-low border border-surface-container">
                                    <span class="text-on-surface-variant font-medium">Waiting For Confirmation</span>
                                    <span class="font-bold text-on-surface">${waitingConfirmation != null ? waitingConfirmation : 3}</span>
                                </div>
                                <div class="flex justify-between items-center p-2.5 rounded-lg bg-surface-container-low border border-surface-container">
                                    <span class="text-on-surface-variant font-medium">Processing Orders</span>
                                    <span class="font-bold text-secondary">${processingOrders != null ? processingOrders : 8}</span>
                                </div>
                                <div class="flex justify-between items-center p-2.5 rounded-lg bg-surface-container-low border border-surface-container">
                                    <span class="text-on-surface-variant font-medium">Ready For Delivery</span>
                                    <span class="font-bold text-on-surface">${readyForDelivery != null ? readyForDelivery : 5}</span>
                                </div>
                                <div class="flex justify-between items-center p-2.5 rounded-lg bg-surface-container-low border border-surface-container">
                                    <span class="text-on-surface-variant font-medium">Delivered Orders</span>
                                    <span class="font-bold text-on-tertiary-container">${deliveredOrders != null ? deliveredOrders : 32}</span>
                                </div>
                            </div>
                        </div>

                        <div class="mt-space-md pt-space-xs">
                            <a href="${pageContext.request.contextPath}/admin/orders" class="w-full inline-block text-center bg-primary text-on-primary hover:bg-secondary py-2 rounded-lg text-xs font-semibold transition-colors">
                                Manage Orders Pipeline
                            </a>
                        </div>
                    </div>

                </div>

            </div>
        </main>
    </div>

</body>
</html>
