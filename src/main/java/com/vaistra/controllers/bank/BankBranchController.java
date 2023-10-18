package com.vaistra.controllers.bank;

import com.vaistra.dto.HttpResponse;
import com.vaistra.dto.bank.BankBranchDto;
import com.vaistra.dto.bank.BankBranchUpdateDto;
import com.vaistra.services.bank.BankBranchService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("bankBranch")
public class BankBranchController {

    private final BankBranchService bankBranchService;

    @Autowired
    public BankBranchController(BankBranchService bankBranchService) {
        this.bankBranchService = bankBranchService;
    }

    @PostMapping
    public ResponseEntity<BankBranchDto> addBankBranch(@Valid @RequestBody BankBranchDto bankBranchDto)
    {
        return new ResponseEntity<>(bankBranchService.addBankBranch(bankBranchDto), HttpStatus.CREATED);
    }

    @GetMapping("{bankBranchId}")
    public ResponseEntity<BankBranchDto> getBankBranchById(@PathVariable int bankBranchId)
    {
        return new ResponseEntity<>(bankBranchService.getBankBranchById(bankBranchId), HttpStatus.OK);
    }

    @GetMapping("search")
    public ResponseEntity<HttpResponse> searchBankBranchByKeyword(@RequestParam(value = "keyword", defaultValue = "", required = false) String keyword,
                                                                  @RequestParam(value = "pageNumber", defaultValue = "0", required = false) Integer pageNumber,
                                                                  @RequestParam(value = "pageSize", defaultValue = "10", required = false) Integer pageSize,
                                                                  @RequestParam(value = "sortBy", defaultValue = "branchId", required = false) String sortBy,
                                                                  @RequestParam(value = "sortDirection", defaultValue = "asc", required = false) String sortDirection)
    {
        return new ResponseEntity<>(bankBranchService.searchBankBranchByKeyword(keyword, pageNumber, pageSize, sortBy, sortDirection), HttpStatus.OK);
    }

    @GetMapping("bankId/{bankId}")
    public ResponseEntity<HttpResponse> getBankBranchByBankId(@PathVariable int bankId,
                                                              @RequestParam(value = "pageNumber", defaultValue = "0", required = false) Integer pageNumber,
                                                              @RequestParam(value = "pageSize", defaultValue = "10", required = false) Integer pageSize,
                                                              @RequestParam(value = "sortBy", defaultValue = "branchId", required = false) String sortBy,
                                                              @RequestParam(value = "sortDirection", defaultValue = "asc", required = false) String sortDirection)
    {
        return new ResponseEntity<>(bankBranchService.getBankBranchesByBankId(bankId, pageNumber, pageSize, sortBy, sortDirection), HttpStatus.OK);
    }

    @GetMapping("stateId/{stateId}")
    public ResponseEntity<HttpResponse> getBankBranchByStateId(@PathVariable int stateId,
                                                              @RequestParam(value = "pageNumber", defaultValue = "0", required = false) Integer pageNumber,
                                                              @RequestParam(value = "pageSize", defaultValue = "10", required = false) Integer pageSize,
                                                              @RequestParam(value = "sortBy", defaultValue = "branchId", required = false) String sortBy,
                                                              @RequestParam(value = "sortDirection", defaultValue = "asc", required = false) String sortDirection)
    {
        return new ResponseEntity<>(bankBranchService.getBankBranchesByStateId(stateId, pageNumber, pageSize, sortBy, sortDirection), HttpStatus.OK);
    }
    @GetMapping("districtId/{districtId}")
    public ResponseEntity<HttpResponse> getBankBranchByDistrictId(@PathVariable int districtId,
                                                               @RequestParam(value = "pageNumber", defaultValue = "0", required = false) Integer pageNumber,
                                                               @RequestParam(value = "pageSize", defaultValue = "10", required = false) Integer pageSize,
                                                               @RequestParam(value = "sortBy", defaultValue = "branchId", required = false) String sortBy,
                                                               @RequestParam(value = "sortDirection", defaultValue = "asc", required = false) String sortDirection)
    {
        return new ResponseEntity<>(bankBranchService.getBankBranchesByDistrictId(districtId, pageNumber, pageSize, sortBy, sortDirection), HttpStatus.OK);
    }

    @GetMapping("all")
    public ResponseEntity<HttpResponse> getAllBankBranches( @RequestParam(value = "pageNumber", defaultValue = "0", required = false) Integer pageNumber,
                                                            @RequestParam(value = "pageSize", defaultValue = "10", required = false) Integer pageSize,
                                                            @RequestParam(value = "sortBy", defaultValue = "branchId", required = false) String sortBy,
                                                            @RequestParam(value = "sortDirection", defaultValue = "asc", required = false) String sortDirection)
    {
        return new ResponseEntity<>(bankBranchService.getAllBankBranches(pageNumber, pageSize, sortBy, sortDirection), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<HttpResponse> getAllActiveBankBranches(@RequestParam(value = "pageNumber", defaultValue = "0", required = false) Integer pageNumber,
                                                                @RequestParam(value = "pageSize", defaultValue = "10", required = false) Integer pageSize,
                                                                @RequestParam(value = "sortBy", defaultValue = "branchId", required = false) String sortBy,
                                                                @RequestParam(value = "sortDirection", defaultValue = "asc", required = false) String sortDirection)
    {
        return new ResponseEntity<>(bankBranchService.getAllActiveBankBranches(pageNumber, pageSize, sortBy, sortDirection), HttpStatus.OK);
    }
    @GetMapping("activeBank")
    public ResponseEntity<HttpResponse> getAlLBankBranchesByActiveBank(@RequestParam(value = "pageNumber", defaultValue = "0", required = false) Integer pageNumber,
                                                                 @RequestParam(value = "pageSize", defaultValue = "10", required = false) Integer pageSize,
                                                                 @RequestParam(value = "sortBy", defaultValue = "branchId", required = false) String sortBy,
                                                                 @RequestParam(value = "sortDirection", defaultValue = "asc", required = false) String sortDirection)
    {
        return new ResponseEntity<>(bankBranchService.getAlLBankBranchesByActiveBank(pageNumber, pageSize, sortBy, sortDirection), HttpStatus.OK);
    }

    @PutMapping("{bankBranchId}")
    public ResponseEntity<BankBranchDto> updateBankBranch(@Valid @RequestBody BankBranchUpdateDto bankBranchDto, @PathVariable int bankBranchId)
    {
        return new ResponseEntity<>(bankBranchService.updateBankBranch(bankBranchDto, bankBranchId), HttpStatus.OK);
    }

    @DeleteMapping("{bankBranchId}")
    public ResponseEntity<String> deleteBankBranch(@PathVariable int bankBranchId)
    {
        return new ResponseEntity<>(bankBranchService.deleteBankBranch(bankBranchId), HttpStatus.OK);
    }

}
