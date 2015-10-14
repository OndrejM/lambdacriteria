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

public class WhereCriteriaVisitor extends SimpleExpressionVisitor {

    public WhereCriteriaVisitor() {
    } 

    @Override
    public Expression visit(UnaryExpression e) {
        info(e);
        return super.visit(e); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Expression visit(ParameterExpression e) {
        info(e);
        return super.visit(e); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Expression visit(MemberExpression e) {
        info(e);
        return super.visit(e); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Expression visit(LambdaExpression<?> e) {
        info(e);
        return super.visit(e); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Expression visit(InvocationExpression e) {
        info(e);
        return super.visit(e); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Expression visit(ConstantExpression e) {
        info(e);
        return super.visit(e); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Expression visit(BinaryExpression e) {
        info(e);
        return super.visit(e); //To change body of generated methods, choose Tools | Templates.
    }

    private void info(Object loggedValue) {
        if (loggedValue != null) {
            System.out.println("[" + loggedValue.getClass().getSimpleName() + "]: " + loggedValue);
        } else {
            System.out.println(">>NULL<<");
        }
    }

}
