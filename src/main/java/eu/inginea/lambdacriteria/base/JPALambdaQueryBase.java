package eu.inginea.lambdacriteria.base;

import eu.inginea.lambdacriteria.Alias;
import eu.inginea.lambdacriteria.alternative1.LambdaQuery;
import eu.inginea.lambdacriteria.alternative1.Selector;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaQuery;

public abstract class JPALambdaQueryBase<T> {
    protected EntityManager em;
    protected List<SelectionInstance> roots = new ArrayList<>();
    protected List<SelectionInstance> selects = new ArrayList<>();
    protected List<SelectionInstance> groupBys = new ArrayList<>();

    public JPALambdaQueryBase(EntityManager em) {
        this.em = em;
    }

    public JPALambdaQueryBase<T> select(Alias<?> a) {
        this.selects.add(new SelectionInstance(a));
        return this;
    }

    public <SELECTED> JPALambdaQueryBase<T> select(Selector<SELECTED> selector) {
        this.selects.add(new SelectionInstance(selector));
        return this;
    }

    public JPALambdaQueryBase<T> from(Alias<?> root) {
        this.roots.add(new SelectionInstance(root));
        return this;
    }

    public <SELECTED> JPALambdaQueryBase<T> groupBy(Selector<SELECTED> selector) {
        this.groupBys.add(new SelectionInstance(selector));
        return this;
    }
    
    protected List<SelectionInstance> initAliases(CriteriaQuery q) {
        for (SelectionInstance root : roots) {
            root.setInstance(q.from(root.getAlias().getEntityClass()));
        }
        ArrayList<SelectionInstance> allAliases = new ArrayList<>(roots);
        return allAliases;
    }

}
