// Chạy hàm tải dữ liệu sau khi DOM đã tải xong
document.addEventListener('DOMContentLoaded', loadDynamicContent);

/**
 * Hàm trợ giúp để tải file JSON
 * @param {string} url Đường dẫn đến file JSON
 * @returns {Promise<object|null>} Dữ liệu JSON hoặc null nếu lỗi
 */
async function fetchJson(url) {
    try {
        const response = await fetch(url);
        if (!response.ok) {
            throw new Error(`HTTP error! status: ${response.status} for ${url}`);
        }
        return await response.json();
    } catch (e) {
        console.error(`Failed to fetch ${url}:`, e);
        return null;
    }
}

/**
 * Hàm khởi tạo chính để tải và hiển thị nội dung động
 */
async function loadDynamicContent() {
    
    // --- TẢI DỮ LIỆU TỪ JSON ---
    const [productData, imageData, accessoryData] = await Promise.all([
        fetchJson('assets/js/product.json'),     // Tải sản phẩm
        fetchJson('assets/js/image.json'),      // Tải ảnh banner
        fetchJson('assets/js/phukien.json')   // Tải phụ kiện
    ]);

    // Kiểm tra nếu dữ liệu quan trọng không tải được
    if (!productData || !imageData || !accessoryData) {
        console.error("Failed to load critical data. App initialization aborted.");
        return;
    }

    // Gán dữ liệu đã tải vào các biến VÀ LỌC THEO TRƯỜNG "show"
    const carsData = productData.cars.filter(car => car.show === 1);
    const bikesData = productData.bikes.filter(bike => bike.show === 1);
    
    const { banner: bannerData } = imageData;
    const { pk: accessoriesData } = accessoryData; // 'pk' từ file phukien.json

    // --- KHỞI TẠO CÁC BIẾN ---
    let currentCarIndex = 0;
    let currentBikeIndex = 0;
    let carInterval;
    let bikeInterval;

    // --- Banner Slider Logic ---
    const sliderContainer = document.getElementById('hero-slider');
    if (sliderContainer) {
        sliderContainer.innerHTML = ''; // Xóa ảnh tĩnh (nếu có)

        bannerData.forEach((banner, index) => {
            const img = document.createElement('img');
            img.src = banner.image;
            img.alt = `VinFast Banner ${index + 1}`;
            img.className = 'slider-image';
            if (index === 0) {
                img.classList.add('active'); // Đặt ảnh đầu tiên là active
            }
            sliderContainer.appendChild(img);
        });

        const sliderImages = document.querySelectorAll('.slider-image');
        let currentImageIndex = 0;
        
        if (sliderImages.length > 0) {
            setInterval(() => {
                sliderImages[currentImageIndex].classList.remove('active');
                currentImageIndex = (currentImageIndex + 1) % sliderImages.length;
                sliderImages[currentImageIndex].classList.add('active');
            }, 5000);
        }
    }

    // --- LẤY CÁC ELEMENT TRÊN TRANG ---
    // Car elements
    const carProductContainer = document.getElementById('car-product-container');
    const carImage = document.getElementById('car-image');
    const carName = document.getElementById('car-name');
    const carPrice = document.getElementById('car-price');
    const carRange = document.getElementById('car-range');
    const carType = document.getElementById('car-type');
    const carSeats = document.getElementById('car-seats');
    const prevCarBtn = document.getElementById('prev-car');
    const nextCarBtn = document.getElementById('next-car');
    const carDetailLink = document.getElementById('car-detail-link');

    // Bike elements
    const bikeProductContainer = document.getElementById('bike-product-container');
    const bikeImage = document.getElementById('bike-image');
    const bikeName = document.getElementById('bike-name');
    const bikePrice = document.getElementById('bike-price');
    const bikeRange = document.getElementById('bike-range');
    const bikeType = document.getElementById('bike-type');
    const bikeSeats = document.getElementById('bike-seats');
    const prevBikeBtn = document.getElementById('prev-bike');
    const nextBikeBtn = document.getElementById('next-bike');
    const bikeDetailLink = document.getElementById('bike-detail-link');

    
    // Populate Accessories Grid (Giới hạn 4 sản phẩm)
    const accessoriesGrid = document.getElementById('accessories-grid');
    if (accessoriesGrid) { 
        
        const accessoriesToShow = accessoriesData.slice(0, 4); 

        accessoriesToShow.forEach((item, index) => {
            const card = document.createElement('div');
            card.className = 'accessory-card bg-white rounded-lg shadow-md overflow-hidden reveal';
            card.style.transitionDelay = `${index * 100}ms`;
            
            card.innerHTML = `
                <div class="overflow-hidden aspect-square">
                    <img src="${item.image}" alt="${item.name}" class="w-full h-full object-cover">
                </div>
                <div class="p-4">
                    <h4 class="font-bold text-gray-800 truncate">${item.name}</h4>
                    <p class="text-blue-600 font-semibold">${item.gia}</p>
                </div>
            `;
            accessoriesGrid.appendChild(card);
        });
    }

    // --- HÀM CẬP NHẬT GIAO DIỆN SẢN PHẨM ---
    function updateCarDisplay() {
        if (!carsData || carsData.length === 0 || !carProductContainer) return; 
        
        const car = carsData[currentCarIndex];
        carProductContainer.classList.add('product-transitioning');
        
        setTimeout(() => {
            carImage.src = car.image; 
            carName.textContent = car.name; 
            carPrice.textContent = car.price; 
            carRange.textContent = car.specs.range || 'N/A'; 
            carSeats.textContent = car.specs.seats || 'N/A';
            carType.textContent = car.category || 'N/A'; 
            carDetailLink.href = `product-detail.html?id=${car.id}&type=car`;
            carProductContainer.classList.remove('product-transitioning');
        }, 400);
    }
    
    function updateBikeDisplay() {
        if (!bikesData || bikesData.length === 0 || !bikeProductContainer) return; 

        const bike = bikesData[currentBikeIndex];
        bikeProductContainer.classList.add('product-transitioning');
        
        setTimeout(() => {
            bikeImage.src = bike.image; 
            bikeName.textContent = bike.name; 
            bikePrice.textContent = bike.price; 
            bikeRange.textContent = bike.specs.range || 'N/A';
            bikeType.textContent = 'Xe máy điện'; 
            bikeSeats.textContent = '2 Chỗ';
            bikeDetailLink.href = `product-detail.html?id=${bike.id}&type=bike`;
            bikeProductContainer.classList.remove('product-transitioning');
        }, 400);
    }

    // --- HÀM AUTO-SLIDE VÀ RESET ---
    function startCarAutoplay() {
        // Chỉ chạy autoplay nếu có nhiều hơn 1 xe
        if (!carsData || carsData.length <= 1) return;
        
        clearInterval(carInterval);
        carInterval = setInterval(() => {
            currentCarIndex = (currentCarIndex + 1) % carsData.length;
            updateCarDisplay();
        }, 5000);
    }

    function startBikeAutoplay() {
        // Chỉ chạy autoplay nếu có nhiều hơn 1 xe máy
        if (!bikesData || bikesData.length <= 1) return;

        clearInterval(bikeInterval);
        bikeInterval = setInterval(() => {
            currentBikeIndex = (currentBikeIndex + 1) % bikesData.length;
            updateBikeDisplay();
        }, 5000);
    }

    // --- EVENT LISTENERS (Product Sliders) ---
    // Ẩn nút nếu chỉ có 1 hoặc 0 sản phẩm
    if (prevCarBtn && nextCarBtn) {
        if (!carsData || carsData.length <= 1) {
            prevCarBtn.style.display = 'none';
            nextCarBtn.style.display = 'none';
        } else {
            prevCarBtn.addEventListener('click', () => {
                currentCarIndex = (currentCarIndex - 1 + carsData.length) % carsData.length;
                updateCarDisplay();
                startCarAutoplay();
            });

            nextCarBtn.addEventListener('click', () => {
                currentCarIndex = (currentCarIndex + 1) % carsData.length;
                updateCarDisplay();
                startCarAutoplay();
            });
        }
    }

    if (prevBikeBtn && nextBikeBtn) {
        if (!bikesData || bikesData.length <= 1) {
            prevBikeBtn.style.display = 'none';
            nextBikeBtn.style.display = 'none';
        } else {
            prevBikeBtn.addEventListener('click', () => {
                currentBikeIndex = (currentBikeIndex - 1 + bikesData.length) % bikesData.length;
                updateBikeDisplay();
                startBikeAutoplay();
            });

            nextBikeBtn.addEventListener('click', () => {
                currentBikeIndex = (currentBikeIndex + 1) % bikesData.length;
                updateBikeDisplay();
                startBikeAutoplay();
            });
        }
    }
    
    // --- Hiệu ứng Reveal on scroll ---
    // Đặt ở đây để nó chạy sau khi các card phụ kiện đã được thêm vào DOM
    const revealElements = document.querySelectorAll('.reveal');
    const observer = new IntersectionObserver((entries) => {
        entries.forEach(entry => {
            if (entry.isIntersecting) {
                entry.target.classList.add('visible');
            }
        });
    }, { threshold: 0.1 });
    
    revealElements.forEach(el => observer.observe(el));
    
    // --- INITIAL DISPLAY & START AUTOPLAY ---
    if (carProductContainer) {
        updateCarDisplay();
        startCarAutoplay();
    }
    if (bikeProductContainer) {
        updateBikeDisplay();
        startBikeAutoplay();
    }
}