<%@ page contentType="text/html;charset=UTF-8" language="java" isErrorPage="true" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>403 - Access Forbidden | ShopSphere</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body class="bg-light d-flex align-items-center justify-content-center" style="min-height: 100vh;">
    <div class="card p-5 text-center shadow-sm" style="max-width: 500px; border-radius: 16px;">
        <div class="text-danger mb-3">
            <i class="bi bi-shield-lock-fill" style="font-size: 4rem;"></i>
        </div>
        <h1 class="h3 fw-bold mb-2">403 - Access Forbidden</h1>
        <p class="text-muted mb-4">
            ${not empty errorMessage ? errorMessage : 'You do not have administrative permissions to view this resource.'}
        </p>
        <div class="d-flex justify-content-center gap-2">
            <a href="${pageContext.request.contextPath}/" class="btn btn-outline-secondary">Go to Home</a>
            <a href="${pageContext.request.contextPath}/login" class="btn btn-dark">Log In with Admin Account</a>
        </div>
    </div>
</body>
</html>
