package eu.inginea.lambdaquery.base;

import java.io.Serializable;

@FunctionalInterface
public interface QueryPredicate<T> extends Serializable {
    public boolean select(T rootEntity);
}
