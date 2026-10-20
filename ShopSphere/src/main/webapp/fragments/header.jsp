<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title><c:out value="${pageTitle != null ? pageTitle : 'ShopSphere — Nordic Retail Luxe E-Commerce'}" /></title>
    
    <!-- Google Fonts & Material Symbols -->
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Inter:wght@400;500;600&family=Plus+Jakarta+Sans:wght@600;700;800&display=swap" rel="stylesheet">
    <link href="https://fonts.googleapis.com/css2?family=Material+Symbols+Outlined:opsz,wght,FILL,GRAD@20..48,100..700,0..1,-50..200" rel="stylesheet">

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

    <!-- Project Stylesheet & Bootstrap Utility Interop -->
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body class="bg-background font-body-md text-on-surface antialiased">

    <!-- Top Announcement Bar (Nordic Luxe) -->
    <div class="w-full bg-primary text-on-primary py-space-xs px-gutter">
        <div class="max-w-[1440px] mx-auto flex items-center justify-between text-xs font-medium">
            <div class="flex items-center gap-space-sm">
                <span class="bg-surface-container-highest text-on-surface px-space-sm py-0.5 rounded-full text-[11px] font-bold">Free Shipping</span>
                <span>HOT: Get 50% Off on Selected Curations | <a class="underline hover:text-surface-variant transition-colors" href="${pageContext.request.contextPath}/products">Shop Now</a></span>
            </div>
            <div class="hidden md:flex items-center gap-space-md text-xs">
                <span class="flex items-center gap-1"><span class="material-symbols-outlined text-sm text-secondary-fixed">call</span> Helpline: +91 1800-SPHERE</span>
                <span class="text-outline">|</span>
                <span class="flex items-center gap-1"><span class="material-symbols-outlined text-sm text-secondary-fixed">local_shipping</span> Express Global Delivery</span>
                <span class="text-outline">|</span>
                <a class="hover:text-surface-variant transition-colors flex items-center gap-1" href="${pageContext.request.contextPath}/wishlist">
                    <span class="material-symbols-outlined text-sm">bookmark</span> Wishlist Preview
                </a>
            </div>
        </div>
    </div>
