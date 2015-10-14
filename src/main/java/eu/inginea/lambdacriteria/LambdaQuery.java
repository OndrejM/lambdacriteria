package eu.inginea.lambdacriteria;

import com.trigersoft.jaque.expression.BinaryExpression;
import com.trigersoft.jaque.expression.ConstantExpression;
import com.trigersoft.jaque.expression.Expression;
import com.trigersoft.jaque.expression.InvocationExpression;
import com.trigersoft.jaque.expression.LambdaExpression;
import com.trigersoft.jaque.expression.MemberExpression;
import com.trigersoft.jaque.expression.ParameterExpression;
import com.trigersoft.jaque.expression.SimpleExpressionVisitor;
import com.trigersoft.jaque.expression.UnaryExpression;
import java.lang.reflect.Member;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import ondrom.experiments.jpa.Person;

public class LambdaQuery<T> {

    private EntityManager em;
    Alias<?>[] roots;
    Alias<?>[] selects;

    public LambdaQuery(EntityManager em) {
        this.em = em;
    }

    public LambdaQuery select(Alias<?>... a) {
        this.selects = a; // to be extended to support more than aliased entity in selection
        return this;
    }

    public LambdaQuery from(Alias<?>... rootAliases) {
        this.roots = rootAliases;
        return this;
    }

    public LambdaQuery where(Condition e) {
        LambdaExpression<Condition> parsed = LambdaExpression
                .parse(e);
        //parseExpressionManually(parsed);

        parsed.accept(new WhereCriteriaVisitor());

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

    
    private class AliasInstance {
        private Alias<?> alias;
        private Root<?> instance;

        public AliasInstance(Alias<?> alias, Root<?> instance) {
            this.alias = alias;
            this.instance = instance;
        }
        
    }
    
    public List<T> getResultList() {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery q = cb.createQuery();
        AliasInstance aliasInstance = null;
        for (Alias<?> alias : roots) {
            aliasInstance = new AliasInstance(alias, q.from(alias.getEntityClass()));
            // TODO support more aliases
        }
        
        Root<?> p = aliasInstance.instance;
        q.select(p).where(cb.equal(p.get("name"), cb.parameter(String.class, "name")));
        List<T> persons = em.createQuery(q)
                .setParameter("name", "Ondro")
                .getResultList();
        return persons;
    }

    
}
