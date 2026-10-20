/**
 * ShopSphere - Admin Dashboard Interactive Script (Screenshot 5 Faithful)
 * Renders the dual-tone daily sales bar chart ("Earning and Commission")
 * and handles UI interactions (sidebar, modals, filters).
 */

document.addEventListener("DOMContentLoaded", function () {
    initSalesChart();
    initSidebarToggle();
});

function initSidebarToggle() {
    const collapseBtn = document.querySelector(".sidebar-collapse-btn");
    const sidebar = document.querySelector(".admin-sidebar");
    if (collapseBtn && sidebar) {
        collapseBtn.addEventListener("click", function () {
            sidebar.classList.toggle("collapsed");
        });
    }
}

/**
 * Renders high-resolution Canvas bar chart matching Screenshot 5:
 * 31 daily columns, dual stacked/grouped bars for Earning & Commission.
 */
function initSalesChart() {
    const canvas = document.getElementById("salesChartCanvas");
    if (!canvas) return;

    const ctx = canvas.getContext("2d");
    const dpr = window.devicePixelRatio || 1;
    
    // Fit canvas to parent container width and height
    const rect = canvas.parentElement.getBoundingClientRect();
    canvas.width = rect.width * dpr;
    canvas.height = rect.height * dpr;
    canvas.style.width = rect.width + "px";
    canvas.style.height = rect.height + "px";
    ctx.scale(dpr, dpr);

    const width = rect.width;
    const height = rect.height;
    const paddingLeft = 50;
    const paddingRight = 20;
    const paddingTop = 20;
    const paddingBottom = 40;

    const chartWidth = width - paddingLeft - paddingRight;
    const chartHeight = height - paddingTop - paddingBottom;

    // Daily metrics (days 1 through 31) mimicking Screenshot 5 distribution
    const days = 31;
    const earningsData = [
        380, 340, 100, 180, 370, 240, 110, 260, 290, 210, 240, 190, 250, 95, 170, 240, 280, 370,
        180, 360, 370, 190, 260, 375, 360, 230, 360, 350, 40, 340, 250
    ]; // in Thousands ($0 to $400K)
    
    const commissionRatio = 0.35; // Light blue top section

    // Draw horizontal grid lines & Y labels (0, 100K, 200K, 300K, 400K)
    const ySteps = [0, 100, 200, 300, 400];
    ctx.strokeStyle = "#e9edf3";
    ctx.lineWidth = 1;
    ctx.fillStyle = "#8e95ad";
    ctx.font = "11px sans-serif";
    ctx.textAlign = "right";

    ySteps.forEach(val => {
        const y = paddingTop + chartHeight - (val / 400) * chartHeight;
        ctx.beginPath();
        ctx.moveTo(paddingLeft, y);
        ctx.lineTo(width - paddingRight, y);
        ctx.stroke();

        const label = val === 0 ? "0" : val + "K";
        ctx.fillText(label, paddingLeft - 8, y + 4);
    });

    // Draw daily bars
    const colWidth = chartWidth / days;
    const barWidth = Math.max(6, Math.min(10, colWidth * 0.45));

    ctx.textAlign = "center";
    for (let i = 0; i < days; i++) {
        const dayNumber = i + 1;
        const totalVal = earningsData[i];
        const commVal = totalVal * commissionRatio;
        const earnVal = totalVal - commVal;

        const xCenter = paddingLeft + (i * colWidth) + (colWidth / 2);
        const x = xCenter - (barWidth / 2);

        const totalBarHeight = (totalVal / 400) * chartHeight;
        const commBarHeight = (commVal / 400) * chartHeight;
        const earnBarHeight = totalBarHeight - commBarHeight;

        const yBase = paddingTop + chartHeight;
        const yEarn = yBase - earnBarHeight;
        const yComm = yEarn - commBarHeight;

        // Draw Earn bar (Dark Navy Blue)
        ctx.fillStyle = "#1e3a8a";
        ctx.beginPath();
        ctx.rect(x, yEarn, barWidth, earnBarHeight);
        ctx.fill();

        // Draw Commission bar on top (Light Sky Blue with rounded top cap)
        ctx.fillStyle = "#38bdf8";
        ctx.beginPath();
        const r = 2; // radius
        ctx.moveTo(x, yEarn);
        ctx.lineTo(x, yComm + r);
        ctx.quadraticCurveTo(x, yComm, x + r, yComm);
        ctx.lineTo(x + barWidth - r, yComm);
        ctx.quadraticCurveTo(x + barWidth, yComm, x + barWidth, yComm + r);
        ctx.lineTo(x + barWidth, yEarn);
        ctx.closePath();
        ctx.fill();

        // X Axis labels (day numbers 1 through 31)
        ctx.fillStyle = "#8e95ad";
        ctx.font = "10px sans-serif";
        ctx.fillText(dayNumber.toString(), xCenter, yBase + 18);
    }
}

// Window resize listener to keep chart sharp
window.addEventListener("resize", function () {
    initSalesChart();
});
