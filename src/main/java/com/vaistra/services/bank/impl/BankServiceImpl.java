package com.vaistra.services.bank.impl;

import com.vaistra.dto.HttpResponse;

import com.vaistra.dto.bank.BankDto;
import com.vaistra.entities.bank.Bank;

import com.vaistra.exception.DuplicateEntryException;
import com.vaistra.exception.ResourceNotFoundException;
import com.vaistra.repositories.bank.BankRepository;
import com.vaistra.services.bank.BankService;
import com.vaistra.utils.AppUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class BankServiceImpl implements BankService {

    private final BankRepository bankRepository;
    private final AppUtils appUtils;

    @Autowired
    public BankServiceImpl(BankRepository bankRepository, AppUtils appUtils) {
        this.bankRepository = bankRepository;
        this.appUtils = appUtils;
    }

    @Override
    public BankDto addBank(BankDto bankDto, MultipartFile file) throws IOException {

        if(bankRepository.existsByBankLongNameIgnoreCase(bankDto.getBankLongName()))
            throw new DuplicateEntryException("Bank '"+bankDto.getBankLongName()+"' already exist!");
        if(file.isEmpty())
            throw new ResourceNotFoundException("File should not be empty!");

        if(!appUtils.isSupportedExtension(file.getOriginalFilename()))
            throw new ResourceNotFoundException("Only JPG,PNG and JPEG File is Accepted");

        Bank bank = new Bank();
        bank.setBankShortName(bankDto.getBankShortName().trim());
        bank.setBankLongName(bankDto.getBankLongName().trim());
        bank.setBankLogo(file.getBytes());

        if(bankDto.getStatus() == null)
            bank.setStatus(true);
        else
            bank.setStatus(bankDto.getStatus());

        return appUtils.bankToDto(bankRepository.save(bank));
    }

    @Override
    public BankDto getBankById(int bankId) {
        return appUtils.bankToDto(bankRepository.findById(bankId)
                .orElseThrow(()->new ResourceNotFoundException("Bank with ID '"+bankId+"' not found!")));
    }

    @Override
    public byte[] getBankLogo(int bankId) {

        Bank bank = bankRepository.findById(bankId)
                .orElseThrow(()->new ResourceNotFoundException("Bank with ID '"+bankId+"' not found!"));

        return bank.getBankLogo();
    }

    @Override
    public HttpResponse searchBanksByKeyword(String keyword, int pageNumber, int pageSize, String sortBy, String sortDirection) {
        Integer integerKeyword = null;
        Boolean booleanKeyword = null;

        if(keyword.equalsIgnoreCase("true"))
            booleanKeyword = Boolean.TRUE;
        else if (keyword.equalsIgnoreCase("false"))
            booleanKeyword = Boolean.FALSE;

        try {
            integerKeyword = Integer.parseInt(keyword);
        }catch (Exception e){
        }

        Sort sort = (sortDirection.equalsIgnoreCase("asc")) ?
                Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
        Page<Bank> pageBank = bankRepository.findAllByBankIdOrStatusOrBankShortNameContainingIgnoreCaseOrBankLongNameContainingIgnoreCase
                (integerKeyword, booleanKeyword, keyword, keyword, pageable);
        List<BankDto> banks = appUtils.banksToDtos(pageBank.getContent());

        return HttpResponse.builder()
                .pageNumber(pageBank.getNumber())
                .pageSize(pageBank.getSize())
                .totalElements(pageBank.getTotalElements())
                .totalPages(pageBank.getTotalPages())
                .isLastPage(pageBank.isLast())
                .data(banks)
                .build();
    }

    @Override
    public HttpResponse getAlLBanks(int pageNumber, int pageSize, String sortBy, String sortDirection) {
        Sort sort = (sortDirection.equalsIgnoreCase("asc")) ?
                Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
        Page<Bank> pageBank = bankRepository.findAll(pageable);
        List<BankDto> banks = appUtils.banksToDtos(pageBank.getContent());

        return HttpResponse.builder()
                .pageNumber(pageBank.getNumber())
                .pageSize(pageBank.getSize())
                .totalElements(pageBank.getTotalElements())
                .totalPages(pageBank.getTotalPages())
                .isLastPage(pageBank.isLast())
                .data(banks)
                .build();
    }

    @Override
    public HttpResponse getAlLActiveBanks(int pageNumber, int pageSize, String sortBy, String sortDirection) {
        Sort sort = (sortDirection.equalsIgnoreCase("asc")) ?
                Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
        Page<Bank> pageBank = bankRepository.findAllByStatus(true,pageable);
        List<BankDto> banks = appUtils.banksToDtos(pageBank.getContent());

        return HttpResponse.builder()
                .pageNumber(pageBank.getNumber())
                .pageSize(pageBank.getSize())
                .totalElements(pageBank.getTotalElements())
                .totalPages(pageBank.getTotalPages())
                .isLastPage(pageBank.isLast())
                .data(banks)
                .build();
    }

    @Override
    public BankDto updateBank(BankDto bankDto, int bankId, MultipartFile file) throws IOException {

        Bank bank = bankRepository.findById(bankId)
                .orElseThrow(()->new ResourceNotFoundException("Bank with ID '"+bankId+"' not found!"));

        if(bankDto.getBankShortName() != null)
            bank.setBankShortName(bankDto.getBankShortName().trim());

        if(bankDto.getBankLongName() != null) {
            if (bankRepository.existsByBankLongNameIgnoreCase(bankDto.getBankLongName().trim()))
                throw new DuplicateEntryException("Bank '" + bankDto.getBankLongName() + "' already exist!");

            bank.setBankLongName(bankDto.getBankLongName().trim());
        }

        if(!file.isEmpty()) {
            if (!appUtils.isSupportedExtension(file.getOriginalFilename()))
                throw new ResourceNotFoundException("Only JPG,PNG and JPEG File is Accepted");

            bank.setBankLogo(file.getBytes());
        }

        if(bankDto.getStatus() != null)
            bank.setStatus(bankDto.getStatus());

        return appUtils.bankToDto(bankRepository.save(bank));
    }

    @Override
    public String deleteBank(int bankId) {
        Bank bank = bankRepository.findById(bankId)
                .orElseThrow(()->new ResourceNotFoundException("Bank with ID '"+bankId+"' not found!"));

        bankRepository.delete(bank);
        return "Bank '"+bank.getBankLongName()+"' deleted!";
    }
}
