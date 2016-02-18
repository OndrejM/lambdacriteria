package ondrom.experiments.jpa;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.persistence.*;
import static javax.persistence.CascadeType.ALL;

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
    private String hairColor;
    
    @OneToOne(cascade = ALL)
    private Address address;
    
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

    public String getHairColor() {
        return hairColor;
    }

    public void setHairColor(String hairColor) {
        this.hairColor = hairColor;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

}
