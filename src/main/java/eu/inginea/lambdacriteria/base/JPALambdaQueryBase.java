package eu.inginea.lambdacriteria.base;

import eu.inginea.lambdacriteria.Alias;
import eu.inginea.lambdacriteria.alternative1.LambdaQuery;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaQuery;

public abstract class JPALambdaQueryBase<T> {
    protected EntityManager em;
    protected List<AliasInstance> roots = new ArrayList<>();
    protected List<AliasInstance> selects = new ArrayList<>();

    public JPALambdaQueryBase(EntityManager em) {
        this.em = em;
    }

    public JPALambdaQueryBase<T> select(Alias<?> a) {
        this.selects.add(new AliasInstance(a));
        return this;
    }

    public JPALambdaQueryBase<T> from(Alias<?> root) {
        this.roots.add(new AliasInstance(root));
        return this;
    }

    protected List<AliasInstance> initAliasInstances(CriteriaQuery q) {
        for (AliasInstance root : roots) {
            root.setInstance(q.from(root.getAlias().getEntityClass()));
        }
        ArrayList<AliasInstance> allAliases = new ArrayList<>(roots);
        return allAliases;
    }

}
