package com.vaistra.services.bank;

import com.vaistra.dto.HttpResponse;
import com.vaistra.dto.bank.BankDto;

public interface BankService {

    BankDto addBank(BankDto bankDto);
    BankDto getBankById(int bankId);
    HttpResponse searchBanksByKeyword(String keyword ,int pageNumber, int pageSize, String sortBy, String sortDirection);
    HttpResponse getAlLBanks(int pageNumber, int pageSize, String sortBy, String sortDirection);
    HttpResponse getAlLActiveBanks(int pageNumber, int pageSize, String sortBy, String sortDirection);
    BankDto updateBank(BankDto bankDto, int bankId);
    String deleteBank(int bankId);
}
