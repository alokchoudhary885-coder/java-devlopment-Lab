<%@ tag description="Dynamic Star Rating Display" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ attribute name="score" required="true" type="java.lang.Object" %>
<%@ attribute name="count" required="false" type="java.lang.Integer" %>

<div class="d-inline-flex align-items-center gap-1 text-warning" style="font-size: 0.85rem;">
    <c:set var="numScore" value="${score != null ? score : 0.0}" />
    
    <c:forEach var="i" begin="1" end="5">
        <c:choose>
            <c:when test="${numScore >= i}">
                <i class="bi bi-star-fill"></i>
            </c:when>
            <c:when test="${numScore >= (i - 0.5)}">
                <i class="bi bi-star-half"></i>
            </c:when>
            <c:otherwise>
                <i class="bi bi-star text-muted" style="opacity: 0.4;"></i>
            </c:otherwise>
        </c:choose>
    </c:forEach>
    
    <span class="text-dark fw-bold ms-1" style="font-size: 0.8rem;">${numScore}</span>
    
    <c:if test="${not empty count && count > 0}">
        <span class="text-muted small" style="font-size: 0.75rem;">(${count} Reviews)</span>
    </c:if>
</div>
