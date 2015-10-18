package eu.inginea.lambdacriteria.base;

import com.trigersoft.jaque.expression.BinaryExpression;
import com.trigersoft.jaque.expression.ConstantExpression;
import com.trigersoft.jaque.expression.Expression;
import com.trigersoft.jaque.expression.InvocableExpression;
import com.trigersoft.jaque.expression.InvocationExpression;
import com.trigersoft.jaque.expression.LambdaExpression;
import com.trigersoft.jaque.expression.MemberExpression;
import com.trigersoft.jaque.expression.ParameterExpression;
import com.trigersoft.jaque.expression.SimpleExpressionVisitor;
import com.trigersoft.jaque.expression.UnaryExpression;
import java.util.List;

public class QueryExpressionVisitor extends SimpleExpressionVisitor {

    protected int level = 0;
    
    public QueryExpressionVisitor() {
    }

    /**
     * Arguments visited first, then target
     */
    @Override
    public Expression visit(InvocationExpression e) {
        level++;
        List<Expression> args = visitExpressionList(e.getArguments());
        Expression expr = e.getTarget().accept(this);
        if (args != e.getArguments() || expr != e.getTarget()) {
            return Expression.invoke((InvocableExpression) expr, args);
        }
        level--;
        return e;
    }

        @Override
        public Expression visit(UnaryExpression e) {
            level++;
            Expression visitResult = super.visit(e);
            level--;
            return visitResult; //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public Expression visit(ParameterExpression e) {
            level++;
            Expression visitResult = super.visit(e);
            level--;
            return visitResult; //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public Expression visit(MemberExpression e) {
            level++;
            Expression visitResult = super.visit(e);
            level--;
            return visitResult; //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public Expression visit(LambdaExpression<?> e) {
            level++;
            Expression visitResult = super.visit(e);
            level--;
            return visitResult; //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public Expression visit(ConstantExpression e) {
            level++;
            Expression visitResult = super.visit(e);
            level--;
            return visitResult; //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public Expression visit(BinaryExpression e) {
            level++;
            Expression visitResult = super.visit(e);
            level--;
            return visitResult; //To change body of generated methods, choose Tools | Templates.
        }

}
