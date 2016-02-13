package eu.inginea.lambdacriteria.streamQuery;

import com.trigersoft.jaque.expression.*;
import com.trigersoft.jaque.expression.Expression;
import com.trigersoft.jaque.expression.ParameterExpression;
import eu.inginea.lambdacriteria.base.*;
import java.util.*;

/**
 * In final version, can extend QueryExpressionVisitor directly to avoid debug logging
 */
class LambdaVisitor extends LoggingQueryExpressionVisitor {

    // processing invocation parameter with this index
    private Integer currentParameterIndex = null;
    private boolean topLevelInvocationExpressionVisited = false;

    public LambdaVisitor() {
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
        
        clearInfoParsed();
        return visitResult;
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

}
