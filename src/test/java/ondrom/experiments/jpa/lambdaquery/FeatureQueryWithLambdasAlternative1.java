package ondrom.experiments.jpa.lambdaquery;

import eu.inginea.lambdacriteria.alternative1.LambdaInMemoryQuery;
import eu.inginea.lambdacriteria.Alias;
import eu.inginea.lambdacriteria.alternative1.LambdaQuery;
import eu.inginea.lambdacriteria.alternative2.LambdaQuery2;
import java.util.List;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import ondrom.experiments.jpa.LifeEvent;
import ondrom.experiments.jpa.Person;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;
import org.junit.Before;

public class FeatureQueryWithLambdasAlternative1 extends QueryWithLambdasBase {

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
    public void canQueryPersonByNameUsingLambdas() {
        Alias<Person> p = new Alias<>(Person.class);
        List<Person> persons = new LambdaQuery<Person>(getEM())
                .select(p)
                .from(p)
                .where(() -> p.val.getName() == "Ondro")
                .getResultList();
        isValidPersonByName(persons);
    }
    
    List<Person> allPersons = null;
    List<Person> persons = null;
    List<String> colors = null;
    
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
    
    @Test
    @Override
    public void canQueryPersonByNameGroupByLifeEventPlaceUsingJPQL() {
        super.canQueryPersonByNameGroupByLifeEventPlaceUsingJPQL();
    }

    @Test
    @Override
    public void canQueryPersonByNameGroupByLifeEventPlaceUsingCriteria() {
        super.canQueryPersonByNameGroupByLifeEventPlaceUsingCriteria();
    }
    
    @Test
    public void canQueryPersonByNameGroupByLifeEventPlace() {
        when(() -> {
            Alias<Person> p = new Alias<>(Person.class);
            Alias<LifeEvent> le = new Alias<>(LifeEvent.class);
            colors = new LambdaQuery<String>(getEM())
                .select(() -> le.val.getPlace())
                .from(p)
                .where(() -> "Ondro".equals(p.val.getName()))
                .groupBy(le, LifeEvent::getPlace)
                .getResultList();
        });
        then(() -> {
            isValidPersonByNameGroupByHairColor(colors);
        });
    }

}
