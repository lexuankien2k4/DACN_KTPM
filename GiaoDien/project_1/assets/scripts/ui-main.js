// Chạy ngay khi DOM được tải
document.addEventListener('DOMContentLoaded', () => {
    
    // --- Chức năng Header cuộn ---
    const header = document.getElementById('main-header');
    if (header) {
        window.addEventListener('scroll', () => {
            // Thêm class 'header-scrolled' khi cuộn xuống hơn 50px
            header.classList.toggle('header-scrolled', window.scrollY > 50);
        });
    }

    // --- Chức năng Menu (Mobile) ---
    const menuToggleButton = document.getElementById('menu-toggle-button');
    const closeMenuButton = document.getElementById('close-menu-button');
    const sideMenu = document.getElementById('side-menu');
    const menuOverlay = document.getElementById('menu-overlay');

    // Hàm để bật/tắt menu
    function toggleMenu() {
        if (sideMenu && menuOverlay) {
            sideMenu.classList.toggle('is-open');
            menuOverlay.classList.toggle('is-open');
        }
    }

    // Gán sự kiện click cho các nút
    // Kiểm tra xem các element có tồn tại không trước khi gán
    if (menuToggleButton && closeMenuButton && menuOverlay) {
        [menuToggleButton, closeMenuButton, menuOverlay].forEach(el => {
            el.addEventListener('click', toggleMenu);
        });
    }

});