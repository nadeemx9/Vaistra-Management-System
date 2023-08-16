package com.vaistra.payloads;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public class DistrictDto {
    private Integer districtId;
    @NotEmpty(message = "District name Should not be Empty!")
    @NotBlank(message = "District name Should not be Blank!")
    @Size(min = 3, message = "District name should be at least 3 characters!")
    private String districtName;

    private boolean status = true;
    private boolean deleted = false;
    private StateDto state;
    public DistrictDto() {
    }

    public DistrictDto(Integer districtId, String districtName, boolean status, boolean deleted, StateDto state) {
        this.districtId = districtId;
        this.districtName = districtName;
        this.status = status;
        this.deleted = deleted;
        this.state = state;
    }

    public Integer getDistrictId() {
        return districtId;
    }

    public void setDistrictId(Integer districtId) {
        this.districtId = districtId;
    }

    public String getDistrictName() {
        return districtName;
    }

    public void setDistrictName(String districtName) {
        this.districtName = districtName;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public StateDto getState() {
        return state;
    }

    public void setState(StateDto state) {
        this.state = state;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    @Override
    public String toString() {
        return "District{" +
                "districtId=" + districtId +
                ", districtName='" + districtName + '\'' +
                ", status=" + status +
                ", deleted=" + deleted +
                ", state=" + state +
                '}';
    }

}
