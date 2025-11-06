
    const initialSampleEmployeesData = [
        { MaNhanVien: 1, HoNhanVien: 'Nguyễn', TenNhanVien: 'Văn An', ChucVu: 'Quản trị viên cao cấp', SoDienThoaiCaNhan: '0901234567', EmailCongViec: 'an.nguyen@example.com', TrangThaiLamViec: 'Đang làm việc', DiaChiThuongTru: '123 Đường ABC, Quận 1, TP. HCM', TaiKhoan: { MaTaiKhoan: 1, TenDangNhap: 'admin_an' }},
        { MaNhanVien: 2, HoNhanVien: 'Trần', TenNhanVien: 'Thị Bình', ChucVu: 'Quản lý kho', SoDienThoaiCaNhan: '0912345678', EmailCongViec: 'binh.tran@example.com', TrangThaiLamViec: 'Đang làm việc', DiaChiThuongTru: '456 Đường XYZ, Quận 2, TP. HCM', TaiKhoan: { MaTaiKhoan: 2, TenDangNhap: 'manager_binh' }},
        { MaNhanVien: 3, HoNhanVien: 'Lê', TenNhanVien: 'Hoàng Dũng', ChucVu: 'Nhân viên bán hàng', SoDienThoaiCaNhan: '0987654321', EmailCongViec: 'dung.le@example.com', TrangThaiLamViec: 'Đang làm việc', DiaChiThuongTru: '789 Đường KLM, Quận 3, TP. HCM', TaiKhoan: { MaTaiKhoan: 3, TenDangNhap: 'staff_dung' }},
        { MaNhanVien: 4, HoNhanVien: 'Phạm', TenNhanVien: 'Minh Châu', ChucVu: 'Nhân viên bán hàng', SoDienThoaiCaNhan: '0934567890', EmailCongViec: 'chau.pham@example.com', TrangThaiLamViec: 'Đang làm việc', DiaChiThuongTru: '321 Đường UVW, Quận 4, TP. HCM', TaiKhoan: { MaTaiKhoan: 4, TenDangNhap: 'staff_chau' }},
        { MaNhanVien: 5, HoNhanVien: 'Võ', TenNhanVien: 'Thị Mai', ChucVu: 'Kế toán', SoDienThoaiCaNhan: '0911223344', EmailCongViec: 'mai.vo@example.com', TrangThaiLamViec: 'Nghỉ phép', DiaChiThuongTru: '101 Đường GHI, Quận 5, TP. HCM', TaiKhoan: { MaTaiKhoan: 5, TenDangNhap: 'accountant_mai' }},
        // Thêm nhiều nhân viên giả để đủ phân trang (ít nhất 10-20 người)
        { MaNhanVien: 6, HoNhanVien: 'Đỗ', TenNhanVien: 'Gia Huy', ChucVu: 'Quản lý kho', SoDienThoaiCaNhan: '0902112233', EmailCongViec: 'huy.do@example.com', TrangThaiLamViec: 'Đang làm việc', DiaChiThuongTru: '111 Đường JQK, Quận 6, TP. HCM', TaiKhoan: { MaTaiKhoan: 6, TenDangNhap: 'manager_huy' }},
        { MaNhanVien: 7, HoNhanVien: 'Huỳnh', TenNhanVien: 'Tấn Phát', ChucVu: 'Nhân viên bán hàng', SoDienThoaiCaNhan: '0903223344', EmailCongViec: 'phat.huynh@example.com', TrangThaiLamViec: 'Nghỉ phép', DiaChiThuongTru: '222 Đường LMN, Quận 7, TP. HCM', TaiKhoan: { MaTaiKhoan: 7, TenDangNhap: 'staff_phat' }},
        { MaNhanVien: 8, HoNhanVien: 'Ngô', TenNhanVien: 'Bảo Thy', ChucVu: 'Kế toán', SoDienThoaiCaNhan: '0904334455', EmailCongViec: 'thy.ngo@example.com', TrangThaiLamViec: 'Đang làm việc', DiaChiThuongTru: '333 Đường OPQ, Quận 8, TP. HCM', TaiKhoan: { MaTaiKhoan: 8, TenDangNhap: 'accountant_thy' }},
        { MaNhanVien: 9, HoNhanVien: 'Dương', TenNhanVien: 'Quốc Hùng', ChucVu: 'Quản trị viên cao cấp', SoDienThoaiCaNhan: '0905445566', EmailCongViec: 'hung.duong@example.com', TrangThaiLamViec: 'Đang làm việc', DiaChiThuongTru: '444 Đường RST, Quận 9, TP. HCM', TaiKhoan: { MaTaiKhoan: 9, TenDangNhap: 'admin_hung' }},
        { MaNhanVien: 10, HoNhanVien: 'Lý', TenNhanVien: 'Mỹ Tâm', ChucVu: 'Nhân viên bán hàng', SoDienThoaiCaNhan: '0906556677', EmailCongViec: 'tam.ly@example.com', TrangThaiLamViec: 'Đang làm việc', DiaChiThuongTru: '555 Đường UVW, Quận 10, TP. HCM', TaiKhoan: { MaTaiKhoan: 10, TenDangNhap: 'staff_tam' }},
        { MaNhanVien: 11, HoNhanVien: 'Vũ', TenNhanVien: 'Đức Thịnh', ChucVu: 'Quản lý kho', SoDienThoaiCaNhan: '0907667788', EmailCongViec: 'thinh.vu@example.com', TrangThaiLamViec: 'Đang làm việc', DiaChiThuongTru: '666 Đường XYZ, Quận 11, TP. HCM', TaiKhoan: { MaTaiKhoan: 11, TenDangNhap: 'manager_thinh' }},
        { MaNhanVien: 12, HoNhanVien: 'Hoàng', TenNhanVien: 'Minh Nguyệt', ChucVu: 'Kế toán', SoDienThoaiCaNhan: '0908778899', EmailCongViec: 'nguyet.hoang@example.com', TrangThaiLamViec: 'Nghỉ phép', DiaChiThuongTru: '777 Đường 123, Quận 12, TP. HCM', TaiKhoan: { MaTaiKhoan: 12, TenDangNhap: 'accountant_nguyet' }},
        { MaNhanVien: 13, HoNhanVien: 'Phan', TenNhanVien: 'Thanh Sang', ChucVu: 'Nhân viên bán hàng', SoDienThoaiCaNhan: '0909889900', EmailCongViec: 'sang.phan@example.com', TrangThaiLamViec: 'Đang làm việc', DiaChiThuongTru: '888 Đường 456, Quận Bình Tân, TP. HCM', TaiKhoan: { MaTaiKhoan: 13, TenDangNhap: 'staff_sang' }},
        { MaNhanVien: 14, HoNhanVien: 'Bùi', TenNhanVien: 'Ngọc Mai', ChucVu: 'Quản trị viên cao cấp', SoDienThoaiCaNhan: '0912990011', EmailCongViec: 'mai.bui@example.com', TrangThaiLamViec: 'Đang làm việc', DiaChiThuongTru: '999 Đường 789, Quận Tân Phú, TP. HCM', TaiKhoan: { MaTaiKhoan: 14, TenDangNhap: 'admin_mai' }},
        { MaNhanVien: 15, HoNhanVien: 'Nguyễn', TenNhanVien: 'Anh Khoa', ChucVu: 'Quản lý kho', SoDienThoaiCaNhan: '0913001122', EmailCongViec: 'khoa.nguyen@example.com', TrangThaiLamViec: 'Đang làm việc', DiaChiThuongTru: '100 Đường ABC, Quận Gò Vấp, TP. HCM', TaiKhoan: { MaTaiKhoan: 15, TenDangNhap: 'manager_khoa' }},
        { MaNhanVien: 16, HoNhanVien: 'Trần', TenNhanVien: 'Phương Vy', ChucVu: 'Nhân viên bán hàng', SoDienThoaiCaNhan: '0914112233', EmailCongViec: 'vy.tran@example.com', TrangThaiLamViec: 'Đang làm việc', DiaChiThuongTru: '200 Đường DEF, Quận Phú Nhuận, TP. HCM', TaiKhoan: { MaTaiKhoan: 16, TenDangNhap: 'staff_vy' }},
        { MaNhanVien: 17, HoNhanVien: 'Lê', TenNhanVien: 'Minh Quân', ChucVu: 'Kế toán', SoDienThoaiCaNhan: '0915223344', EmailCongViec: 'quan.le@example.com', TrangThaiLamViec: 'Đang làm việc', DiaChiThuongTru: '300 Đường GHI, Quận Bình Thạnh, TP. HCM', TaiKhoan: { MaTaiKhoan: 17, TenDangNhap: 'accountant_quan' }}
    ];

    let allEmployees = [...initialSampleEmployeesData];
    let filteredEmployees = [...allEmployees];
    let currentPage = 1;
    const itemsPerPage = 8; // Hiển thị 8 hàng mỗi trang
    let totalPages = 1;

    function displayCurrentPageEmployees() {
        const tableBody = document.getElementById('car-table-body');
        if (!tableBody) {
            console.error("Lỗi: Không tìm thấy tbody với ID 'car-table-body'.");
            return;
        }
        tableBody.innerHTML = '';

        const noDataMessageRow = tableBody.insertRow();
        const noDataCell = noDataMessageRow.insertCell();
        noDataCell.colSpan = 9; // Số cột trong bảng nhân viên
        noDataCell.textContent = 'Không có nhân viên nào phù hợp.';
        noDataCell.style.textAlign = 'center';
        noDataMessageRow.style.display = 'none'; // Mặc định ẩn

        if (filteredEmployees.length === 0) {
            noDataMessageRow.style.display = '';
            updatePaginationUI_NhanVien();
            return;
        }

        const startIndex = (currentPage - 1) * itemsPerPage;
        const endIndex = Math.min(startIndex + itemsPerPage, filteredEmployees.length);
        const employeesToDisplay = filteredEmployees.slice(startIndex, endIndex);
        
        if (employeesToDisplay.length === 0 && currentPage > 1) {
            currentPage--;
            displayCurrentPageEmployees();
            return;
        }

        if (employeesToDisplay.length > 0) {
             tableBody.innerHTML = ''; 
        }

        employeesToDisplay.forEach(emp => {
            const row = tableBody.insertRow();
            row.insertCell().textContent = emp.MaNhanVien;
            row.insertCell().textContent = emp.TaiKhoan ? emp.TaiKhoan.MaTaiKhoan : 'N/A';
            row.insertCell().textContent = `${emp.HoNhanVien} ${emp.TenNhanVien}`;
            row.insertCell().textContent = emp.SoDienThoaiCaNhan;
            row.insertCell().textContent = emp.EmailCongViec;
            row.insertCell().textContent = emp.DiaChiThuongTru;
            row.insertCell().textContent = emp.ChucVu;

            // Ô Trạng thái với badge (ví dụ)
            const statusCell = row.insertCell();
            const statusBadge = document.createElement('span');
            statusBadge.className = 'status-badge'; // Cần CSS cho class này
            statusBadge.textContent = emp.TrangThaiLamViec;
            if (emp.TrangThaiLamViec === "Đang làm việc") statusBadge.classList.add('status-active');
            else if (emp.TrangThaiLamViec === "Nghỉ phép") statusBadge.classList.add('status-on-leave'); // Ví dụ class mới
            else statusBadge.classList.add('status-inactive'); // Cho các trạng thái khác
            statusCell.appendChild(statusBadge);

            const actionsCell = row.insertCell();
            actionsCell.innerHTML = `
                <button class="icon-btn view-btn" title="Xem chi tiết" onclick="viewEmployeeDetails(${emp.MaNhanVien})"><i class="fas fa-eye"></i></button>
                <button class="icon-btn edit-btn" title="Cập nhật" onclick="editEmployeeDetails(${emp.MaNhanVien})"><i class="fas fa-edit"></i></button>
                <button class="icon-btn delete-btn" title="Xóa" onclick="deleteEmployeeConfirm(${emp.MaNhanVien})"><i class="fas fa-trash-alt"></i></button>
            `;
        });
        updatePaginationUI_NhanVien();
    }

    function updatePaginationUI_NhanVien() {
        const prevPageBtn = document.getElementById('prevPageBtnNhanVien');
        const nextPageBtn = document.getElementById('nextPageBtnNhanVien');
        const pageInfoSpan = document.getElementById('pageInfoNhanVien');
        const pageNumbersContainer = document.getElementById('pageNumbersContainerNhanVien');
        const paginationControlsDiv = document.querySelector('#nhan-vien-html .pagination-controls') || document.querySelector('.pagination-controls'); // Selector cụ thể hơn nếu cần


        if (!prevPageBtn || !nextPageBtn || !pageInfoSpan || !pageNumbersContainer || !paginationControlsDiv) {
            return; 
        }
        
        totalPages = Math.ceil(filteredEmployees.length / itemsPerPage);

        if (totalPages <= 1 && filteredEmployees.length <= itemsPerPage && filteredEmployees.length > 0 ) {
             paginationControlsDiv.style.display = 'none';
        } else if (filteredEmployees.length === 0) {
             paginationControlsDiv.style.display = 'none';
        }
         else {
            paginationControlsDiv.style.display = 'flex';
        }

        if (totalPages === 0 && filteredEmployees.length === 0) {
            pageInfoSpan.textContent = 'Trang 0 / 0';
            prevPageBtn.disabled = true;
            nextPageBtn.disabled = true;
            pageNumbersContainer.innerHTML = '';
            return;
        }

        pageInfoSpan.textContent = `Trang ${currentPage} / ${totalPages === 0 ? 1: totalPages}`;
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
            pageNumbersContainer.appendChild(createPageButton_NhanVien(1));
            if (startPage > 2) {
                pageNumbersContainer.appendChild(createEllipsis_NhanVien());
            }
        }

        for (let i = startPage; i <= endPage; i++) {
            if (i > 0 && i <= totalPages) {
                 pageNumbersContainer.appendChild(createPageButton_NhanVien(i));
            }
        }

        if (endPage < totalPages) {
            if (endPage < totalPages - 1) {
                pageNumbersContainer.appendChild(createEllipsis_NhanVien());
            }
            pageNumbersContainer.appendChild(createPageButton_NhanVien(totalPages));
        }
    }

    function createPageButton_NhanVien(pageNumber) {
        const button = document.createElement('button');
        button.textContent = pageNumber;
        button.classList.add('btn', 'btn-pagination', 'btn-page-number');
        if (pageNumber === currentPage) {
            button.classList.add('active');
            button.disabled = true;
        }
        button.addEventListener('click', () => {
            currentPage = pageNumber;
            displayCurrentPageEmployees();
        });
        return button;
    }
    
    function createEllipsis_NhanVien() {
        const span = document.createElement('span');
        span.textContent = '...';
        span.className = 'pagination-ellipsis';
        return span;
    }

    // Event Listeners for Prev/Next Buttons
    const prevBtnNV = document.getElementById('prevPageBtnNhanVien');
    if(prevBtnNV) {
        prevBtnNV.addEventListener('click', () => {
            if (currentPage > 1) {
                currentPage--;
                displayCurrentPageEmployees();
            }
        });
    }

    const nextBtnNV = document.getElementById('nextPageBtnNhanVien');
    if(nextBtnNV) {
        nextBtnNV.addEventListener('click', () => {
            totalPages = Math.ceil(filteredEmployees.length / itemsPerPage);
            if (currentPage < totalPages) {
                currentPage++;
                displayCurrentPageEmployees();
            }
        });
    }

    // --- Các hàm xử lý hành động ---
    window.viewEmployeeDetails = function(employeeId) {
        const employee = allEmployees.find(emp => emp.MaNhanVien === employeeId);
        if (employee) {
            alert(`Xem chi tiết Nhân viên:
                Mã NV: ${employee.MaNhanVien}
                Tên: ${employee.HoNhanVien} ${employee.TenNhanVien}
                SĐT: ${employee.SoDienThoaiCaNhan}
                Email: ${employee.EmailCongViec}
                Chức vụ: ${employee.ChucVu}
                Địa chỉ: ${employee.DiaChiThuongTru}
                Trạng thái: ${employee.TrangThaiLamViec}
                Mã TK: ${employee.TaiKhoan ? employee.TaiKhoan.MaTaiKhoan : 'N/A'}`);
        }
    };

    window.editEmployeeDetails = function(employeeId) {
        alert(`Chức năng sửa nhân viên (Mã NV: ${employeeId}) chưa được cài đặt.`);
    };

    window.deleteEmployeeConfirm = function(employeeId) {
        if (confirm(`Bạn có chắc chắn muốn xóa nhân viên có Mã NV: ${employeeId}? Thao tác này không thể hoàn tác.`)) {
            allEmployees = allEmployees.filter(emp => emp.MaNhanVien !== employeeId);
            // Áp dụng lại bộ lọc hiện tại sau khi xóa khỏi allEmployees
            applyNhanVienFiltersAndSearch(false); // false để không alert "Đã xóa" lần nữa
            alert(`Đã xóa nhân viên Mã NV: ${employeeId} khỏi dữ liệu giả.`);
        }
    };

    // --- Search and Filter Logic ---
    window.applyNhanVienFiltersAndSearch = function(showAlert = true) {
        const searchTerm = document.getElementById('searchInputNhanVien')?.value.toLowerCase().trim() || "";
        const chucVu = document.getElementById('chucVuFilterNhanVien')?.value || "";
        const trangThai = document.getElementById('trangThaiLamViecFilterNhanVien')?.value || "";

        filteredEmployees = allEmployees.filter(emp => {
            const fullName = `${emp.HoNhanVien} ${emp.TenNhanVien}`.toLowerCase();
            const matchesSearch = (
                emp.MaNhanVien.toString().includes(searchTerm) ||
                (emp.TaiKhoan && emp.TaiKhoan.MaTaiKhoan.toString().includes(searchTerm)) ||
                fullName.includes(searchTerm) ||
                emp.EmailCongViec.toLowerCase().includes(searchTerm) ||
                emp.SoDienThoaiCaNhan.includes(searchTerm)
            );
            const matchesChucVu = chucVu ? emp.ChucVu === chucVu : true;
            const matchesTrangThai = trangThai ? emp.TrangThaiLamViec === trangThai : true;
            
            return matchesSearch && matchesChucVu && matchesTrangThai;
        });
        currentPage = 1; // Reset về trang đầu tiên
        displayCurrentPageEmployees();
        if (showAlert && searchTerm) { // Chỉ alert nếu có searchTerm và không phải từ hàm delete
            // alert("Đã áp dụng tìm kiếm và bộ lọc.");
        }
    }

    window.resetNhanVienFiltersAndSearch = function() {
        if(document.getElementById('searchInputNhanVien')) document.getElementById('searchInputNhanVien').value = '';
        if(document.getElementById('chucVuFilterNhanVien')) document.getElementById('chucVuFilterNhanVien').value = '';
        if(document.getElementById('trangThaiLamViecFilterNhanVien')) document.getElementById('trangThaiLamViecFilterNhanVien').value = '';
        
        filteredEmployees = [...allEmployees];
        currentPage = 1;
        displayCurrentPageEmployees();
    }

    // --- Initial Load ---
    // Gán sự kiện cho các nút lọc/tìm kiếm
    // Event listener cho DOMContentLoaded đã bao gồm ở ngoài cùng script tag trong HTML gốc
    // Nên chỉ cần gọi hàm khởi tạo chính ở đây nếu script này nằm trong DOMContentLoaded đó
    // Hoặc nếu script này là file riêng thì DOMContentLoaded ở đây là đúng
    
    const searchInput = document.getElementById('searchInputNhanVien');
    const chucVuSelect = document.getElementById('chucVuFilterNhanVien');
    const trangThaiSelect = document.getElementById('trangThaiLamViecFilterNhanVien');
    // Nút tìm kiếm đã có onclick, nếu muốn dùng addEventListener thì:
    // const searchButton = document.querySelector('.controller-bottom button:not(.reset)');
    // if (searchButton) searchButton.addEventListener('click', applyNhanVienFiltersAndSearch);
    // const resetButton = document.querySelector('.controller-bottom button.reset');
    // if (resetButton) resetButton.addEventListener('click', resetNhanVienFiltersAndSearch);


    // Tự động lọc khi thay đổi input/select (tùy chọn)
    if(searchInput) searchInput.addEventListener('input', () => applyNhanVienFiltersAndSearch(false)); // false để không alert khi gõ
    if(chucVuSelect) chucVuSelect.addEventListener('change', applyNhanVienFiltersAndSearch);
    if(trangThaiSelect) trangThaiSelect.addEventListener('change', applyNhanVienFiltersAndSearch);
    
    // Hiển thị bảng lần đầu
    // Không cần generateInitialData nếu sampleEmployeesData đã là allEmployees
    // generateInitialData(); // Nếu sampleEmployeesData chỉ là mẫu ban đầu
    displayCurrentPageEmployees(); 

    // Gán sự kiện cho nút "Add" nhân viên (nếu có)
    const addNhanVienButtonHTML = document.querySelector('.controller-top a[href="/Admin/product-car/add-car.html"] button');
    if(addNhanVienButtonHTML) {
        addNhanVienButtonHTML.parentElement.href = "#"; // Vô hiệu hóa link tạm thời nếu chưa có trang add
        addNhanVienButtonHTML.addEventListener('click', function(event) {
            event.preventDefault(); 
            alert('Chức năng thêm nhân viên mới chưa được cài đặt.');
        });
    }
    console.log("Employee data with pagination loaded!");
