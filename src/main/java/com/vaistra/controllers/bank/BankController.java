package com.vaistra.controllers.bank;

import com.vaistra.dto.HttpResponse;
import com.vaistra.dto.bank.BankDto;
import com.vaistra.services.bank.BankService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("bank")
public class BankController {

    private final BankService bankService;

    @Autowired
    public BankController(BankService bankService) {
        this.bankService = bankService;
    }

//    @PostMapping

    @GetMapping("{bankId}")
    public ResponseEntity<BankDto> getBankById(@PathVariable int bankId)
    {
        return new ResponseEntity<>(bankService.getBankById(bankId), HttpStatus.OK);
    }

    @GetMapping("search")
    public ResponseEntity<HttpResponse> searchBanksByKeyword(@RequestParam(value = "keyword", defaultValue = "", required = false) String keyword,
                                                             @RequestParam(value = "pageNumber", defaultValue = "0", required = false) Integer pageNumber,
                                                             @RequestParam(value = "pageSize", defaultValue = "10", required = false) Integer pageSize,
                                                             @RequestParam(value = "sortBy", defaultValue = "bankId", required = false) String sortBy,
                                                             @RequestParam(value = "sortDirection", defaultValue = "asc", required = false) String sortDirection)
    {
        return new ResponseEntity<>(bankService.searchBanksByKeyword(keyword, pageNumber, pageSize, sortBy, sortDirection), HttpStatus.OK);
    }

    @GetMapping("all")
    public ResponseEntity<HttpResponse> getAllBanks(@RequestParam(value = "pageNumber", defaultValue = "0", required = false) Integer pageNumber,
                                                    @RequestParam(value = "pageSize", defaultValue = "10", required = false) Integer pageSize,
                                                    @RequestParam(value = "sortBy", defaultValue = "bankId", required = false) String sortBy,
                                                    @RequestParam(value = "sortDirection", defaultValue = "asc", required = false) String sortDirection)
    {
        return new ResponseEntity<>(bankService.getAlLBanks(pageNumber, pageSize, sortBy, sortDirection), HttpStatus.OK);
    }
    @GetMapping
    public ResponseEntity<HttpResponse> getAllActiveBanks(@RequestParam(value = "pageNumber", defaultValue = "0", required = false) Integer pageNumber,
                                                        @RequestParam(value = "pageSize", defaultValue = "10", required = false) Integer pageSize,
                                                        @RequestParam(value = "sortBy", defaultValue = "bankId", required = false) String sortBy,
                                                        @RequestParam(value = "sortDirection", defaultValue = "asc", required = false) String sortDirection)
    {
        return new ResponseEntity<>(bankService.getAlLActiveBanks(pageNumber, pageSize, sortBy, sortDirection), HttpStatus.OK);
    }

//    @PutMapping


    @DeleteMapping("{bankId}")
    public ResponseEntity<String> deleteBank(@PathVariable int bankId)
    {
        return new ResponseEntity<>(bankService.deleteBank(bankId), HttpStatus.OK);
    }
}
