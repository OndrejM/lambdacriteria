package eu.inginea.lambdacriteria.streamQuery.ruleengine;

import java.util.Objects;

public interface Term {

    @Override
    String toString();

    default int hashCodeFromToString() {
        int hash = 7;
        hash = 89 * hash + Objects.hashCode(this.toString());
        return hash;
    }

    default boolean equalsFromToString(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        if (!Objects.equals(this.toString(), obj.toString())) {
            return false;
        }
        return true;
    }

}
