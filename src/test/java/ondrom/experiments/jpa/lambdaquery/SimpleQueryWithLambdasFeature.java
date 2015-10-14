package ondrom.experiments.jpa.lambdaquery;

import eu.inginea.lambdacriteria.Alias;
import eu.inginea.lambdacriteria.LambdaQuery;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.ParameterExpression;
import javax.persistence.criteria.Root;
import ondrom.experiments.jpa.BaseJPATest;
import ondrom.experiments.jpa.Person;
import static ondrom.experiments.jpa.entitybuilders.PersonBuilder.aPerson;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;
import org.junit.Before;

public class SimpleQueryWithLambdasFeature extends BaseJPATest {

    @Before
    public void preparePersons() {
        final List<String> PERSON_NAMES = Arrays.asList("Ondro", "Janka");

        given(() -> {
            reopenEM();
            beginTx();
            // clear DB data
            getEM().createQuery("DELETE FROM Person")
                    .executeUpdate();
            // fill DB data
            Iterator<String> itNames = PERSON_NAMES.iterator();
            aPerson()
                    .withName(itNames.next())
                    .existsIn(getEM());
            aPerson()
                    .withName(itNames.next())
                    .existsIn(getEM());
            commitTx();
            reopenEM();
        });

    }

    @Test
    public void canQueryPersonByNameUsingJPQL() {
        List<Person> persons = getEM().createQuery("select p from Person p where p.name = :name", Person.class)
                .setParameter("name", "Ondro")
                .getResultList();
        assertThat("List of persons matching criteria", persons, is(not(empty())));
        assertThat("List of persons matching criteria", persons, is(iterableWithSize( 1 )));
    }

    @Test
    public void canQueryPersonByNameUsingCriteria() {
        CriteriaBuilder cb = getEM().getCriteriaBuilder();
        CriteriaQuery<Person> q = cb.createQuery(Person.class);
        Root<Person> p = q.from(Person.class);
        q.select(p).where(cb.equal(p.get("name"), cb.parameter(String.class, "name")));
        List<Person> persons = getEM().createQuery(q)
                .setParameter("name", "Ondro")
                .getResultList();
        assertThat("List of persons matching criteria", persons, is(not(empty())));
        assertThat("List of persons matching criteria", persons, is(iterableWithSize( 1 )));
    }

    @Test
    public void canQueryPersonByNameUsingLambdas() {
        Alias<Person> p = new Alias<>();
        List<Person> persons = new LambdaQuery<Person>(getEM())
                .select(p)
                .from(p)
                .where(() -> p.val.getName() == "Ondro")
                .getResultList();
        assertThat("List of persons matching criteria", persons, is(not(empty())));
        assertThat("List of persons matching criteria", persons, is(iterableWithSize( 1 )));
    }
    
}
