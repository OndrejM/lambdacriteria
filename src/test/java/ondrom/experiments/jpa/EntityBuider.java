package ondrom.experiments.jpa;

import javax.persistence.EntityManager;

abstract class EntityBuider {
    protected abstract Object build();
    
    protected void existsIn(EntityManager em) {
        em.persist(build());
    }

}
