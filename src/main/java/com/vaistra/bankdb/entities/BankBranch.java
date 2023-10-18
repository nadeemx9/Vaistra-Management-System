package com.vaistra.bankdb.entities;

//import com.vaistra.cscvdb.entities.District;
//import com.vaistra.cscvdb.entities.State;
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
    private Integer branchId;

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

    private Integer stateId;

    private Integer districtId;

    private Boolean status;

}
