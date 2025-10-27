package com.Nhom7.DACN_KTPM.mapper;

import com.Nhom7.DACN_KTPM.dto.request.BankCreationRequest;
import com.Nhom7.DACN_KTPM.dto.request.BankUpdateRequest;
import com.Nhom7.DACN_KTPM.dto.response.BankResponse;
import com.Nhom7.DACN_KTPM.entity.Bank;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface BankMapper {

    // Map Entity to Response DTO
    BankResponse toBankResponse(Bank bank);

    // Map List<Entity> to List<Response DTO>
    List<BankResponse> toBankResponseList(List<Bank> banks);

    // Map Creation Request DTO to Entity (ignore ID and policies)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "loanPolicies", ignore = true) // Ignore the list of policies
    Bank toBank(BankCreationRequest request);

    // Update existing Entity from Update Request DTO (ignore ID and policies)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "loanPolicies", ignore = true)
    void updateBank(@MappingTarget Bank bank, BankUpdateRequest request);
}