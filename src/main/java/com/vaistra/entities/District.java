package com.vaistra.entities;

import jakarta.persistence.*;

@Entity
public class District {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "district_id")
    private int districtId;

    @Column(name = "district_name")
    private String districtName;

    @Column(name = "status")
    private boolean status;
    @Column(name = "deleted")
    private boolean deleted = false;

    @ManyToOne
    @JoinColumn(name = "state_id")
    private State state;

    public District() {
    }

    public District(int districtId, String districtName, boolean status, boolean deleted, State state) {
        this.districtId = districtId;
        this.districtName = districtName;
        this.status = status;
        this.deleted = deleted;
        this.state = state;
    }

    public int getDistrictId() {
        return districtId;
    }

    public void setDistrictId(int districtId) {
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

    public State getState() {
        return state;
    }

    public void setState(State state) {
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
