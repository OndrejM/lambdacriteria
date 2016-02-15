package eu.inginea.lambdacriteria.streamQuery;

import com.trigersoft.jaque.expression.*;
import com.trigersoft.jaque.expression.Expression;
import com.trigersoft.jaque.expression.ParameterExpression;
import eu.inginea.lambdacriteria.base.*;
import java.beans.*;
import java.lang.reflect.Member;
import java.util.*;
import java.util.logging.*;

/**
 * In final version, can extend QueryExpressionVisitor directly to avoid debug logging
 */
class LambdaVisitor extends LoggingQueryExpressionVisitor {
    
    private QueryMapping queryMapping;
    private QueryVisitor queryVisitor;

    // processing invocation parameter with this index
    private Integer currentParameterIndex = null;
    // flag to ignore first InvocationExpression, as it is only a wrapper and is not needed
    private boolean topLevelInvocationExpressionVisited = false;

    public LambdaVisitor() {
    }

    LambdaVisitor(StreamOperation streamOperation) {
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
        return visitResult;
    }

    @Override
    public Expression visit(MemberExpression e) {
        Expression visitResult = null;
        infoParsed(e);

        boolean resolved = false;
        resolved = resolved || (visitResult = resolveFunction(e)) != null;
        resolved = resolved || (visitResult = resolveProperty(e)) != null;
        resolved = resolved || (visitResult = super.visit(e)) != null;
        
        if (!resolved) {
            throw new RuntimeException("Member expression not resolved");
        }
        
        clearInfoParsed();
        return visitResult;
    }

    private Expression resolveFunction(MemberExpression e) {
        Member member = e.getMember();
        Optional<Literal> literal = queryMapping.getTermForExpression(member);
        if (literal.isPresent()) {
            queryVisitor.visit(literal.get());
            return super.visit(e);
        } else {
            return null;
        }
    }
    
    private Expression resolveProperty(MemberExpression e) {
        Member member = e.getMember();
        List<PropertyDescriptor> propertyDescriptors = null;
        try {
            propertyDescriptors = Arrays.asList(Introspector.getBeanInfo(member.getDeclaringClass()).getPropertyDescriptors());
        } catch (IntrospectionException ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
        }
        if (propertyDescriptors != null) {
            Optional<PropertyDescriptor> propertyDesc = propertyDescriptors
                    .stream()
                    .filter(x -> {
                        return x.getReadMethod().equals(member);
                    })
                    .findAny();
            if (propertyDesc.isPresent()) {
                infoParsed(propertyDesc.get());
                Expression visitResult = super.visit(e);
                queryVisitor.visit(new Path("Property " 
                        + "P_" + ((ParameterExpression)e.getInstance()).getIndex() + "." 
                        + propertyDesc.get().getName()));
                return visitResult;
            } else {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, 
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
        queryVisitor.visit(new Constant(e.getValue() + ":" + e.getResultType().getSimpleName()));
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
        e.getSecond().accept(this);

        level--;
        clearInfoParsed();
        return visitResult;
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

    public void setQueryMapping(QueryMapping queryMapping) {
        this.queryMapping = queryMapping;
    }

    public void setQueryVisitor(QueryVisitor queryVisitor) {
        this.queryVisitor = queryVisitor;
    }



}
