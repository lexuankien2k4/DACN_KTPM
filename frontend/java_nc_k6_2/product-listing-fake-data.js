// Trong file: product-listing-fake-data.js

document.addEventListener('DOMContentLoaded', function () {
    // ... (toàn bộ code helper, data mẫu, biến phân trang đã có từ trước) ...

    // --- Helper Functions (Hàm tiện ích) ---
    function getRandomInt(min, max) {
        return Math.floor(Math.random() * (max - min + 1)) + min;
    }

    function formatCurrencyVND(amount) {
        if (typeof amount !== 'number') return '0 ₫';
        return amount.toLocaleString('vi-VN', { style: 'currency', currency: 'VND' });
    }

    // --- Sample Data Arrays (Mảng dữ liệu mẫu) ---
    const productBaseNames = [
        "Thịt Ba Chỉ Bò", "Tôm Sú Tươi", "Cá Hồi Fillet", "Rau Cải Ngọt", "Gạo ST25", "Ức Gà Ta",
        "Nho Xanh Không Hạt", "Sữa Chua Tự Nhiên", "Trứng Gà Omega-3", "Bánh Mì Nguyên Cám", "Dầu Olive Extra Virgin",
        "Nước Mắm Cốt", "Xúc Xích Đức Việt", "Phô Mai Cheddar", "Mì Ý Spaghetti", "Sườn Non Heo",
        "Cua Biển Cà Mau", "Bí Đỏ Hồ Lô", "Nấm Đông Cô Khô", "Hạt Điều Rang Muối"
    ];
    const brands = ["VinMart Cook", "CP Foods", "Meat Deli", "OrgaFood", "Dalat Farm", "TH True Milk", "Lavie", "Ajinomoto", "Chinsu", ""];
    const origins = ["Việt Nam", "Mỹ", "Úc", "Na Uy", "Nhật Bản", "Hàn Quốc", "Thái Lan"];
    const categories = [
        { id: "DM01", name: "Thịt Tươi Sống" }, { id: "DM02", name: "Hải Sản Tươi" },
        { id: "DM03", name: "Rau Củ Quả" }, { id: "DM04", name: "Thực Phẩm Khô & Gia Vị" },
        { id: "DM05", name: "Đồ Uống & Sữa" }, { id: "DM06", name: "Đồ Ăn Liền & Chế Biến" }
    ];
    const imagePlaceholders = ["img/1.jpg", "img/2.jpg", "img/3.jpg", "img/4.jpg", "img/5.jpg"];

    let allProducts = [];
    let currentPage = 1;
    const productsPerPage = 10;

    function createFakeProduct(index) {
        const baseName = productBaseNames[getRandomInt(0, productBaseNames.length - 1)];
        const brandName = brands[getRandomInt(0, brands.length - 1)];
        const originName = origins[getRandomInt(0, origins.length - 1)];
        const categoryObj = categories[getRandomInt(0, categories.length - 1)];

        return {
            stt: index + 1,
            maSanPham: `SP${String(getRandomInt(1000, 9999))}${String.fromCharCode(65 + getRandomInt(0,3))}`,
            tenSanPham: `${baseName} ${brandName ? '('+brandName+')' : ''} - ${originName}`.trim().replace(/\s*-\s*$/, ''),
            maDanhMuc: categoryObj.id,
            tenDanhMuc: categoryObj.name,
            khoHang: getRandomInt(0, 200),
            giaBanLe: getRandomInt(20, 500) * 1000,
            urlHinhAnhChinh: imagePlaceholders[getRandomInt(0, imagePlaceholders.length - 1)],
            thuongHieu: brandName,
            xuatXu: originName,
            moTaNgan: `Mô tả ngắn cho ${baseName}. Sản phẩm chất lượng cao, đảm bảo an toàn vệ sinh.`,
        };
    }

    function generateAllProducts(numberOfProducts = 50) {
        allProducts = [];
        for (let i = 0; i < numberOfProducts; i++) {
            allProducts.push(createFakeProduct(i));
        }
    }

    function displayCurrentPageProducts() {
        const tableBody = document.getElementById('car-table-body'); // Giữ ID này nếu HTML của bạn vẫn dùng nó cho bảng sản phẩm
        if (!tableBody) {
            console.error("Không tìm thấy tbody với ID 'car-table-body'!");
            return;
        }
        tableBody.innerHTML = ''; 

        const startIndex = (currentPage - 1) * productsPerPage;
        const endIndex = Math.min(startIndex + productsPerPage, allProducts.length);
        const productsToDisplay = allProducts.slice(startIndex, endIndex);

        productsToDisplay.forEach(product => {
            const row = tableBody.insertRow();
            row.insertCell().textContent = product.stt;
            row.insertCell().textContent = product.maSanPham;
            row.insertCell().textContent = product.tenSanPham;
            row.insertCell().textContent = product.tenDanhMuc;
            row.insertCell().textContent = product.khoHang;
            row.insertCell().textContent = formatCurrencyVND(product.giaBanLe);

            const imgCell = row.insertCell();
            const img = document.createElement('img');
            img.src = product.urlHinhAnhChinh;
            img.alt = product.tenSanPham;
            img.style.width = '50px';
            img.style.height = '50px';
            img.style.objectFit = 'cover';
            img.style.borderRadius = '4px';
            imgCell.appendChild(img);

            // === PHẦN CẬP NHẬT NÚT HÀNH ĐỘNG CHO SẢN PHẨM ===
            const actionCell = row.insertCell();
            actionCell.innerHTML = `
                <button class="icon-btn view-btn" title="Xem chi tiết" onclick="viewProduct('${product.maSanPham}')">
                    <i class="fas fa-eye"></i>
                </button>
                <button class="icon-btn edit-btn" title="Cập nhật" onclick="editProduct('${product.maSanPham}')">
                    <i class="fas fa-edit"></i>
                </button>
                <button class="icon-btn delete-btn" title="Xóa" onclick="deleteProduct('${product.maSanPham}')">
                    <i class="fas fa-trash-alt"></i>
                </button>
            `;
            // === KẾT THÚC PHẦN CẬP NHẬT ===
        });
    }

    // --- Function to Update Pagination Controls UI ---
    function updatePaginationUI() {
        const pageNumbersContainer = document.getElementById('pageNumbersContainer');
        const prevPageBtn = document.getElementById('prevPageBtn');
        const nextPageBtn = document.getElementById('nextPageBtn');
        const pageInfo = document.getElementById('pageInfo');

        if (!pageNumbersContainer || !prevPageBtn || !nextPageBtn || !pageInfo) {
            // Phân trang có thể không tồn tại trên mọi trang, không cần báo lỗi
            return;
        }
        
        const paginationControlsDiv = document.querySelector('.pagination-controls');
        if (!paginationControlsDiv) return;


        pageNumbersContainer.innerHTML = ''; 
        const totalPages = Math.ceil(allProducts.length / productsPerPage);

        if (totalPages <= 1) { 
            paginationControlsDiv.style.display = 'none';
            return;
        }
        paginationControlsDiv.style.display = 'flex';

        for (let i = 1; i <= totalPages; i++) {
            const pageButton = document.createElement('button');
            pageButton.textContent = i;
            pageButton.classList.add('btn', 'btn-page-number');
            if (i === currentPage) {
                pageButton.classList.add('active');
            }
            pageButton.addEventListener('click', function () {
                currentPage = i;
                displayCurrentPageProducts();
                updatePaginationUI();
            });
            pageNumbersContainer.appendChild(pageButton);
        }

        pageInfo.textContent = `Trang ${currentPage} / ${totalPages}`;
        prevPageBtn.disabled = currentPage === 1;
        nextPageBtn.disabled = currentPage === totalPages;
    }

    // Event Listeners for Prev/Next Buttons
    const prevPageBtn = document.getElementById('prevPageBtn');
    const nextPageBtn = document.getElementById('nextPageBtn');

    if (prevPageBtn) {
        prevPageBtn.addEventListener('click', () => {
            if (currentPage > 1) {
                currentPage--;
                displayCurrentPageProducts();
                updatePaginationUI();
            }
        });
    }

    if (nextPageBtn) {
        nextPageBtn.addEventListener('click', () => {
            const totalPages = Math.ceil(allProducts.length / productsPerPage);
            if (currentPage < totalPages) {
                currentPage++;
                displayCurrentPageProducts();
                updatePaginationUI();
            }
        });
    }
    
    // --- Dummy functions for actions ---
    // THÊM HÀM viewProduct
    window.viewProduct = function(productId) {
        const product = allProducts.find(p => p.maSanPham === productId);
        if (product) {
            alert(`Xem chi tiết Sản phẩm:
                Mã SP: ${product.maSanPham}
                Tên: ${product.tenSanPham}
                Danh mục: ${product.tenDanhMuc}
                Giá bán: ${formatCurrencyVND(product.giaBanLe)}
                Kho hàng: ${product.khoHang}
                Mô tả: ${product.moTaNgan}`);
        } else {
            alert("Không tìm thấy thông tin sản phẩm!");
        }
    }

    window.editProduct = function(productId) {
        alert(`Chức năng sửa sản phẩm: ${productId} (chưa được triển khai)`);
    }

    window.deleteProduct = function(productId) {
        if (confirm(`Bạn có chắc chắn muốn xóa sản phẩm ${productId} không?`)) {
            // Tạm thời chỉ ẩn đi, không xóa thật khỏi allProducts để giữ phân trang đơn giản
            // Trong thực tế, bạn sẽ gọi API xóa và tải lại dữ liệu hoặc xóa khỏi allProducts và tính toán lại phân trang.
            const productRow = Array.from(document.getElementById('car-table-body').querySelectorAll('button'))
                                .find(btn => btn.outerHTML.includes(`deleteProduct('${productId}')`))
                                ?.closest('tr');
            if(productRow) productRow.style.display = 'none'; // Ẩn hàng đi
            alert(`Sản phẩm ${productId} đã được "xóa" (chức năng giả - chỉ ẩn hàng). Vui lòng tải lại trang để xem đầy đủ nếu cần.`);
            // Nếu muốn xóa thật và cập nhật phân trang:
            // allProducts = allProducts.filter(p => p.maSanPham !== productId);
            // if (currentPage > Math.ceil(allProducts.length / productsPerPage) && currentPage > 1) {
            //     currentPage--; // Lùi về trang trước nếu trang hiện tại trống
            // }
            // displayCurrentPageProducts();
            // updatePaginationUI();
        }
    }

    // --- Initial data load ---
    if (document.getElementById('car-table-body')) { // Chỉ chạy nếu có bảng sản phẩm/nhân viên
        generateAllProducts(53); 
        displayCurrentPageProducts();
        updatePaginationUI();
        console.log("Product listing fake data with pagination loaded!");
    }
});