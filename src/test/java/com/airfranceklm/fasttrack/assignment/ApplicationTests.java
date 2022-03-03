package com.airfranceklm.fasttrack.assignment;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Date;
import java.util.NoSuchElementException;

import com.airfranceklm.fasttrack.assignment.model.Holiday;
import com.airfranceklm.fasttrack.assignment.service.HolidayService;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ApplicationTests {

	@Autowired  
    HolidayService holidayService;
	
	@Test
	void contextLoads() {
	}

	@Test
    public void should_create_new_holiday() {
		Holiday holiday = new Holiday("Test", "klm123456", new Date(), new Date());
        holidayService.upsertHoliday(holiday);

        Holiday holidaybyEmpId = holidayService.getAllHolidaysByEmployeeId("klm123456").get(0);
        assertNotNull(holidaybyEmpId);

    }

	@Test
    public void should_delete_holiday() {
        
		Holiday holidaybyEmpId = holidayService.getAllHolidaysByEmployeeId("klm123456").get(0);
		holidayService.delete(holidaybyEmpId.getId());
		
        assertThrows(NoSuchElementException.class, () -> {holidayService.getHolidayById(holidaybyEmpId.getId());}
		);

    }


}
