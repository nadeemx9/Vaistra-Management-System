package com.vaistra.entities;

import jakarta.persistence.*;

@Entity
public class SubDistrict {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "subdistrict_id")
    private int subDistrictId;

    @Column(name = "subdistrict_name")
    private String subDistrictName;
    @Column(name = "deleted")
    private boolean deleted = false;

    @Column(name = "status")
    private boolean status;

    @ManyToOne
    @JoinColumn(name = "district_id")
    private District district;

    public SubDistrict() {
    }

    public SubDistrict(int subDistrictId, String subDistrictName, boolean status, boolean deleted, District district) {
        this.subDistrictId = subDistrictId;
        this.subDistrictName = subDistrictName;
        this.status = status;
        this.deleted = deleted;
        this.district = district;
    }

    public int getSubDistrictId() {
        return subDistrictId;
    }

    public void setSubDistrictId(int subDistrictId) {
        this.subDistrictId = subDistrictId;
    }

    public String getSubDistrictName() {
        return subDistrictName;
    }

    public void setSubDistrictName(String subDistrictName) {
        this.subDistrictName = subDistrictName;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public District getDistrict() {
        return district;
    }

    public void setDistrict(District district) {
        this.district = district;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    @Override
    public String toString() {
        return "SubDistrict{" +
                "subDistrictId=" + subDistrictId +
                ", subDistrictName='" + subDistrictName + '\'' +
                ", deleted=" + deleted +
                ", status=" + status +
                ", district=" + district +
                '}';
    }
}
