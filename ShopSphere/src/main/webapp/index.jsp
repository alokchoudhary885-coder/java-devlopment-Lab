<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<c:set var="pageTitle" value="ShopSphere — Scandinavian Luxe Interior & Curated Tech" />
<%@ include file="/fragments/header.jsp" %>
<%@ include file="/fragments/navbar.jsp" %>

<!-- Main Storefront Body -->
<main class="w-full bg-background">
    
    <!-- Top Editorial Ticker Bar -->
    <section class="w-full bg-surface-container-low py-2.5 px-gutter border-b border-surface-container">
        <div class="max-w-[1440px] mx-auto flex flex-wrap items-center justify-between text-xs text-on-surface-variant gap-y-2">
            <div class="flex items-center gap-space-md">
                <span class="inline-flex items-center gap-1.5 text-secondary font-bold">
                    <span class="w-2 h-2 rounded-full bg-secondary animate-pulse"></span>
                    NEW RELEASE
                </span>
                <span class="hidden sm:inline text-on-surface font-medium">Autumn / Winter '25 Scandinavian Collection has landed</span>
            </div>
            <div class="flex items-center gap-space-lg text-[11px] font-bold tracking-wider uppercase">
                <span class="flex items-center gap-1"><span class="material-symbols-outlined text-base text-secondary">local_shipping</span> Express Global Courier</span>
                <span class="hidden md:flex items-center gap-1"><span class="material-symbols-outlined text-base text-secondary">verified</span> 5-Year Craft Guarantee</span>
                <span class="flex items-center gap-1"><span class="material-symbols-outlined text-base text-secondary">cached</span> 30-Day In-Home Trial</span>
            </div>
        </div>
    </section>

    <!-- 1. Hero Section: Asymmetrical Bento Grid -->
    <section class="w-full px-gutter pt-space-md pb-space-xl">
        <div class="max-w-[1440px] mx-auto">
            <div class="grid grid-cols-1 lg:grid-cols-12 gap-space-md min-h-[580px]">
                
                <!-- Hero Left: 60% Bento Master (7 cols) -->
                <div class="lg:col-span-7 relative rounded-2xl overflow-hidden shadow-md flex flex-col justify-end p-space-lg lg:p-space-xl group border border-surface-container">
                    <div class="absolute inset-0 bg-cover bg-center transition-transform duration-700 ease-out group-hover:scale-[1.03]" 
                         style="background-image: url('https://lh3.googleusercontent.com/aida-public/AB6AXuBAXGp_Hi9AAjMYlQJBTgNs_n9muk7BysGnjxWdFm5YcIQgKjkK4KeA-2MvevLA91Qu9KV-leaP7VWw7cNzr943I1Ix898mWhy8Sqi95tsmorScqv7OvAKai0MpbYynaMUr451JCv0oR6yBfAXzb6xVRUFvNDeFrWwUglOaL8LCk6BiqTKAYgXPG976OlcknxLp98WCoUZVC8D4bMrP3EF1Kmo7NBn4SXdLupMx7ipdRHWpSr31aR7Y')">
                    </div>
                    <!-- Subtle Scrim Gradient -->
                    <div class="absolute inset-0 bg-gradient-to-t from-primary/90 via-primary/40 to-transparent"></div>
                    
                    <!-- Floating Price Badge -->
                    <div class="absolute top-6 left-6 z-10">
                        <span class="inline-flex items-center gap-1.5 bg-surface-container-lowest/90 backdrop-blur-md text-on-surface px-3.5 py-1.5 rounded-full text-xs font-bold shadow-sm">
                            <span class="w-1.5 h-1.5 rounded-full bg-secondary"></span>
                            Starting at ₹5,899
                        </span>
                    </div>

                    <!-- Editorial Curator Badge -->
                    <div class="absolute top-6 right-6 z-10 hidden sm:flex items-center gap-2 bg-surface-container-lowest/20 backdrop-blur-md px-3 py-1 rounded-full text-xs font-bold text-on-primary">
                        <span class="material-symbols-outlined text-sm">auto_awesome</span>
                        Architectural Digest Pick
                    </div>

                    <!-- Foreground Content -->
                    <div class="relative z-10 max-w-xl text-on-primary mt-32">
                        <div class="flex items-center gap-2 mb-space-xs">
                            <span class="h-px w-6 bg-secondary-fixed"></span>
                            <span class="text-secondary-fixed text-[11px] font-bold tracking-widest uppercase">The Nordic Heritage Series</span>
                        </div>
                        <h1 class="font-display-hero text-3xl sm:text-4xl lg:text-5xl text-on-primary tracking-tight font-extrabold mb-space-sm leading-tight">
                            Redefine Comfort with Timeless Elegance
                        </h1>
                        <p class="font-body-lg text-sm sm:text-base text-surface-container-high/90 max-w-lg mb-space-lg">
                            Discover handcrafted furniture, minimalist Scandinavian silhouettes, and sustainable materials curated for refined living spaces.
                        </p>
                        <div class="flex flex-wrap items-center gap-space-md">
                            <a class="inline-flex items-center justify-center gap-2 bg-secondary text-on-secondary px-6 py-3.5 rounded-lg text-xs font-bold shadow-md hover:bg-secondary-container transition-all duration-200 group/btn" 
                               href="${pageContext.request.contextPath}/products">
                                <span>Explore Catalog</span>
                                <span class="material-symbols-outlined text-base transition-transform duration-200 group-hover/btn:translate-x-1">arrow_forward</span>
                            </a>
                            <a class="inline-flex items-center justify-center gap-1.5 bg-surface-container-lowest/20 hover:bg-surface-container-lowest/35 backdrop-blur-sm text-on-primary px-5 py-3.5 rounded-lg text-xs font-semibold transition-colors" 
                               href="${pageContext.request.contextPath}/products?category=1">
                                <span class="material-symbols-outlined text-base">chair</span>
                                <span>Living Room Curations</span>
                            </a>
                        </div>
                    </div>
                </div>

                <!-- Hero Right: 40% Bento Vertical Split (5 cols) -->
                <div class="lg:col-span-5 flex flex-col gap-space-md">
                    
                    <!-- Top Promo Card: Audio / Smart Living -->
                    <div class="relative flex-1 rounded-2xl overflow-hidden shadow-md group min-h-[270px] p-space-lg flex flex-col justify-between border border-surface-container">
                        <div class="absolute inset-0 bg-cover bg-center transition-transform duration-700 ease-out group-hover:scale-105" 
                             style="background-image: url('https://lh3.googleusercontent.com/aida-public/AB6AXuCcSH3f_32L8x6dgqzhaQ1r3j1sHnNyG1JIvaV0OSfi8dewSTw9aNBvxbVYteuRlywAV894HRL6xyGrRGwkkDDQP46BWEioV2KPs3GxcoyjU-VOT-zTWw088O-0zvxrs1SvavR3TG7XWosB68f4hqmKRX0StsGOELhgmXu9Qk3S9EShseKNgwSvjnC3nZI6J-xzQ3kC-kmd9JtFSrkmVa8nKjrQ4m2eB2P6plVHNbTwy7rAdBSBC5PZ')">
                        </div>
                        <div class="absolute inset-0 bg-gradient-to-r from-primary/90 via-primary/60 to-primary/20"></div>
                        
                        <div class="relative z-10 flex items-start justify-between">
                            <span class="bg-secondary text-on-secondary px-2.5 py-1 rounded-full text-[10px] font-bold tracking-wider uppercase">
                                TRENDING NOW
                            </span>
                            <span class="p-2 rounded-full bg-surface-container-lowest/20 backdrop-blur-sm text-on-primary">
                                <span class="material-symbols-outlined text-base">graphic_eq</span>
                            </span>
                        </div>
                        <div class="relative z-10 max-w-xs text-on-primary mt-6">
                            <p class="text-[11px] font-bold text-secondary-fixed uppercase tracking-wider mb-1">Acoustic Precision</p>
                            <h2 class="font-headline-md text-xl font-bold mb-1 text-on-primary">
                                Wireless Audio &amp; Smart Living
                            </h2>
                            <p class="text-xs text-surface-container-high/85 mb-3 line-clamp-2">
                                Spatial soundscapes integrated into sculpted, studio-grade hardware.
                            </p>
                            <a class="inline-flex items-center gap-1.5 text-on-primary text-xs font-semibold underline underline-offset-4 decoration-secondary hover:text-secondary-fixed transition-colors" 
                               href="${pageContext.request.contextPath}/products?category=4">
                                <span>Shop Audio</span>
                                <span class="material-symbols-outlined text-sm">arrow_outward</span>
                            </a>
                        </div>
                    </div>

                    <!-- Bottom Promo Card: Weekend Special -->
                    <div class="relative flex-1 rounded-2xl overflow-hidden shadow-md group min-h-[270px] p-space-lg flex flex-col justify-between bg-surface-container border border-surface-container">
                        <div class="absolute inset-0 bg-cover bg-center transition-transform duration-700 ease-out group-hover:scale-105" 
                             style="background-image: url('https://lh3.googleusercontent.com/aida-public/AB6AXuAmFirLTWd1CQPwUW2YWZuCSbYA3tuazk-rONGPqEGFK5S60q8L_UtZzhEMC4WDicSdJaZCL0jT1yE5EGPlh-bcAwRaTvsZT_p2rJDJIBmeSCq3ltHDa_sJkoJbBT2dPpWsR4vp-NNMwFXzDEvPgp4ML2G_UcApJLaRbEHhaYh3hqIjibwyY8YAg2A5OGkgWThUG5IbZLUFhYNERczvYSMty0Q4vsWR2qaobS5_jhg_wfbQ91HqSob4')">
                        </div>
                        <div class="absolute inset-0 bg-gradient-to-t from-surface-container-lowest/95 via-surface-container-lowest/80 to-surface-container-lowest/40"></div>
                        
                        <div class="relative z-10 flex items-start justify-between">
                            <div class="inline-flex items-center gap-1.5 bg-error-container text-on-error-container px-3 py-1 rounded-full text-[10px] font-bold tracking-wide">
                                <span class="material-symbols-outlined text-xs">local_fire_department</span>
                                WEEKEND SPECIAL
                            </div>
                            <div class="bg-primary text-on-primary px-2.5 py-1 rounded-md text-[10px] font-bold font-mono tracking-wider shadow-sm">
                                USE CODE: LUXE20
                            </div>
                        </div>
                        <div class="relative z-10 mt-6">
                            <div class="flex items-baseline gap-2 mb-1">
                                <span class="font-headline-md text-xl font-bold text-on-surface">20% Off Design Accents</span>
                            </div>
                            <p class="text-xs text-on-surface-variant mb-3">
                                Refined leatherwares, mouth-blown glassware, and bespoke stationery.
                            </p>
                            <div class="flex items-center justify-between">
                                <a class="inline-flex items-center justify-center gap-2 bg-primary text-on-primary px-4 py-2 rounded-lg text-xs font-semibold hover:bg-on-surface-variant transition-colors shadow-sm" 
                                   href="${pageContext.request.contextPath}/products?category=2">
                                    <span>Shop Now</span>
                                    <span class="material-symbols-outlined text-sm">arrow_forward</span>
                                </a>
                                <span class="text-[11px] text-outline font-medium">Ends Sunday Midnight</span>
                            </div>
                        </div>
                    </div>

                </div>
            </div>
        </div>
    </section>

    <!-- 2. Atmosphere / Category Showcase Carousel -->
    <section class="w-full px-gutter py-space-xl bg-surface-container-lowest border-t border-b border-surface-container">
        <div class="max-w-[1440px] mx-auto">
            <div class="flex flex-col md:flex-row md:items-end justify-between mb-space-lg gap-space-sm">
                <div>
                    <span class="text-[11px] font-bold text-secondary tracking-widest uppercase mb-1 block">Spaces &amp; Objects</span>
                    <h2 class="font-headline-lg text-2xl md:text-3xl text-on-surface font-bold tracking-tight">Explore by Atmosphere</h2>
                </div>
                <div class="flex items-center gap-space-sm">
                    <a href="${pageContext.request.contextPath}/products" class="text-xs font-semibold text-secondary hover:underline flex items-center gap-1">
                        <span>All Categories</span>
                        <span class="material-symbols-outlined text-sm">arrow_forward</span>
                    </a>
                </div>
            </div>

            <!-- Atmosphere Cards Grid -->
            <div class="grid grid-cols-2 md:grid-cols-3 lg:grid-cols-5 gap-space-md">
                
                <!-- 1. Living Room -->
                <a class="group flex flex-col bg-surface-container-low hover:bg-surface-container rounded-xl p-space-md transition-all duration-300 shadow-sm hover:shadow-md border border-surface-container" 
                   href="${pageContext.request.contextPath}/products?category=1">
                    <div class="w-full aspect-square rounded-lg overflow-hidden bg-surface-container mb-space-sm relative">
                        <img class="w-full h-full object-cover transition-transform duration-500 group-hover:scale-105" 
                             alt="Scandinavian living room vignette with sculpted walnut lounge chair" 
                             src="https://lh3.googleusercontent.com/aida-public/AB6AXuDL1M5Sl9WY02OrKqk5SqeLFDjmY50GCLf7D6gmk3cdR-tjihVhWVCwaCmkndvYdnudLe7ndJp_JIbuMNEsUcW45DU0UfYW8KGwdrvzuuhCC-9mR_o2jeehlhElXQoajf2V4kS8RYai-JqNYVBRNoTlPzNzTQoFmSy0OmKbWUFCRr0K7Hdbh1fM9Fx8m08k3ksrLD-AAotWE2wTXOG1hTia0C6Gp4rOsPQCAEQlWu5v57g724FAXdB3"/>
                        <span class="absolute top-2 right-2 bg-surface-container-lowest/80 backdrop-blur-sm text-on-surface p-1.5 rounded-full opacity-0 group-hover:opacity-100 transition-opacity">
                            <span class="material-symbols-outlined text-sm">arrow_outward</span>
                        </span>
                    </div>
                    <span class="font-headline-sm text-sm font-semibold text-on-surface group-hover:text-secondary transition-colors">Living Room</span>
                    <span class="text-[11px] text-outline mt-0.5">Handcrafted Sofas &amp; Accents</span>
                </a>

                <!-- 2. Bedroom -->
                <a class="group flex flex-col bg-surface-container-low hover:bg-surface-container rounded-xl p-space-md transition-all duration-300 shadow-sm hover:shadow-md border border-surface-container" 
                   href="${pageContext.request.contextPath}/products?category=1">
                    <div class="w-full aspect-square rounded-lg overflow-hidden bg-surface-container mb-space-sm relative">
                        <img class="w-full h-full object-cover transition-transform duration-500 group-hover:scale-105" 
                             alt="Calm tranquil master bedroom with organic natural flax linen bed sheets" 
                             src="https://lh3.googleusercontent.com/aida-public/AB6AXuAhDYeZ_Pq3dUCkmEZSHEX2LFuvaGxqtAXXly8DxYAFQ2ptqFErL48yaeKRXwCbEXWJo5OqP71AXeUt7fDmU-XUraxh6YREdUliXlUSJ88fJYQEYXaBUer0XagUKkA_PrDWiSkWE8bbJJR2ZHkHvYYnPL_JnnJucxH1lnWfvuX_Ds7uoAMFGKP-UVfoQFif4GB3D2-vWxycU55UUaH71_xgb-TpwM2kw2Cj72JzVTSMrNfBFPHcrCc_"/>
                        <span class="absolute top-2 right-2 bg-surface-container-lowest/80 backdrop-blur-sm text-on-surface p-1.5 rounded-full opacity-0 group-hover:opacity-100 transition-opacity">
                            <span class="material-symbols-outlined text-sm">arrow_outward</span>
                        </span>
                    </div>
                    <span class="font-headline-sm text-sm font-semibold text-on-surface group-hover:text-secondary transition-colors">Bedroom &amp; Linens</span>
                    <span class="text-[11px] text-outline mt-0.5">Duvets, Throws &amp; Bedframes</span>
                </a>

                <!-- 3. Dining & Kitchen -->
                <a class="group flex flex-col bg-surface-container-low hover:bg-surface-container rounded-xl p-space-md transition-all duration-300 shadow-sm hover:shadow-md border border-surface-container" 
                   href="${pageContext.request.contextPath}/products?category=1">
                    <div class="w-full aspect-square rounded-lg overflow-hidden bg-surface-container mb-space-sm relative">
                        <img class="w-full h-full object-cover transition-transform duration-500 group-hover:scale-105" 
                             alt="Modern dining setting with a heavy round solid teak dining table" 
                             src="https://lh3.googleusercontent.com/aida-public/AB6AXuCOiYzaAkhnxz7SGJoWY3Mq4zQ_6yDvB3eRISsNIs1eEXU64DxvdFg5YztDlpKdZOS2M0ObV6bepODkoCvuGmfBQX2Vp1QqFuD_b38Dlsevez0r0lbxFSGqyWMpUdl6ZGH9lnVtoIi0eiyRbw93pMfZWlESk5iF-n50ZdH5XG-QK7M3yDd5lKXGlS_LFKeq41pz9sgaiGklqvBM4lyHymf6RKu2cB0-cr9eKWHWNogk10JW29xwgMVe"/>
                        <span class="absolute top-2 right-2 bg-surface-container-lowest/80 backdrop-blur-sm text-on-surface p-1.5 rounded-full opacity-0 group-hover:opacity-100 transition-opacity">
                            <span class="material-symbols-outlined text-sm">arrow_outward</span>
                        </span>
                    </div>
                    <span class="font-headline-sm text-sm font-semibold text-on-surface group-hover:text-secondary transition-colors">Dining &amp; Kitchen</span>
                    <span class="text-[11px] text-outline mt-0.5">Solid Teak &amp; Stoneware</span>
                </a>

                <!-- 4. Electronics -->
                <a class="group flex flex-col bg-surface-container-low hover:bg-surface-container rounded-xl p-space-md transition-all duration-300 shadow-sm hover:shadow-md border border-surface-container" 
                   href="${pageContext.request.contextPath}/products?category=4">
                    <div class="w-full aspect-square rounded-lg overflow-hidden bg-surface-container mb-space-sm relative">
                        <img class="w-full h-full object-cover transition-transform duration-500 group-hover:scale-105" 
                             alt="High-end industrial design smart home speaker" 
                             src="https://lh3.googleusercontent.com/aida-public/AB6AXuCGLJAE42BJqyF4O2bKBd7QZlUHvWqxuNFjnxhu-Jq5ctES81SDx42T1Zs_eoG4vM-DAIjeVYQym06JtyaN1VUcmhgjzaT2PFkbZHHe5HIG0ulLEzBAj_Zy1DqhmTxfzFUcJ3nJ5XoZfgwyXqvbFv_0jsYS8_dw25TOvVLncKabqfxEUMwbjik2BOeJMyIv4Qbmc_lEm3cKP0pdzQCg5Ss5ULmj9q3EjI0S5PC-E0-X6rSsSmHLn9Fb"/>
                        <span class="absolute top-2 right-2 bg-surface-container-lowest/80 backdrop-blur-sm text-on-surface p-1.5 rounded-full opacity-0 group-hover:opacity-100 transition-opacity">
                            <span class="material-symbols-outlined text-sm">arrow_outward</span>
                        </span>
                    </div>
                    <span class="font-headline-sm text-sm font-semibold text-on-surface group-hover:text-secondary transition-colors">Electronics &amp; Audio</span>
                    <span class="text-[11px] text-outline mt-0.5">Acoustics &amp; Air Care</span>
                </a>

                <!-- 5. Accessories -->
                <a class="group flex flex-col bg-surface-container-low hover:bg-surface-container rounded-xl p-space-md transition-all duration-300 shadow-sm hover:shadow-md border border-surface-container" 
                   href="${pageContext.request.contextPath}/products?category=2">
                    <div class="w-full aspect-square rounded-lg overflow-hidden bg-surface-container mb-space-sm relative">
                        <img class="w-full h-full object-cover transition-transform duration-500 group-hover:scale-105" 
                             alt="Artisanal leather personal goods, minimalist brass watch" 
                             src="https://lh3.googleusercontent.com/aida-public/AB6AXuCd1bhWdHmUOdBlotIDLsUiNJHDdBt6dwPQP0nyP6GZrGSZc02rvBgNL6Phlfxv3XzgeQZnvml2hpnlPYHuOnAuvLfSRdH8IhXsxO-fVAu0cUoCFZ54QZllemvsGNKRKhc56R-zj3XI3tGtZR6GJYTbukScEQFZtRC01YfV3xaxffxbx_tRrTLSvvXVtFOGO3R-4MOLbGJN5AIklcSxLzPaO14SyjGDPOFKuRCK1G3kKdRGuAHjHQxO"/>
                        <span class="absolute top-2 right-2 bg-surface-container-lowest/80 backdrop-blur-sm text-on-surface p-1.5 rounded-full opacity-0 group-hover:opacity-100 transition-opacity">
                            <span class="material-symbols-outlined text-sm">arrow_outward</span>
                        </span>
                    </div>
                    <span class="font-headline-sm text-sm font-semibold text-on-surface group-hover:text-secondary transition-colors">Curated Accessories</span>
                    <span class="text-[11px] text-outline mt-0.5">Fine Wallets &amp; Objects</span>
                </a>

            </div>
        </div>
    </section>

    <!-- Curatorial Metric Ribbon -->
    <section class="w-full bg-primary-container text-on-primary py-space-lg px-gutter">
        <div class="max-w-[1440px] mx-auto grid grid-cols-2 lg:grid-cols-4 gap-space-lg items-center text-center">
            <div class="flex flex-col items-center">
                <span class="font-display-hero text-2xl sm:text-3xl font-extrabold text-secondary-fixed">100%</span>
                <span class="text-xs font-semibold text-surface-container-highest mt-1">FSC Certified Solid Woods</span>
            </div>
            <div class="flex flex-col items-center">
                <span class="font-display-hero text-2xl sm:text-3xl font-extrabold text-secondary-fixed">48 Hrs</span>
                <span class="text-xs font-semibold text-surface-container-highest mt-1">White-Glove In-Room Delivery</span>
            </div>
            <div class="flex flex-col items-center">
                <span class="font-display-hero text-2xl sm:text-3xl font-extrabold text-secondary-fixed">5 Years</span>
                <span class="text-xs font-semibold text-surface-container-highest mt-1">Structural Warranty Standard</span>
            </div>
            <div class="flex flex-col items-center">
                <span class="font-display-hero text-2xl sm:text-3xl font-extrabold text-secondary-fixed">98.6%</span>
                <span class="text-xs font-semibold text-surface-container-highest mt-1">Client Satisfaction Score</span>
            </div>
        </div>
    </section>

    <!-- 3. Featured Products Section -->
    <section class="w-full px-gutter py-space-xl bg-background">
        <div class="max-w-[1440px] mx-auto">
            
            <div class="flex flex-col md:flex-row md:items-end justify-between mb-space-lg gap-space-md">
                <div>
                    <span class="text-[11px] font-bold text-secondary tracking-widest uppercase mb-1 block">Curator's Selection</span>
                    <h2 class="font-headline-lg text-2xl md:text-3xl text-on-surface font-bold tracking-tight">
                        Featured Products — Handpicked for Quality
                    </h2>
                    <p class="text-xs sm:text-sm text-on-surface-variant mt-1 max-w-xl">
                        Curated by our architectural design team for durability, aesthetics, and everyday luxury.
                    </p>
                </div>
                <div class="flex items-center gap-space-sm">
                    <a class="inline-flex items-center gap-1.5 text-secondary hover:text-on-surface text-xs font-bold transition-colors" 
                       href="${pageContext.request.contextPath}/products">
                        <span>View All Curations (180+)</span>
                        <span class="material-symbols-outlined text-base">arrow_forward</span>
                    </a>
                </div>
            </div>

            <!-- Dynamic 4-Column Responsive Grid -->
            <div class="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-space-md">
                <c:forEach var="product" items="${featuredProducts}">
                    <tags:productCard product="${product}" />
                </c:forEach>
            </div>
        </div>
    </section>

</main>

<%@ include file="/fragments/footer.jsp" %>
