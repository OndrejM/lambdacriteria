package eu.inginea.lambdacriteria.streamQuery;

public class Parameter implements Term {

    public Parameter(int paramIndex) {
    }

    @Override
    public String toString() {
        return "Parameter";
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
