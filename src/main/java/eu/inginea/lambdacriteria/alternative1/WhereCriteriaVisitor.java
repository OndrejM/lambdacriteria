package eu.inginea.lambdacriteria.alternative1;

import com.trigersoft.jaque.expression.BinaryExpression;
import com.trigersoft.jaque.expression.ConstantExpression;
import com.trigersoft.jaque.expression.Expression;
import com.trigersoft.jaque.expression.ExpressionType;
import com.trigersoft.jaque.expression.InvocationExpression;
import com.trigersoft.jaque.expression.LambdaExpression;
import com.trigersoft.jaque.expression.MemberExpression;
import com.trigersoft.jaque.expression.ParameterExpression;
import com.trigersoft.jaque.expression.SimpleExpressionVisitor;
import com.trigersoft.jaque.expression.UnaryExpression;
import eu.inginea.lambdacriteria.Alias;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

class WhereCriteriaVisitor extends SimpleExpressionVisitor {

    private CriteriaBuilder cb;
    private CriteriaQuery q;
    private Map<Alias, LambdaQuery.AliasInstance> aliases = new HashMap<>();
    private javax.persistence.criteria.Expression jpaExpression;
    // processing invocation parameter with this index
    private Integer currentParameterIndex = null;

    public WhereCriteriaVisitor(CriteriaBuilder cb, CriteriaQuery q, List<LambdaQuery.AliasInstance> aliasInstances) {
        this.cb = cb;
        this.q = q;
        for (LambdaQuery.AliasInstance aliasInstance : aliasInstances) {
            aliases.put(aliasInstance.getAlias(), aliasInstance);
        }
    }

    public javax.persistence.criteria.Expression getJpaExpression() {
        return jpaExpression;
    }

    @Override
    public Expression visit(UnaryExpression e) {
        Expression visitResult = super.visit(e); //To change body of generated methods, choose Tools | Templates.
        info(e, false);
        return visitResult;
    }

    @Override
    public Expression visit(ParameterExpression e) {
        Expression visitResult = super.visit(e); //To change body of generated methods, choose Tools | Templates.
        e.getIndex();
        info(e, false);
        return visitResult;
    }

    @Override
    public Expression visit(MemberExpression e) {
        Expression visitResult = super.visit(e); //To change body of generated methods, choose Tools | Templates.
        Root<?> root = aliases.values().iterator().next().getInstance();
        
        jpaExpression = root.get("name");
        
        info(e, true);
        return visitResult;
    }

    @Override
    public Expression visit(LambdaExpression<?> e) {
        Expression visitResult = super.visit(e); //To change body of generated methods, choose Tools | Templates.
        //info(e);
        return visitResult;
    }

    @Override
    public Expression visit(InvocationExpression e) {
        currentParameterIndex = 0;
        for (Expression arg : e.getArguments()) {
            arg.accept(this);
            currentParameterIndex++;
        }
        currentParameterIndex = null;
        Expression visitResult = e.getTarget().accept(this);
        info(e, false);
        return visitResult;
    }

    @Override
    public Expression visit(ConstantExpression e) {
        Expression visitResult = super.visit(e); //To change body of generated methods, choose Tools | Templates.
        if (Alias.class.isAssignableFrom(e.getResultType())) {
            LambdaQuery.AliasInstance aliasInstance = aliases.get(e.getValue());
            info("Alias instance: " + aliasInstance, true);
            info(e, true);
        } else {
            jpaExpression = cb.literal(e.getValue());
            info(e, true);
        }
        return visitResult;
    }

    private void info(Object loggedValue, boolean parsed) {
        final boolean logOnlyParsed = false;
        if (logOnlyParsed && !parsed) {
            return;
        }
        if (loggedValue != null) {
            if (loggedValue instanceof Expression) {
                System.out.println("[" + loggedValue.getClass().getSimpleName()
                    + (parsed ? "*" : "")
                    + "]: " + loggedValue);
            } else {
                System.out.println("MESSAGE:" + loggedValue);
            }
        } else {
            System.out.println(">>NULL<<");
        }
    }

}
