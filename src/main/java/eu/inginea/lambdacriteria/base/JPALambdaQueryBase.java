package eu.inginea.lambdacriteria.base;

import eu.inginea.lambdacriteria.Alias;
import eu.inginea.lambdacriteria.alternative1.LambdaQuery;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaQuery;

public abstract class JPALambdaQueryBase<T> {
    protected EntityManager em;
    protected Alias<?>[] roots;
    protected Alias<?>[] selects;

    public JPALambdaQueryBase(EntityManager em) {
        this.em = em;
    }

    public JPALambdaQueryBase<T> select(Alias<?>... a) {
        this.selects = a; // to be extended to support more than aliased entity in selection
        return this;
    }

    public JPALambdaQueryBase<T> from(Alias<?>... rootAliases) {
        this.roots = rootAliases;
        return this;
    }

    protected List<LambdaQuery.AliasInstance> initAliasInstances(CriteriaQuery q) {
        List<LambdaQuery.AliasInstance> aliasInstances = new ArrayList<>();
        for (Alias<?> alias : roots) {
            aliasInstances.add(new LambdaQuery.AliasInstance(alias, q.from(alias.getEntityClass())));
        }
        return aliasInstances;
    }

}
