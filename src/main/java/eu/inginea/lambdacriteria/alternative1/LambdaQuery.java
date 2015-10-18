package eu.inginea.lambdacriteria.alternative1;

import eu.inginea.lambdacriteria.base.SelectionInstance;
import eu.inginea.lambdacriteria.base.JPALambdaQueryBase;
import com.trigersoft.jaque.expression.LambdaExpression;
import eu.inginea.lambdacriteria.Alias;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

/**
 * Main object to build query
 *
 * @param <RESULT> Type of query result
 */
public class LambdaQuery<RESULT> extends JPALambdaQueryBase<RESULT> {

    protected Condition whereCond;

    public LambdaQuery(EntityManager em) {
        super(em);
    }

    @Override
    public LambdaQuery<RESULT> from(Alias<?> root) {
        super.from(root);
        return this;
    }

    @Override
    public LambdaQuery<RESULT> select(Alias<?> a) {
        super.select(a);
        return this;
    }

    public <SELECTED> LambdaQuery<RESULT> select(Selector<SELECTED> selector) {
        return this;
    }

    public LambdaQuery<RESULT> where(Condition e) {
        this.whereCond = e;
        return this;
    }

    public <SELECTED> LambdaQuery<RESULT> groupBy(Selector<SELECTED> selector) {
        return this;
    }

    public List<RESULT> getResultList() {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery q = cb.createQuery();

        List<SelectionInstance> allAliases = initAliases(q);

        // TODO support more aliases
        SelectionInstance aliasInstance = roots.get(0);
        Root<?> p = aliasInstance.getInstance();
        q.select(p);
        if (whereCond != null) {
            WhereCriteriaVisitor whereCriteriaVisitor = new WhereCriteriaVisitor(cb, q, allAliases);
            LambdaExpression.parse(whereCond).accept(whereCriteriaVisitor);
            q.where(whereCriteriaVisitor.getJpaExpression());
        }
        List<RESULT> persons = em.createQuery(q)
                .getResultList();
        return persons;
    }

}
