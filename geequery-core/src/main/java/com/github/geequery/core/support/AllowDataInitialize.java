package com.github.geequery.core.support;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.github.geequery.annotation.EasyEntity;

@EasyEntity(checkEnhanced = false)
@Entity
@Table(name = "allow_data_initialize")
public class AllowDataInitialize{

    @Column(name = "do_init", columnDefinition = "char(1) default '0'", length = 1, nullable = false)
    private boolean doInit;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "last_data_init_time", columnDefinition = "timestamp")
    private Date lastDataInitTime;

    @Column(name = "last_data_init_user", columnDefinition = "varchar(255)", length = 255)
    private String lastDataInitUser;

    @Column(name = "last_data_init_result", columnDefinition = "varchar(400)", length = 400)
    private String lastDataInitResult;
    
    public void setDoInit(boolean obj) {
        this.doInit = obj;
    }

    public boolean isDoInit() {
        return doInit;
    }

    public void setLastDataInitTime(Date obj) {
        this.lastDataInitTime = obj;
    }

    public Date getLastDataInitTime() {
        return lastDataInitTime;
    }

    public void setLastDataInitUser(String obj) {
        this.lastDataInitUser = obj;
    }

    public String getLastDataInitUser() {
        return lastDataInitUser;
    }

    public String getLastDataInitResult() {
        return lastDataInitResult;
    }

    public void setLastDataInitResult(String lastDataInitResult) {
        this.lastDataInitResult = lastDataInitResult;
    }
}