package ondrom.experiments.jpa;

class LifeEventBuilder extends EntityBuider {

    private LifeEvent le = new LifeEvent();
    
    static LifeEventBuilder aLifeEvent() {
        return new LifeEventBuilder();
    }

    @Override
    protected LifeEvent build() {
        return le;
    }

    LifeEventBuilder withPlace(String place) {
        le.setPlace(place);
        return this;
    }
    
}
