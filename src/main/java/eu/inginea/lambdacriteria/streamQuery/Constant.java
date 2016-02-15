package eu.inginea.lambdacriteria.streamQuery;

public class Constant implements Term {

    private final String value;
    
    public Constant(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "Constant{" + "value=" + value + '}';
    }
    
}
