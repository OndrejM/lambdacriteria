package integration.jpa.lambdaquery;

import eu.inginea.lambdaquery.jpacriteria.JPAStreamQuery;
import eu.inginea.lambdaquery.QueryStream;
import eu.inginea.lambdaquery.jpacriteria.*;
import static eu.inginea.lambdaquery.jpacriteria.JPAQueryExpressions.like;
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
 * Tests for queries with filter
 * <p>
 * Stream-like API should be translatable to JPA Criteria (or SQL) and also
 * should be executable on in-memory data
 * <p>
 * TODO:
 * <ul>
 * <li>greater, less than operators</li>
 * <li>special SQL functions as LIKE etc.</li>
 * <li>boolean opearations (AND, OR, NOT)</li>
 * </ul>
 */
public class QueryWithStreamsFilterTest extends QueryWithLambdasBase {

    List<Person> allPersons = null;
    List<Person> persons = null;

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
    public void canQueryPersonByNameUsingJPQL() {
        super.canQueryPersonByNameUsingJPQL();
    }

    @Test
    @Override
    public void canQueryPersonByNameUsingCriteria() {
        super.canQueryPersonByNameUsingCriteria();
    }

    @Test
    public void canQueryPersonByNameUsingStream() {
        QueryStream<Person> stream = personStreams.getParameter();
        when(() -> {
            persons = stream
                    .filter(p -> "Ondro".equals(p.getName()))
                    .collect(Collectors.toList());
        });
        then(() -> {
            assertThat("List of persons matching criteria", persons, is(not(empty())));
            assertThat("List of persons matching criteria", persons, is(iterableWithSize(1)));
        });
    }

    @Test
    @Override
    public void canQueryPersonByCityUsingJPQL() {
        super.canQueryPersonByCityUsingJPQL();
    }

    @Test
    public void canQueryPersonByCityUsingStream() {
        QueryStream<Person> stream = personStreams.getParameter();
        when(() -> {
            persons = stream
                    .filter(p -> "Nitra".equals(p.getAddress().getCity()))
                    .collect(Collectors.toList());
        });
        then(() -> {
            assertThat("List of persons matching criteria", persons, is(not(empty())));
            assertThat("List of persons matching criteria", persons, is(iterableWithSize(1)));
        });
    }

    @Test
    @Override
    public void canQueryPersonWithLikeUsingCriteria() {
        super.canQueryPersonWithLikeUsingCriteria();
    }
    
    @Test
    public void canQueryPersonWithLikeUsingStream() {
        QueryStream<Person> stream = personStreams.getParameter();
        when(() -> {
            persons = stream
                    .filter(p -> like(p.getAddress().getCity(), "%a%"))
                    .collect(Collectors.toList());
        });
        then(() -> {
            assertThat("List of persons matching criteria", persons, is(not(empty())));
            assertThat("List of persons matching criteria", persons, is(iterableWithSize(2)));
        });
    }

    @Test
    public void canQueryPersonWithIsLikeUsingStream() {
        QueryStream<Person> stream = personStreams.getParameter();
        when(() -> {
            persons = stream
                    .filter(p -> JPAQueryExpressions.is(p.getAddress().getCity()).like("%a%"))
                    .collect(Collectors.toList());
        });
        then(() -> {
            assertThat("List of persons matching criteria", persons, is(not(empty())));
            assertThat("List of persons matching criteria", persons, is(iterableWithSize(2)));
        });
    }

    private QueryStream<Person> aPersonInMemoryStream() {
        return new InMemoryStreamQuery().from(getAllPersons().stream());
    }

    private QueryStream<Person> aPersonJPAStream() {
        return new JPAStreamQuery(getEM()).from(Person.class);
    }

}
