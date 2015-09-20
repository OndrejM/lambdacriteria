package ondrom.experiments.jpa;

import java.util.HashMap;
import java.util.Map;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;

@Entity
@NamedQuery(name = Person.FindAllLeByName.QName, query = "select le from Person p, in (p.lifeEvents) le where le.place = :leplace")
public class Person {
    
    private static final String QPrefix = "Person";
    
    public interface FindAllLeByName {
        String QName = QPrefix + "findAllLEByName";
        String place = "leplace";
    }
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    
    @ElementCollection
    private Map<String, LifeEvent> lifeEvents = new HashMap<>();

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Map<String, LifeEvent> getLifeEvents() {
        return lifeEvents;
    }
    
}
