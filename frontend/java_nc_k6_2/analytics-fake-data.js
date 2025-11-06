document.addEventListener('DOMContentLoaded', function () {
    // --- Helper Functions ---
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
        return amount.toLocaleString('vi-VN', { style: 'currency', currency: 'VND' });
    }

    const vietnameseNames = [
        "Nguyễn Văn An", "Trần Thị Bình", "Lê Hoàng Cường", "Phạm Minh Châu", "Võ Thanh Dũng",
        "Đỗ Gia Hân", "Hoàng Ngọc Khánh", "Bùi Phương Linh", "Đặng Quốc Minh", "Ngô Thùy Dương",
        "Lý Thành Đạt", "Trịnh Bảo Vy", "Mai Anh Tuấn", "Dương Mỹ Lệ", "Hồ Quang Khải"
    ];

    const productNames = [
        "Thịt Ba Chỉ Bò Mỹ", "Tôm Sú Tươi", "Cá Hồi Fillet", "Rau Cải Ngọt VietGAP", "Gạo ST25",
        "Ức Gà Công Nghiệp", "Nho Xanh Không Hạt", "Sữa Chua Vinamilk", "Trứng Gà Ta", "Bánh Mì Sandwich",
        "Dầu Ăn Simply", "Nước Mắm Nam Ngư", "Xúc Xích CP", "Phô Mai Con Bò Cười", "Mì Gói Hảo Hảo"
    ];

    const categories = ["Thịt Tươi Sống", "Hải Sản", "Rau Củ Quả", "Gạo & Nông Sản Khô", "Thực Phẩm Chế Biến", "Đồ Uống", "Sản Phẩm Từ Sữa"];

    // --- Chart Instances (để có thể destroy khi cập nhật) ---
    let revenueChartInstance;
    let topProductsChartInstance;
    let orderStatusChartInstance;
    let customerSegmentsChartInstance;

    // --- Data Generation Functions ---
    function generateOverviewData() {
        const topProductIndex = getRandomInt(0, productNames.length - 1);
        return {
            totalOrders: getRandomInt(50, 500),
            totalRevenue: getRandomInt(50000000, 500000000),
            newCustomers: getRandomInt(10, 100),
            topProduct: {
                name: productNames[topProductIndex],
                quantity: getRandomInt(20, 150)
            }
        };
    }

    function generateRevenueChartData(days = 7) {
        const labels = [];
        const data = [];
        const today = new Date();
        for (let i = days - 1; i >= 0; i--) {
            const date = new Date(today);
            date.setDate(today.getDate() - i);
            labels.push(`${date.getDate()}/${date.getMonth() + 1}`);
            data.push(getRandomInt(1000000, 15000000));
        }
        return { labels, data };
    }

    function generateTopProductsChartData(count = 5) {
        const labels = [];
        const data = [];
        const usedIndices = new Set();
        while (labels.length < count && usedIndices.size < productNames.length) {
            const index = getRandomInt(0, productNames.length - 1);
            if (!usedIndices.has(index)) {
                labels.push(productNames[index].substring(0, 15) + (productNames[index].length > 15 ? '...' : '')); // Rút gọn tên SP
                data.push(getRandomInt(30, 200));
                usedIndices.add(index);
            }
        }
        return { labels, data };
    }

    function generateOrderStatusChartData() {
        return {
            labels: ["Hoàn thành", "Đang xử lý", "Đã hủy", "Đang giao"],
            data: [
                getRandomInt(100, 300), // Hoàn thành
                getRandomInt(20, 80),   // Đang xử lý
                getRandomInt(5, 30),    // Đã hủy
                getRandomInt(15, 50)    // Đang giao
            ]
        };
    }

    function generateCustomerSegmentsData() {
        return {
            labels: ["Khách mới", "Khách quay lại"],
            data: [
                getRandomInt(20, 100), // Khách mới
                getRandomInt(80, 250)  // Khách quay lại
            ]
        };
    }

    function generateRecentOrdersData(count = 5) {
        const orders = [];
        const statusOptions = [
            { text: "Hoàn thành", class: "status-completed" },
            { text: "Đang xử lý", class: "status-processing" },
            { text: "Đang giao", class: "status-pending" }, // Giả sử class status-pending cho "Đang giao"
            { text: "Đã hủy", class: "status-cancelled" }
        ];
        const today = new Date();
        const sevenDaysAgo = new Date();
        sevenDaysAgo.setDate(today.getDate() - 7);

        for (let i = 0; i < count; i++) {
            orders.push({
                id: `#${getRandomInt(10000, 99999)}`,
                customer: vietnameseNames[getRandomInt(0, vietnameseNames.length - 1)],
                date: getRandomDate(sevenDaysAgo, today),
                total: getRandomInt(50000, 2000000),
                status: statusOptions[getRandomInt(0, statusOptions.length - 1)]
            });
        }
        return orders.sort((a, b) => new Date(b.date.split('/').reverse().join('-')) - new Date(a.date.split('/').reverse().join('-'))); // Sắp xếp mới nhất trước
    }

    function generateLowStockProductsData(count = 4) {
        const products = [];
        const usedIndices = new Set();
        while (products.length < count && usedIndices.size < productNames.length) {
            const index = getRandomInt(0, productNames.length - 1);
            if (!usedIndices.has(index)) {
                const stock = getRandomInt(1, 9);
                products.push({
                    id: `SP${String(getRandomInt(1, 999)).padStart(3, '0')}`,
                    name: productNames[index],
                    category: categories[getRandomInt(0, categories.length - 1)],
                    stock: stock,
                    threshold: stock + getRandomInt(3,10) // Ngưỡng luôn cao hơn tồn kho
                });
                usedIndices.add(index);
            }
        }
        return products;
    }


    // --- DOM Population Functions ---
    function populateKPIs(data) {
        document.getElementById('total-orders').textContent = data.totalOrders;
        document.getElementById('total-revenue').textContent = formatCurrencyVND(data.totalRevenue);
        document.getElementById('new-customers').textContent = data.newCustomers;
        document.getElementById('top-product-name').textContent = data.topProduct.name;
        document.getElementById('top-product-quantity').textContent = `Số lượng: ${data.topProduct.quantity}`;
    }

    function renderRevenueChart(chartData) {
        const ctx = document.getElementById('revenueChart').getContext('2d');
        if (revenueChartInstance) {
            revenueChartInstance.destroy();
        }
        revenueChartInstance = new Chart(ctx, {
            type: 'line',
            data: {
                labels: chartData.labels,
                datasets: [{
                    label: 'Doanh thu',
                    data: chartData.data,
                    borderColor: 'rgba(108, 155, 207, 1)', // var(--color-primary)
                    backgroundColor: 'rgba(108, 155, 207, 0.2)',
                    tension: 0.2,
                    fill: true,
                }]
            },
            options: {
                responsive: true,
                maintainAspectRatio: false,
                scales: {
                    y: {
                        beginAtZero: true,
                        ticks: {
                            callback: function(value) { return formatCurrencyVND(value); }
                        }
                    }
                },
                plugins: { legend: { display: true } }
            }
        });
    }

    function renderTopProductsChart(chartData) {
        const ctx = document.getElementById('topProductsChart').getContext('2d');
        if (topProductsChartInstance) {
            topProductsChartInstance.destroy();
        }
        topProductsChartInstance = new Chart(ctx, {
            type: 'bar',
            data: {
                labels: chartData.labels,
                datasets: [{
                    label: 'Số lượng bán',
                    data: chartData.data,
                    backgroundColor: [
                        'rgba(27, 156, 133, 0.7)',  // var(--color-success)
                        'rgba(108, 155, 207, 0.7)', // var(--color-primary)
                        'rgba(247, 208, 96, 0.7)',  // var(--color-warning)
                        'rgba(255, 0, 96, 0.6)',    // var(--color-danger)
                        'rgba(125, 141, 161, 0.7)'  // var(--color-info-dark)
                    ],
                    borderColor: [
                        'rgba(27, 156, 133, 1)',
                        'rgba(108, 155, 207, 1)',
                        'rgba(247, 208, 96, 1)',
                        'rgba(255, 0, 96, 1)',
                        'rgba(125, 141, 161, 1)'
                    ],
                    borderWidth: 1
                }]
            },
            options: {
                responsive: true,
                maintainAspectRatio: false,
                indexAxis: 'y', // Hiển thị bar ngang cho dễ đọc tên sản phẩm
                scales: { x: { beginAtZero: true } },
                plugins: { legend: { display: false } } // Ẩn legend vì label đã rõ
            }
        });
    }

    function renderOrderStatusChart(chartData) {
        const ctx = document.getElementById('orderStatusChart').getContext('2d');
        if (orderStatusChartInstance) {
            orderStatusChartInstance.destroy();
        }
        orderStatusChartInstance = new Chart(ctx, {
            type: 'doughnut',
            data: {
                labels: chartData.labels,
                datasets: [{
                    label: 'Trạng thái đơn hàng',
                    data: chartData.data,
                    backgroundColor: [
                        'rgba(27, 156, 133, 0.8)',  // Hoàn thành (Success)
                        'rgba(247, 208, 96, 0.8)',  // Đang xử lý (Warning)
                        'rgba(255, 0, 96, 0.7)',    // Đã hủy (Danger)
                        'rgba(108, 155, 207, 0.8)'  // Đang giao (Primary/Info)
                    ],
                    hoverOffset: 4
                }]
            },
            options: {
                responsive: true,
                maintainAspectRatio: false,
                plugins: { legend: { position: 'top' } }
            }
        });
    }

    function renderCustomerSegmentsChart(chartData) {
        const ctx = document.getElementById('customerSegmentsChart').getContext('2d');
        if (customerSegmentsChartInstance) {
            customerSegmentsChartInstance.destroy();
        }
        customerSegmentsChartInstance = new Chart(ctx, {
            type: 'pie',
            data: {
                labels: chartData.labels,
                datasets: [{
                    data: chartData.data,
                    backgroundColor: [
                        'rgba(108, 155, 207, 0.8)', // Khách mới (Primary)
                        'rgba(27, 156, 133, 0.8)'   // Khách quay lại (Success)
                    ],
                    hoverOffset: 4
                }]
            },
            options: {
                responsive: true,
                maintainAspectRatio: false,
                plugins: { legend: { position: 'top' } }
            }
        });
    }

    function populateRecentOrdersTable(orders) {
        const tableBody = document.getElementById('recent-orders-table').getElementsByTagName('tbody')[0];
        if (!tableBody) return;
        tableBody.innerHTML = ''; // Xóa dữ liệu cũ

        orders.forEach(order => {
            const row = tableBody.insertRow();
            row.insertCell().textContent = order.id;
            row.insertCell().textContent = order.customer;
            row.insertCell().textContent = order.date;
            row.insertCell().textContent = formatCurrencyVND(order.total);

            const statusCell = row.insertCell();
            statusCell.textContent = order.status.text;
            statusCell.className = order.status.class;

            const detailCell = row.insertCell();
            const link = document.createElement('a');
            link.href = `don-hang.html?id=${order.id.replace('#','')}`; // Giả sử link đến trang chi tiết
            link.textContent = 'Xem';
            detailCell.appendChild(link);
        });
    }

    function populateLowStockTable(products) {
        const tableBody = document.getElementById('low-stock-products-table').getElementsByTagName('tbody')[0];
        if(!tableBody) return;
        tableBody.innerHTML = ''; // Xóa dữ liệu cũ

        products.forEach(product => {
            const row = tableBody.insertRow();
            row.insertCell().textContent = product.id;
            row.insertCell().textContent = product.name;
            row.insertCell().textContent = product.category;

            const stockCell = row.insertCell();
            stockCell.textContent = product.stock;
            if (product.stock < product.threshold) { // Logic này đúng hơn là <= threshold
                stockCell.classList.add('stock-low');
            }

            row.insertCell().textContent = product.threshold;

            const actionCell = row.insertCell();
            const link = document.createElement('a');
            link.href = `kho-hang.html?product_id=${product.id}`; // Giả sử link đến trang kho hàng
            link.textContent = 'Nhập hàng';
            actionCell.appendChild(link);
        });
    }

    // --- Load all data on page start ---
    function loadAllAnalyticsData() {
        populateKPIs(generateOverviewData());
        renderRevenueChart(generateRevenueChartData());
        renderTopProductsChart(generateTopProductsChartData());
        renderOrderStatusChart(generateOrderStatusChartData());
        renderCustomerSegmentsChart(generateCustomerSegmentsData());
        populateRecentOrdersTable(generateRecentOrdersData());
        populateLowStockTable(generateLowStockProductsData());

        console.log("Fake analytics data loaded.");
    }

    // --- Initial Load ---
    loadAllAnalyticsData();

    // --- Optional: Handle Date Range Filter (Giả lập) ---
    // Nếu bạn có bộ lọc ngày tháng như trong HTML gợi ý trước đó:
    const dateRangeSelect = document.getElementById('date-range');
    if (dateRangeSelect) {
        dateRangeSelect.addEventListener('change', function() {
            console.log(`Date range changed to: ${this.value}. Reloading fake data...`);
            // Trong ứng dụng thật, bạn sẽ fetch dữ liệu mới từ backend
            // Ở đây, chúng ta chỉ cần gọi lại hàm load để tạo dữ liệu giả mới
            if (this.value !== 'custom') { // Bỏ qua custom cho đơn giản
                 loadAllAnalyticsData();
            }
        });
    }
    const applyCustomDateButton = document.getElementById('apply-custom-date');
    if(applyCustomDateButton){
        applyCustomDateButton.addEventListener('click', function(){
            // Lấy giá trị ngày tháng tùy chỉnh và gọi API (hoặc loadAllAnalyticsData)
            console.log("Applying custom date range. Reloading fake data...");
            loadAllAnalyticsData();
        })
    }

});