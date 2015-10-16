package ondrom.experiments.jpa.entitybuilders;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import ondrom.experiments.jpa.Person;

public class PersonBuilder extends EntityBuider {

    private Person person = new Person();
    private Map<String, LifeEventBuilder> leBuilders = new HashMap<>();
    private List<LifeEventBuilder> leListBuilders = new ArrayList<>();
    
    public static PersonBuilder aPerson() {
        return new PersonBuilder();
    }

    public PersonBuilder withLifeEvent(String type, LifeEventBuilder leBuilder) {
        this.leBuilders.put(type, leBuilder);
        return this;
    }

    public PersonBuilder withLifeEvent(LifeEventBuilder leBuilder) {
        this.leListBuilders.add(leBuilder);
        return this;
    }

    @Override
    protected Person build() {
        person.getLifeEvents().clear();
        leBuilders.entrySet().forEach(e -> {
            person.getLifeEvents().put(e.getKey(), e.getValue().build());
        });
        person.getLifeEventList().clear();
        leListBuilders.forEach(e -> {
            person.getLifeEventList().add(e.build());
        });
        return person;
    }

    public PersonBuilder withName(String name) {
        person.setName(name);
        return this;
    }

    public PersonBuilder withHairColor(String color) {
        person.setHairColor(color);
        return this;
    }
    
}
