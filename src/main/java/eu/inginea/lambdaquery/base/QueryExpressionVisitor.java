package eu.inginea.lambdaquery.base;

import com.trigersoft.jaque.expression.Expression;
import com.trigersoft.jaque.expression.InvocableExpression;
import com.trigersoft.jaque.expression.InvocationExpression;
import com.trigersoft.jaque.expression.SimpleExpressionVisitor;
import java.util.List;

public class QueryExpressionVisitor extends SimpleExpressionVisitor {

    public QueryExpressionVisitor() {
    }

    /**
     * Arguments visited first, then target
     */
    @Override
    public Expression visit(InvocationExpression e) {
        List<Expression> args = visitExpressionList(e.getArguments());
        Expression expr = e.getTarget().accept(this);
        if (args != e.getArguments() || expr != e.getTarget()) {
            return Expression.invoke((InvocableExpression) expr, args);
        }
        return e;
    }

}
