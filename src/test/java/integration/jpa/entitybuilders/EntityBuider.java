package integration.jpa.entitybuilders;

import javax.persistence.EntityManager;

abstract class EntityBuider {
    protected abstract Object build();
    
    public void existsIn(EntityManager em) {
        em.persist(build());
    }

}
