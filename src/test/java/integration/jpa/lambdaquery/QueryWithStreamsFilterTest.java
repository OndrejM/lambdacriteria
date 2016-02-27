package integration.jpa.lambdaquery;

import eu.inginea.lambdaquery.jpacriteria.JPAStreamQuery;
import eu.inginea.lambdaquery.QueryStream;
import eu.inginea.lambdaquery.memory.InMemoryStreamQuery;
import java.util.List;
import java.util.stream.Collectors;
import integration.jpa.model.Person;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;
import org.junit.Before;

/**
 * <p>
 * Tests for queries with filter 
 * <p>
 * Stream-like API should be translatable to JPA Criteria (or SQL) and also
 * should be executable on in-memory data
 */
public class QueryWithStreamsFilterTest extends QueryWithLambdasBase {

    List<Person> allPersons = null;
    List<Person> persons = null;

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
    public void canQueryPersonByNameUsingJPAStream() {
        canQueryPersonByNameUsingSuppliedStream(aPersonJPAStream());
    }

    @Test
    public void canQueryPersonByNameUsingStreamInMemory() {
        canQueryPersonByNameUsingSuppliedStream(aPersonInMemoryStream());
    }

    private QueryStream<Person> aPersonInMemoryStream() {
        return new InMemoryStreamQuery().from(getAllPersons().stream());
    }

    private void canQueryPersonByNameUsingSuppliedStream(QueryStream<Person> stream) {
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
    public void canQueryPersonByCityUsingJPAStream() {
        canQueryPersonByCityUsingSuppliedStream(aPersonJPAStream());
    }

    private QueryStream<Person> aPersonJPAStream() {
        return new JPAStreamQuery(getEM()).from(Person.class);
    }

    @Test
    public void canQueryPersonByCityUsingStreamInMemory() {
        canQueryPersonByCityUsingSuppliedStream(aPersonInMemoryStream());
    }

    private void canQueryPersonByCityUsingSuppliedStream(QueryStream<Person> stream) {
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

}
