package com.vaistra.services.bank.impl;

import com.vaistra.dto.HttpResponse;

import com.vaistra.dto.bank.BankDto;
import com.vaistra.entities.bank.Bank;

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
    public BankDto addBank(BankDto bankDto) {
        return null;
    }

    @Override
    public BankDto getBankById(int bankId) {
        return appUtils.bankToDto(bankRepository.findById(bankId)
                .orElseThrow(()->new ResourceNotFoundException("Bank with ID '"+bankId+"' not found!")));
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
    public BankDto updateBank(BankDto bankDto, int bankId) {
        return null;
    }

    @Override
    public String deleteBank(int bankId) {
        Bank bank = bankRepository.findById(bankId)
                .orElseThrow(()->new ResourceNotFoundException("Bank with ID '"+bankId+"' not found!"));

        bankRepository.delete(bank);
        return "Bank '"+bank.getBankLongName()+"' deleted!";
    }
}
