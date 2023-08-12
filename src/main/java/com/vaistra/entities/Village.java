package com.vaistra.entities;

import jakarta.persistence.*;

@Entity
public class Village {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "village_id")
    private int villageId;

    @Column(name = "village_name")
    private String villageName;

    @Column(name = "status")
    private boolean status;
    @Column(name = "deleted")
    private boolean deleted = false;

    @ManyToOne
    @JoinColumn(name = "subdistrict_id")
    private SubDistrict subDistrict;

    public Village() {
    }

    public Village(int villageId, String villageName, boolean status, boolean deleted, SubDistrict subDistrict) {
        this.villageId = villageId;
        this.villageName = villageName;
        this.status = status;
        this.subDistrict = subDistrict;
        this.deleted = deleted;
    }

    public int getVillageId() {
        return villageId;
    }

    public void setVillageId(int villageId) {
        this.villageId = villageId;
    }

    public String getVillageName() {
        return villageName;
    }

    public void setVillageName(String villageName) {
        this.villageName = villageName;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public SubDistrict getSubDistrict() {
        return subDistrict;
    }

    public void setSubDistrict(SubDistrict subDistrict) {
        this.subDistrict = subDistrict;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    @Override
    public String toString() {
        return "Village{" +
                "villageId=" + villageId +
                ", villageName='" + villageName + '\'' +
                ", status=" + status +
                ", deleted=" + deleted +
                ", subDistrict=" + subDistrict +
                '}';
    }
}
