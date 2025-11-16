document.addEventListener('DOMContentLoaded', function() {
    const mainImageInput = document.getElementById('HinhAnhURL');
    const mainImagePreview = document.getElementById('mainImagePreview');
    const mainImagePlaceholder = document.getElementById('mainImagePlaceholder');

    const additionalImagesInput = document.getElementById('DanhSachURLHinhAnh');
    const additionalImagesPreviewContainer = document.getElementById('additionalImagesPreviewContainer');
    const additionalImagesPlaceholder = document.getElementById('additionalImagesPlaceholder');

    // Preview for the main image
    mainImageInput.addEventListener('change', function() {
        const file = this.files && this.files.length > 0 ? this.files.item(0) : null;

        if (file) {
            const reader = new FileReader();

            reader.onload = function(e) {
                mainImagePreview.src = e.target.result;
                mainImagePreview.style.display = 'block';
                mainImagePlaceholder.style.display = 'none';
            }

            reader.readAsDataURL(file);
        } else {
            mainImagePreview.src = '#';
            mainImagePreview.style.display = 'none';
            mainImagePlaceholder.style.display = 'block';
        }
    });

    // Preview for additional images
    additionalImagesInput.addEventListener('change', function() {
        additionalImagesPreviewContainer.innerHTML = ''; // Clear previous previews
        additionalImagesPlaceholder.style.display = 'none';

        if (this.files && this.files.length > 0) {
            for (let i = 0; i < Math.min(this.files.length, 4); i++) { // Limit to 4 additional images
                const file = this.files.item(i);
                const reader = new FileReader();
                const img = document.createElement('img');
                img.style.maxHeight = '80px';
                img.style.maxWidth = '80px';
                img.style.border = '1px solid var(--color-light)';
                img.style.objectFit = 'cover';

                reader.onload = function(e) {
                    img.src = e.target.result;
                    additionalImagesPreviewContainer.appendChild(img);
                }
                reader.readAsDataURL(file);
            }
        } else {
            additionalImagesPlaceholder.style.display = 'block';
        }
    });
});
document.addEventListener('DOMContentLoaded', function() {
    // --- Xử lý cho Ảnh Đại Diện ---
    const mainImageInput = document.getElementById('HinhAnhURL');
    const mainImageContainer = document.getElementById('mainImageContainer');
    const mainImagePreview = document.getElementById('mainImagePreview');
    const mainImagePlaceholder = mainImageContainer.querySelector('.placeholder-text');

    // Khi người dùng nhấp vào khu vực xem trước, kích hoạt input file
    mainImageContainer.addEventListener('click', () => {
        mainImageInput.click();
    });

    // Khi người dùng chọn một tệp
    mainImageInput.addEventListener('change', function() {
        const file = this.files[0];
        if (file) {
            const reader = new FileReader();

            reader.onload = function(e) {
                // Hiển thị ảnh và ẩn placeholder
                mainImagePreview.src = e.target.result;
                mainImagePreview.style.display = 'block';
                mainImagePlaceholder.style.display = 'none';
            }
            reader.readAsDataURL(file);
        }
    });


    // --- Xử lý cho các Ảnh Phụ ---
    const additionalImagesInput = document.getElementById('DanhSachURLHinhAnh');
    const additionalImagesContainer = document.getElementById('additionalImagesContainer');
    const selectAdditionalImagesBtn = document.getElementById('selectAdditionalImagesBtn');

    // Khi nhấn nút "Chọn Ảnh Phụ"
    selectAdditionalImagesBtn.addEventListener('click', () => {
        additionalImagesInput.click();
    });
    
    additionalImagesInput.addEventListener('change', function() {
        // Xóa các ảnh xem trước cũ
        additionalImagesContainer.innerHTML = ''; 

        if (this.files && this.files.length > 0) {
            // Giới hạn chỉ hiển thị 4 ảnh đầu tiên
            const filesToShow = Array.from(this.files).slice(0, 4);

            filesToShow.forEach(file => {
                const reader = new FileReader();

                reader.onload = function(e) {
                    // Tạo wrapper và ảnh
                    const wrapper = document.createElement('div');
                    wrapper.className = 'img-preview-wrapper';
                    
                    const img = document.createElement('img');
                    img.src = e.target.result;
                    
                    wrapper.appendChild(img);
                    additionalImagesContainer.appendChild(wrapper);
                }

                reader.readAsDataURL(file);
            });
        } else {
             // Nếu không có ảnh nào, hiển thị lại placeholder
            additionalImagesContainer.innerHTML = `
                <div class="placeholder-text">
                    <span class="material-icons-sharp">add_photo_alternate</span>
                    <p>Chưa có ảnh phụ</p>
                </div>`;
        }
    });
});