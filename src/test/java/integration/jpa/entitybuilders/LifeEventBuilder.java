package integration.jpa.entitybuilders;

import integration.jpa.model.LifeEvent;
import integration.jpa.entitybuilders.EntityBuider;

public class LifeEventBuilder extends EntityBuider {

    private LifeEvent le = new LifeEvent();
    
    public static LifeEventBuilder aLifeEvent() {
        return new LifeEventBuilder();
    }

    @Override
    protected LifeEvent build() {
        return le;
    }

    public LifeEventBuilder withPlace(String place) {
        le.setPlace(place);
        return this;
    }
    
}
