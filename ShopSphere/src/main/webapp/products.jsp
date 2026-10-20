<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<c:set var="pageTitle" value="Bespoke Catalog — ShopSphere | Nordic Retail Luxe" />
<%@ include file="/fragments/header.jsp" %>
<%@ include file="/fragments/navbar.jsp" %>

<main class="w-full bg-background">

    <!-- Editorial Breadcrumb & Catalog Meta Section -->
    <section class="max-w-[1440px] mx-auto w-full px-gutter pt-space-lg pb-space-md">
        <div class="flex flex-col md:flex-row md:items-end justify-between gap-space-md">
            <div class="flex flex-col gap-space-xs">
                <div class="flex items-center gap-2 text-xs font-medium text-outline">
                    <a class="hover:text-primary transition-colors" href="${pageContext.request.contextPath}/">Home</a>
                    <span>/</span>
                    <a class="text-on-surface font-semibold" href="${pageContext.request.contextPath}/products">Bespoke Collection</a>
                    <span>/</span>
                    <span class="text-secondary font-medium">All Curations</span>
                </div>
                <div class="flex items-baseline gap-space-sm pt-space-xs">
                    <h1 class="font-headline-lg text-2xl md:text-3xl text-on-surface tracking-tight font-bold">The Modern Sanctuary</h1>
                    <span class="text-xs font-semibold text-outline tracking-wider uppercase font-mono">${totalResults != null ? totalResults : '8'} Curated Pieces</span>
                </div>
            </div>
            
            <!-- Quick Metric Snapshot Micro-Bar -->
            <div class="hidden lg:flex items-center gap-space-lg bg-surface-container-lowest px-space-lg py-2.5 rounded-xl shadow-sm border border-surface-container">
                <div class="flex items-center gap-space-sm">
                    <span class="w-2 h-2 rounded-full bg-on-tertiary-container animate-pulse"></span>
                    <span class="text-xs font-medium text-on-surface-variant">Live Stock Feed</span>
                </div>
                <div class="h-4 w-px bg-surface-container"></div>
                <div class="flex items-center gap-1.5 text-xs font-medium">
                    <span class="text-outline">Free White-Glove Logistics</span>
                    <span class="material-symbols-outlined text-secondary text-base">verified</span>
                </div>
            </div>
        </div>
    </section>

    <!-- Main Body Grid: Sticky Filters Sidebar (25%) + Results Section (75%) -->
    <section class="max-w-[1440px] mx-auto w-full px-gutter pb-space-xl">
        <div class="grid grid-cols-1 lg:grid-cols-12 gap-space-xl items-start">
            
            <!-- Filter Sidebar (approx 3 cols) -->
            <aside class="lg:col-span-3 w-full bg-surface-container-lowest rounded-xl p-space-lg shadow-sm border border-surface-container lg:sticky lg:top-[160px] flex flex-col gap-space-lg">
                
                <form id="filterForm" action="${pageContext.request.contextPath}/products" method="GET" class="space-y-space-md">
                    <!-- Header & Reset -->
                    <div class="flex items-center justify-between pb-space-sm border-b border-surface-container">
                        <div class="flex items-center gap-space-xs">
                            <span class="material-symbols-outlined text-secondary text-lg">tune</span>
                            <span class="font-headline-sm text-sm font-bold text-on-surface">Filters</span>
                        </div>
                        <a href="${pageContext.request.contextPath}/products" class="text-xs text-secondary hover:underline flex items-center gap-1 cursor-pointer">
                            <span class="material-symbols-outlined text-sm">refresh</span>
                            <span>Reset</span>
                        </a>
                    </div>

                    <!-- Price Range Filter -->
                    <div class="flex flex-col gap-space-xs">
                        <div class="flex items-center justify-between">
                            <span class="text-xs font-bold text-on-surface uppercase tracking-wider">Price Range</span>
                            <span class="text-[11px] text-outline font-semibold">INR (₹)</span>
                        </div>
                        
                        <div class="grid grid-cols-2 gap-space-sm items-center pt-1">
                            <div class="flex flex-col gap-1">
                                <label class="text-[10px] text-outline uppercase font-semibold">Min (₹)</label>
                                <div class="flex items-center bg-surface-container-low px-2 py-1.5 rounded-lg border border-outline-variant/30">
                                    <span class="text-on-surface-variant text-xs mr-1">₹</span>
                                    <input class="w-full bg-transparent text-xs font-semibold text-on-surface focus:outline-none" 
                                           id="inputMinPrice" name="minPrice" min="0" max="150000" type="number" 
                                           placeholder="0" value="${selectedMinPrice}"/>
                                </div>
                            </div>
                            <div class="flex flex-col gap-1">
                                <label class="text-[10px] text-outline uppercase font-semibold">Max (₹)</label>
                                <div class="flex items-center bg-surface-container-low px-2 py-1.5 rounded-lg border border-outline-variant/30">
                                    <span class="text-on-surface-variant text-xs mr-1">₹</span>
                                    <input class="w-full bg-transparent text-xs font-semibold text-on-surface focus:outline-none" 
                                           id="inputMaxPrice" name="maxPrice" min="0" max="150000" type="number" 
                                           placeholder="150000" value="${selectedMaxPrice}"/>
                                </div>
                            </div>
                        </div>
                    </div>

                    <div class="h-px bg-surface-container w-full"></div>

                    <!-- Categories List -->
                    <div class="flex flex-col gap-space-xs">
                        <span class="text-xs font-bold text-on-surface uppercase tracking-wider">Categories</span>
                        <div class="flex flex-col gap-1 pt-1">
                            <label class="flex items-center justify-between text-left px-2.5 py-1.5 rounded-lg ${empty selectedCategory ? 'bg-surface-container font-semibold text-on-surface' : 'text-on-surface-variant hover:bg-surface-container-low'} text-xs cursor-pointer transition-colors">
                                <div class="flex items-center gap-2">
                                    <input type="radio" name="category" value="" ${empty selectedCategory ? 'checked' : ''} onchange="this.form.submit()" class="accent-primary"/>
                                    <span>All Categories</span>
                                </div>
                            </label>
                            <c:forEach var="cat" items="${allCategories}">
                                <label class="flex items-center justify-between text-left px-2.5 py-1.5 rounded-lg ${selectedCategory == cat.id ? 'bg-surface-container font-semibold text-on-surface' : 'text-on-surface-variant hover:bg-surface-container-low'} text-xs cursor-pointer transition-colors">
                                    <div class="flex items-center gap-2">
                                        <input type="radio" name="category" value="${cat.id}" ${selectedCategory == cat.id ? 'checked' : ''} onchange="this.form.submit()" class="accent-primary"/>
                                        <span>${cat.name}</span>
                                    </div>
                                </label>
                            </c:forEach>
                        </div>
                    </div>

                    <div class="h-px bg-surface-container w-full"></div>

                    <!-- Brands Filter -->
                    <div class="flex flex-col gap-space-xs">
                        <span class="text-xs font-bold text-on-surface uppercase tracking-wider">Design Studio &amp; Brands</span>
                        <div class="flex flex-col gap-1 pt-1">
                            <c:forEach var="b" items="${allBrands}">
                                <label class="flex items-center gap-2 text-xs text-on-surface-variant hover:text-on-surface px-1 py-1 cursor-pointer select-none">
                                    <input type="radio" name="brand" value="${b}" ${selectedBrand eq b ? 'checked' : ''} onchange="this.form.submit()" class="accent-primary"/>
                                    <span>${b}</span>
                                </label>
                            </c:forEach>
                        </div>
                    </div>

                    <div class="h-px bg-surface-container w-full"></div>

                    <!-- Customer Rating Filter -->
                    <div class="flex flex-col gap-space-xs">
                        <span class="text-xs font-bold text-on-surface uppercase tracking-wider">Customer Rating</span>
                        <div class="flex flex-col gap-1.5 pt-1">
                            <label class="flex items-center gap-2 text-xs cursor-pointer">
                                <input type="radio" name="minRating" value="5" ${selectedMinRating == 5 ? 'checked' : ''} onchange="this.form.submit()" class="accent-primary"/>
                                <span class="flex items-center text-amber-500 gap-0.5">
                                    <span class="material-symbols-outlined text-sm" style="font-variation-settings: 'FILL' 1;">star</span>
                                    <span class="material-symbols-outlined text-sm" style="font-variation-settings: 'FILL' 1;">star</span>
                                    <span class="material-symbols-outlined text-sm" style="font-variation-settings: 'FILL' 1;">star</span>
                                    <span class="material-symbols-outlined text-sm" style="font-variation-settings: 'FILL' 1;">star</span>
                                    <span class="material-symbols-outlined text-sm" style="font-variation-settings: 'FILL' 1;">star</span>
                                </span>
                                <span class="text-xs text-on-surface font-semibold ml-1">5 Stars</span>
                            </label>
                            <label class="flex items-center gap-2 text-xs cursor-pointer">
                                <input type="radio" name="minRating" value="4" ${selectedMinRating == 4 ? 'checked' : ''} onchange="this.form.submit()" class="accent-primary"/>
                                <span class="flex items-center text-amber-500 gap-0.5">
                                    <span class="material-symbols-outlined text-sm" style="font-variation-settings: 'FILL' 1;">star</span>
                                    <span class="material-symbols-outlined text-sm" style="font-variation-settings: 'FILL' 1;">star</span>
                                    <span class="material-symbols-outlined text-sm" style="font-variation-settings: 'FILL' 1;">star</span>
                                    <span class="material-symbols-outlined text-sm" style="font-variation-settings: 'FILL' 1;">star</span>
                                </span>
                                <span class="text-xs text-on-surface font-semibold ml-1">4 Stars &amp; above</span>
                            </label>
                        </div>
                    </div>

                    <!-- Hidden Search Keyword -->
                    <c:if test="${not empty searchKeyword}">
                        <input type="hidden" name="q" value="${searchKeyword}"/>
                    </c:if>

                    <button type="submit" class="w-full bg-primary hover:bg-secondary text-on-primary py-2.5 rounded-lg text-xs font-semibold shadow-sm transition-colors cursor-pointer mt-space-sm">
                        Apply Filters
                    </button>
                </form>
            </aside>

            <!-- Results Section (9 cols) -->
            <section class="lg:col-span-9 flex flex-col gap-space-md">
                
                <!-- Results Toolbar -->
                <div class="bg-surface-container-lowest rounded-xl p-space-md shadow-sm border border-surface-container flex flex-col sm:flex-row justify-between sm:items-center gap-space-sm">
                    <div class="flex items-center gap-space-sm">
                        <span class="text-xs text-on-surface-variant font-medium">
                            Showing <strong class="text-on-surface">${startIndex != null ? startIndex : 1}-${endIndex != null ? endIndex : (products != null ? products.size() : 0)}</strong> of <strong class="text-on-surface">${totalResults != null ? totalResults : (products != null ? products.size() : 0)}</strong> curations
                        </span>
                        <c:if test="${not empty searchKeyword}">
                            <span class="bg-surface-container-high text-secondary px-2.5 py-0.5 rounded-full text-xs font-semibold flex items-center gap-1">
                                "${searchKeyword}"
                                <a href="${pageContext.request.contextPath}/products" class="text-on-surface hover:text-error">&times;</a>
                            </span>
                        </c:if>
                    </div>

                    <!-- Sort Dropdown -->
                    <div class="flex items-center gap-2">
                        <label for="sortSelect" class="text-xs text-outline font-semibold">Sort by:</label>
                        <select id="sortSelect" name="sort" onchange="updateSort(this.value)" 
                                class="bg-surface-container-low text-xs font-semibold text-on-surface rounded-lg px-3 py-1.5 border border-outline-variant/30 outline-none cursor-pointer">
                            <option value="top_rated" ${selectedSort eq 'top_rated' ? 'selected' : ''}>Top Rated</option>
                            <option value="popular" ${selectedSort eq 'popular' ? 'selected' : ''}>Popular</option>
                            <option value="newest" ${selectedSort eq 'newest' ? 'selected' : ''}>Newest</option>
                            <option value="price_asc" ${selectedSort eq 'price_asc' ? 'selected' : ''}>Price: Low to High</option>
                            <option value="price_desc" ${selectedSort eq 'price_desc' ? 'selected' : ''}>Price: High to Low</option>
                        </select>
                    </div>
                </div>

                <!-- Products Grid -->
                <c:choose>
                    <c:when test="${not empty products}">
                        <div class="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 xl:grid-cols-3 gap-space-md">
                            <c:forEach var="product" items="${products}">
                                <tags:productCard product="${product}" />
                            </c:forEach>
                        </div>

                        <!-- Pagination -->
                        <c:if test="${totalPages > 1}">
                            <div class="flex justify-center items-center gap-2 mt-space-xl">
                                <a class="px-3 py-1.5 rounded-lg text-xs font-semibold bg-surface-container-lowest border border-surface-container ${currentPage == 1 ? 'pointer-events-none opacity-40' : 'hover:bg-surface-container'}" 
                                   href="?page=${currentPage - 1}&category=${selectedCategory}&brand=${selectedBrand}&minPrice=${selectedMinPrice}&maxPrice=${selectedMaxPrice}&minRating=${selectedMinRating}&sort=${selectedSort}&q=${searchKeyword}">
                                    Previous
                                </a>
                                <c:forEach var="p" begin="1" end="${totalPages}">
                                    <a class="w-8 h-8 rounded-lg text-xs font-semibold flex items-center justify-center ${currentPage == p ? 'bg-primary text-on-primary' : 'bg-surface-container-lowest border border-surface-container hover:bg-surface-container'}" 
                                       href="?page=${p}&category=${selectedCategory}&brand=${selectedBrand}&minPrice=${selectedMinPrice}&maxPrice=${selectedMaxPrice}&minRating=${selectedMinRating}&sort=${selectedSort}&q=${searchKeyword}">
                                        ${p}
                                    </a>
                                </c:forEach>
                                <a class="px-3 py-1.5 rounded-lg text-xs font-semibold bg-surface-container-lowest border border-surface-container ${currentPage == totalPages ? 'pointer-events-none opacity-40' : 'hover:bg-surface-container'}" 
                                   href="?page=${currentPage + 1}&category=${selectedCategory}&brand=${selectedBrand}&minPrice=${selectedMinPrice}&maxPrice=${selectedMaxPrice}&minRating=${selectedMinRating}&sort=${selectedSort}&q=${searchKeyword}">
                                    Next
                                </a>
                            </div>
                        </c:if>
                    </c:when>
                    <c:otherwise>
                        <div class="bg-surface-container-lowest rounded-xl p-space-xl text-center border border-surface-container shadow-sm my-space-md">
                            <div class="w-16 h-16 rounded-full bg-surface-container mx-auto flex items-center justify-center text-outline mb-space-md">
                                <span class="material-symbols-outlined text-3xl">search_off</span>
                            </div>
                            <h3 class="text-base font-bold text-on-surface mb-1">No Curated Items Found</h3>
                            <p class="text-xs text-on-surface-variant max-w-md mx-auto mb-space-md">
                                No items matched your selected filter criteria. Try expanding your price range or exploring other categories.
                            </p>
                            <a href="${pageContext.request.contextPath}/products" class="inline-flex items-center gap-1.5 bg-primary text-on-primary px-4 py-2 rounded-lg text-xs font-semibold hover:bg-secondary transition-colors">
                                <span class="material-symbols-outlined text-base">refresh</span>
                                <span>Reset All Filters</span>
                            </a>
                        </div>
                    </c:otherwise>
                </c:choose>

            </section>

        </div>
    </section>

</main>

<%@ include file="/fragments/footer.jsp" %>

<script>
    function updateSort(sortValue) {
        const url = new URL(window.location.href);
        url.searchParams.set('sort', sortValue);
        url.searchParams.set('page', '1');
        window.location.href = url.toString();
    }
</script>
