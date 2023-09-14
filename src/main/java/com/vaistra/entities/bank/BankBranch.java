package com.vaistra.entities.bank;

import com.vaistra.entities.cscv.District;
import com.vaistra.entities.cscv.State;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BankBranch {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer BranchId;

    private String branchName;
    private String branchCode;
    private String branchAddress;
    private String branchIfsc;
    private String branchPhoneNumber;
    private String branchMicr;
    private LocalTime fromTiming;
    private LocalTime toTiming;

    @ManyToOne
    @JoinColumn(name = "bank_id")
    private Bank bank;

    @ManyToOne
    @JoinColumn(name = "state_id")
    private State state;

    @ManyToOne
    @JoinColumn(name = "district_id")
    private District district;

    private Boolean status;


}
