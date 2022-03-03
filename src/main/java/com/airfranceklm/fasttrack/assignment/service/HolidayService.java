package com.airfranceklm.fasttrack.assignment.service;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import com.airfranceklm.fasttrack.assignment.model.Holiday;
import com.airfranceklm.fasttrack.assignment.repository.HolidayRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class HolidayService {
    
    @Autowired  
    HolidayRepository holidayRepository; 

        
    //getting all holiday records  
    public List<Holiday> getAllHolidays()   
    {  
        List<Holiday> holidays = new ArrayList<Holiday>();  
        holidayRepository.findAll().forEach(holiday -> holidays.add(holiday));  
        return holidays;  
    }  

    //getting all holiday records by EmployeeId
    public List<Holiday> getAllHolidaysByEmployeeId(String employeeId)   
    {  
        List<Holiday> holidays = new ArrayList<Holiday>();  
        holidayRepository.findAll().forEach(holiday -> holidays.add(holiday)); 
        Predicate<Holiday> filter = holiday -> !holiday.getEmployeeId().equals(employeeId);
        holidays.removeIf(filter);
        return holidays;  
    }  

    //getting holiday record by id  
    public Holiday getHolidayById(String id)   
    {  
        return holidayRepository.findById(id).get(); 
    } 

    //upsert a holiday record  
    public void upsertHoliday(Holiday holiday)   
    {  
        holidayRepository.save(holiday);  
    }  

    //deleting holiday record by id 
    public void delete(String id)   
    {  
        holidayRepository.deleteById(id);  
    }  


}
