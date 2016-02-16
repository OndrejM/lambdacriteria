package eu.inginea.lambdacriteria.streamQuery.jpacriteria;

import eu.inginea.lambdacriteria.streamQuery.*;
import java.util.*;
import ondrom.experiments.jpa.*;
import static org.hamcrest.Matchers.*;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author ondro
 */
public class JPACriteriaTransformerTest implements BDDTestBase {
    private JPACriteriaFilterVisitor transformer;
    private List<Term> terms;

    public JPACriteriaTransformerTest() {
    }

    @Test
    public void can_build_criteria_for_simple_term_expression() {
        given(() -> {
            transformer = new JPACriteriaFilterVisitor(Person.class);
            terms = Arrays.asList(new Constant("Ondro:String"),
                    Operation.EQUAL,
                    new Parameter(0),
                    new Path("Property name"));
        });
        when(() -> {
            terms.stream().forEach(transformer::visit);
        });
        then(() -> {
            assertThat("criteria query", transformer.getCriteriaQuery(), is(not(nullValue())));
        });
    }
}
