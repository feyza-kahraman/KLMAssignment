package com.airfranceklm.fasttrack.assignment.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.airfranceklm.fasttrack.assignment.model.Constants.STATUS;


@Entity
@Table(name = "HOLIDAY")
public class Holiday {

    @Id    
    @Column
    private String id;
    
    @Column
    private String label;
    
    @Column
    private String employeeId;
    
    @Column
    private Date startOfHoliday;
    
    @Column
    private Date endOfHoliday;
    
    @Column
    private STATUS status;

    public Holiday(){

    }

    
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLabel() {
        return label;
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    public Date getStartOfHoliday() {
        return startOfHoliday;
    }

    public void setStartOfHoliday(Date startOfHoliday) {
        this.startOfHoliday = startOfHoliday;
    }

    public Date getEndOfHoliday() {
        return endOfHoliday;
    }

    public void setEndOfHoliday(Date endOfHoliday) {
        this.endOfHoliday = endOfHoliday;
    }

    public STATUS getStatus() {
        return status;
    }

    public void setStatus(STATUS status) {
        this.status = status;
    }

    
}
