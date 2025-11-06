document.addEventListener('DOMContentLoaded', function () {
    // --- Helper Functions (Hàm tiện ích) ---
    function getRandomInt(min, max) {
        return Math.floor(Math.random() * (max - min + 1)) + min;
    }

    function getRandomDate(startDate, endDate) {
        const date = new Date(startDate.getTime() + Math.random() * (endDate.getTime() - startDate.getTime()));
        const day = String(date.getDate()).padStart(2, '0');
        const month = String(date.getMonth() + 1).padStart(2, '0'); // Tháng bắt đầu từ 0
        const year = date.getFullYear();
        return `${day}/${month}/${year}`;
    }

    function formatCurrencyVND(amount) {
        if (typeof amount !== 'number') return '0 ₫';
        return amount.toLocaleString('vi-VN', { style: 'currency', currency: 'VND' });
    }

    const vietnameseNames = [
        "Nguyễn Ánh Tuyết", "Trần Minh Đức", "Lê Bảo An", "Phạm Gia Huy", "Võ Ngọc Mai",
        "Đặng Nhật Anh", "Hoàng Yến Nhi", "Bùi Quang Vinh", "Ngô Khánh Linh", "Lý Gia Bảo"
    ];

    const productNamesShort = [
        "Ba Chỉ Bò Mỹ", "Tôm Sú", "Cá Hồi Fillet", "Cải Ngọt", "Gạo ST25",
        "Ức Gà", "Nho Xanh", "Sữa Chua", "Trứng Gà Ta", "Bánh Mì"
    ];

    const orderStatuses = [
        { text: "Mới", class: "status-new" },
        { text: "Đang xử lý", class: "status-processing" },
        { text: "Đang giao", class: "status-delivering" }, // Giả sử có class này trong dashboard.css
        { text: "Hoàn thành", class: "status-completed" },
        { text: "Đã hủy", class: "status-cancelled" }
    ];

    let dbOrderStatusPieChartInstance; // Biến lưu trữ biểu đồ

    // --- Data Generation & DOM Update Functions ---

    // 1. Cập nhật các thẻ KPI
    function populateKPIs() {
        document.getElementById('db-new-orders-today').textContent = getRandomInt(5, 30);
        document.getElementById('db-processing-orders').textContent = getRandomInt(10, 50);
        document.getElementById('db-low-stock-items').textContent = getRandomInt(0, 15);
        document.getElementById('db-daily-sales').textContent = formatCurrencyVND(getRandomInt(5000000, 25000000));
    }

    // 2. Điền dữ liệu vào bảng "Đơn hàng gần đây"
    function populateRecentOrdersTable(numberOfOrders = 4) {
        const tableBody = document.getElementById('db-recent-orders-table')?.getElementsByTagName('tbody')[0];
        if (!tableBody) return;

        tableBody.innerHTML = ''; // Xóa dữ liệu cũ

        for (let i = 0; i < numberOfOrders; i++) {
            const randomStatus = orderStatuses[getRandomInt(0, orderStatuses.length - 2)]; // Tránh "Đã hủy" nhiều
            const row = tableBody.insertRow();
            row.insertCell().textContent = `#${getRandomInt(10000, 19999)}`;
            row.insertCell().textContent = vietnameseNames[getRandomInt(0, vietnameseNames.length - 1)];
            row.insertCell().textContent = formatCurrencyVND(getRandomInt(150000, 1500000));

            const statusCell = row.insertCell();
            const statusSpan = document.createElement('span');
            statusSpan.className = `status-tag ${randomStatus.class}`;
            statusSpan.textContent = randomStatus.text;
            statusCell.appendChild(statusSpan);
        }
    }

    // 3. Vẽ biểu đồ tròn "Tỷ lệ trạng thái đơn (hôm nay)"
    function renderOrderStatusPieChart() {
        const canvas = document.getElementById('db-order-status-pie-chart');
        if (!canvas) return;
        const ctx = canvas.getContext('2d');

        if (dbOrderStatusPieChartInstance) {
            dbOrderStatusPieChartInstance.destroy(); // Hủy biểu đồ cũ nếu có
        }

        // Lấy giá trị màu đã được tính toán từ các biến CSS
        const computedStyles = getComputedStyle(document.documentElement);
        const colorPrimary = computedStyles.getPropertyValue('--color-primary').trim();
        const colorWarning = computedStyles.getPropertyValue('--color-warning').trim();
        const colorInfoDark = computedStyles.getPropertyValue('--color-info-dark').trim();
        const colorSuccess = computedStyles.getPropertyValue('--color-success').trim();

        // Màu dự phòng trường hợp biến CSS không được tìm thấy (ít khả năng xảy ra)
        const fallbackPrimary = '#6C9BCF';
        const fallbackWarning = '#F7D060';
        const fallbackInfoDark = '#7d8da1';
        const fallbackSuccess = '#1B9C85';

        const data = {
            labels: ['Mới', 'Đang xử lý', 'Đang giao', 'Hoàn thành'],
            datasets: [{
                label: 'Trạng thái đơn hàng',
                data: [
                    getRandomInt(5, 20),  // Mới
                    getRandomInt(10, 30), // Đang xử lý
                    getRandomInt(8, 25),  // Đang giao
                    getRandomInt(20, 60)  // Hoàn thành
                ],
                backgroundColor: [
                    colorPrimary || fallbackPrimary,       // Sử dụng màu từ CSS var, nếu không có thì dùng màu dự phòng
                    colorWarning || fallbackWarning,
                    colorInfoDark || fallbackInfoDark,
                    colorSuccess || fallbackSuccess
                ],
                hoverOffset: 4
            }]
        };

        dbOrderStatusPieChartInstance = new Chart(ctx, {
            type: 'pie', // Hoặc 'doughnut' nếu muốn
            data: data,
            options: {
                responsive: true,
                maintainAspectRatio: false, // Quan trọng khi canvas có height cố định
                plugins: {
                    legend: {
                        position: 'bottom', // Vị trí chú giải
                        labels: {
                            padding: 15,
                            font: {
                                size: 10 // Cỡ chữ chú giải
                            }
                        }
                    }
                }
            }
        });
    }

    // 4. Hiển thị các cảnh báo (ví dụ)
    function populateAlerts(numberOfAlerts = 0) { // Mặc định không có cảnh báo để hiển thị "db-no-alerts"
        const alertListDiv = document.getElementById('db-alert-list');
        const noAlertsP = document.getElementById('db-no-alerts');
        if (!alertListDiv || !noAlertsP) return;

        alertListDiv.innerHTML = ''; // Xóa cảnh báo cũ

        if (numberOfAlerts === 0) {
            noAlertsP.style.display = 'block';
            alertListDiv.style.display = 'none';
            return;
        }

        noAlertsP.style.display = 'none';
        alertListDiv.style.display = 'block';

        const alertTypes = [
            { type: 'critical', icon: 'error', messagePrefix: 'Hết hàng:', actionText: 'Nhập hàng' },
            { type: 'warning', icon: 'warning_amber', messagePrefix: 'Sắp hết:', actionText: 'Kiểm tra' },
            { type: 'info', icon: 'campaign', messagePrefix: 'Gợi ý:', actionText: 'Xem ngay' }
        ];

        for (let i = 0; i < numberOfAlerts; i++) {
            const randomAlertType = alertTypes[getRandomInt(0, alertTypes.length - 1)];
            const randomProduct = productNamesShort[getRandomInt(0, productNamesShort.length - 1)];
            const alertProductId = `SP${getRandomInt(100,999)}`;

            const alertItem = document.createElement('div');
            alertItem.className = `alert-item ${randomAlertType.type}`;

            alertItem.innerHTML = `
                <span class="material-icons-sharp">${randomAlertType.icon}</span>
                <div>
                    <strong>${randomAlertType.messagePrefix}</strong> <a href="san-pham.html?id=${alertProductId}">${randomProduct} (${alertProductId})</a>
                    <small>Số lượng còn lại: ${getRandomInt(0,5)} - Cần chú ý ngay!</small>
                </div>
                <a href="kho-hang.html?product_id=${alertProductId}" class="alert-action-btn">${randomAlertType.actionText}</a>
            `;
            alertListDiv.appendChild(alertItem);
        }
    }


    // --- Chạy các hàm khi trang được tải ---
    populateKPIs();
    populateRecentOrdersTable(4); // Hiển thị 4 đơn hàng gần đây
    renderOrderStatusPieChart();
    populateAlerts(getRandomInt(0, 3)); // Hiển thị ngẫu nhiên từ 0 đến 3 cảnh báo

    console.log("Dashboard fake data loaded!");
});