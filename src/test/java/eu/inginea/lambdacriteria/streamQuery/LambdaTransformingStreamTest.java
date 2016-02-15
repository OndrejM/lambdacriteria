package eu.inginea.lambdacriteria.streamQuery;

import com.trigersoft.jaque.expression.*;
import eu.inginea.lambdacriteria.streamQuery.loggingtransfromer.*;
import java.util.*;
import java.util.stream.Collectors;
import ondrom.experiments.jpa.*;
import org.hamcrest.Matcher;
import static org.hamcrest.collection.IsIterableContainingInOrder.*;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author ondro
 */
public class LambdaTransformingStreamTest implements BDDTestBase {

    private QueryStream<Person> queryStream;
    private List<Person> persons;
    private InspectingTransformer transformer;

    public LambdaTransformingStreamTest() {
    }

    @Test
    public void canQueryPersonByNameUsingTransfromingStream() {
        given(() -> {
            transformer = new InspectingTransformer();
            queryStream = from(Person.class);
        });
        when(() -> {
            queryStream.filter((p) -> "Ondro".equals(p.getName()));
        });
        then(() -> {
            assertThat("terms parsed", transformer.getTerms(), containsExactly(
                    new Constant("Ondro:String"),
                    Operation.EQUAL,
                    new Parameter(0),
                    new Path("Property name")
            ));
        });
    }

    @Test
    public void canQueryPersonByCityUsingTransfromingStream() {
        given(() -> {
            transformer = new InspectingTransformer();
            queryStream = from(Person.class);
        });
        when(() -> {
            queryStream.filter((p) -> "Nitra".equals(p.getAddress().getCity()));
        });
        then(() -> {
            assertThat("terms parsed", transformer.getTerms(), containsExactly(
                    new Constant("Nitra:String"),
                    Operation.EQUAL,
                    new Parameter(0),
                    new Path("Property address"),
                    new Path("Property city")
            ));
        });
    }

    private static <Term> Matcher<Iterable<? extends Term>> containsExactly(Term... values) {
        return contains(values);
    }

    private <ENTITY> LambdaTransformingStream<ENTITY> from(Class<ENTITY> entityClass) {
        return new LambdaTransformingStream<>(
                (StreamOperation op) -> new InspectingLambdaVisitor(op),
                () -> transformer,
                () -> transformer);
    }

    private static class InspectingLambdaVisitor extends LambdaVisitor {

        public InspectingLambdaVisitor(StreamOperation streamOperation) {
            super(streamOperation);
        }

    }

    private static class InspectingTransformer implements QueryMapping, QueryVisitor {

        private final QueryMapping underlyingMapping;
        private final QueryVisitor underlyingVisitor;
        private Collection<Term> terms = new ArrayList<>();

        {
            LambdaQueryLoggingTransformer t = new LambdaQueryLoggingTransformer();
            underlyingMapping = t;
            underlyingVisitor = t;
        }

        @Override
        public Optional<Literal> getTermForExpression(Object expr) {
            return underlyingMapping.getTermForExpression(expr);
        }

        @Override
        public void visit(Term literal) {
            underlyingVisitor.visit(literal);
            terms.add(literal);
        }

        public Collection<Term> getTerms() {
            return terms;
        }

    }

}
