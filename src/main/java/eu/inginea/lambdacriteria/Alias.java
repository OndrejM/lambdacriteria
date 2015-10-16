package eu.inginea.lambdacriteria;

import java.io.Serializable;

public class Alias<T> implements Serializable {
    
    public T val;
    
    private Class<?> entityClass;
    
    // necessary to identify aliases within lambda expression, as real instances differ due to serialization
    private long key = System.identityHashCode(this);

    public Alias(Class<?> entityClass) {
        this.entityClass = entityClass;
    }

    public Class<?> getEntityClass() {
        return entityClass;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 23 * hash + (int) (this.key ^ (this.key >>> 32));
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Alias<?> other = (Alias<?>) obj;
        if (this.key != other.key) {
            return false;
        }
        return true;
    }

    
    
}
