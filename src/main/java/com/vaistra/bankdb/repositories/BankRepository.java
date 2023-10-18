package com.vaistra.bankdb.repositories;

import com.vaistra.bankdb.entities.Bank;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BankRepository extends JpaRepository<Bank, Integer> {

    Boolean existsByBankLongNameIgnoreCase(String name);
    Page<Bank> findAllByBankIdOrStatusOrBankShortNameContainingIgnoreCaseOrBankLongNameContainingIgnoreCase(Integer bankId, Boolean status, String shortName, String longName, Pageable p);
    Page<Bank> findAllByStatus(Boolean b, Pageable p);
    Bank findByBankLongNameIgnoreCase(String name);
}
