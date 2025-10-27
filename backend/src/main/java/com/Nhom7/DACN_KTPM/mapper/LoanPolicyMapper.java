package com.Nhom7.DACN_KTPM.mapper;

import com.Nhom7.DACN_KTPM.dto.request.LoanPolicyCreationRequest;
import com.Nhom7.DACN_KTPM.dto.request.LoanPolicyUpdateRequest;
import com.Nhom7.DACN_KTPM.dto.response.LoanPolicyResponse;
import com.Nhom7.DACN_KTPM.entity.LoanPolicy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface LoanPolicyMapper {

    @Mapping(source = "bank.id", target = "bankId")
    @Mapping(source = "bank.name", target = "bankName")
    LoanPolicyResponse toLoanPolicyResponse(LoanPolicy loanPolicy);

    List<LoanPolicyResponse> toLoanPolicyResponseList(List<LoanPolicy> loanPolicies);

    // Map Creation Request to Entity (ignore ID and Bank object)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "bank", ignore = true)
    LoanPolicy toLoanPolicy(LoanPolicyCreationRequest request);

    // Update existing Entity from Update Request DTO (ignore ID and Bank object)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "bank", ignore = true)
    void updateLoanPolicy(@MappingTarget LoanPolicy loanPolicy, LoanPolicyUpdateRequest request);
}