package com.vaistra.payloads;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public class StateDto {
    private Integer stateId;

    @NotEmpty(message = "State name Should not be Empty!")
    @NotBlank(message = "State name Should not be Blank!")
    @Size(min = 3, message = "State name should be at least 3 characters!")
    private String stateName;
    private boolean status = true;

    private boolean deleted = false;

    //    @NotEmpty(message = "Country Should not be Empty!")
//    @NotBlank(message = "Country Should not be Blank!")
//    @Valid
//    @NotNull(message = "Country should not be Null!")
    private CountryDto country;

    public StateDto() {
    }

    public StateDto(Integer stateId, String stateName, boolean status, boolean deleted, CountryDto country) {
        this.stateId = stateId;
        this.stateName = stateName;
        this.status = status;
        this.deleted = deleted;
        this.country = country;
    }

    public Integer getStateId() {
        return stateId;
    }

    public void setStateId(Integer stateId) {
        this.stateId = stateId;
    }

    public String getStateName() {
        return stateName;
    }

    public void setStateName(String stateName) {
        this.stateName = stateName;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public CountryDto getCountry() {
        return country;
    }

    public void setCountry(CountryDto countryDto) {
        this.country = countryDto;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    @Override
    public String toString() {
        return "StateDto{" +
                "stateId=" + stateId +
                ", stateName='" + stateName + '\'' +
                ", status=" + status +
                ", deleted=" + deleted +
                ", countryDto=" + country +
                '}';
    }
}
