package com.Nhom7.DACN_KTPM.controller;

import com.Nhom7.DACN_KTPM.dto.request.ContractCreationRequest;
import com.Nhom7.DACN_KTPM.dto.response.ApiResponse;
import com.Nhom7.DACN_KTPM.entity.Contract;
import com.Nhom7.DACN_KTPM.service.ContractService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/contracts")
@RequiredArgsConstructor
public class ContractController {

    private final ContractService contractService;

    @PostMapping
    public ApiResponse<Contract> createContract(@RequestBody ContractCreationRequest request) {
        return ApiResponse.<Contract>builder()
                .code(1000)
                .result(contractService.createContract(request))
                .build();
    }
}