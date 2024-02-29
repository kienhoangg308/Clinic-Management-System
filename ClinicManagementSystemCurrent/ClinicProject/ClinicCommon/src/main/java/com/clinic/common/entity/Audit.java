package com.clinic.common.entity;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Date;

@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class Audit<String> {

    @CreatedBy
    @Column(updatable = false)
    private String createdBy;

    @CreatedDate
    //@DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(updatable = false)
    private Date createdDate;

    @LastModifiedBy
    private String modifiedBy;

    @LastModifiedDate
    //@DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date modifiedDate;

    // Getters and setters for all fields
    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public String getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(String modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    public Date getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate(Date modifiedDate) {
        this.modifiedDate = modifiedDate;
    }
}