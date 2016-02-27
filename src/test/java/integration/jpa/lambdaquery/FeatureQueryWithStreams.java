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
 * Tests stream-like API for lambda JPA queries.
 * <p>
 * Stream-like API should be translatable to JPA Criteria (or SQL) and also
 * should be execcutable on in-memory data
 * <p>
 * Tests to do:
 * <dl>
 * <dt>GROUP BY</dt>
 * <dd>new method like sorted? distinct() is enough? Possibly map to array of
 * objects?</dd>
 * <dt>DISTINCT</dt>
 * <dd>distinct() - as select() but with distinct values</dd>
 * <dt>JOIN collection</dt>
 * <dd>OK - util param and nested query stream</dd>
 * <dt>ORDER BY collection</dt>
 * <dd>sorted?</dd>
 * <dt>aggregate functions</dt>
 * <dd>reduce?</dd>
 * <dt>Operators (BETWEEN, LIKE, ...)</dt>
 * <dd></dd>
 * <dt>SELECT</dt>
 * <dd>collect, forEach - terminal operations, peek (with function instead of
 * consumer)?</dd>
 * <dt>Parameters</dt>
 * <dd>detect variables defined outside of the lambda expression?</dd>
 * <dt>LIMIT</dt>
 * <dd>limit</dd>
 * <dt>dynamic OR</dt>
 * <dd>using non-parsed lambda executed before parsing?</dd>
 * <dt>dynamic AND</dt>
 * <dd>simply multiple filters</dd>
 * <dt>dynamic NOT</dt>
 * <dd>?</dd>
 * </dl>
 */
public class FeatureQueryWithStreams extends QueryWithLambdasBase {

    List<Person> allPersons = null;
    List<Person> persons = null;

    @Before
    public void preparePersons() {
        super.preparePersons();
    }

    @Test
    public void canQueryPersonByNameUsingJPQL() {
        super.canQueryPersonByNameUsingJPQL();
    }

    @Test
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

    /**
     * Example of API to handle all possible JPA query options, which make sense
     * in single query
     */
    @Test
    public void canParseVeryComplexQuery() {
        when(() -> {
            List<List<?>> result = aPersonJPAStream()
                    .filter((p) -> "Nitra".equals(p.getAddress().getCity()))
                    .select() // add person entity to select clause
                    .select(p -> p.getName()) // add person name to select clause
                    .distinct(p -> p.getAddress()) // add distinct p.address to select clause 
                    // join with filter and select on joined entity. Probably will need custom conversion 
                    // to QueryStream insteda of stream, as lambdas in filter must be serializable
                    .join((p, util) -> {
                        return util.toStream(p.getLifeEventList())
                                .filter(le -> le.getPlace().equals(p.getAddress().getCity()))
                                .select();
                    })
                    // choose selected values and collect - only necessary if we want to have more than single entity in result
                    .mapToSelection()
                    // alternatively map() with expression as in select to change to different type in result
                    // without map operation, root entity is in result
                    .collect(Collectors.toList()); // collect on lists -> result is list of lists
        });
    }

    /**
     * Example of API to handle all possible JPA query options, which make sense
     * in single query with aggregation
     */
    @Test
    public void canParseVeryComplexQueryWithAggregation() {
        when(() -> {
            List<List<?>> result = aPersonJPAStream()
                    .filter((p) -> "Nitra".equals(p.getAddress().getCity()))
                    .select() // add person entity to select clause
                    .distinct(p -> p.getAddress()) // group by address -> select persons and their distinct addresses
                    // join with filter and select on joined entity. Probably will need custom conversion 
                    // to QueryStream insteda of stream, as lambdas in filter must be serializable
                    .join((p, util) -> {
                        return util.toStream(p.getLifeEventList())
                                .filter(le -> le.getPlace().equals(p.getAddress().getCity()))
                                .select();
                    })
                    // choose selected values and collect - only necessary if we want to have more than single entity in result
                    .mapToSelection()
                    // alternatively map() with expression as in select to change to different type in result
                    // without map operation, root entity is in result
                    .collect(Collectors.toList()); // collect on lists -> result is list of lists
        });
    }

}
