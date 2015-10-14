package eu.inginea.lambdacriteria;

import java.io.Serializable;

public class Alias<T> implements Serializable {
    
    public T val;
    
    private Class<?> entityClass;

    public Alias(Class<?> entityClass) {
        this.entityClass = entityClass;
    }

    public Class<?> getEntityClass() {
        return entityClass;
    }

    
}
