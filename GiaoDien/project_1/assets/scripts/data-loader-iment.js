document.addEventListener("DOMContentLoaded", function () {
    // --- START: JS for Loan Calculator ---
    const carSelect = document.getElementById("name");
    const versionSelect = document.getElementById("version");
    
    // Biến cho logic giá
    const priceTypeList = document.getElementById("price-type-list");
    const priceTypeCustom = document.getElementById("price-type-custom");
    const listPriceDisplay = document.getElementById("list-price-display"); 
    const customPriceContainer = document.getElementById("custom-price-container");
    const customPriceInput = document.getElementById("custom-price-input");
    
    const batterySelect = document.getElementById("battery");
    const carPriceEl = document.getElementById("car-price");
    const imgProduct = document.querySelector(".img-product");
    const bankSelect = document.getElementById("bank");
    const interestRateInput = document.getElementById("interest-rate-input");
    const interestRateDisplay = document.getElementById("interest-rate-display");
    const downPaymentEl = document.getElementById("down-payment");
    const loanAmountEl = document.getElementById("loan-amount");
    const interestAmountEl = document.getElementById("interest-amount");
    const totalLoanInterestEl = document.getElementById("total-loan-interest");
    const loanDurationButtonsContainer = document.getElementById("loan-duration-buttons");
    const downPaymentButtonsContainer = document.getElementById("down-payment-buttons");
    
    // Modal elements
    const showScheduleBtn = document.getElementById("show-schedule-btn");
    const scheduleModal = document.getElementById("schedule-modal");
    const scheduleModalOverlay = document.getElementById("schedule-modal-overlay");
    const closeModalBtn = document.getElementById("close-modal-btn");
    const scheduleBody = document.getElementById("schedule-body");
    const downloadCsvBtn = document.getElementById("download-csv-btn");

    let selectedDownPaymentPercent = 0.2;
    let selectedLoanDuration = 1;
    let basePrice = 0; // Giá niêm yết từ JSON
    let batteryPrice = 0;
    const BATTERY_COST = 90000000; 
    let scheduleData = [];

    const currencyFormatter = new Intl.NumberFormat('vi-VN', { style: 'currency', currency: 'VND' });

    // --- HÀM MỚI: Xử lý định dạng số ---

    // Hàm_1: Loại bỏ ký tự, chỉ giữ lại số
    function parseFormattedNumber(string) {
        return parseFloat(string.replace(/[^0-9]/g, '')) || 0;
    }

    // Hàm_2: Định dạng số với dấu chấm
    function formatCurrencyInput(inputElement) {
        // 1. Lấy giá trị, loại bỏ dấu chấm và ký tự
        let value = parseFormattedNumber(inputElement.value.toString());
        
        // 2. Chuyển thành chuỗi
        let numString = value.toString();

        // 3. Định dạng lại với dấu chấm (nếu không phải là rỗng)
        if (numString.length > 0) {
            inputElement.value = numString.replace(/\B(?=(\d{3})+(?!\d))/g, '.');
        } else {
            inputElement.value = '';
        }
    }
    // ------------------------------------

    function parsePrice(priceString) {
        if (!priceString) return 0;
        const numericString = priceString.replace(/[^0-9]/g, '');
        return parseInt(numericString, 10) || 0;
    }

    async function fetchCarData() {
        try {
            const response = await fetch("../assets/js/product.json"); 
            const productData = await response.json();

            if (versionSelect) {
                versionSelect.parentElement.style.display = 'none';
            }

            carSelect.innerHTML = '<option value="">Chọn mẫu xe</option>';
            
            const allProducts = productData.cars;
            
            allProducts.forEach(product => {
                if (product.show === 1) {
                    const option = new Option(product.name, product.id);
                    option.dataset.price = product.price; 
                    option.dataset.image = product.image;
                    carSelect.add(option);
                }
            });

            carSelect.addEventListener("change", function () {
                const selectedOption = this.options[this.selectedIndex];
                if (selectedOption && selectedOption.value) {
                    imgProduct.src = "../" + selectedOption.dataset.image;
                    basePrice = parsePrice(selectedOption.dataset.price); 
                    listPriceDisplay.textContent = currencyFormatter.format(basePrice);
                } else {
                    imgProduct.src = "../image/icon_logo/VinFast-logo.svg";
                    basePrice = 0;
                    listPriceDisplay.textContent = "Chưa chọn xe";
                }
                updateCalculations();
            });

        } catch (error) {
            console.error("Lỗi tải dữ liệu xe:", error);
            carSelect.innerHTML = '<option value="">Không thể tải dữ liệu</option>';
        }
    }

    async function fetchBankData() {
        try {
            const response = await fetch("../assets/js/banks.json");
            const banks = await response.json();

            bankSelect.innerHTML = "<option value=''>Chọn ngân hàng</option>";
            banks.forEach(bank => {
                const option = new Option(`${bank.name}`, bank.interestRate);
                bankSelect.add(option);
            });

            bankSelect.addEventListener("change", function () {
                interestRateInput.value = this.value;
                updateCalculations();
            });
            
            interestRateInput.addEventListener('input', updateCalculations);

        } catch (error) {
            console.error("Lỗi tải dữ liệu ngân hàng:", error);
            bankSelect.innerHTML = '<option value="">Không thể tải dữ liệu</option>';
        }
    }
    
    // --- Event Listener cho các lựa chọn giá ---
    priceTypeList.addEventListener("change", function () {
        if (this.checked) {
            customPriceContainer.classList.add("hidden");
            updateCalculations(); 
        }
    });
    
    priceTypeCustom.addEventListener("change", function () {
        if (this.checked) {
            customPriceContainer.classList.remove("hidden");
            updateCalculations(); 
        }
    });

    // THÊM MỚI: Event Listener cho định dạng input
    customPriceInput.addEventListener("input", function() {
        formatCurrencyInput(this); // Định dạng khi gõ
        updateCalculations(); // Cập nhật tính toán ngay
    });
    
    batterySelect.addEventListener("change", function () {
        batteryPrice = this.value === "include" ? BATTERY_COST : 0;
        updateCalculations();
    });
    
    // CẬP NHẬT: Hàm tính toán
    function updateCalculations() {
        showScheduleBtn.classList.add('hidden');

        let currentBasePrice = 0; 
        if (priceTypeCustom.checked) { 
            // CẬP NHẬT: Dùng hàm parseFormattedNumber để đọc giá trị
            currentBasePrice = parseFormattedNumber(customPriceInput.value);
        } else { 
            currentBasePrice = basePrice; 
        }

        if (currentBasePrice === 0) {
            carPriceEl.textContent = currencyFormatter.format(0);
            downPaymentEl.textContent = currencyFormatter.format(0);
            loanAmountEl.textContent = currencyFormatter.format(0);
            interestRateDisplay.textContent = '0%';
            interestAmountEl.textContent = currencyFormatter.format(0);
            totalLoanInterestEl.textContent = currencyFormatter.format(0);
            return;
        }
        
        const totalCarPrice = currentBasePrice + batteryPrice; 
        const interestRate = parseFloat(interestRateInput.value) / 100 || 0;
        const downPayment = totalCarPrice * selectedDownPaymentPercent;
        const loanAmount = totalCarPrice - downPayment;
        
        if (loanAmount > 0 && interestRate > 0) {
             showScheduleBtn.classList.remove('hidden');
        }
        
        const totalInterest = loanAmount * interestRate * selectedLoanDuration;

        carPriceEl.textContent = currencyFormatter.format(totalCarPrice);
        downPaymentEl.textContent = currencyFormatter.format(downPayment);
        loanAmountEl.textContent = currencyFormatter.format(loanAmount);
        interestRateDisplay.textContent = `${(interestRate * 100).toFixed(1)}%`;
        interestAmountEl.textContent = currencyFormatter.format(totalInterest);
        totalLoanInterestEl.textContent = currencyFormatter.format(loanAmount + totalInterest);
    }
    
    function openModal() {
        generateRepaymentSchedule();
        scheduleModal.classList.remove('hidden');
        document.body.style.overflow = 'hidden';
    }

    function closeModal() {
        scheduleModal.classList.add('hidden');
        document.body.style.overflow = '';
    }

    // CẬP NHẬT: Hàm tạo lịch trả nợ
    function generateRepaymentSchedule() {
        scheduleData = [];
        scheduleBody.innerHTML = '';
        
        let currentBasePrice = 0;
        if (priceTypeCustom.checked) { 
            // CẬP NHẬT: Dùng hàm parseFormattedNumber để đọc giá trị
            currentBasePrice = parseFormattedNumber(customPriceInput.value);
        } else {
            currentBasePrice = basePrice;
        }

        const totalCarPrice = currentBasePrice + batteryPrice; 
        const loanAmount = totalCarPrice * (1 - selectedDownPaymentPercent);
        const annualRate = parseFloat(interestRateInput.value) / 100 || 0;
        const totalMonths = selectedLoanDuration * 12;
        const monthlyPrincipal = loanAmount / totalMonths;
        
        const monthlyRate = annualRate / 12;
        
        let openingBalance = loanAmount;
        let totalInterestPaid = 0; 

        for(let i = 1; i <= totalMonths; i++) {
            const monthlyInterest = openingBalance * monthlyRate;
            totalInterestPaid += monthlyInterest; 
            const totalPayment = monthlyPrincipal + monthlyInterest;
            const closingBalance = openingBalance - monthlyPrincipal;

            const rowData = {
                term: i,
                openingBalance: Math.round(openingBalance),
                principal: Math.round(monthlyPrincipal),
                interest: Math.round(monthlyInterest),
                total: Math.round(totalPayment),
                closingBalance: Math.round(closingBalance < 0 ? 0 : closingBalance)
            };
            scheduleData.push(rowData);

            const row = `
                <tr>
                    <td>${rowData.term}</td>
                    <td>${currencyFormatter.format(rowData.openingBalance)}</td>
                    <td>${currencyFormatter.format(rowData.principal)}</td>
                    <td>${currencyFormatter.format(rowData.interest)}</td>
                    <td class="font-semibold">${currencyFormatter.format(rowData.total)}</td>
                    <td>${currencyFormatter.format(rowData.closingBalance)}</td>
                </tr>
            `;
            scheduleBody.innerHTML += row;
            
            openingBalance = closingBalance;
        }
        
        interestAmountEl.textContent = currencyFormatter.format(totalInterestPaid);
        totalLoanInterestEl.textContent = currencyFormatter.format(loanAmount + totalInterestPaid);
    }

    function downloadCSV() {
        const headers = ["Ky han", "Du no dau ky", "Goc phai tra", "Lai phai tra", "Goc + Lai", "Du no cuoi ky"];
        let csvContent = "data:text/csv;charset=utf-8," + headers.join(",") + "\n";

        scheduleData.forEach(row => {
            const values = Object.values(row).join(",");
            csvContent += values + "\n";
        });

        const encodedUri = encodeURI(csvContent);
        const link = document.createElement("a");
        link.setAttribute("href", encodedUri);
        link.setAttribute("download", "lich_tra_gop_chi_tiet.csv");
        document.body.appendChild(link);
        link.click();
        document.body.removeChild(link);
    }
    
    showScheduleBtn.addEventListener('click', openModal);
    closeModalBtn.addEventListener('click', closeModal);
    scheduleModalOverlay.addEventListener('click', closeModal);
    downloadCsvBtn.addEventListener('click', downloadCSV);

    function handleButtonGroup(container, callback) {
        if (!container) return;
        const buttons = container.querySelectorAll('button');
        buttons.forEach(button => {
            button.addEventListener('click', function() {
                buttons.forEach(btn => btn.classList.remove('bg-blue-600', 'text-white'));
                this.classList.add('bg-blue-600', 'text-white');
                callback(this.dataset.value);
                updateCalculations();
            });
        });
        if(buttons.length > 0) {
           buttons[0].click();
        }
    }

    handleButtonGroup(loanDurationButtonsContainer, (value) => {
        selectedLoanDuration = parseInt(value, 10);
    });
    
    handleButtonGroup(downPaymentButtonsContainer, (value) => {
        selectedDownPaymentPercent = parseFloat(value);
    });

    fetchCarData();
    fetchBankData();
});