package eu.inginea.lambdacriteria.streamQuery;

import java.io.Serializable;

@FunctionalInterface
public interface QueryPredicate<T> extends Serializable {
    public boolean select(T rootEntity);
}
