package com.vaistra.payloads;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public class CountryDto {

    private int countryId;

    @NotEmpty(message = "Country Should not be Empty!")
    @NotBlank(message = "Country Should not be Blank!")
    @Size(min = 3, message = "Country name should be at least 3 characters!")
    private String countryName;
    private boolean status = true;
    private boolean deleted = false;

    public CountryDto() {
    }

    public CountryDto(int countryId, String countryName, boolean status, boolean deleted) {
        this.countryId = countryId;
        this.countryName = countryName;
        this.status = status;
        this.deleted = deleted;
    }


    public int getCountryId() {
        return countryId;
    }

    public void setCountryId(int countryId) {
        this.countryId = countryId;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    @Override
    public String toString() {
        return "CountryDto{" +
                "countryId=" + countryId +
                ", countryName='" + countryName + '\'' +
                ", status=" + status +
                ", deleted=" + deleted +
                '}';
    }
}
