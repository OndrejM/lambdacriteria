/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ondrom.experiments.jpa;

import java.util.Calendar;
import java.util.TimeZone;
import javax.persistence.EntityTransaction;
import ondrom.experiments.jpa.calendarmapping.CalendarEntity;
import static org.junit.Assert.*;
import org.junit.Test;

/**
 *
 * @author ondro
 */
public class CalendarMappingTest extends JPATestBase {

    public CalendarMappingTest() {
    }

    /**
     * Experimented with timestamp with timezone, but it is not supported neither by JPA nor by many databases. It makes more sense to put timezone info into an extra column.
     */
    @Test
    public void calendarTimezoneMapping() {
        CalendarEntity cal = new CalendarEntity();
        Calendar time = Calendar.getInstance(TimeZone.getTimeZone("GMT+6:00"));
        System.out.println("Time is: " + dateTimeFormatter().format(time.getTime()));
        cal.setTimeWithTimezone(time);
        beginTx();
        getEM().persist(cal);
        commitTx();
    }


}
