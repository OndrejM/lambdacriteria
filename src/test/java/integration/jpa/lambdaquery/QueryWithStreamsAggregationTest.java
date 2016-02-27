package integration.jpa.lambdaquery;

import eu.inginea.lambdaquery.jpacriteria.JPAStreamQuery;
import eu.inginea.lambdaquery.QueryStream;
import eu.inginea.lambdaquery.jpacriteria.*;
import eu.inginea.lambdaquery.memory.InMemoryStreamQuery;
import java.util.List;
import java.util.stream.Collectors;
import integration.jpa.model.Person;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;
import org.junit.*;
import testsupport.parameterized.*;

/**
 * <p>
 * Tests for queries with some form of aggregation
 * <p>
 * Stream-like API should be translatable to JPA Criteria (or SQL) and also
 * should be executable on in-memory data
 */
public class QueryWithStreamsAggregationTest extends QueryWithLambdasBase {

    private static final String LIST_OF_PERSONS_MATCHING_CRITERIA = "List of persons matching criteria";
    private List<Person> allPersons = null;
    private List<Person> persons = null;
    private List<String> colors = null;
    
    @Rule
    public ParameterRule<QueryStream<Person>> personStreams = new ParameterRule<>(ProducingIterable.from(
            this::aPersonJPAStream,
            this::aPersonInMemoryStream)
    );

    @Before
    @Override
    public void preparePersons() {
        super.preparePersons();
    }

    @Test
    @Override
    public void canQueryPersonByNameGroupByLifeEventPlaceUsingCriteria() {
        super.canQueryPersonByNameGroupByLifeEventPlaceUsingCriteria();
    }
    
    @Test
    @Override
    public void canQueryPersonByNameGroupByLifeEventPlaceUsingJPQL() {
        super.canQueryPersonByNameGroupByLifeEventPlaceUsingJPQL();
    }

    @Test
    @Override
    public void canQueryPersonByNameGroupByHairColorUsingCriteria() {
        super.canQueryPersonByNameGroupByHairColorUsingCriteria();
    }

    @Test
    @Override
    public void canQueryPersonByNameGroupByHairColorUsingJPQL() {
        super.canQueryPersonByNameGroupByHairColorUsingJPQL();
    }

    @Test
    public void canQueryPersonByNameGroupByHairColorUsingStream() {
        QueryStream<Person> stream = personStreams.getParameter();
        when(() -> {
            colors = stream
                    .filter(p -> JPAQueryExpressions.is(p.getName()).like("J%"))
                    .grouped(p -> p.getHairColor())
                    .select(p -> p.getHairColor())
                    .mapToSelection()
                    .map(row -> (String)(row.get(0)))
                    .collect(Collectors.toList());
        });
        then(() -> {
            assertThat("List of colors matching criteria", colors, is(not(empty())));
            assertThat("List of colors matching criteria", colors, is(iterableWithSize(1)));
        });
    }

    private QueryStream<Person> aPersonInMemoryStream() {
        return new InMemoryStreamQuery().from(getAllPersons().stream());
    }

    private QueryStream<Person> aPersonJPAStream() {
        return new JPAStreamQuery(getEM()).from(Person.class);
    }

}
