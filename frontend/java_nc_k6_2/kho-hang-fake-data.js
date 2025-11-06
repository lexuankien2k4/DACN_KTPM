document.addEventListener('DOMContentLoaded', function () {
    // --- Helper Functions ---
    function getRandomInt(min, max) {
        return Math.floor(Math.random() * (max - min + 1)) + min;
    }

    // --- Sample Data for Warehouses ---
    const warehouseNames = [
        "Kho Chính Sài Gòn", "Kho Trung Chuyển Hà Nội", "Kho Phụ Đà Nẵng", "Kho Hàng Lạnh Bình Dương",
        "Kho Tổng Hợp Long An", "Kho Vệ Tinh Quận 9", "Kho Ngoại Quan Cát Lái", "Kho Lưu Trữ Miền Tây",
        "Kho Miền Đông", "Kho An Sương", "Kho Linh Trung", "Kho Sóng Thần 2", "Kho Tân Tạo", "Kho Củ Chi", "Kho Quận 7"
    ];
    const locations = [
        "KCN Tân Bình, TP. HCM", "KCN Ngọc Hồi, Hà Nội", "KCN Hòa Khánh, Đà Nẵng",
        "KCN Sóng Thần 1, Bình Dương", "Huyện Bến Lức, Long An", "Khu Công Nghệ Cao, Q.9, TP. HCM",
        "Cảng Cát Lái, TP. Thủ Đức", "TP. Cần Thơ", "Đồng Nai", "Hóc Môn, TP.HCM", "TP. Thủ Đức",
        "Dĩ An, Bình Dương", "Bình Tân, TP.HCM", "H. Củ Chi, TP.HCM", "KCX Tân Thuận, Q.7, TP.HCM"
    ];

    // --- Pagination Variables ---
    let allWarehouses = [];
    let filteredWarehouses = []; // Dùng cho tìm kiếm
    let currentPage = 1;
    const itemsPerPage = 7; // Số kho hàng mỗi trang
    let totalPages = 1;


    // --- Function to Create a Single Fake Warehouse ---
    function createFakeWarehouse(index) {
        const tenKhoHang = warehouseNames[getRandomInt(0, warehouseNames.length - 1)] + ` #${index + 1}`;
        const maKhoHang = `KHO${String(getRandomInt(100, 999))}${tenKhoHang.substring(0,2).toUpperCase().replace(' ', '')}`;
        const soLuongSanPham = getRandomInt(50, 5000);
        const nguongTonKho = Math.floor(soLuongSanPham * (getRandomInt(10, 30) / 100));

        return {
            id: index + 1,
            maKhoHang: maKhoHang,
            tenKhoHang: tenKhoHang,
            soLuongSanPham: soLuongSanPham,
            nguongTonKho: nguongTonKho,
            viTri: locations[getRandomInt(0, locations.length - 1)]
        };
    }

    // --- Function to Generate Initial Warehouse Data ---
    function generateInitialData(count = 25) { // Tạo nhiều kho để phân trang
        allWarehouses = [];
        for (let i = 0; i < count; i++) {
            allWarehouses.push(createFakeWarehouse(i));
        }
        filteredWarehouses = [...allWarehouses]; // Ban đầu, danh sách lọc là toàn bộ danh sách
    }

    // --- Function to Render Warehouse Table for the Current Page ---
    function displayCurrentPageWarehouses() {
        const tableBody = document.getElementById('account-table-body');
        if (!tableBody) {
            console.error("Lỗi: Không tìm thấy tbody với ID 'account-table-body'.");
            return;
        }
        tableBody.innerHTML = ''; 

        const noDataMessageRow = tableBody.insertRow();
        const noDataCell = noDataMessageRow.insertCell();
        noDataCell.colSpan = 6; // Số cột trong bảng
        noDataCell.textContent = 'Không có dữ liệu kho hàng.';
        noDataCell.style.textAlign = 'center';
        noDataMessageRow.style.display = 'none'; // Mặc định ẩn

        if (filteredWarehouses.length === 0) {
            noDataMessageRow.style.display = ''; // Hiện thông báo không có dữ liệu
            updatePaginationUI(); // Cập nhật phân trang (sẽ hiển thị 0 trang hoặc ẩn đi)
            return;
        }
        
        const startIndex = (currentPage - 1) * itemsPerPage;
        const endIndex = Math.min(startIndex + itemsPerPage, filteredWarehouses.length);
        const warehousesToDisplay = filteredWarehouses.slice(startIndex, endIndex);

        if (warehousesToDisplay.length === 0 && currentPage > 1) { // Nếu xóa hết item ở trang cuối cùng (không phải trang 1)
            currentPage--;
            displayCurrentPageWarehouses(); // Gọi lại để render trang trước đó
            return;
        }
        
        if (warehousesToDisplay.length > 0) {
             tableBody.innerHTML = ''; // Xóa thông báo "Không có dữ liệu" nếu có sản phẩm để hiển thị
        }


        warehousesToDisplay.forEach(kho => {
            const row = tableBody.insertRow();
            row.insertCell().textContent = kho.maKhoHang;
            row.insertCell().textContent = kho.tenKhoHang;
            row.insertCell().textContent = kho.soLuongSanPham.toLocaleString('vi-VN');
            row.insertCell().textContent = kho.nguongTonKho.toLocaleString('vi-VN');
            row.insertCell().textContent = kho.viTri;

            const actionsCell = row.insertCell();
            actionsCell.className = 'actions-cell';
            actionsCell.innerHTML = `
                <button class="icon-btn view-btn" title="Xem chi tiết" onclick="viewWarehouse('${kho.maKhoHang}')">
                    <i class="fas fa-eye"></i>
                </button>
                <button class="icon-btn edit-btn" title="Cập nhật" onclick="editWarehouse('${kho.maKhoHang}')">
                    <i class="fas fa-edit"></i>
                </button>
                <button class="icon-btn delete-btn" title="Xóa" onclick="deleteWarehouse('${kho.maKhoHang}')">
                    <i class="fas fa-trash-alt"></i>
                </button>
            `;
        });
        updatePaginationUI();
    }

    // --- Function to Update Pagination Controls UI ---
    function updatePaginationUI() {
        const prevPageBtn = document.getElementById('prevPageBtn');
        const nextPageBtn = document.getElementById('nextPageBtn');
        const pageInfoSpan = document.getElementById('pageInfo');
        const pageNumbersContainer = document.getElementById('pageNumbersContainer');
        const paginationControlsDiv = document.querySelector('.pagination-controls');

        if (!prevPageBtn || !nextPageBtn || !pageInfoSpan || !pageNumbersContainer || !paginationControlsDiv) {
            return; // Không làm gì nếu không tìm thấy các element phân trang
        }
        
        totalPages = Math.ceil(filteredWarehouses.length / itemsPerPage);
        
        if (totalPages <= 1 && filteredWarehouses.length <= itemsPerPage && filteredWarehouses.length > 0) {
             paginationControlsDiv.style.display = 'none'; // Ẩn nếu chỉ có 1 trang và có dữ liệu
        } else if (filteredWarehouses.length === 0) {
            paginationControlsDiv.style.display = 'none'; // Ẩn nếu không có dữ liệu
        }
        else {
            paginationControlsDiv.style.display = 'flex'; // Hiện nếu nhiều hơn 1 trang
        }

        if (totalPages === 0 && filteredWarehouses.length === 0) { // Đặc biệt xử lý khi không có item nào
            pageInfoSpan.textContent = 'Trang 0 / 0';
            prevPageBtn.disabled = true;
            nextPageBtn.disabled = true;
            pageNumbersContainer.innerHTML = '';
            return;
        }
        
        pageInfoSpan.textContent = `Trang ${currentPage} / ${totalPages === 0 ? 1 : totalPages}`;
        prevPageBtn.disabled = currentPage === 1;
        nextPageBtn.disabled = currentPage === totalPages || totalPages === 0;

        pageNumbersContainer.innerHTML = ''; 

        const maxPageButtons = 5;
        let startPage, endPage;

        if (totalPages <= maxPageButtons) {
            startPage = 1;
            endPage = totalPages;
        } else {
            const maxPagesBeforeCurrentPage = Math.floor(maxPageButtons / 2);
            const maxPagesAfterCurrentPage = Math.ceil(maxPageButtons / 2) - 1;
            if (currentPage <= maxPagesBeforeCurrentPage) {
                startPage = 1;
                endPage = maxPageButtons;
            } else if (currentPage + maxPagesAfterCurrentPage >= totalPages) {
                startPage = totalPages - maxPageButtons + 1;
                endPage = totalPages;
            } else {
                startPage = currentPage - maxPagesBeforeCurrentPage;
                endPage = currentPage + maxPagesAfterCurrentPage;
            }
        }
        
        if (startPage > 1) {
            pageNumbersContainer.appendChild(createPageButton(1));
            if (startPage > 2) {
                pageNumbersContainer.appendChild(createEllipsis());
            }
        }

        for (let i = startPage; i <= endPage; i++) {
             if (i > 0 && i <= totalPages) { // Chỉ tạo nút nếu i hợp lệ
                pageNumbersContainer.appendChild(createPageButton(i));
            }
        }

        if (endPage < totalPages) {
            if (endPage < totalPages - 1) {
                pageNumbersContainer.appendChild(createEllipsis());
            }
            pageNumbersContainer.appendChild(createPageButton(totalPages));
        }
    }

    function createPageButton(pageNumber) {
        const button = document.createElement('button');
        button.textContent = pageNumber;
        button.classList.add('btn', 'btn-pagination', 'btn-page-number');
        if (pageNumber === currentPage) {
            button.classList.add('active');
            button.disabled = true;
        }
        button.addEventListener('click', () => {
            currentPage = pageNumber;
            displayCurrentPageWarehouses();
        });
        return button;
    }
    
    function createEllipsis() {
        const span = document.createElement('span');
        span.textContent = '...';
        span.className = 'pagination-ellipsis';
        return span;
    }

    // --- Event Listeners for Prev/Next Buttons ---
    const prevBtn = document.getElementById('prevPageBtn');
    if (prevBtn) {
        prevBtn.addEventListener('click', () => {
            if (currentPage > 1) {
                currentPage--;
                displayCurrentPageWarehouses();
            }
        });
    }

    const nextBtn = document.getElementById('nextPageBtn');
    if (nextBtn) {
        nextBtn.addEventListener('click', () => {
            totalPages = Math.ceil(filteredWarehouses.length / itemsPerPage);
            if (currentPage < totalPages) {
                currentPage++;
                displayCurrentPageWarehouses();
            }
        });
    }

    // --- Action Functions ---
    window.viewWarehouse = function(maKhoHang) {
        const kho = allWarehouses.find(k => k.maKhoHang === maKhoHang); // Tìm trong allWarehouses
        if (kho) {
            alert(`Xem chi tiết Kho Hàng:
                Mã Kho: ${kho.maKhoHang}
                Tên Kho: ${kho.tenKhoHang}
                Số lượng SP: ${kho.soLuongSanPham.toLocaleString('vi-VN')}
                Ngưỡng tồn: ${kho.nguongTonKho.toLocaleString('vi-VN')}
                Vị trí: ${kho.viTri}`);
        }
    };

    window.editWarehouse = function(maKhoHang) {
        alert(`Chức năng sửa kho hàng (Mã Kho: ${maKhoHang}) chưa được cài đặt.`);
    };

    window.deleteWarehouse = function(maKhoHang) {
        if (confirm(`Bạn có chắc chắn muốn xóa kho hàng có Mã Kho: ${maKhoHang}?`)) {
            allWarehouses = allWarehouses.filter(k => k.maKhoHang !== maKhoHang);
            // Sau khi xóa, áp dụng lại bộ lọc hiện tại (nếu có) hoặc dùng toàn bộ danh sách
            const searchTerm = document.getElementById('searchInputTaiKhoan').value.toLowerCase().trim();
            if (searchTerm) {
                 applySearchFilter(); // Gọi hàm tìm kiếm để cập nhật filteredWarehouses
            } else {
                filteredWarehouses = [...allWarehouses];
            }

            totalPages = Math.ceil(filteredWarehouses.length / itemsPerPage);
            if (currentPage > totalPages && totalPages > 0) {
                currentPage = totalPages;
            } else if (totalPages === 0) {
                currentPage = 1;
            }
            displayCurrentPageWarehouses();
            alert(`Đã xóa kho hàng (Mã Kho: ${maKhoHang}) khỏi dữ liệu giả.`);
        }
    };

    // --- Search/Filter Functions ---
    function applySearchFilter() {
        const searchTerm = document.getElementById('searchInputTaiKhoan').value.toLowerCase().trim();
        // Hiện tại chưa có filter select phù hợp cho kho, nên chỉ tìm kiếm theo searchTerm
        
        filteredWarehouses = allWarehouses.filter(kho => {
            return kho.maKhoHang.toLowerCase().includes(searchTerm) ||
                   kho.tenKhoHang.toLowerCase().includes(searchTerm) ||
                   kho.viTri.toLowerCase().includes(searchTerm);
        });
        currentPage = 1; // Reset về trang đầu tiên sau khi tìm kiếm/lọc
        displayCurrentPageWarehouses();
    }

    window.searchAccount = function() { // Giữ tên hàm theo onclick trong HTML
        applySearchFilter();
    };

    window.resetSearchAccount = function() { // Giữ tên hàm theo onclick trong HTML
        document.getElementById('searchInputTaiKhoan').value = '';
        // document.getElementById('roleFilterTaiKhoan').value = ''; // Nếu có filter select phù hợp
        filteredWarehouses = [...allWarehouses]; // Reset lại danh sách lọc
        currentPage = 1;
        displayCurrentPageWarehouses();
    };

    // --- Initial Data Load ---
    const tableBodyCheck = document.getElementById('account-table-body');
    const paginationControlsCheck = document.querySelector('.pagination-controls');

    if (tableBodyCheck && paginationControlsCheck) {
        generateInitialData(25); // Tạo 25 kho hàng giả
        displayCurrentPageWarehouses(); // Hiển thị trang đầu tiên
        console.log("Kho hang fake data with pagination loaded!");
    } else {
        if(!tableBodyCheck) console.warn("Warehouse table body ('account-table-body') not found.");
        if(!paginationControlsCheck) console.warn("Pagination controls not found. Pagination will not be initialized.");
    }
});