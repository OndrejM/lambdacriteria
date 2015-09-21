package ondrom.experiments.jpa;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MapKeyColumn;
import javax.persistence.NamedQuery;

@Entity
@NamedQuery(name = Person.FindAllLeByName.QName, query = "select le from Person p, in (p.lifeEvents) le where le.place = ?1")
public class Person {
    
    private static final String QPrefix = "Person";
    
    public interface FindAllLeByName {
        String QName = QPrefix + "findAllLEByName";
    }
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    
    private String name;
    
    @ElementCollection
    @MapKeyColumn(name = "type")
    private Map<String, LifeEvent> lifeEvents = new HashMap<>();
    
    @ElementCollection
    private List<LifeEvent> lifeEventList = new ArrayList<>();

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Map<String, LifeEvent> getLifeEvents() {
        return lifeEvents;
    }

    public List<LifeEvent> getLifeEventList() {
        return lifeEventList;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
