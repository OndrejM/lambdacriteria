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
class LambdaVisitor extends QueryExpressionVisitor {
    
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
        int paramIndex = e.getIndex();
        Expression visitResult = super.visit(e);
        return visitResult;
    }

    @Override
    public Expression visit(MemberExpression e) {
        Expression visitResult = null;

        boolean resolved = false;
        resolved = resolved || (visitResult = resolveFunction(e)) != null;
        resolved = resolved || (visitResult = resolveProperty(e)) != null;
        resolved = resolved || (visitResult = super.visit(e)) != null;
        
        if (!resolved) {
            throw new RuntimeException("Member expression not resolved");
        }
        
        return visitResult;
    }

    private Expression resolveFunction(MemberExpression e) {
        Member member = e.getMember();
        Optional<Literal> literal = queryMapping.getTermForExpression(member);
        if (literal.isPresent()) {
            Expression visitResult = super.visit(e);
            queryVisitor.visit(literal.get());
            return visitResult;
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
                if (e.getInstance() instanceof ParameterExpression) {
                    ParameterExpression param = (ParameterExpression)e.getInstance();
                    queryVisitor.visit(new Parameter(param.getIndex()));
                }
                queryVisitor.visit(new Path("Property " 
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
        currentParameterIndex = null;
        Expression visitResult = e.getTarget().accept(this);
        for (Expression arg : e.getArguments()) {
            if (!topLevelInvocationExpressionVisited) {
                currentParameterIndex = (currentParameterIndex == null) ? 0 : currentParameterIndex + 1;
            }
            arg.accept(this);
        }
        topLevelInvocationExpressionVisited = true;
        currentParameterIndex = null;
        return visitResult;
    }

    @Override
    public Expression visit(ConstantExpression e) {
        queryVisitor.visit(new Constant(e.getValue() + ":" + e.getResultType().getSimpleName()));
        Expression visitResult = super.visit(e);
        return visitResult;
    }

    @Override
    public Expression visit(BinaryExpression e) {
        Expression visitResult = e;

        Queue<javax.persistence.criteria.Expression> expressions = new LinkedList<>();
        e.getFirst().accept(this);
        e.getSecond().accept(this);

        return visitResult;
    }

    public void setQueryMapping(QueryMapping queryMapping) {
        this.queryMapping = queryMapping;
    }

    public void setQueryVisitor(QueryVisitor queryVisitor) {
        this.queryVisitor = queryVisitor;
    }

    protected void infoParsed(Object get) {
        // empty, to be extended if we want info
    }

}
