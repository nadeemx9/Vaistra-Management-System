package com.vaistra.services.bank;

import com.vaistra.dto.HttpResponse;
import com.vaistra.dto.bank.BankDto;
import com.vaistra.dto.bank.BankUpdateDto;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface BankService {
    BankDto addBank(BankDto bankDto, MultipartFile file) throws IOException;
    BankDto getBankById(int bankId);
    byte[] getBankLogo(int bankId);
    HttpResponse searchBanksByKeyword(String keyword ,int pageNumber, int pageSize, String sortBy, String sortDirection);
    HttpResponse getAlLBanks(int pageNumber, int pageSize, String sortBy, String sortDirection);
    HttpResponse getAlLActiveBanks(int pageNumber, int pageSize, String sortBy, String sortDirection);
    BankDto updateBank(BankUpdateDto bankDto, int bankId, MultipartFile file) throws IOException;
    String deleteBank(int bankId);
}
