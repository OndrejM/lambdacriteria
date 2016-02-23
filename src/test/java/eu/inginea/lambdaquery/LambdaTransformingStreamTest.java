package eu.inginea.lambdaquery;

import eu.inginea.lambdaquery.base.LambdaTransformingStream;
import eu.inginea.lambdaquery.base.QueryMapping;
import eu.inginea.lambdaquery.base.TokenHandler;
import eu.inginea.lambdaquery.base.StreamOperation;
import eu.inginea.lambdaquery.base.LambdaVisitor;
import eu.inginea.lambdaquery.base.QueryTransformer;
import eu.inginea.lambdaquery.ruleengine.*;
import eu.inginea.lambdaquery.jpacriteria.LoggingTransformer;
import java.util.*;
import java.util.stream.*;
import integration.jpa.model.Person;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;
import org.junit.Test;
import testbase.BDDTestBase;

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
            assertThat("terms parsed", transformer.getTerms(), contains(
                    new Constant("Ondro"),
                    BinaryOperation.EQUAL,
                    new Parameter(0),
                    new Path("name")
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
            assertThat("terms parsed", transformer.getTerms(), contains(
                    new Constant("Nitra"),
                    BinaryOperation.EQUAL,
                    new Parameter(0),
                    new Path("address"),
                    new Path("city")
            ));
        });
    }

    @Test
    public void canQueryPersonAndCollectUsingTransfromingStream() {
        given(() -> {
            transformer = new InspectingTransformer();
            queryStream = from(Person.class);
        });
        when(() -> {
            persons = queryStream.filter((p) -> "Nitra".equals(p.getAddress().getCity()))
                    .collect(Collectors.toList());
        });
        then(() -> {
            assertThat("persons collected", persons, allOf(is(not(nullValue())),
                    hasSize(SIZE)
            ));
        });
    }
    private static final int SIZE = 2;

    private <ROOT_ENTITY> LambdaTransformingStream<ROOT_ENTITY> from(Class<ROOT_ENTITY> entityClass) {
        return new LambdaTransformingStream<>(new TestTransformer<ROOT_ENTITY>());
    }

    private class TestTransformer<ROOT_ENTITY> implements QueryTransformer<ROOT_ENTITY> {

        @Override
        public LambdaVisitor supplyLambdaVisitor(StreamOperation op) {
            return new InspectingLambdaVisitor(op);
        }

        @Override
        public QueryMapping supplyMapping() {
            return transformer;
        }

        @Override
        public TokenHandler supplyQueryVisitor() {
            return transformer;
        }

        @Override
        public Stream<ROOT_ENTITY> getResults() {
            ArrayList<ROOT_ENTITY> list = new ArrayList<>();
            IntStream.range(0, SIZE).forEach(i -> list.add(null));
            return list.stream();
        }

    }

    private static class InspectingLambdaVisitor extends LambdaVisitor {

        public InspectingLambdaVisitor(StreamOperation streamOperation) {
            super(streamOperation);
        }

    }

    private static class InspectingTransformer implements QueryMapping, TokenHandler {

        private final QueryMapping underlyingMapping;
        private final TokenHandler underlyingVisitor;
        private Collection<Term> terms = new ArrayList<>();

        {
            LoggingTransformer t = new LoggingTransformer();
            underlyingMapping = t;
            underlyingVisitor = t;
        }

        @Override
        public Optional<Literal> getTermForExpression(Object expr) {
            return underlyingMapping.getTermForExpression(expr);
        }

        @Override
        public void handleToken(Term literal) {
            underlyingVisitor.handleToken(literal);
            terms.add(literal);
        }

        public Collection<Term> getTerms() {
            return terms;
        }

    }

}
