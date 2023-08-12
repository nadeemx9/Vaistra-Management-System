package com.vaistra.entities;

import jakarta.persistence.*;

@Entity
public class Country {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "country_id")
    private int countryId;
    @Column(name = "country_name")
    private String countryName;
    @Column(name = "status")
    private boolean status = true;

    @Column(name = "deleted")
    private boolean deleted = false;

    public Country() {
    }

    public Country(int countryId, String countryName, boolean status, boolean deleted) {
        this.countryName = countryName;
        this.countryId = countryId;
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
        return "Country{" +
                "countryId=" + countryId +
                ", countryName='" + countryName + '\'' +
                ", status=" + status +
                ", deleted=" + deleted +
                '}';
    }
}
