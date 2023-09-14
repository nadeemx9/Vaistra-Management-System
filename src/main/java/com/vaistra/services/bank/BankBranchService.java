package com.vaistra.services.bank;

import com.vaistra.dto.HttpResponse;
import com.vaistra.dto.bank.BankBranchDto;

public interface BankBranchService {

    BankBranchDto addBankBranch(BankBranchDto bankBranchDto);
    BankBranchDto getBankBranchById(int bankBranchId);
    HttpResponse searchBankBranchByKeyword(String keyword,int pageNumber, int pageSize, String sortBy, String sortDirection);
    HttpResponse getBankBranchesByBankId(int bankId,int pageNumber, int pageSize, String sortBy, String sortDirection);
    HttpResponse getBankBranchesByStateId(int stateId,int pageNumber, int pageSize, String sortBy, String sortDirection);
    HttpResponse getBankBranchesByDistrictId(int districtId,int pageNumber, int pageSize, String sortBy, String sortDirection);
    HttpResponse getAllBankBranches(int pageNumber, int pageSize, String sortBy, String sortDirection);
    HttpResponse getAllActiveBankBranches(int pageNumber, int pageSize, String sortBy, String sortDirection);
    HttpResponse getAlLBankBranchesByActiveBank(int pageNumber, int pageSize, String sortBy, String sortDirection);
    BankBranchDto updateBankBranch(BankBranchDto bankBranchDto, int bankBranchId);
    String deleteBankBranch(int bankBranchId);
}
