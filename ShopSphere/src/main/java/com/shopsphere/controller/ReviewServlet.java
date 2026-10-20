package com.shopsphere.controller;

import com.shopsphere.model.Review;
import com.shopsphere.model.User;
import com.shopsphere.service.ProductService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

/**
 * Controller for Customer Product Reviews and Ratings submission.
 * Handles POST /reviews
 * Recalculates average rating and review count on the product table.
 */
@WebServlet(name = "ReviewServlet", urlPatterns = {"/reviews"})
public class ReviewServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private transient ProductService productService;

    @Override
    public void init() throws ServletException {
        super.init();
        this.productService = new ProductService();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        User user = (session != null) ? (User) session.getAttribute("user") : null;

        String productIdParam = request.getParameter("productId");
        int productId;
        try {
            productId = Integer.parseInt(productIdParam);
        } catch (Exception e) {
            response.sendRedirect(request.getContextPath() + "/products");
            return;
        }

        // Must be logged in to submit a review
        if (user == null) {
            session = request.getSession(true);
            session.setAttribute("redirectAfterLogin", request.getContextPath() + "/product-details?id=" + productId);
            session.setAttribute("loginNotice", "Please log in to submit a product review.");
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        String ratingParam = request.getParameter("rating");
        String comment = request.getParameter("comment");

        int rating = 5;
        try {
            rating = Integer.parseInt(ratingParam);
            if (rating < 1) rating = 1;
            if (rating > 5) rating = 5;
        } catch (NumberFormatException ignored) {}

        Review review = new Review();
        review.setUserId(user.getId());
        review.setProductId(productId);
        review.setRating(rating);
        review.setComment(com.shopsphere.util.InputSanitizer.escapeHtml(comment));

        boolean success = productService.addReview(review);
        if (success) {
            session.setAttribute("reviewSuccess", "Thank you! Your review has been published.");
        } else {
            session.setAttribute("reviewError", "Unable to post review at this time.");
        }

        response.sendRedirect(request.getContextPath() + "/product-details?id=" + productId);
    }
}
