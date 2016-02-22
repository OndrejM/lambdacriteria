package unit.lambdaparsing;

import eu.inginea.lambdacriteria.base.QueryExpressionVisitor;
import com.trigersoft.jaque.expression.LambdaExpression;
import eu.inginea.lambdacriteria.streamQuery.*;
import java.io.Serializable;
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
        parse(() -> new Person().getName().equals("Ondro"));
    }

    public interface Callable extends java.util.concurrent.Callable<Object>, Serializable {

    }

    private void parse(Callable lambdaExpr) {
        LambdaExpression.parse(lambdaExpr)
                .accept(new LoggingExpressionVisitor());
    }


}
