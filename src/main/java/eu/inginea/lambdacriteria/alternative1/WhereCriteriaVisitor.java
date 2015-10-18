package eu.inginea.lambdacriteria.alternative1;

import eu.inginea.lambdacriteria.base.SelectionInstance;
import com.trigersoft.jaque.expression.BinaryExpression;
import com.trigersoft.jaque.expression.ConstantExpression;
import com.trigersoft.jaque.expression.Expression;
import com.trigersoft.jaque.expression.ExpressionType;
import com.trigersoft.jaque.expression.InvocationExpression;
import com.trigersoft.jaque.expression.LambdaExpression;
import com.trigersoft.jaque.expression.MemberExpression;
import com.trigersoft.jaque.expression.ParameterExpression;
import com.trigersoft.jaque.expression.UnaryExpression;
import eu.inginea.lambdacriteria.Alias;
import eu.inginea.lambdacriteria.base.ExpressionInfo;
import eu.inginea.lambdacriteria.base.JpaOperationType;
import eu.inginea.lambdacriteria.base.LoggingQueryExpressionVisitor;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Member;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Queue;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;

class WhereCriteriaVisitor extends LoggingQueryExpressionVisitor {

    private CriteriaBuilder cb;
    private CriteriaQuery q;
    private Map<Alias, SelectionInstance> aliases = new HashMap<>();
    private ExpressionInfo expressionInfo = new ExpressionInfo();
    // processing invocation parameter with this index
    private Integer currentParameterIndex = null;
    private boolean topLevelInvocationExpressionVisited = false;

    public WhereCriteriaVisitor(CriteriaBuilder cb, CriteriaQuery q, List<SelectionInstance> aliasInstances) {
        this.cb = cb;
        this.q = q;
        for (SelectionInstance aliasInstance : aliasInstances) {
            aliases.put(aliasInstance.getAlias(), aliasInstance);
        }
    }

    public javax.persistence.criteria.Expression getJpaExpression() {
        return expressionInfo.getJpaExpression();
    }

    @Override
    public Expression visit(UnaryExpression e) {
        Expression visitResult = super.visit(e); //To change body of generated methods, choose Tools | Templates.
        return visitResult;
    }

    @Override
    public Expression visit(ParameterExpression e) {
        infoParsed(e);
        Expression visitResult = super.visit(e);
        clearInfoParsed();
        int paramIndex = e.getIndex();
        Optional<SelectionInstance> alias = aliases.values().stream().filter(x -> {
            Integer idx = x.getParameterIndex();
            return idx != null && idx.equals(paramIndex);
        }).findFirst();
        if (alias.isPresent()) {
            setJpaExpression(alias.get().getInstance());
        }
        return visitResult;
    }

    @Override
    public Expression visit(MemberExpression e) {
        Expression visitResult = null;
        infoParsed(e);

        boolean resolved = false;
        
        resolved = resolved || (visitResult = resolveAliasVal(e)) != null;
        resolved = resolved || (visitResult = resolveFunction(e)) != null;
        resolved = resolved || (visitResult = resolveProperty(e)) != null;
        
        clearInfoParsed();
        return visitResult;
    }

