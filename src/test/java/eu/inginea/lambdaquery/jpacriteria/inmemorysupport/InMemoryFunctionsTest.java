package eu.inginea.lambdaquery.jpacriteria.inmemorysupport;

import static org.hamcrest.Matchers.*;
import org.junit.*;
import static org.junit.Assert.*;
import testsupport.parameterized.ParameterRule;

/**
 *
 * @author ondro
 */
public class InMemoryFunctionsTest {

    @Rule
    public ParameterRule<LikeParam> likeParams = new ParameterRule<>(
            likeParam("Nitra", "Nit%"),
            likeParam("Nitra", "Nit%a"),
            likeParam("Nitra", "Na%", false),
            likeParam("Nitra Praha", "%Nitra%"),
            likeParam("Nitra Praha", "%a P%"),
            likeParam("Nitra Praha", "_itra%"),
            likeParam("Nitra Praha", "N%ra_P%a"),
            likeParam("Nitra Praha", "%ra_Pra%")
    );
     
    @Test
    public void canMatchWithLike() {
        LikeParam params = likeParams.getParameter();
        boolean matches = InMemoryFactory.getFunctions().like(params.value, params.expr);
        assertThat(matches, is(equalTo(params.expectedResult)));
    }

    private static class LikeParam {
        public String value;
        public String expr;
        public boolean expectedResult;
    }
    
    private LikeParam likeParam(String value, String expr) {
        return likeParam(value, expr, true);
    }
    private LikeParam likeParam(String value, String expr, boolean expected) {
        LikeParam likeParam = new LikeParam();
        likeParam.value = value;
        likeParam.expr = expr;
        likeParam.expectedResult = expected;
        return likeParam;
    }
}
