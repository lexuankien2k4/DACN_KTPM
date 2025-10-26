package com.Nhom7.DACN_KTPM.service;

import com.Nhom7.DACN_KTPM.dto.request.CarModelCreationRequest;
import com.Nhom7.DACN_KTPM.dto.request.CarModelUpdateRequest;
import com.Nhom7.DACN_KTPM.dto.response.CarModelResponse;
import com.Nhom7.DACN_KTPM.entity.CarCategory;
import com.Nhom7.DACN_KTPM.entity.CarModel;
import com.Nhom7.DACN_KTPM.exception.AppException;
import com.Nhom7.DACN_KTPM.exception.ErrorCode;
import com.Nhom7.DACN_KTPM.mapper.CarModelMapper;
import com.Nhom7.DACN_KTPM.repository.CarCategoryRepository;
import com.Nhom7.DACN_KTPM.repository.CarModelRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class CarModelService {

    CarModelRepository carModelRepository;
    CarCategoryRepository carCategoryRepository;
    CarModelMapper carModelMapper;

    @Transactional
    // @PreAuthorize("hasRole('ADMIN')") // Add security if needed
    public CarModelResponse createCarModel(CarModelCreationRequest request) {
        log.info("Creating new car model: {}", request.getName());
        CarModel carModel = carModelMapper.toCarModel(request);

        CarCategory category = carCategoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new AppException(ErrorCode.CATEGORY_NOT_FOUND));
        carModel.setCategory(category);

        try {
            carModel = carModelRepository.save(carModel);
        } catch (DataIntegrityViolationException exception) {
            log.error("Error creating car model - name likely exists: {}", request.getName(), exception);
            throw new AppException(ErrorCode.CAR_MODEL_EXISTED);
        }

        return carModelMapper.toCarModelResponse(carModel);
    }

    @Transactional(readOnly = true)
    public List<CarModelResponse> getAllActiveCarModels() {
        log.info("Fetching all active car models");
        return carModelMapper.toCarModelResponseList(
                carModelRepository.findByIsActiveTrueOrderByNameAsc());
    }

    @Transactional(readOnly = true)
    public CarModelResponse getCarModel(Long id) {
        log.info("Fetching car model with ID: {}", id);
        return carModelMapper.toCarModelResponse(
                carModelRepository.findById(id)
                        .orElseThrow(() -> new AppException(ErrorCode.CAR_MODEL_NOT_FOUND)));
    }

    @Transactional
    // @PreAuthorize("hasRole('ADMIN')")
    public CarModelResponse updateCarModel(Long id, CarModelUpdateRequest request) {
        log.info("Updating car model with ID: {}", id);
        CarModel carModel = carModelRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.CAR_MODEL_NOT_FOUND));

        // Use mapper to update basic fields
        carModelMapper.updateCarModel(carModel, request);

        // Update Category if provided and different
        if (request.getCategoryId() != null &&
                (carModel.getCategory() == null || !carModel.getCategory().getId().equals(request.getCategoryId()))) {
            CarCategory category = carCategoryRepository.findById(request.getCategoryId())
                    .orElseThrow(() -> new AppException(ErrorCode.CATEGORY_NOT_FOUND));
            carModel.setCategory(category);
            log.info("Updated category for car model ID: {}", id);
        }

        try {
            carModel = carModelRepository.save(carModel);
        } catch (DataIntegrityViolationException exception) {
            log.error("Error updating car model ID {} - name likely exists: {}", id, request.getName(), exception);
            throw new AppException(ErrorCode.CAR_MODEL_EXISTED);
        }

        return carModelMapper.toCarModelResponse(carModel);
    }

    @Transactional
    // @PreAuthorize("hasRole('ADMIN')")
    public void deleteCarModel(Long id) {
        log.info("Deleting car model with ID: {}", id);
        if (!carModelRepository.existsById(id)) {
            throw new AppException(ErrorCode.CAR_MODEL_NOT_FOUND);
        }
        carModelRepository.deleteById(id);
        log.info("Deleted car model with ID: {}", id);
    }
    @Transactional(readOnly = true)
    // @PreAuthorize("hasRole('ADMIN')")
    public List<CarModelResponse> getAllCarModel() {
        log.info("Fetching ALL car models (including inactive)");
        return carModelMapper.toCarModelResponseList(carModelRepository.findAll());
    }
}