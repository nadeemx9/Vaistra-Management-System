package com.vaistra.repositories.bank;

import com.vaistra.entities.bank.BankBranch;
import com.vaistra.entities.cscv.State;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BankBranchRepository extends JpaRepository<BankBranch, Integer> {

    Boolean existsByBranchNameIgnoreCase(String branchName);
    Boolean existsByBranchCodeIgnoreCase(String branchCode);
    Boolean existsByBranchIfscIgnoreCase(String branchIFSC);
    Boolean existsByBranchMicrIgnoreCase(String branchMICR);

    Page<BankBranch> findAllByBranchIdOrStatusOrBranchNameContainingIgnoreCaseOrBranchCodeContainingIgnoreCaseOrBranchAddressContainingIgnoreCaseOrBranchIfscContainingIgnoreCaseOrBranchMicrContainingIgnoreCaseOrBank_BankLongNameContainingIgnoreCaseOrState_StateNameContainingIgnoreCaseOrDistrict_DistrictNameContainingIgnoreCase
            (Integer branchId, Boolean status, String branchName, String branchCode, String branchAddress, String branchIfsc, String branchMicr, String bankName, String stateName, String districtName, Pageable p);

    Page<BankBranch> findAllByBank_BankId(int bankId, Pageable p);
    Page<BankBranch> findAllByState(State state, Pageable p);
    Page<BankBranch> findAllByDistrict_DistrictId(int districtId, Pageable p);
    Page<BankBranch> findAllByStatus(Boolean b, Pageable p);
    Page<BankBranch> findAllByBank_Status(Boolean b, Pageable p);

    BankBranch findByBranchNameIgnoreCase(String name);
    BankBranch findByBranchCodeIgnoreCase(String name);
    BankBranch findByBranchIfscIgnoreCase(String name);
    BankBranch findByBranchMicrIgnoreCase(String name);


}