    private Expression resolveAliasVal(MemberExpression e) {
        Member member = e.getMember();
        
        if (Alias.class.isAssignableFrom(member.getDeclaringClass())) {
            try {
                if (member.equals(Alias.class.getField("val"))) {
                    // just pass expression further
                    return super.visit(e);
                }
            } catch (NoSuchFieldException | SecurityException ex) {
                Logger.getLogger(WhereCriteriaVisitor.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return null;
    }

    private Expression resolveFunction(MemberExpression e) {
        Member member = e.getMember();
        switch (member.getName()) {
            case "equals":
                Expression visitResult = super.visit(e);
                List<javax.persistence.criteria.Expression> expressions = new ArrayList<>();
                expressions.add(getJpaExpression());
                return visitResult;
        }
        return null;
    }
    
    private Expression resolveProperty(MemberExpression e) {
        Member member = e.getMember();
        List<PropertyDescriptor> propertyDescriptors = null;
        try {
            propertyDescriptors = Arrays.asList(Introspector.getBeanInfo(member.getDeclaringClass()).getPropertyDescriptors());
        } catch (IntrospectionException ex) {
            Logger.getLogger(WhereCriteriaVisitor.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (propertyDescriptors != null) {
            Optional<PropertyDescriptor> propertyDesc = propertyDescriptors
                    .stream()
                    .filter(x -> {
                        return x.getReadMethod().equals(member);
                    })
                    .findFirst();
            if (propertyDesc.isPresent()) {
                infoParsed(propertyDesc);
                Expression visitResult = super.visit(e);
                if (getJpaExpression() instanceof Path) {
                    Path path = (Path)getJpaExpression();
                    setJpaExpression(path.get(propertyDesc.get().getName()));
                } else {
                    Logger.getLogger(WhereCriteriaVisitor.class.getName()).log(Level.SEVERE, 
                            "Syntax error: Previous expression is not a criteria path: " + getJpaExpression());
                }
                return visitResult;
            } else {
                Logger.getLogger(WhereCriteriaVisitor.class.getName()).log(Level.SEVERE, 
                        "Method " + member + " is not a getter");
            }
        }
        return null;
    }

    @Override
    public Expression visit(LambdaExpression<?> e) {
        Expression visitResult = super.visit(e); //To change body of generated methods, choose Tools | Templates.
        return visitResult;
    }

    @Override
    public Expression visit(InvocationExpression e) {
        infoParsed(e);
        clearInfoParsed();
        level++;
        currentParameterIndex = null;
        for (Expression arg : e.getArguments()) {
            if (!topLevelInvocationExpressionVisited) {
                currentParameterIndex = (currentParameterIndex == null) ? 0 : currentParameterIndex + 1;
            }
            arg.accept(this);
        }
        topLevelInvocationExpressionVisited = true;
        currentParameterIndex = null;
        Expression visitResult = e.getTarget().accept(this);
        level--;
        return visitResult;
    }

    @Override
    public Expression visit(ConstantExpression e) {
        infoParsed(e);
        Expression visitResult = super.visit(e);
        if (Alias.class.isAssignableFrom(e.getResultType())) {
            SelectionInstance aliasInstance = aliases.get((Alias) e.getValue());
            if (currentParameterIndex != null) {
                aliasInstance.setParameterIndex(currentParameterIndex);
                infoParsed("Alias instance at index " + currentParameterIndex + ": " + aliasInstance);
            } else {
                infoParsed("Alias instance: " + aliasInstance);
            }
        } else {
            setJpaExpression(cb.literal(e.getValue()));
        }
        clearInfoParsed();
        return visitResult;
    }

    @Override
    public Expression visit(BinaryExpression e) {
        infoParsed(e);
        clearInfoParsed();
        level++;
        Expression visitResult = e;

        Queue<javax.persistence.criteria.Expression> expressions = new LinkedList<>();
        e.getFirst().accept(this);
        expressions.add(getJpaExpression());
        e.getSecond().accept(this);
        expressions.add(getJpaExpression());

        setJpaExpression(resolveJpaOpeation(expressions, JpaOperationType.valueFromJaQueOperationType(ExpressionType.Equal)));
        level--;
        clearInfoParsed();
        return visitResult;
    }

    private javax.persistence.criteria.Expression<?> resolveJpaOpeation(Collection<javax.persistence.criteria.Expression> expressions, 
            JpaOperationType operation) {
        Iterator<javax.persistence.criteria.Expression> itExpr = expressions.iterator();
        switch (operation) {
            case EQUALS:
                return cb.equal(itExpr.next(), itExpr.next());
            default:
                throw new UnsupportedOperationException("" + operation);
        }
    }

    private final boolean logOnlyParsed = false;
    private boolean lastLoggedParsed = false;

    @Override
    protected void info(Object loggedValue) {
        if (!logOnlyParsed && !lastLoggedParsed) {
            super.info(loggedValue);
        }
        lastLoggedParsed = false;
    }

    private void infoParsed(Object loggedValue) {
        lastLoggedParsed = true;
        String prefix = getPrefix();
        System.out.println(prefix + "*" + createLogMessage(loggedValue));
    }

    private void clearInfoParsed() {
        lastLoggedParsed = false;
    }

    private void setJpaExpression(javax.persistence.criteria.Expression jpaExpression) {
        this.expressionInfo.addJpaExpression(jpaExpression);
    }

}
