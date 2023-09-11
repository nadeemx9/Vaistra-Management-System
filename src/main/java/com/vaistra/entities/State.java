package com.vaistra.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class State {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "state_id")
    private Integer stateId;

    @Column(name = "state_name")
    private String stateName;

    @Column(name = "status")
    private boolean status = true;
    @Column(name = "deleted")
    private boolean deleted = false;

    @ManyToOne
    @JoinColumn(name = "country_id")
    private Country country;

}
