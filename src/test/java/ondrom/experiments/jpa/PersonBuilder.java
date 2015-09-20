package ondrom.experiments.jpa;

import java.util.HashMap;
import java.util.Map;

class PersonBuilder extends EntityBuider {

    private Person person = new Person();
    private Map<String, LifeEventBuilder> leBuilders = new HashMap<>();
    
    static PersonBuilder aPerson() {
        return new PersonBuilder();
    }

    PersonBuilder withLifeEvent(String type, LifeEventBuilder leBuilder) {
        this.leBuilders.put(type, leBuilder);
        return this;
    }

    @Override
    protected Person build() {
        person.getLifeEvents().clear();
        leBuilders.entrySet().forEach(e -> {
            person.getLifeEvents().put(e.getKey(), e.getValue().build());
        });
        return person;
    }
    
}
