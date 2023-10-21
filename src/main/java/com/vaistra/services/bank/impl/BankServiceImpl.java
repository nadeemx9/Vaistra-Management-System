package com.vaistra.services.bank.impl;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.vaistra.dto.HttpResponse;

import com.vaistra.dto.bank.BankDto;
import com.vaistra.dto.bank.BankUpdateDto;
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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

@Service
public class BankServiceImpl implements BankService {

    private final BankRepository bankRepository;
    private final AppUtils appUtils;
    private final Cloudinary cloudinary;
    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyyMMdd");
    private final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HHmmss");
    private final String publicId = "Vaistra-Management-System/";

    @Autowired
    public BankServiceImpl(BankRepository bankRepository, AppUtils appUtils, Cloudinary cloudinary) {
        this.bankRepository = bankRepository;
        this.appUtils = appUtils;
        this.cloudinary = cloudinary;
    }

    @Override
    @Transactional
    public BankDto addBank(BankDto bankDto, MultipartFile file) throws IOException {

        if(bankRepository.existsByBankLongNameIgnoreCase(bankDto.getBankLongName()))
            throw new DuplicateEntryException("Bank '"+bankDto.getBankLongName()+"' already exist!");
//        if(file==null && file.isEmpty())
//            throw new ResourceNotFoundException("File should not be empty!");

        if(file!=null && !appUtils.isSupportedExtension(file.getOriginalFilename()))
            throw new ResourceNotFoundException("Only JPG,PNG and JPEG File is Accepted");

        Bank bank = new Bank();
        bank.setBankShortName(bankDto.getBankShortName().trim());
        bank.setBankLongName(bankDto.getBankLongName().trim());

        if(file != null){
            if (!file.isEmpty()){
                String nm = file.getOriginalFilename().split("\\.")[0];
                String ext = file.getOriginalFilename().split("\\.")[1];

                String fileName = LocalDate.now().format(dateFormatter) + "_" + LocalTime.now().format(timeFormatter)+ "_NOTE_" + nm;

                Map<String, Object> uploadOption = ObjectUtils.asMap(
                        "public_id", bank.getBankShortName() +"/"+fileName,
                        "folder", "Vaistra-Management-System"
                );
                Map result = cloudinary.uploader().upload(file.getBytes(), uploadOption);

                bank.setBankLogo(fileName + "." + ext);
            }
        }

        if(bankDto.getStatus() == null)
            bank.setStatus(true);
        else
            bank.setStatus(bankDto.getStatus());

        return appUtils.bankToDto(bankRepository.save(bank));
    }

    @Override
    @Transactional
    public BankDto getBankById(int bankId) {
        return appUtils.bankToDto(bankRepository.findById(bankId)
                .orElseThrow(()->new ResourceNotFoundException("Bank with ID '"+bankId+"' not found!")));
    }

    @Override
    @Transactional
    public byte[] getBankLogo(int bankId) {

//        Bank bank = bankRepository.findById(bankId)
//                .orElseThrow(()->new ResourceNotFoundException("Bank with ID '"+bankId+"' not found!"));
//
//        return bank.getBankLogo();
        return null;
    }

    @Override
    @Transactional
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
    @Transactional
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
    @Transactional
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
    @Transactional
    public BankDto updateBank(BankUpdateDto bankDto, int bankId, MultipartFile file) throws IOException {

        Bank bank = bankRepository.findById(bankId)
                .orElseThrow(()->new ResourceNotFoundException("Bank with ID '"+bankId+"' not found!"));

        if(bankDto!=null) {
            if (bankDto.getBankShortName() != null)
                bank.setBankShortName(bankDto.getBankShortName().trim());

            if (bankDto.getBankLongName() != null) {
                Bank bankWithSameName = bankRepository.findByBankLongNameIgnoreCase(bank.getBankLongName().trim());
                if (bankWithSameName != null && !bankWithSameName.getBankId().equals(bank.getBankId()))
                    throw new DuplicateEntryException("Bank '" + bankDto.getBankLongName() + "' already exist!");

                bank.setBankLongName(bankDto.getBankLongName().trim());
            }

            if (bankDto.getStatus() != null)
                bank.setStatus(bankDto.getStatus());
        }


        if(file != null) {
            if (!file.isEmpty()) {
                if (!appUtils.isSupportedExtension(file.getOriginalFilename()))
                    throw new ResourceNotFoundException("Only JPG,PNG and JPEG File is Accepted");
                if(bank.getBankLogo()!=null){
                    String destroy = publicId + bank.getBankShortName() + "/" + bank.getBankLogo().split("\\.")[0];
                    cloudinary.uploader().destroy(destroy,ObjectUtils.emptyMap());
                }
                String nm = file.getOriginalFilename().split("\\.")[0];
                String ext = file.getOriginalFilename().split("\\.")[1];

                String fileName = LocalDate.now().format(dateFormatter) + "_" + LocalTime.now().format(timeFormatter)+ "_USER_" + nm;

                Map<String, Object> uploadOption = ObjectUtils.asMap(
                        "public_id", bank.getBankShortName() +"/"+fileName,
                        "folder", "Vaistra-Management-System"
                );
                cloudinary.uploader().upload(file.getBytes(), uploadOption);

                bank.setBankLogo(fileName + "." + ext);
            }
        }


        return appUtils.bankToDto(bankRepository.save(bank));
    }

    @Override
    public String deleteBank(int bankId) throws IOException {
        Bank bank = bankRepository.findById(bankId)
                .orElseThrow(()->new ResourceNotFoundException("Bank with ID '"+bankId+"' not found!"));

        if(bank.getBankLogo()!=null){
            String destroy = publicId + bank.getBankShortName() + "/" + bank.getBankLogo().split("\\.")[0];
            cloudinary.uploader().destroy(destroy,ObjectUtils.emptyMap());
        }

        bankRepository.delete(bank);
        return "Bank '"+bank.getBankLongName()+"' deleted!";
    }
}
