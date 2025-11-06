
    const initialSystemAccounts = [
        { MaTaiKhoan: 1, TenDangNhap: 'admin_an', MatKhauHash: 'hashed_password_an_secure', EmailLienKet: 'an.nguyen@example.com', MaNhanVien: 1, HoTenNguoiDung: 'Nguyễn Văn An', VaiTro: 'Admin', QuyenHan: '{"all_permissions": true, "module_A": "CRUD", "module_B": "R"}', TrangThaiTaiKhoan: 'Hoạt động' },
        { MaTaiKhoan: 2, TenDangNhap: 'manager_binh', MatKhauHash: 'hashed_password_binh_secure', EmailLienKet: 'binh.tran@example.com', MaNhanVien: 2, HoTenNguoiDung: 'Trần Thị Bình', VaiTro: 'Quản lý kho', QuyenHan: '{"manage_inventory": true, "view_reports": true, "approve_transfers": false}', TrangThaiTaiKhoan: 'Hoạt động' },
        { MaTaiKhoan: 3, TenDangNhap: 'staff_dung', MatKhauHash: 'hashed_password_dung_secure', EmailLienKet: 'dung.le@example.com', MaNhanVien: 3, HoTenNguoiDung: 'Lê Hoàng Dũng', VaiTro: 'Nhân viên bán hàng', QuyenHan: '{"create_orders": true, "view_products": true, "edit_own_profile": true}', TrangThaiTaiKhoan: 'Hoạt động' },
        { MaTaiKhoan: 4, TenDangNhap: 'staff_chau', MatKhauHash: 'hashed_password_chau_secure', EmailLienKet: 'chau.pham@example.com', MaNhanVien: 4, HoTenNguoiDung: 'Phạm Minh Châu', VaiTro: 'Nhân viên bán hàng', QuyenHan: '{"create_orders": true, "view_products": true}', TrangThaiTaiKhoan: 'Hoạt động' },
        { MaTaiKhoan: 5, TenDangNhap: 'accountant_mai', MatKhauHash: 'hashed_password_mai_secure', EmailLienKet: 'mai.vo@example.com', MaNhanVien: 5, HoTenNguoiDung: 'Võ Thị Mai', VaiTro: 'Kế toán', QuyenHan: '{"view_financial_reports": true, "manage_invoices": true, "access_payroll": false, "generate_tax_docs": true, "audit_logs": "read_only"}', TrangThaiTaiKhoan: 'Tạm khóa' },
        // Thêm dữ liệu giả để có thể phân trang
        { MaTaiKhoan: 6, TenDangNhap: 'admin_huy', MatKhauHash: 'hashed_pass_huy', EmailLienKet: 'huy.do@example.com', MaNhanVien: 6, HoTenNguoiDung: 'Đỗ Gia Huy', VaiTro: 'Admin', QuyenHan: '{"all_permissions": true}', TrangThaiTaiKhoan: 'Hoạt động' },
        { MaTaiKhoan: 7, TenDangNhap: 'manager_phat', MatKhauHash: 'hashed_pass_phat', EmailLienKet: 'phat.huynh@example.com', MaNhanVien: 7, HoTenNguoiDung: 'Huỳnh Tấn Phát', VaiTro: 'Quản lý kho', QuyenHan: '{"manage_inventory": true}', TrangThaiTaiKhoan: 'Ngừng hoạt động' },
        { MaTaiKhoan: 8, TenDangNhap: 'staff_thy', MatKhauHash: 'hashed_pass_thy', EmailLienKet: 'thy.ngo@example.com', MaNhanVien: 8, HoTenNguoiDung: 'Ngô Bảo Thy', VaiTro: 'Nhân viên bán hàng', QuyenHan: '{"create_orders": true}', TrangThaiTaiKhoan: 'Hoạt động' },
        { MaTaiKhoan: 9, TenDangNhap: 'accountant_hung', MatKhauHash: 'hashed_pass_hung', EmailLienKet: 'hung.duong@example.com', MaNhanVien: 9, HoTenNguoiDung: 'Dương Quốc Hùng', VaiTro: 'Kế toán', QuyenHan: '{"view_financial_reports": true}', TrangThaiTaiKhoan: 'Hoạt động' },
        { MaTaiKhoan: 10, TenDangNhap: 'admin_tam', MatKhauHash: 'hashed_pass_tam', EmailLienKet: 'tam.ly@example.com', MaNhanVien: 10, HoTenNguoiDung: 'Lý Mỹ Tâm', VaiTro: 'Admin', QuyenHan: '{"all_permissions": true}', TrangThaiTaiKhoan: 'Tạm khóa' },
        { MaTaiKhoan: 11, TenDangNhap: 'manager_thinh', MatKhauHash: 'hashed_pass_thinh', EmailLienKet: 'thinh.vu@example.com', MaNhanVien: 11, HoTenNguoiDung: 'Vũ Đức Thịnh', VaiTro: 'Quản lý kho', QuyenHan: '{"manage_inventory": true}', TrangThaiTaiKhoan: 'Hoạt động' },
        { MaTaiKhoan: 12, TenDangNhap: 'staff_nguyet', MatKhauHash: 'hashed_pass_nguyet', EmailLienKet: 'nguyet.hoang@example.com', MaNhanVien: 12, HoTenNguoiDung: 'Hoàng Minh Nguyệt', VaiTro: 'Nhân viên bán hàng', QuyenHan: '{"create_orders": true}', TrangThaiTaiKhoan: 'Ngừng hoạt động' },
        { MaTaiKhoan: 13, TenDangNhap: 'accountant_sang', MatKhauHash: 'hashed_pass_sang', EmailLienKet: 'sang.phan@example.com', MaNhanVien: 13, HoTenNguoiDung: 'Phan Thanh Sang', VaiTro: 'Kế toán', QuyenHan: '{"view_financial_reports": true}', TrangThaiTaiKhoan: 'Hoạt động' },
        { MaTaiKhoan: 14, TenDangNhap: 'admin_mai_b', MatKhauHash: 'hashed_pass_mai_b', EmailLienKet: 'mai.bui@example.com', MaNhanVien: 14, HoTenNguoiDung: 'Bùi Ngọc Mai', VaiTro: 'Admin', QuyenHan: '{"all_permissions": true}', TrangThaiTaiKhoan: 'Hoạt động' },
        { MaTaiKhoan: 15, TenDangNhap: 'manager_khoa', MatKhauHash: 'hashed_pass_khoa', EmailLienKet: 'khoa.nguyen@example.com', MaNhanVien: 15, HoTenNguoiDung: 'Nguyễn Anh Khoa', VaiTro: 'Quản lý kho', QuyenHan: '{"manage_inventory": true}', TrangThaiTaiKhoan: 'Tạm khóa' },
        { MaTaiKhoan: 16, TenDangNhap: 'staff_vy_t', MatKhauHash: 'hashed_pass_vy_t', EmailLienKet: 'vy.tran@example.com', MaNhanVien: 16, HoTenNguoiDung: 'Trần Phương Vy', VaiTro: 'Nhân viên bán hàng', QuyenHan: '{"create_orders": true}', TrangThaiTaiKhoan: 'Hoạt động' },
        { MaTaiKhoan: 17, TenDangNhap: 'accountant_quan_l', MatKhauHash: 'hashed_pass_quan_l', EmailLienKet: 'quan.le@example.com', MaNhanVien: 17, HoTenNguoiDung: 'Lê Minh Quân', VaiTro: 'Kế toán', QuyenHan: '{"view_financial_reports": true}', TrangThaiTaiKhoan: 'Hoạt động' }
    ];

    let allAccounts = [...initialSystemAccounts];
    let filteredAccounts = [...allAccounts];
    let currentPage = 1;
    const itemsPerPage = 8; // Hiển thị 8 tài khoản mỗi trang
    let totalPages = 1;

    function displayCurrentPageAccounts() {
        const tableBody = document.getElementById('account-table-body');
        if (!tableBody) {
            console.error("Lỗi: Không tìm thấy tbody với ID 'account-table-body'.");
            return;
        }
        tableBody.innerHTML = '';

        const noDataMessageRow = tableBody.insertRow();
        const noDataCell = noDataMessageRow.insertCell();
        noDataCell.colSpan = 9; // Số cột của bảng tài khoản
        noDataCell.textContent = 'Không có tài khoản nào phù hợp.';
        noDataCell.style.textAlign = 'center';
        noDataMessageRow.style.display = 'none'; // Mặc định ẩn

        if (filteredAccounts.length === 0) {
            noDataMessageRow.style.display = '';
            updatePaginationUI_TaiKhoan();
            return;
        }

        const startIndex = (currentPage - 1) * itemsPerPage;
        const endIndex = Math.min(startIndex + itemsPerPage, filteredAccounts.length);
        const accountsToDisplay = filteredAccounts.slice(startIndex, endIndex);

        if (accountsToDisplay.length === 0 && currentPage > 1) {
            currentPage--;
            displayCurrentPageAccounts();
            return;
        }
        
        if (accountsToDisplay.length > 0) {
            tableBody.innerHTML = '';
        }


        accountsToDisplay.forEach(acc => {
            const row = tableBody.insertRow();
            row.insertCell().textContent = acc.MaTaiKhoan;
            row.insertCell().textContent = acc.MaNhanVien !== null ? acc.MaNhanVien : 'N/A';
            row.insertCell().textContent = acc.TenDangNhap;
            row.insertCell().textContent = acc.HoTenNguoiDung;
            row.insertCell().textContent = acc.EmailLienKet;
            row.insertCell().textContent = acc.VaiTro;

            const quyenHanCell = row.insertCell();
            quyenHanCell.textContent = acc.QuyenHan.length > 25 ? acc.QuyenHan.substring(0, 22) + '...' : acc.QuyenHan;
            quyenHanCell.title = acc.QuyenHan;
            quyenHanCell.classList.add('quyenhan-cell');

            const statusCell = row.insertCell();
            const statusBadge = document.createElement('span');
            statusBadge.className = 'status-badge';
            statusBadge.textContent = acc.TrangThaiTaiKhoan;
            if (acc.TrangThaiTaiKhoan === "Hoạt động") statusBadge.classList.add('status-active');
            else if (acc.TrangThaiTaiKhoan === "Tạm khóa") statusBadge.classList.add('status-locked'); // Ví dụ: màu vàng/cam
            else if (acc.TrangThaiTaiKhoan === "Ngừng hoạt động") statusBadge.classList.add('status-inactive'); // Ví dụ: màu đỏ/xám
            else statusBadge.classList.add('status-other'); // Màu mặc định cho trạng thái khác
            statusCell.appendChild(statusBadge);

            const actionsCell = row.insertCell();
            actionsCell.innerHTML = `
                <button class="icon-btn view-btn" title="Xem chi tiết" onclick="viewAccountDetails(${acc.MaTaiKhoan})"><i class="fas fa-eye"></i></button>
                <button class="icon-btn edit-btn" title="Cập nhật" onclick="editAccountDetails(${acc.MaTaiKhoan})"><i class="fas fa-edit"></i></button>
                <button class="icon-btn delete-btn" title="Xóa" onclick="deleteAccountConfirm(${acc.MaTaiKhoan})"><i class="fas fa-trash-alt"></i></button>
            `;
        });
        updatePaginationUI_TaiKhoan();
    }

    function updatePaginationUI_TaiKhoan() {
        const prevPageBtn = document.getElementById('prevPageBtnTaiKhoan');
        const nextPageBtn = document.getElementById('nextPageBtnTaiKhoan');
        const pageInfoSpan = document.getElementById('pageInfoTaiKhoan');
        const pageNumbersContainer = document.getElementById('pageNumbersContainerTaiKhoan');
        const paginationControlsDiv = document.querySelector('#tai-khoan-html .pagination-controls') || document.querySelector('.pagination-controls'); // Cụ thể hơn nếu có nhiều trang cùng ID

        if (!prevPageBtn || !nextPageBtn || !pageInfoSpan || !pageNumbersContainer || !paginationControlsDiv) {
            return;
        }
        
        totalPages = Math.ceil(filteredAccounts.length / itemsPerPage);

        if (totalPages <= 1 && filteredAccounts.length <= itemsPerPage && filteredAccounts.length > 0) {
             paginationControlsDiv.style.display = 'none';
        } else if (filteredAccounts.length === 0) {
             paginationControlsDiv.style.display = 'none';
        }
         else {
            paginationControlsDiv.style.display = 'flex';
        }
        
        if (totalPages === 0 && filteredAccounts.length === 0) {
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
            pageNumbersContainer.appendChild(createPageButton_TaiKhoan(1));
            if (startPage > 2) {
                pageNumbersContainer.appendChild(createEllipsis_TaiKhoan());
            }
        }

        for (let i = startPage; i <= endPage; i++) {
            if (i > 0 && i <= totalPages) {
                pageNumbersContainer.appendChild(createPageButton_TaiKhoan(i));
            }
        }

        if (endPage < totalPages) {
            if (endPage < totalPages - 1) {
                pageNumbersContainer.appendChild(createEllipsis_TaiKhoan());
            }
            pageNumbersContainer.appendChild(createPageButton_TaiKhoan(totalPages));
        }
    }

    function createPageButton_TaiKhoan(pageNumber) {
        const button = document.createElement('button');
        button.textContent = pageNumber;
        button.classList.add('btn', 'btn-pagination', 'btn-page-number');
        if (pageNumber === currentPage) {
            button.classList.add('active');
            button.disabled = true;
        }
        button.addEventListener('click', () => {
            currentPage = pageNumber;
            displayCurrentPageAccounts();
        });
        return button;
    }
    
    function createEllipsis_TaiKhoan() {
        const span = document.createElement('span');
        span.textContent = '...';
        span.className = 'pagination-ellipsis';
        return span;
    }
    
    const prevBtnTK = document.getElementById('prevPageBtnTaiKhoan');
    if (prevBtnTK) {
        prevBtnTK.addEventListener('click', () => {
            if (currentPage > 1) {
                currentPage--;
                displayCurrentPageAccounts();
            }
        });
    }
    const nextBtnTK = document.getElementById('nextPageBtnTaiKhoan');
    if(nextBtnTK) {
        nextBtnTK.addEventListener('click', () => {
            totalPages = Math.ceil(filteredAccounts.length / itemsPerPage);
            if (currentPage < totalPages) {
                currentPage++;
                displayCurrentPageAccounts();
            }
        });
    }

    window.viewAccountDetails = function(accountId) {
        const account = allAccounts.find(acc => acc.MaTaiKhoan === accountId);
        if (account) {
            alert(`Xem chi tiết Tài khoản:
Mã TK: ${account.MaTaiKhoan}
Mã NV: ${account.MaNhanVien || 'N/A'}
Tên đăng nhập: ${account.TenDangNhap}
Mật khẩu (Hash): ${account.MatKhauHash}
Tên người dùng: ${account.HoTenNguoiDung}
Email: ${account.EmailLienKet}
Vai trò: ${account.VaiTro}
Quyền hạn: ${account.QuyenHan} 
Trạng thái: ${account.TrangThaiTaiKhoan}`);
        }
    };

    window.editAccountDetails = function(accountId) {
        alert(`Chức năng sửa tài khoản (Mã TK: ${accountId}) chưa được cài đặt.`);
    };

    window.deleteAccountConfirm = function(accountId) {
        if (confirm(`Bạn có chắc chắn muốn xóa tài khoản có Mã TK: ${accountId}? Thao tác này không thể hoàn tác.`)) {
            allAccounts = allAccounts.filter(acc => acc.MaTaiKhoan !== accountId);
            applyFiltersAndSearchAccounts(false); // false để không alert
            alert(`Đã xóa tài khoản Mã TK: ${accountId} khỏi dữ liệu giả.`);
        }
    };

    window.applyFiltersAndSearchAccounts = function(showAlert = true) {
        const searchTerm = document.getElementById('searchInputTaiKhoan')?.value.toLowerCase().trim() || "";
        const roleFilter = document.getElementById('roleFilterTaiKhoan')?.value || "";
        // Thêm filter cho trạng thái tài khoản nếu có select tương ứng trong HTML
        // const statusFilter = document.getElementById('statusFilterTaiKhoan')?.value || "";


        filteredAccounts = allAccounts.filter(acc => {
            const matchesSearch = (
                acc.MaTaiKhoan.toString().includes(searchTerm) ||
                (acc.MaNhanVien && acc.MaNhanVien.toString().includes(searchTerm)) ||
                acc.TenDangNhap.toLowerCase().includes(searchTerm) ||
                acc.HoTenNguoiDung.toLowerCase().includes(searchTerm) ||
                acc.EmailLienKet.toLowerCase().includes(searchTerm)
            );
            const matchesRole = roleFilter ? acc.VaiTro === roleFilter : true;
            // const matchesStatus = statusFilter ? acc.TrangThaiTaiKhoan === statusFilter : true;
            
            return matchesSearch && matchesRole; // && matchesStatus;
        });
        currentPage = 1;
        displayCurrentPageAccounts();
    }

    window.searchAccount = function() { // Giữ tên hàm theo onclick HTML
        applyFiltersAndSearchAccounts();
    }

    window.resetSearchAccount = function() { // Giữ tên hàm theo onclick HTML
        if(document.getElementById('searchInputTaiKhoan')) document.getElementById('searchInputTaiKhoan').value = '';
        if(document.getElementById('roleFilterTaiKhoan')) document.getElementById('roleFilterTaiKhoan').value = '';
        // if(document.getElementById('statusFilterTaiKhoan')) document.getElementById('statusFilterTaiKhoan').value = '';
        
        filteredAccounts = [...allAccounts];
        currentPage = 1;
        displayCurrentPageAccounts();
    }
    
    // Gán sự kiện cho các input/select để tự động lọc khi thay đổi
    const searchInputTK = document.getElementById('searchInputTaiKhoan');
    const roleFilterTK = document.getElementById('roleFilterTaiKhoan');

    if(searchInputTK) searchInputTK.addEventListener('input', () => applyFiltersAndSearchAccounts(false));
    if(roleFilterTK) roleFilterTK.addEventListener('change', applyFiltersAndSearchAccounts);


    // Khởi tạo hiển thị
    // Không cần generateInitialData vì sampleSystemAccounts đã là allAccounts
    const tableBodyTK = document.getElementById('account-table-body');
    const paginationControlsTK = document.querySelector('#tai-khoan-html .pagination-controls') || document.querySelector('.pagination-controls');

    if (tableBodyTK && paginationControlsTK) {
        displayCurrentPageAccounts();
        console.log("System accounts data with pagination loaded!");
    } else {
        if (!tableBodyTK) console.warn("Account table body ('account-table-body') not found.");
        if (!paginationControlsTK) console.warn("Pagination controls for accounts not found.");
    }
    

    // Nút Add (ví dụ, nếu bạn muốn xử lý bằng JS)
    const addTaiKhoanButtonHTML = document.querySelector('.controller-top a[href="/Admin/product-car/add-car.html"] button');
    if(addTaiKhoanButtonHTML) {
        addTaiKhoanButtonHTML.parentElement.href = "#"; // Vô hiệu hóa link tạm
        addTaiKhoanButtonHTML.addEventListener('click', function(event) {
            event.preventDefault();
            alert('Chức năng thêm tài khoản mới chưa được cài đặt.');
        });
    }
