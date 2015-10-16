package eu.inginea.lambdacriteria.alternative2;

import com.trigersoft.jaque.expression.BinaryExpression;
import com.trigersoft.jaque.expression.ExpressionType;
import com.trigersoft.jaque.expression.SimpleExpressionVisitor;
import eu.inginea.lambdacriteria.Alias;
import eu.inginea.lambdacriteria.alternative1.LambdaQuery;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;

public abstract class JPAWhereCriteriaVisitorBase extends SimpleExpressionVisitor {
    protected CriteriaBuilder cb;
    protected CriteriaQuery q;
    protected Map<Alias, LambdaQuery.AliasInstance> aliases = new HashMap<>();
    protected Expression jpaExpression;

    public JPAWhereCriteriaVisitorBase(CriteriaBuilder cb, CriteriaQuery q, List<LambdaQuery.AliasInstance> aliasInstances) {
        this.cb = cb;
        this.q = q;
        for (LambdaQuery.AliasInstance aliasInstance : aliasInstances) {
            aliases.put(aliasInstance.getAlias(), aliasInstance);
        }
    }

    public Expression getJpaExpression() {
        return jpaExpression;
    }

    protected void info(Object loggedValue, boolean parsed) {
        final boolean logOnlyParsed = false;
        if (logOnlyParsed && !parsed) {
            return;
        }
        if (loggedValue != null) {
            if (loggedValue instanceof com.trigersoft.jaque.expression.Expression) {
                System.out.println("[" + loggedValue.getClass().getSimpleName() + (parsed ? "*" : "") + "]: " + loggedValue);
            } else {
                System.out.println("MESSAGE:" + loggedValue);
            }
        } else {
            System.out.println(">>NULL<<");
        }
    }

    @Override
    public com.trigersoft.jaque.expression.Expression visit(BinaryExpression e) {
        com.trigersoft.jaque.expression.Expression visitResult = e;
        Queue<Expression> expressions = new LinkedList<>();
        e.getFirst().accept(this);
        expressions.add(jpaExpression);
        e.getSecond().accept(this);
        expressions.add(jpaExpression);
        switch (e.getExpressionType()) {
            case ExpressionType.Equal:
                jpaExpression = cb.equal(expressions.remove(), expressions.remove());
        }
        info(e, true);
        return visitResult;
    }

}
