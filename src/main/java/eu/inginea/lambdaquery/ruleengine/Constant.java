package eu.inginea.lambdaquery.ruleengine;

public class Constant implements Term {

    private final Object value;
    
    public Constant(Object value) {
        this.value = value;
    }

    public Object getValue() {
        return value;
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
