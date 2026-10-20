package com.shopsphere.controller.admin;

import com.shopsphere.dao.CouponDAO;
import com.shopsphere.model.Coupon;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Date;
import java.util.List;

/**
 * Controller for Administrative Promotional Coupon Management.
 * Handles generation of promo codes, discount percentages, limits, expiry dates, and revocation.
 * Mapped to /admin/coupons
 */
@WebServlet(name = "AdminCouponServlet", urlPatterns = {"/admin/coupons"})
public class AdminCouponServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private transient CouponDAO couponDAO;

    @Override
    public void init() throws ServletException {
        super.init();
        this.couponDAO = new CouponDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        List<Coupon> coupons = couponDAO.findAll();
        request.setAttribute("coupons", coupons);
        request.setAttribute("activeNav", "coupons");

        // Flash message handling
        HttpSession session = request.getSession(false);
        if (session != null) {
            String flashMessage = (String) session.getAttribute("flashMessage");
            String flashType = (String) session.getAttribute("flashType");
            if (flashMessage != null) {
                request.setAttribute("flashMessage", flashMessage);
                request.setAttribute("flashType", flashType != null ? flashType : "success");
                session.removeAttribute("flashMessage");
                session.removeAttribute("flashType");
            }
        }

        request.getRequestDispatcher("/admin/coupons.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        String action = request.getParameter("action");
        if (action == null) {
            action = "add";
        }

        HttpSession session = request.getSession(true);

        try {
            switch (action) {
                case "add" -> {
                    Coupon coupon = new Coupon();
                    coupon.setCode(request.getParameter("code").trim().toUpperCase());
                    coupon.setDiscountPercent(new BigDecimal(request.getParameter("discountPercent")));
                    coupon.setMaxDiscount(new BigDecimal(request.getParameter("maxDiscount")));
                    coupon.setMinOrderAmount(new BigDecimal(request.getParameter("minOrderAmount")));
                    coupon.setExpiryDate(Date.valueOf(request.getParameter("expiryDate")));
                    coupon.setStatus(Boolean.parseBoolean(request.getParameter("status")));

                    boolean created = couponDAO.insert(coupon);
                    if (created) {
                        session.setAttribute("flashMessage", "Coupon '" + coupon.getCode() + "' created successfully.");
                        session.setAttribute("flashType", "success");
                    } else {
                        session.setAttribute("flashMessage", "Failed to create coupon (code may already exist).");
                        session.setAttribute("flashType", "danger");
                    }
                }
                case "delete" -> {
                    int id = Integer.parseInt(request.getParameter("id"));
                    boolean deleted = couponDAO.delete(id);
                    if (deleted) {
                        session.setAttribute("flashMessage", "Coupon ID #" + id + " deleted.");
                        session.setAttribute("flashType", "success");
                    } else {
                        session.setAttribute("flashMessage", "Failed to delete coupon.");
                        session.setAttribute("flashType", "danger");
                    }
                }
                default -> {
                    session.setAttribute("flashMessage", "Unknown action: " + action);
                    session.setAttribute("flashType", "warning");
                }
            }
        } catch (Exception e) {
            session.setAttribute("flashMessage", "Error processing coupon: " + e.getMessage());
            session.setAttribute("flashType", "danger");
        }

        response.sendRedirect(request.getContextPath() + "/admin/coupons");
    }
}
