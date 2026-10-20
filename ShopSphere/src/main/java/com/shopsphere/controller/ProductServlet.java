package com.shopsphere.controller;

import com.shopsphere.model.Category;
import com.shopsphere.model.Product;
import com.shopsphere.service.ProductService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

/**
 * Controller for Product Catalog, Multi-faceted Filtering, Search, and Sorting.
 * Handles GET /products matching Screenshot 4 ("Shop").
 * Demonstrates: Controller request parameter parsing, Service layer delegation, dynamic filtering.
 */
@WebServlet(name = "ProductServlet", urlPatterns = {"/products"})
public class ProductServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private transient ProductService productService;

    private static final int PAGE_SIZE = 8; // Exactly matches Screenshot 4 (Showing 1-8 of X)

    @Override
    public void init() throws ServletException {
        super.init();
        this.productService = new ProductService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // 1. Parse Search & Filter Parameters
        String searchKeyword = request.getParameter("q");
        if (searchKeyword == null || searchKeyword.trim().isEmpty()) {
            searchKeyword = request.getParameter("search");
        }

        Integer categoryId = null;
        String catParam = request.getParameter("category");
        if (catParam != null && !catParam.trim().isEmpty()) {
            try {
                categoryId = Integer.parseInt(catParam.trim());
            } catch (NumberFormatException ignored) {}
        }

        String brand = request.getParameter("brand");
        if (brand != null && brand.trim().isEmpty()) {
            brand = null;
        }

        BigDecimal minPrice = null;
        String minPriceParam = request.getParameter("minPrice");
        if (minPriceParam != null && !minPriceParam.trim().isEmpty()) {
            try {
                minPrice = new BigDecimal(minPriceParam.trim());
            } catch (NumberFormatException ignored) {}
        }

        BigDecimal maxPrice = null;
        String maxPriceParam = request.getParameter("maxPrice");
        if (maxPriceParam != null && !maxPriceParam.trim().isEmpty()) {
            try {
                maxPrice = new BigDecimal(maxPriceParam.trim());
            } catch (NumberFormatException ignored) {}
        }

        Integer minRating = null;
        String ratingParam = request.getParameter("minRating");
        if (ratingParam != null && !ratingParam.trim().isEmpty()) {
            try {
                minRating = Integer.parseInt(ratingParam.trim());
            } catch (NumberFormatException ignored) {}
        }

        String sortBy = request.getParameter("sort");
        if (sortBy == null || sortBy.trim().isEmpty()) {
            sortBy = "top_rated"; // Default to Top Rated matching Screenshot 4
        }

        int page = 1;
        String pageParam = request.getParameter("page");
        if (pageParam != null && !pageParam.trim().isEmpty()) {
            try {
                page = Math.max(1, Integer.parseInt(pageParam.trim()));
            } catch (NumberFormatException ignored) {}
        }

        // 2. Fetch Products and Total Counts
        List<Product> products;
        int totalResults;

        if (searchKeyword != null && !searchKeyword.trim().isEmpty()) {
            // Keyword search
            products = productService.searchProducts(searchKeyword);
            totalResults = products.size();
            // In-memory slicing if raw search is invoked
            int fromIndex = Math.min((page - 1) * PAGE_SIZE, totalResults);
            int toIndex = Math.min(fromIndex + PAGE_SIZE, totalResults);
            products = products.subList(fromIndex, toIndex);
        } else {
            // Faceted filter
            products = productService.getFilteredProducts(
                    categoryId, brand, minPrice, maxPrice, minRating, sortBy, page, PAGE_SIZE);
            totalResults = productService.countFilteredProducts(
                    categoryId, brand, minPrice, maxPrice, minRating);
        }

        int totalPages = (int) Math.ceil((double) totalResults / PAGE_SIZE);
        if (totalPages == 0) totalPages = 1;

        int startIndex = (totalResults == 0) ? 0 : ((page - 1) * PAGE_SIZE + 1);
        int endIndex = Math.min(page * PAGE_SIZE, totalResults);

        // 3. Fetch Metadata for Filter Sidebar
        List<Category> allCategories = productService.getAllCategories();
        List<String> allBrands = productService.getAllBrands();

        // 4. Set Request Attributes
        request.setAttribute("products", products);
        request.setAttribute("totalResults", totalResults);
        request.setAttribute("startIndex", startIndex);
        request.setAttribute("endIndex", endIndex);
        request.setAttribute("currentPage", page);
        request.setAttribute("totalPages", totalPages);

        request.setAttribute("allCategories", allCategories);
        request.setAttribute("allBrands", allBrands);

        // Preserve current filter state
        request.setAttribute("selectedCategory", categoryId);
        request.setAttribute("selectedBrand", brand);
        request.setAttribute("selectedMinPrice", minPrice);
        request.setAttribute("selectedMaxPrice", maxPrice);
        request.setAttribute("selectedMinRating", minRating);
        request.setAttribute("selectedSort", sortBy);
        request.setAttribute("searchKeyword", searchKeyword);

        request.setAttribute("pageTitle", "Shop — Product Catalog | ShopSphere");

        // 5. Forward to View (products.jsp)
        request.getRequestDispatcher("/products.jsp").forward(request, response);
    }
}
