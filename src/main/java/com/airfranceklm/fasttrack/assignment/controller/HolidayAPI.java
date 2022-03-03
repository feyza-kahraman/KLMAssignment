package com.airfranceklm.fasttrack.assignment.controller;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import com.airfranceklm.fasttrack.assignment.model.Holiday;
import com.airfranceklm.fasttrack.assignment.model.Constants.STATUS;
import com.airfranceklm.fasttrack.assignment.service.HolidayService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HolidayAPI {

    @Autowired  
    HolidayService holidayService;

    @GetMapping("/holidays")
    public ResponseEntity<List<Holiday>> getHolidays() {
        return new ResponseEntity<List<Holiday>>(holidayService.getAllHolidays(), HttpStatus.OK);
    }

    @GetMapping("/holidays/{id}")
    public ResponseEntity<Holiday> getHolidayById(@PathVariable("id") String id) {
        return new ResponseEntity<Holiday>(holidayService.getHolidayById(id), HttpStatus.OK);
    }

    @GetMapping("/holidays/employees/{id}")
    public ResponseEntity<List<Holiday>> getHolidaysByEmployeeId(@PathVariable("id") String id) {
        System.out.println(id);
        return new ResponseEntity<List<Holiday>>(holidayService.getAllHolidaysByEmployeeId(id), HttpStatus.OK);
    }

    @DeleteMapping("/holidays/{id}")
    public ResponseEntity<String> deleteHolidayById(@PathVariable("id") String id) {

        //BUSINESS-RULE3: A holiday must be cancelled at least 5 working days before the start date.
        Holiday holiday = holidayService.getHolidayById(id);
        Calendar c = Calendar.getInstance();
        c.setTime(holiday.getStartOfHoliday());
        c.add(Calendar.DATE, -5);
        Date sysBound = c.getTime();
        Date today = new Date();

        System.out.println("sysBound:" + sysBound);
        System.out.println("today:" + today);
        
        if ( !(today.before(sysBound)) )
            return new ResponseEntity<String>(new String("A holiday must be cancelled at least 5 working days before the start date."), HttpStatus.BAD_REQUEST);

        holidayService.delete(id);
        return new ResponseEntity<String>(new String("Holiday deleted:" + id), HttpStatus.GONE);
    }

    
    @PostMapping("/holidays")
    public ResponseEntity<String> createHoliday(@RequestBody Holiday holiday) {

        //EmployeeId Format check
        if (!holiday.getEmployeeId().matches("^klm[0-9]{6}$"))
            return new ResponseEntity<String>(new String("Invalid format for EmployeeId:" + holiday.getEmployeeId()), HttpStatus.BAD_REQUEST);

        //BUSINESS-RULE1: There should be a gap of at least 3 working days between holidays
        List<Holiday> existingHolidays = holidayService.getAllHolidaysByEmployeeId(holiday.getEmployeeId());
        for (Holiday existingHoliday : existingHolidays) {
            Calendar c1 = Calendar.getInstance();
            c1.setTime(existingHoliday.getStartOfHoliday());
            c1.add(Calendar.DATE, -3);
            Date lowerBound = c1.getTime();
            
            Calendar c2 = Calendar.getInstance();
            c2.setTime(existingHoliday.getEndOfHoliday());
            c2.add(Calendar.DATE, 3);
            Date upperBound = c2.getTime();

            System.out.println("lowerBound:" + lowerBound);
            System.out.println("holiday start:" + holiday.getStartOfHoliday());
            System.out.println("holiday end:" + holiday.getEndOfHoliday());
            System.out.println("upperBound:" + upperBound);

            if( !((holiday.getEndOfHoliday().before(lowerBound) && holiday.getStartOfHoliday().before(lowerBound))
                ||
                (holiday.getStartOfHoliday().after(upperBound) && holiday.getEndOfHoliday().after(upperBound)))
            )
                return new ResponseEntity<String>(new String("There should be a gap of at least 3 working days between holidays"), HttpStatus.BAD_REQUEST);
        }

        //BUSINESS-RULE2: A holiday must be planned at least 5 working days before the start date.
        Calendar c3 = Calendar.getInstance();
        c3.setTime(holiday.getStartOfHoliday());
        c3.add(Calendar.DATE, -5);
        Date sysBound = c3.getTime();
        Date today = new Date();

        System.out.println("sysBound:" + sysBound);
        System.out.println("today:" + today);
        
        if ( !(today.before(sysBound)) )
            return new ResponseEntity<String>(new String("A holiday must be planned at least 5 working days before the start date"), HttpStatus.BAD_REQUEST);

        
        //BUSINESS-RULE4: Holidays must not overlap (for the sake of this assignment also not between different crew members)
        List<Holiday> allHolidays = holidayService.getAllHolidays();
        for (Holiday eachHoliday : allHolidays) {
            if ( (holiday.getStartOfHoliday().after(eachHoliday.getStartOfHoliday()) && holiday.getStartOfHoliday().before(eachHoliday.getEndOfHoliday()))
                ||
                (holiday.getEndOfHoliday().after(eachHoliday.getStartOfHoliday()) && holiday.getEndOfHoliday().before(eachHoliday.getEndOfHoliday()))
              )
                return new ResponseEntity<String>(new String("Holidays must not overlap"), HttpStatus.BAD_REQUEST);
        }


        //ALL BUSINESS RULES ARE OK, READY TO CREATE
        holiday.setId(UUID.randomUUID().toString());
        holiday.setStatus(STATUS.REQUESTED);
        
        holidayService.upsertHoliday(holiday);
        return new ResponseEntity<String>(new String("Holiday Created for Employee:" + holiday.getEmployeeId()), HttpStatus.CREATED);
    }

    @PutMapping("/holidays")
    public ResponseEntity<String> updateHoliday(@RequestBody Holiday holiday) {

        if (!holiday.getEmployeeId().matches("^klm[0-9]{6}$"))
            return new ResponseEntity<String>(new String("Invalid format for EmployeeId:" + holiday.getEmployeeId()), HttpStatus.BAD_REQUEST);
        else{
            holidayService.upsertHoliday(holiday);
            return new ResponseEntity<String>(new String("Holiday Updated for Employee:" + holiday.getEmployeeId()), HttpStatus.ACCEPTED);
        }
    }

}
