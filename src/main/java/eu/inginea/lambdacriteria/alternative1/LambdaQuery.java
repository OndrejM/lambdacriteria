package eu.inginea.lambdacriteria.alternative1;

import eu.inginea.lambdacriteria.base.JPALambdaQueryBase;
import com.trigersoft.jaque.expression.BinaryExpression;
import com.trigersoft.jaque.expression.Expression;
import com.trigersoft.jaque.expression.InvocationExpression;
import com.trigersoft.jaque.expression.LambdaExpression;
import com.trigersoft.jaque.expression.UnaryExpression;
import eu.inginea.lambdacriteria.Alias;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

/**
 * Main object to build query
 * @param <T> Type of query result
 */
public class LambdaQuery<T> extends JPALambdaQueryBase<T> {

    protected Condition whereCond;

    public LambdaQuery(EntityManager em) {
        super(em);
    }

    @Override
    public LambdaQuery<T> from(Alias<?>... rootAliases) {
        super.from(rootAliases);
        return this;
    }

    @Override
    public LambdaQuery<T> select(Alias<?>... a) {
        super.select(a);
        return this;
    }


    public LambdaQuery<T> where(Condition e) {
        this.whereCond = e;
        return this;
    }

    @Deprecated
    private void parseExpressionManually(LambdaExpression<Condition> parsed) {
        Expression body = parsed.getBody();
        Expression methodCall = body;

        // remove casts
        while (methodCall instanceof UnaryExpression) {
            methodCall = ((UnaryExpression) methodCall).getFirst();
        }

        // checks are omitted for brevity
        UnaryExpression lambdaBody = (UnaryExpression) ((LambdaExpression) ((InvocationExpression) methodCall)
                .getTarget()).getBody();

        BinaryExpression binaryExpr = (BinaryExpression) lambdaBody.getFirst();
        Expression operator = binaryExpr.getOperator();
    }

    public static class AliasInstance {
        private Alias<?> alias;
        private Root<?> instance;
        private int parameterIndex;

        public AliasInstance(Alias<?> alias, Root<?> instance) {
            this.alias = alias;
            this.instance = instance;
        }

        public Alias<?> getAlias() {
            return alias;
        }

        public Root<?> getInstance() {
            return instance;
        }

        public int getParameterIndex() {
            return parameterIndex;
        }

        public void setParameterIndex(int parameterIndex) {
            this.parameterIndex = parameterIndex;
        }
        
    }
    
    public List<T> getResultList() {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery q = cb.createQuery();

        List<AliasInstance> aliasInstances = initAliasInstances(q);
        
        LambdaExpression<Condition> parsed = LambdaExpression.parse(whereCond);
        WhereCriteriaVisitor whereCriteriaVisitor = new WhereCriteriaVisitor(cb, q, aliasInstances);
        parsed.accept(whereCriteriaVisitor);
        
        // TODO support more aliases
        AliasInstance aliasInstance = aliasInstances.get(0);
        Root<?> p = aliasInstance.instance;
        q.select(p).where(whereCriteriaVisitor.getJpaExpression());
        List<T> persons = em.createQuery(q)
                .setParameter("name", "Ondro")
                .getResultList();
        return persons;
    }

}
