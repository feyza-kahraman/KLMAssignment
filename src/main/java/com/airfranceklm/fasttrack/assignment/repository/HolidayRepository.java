package com.airfranceklm.fasttrack.assignment.repository;

import org.springframework.data.repository.CrudRepository;
import com.airfranceklm.fasttrack.assignment.model.Holiday;

public interface HolidayRepository extends CrudRepository<Holiday, String>  
{  
    // @Query("select EMPLOYEE_ID from HOLIDAY where EMPLOYEE_ID = ?1")
    // List<Holiday> getHolidaysByEmployeeId(String employeeId);

    
}  