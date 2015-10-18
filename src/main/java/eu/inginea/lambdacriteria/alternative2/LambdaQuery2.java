package eu.inginea.lambdacriteria.alternative2;

import com.trigersoft.jaque.expression.LambdaExpression;
import eu.inginea.lambdacriteria.Alias;
import eu.inginea.lambdacriteria.base.SelectionInstance;
import eu.inginea.lambdacriteria.alternative1.Condition;
import eu.inginea.lambdacriteria.base.JPALambdaQueryBase;
import eu.inginea.lambdacriteria.alternative1.LambdaQuery;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import ondrom.experiments.jpa.Person;

public class LambdaQuery2<T> extends JPALambdaQueryBase<T> {
    
    protected Condition2<?> whereCondition;
    
    public LambdaQuery2(EntityManager em) {
        super(em);
    }

    @Override
    public LambdaQuery2<T> from(Alias<?>... rootAliases) {
        super.from(rootAliases);
        return this;
    }

    @Override
    public LambdaQuery2<T> select(Alias<?>... a) {
        super.select(a);
        return this;
    }

    public <ENTITY> LambdaQuery2<T> where(Alias<ENTITY> a, Condition2<ENTITY> cond) {
        this.whereCondition = cond;
        return this;
    }

    public List<Person> getResultList() {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery q = cb.createQuery();

        List<SelectionInstance> aliasInstances = initAliases(q);
        
        LambdaExpression<Condition2> parsed = LambdaExpression.parse(whereCondition);
        
        return null;
    }

}
