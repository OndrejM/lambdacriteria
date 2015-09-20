package ondrom.experiments.jpa.calendarmapping;

import java.util.Calendar;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
public class CalendarEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    
    @Temporal(TemporalType.TIMESTAMP)
    private Calendar timeWithTimezone; 

    public Calendar getTimeWithTimezone() {
        return timeWithTimezone;
    }

    public void setTimeWithTimezone(Calendar timeWithTimezone) {
        this.timeWithTimezone = timeWithTimezone;
    }
    
}
