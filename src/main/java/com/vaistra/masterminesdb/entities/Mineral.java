package com.vaistra.masterminesdb.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Mineral {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer mineralId;

    private String mineralName;

    private String category;

    private String atrName;

    private String hsnCode;

    private List<String> grade = new ArrayList<>();
}
