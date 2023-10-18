package com.vaistra.services.bank.impl;

import com.vaistra.dto.HttpResponse;
import com.vaistra.dto.bank.BankBranchDto;
import com.vaistra.dto.bank.BankBranchUpdateDto;
import com.vaistra.bankdb.entities.Bank;
import com.vaistra.bankdb.entities.BankBranch;
import com.vaistra.cscvdb.entities.District;
import com.vaistra.cscvdb.entities.State;
import com.vaistra.exception.DuplicateEntryException;
import com.vaistra.exception.InactiveStatusException;
import com.vaistra.exception.ResourceNotFoundException;
import com.vaistra.bankdb.repositories.BankBranchRepository;
import com.vaistra.bankdb.repositories.BankRepository;
import com.vaistra.cscvdb.repositories.DistrictRepository;
import com.vaistra.cscvdb.repositories.StateRepository;
import com.vaistra.services.bank.BankBranchService;
import com.vaistra.utils.AppUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class BankBranchServiceImpl implements BankBranchService {

    private final BankBranchRepository bankBranchRepository;
    private final BankRepository bankRepository;
    private final StateRepository stateRepository;
    private final DistrictRepository districtRepository;
    private final AppUtils appUtils;

    @Autowired
    public BankBranchServiceImpl(BankBranchRepository bankBranchRepository, BankRepository bankRepository, StateRepository stateRepository, DistrictRepository districtRepository, AppUtils appUtils) {
        this.bankBranchRepository = bankBranchRepository;
        this.bankRepository = bankRepository;
        this.stateRepository = stateRepository;
        this.districtRepository = districtRepository;
        this.appUtils = appUtils;
    }

    @Override
    @Transactional
    public BankBranchDto addBankBranch(BankBranchDto bankBranchDto) {
        try {
            bankBranchDto.setBranchName(bankBranchDto.getBranchName().trim());
            bankBranchDto.setBranchCode(bankBranchDto.getBranchCode().trim());
            bankBranchDto.setBranchAddress(bankBranchDto.getBranchAddress().trim());
            bankBranchDto.setBranchIfsc(bankBranchDto.getBranchIfsc().trim());
            bankBranchDto.setBranchPhoneNumber(bankBranchDto.getBranchPhoneNumber().trim());
            bankBranchDto.setBranchMicr(bankBranchDto.getBranchMicr().trim());
        }catch (Exception e)
        {

        }

        if(bankBranchRepository.existsByBranchNameIgnoreCase(bankBranchDto.getBranchName()))
            throw new DuplicateEntryException("Bank Branch name '"+bankBranchDto.getBranchAddress()+"' already exist!");

        if(bankBranchRepository.existsByBranchCodeIgnoreCase(bankBranchDto.getBranchCode()))
            throw new DuplicateEntryException("Bank Branch code '"+bankBranchDto.getBranchCode()+"' already exist!");

        if(bankBranchRepository.existsByBranchIfscIgnoreCase(bankBranchDto.getBranchIfsc()))
            throw new DuplicateEntryException("Bank Branch IFSC '"+bankBranchDto.getBranchIfsc()+"' already exist!");

        if(bankBranchRepository.existsByBranchMicrIgnoreCase(bankBranchDto.getBranchMicr()))
            throw new DuplicateEntryException("MICR '"+bankBranchDto.getBranchMicr()+"' already exist!");

        Bank bank = bankRepository.findById(bankBranchDto.getBankId())
                .orElseThrow(()->new ResourceNotFoundException("Bank with ID '"+bankBranchDto.getBankId()+"' not found!"));
        if(!bank.getStatus())
            throw new InactiveStatusException("Bank '"+bank.getBankLongName()+"' is inactive!");

        District district = districtRepository.findById(bankBranchDto.getDistrictId())
                .orElseThrow(()->new ResourceNotFoundException("District with ID '"+bankBranchDto.getDistrictId()+"' not found!"));
        if(!district.getStatus())
            throw new InactiveStatusException("District '"+district.getDistrictName()+"' is inactive");

        State state = stateRepository.findById(district.getState().getStateId())
                .orElseThrow(()->new ResourceNotFoundException("State with ID '"+district.getState().getStateId()+"' not found!"));

        BankBranch bankBranch = new BankBranch();
        bankBranch.setBranchName(bankBranchDto.getBranchName());
        bankBranch.setBranchCode(bankBranchDto.getBranchCode());
        bankBranch.setBranchAddress(bankBranchDto.getBranchAddress().trim());
        bankBranch.setBranchIfsc(bankBranchDto.getBranchIfsc());
        bankBranch.setBranchPhoneNumber(bankBranchDto.getBranchPhoneNumber());
        bankBranch.setBranchMicr(bankBranchDto.getBranchMicr());
        bankBranch.setFromTiming(bankBranchDto.getFromTiming());
        bankBranch.setToTiming(bankBranchDto.getToTiming());
        bankBranch.setBank(bank);
        bankBranch.setState(state);
        bankBranch.setDistrict(district);

        if(bankBranch.getStatus() == null)
            bankBranch.setStatus(true);
        else
            bankBranch.setStatus(bankBranchDto.getStatus());

        return appUtils.bankBranchToDto(bankBranchRepository.save(bankBranch));
    }

    @Override
    @Transactional
    public BankBranchDto getBankBranchById(int bankBranchId) {
        return appUtils.bankBranchToDto(bankBranchRepository.findById(bankBranchId)
                .orElseThrow(()->new ResourceNotFoundException("Bank Branch with ID '"+bankBranchId+"' not found!")));
    }

    @Override
    @Transactional
    public HttpResponse searchBankBranchByKeyword(String keyword, int pageNumber, int pageSize, String sortBy, String sortDirection) {

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
        Page<BankBranch> pageBankBranch = bankBranchRepository.findAllByBranchIdOrStatusOrBranchNameContainingIgnoreCaseOrBranchCodeContainingIgnoreCaseOrBranchAddressContainingIgnoreCaseOrBranchIfscContainingIgnoreCaseOrBranchMicrContainingIgnoreCaseOrBank_BankLongNameContainingIgnoreCaseOrState_StateNameContainingIgnoreCaseOrDistrict_DistrictNameContainingIgnoreCase
                (integerKeyword, booleanKeyword,keyword ,keyword, keyword, keyword, keyword, keyword, keyword, keyword, pageable);
        List<BankBranchDto> bankBranches = appUtils.bankBranchesToDtos(pageBankBranch.getContent());

        return HttpResponse.builder()
                .pageNumber(pageBankBranch.getNumber())
                .pageSize(pageBankBranch.getSize())
                .totalElements(pageBankBranch.getTotalElements())
                .totalPages(pageBankBranch.getTotalPages())
                .isLastPage(pageBankBranch.isLast())
                .data(bankBranches)
                .build();
    }

    @Override
    @Transactional
    public HttpResponse getBankBranchesByBankId(int bankId, int pageNumber, int pageSize, String sortBy, String sortDirection) {

        Bank bank = bankRepository.findById(bankId)
                .orElseThrow(()->new ResourceNotFoundException("Bank with ID'"+bankId+"' not found!"));

        Sort sort = (sortDirection.equalsIgnoreCase("asc")) ?
                Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
        Page<BankBranch> pageBankBranch = bankBranchRepository.findAllByBank_BankId(bankId, pageable);
        List<BankBranchDto> bankBranches = appUtils.bankBranchesToDtos(pageBankBranch.getContent());

        return HttpResponse.builder()
                .pageNumber(pageBankBranch.getNumber())
                .pageSize(pageBankBranch.getSize())
                .totalElements(pageBankBranch.getTotalElements())
                .totalPages(pageBankBranch.getTotalPages())
                .isLastPage(pageBankBranch.isLast())
                .data(bankBranches)
                .build();
    }

    @Override
    @Transactional
    public HttpResponse getBankBranchesByStateId(int stateId, int pageNumber, int pageSize, String sortBy, String sortDirection) {

        State state = stateRepository.findById(stateId)
                .orElseThrow(()->new ResourceNotFoundException("State with ID '"+stateId+"' not found!"));

        Sort sort = (sortDirection.equalsIgnoreCase("asc")) ?
                Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
        Page<BankBranch> pageBankBranch = bankBranchRepository.findAllByState(state, pageable);
        List<BankBranchDto> bankBranches = appUtils.bankBranchesToDtos(pageBankBranch.getContent());

        return HttpResponse.builder()
                .pageNumber(pageBankBranch.getNumber())
                .pageSize(pageBankBranch.getSize())
                .totalElements(pageBankBranch.getTotalElements())
                .totalPages(pageBankBranch.getTotalPages())
                .isLastPage(pageBankBranch.isLast())
                .data(bankBranches)
                .build();
    }

    @Override
    @Transactional
    public HttpResponse getBankBranchesByDistrictId(int districtId, int pageNumber, int pageSize, String sortBy, String sortDirection) {

        District district = districtRepository.findById(districtId)
                .orElseThrow(()->new ResourceNotFoundException("District with ID '"+districtId+"' not found!"));

        Sort sort = (sortDirection.equalsIgnoreCase("asc")) ?
                Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
        Page<BankBranch> pageBankBranch = bankBranchRepository.findAllByDistrict_DistrictId(districtId, pageable);
        List<BankBranchDto> bankBranches = appUtils.bankBranchesToDtos(pageBankBranch.getContent());

        return HttpResponse.builder()
                .pageNumber(pageBankBranch.getNumber())
                .pageSize(pageBankBranch.getSize())
                .totalElements(pageBankBranch.getTotalElements())
                .totalPages(pageBankBranch.getTotalPages())
                .isLastPage(pageBankBranch.isLast())
                .data(bankBranches)
                .build();
    }

    @Override
    @Transactional
    public HttpResponse getAllBankBranches(int pageNumber, int pageSize, String sortBy, String sortDirection) {
        Sort sort = (sortDirection.equalsIgnoreCase("asc")) ?
                Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
        Page<BankBranch> pageBankBranch = bankBranchRepository.findAll(pageable);
        List<BankBranchDto> bankBranches = appUtils.bankBranchesToDtos(pageBankBranch.getContent());

        return HttpResponse.builder()
                .pageNumber(pageBankBranch.getNumber())
                .pageSize(pageBankBranch.getSize())
                .totalElements(pageBankBranch.getTotalElements())
                .totalPages(pageBankBranch.getTotalPages())
                .isLastPage(pageBankBranch.isLast())
                .data(bankBranches)
                .build();
    }

    @Override
    @Transactional
    public HttpResponse getAllActiveBankBranches(int pageNumber, int pageSize, String sortBy, String sortDirection) {
        Sort sort = (sortDirection.equalsIgnoreCase("asc")) ?
                Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
        Page<BankBranch> pageBankBranch = bankBranchRepository.findAllByStatus(true, pageable);
        List<BankBranchDto> bankBranches = appUtils.bankBranchesToDtos(pageBankBranch.getContent());

        return HttpResponse.builder()
                .pageNumber(pageBankBranch.getNumber())
                .pageSize(pageBankBranch.getSize())
                .totalElements(pageBankBranch.getTotalElements())
                .totalPages(pageBankBranch.getTotalPages())
                .isLastPage(pageBankBranch.isLast())
                .data(bankBranches)
                .build();
    }

    @Override
    @Transactional
    public HttpResponse getAlLBankBranchesByActiveBank(int pageNumber, int pageSize, String sortBy, String sortDirection) {
        Sort sort = (sortDirection.equalsIgnoreCase("asc")) ?
                Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
        Page<BankBranch> pageBankBranch = bankBranchRepository.findAllByBank_Status(true, pageable);
        List<BankBranchDto> bankBranches = appUtils.bankBranchesToDtos(pageBankBranch.getContent());

        return HttpResponse.builder()
                .pageNumber(pageBankBranch.getNumber())
                .pageSize(pageBankBranch.getSize())
                .totalElements(pageBankBranch.getTotalElements())
                .totalPages(pageBankBranch.getTotalPages())
                .isLastPage(pageBankBranch.isLast())
                .data(bankBranches)
                .build();
    }

    @Override
    @Transactional
    public BankBranchDto updateBankBranch(BankBranchUpdateDto bankBranchDto, int bankBranchId) {

        BankBranch bankBranch = bankBranchRepository.findById(bankBranchId)
                .orElseThrow(()->new ResourceNotFoundException("Bank Branch with "));

        try {
            bankBranchDto.setBranchName(bankBranchDto.getBranchName().trim());
            bankBranchDto.setBranchCode(bankBranchDto.getBranchCode().trim());
            bankBranchDto.setBranchAddress(bankBranchDto.getBranchAddress().trim());
            bankBranchDto.setBranchIfsc(bankBranchDto.getBranchIfsc().trim());
            bankBranchDto.setBranchPhoneNumber(bankBranchDto.getBranchPhoneNumber().trim());
            bankBranchDto.setBranchMicr(bankBranchDto.getBranchMicr().trim());
        }catch (Exception e)
        {

        }

        if(bankBranchDto.getBranchName() != null)
        {
            BankBranch branchWithSameName = bankBranchRepository.findByBranchNameIgnoreCase(bankBranchDto.getBranchName());
            if(branchWithSameName != null && !branchWithSameName.getBranchId().equals(bankBranch.getBranchId()))
                throw new DuplicateEntryException("Branch '"+bankBranchDto.getBranchName()+"' already exist!");

            bankBranch.setBranchName(bankBranchDto.getBranchName());
        }

        if(bankBranchDto.getBranchCode() != null)
        {
            BankBranch branchWithSameCode = bankBranchRepository.findByBranchCodeIgnoreCase(bankBranchDto.getBranchCode());
            if(branchWithSameCode != null && !branchWithSameCode.getBranchId().equals(bankBranch.getBranchId()))
                throw new DuplicateEntryException("Branch Code '"+bankBranchDto.getBranchCode()+"' already exist!");

            bankBranch.setBranchCode(bankBranchDto.getBranchCode());
        }

        if(bankBranchDto.getBranchAddress() != null)
            bankBranch.setBranchAddress(bankBranchDto.getBranchAddress());

        if(bankBranchDto.getBranchIfsc() != null)
        {
            BankBranch branchWithSameIfsc = bankBranchRepository.findByBranchIfscIgnoreCase(bankBranchDto.getBranchIfsc());
            if(branchWithSameIfsc != null && !branchWithSameIfsc.getBranchId().equals(bankBranch.getBranchId()))
                throw new DuplicateEntryException("Branch IFSC '"+bankBranchDto.getBranchIfsc()+"' already exist!");

            bankBranch.setBranchIfsc(bankBranchDto.getBranchIfsc());
        }

        if(bankBranchDto.getBranchPhoneNumber() != null)
            bankBranch.setBranchPhoneNumber(bankBranchDto.getBranchPhoneNumber());

        if(bankBranchDto.getBranchMicr() != null)
        {
            BankBranch branchWithSameMicr = bankBranchRepository.findByBranchMicrIgnoreCase(bankBranchDto.getBranchMicr());
            if(branchWithSameMicr != null && !branchWithSameMicr.getBranchId().equals(bankBranch.getBranchId()))
                throw new DuplicateEntryException("Branch MICR '"+bankBranchDto.getBranchMicr()+"' already exist!");

            bankBranch.setBranchMicr(bankBranchDto.getBranchMicr());
        }

        if(bankBranchDto.getFromTiming() != null)
            bankBranch.setFromTiming(bankBranchDto.getFromTiming());

        if(bankBranchDto.getToTiming() != null)
            bankBranch.setToTiming(bankBranchDto.getToTiming());

        if(bankBranchDto.getBankId() != null)
        {
            Bank bank = bankRepository.findById(bankBranchDto.getBankId())
                    .orElseThrow(()->new ResourceNotFoundException("Bank with ID '"+bankBranchDto.getBankId()+"' not found!"));
            bankBranch.setBank(bank);
        }

        if(bankBranchDto.getDistrictId() != null)
        {
            District district = districtRepository.findById(bankBranchDto.getDistrictId())
                    .orElseThrow(()->new ResourceNotFoundException("District with ID '"+bankBranchDto.getDistrictId()+"' not found!"));
            bankBranch.setDistrict(district);

            State state = stateRepository.findById(district.getState().getStateId())
                    .orElseThrow(()->new ResourceNotFoundException("State with ID '"+district.getState().getStateId()+"' not found!"));
            bankBranch.setState(state);
        }

        return appUtils.bankBranchToDto(bankBranchRepository.save(bankBranch));
    }

    @Override
    @Transactional
    public String deleteBankBranch(int bankBranchId) {

        BankBranch bankBranch = bankBranchRepository.findById(bankBranchId)
                .orElseThrow(()->new ResourceNotFoundException("Bank Branch with "));

        bankBranchRepository.delete(bankBranch);
        return "Bank Branch '"+bankBranch.getBranchName()+"' deleted!";
    }
}
