package integration.jpa.lambdaquery;

import eu.inginea.lambdacriteria.streamQuery.*;
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
 * <dd>new method like sorted? distinct() is enough? Possibly map to array of objects?</dd>
 * <dt>DISTINCT</dt>
 * <dd>distinct()</dd>
 * <dt>JOIN collection</dt>
 * <dd>map? flatMap?</dd>
 * <dt>ORDER BY collection</dt>
 * <dd>sorted?</dd>
 * <dt>aggregate functions</dt>
 * <dd>reduce?</dd>
 * <dt>Operators (BETWEEN, LIKE, ...)</dt>
 * <dd></dd>
 * <dt>SELECT</dt>
 * <dd>collect, forEach - terminal operations, peek (with function instead of consumer)?</dd>
 * <dt>Parameters</dt>
 * <dd>detect variables defined outside of the lambda expression?</dd>
 * <dt>LIMIT</dt>
 * <dd>limit</dd>
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
    public void canQueryPersonByNameUsingStream() {
        when(() -> {
            persons = new StreamQuery<Person>(getEM())
                    .from(Person.class)
                    .filter((p) -> "Ondro".equals(p.getName()))
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
        when(() -> {
            persons = new StreamQuery<Person>(getEM())
                    .from(Person.class)
                    .filter((p) -> "Nitra".equals(p.getAddress().getCity()))
                    .collect(Collectors.toList());
        });
        then(() -> {
            assertThat("List of persons matching criteria", persons, is(not(empty())));
            assertThat("List of persons matching criteria", persons, is(iterableWithSize(1)));
        });
    }
    
    /**
     * Example of API to handle all possible JPA query options, which make sense in single query
     */
    @Test
    public void canParseVeryComplexQuery() {
        when(() -> {
            List<?> result = new StreamQuery<Person>(getEM())
                    .from(Person.class)
                    .filter((p) -> "Nitra".equals(p.getAddress().getCity()))
                    .select() // add person entity to select clause
                    .select(p -> p.getAddress()) // add p.address to select clause 
                    // join with filter and select on joined entity. Probably will need custom conversion 
                    // to QueryStream insteda of stream, as lambdas in filter must be serializable
                    .join((p, util) -> {
                            return util.toStream(p.getLifeEventList())
                            .filter(le -> le.getPlace() == p.getAddress().getCity())
                            .select();
                    })
                    .collectSelection(Collectors.toList());
        });
    }

}
