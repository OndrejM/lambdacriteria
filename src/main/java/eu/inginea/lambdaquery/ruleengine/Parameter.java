package eu.inginea.lambdaquery.ruleengine;

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
