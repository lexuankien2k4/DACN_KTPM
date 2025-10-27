package com.Nhom7.DACN_KTPM.service;

import com.Nhom7.DACN_KTPM.dto.request.CarCategoryCreationRequest;
import com.Nhom7.DACN_KTPM.dto.request.CarCategoryUpdateRequest;
import com.Nhom7.DACN_KTPM.dto.response.CarCategoryResponse;
import com.Nhom7.DACN_KTPM.entity.CarCategory;
import com.Nhom7.DACN_KTPM.exception.AppException;
import com.Nhom7.DACN_KTPM.exception.ErrorCode;
import com.Nhom7.DACN_KTPM.mapper.CarCategoryMapper;
import com.Nhom7.DACN_KTPM.repository.CarCategoryRepository;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class CarCategoryService {

    CarCategoryRepository carCategoryRepository;
    CarCategoryMapper carCategoryMapper;

    // @PreAuthorize("hasRole('ADMIN')")
    public CarCategoryResponse createCategory(CarCategoryCreationRequest request) {
        log.info("Creating new car category: {}", request.getName());
        if (carCategoryRepository.existsByName(request.getName())) {
            throw new AppException(ErrorCode.CATEGORY_EXISTED); // Cần định nghĩa mã lỗi này
        }

        CarCategory category = carCategoryMapper.toCarCategory(request);

        try {
            category = carCategoryRepository.save(category);
        } catch (DataIntegrityViolationException e) {
            log.error("Error creating category - name likely exists: {}", request.getName(), e);
            throw new AppException(ErrorCode.CATEGORY_EXISTED);
        }

        return carCategoryMapper.toCarCategoryResponse(category);
    }


    public List<CarCategoryResponse> getAllCategories() {
        log.info("Fetching all car categories");
        return carCategoryMapper.toCarCategoryResponseList(carCategoryRepository.findAll());
    }


    public CarCategoryResponse getCategory(Integer id) {
        log.info("Fetching car category with ID: {}", id);
        return carCategoryMapper.toCarCategoryResponse(
                carCategoryRepository.findById(id)
                        .orElseThrow(() -> new AppException(ErrorCode.CATEGORY_NOT_FOUND)));
    }

    // @PreAuthorize("hasRole('ADMIN')")
    public CarCategoryResponse updateCategory(Integer id, CarCategoryUpdateRequest request) {
        log.info("Updating car category with ID: {}", id);
        CarCategory category = carCategoryRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.CATEGORY_NOT_FOUND));

        if (!category.getName().equalsIgnoreCase(request.getName()) &&
                carCategoryRepository.existsByNameAndIdNot(request.getName(), id)) {
            throw new AppException(ErrorCode.CATEGORY_EXISTED);
        }

        carCategoryMapper.updateCarCategory(category, request);

        try {
            category = carCategoryRepository.save(category);
        } catch (DataIntegrityViolationException e) {
            log.error("Error updating category ID {} - name likely exists: {}", id, request.getName(), e);
            throw new AppException(ErrorCode.CATEGORY_EXISTED);
        }

        return carCategoryMapper.toCarCategoryResponse(category);
    }


    // @PreAuthorize("hasRole('ADMIN')")
    public void deleteCategory(Integer id) {
        log.info("Deleting car category with ID: {}", id);
        if (!carCategoryRepository.existsById(id)) {
            throw new AppException(ErrorCode.CATEGORY_NOT_FOUND);
        }

        try {
            carCategoryRepository.deleteById(id);
            log.info("Deleted car category with ID: {}", id);
        } catch (DataIntegrityViolationException e){
            log.error("Cannot delete category ID {} as it is currently in use.", id, e);
            throw new AppException(ErrorCode.CATEGORY_IN_USE);
        }
    }
}