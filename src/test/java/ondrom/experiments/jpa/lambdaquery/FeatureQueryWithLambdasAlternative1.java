package ondrom.experiments.jpa.lambdaquery;

import eu.inginea.lambdacriteria.alternative1.LambdaInMemoryQuery;
import eu.inginea.lambdacriteria.Alias;
import eu.inginea.lambdacriteria.alternative1.LambdaQuery;
import java.util.List;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import ondrom.experiments.jpa.Person;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;
import org.junit.Before;

public class FeatureQueryWithLambdasAlternative1 extends QueryWithLambdasBase {

    @Before
    public void preparePersons() {
        super.preparePersons();
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
        Alias<Person> p = new Alias<>(Person.class);
        List<Person> persons = new LambdaQuery<Person>(getEM())
                .select(p)
                .from(p)
                .where(() -> p.val.getName() == "Ondro")
                .getResultList();
        assertThat("List of persons matching criteria", persons, is(not(empty())));
        assertThat("List of persons matching criteria", persons, is(iterableWithSize( 1 )));
    }
    
    List<Person> allPersons = null;
    List<Person> persons = null;
    
    @Test
    public void canQueryPersonByNameUsingLambdasInMemory() {
        given(() -> {
            allPersons = getEM().createQuery("select p from Person p", Person.class)
                .getResultList();
        });
        
        when(() -> {
        Alias<Person> p = new Alias<>(Person.class);
            persons = new LambdaInMemoryQuery<Person>()
                .select(p)
                .from(p)
                .withData(p, allPersons.stream())
                .where(() -> "Ondro".equals(p.val.getName()))
                .getResultList();
        });
        then(() -> {
            assertThat("List of persons matching criteria", persons, is(not(empty())));
            assertThat("List of persons matching criteria", persons, is(iterableWithSize( 1 )));
        });
    }
    
}
