package eu.inginea.lambdacriteria.alternative2;

import eu.inginea.lambdacriteria.base.AliasInstance;
import eu.inginea.lambdacriteria.alternative1.*;
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

class WhereCriteriaVisitor2 extends JPAWhereCriteriaVisitorBase {

    // processing invocation parameter with this index
    private Integer currentParameterIndex = null;

    public WhereCriteriaVisitor2(CriteriaBuilder cb, CriteriaQuery q, List<AliasInstance> aliasInstances) {
        super(cb, q, aliasInstances);
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
        Expression visitResult = super.visit(e);
        Root<?> root = aliases.values().iterator().next().getInstance();
        
        jpaExpression = root.get("name");
        
        info(e, true);
        return visitResult;
    }

    @Override
    public Expression visit(LambdaExpression<?> e) {
        Expression visitResult = super.visit(e);
        info(e, false);
        return visitResult;
    }

    @Override
    public Expression visit(InvocationExpression e) {
        Expression visitResult = e.getTarget().accept(this);
        info(e, false);
        return visitResult;
    }

    @Override
    public Expression visit(ConstantExpression e) {
        Expression visitResult = super.visit(e); //To change body of generated methods, choose Tools | Templates.
        if (Alias.class.isAssignableFrom(e.getResultType())) {
            AliasInstance aliasInstance = aliases.get(e.getValue());
            info("Alias instance: " + aliasInstance, true);
            info(e, true);
        } else {
            jpaExpression = cb.literal(e.getValue());
            info(e, true);
        }
        return visitResult;
    }



}
