package ondrom.experiments.jpa.entitybuilders;

import ondrom.experiments.jpa.Address;

public class AddressBuilder extends EntityBuider {
    private Address address = new Address();
    
    public static AddressBuilder anAddress() {
        return new AddressBuilder();
    }

    @Override
    protected Address build() {
        return address;
    }

    public AddressBuilder withCity(String city) {
        address.setCity(city);
        return this;
    }

}
