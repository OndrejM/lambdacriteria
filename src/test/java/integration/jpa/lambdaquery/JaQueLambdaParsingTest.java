package integration.jpa.lambdaquery;

import eu.inginea.lambdacriteria.base.LoggingQueryExpressionVisitor;
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
import integration.jpa.model.Person;
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
        parse(() -> p.val.getName().equals("Ondro"));
    }

    public interface Callable extends java.util.concurrent.Callable<Object>, Serializable {

    }

    private void parse(Callable lambdaExpr) {
        LambdaExpression.parse(lambdaExpr)
                .accept(new LoggingQueryExpressionVisitor());
    }


}
