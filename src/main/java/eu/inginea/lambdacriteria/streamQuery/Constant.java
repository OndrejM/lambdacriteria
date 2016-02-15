package eu.inginea.lambdacriteria.streamQuery;

public class Constant implements Term {

    private final String value;
    
    public Constant(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "Constant{" + value + '}';
    }
    
    @Override
    public boolean equals(Object obj) {
        return equalsFromToString(obj);
    }

    @Override
    public int hashCode() {
        return hashCodeFromToString();
    }

}
