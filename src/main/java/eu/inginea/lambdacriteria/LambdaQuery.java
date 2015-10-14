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
import java.lang.reflect.Member;
import java.util.List;
import javax.persistence.EntityManager;
import ondrom.experiments.jpa.Person;

public class LambdaQuery<T> {

    private EntityManager em;

    public LambdaQuery(EntityManager em) {
        this.em = em;
    }

    public LambdaQuery select(Alias<?>... a) {
        return this;
    }

    public LambdaQuery from(Alias<?>... a) {
        return this;
    }

    public LambdaQuery where(Condition e) {
        LambdaExpression<Condition> parsed = LambdaExpression
                .parse(e);
        Expression body = parsed.getBody();
        Expression methodCall = body;

        // remove casts
        while (methodCall instanceof UnaryExpression) {
            methodCall = ((UnaryExpression) methodCall).getFirst();
        }

        // checks are omitted for brevity
        UnaryExpression lambdaBody = (UnaryExpression) ((LambdaExpression) ((InvocationExpression) methodCall)
                .getTarget()).getBody();

        BinaryExpression binaryExpr = (BinaryExpression) lambdaBody.getFirst();
        Expression operator = binaryExpr.getOperator();

        parsed.accept(new SimpleExpressionVisitor() {

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
                    System.out.println("["+ loggedValue.getClass().getSimpleName() + "]: " + loggedValue);
                } else {
                    System.out.println(">>NULL<<");
                }
            }

        });

        return this;
    }

    public List<T> getResultList() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
