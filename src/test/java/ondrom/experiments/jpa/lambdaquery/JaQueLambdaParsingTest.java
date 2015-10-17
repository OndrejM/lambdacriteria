package ondrom.experiments.jpa.lambdaquery;

import eu.inginea.lambdacriteria.base.QueryExpressionVisitor;
import com.trigersoft.jaque.expression.BinaryExpression;
import com.trigersoft.jaque.expression.ConstantExpression;
import com.trigersoft.jaque.expression.Expression;
import com.trigersoft.jaque.expression.InvocationExpression;
import com.trigersoft.jaque.expression.LambdaExpression;
import com.trigersoft.jaque.expression.MemberExpression;
import com.trigersoft.jaque.expression.ParameterExpression;
import com.trigersoft.jaque.expression.UnaryExpression;
import eu.inginea.lambdacriteria.Alias;
import java.io.Serializable;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import ondrom.experiments.jpa.Person;
import org.junit.Test;

/**
 *
 * @author ondro
 */
public class JaQueLambdaParsingTest {

    public JaQueLambdaParsingTest() {
    }

    @Test
    public void canParseExpr() {
        Alias<Person> p = new Alias<>(Person.class);
        parse(() -> p.val.getName() == "Ondro");
    }

    public interface Callable extends java.util.concurrent.Callable<Object>, Serializable {

    }

    private void parse(Callable lambdaExpr) {
        LambdaExpression.parse(lambdaExpr)
                .accept(new LoggingQueryExpressionVisitor());
    }


    private static class LoggingQueryExpressionVisitor extends QueryExpressionVisitor {

        private Consumer<String> logger = System.out::println;
        private int level = 0;

        public LoggingQueryExpressionVisitor() {
        }

        @Override
        public Expression visit(UnaryExpression e) {
            level++;
            info(e);
            Expression visitResult = super.visit(e);
            level--;
            return visitResult; //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public Expression visit(ParameterExpression e) {
            level++;
            info(e);
            Expression visitResult = super.visit(e);
            level--;
            return visitResult; //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public Expression visit(MemberExpression e) {
            level++;
            info(e);
            Expression visitResult = super.visit(e);
            level--;
            return visitResult; //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public Expression visit(LambdaExpression<?> e) {
            level++;
            info(e);
            Expression visitResult = super.visit(e);
            level--;
            return visitResult; //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public Expression visit(InvocationExpression e) {
            level++;
            info(e);
            Expression visitResult = super.visit(e);
            level--;
            return visitResult; //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public Expression visit(ConstantExpression e) {
            level++;
            info(e);
            Expression visitResult = super.visit(e);
            level--;
            return visitResult; //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public Expression visit(BinaryExpression e) {
            level++;
            info(e);
            Expression visitResult = super.visit(e);
            level--;
            return visitResult; //To change body of generated methods, choose Tools | Templates.
        }

        private void info(Object loggedValue) {
            String msg = null;
            if (loggedValue != null) {
                if (loggedValue instanceof Expression) {
                    msg = "[" + loggedValue.getClass().getSimpleName()
                            + "]: " + loggedValue;
                } else {
                    msg = "MESSAGE:" + loggedValue;
                }
            } else {
                msg = ">>NULL<<";
            }
            String prefix = IntStream.range(0, level).mapToObj(i -> "-").collect(Collectors.joining());
            logger.accept(prefix + msg);
        }


    }
}
