package ondrom.experiments.jpa;

import java.util.Date;
import javax.persistence.Embeddable;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Embeddable
public class LifeEvent {
    @Temporal(TemporalType.DATE)
    private Date date;
    
    private String name;
}
