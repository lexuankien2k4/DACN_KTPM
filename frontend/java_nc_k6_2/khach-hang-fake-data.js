document.addEventListener('DOMContentLoaded', function () {
    // --- Helper Functions ---
    function getRandomInt(min, max) {
        return Math.floor(Math.random() * (max - min + 1)) + min;
    }
    function getRandomElement(arr) {
        return arr[getRandomInt(0, arr.length - 1)];
    }

    // --- Sample Data for Customers ---
    const ho = ["Nguyễn", "Trần", "Lê", "Phạm", "Hoàng", "Huỳnh", "Võ", "Đặng", "Bùi", "Đỗ", "Hồ", "Ngô", "Dương", "Lý"];
    const tenDemNam = ["Văn", "Hữu", "Đức", "Minh", "Gia", "Tuấn", "Đình"];
    const tenDemNu = ["Thị", "Ngọc", "Mỹ", "Phương", "Thùy", "Khánh", "Diệu"];
    const tenChinh = ["An", "Bình", "Cường", "Dũng", "Hương", "Lan", "Minh", "Nam", "Oanh", "Phúc", "Quân", "Sơn", "Thảo", "Trang", "Uyên", "Vân", "Xuân", "Yến"];
    const gioiTinhOpts = ["Nam", "Nữ", "Khác"];
    const nhomKhachOpts = ["VIP", "Thân thiết", "Mới", "Tiềm năng", "Doanh nghiệp"];
    const trangThaiKhachOpts = ["Hoạt động", "Không hoạt động", "Đã khóa"];
    const duong = ["Lê Lợi", "Trần Hưng Đạo", "Nguyễn Huệ", "Hai Bà Trưng", "Điện Biên Phủ", "Phan Đình Phùng", "Cách Mạng Tháng Tám"];
    const quan = ["Quận 1", "Quận 3", "Quận Tân Bình", "Quận Bình Thạnh", "Quận Gò Vấp", "Quận Hai Bà Trưng", "Quận Đống Đa"];
    const thanhPho = ["TP. Hồ Chí Minh", "Hà Nội", "Đà Nẵng", "Hải Phòng", "Cần Thơ"];

    // --- Pagination Variables ---
    let allCustomers = [];
    let filteredCustomers = [];
    let currentPage = 1;
    const itemsPerPage = 10; // Số khách hàng mỗi trang
    let totalPages = 1;

    // --- Function to Create a Single Fake Customer ---
    function createFakeCustomer(index) {
        const gt = getRandomElement(gioiTinhOpts);
        let hoKhach = getRandomElement(ho);
        let tenDemKhach = gt === "Nam" ? getRandomElement(tenDemNam) : getRandomElement(tenDemNu);
        let tenChinhKhach = getRandomElement(tenChinh);
        const tenKhachHang = `${hoKhach} ${tenDemKhach} ${tenChinhKhach}`;
        const emailName = `${tenChinhKhach.toLowerCase()}.${hoKhach.charAt(0).toLowerCase()}${getRandomInt(1,99)}`;

        return {
            id: index + 1,
            maKhachHang: `KH${String(getRandomInt(10000, 99999))}`,
            tenKhachHang: tenKhachHang,
            soDienThoai: `09${getRandomInt(0,8)}${getRandomInt(1000000,9999999)}`,
            email: `${emailName}@example.com`,
            gioiTinh: gt,
            nhomKhach: getRandomElement(nhomKhachOpts),
            diaChi: `${getRandomInt(1, 500)} ${getRandomElement(duong)}, ${getRandomElement(quan)}, ${getRandomElement(thanhPho)}`,
            trangThai: getRandomElement(trangThaiKhachOpts)
        };
    }

    // --- Function to Generate Initial Customer Data ---
    function generateInitialData(count = 55) { // Tạo nhiều khách hàng để phân trang
        allCustomers = [];
        for (let i = 0; i < count; i++) {
            allCustomers.push(createFakeCustomer(i));
        }
        populateFilters(); // Gọi sau khi có allCustomers
        filteredCustomers = [...allCustomers];
    }
    
    // --- Populate Filter Options ---
    function populateFilters() {
        const nhomKhachSet = new Set(allCustomers.map(c => c.nhomKhach));
        const nhomKhachFilterSelect = document.getElementById('nhomKhachFilter');
        if (nhomKhachFilterSelect) {
            // Giữ option "Tất cả nhóm khách"
            Array.from(nhomKhachFilterSelect.options).forEach((option, index) => {
                if (index > 0) option.remove();
            });
            nhomKhachSet.forEach(nhom => {
                const option = document.createElement('option');
                option.value = nhom;
                option.textContent = nhom;
                nhomKhachFilterSelect.appendChild(option);
            });
        }
    }


    // --- Function to Render Customer Table for the Current Page ---
    function displayCurrentPageCustomers() {
        const tableBody = document.getElementById('car-table-body'); // Sử dụng ID từ HTML của bạn
        if (!tableBody) {
            console.error("Lỗi: Không tìm thấy tbody với ID 'car-table-body'.");
            return;
        }
        tableBody.innerHTML = '';

        const noDataMessageRow = tableBody.insertRow();
        const noDataCell = noDataMessageRow.insertCell();
        noDataCell.colSpan = 9; // Số cột trong bảng khách hàng
        noDataCell.textContent = 'Không có khách hàng nào phù hợp.';
        noDataCell.style.textAlign = 'center';
        noDataMessageRow.style.display = 'none';

        if (filteredCustomers.length === 0) {
            noDataMessageRow.style.display = '';
            updatePaginationUI();
            return;
        }
        
        const startIndex = (currentPage - 1) * itemsPerPage;
        const endIndex = Math.min(startIndex + itemsPerPage, filteredCustomers.length);
        const customersToDisplay = filteredCustomers.slice(startIndex, endIndex);

        if (customersToDisplay.length === 0 && currentPage > 1) {
            currentPage--;
            displayCurrentPageCustomers();
            return;
        }
        
        if (customersToDisplay.length > 0) {
             tableBody.innerHTML = ''; 
        }


        customersToDisplay.forEach(kh => {
            const row = tableBody.insertRow();
            row.insertCell().textContent = kh.maKhachHang;
            row.insertCell().textContent = kh.tenKhachHang;
            row.insertCell().textContent = kh.soDienThoai;
            row.insertCell().textContent = kh.email;
            row.insertCell().textContent = kh.gioiTinh;
            row.insertCell().textContent = kh.nhomKhach;
            row.insertCell().textContent = kh.diaChi;
            
            const statusCell = row.insertCell();
            const statusBadge = document.createElement('span');
            statusBadge.className = 'status-badge'; // Cần CSS cho class này
            statusBadge.textContent = kh.trangThai;
            if (kh.trangThai === "Hoạt động") statusBadge.classList.add('status-active');
            else if (kh.trangThai === "Không hoạt động") statusBadge.classList.add('status-inactive');
            else if (kh.trangThai === "Đã khóa") statusBadge.classList.add('status-locked'); // Ví dụ class mới
            statusCell.appendChild(statusBadge);


            const actionsCell = row.insertCell();
            actionsCell.className = 'actions-cell';
            actionsCell.innerHTML = `
                <button class="icon-btn view-btn" title="Xem chi tiết" onclick="viewCustomer('${kh.maKhachHang}')">
                    <i class="fas fa-eye"></i>
                </button>
                <button class="icon-btn edit-btn" title="Cập nhật" onclick="editCustomer('${kh.maKhachHang}')">
                    <i class="fas fa-edit"></i>
                </button>
                <button class="icon-btn delete-btn" title="Xóa" onclick="deleteCustomer('${kh.maKhachHang}')">
                    <i class="fas fa-trash-alt"></i>
                </button>
            `;
        });
        updatePaginationUI();
    }

    // --- Function to Update Pagination Controls UI ---
    function updatePaginationUI() {
        const prevPageBtn = document.getElementById('prevPageBtnKhachHang');
        const nextPageBtn = document.getElementById('nextPageBtnKhachHang');
        const pageInfoSpan = document.getElementById('pageInfoKhachHang');
        const pageNumbersContainer = document.getElementById('pageNumbersContainerKhachHang');
        const paginationControlsDiv = document.querySelector('#khach-hang-html .pagination-controls') || document.querySelector('.pagination-controls'); // Lấy đúng div

        if (!prevPageBtn || !nextPageBtn || !pageInfoSpan || !pageNumbersContainer || !paginationControlsDiv) {
             return; 
        }
        
        totalPages = Math.ceil(filteredCustomers.length / itemsPerPage);

        if (totalPages <= 1 && filteredCustomers.length <= itemsPerPage && filteredCustomers.length > 0) {
             paginationControlsDiv.style.display = 'none';
        } else if (filteredCustomers.length === 0) {
             paginationControlsDiv.style.display = 'none';
        }
        else {
            paginationControlsDiv.style.display = 'flex';
        }

        if (totalPages === 0 && filteredCustomers.length === 0) {
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
            if (i > 0 && i <= totalPages) {
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
            displayCurrentPageCustomers();
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
    const prevBtnKH = document.getElementById('prevPageBtnKhachHang');
    if (prevBtnKH) {
        prevBtnKH.addEventListener('click', () => {
            if (currentPage > 1) {
                currentPage--;
                displayCurrentPageCustomers();
            }
        });
    }
    const nextBtnKH = document.getElementById('nextPageBtnKhachHang');
    if (nextBtnKH) {
        nextBtnKH.addEventListener('click', () => {
            totalPages = Math.ceil(filteredCustomers.length / itemsPerPage);
            if (currentPage < totalPages) {
                currentPage++;
                displayCurrentPageCustomers();
            }
        });
    }

    // --- Action Functions ---
    window.viewCustomer = function(maKhachHang) {
        const kh = allCustomers.find(c => c.maKhachHang === maKhachHang);
        if (kh) {
            alert(`Xem chi tiết Khách Hàng:
                Mã KH: ${kh.maKhachHang}
                Tên: ${kh.tenKhachHang}
                SĐT: ${kh.soDienThoai}
                Email: ${kh.email}
                Giới tính: ${kh.gioiTinh}
                Nhóm: ${kh.nhomKhach}
                Địa chỉ: ${kh.diaChi}
                Trạng thái: ${kh.trangThai}`);
        }
    };
    window.editCustomer = function(maKhachHang) {
        alert(`Chức năng sửa khách hàng (Mã KH: ${maKhachHang}) chưa được cài đặt.`);
    };
    window.deleteCustomer = function(maKhachHang) {
        if (confirm(`Bạn có chắc chắn muốn xóa khách hàng có Mã KH: ${maKhachHang}?`)) {
            allCustomers = allCustomers.filter(c => c.maKhachHang !== maKhachHang);
            applyKhachHangFilters(); // Áp dụng lại filter và search để cập nhật filteredCustomers và render
            alert(`Đã xóa khách hàng (Mã KH: ${maKhachHang}) khỏi dữ liệu giả.`);
        }
    };

    // --- Search and Filter Logic ---
    window.applyKhachHangFilters = function() {
        const searchTerm = document.getElementById('searchKhachHangInput')?.value.toLowerCase().trim() || "";
        const nhomKhach = document.getElementById('nhomKhachFilter')?.value || "";
        const trangThai = document.getElementById('trangThaiKhachHangFilter')?.value || "";

        filteredCustomers = allCustomers.filter(kh => {
            const matchesSearch = (
                kh.maKhachHang.toLowerCase().includes(searchTerm) ||
                kh.tenKhachHang.toLowerCase().includes(searchTerm) ||
                kh.soDienThoai.includes(searchTerm) ||
                kh.email.toLowerCase().includes(searchTerm)
            );
            const matchesNhomKhach = nhomKhach ? kh.nhomKhach === nhomKhach : true;
            const matchesTrangThai = trangThai ? kh.trangThai === trangThai : true;
            
            return matchesSearch && matchesNhomKhach && matchesTrangThai;
        });
        currentPage = 1;
        displayCurrentPageCustomers();
    }

    window.resetKhachHangFilters = function() {
        if(document.getElementById('searchKhachHangInput')) document.getElementById('searchKhachHangInput').value = '';
        if(document.getElementById('nhomKhachFilter')) document.getElementById('nhomKhachFilter').value = '';
        if(document.getElementById('trangThaiKhachHangFilter')) document.getElementById('trangThaiKhachHangFilter').value = '';
        
        filteredCustomers = [...allCustomers];
        currentPage = 1;
        displayCurrentPageCustomers();
    }
    
    // Gán sự kiện cho các input/select nếu bạn muốn tự động lọc khi thay đổi
     document.getElementById('searchKhachHangInput')?.addEventListener('input', applyKhachHangFilters);
     document.getElementById('nhomKhachFilter')?.addEventListener('change', applyKhachHangFilters);
     document.getElementById('trangThaiKhachHangFilter')?.addEventListener('change', applyKhachHangFilters);


    // --- Initial Data Load ---
    const tableBodyKH = document.getElementById('car-table-body');
    const paginationControlsKH = document.querySelector('#khach-hang-html .pagination-controls') || document.querySelector('.pagination-controls');


    if (tableBodyKH && paginationControlsKH) { // Chỉ khởi tạo nếu các thành phần chính tồn tại
        generateInitialData(55); 
        displayCurrentPageCustomers();
        console.log("Khach hang fake data with pagination loaded!");
    } else {
        if(!tableBodyKH) console.warn("Customer table body ('car-table-body') not found.");
        if(!paginationControlsKH) console.warn("Pagination controls for customers not found.");
    }
});