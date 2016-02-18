package eu.inginea.lambdacriteria.streamQuery.ruleengine;

import java.util.Objects;

public class Path implements Term {

    private final String path;
    
    public Path(String path) {
        this.path = path;
    }

    @Override
    public String toString() {
        return "Path{"  + path + '}';
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
