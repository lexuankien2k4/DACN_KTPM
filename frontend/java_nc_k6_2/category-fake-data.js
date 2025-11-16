document.addEventListener('DOMContentLoaded', function () {
    // --- Helper Functions ---
    function getRandomInt(min, max) {
        return Math.floor(Math.random() * (max - min + 1)) + min;
    }

    function getRandomDate(start = new Date(2023, 0, 1), end = new Date()) {
        const date = new Date(start.getTime() + Math.random() * (end.getTime() - start.getTime()));
        const day = String(date.getDate()).padStart(2, '0');
        const month = String(date.getMonth() + 1).padStart(2, '0');
        const year = date.getFullYear();
        return `${day}/${month}/${year}`;
    }

    // --- Sample Data ---
    const categoryNames = [
        "Rau Hữu Cơ", "Trái Cây Sạch", "Thịt Bò Nhập Khẩu", "Hải Sản Tươi Sống", "Thực Phẩm Đóng Hộp",
        "Đồ Uống Các Loại", "Sản Phẩm Từ Sữa", "Bánh Kẹo Ngon", "Gia Vị Nhà Bếp", "Đồ Khô Các Loại",
        "Thực Phẩm Chay", "Đặc Sản Vùng Miền", "Nông Sản Sạch", "Thực Phẩm Cho Bé", "Đồ Dùng Gia Đình"
    ];
    const statuses = ["Hoạt động", "Không hoạt động"];

    // --- Pagination Variables ---
    let allCategories = [];
    let currentPage = 1;
    const itemsPerPage = 10; // Hiển thị 10 danh mục mỗi trang
    let totalPages = 1;

    // --- Function to Create a Single Fake Category ---
    function createFakeCategory(index) { // index là STT tuyệt đối
        const tenDanhMuc = categoryNames[getRandomInt(0, categoryNames.length - 1)] + ' ' + (index + 1);
        const maDanhMuc = `DM${String(getRandomInt(100, 999))}${tenDanhMuc.substring(0,2).toUpperCase().replace(/\s+/g, '')}`;
        const ngayTao = getRandomDate();
        const ngayCapNhat = getRandomDate(new Date(ngayTao.split('/').reverse().join('-')), new Date());

        return {
            stt: index + 1,
            maDanhMuc: maDanhMuc,
            tenDanhMuc: tenDanhMuc,
            ngayTao: ngayTao,
            ngayCapNhat: ngayCapNhat,
            trangThai: statuses[getRandomInt(0, statuses.length - 1)]
        };
    }

    // --- Function to Generate All Fake Categories ---
    function generateAllFakeCategories(count = 35) {
        allCategories = [];
        for (let i = 0; i < count; i++) {
            allCategories.push(createFakeCategory(i));
        }
        totalPages = Math.ceil(allCategories.length / itemsPerPage);
    }

    // --- Function to Render Categories for the Current Page ---
    function renderCategoriesForPage(page) {
        currentPage = page;
        const tableBody = document.getElementById('categoriesTableBody');
        const noCategoriesMessage = document.getElementById('no-categories-message');

        if (!tableBody || !noCategoriesMessage) {
            console.error("Lỗi: Không tìm thấy 'categoriesTableBody' hoặc 'no-categories-message'.");
            return;
        }
        tableBody.innerHTML = ''; 

        if (allCategories.length === 0) {
            noCategoriesMessage.style.display = 'block';
            if (tableBody.parentElement) tableBody.parentElement.style.display = 'none'; // Ẩn table nếu không có dữ liệu
            updatePaginationControls(); 
            return;
        }

        noCategoriesMessage.style.display = 'none';
        if (tableBody.parentElement) tableBody.parentElement.style.display = ''; // Hiện table

        const startIndex = (page - 1) * itemsPerPage;
        const endIndex = startIndex + itemsPerPage;
        const paginatedItems = allCategories.slice(startIndex, endIndex);

        paginatedItems.forEach((category, indexInPage) => {
            const row = tableBody.insertRow();

            row.insertCell().textContent = startIndex + indexInPage + 1; 
            row.insertCell().textContent = category.maDanhMuc;
            row.insertCell().textContent = category.tenDanhMuc;

            const statusCell = row.insertCell();
            const statusBadge = document.createElement('span');
            statusBadge.className = 'status-badge';
            statusBadge.classList.add(category.trangThai === "Hoạt động" ? 'status-active' : 'status-inactive');
            statusBadge.textContent = category.trangThai;
            statusCell.appendChild(statusBadge);

            row.insertCell().textContent = category.ngayTao;
            row.insertCell().textContent = category.ngayCapNhat;

            // === PHẦN CẬP NHẬT NÚT HÀNH ĐỘNG CHO DANH MỤC ===
            const actionCell = row.insertCell();
            actionCell.className = 'actions-cell'; // Giữ class này nếu bạn có style riêng cho ô actions
            actionCell.innerHTML = `
                <button class="icon-btn view-btn" title="Xem chi tiết" onclick="viewCategory('${category.maDanhMuc}')">
                    <i class="fas fa-eye"></i>
                </button>
                <button class="icon-btn edit-btn" title="Cập nhật" onclick="editCategory('${category.maDanhMuc}')">
                    <i class="fas fa-edit"></i>
                </button>
                <button class="icon-btn delete-btn" title="Xóa" onclick="deleteCategory('${category.maDanhMuc}')">
                    <i class="fas fa-trash-alt"></i>
                </button>
            `;
            // === KẾT THÚC PHẦN CẬP NHẬT ===
        });
        updatePaginationControls();
    }

    // --- Function to Update Pagination Controls ---
    function updatePaginationControls() {
        const prevPageBtn = document.getElementById('prevPageBtn');
        const nextPageBtn = document.getElementById('nextPageBtn');
        const pageInfoSpan = document.getElementById('pageInfo');
        const pageNumbersContainer = document.getElementById('pageNumbersContainer');
        const paginationControlsDiv = document.querySelector('.pagination-controls');


        if (!prevPageBtn || !nextPageBtn || !pageInfoSpan || !pageNumbersContainer || !paginationControlsDiv) {
            // Không cần báo lỗi nếu các control không có trên trang
            return;
        }
        
        totalPages = Math.ceil(allCategories.length / itemsPerPage);
        
        if (totalPages <= 1 && allCategories.length <= itemsPerPage) { // Ẩn nếu chỉ có 1 trang hoặc ít hơn itemsPerPage
            paginationControlsDiv.style.display = 'none';
            return;
        }
        paginationControlsDiv.style.display = 'flex';


        if (totalPages === 0) totalPages = 1; 

        pageInfoSpan.textContent = `Trang ${currentPage} / ${totalPages}`;
        prevPageBtn.disabled = currentPage === 1;
        nextPageBtn.disabled = currentPage === totalPages || allCategories.length === 0;

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
        if (totalPages === 1 && allCategories.length === 0) { 
             pageNumbersContainer.innerHTML = '';
        } else {
            if (startPage > 1) {
                pageNumbersContainer.appendChild(createPageButton(1));
                if (startPage > 2) {
                    pageNumbersContainer.appendChild(createEllipsis());
                }
            }

            for (let i = startPage; i <= endPage; i++) {
                if (i > 0) { // Đảm bảo không tạo nút cho trang 0 nếu totalPages = 0
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
    }

    function createPageButton(pageNumber) {
        const button = document.createElement('button');
        button.textContent = pageNumber;
        // Sử dụng class chung cho nút phân trang số, ví dụ: 'btn-page-number'
        button.classList.add('btn', 'btn-pagination', 'btn-page-number'); 
        if (pageNumber === currentPage) {
            button.classList.add('active');
            button.disabled = true;
        }
        button.addEventListener('click', () => renderCategoriesForPage(pageNumber));
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
                renderCategoriesForPage(currentPage - 1);
            }
        });
    }

    const nextBtn = document.getElementById('nextPageBtn');
    if (nextBtn) {
        nextBtn.addEventListener('click', () => {
            totalPages = Math.ceil(allCategories.length / itemsPerPage); // Cập nhật lại totalPages
            if (currentPage < totalPages) {
                renderCategoriesForPage(currentPage + 1);
            }
        });
    }
    

    // --- Dummy functions for actions ---
    // THÊM HÀM window.viewCategory
    window.viewCategory = function(categoryId) {
        const category = allCategories.find(cat => cat.maDanhMuc === categoryId);
        if (category) {
            alert(`Xem chi tiết Danh mục:
                Mã DM: ${category.maDanhMuc}
                Tên DM: ${category.tenDanhMuc}
                Ngày tạo: ${category.ngayTao}
                Ngày cập nhật: ${category.ngayCapNhat}
                Trạng thái: ${category.trangThai}`);
        } else {
            alert("Không tìm thấy thông tin danh mục!");
        }
    };

    window.editCategory = function(categoryId) {
        alert(`Chức năng sửa danh mục: ${categoryId} (chưa được triển khai)`);
    };

    window.deleteCategory = function(categoryId) {
        if (confirm(`Bạn có chắc chắn muốn xóa danh mục ${categoryId} không?`)) {
            const initialLength = allCategories.length;
            allCategories = allCategories.filter(cat => cat.maDanhMuc !== categoryId);
            
            if (allCategories.length < initialLength) {
                 alert(`Danh mục ${categoryId} đã được "xóa" (trong dữ liệu giả).`);
                // Tính toán lại tổng số trang
                totalPages = Math.ceil(allCategories.length / itemsPerPage);
                // Nếu trang hiện tại lớn hơn tổng số trang mới và không phải là trang 1, thì lùi về trang cuối
                if (currentPage > totalPages && totalPages > 0) {
                    currentPage = totalPages;
                } else if (totalPages === 0) { // Nếu không còn danh mục nào
                    currentPage = 1; // Đặt lại về trang 1
                }
                renderCategoriesForPage(currentPage); // Render lại trang hiện tại hoặc trang cuối
            } else {
                alert(`Không tìm thấy danh mục ${categoryId} để xóa.`);
            }
        }
    };

    // --- Initial data generation and rendering ---
    // Đảm bảo các DOM elements đã sẵn sàng
    const tableBodyCheck = document.getElementById('categoriesTableBody');
    const paginationControlsCheck = document.querySelector('.pagination-controls');

    if (tableBodyCheck && paginationControlsCheck) {
        generateAllFakeCategories(32); 
        if (allCategories.length > 0) {
            renderCategoriesForPage(1); 
        } else {
            renderCategoriesForPage(1); // Vẫn gọi để hiển thị thông báo "không có danh mục"
        }
        console.log("Category fake data with pagination loaded!");
    } else {
        if (!tableBodyCheck) console.warn("Category table body ('categoriesTableBody') not found. Pagination script might not run as expected for categories.");
        if (!paginationControlsCheck) console.warn("Pagination controls (.pagination-controls) not found. Pagination script might not run as expected for categories.");
    }
});