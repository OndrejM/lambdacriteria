package ondrom.experiments.jpa;

import java.util.Map;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Person {
    @Id
    private long id;
    
    @ElementCollection
    private Map<String, LifeEvent> lifeEvents;
}
