package ondrom.experiments.jpa.lambdaquery;

import eu.inginea.lambdacriteria.alternative1.LambdaInMemoryQuery;
import eu.inginea.lambdacriteria.Alias;
import eu.inginea.lambdacriteria.JpaFunctions;
import eu.inginea.lambdacriteria.alternative1.Condition;
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
    public void canQueryPersonByNameEquals() {
        when(() -> {
            Alias<Person> p = new Alias<>(Person.class);
            persons = queryPersons(p, () -> "Ondro".equals(p.val.getName()));
        });
        then(() -> {
            isValidPersonByName(persons, 1);
        });
    }

    @Test
    public void canQueryPersonByNameEqual() {
        when(() -> {
            Alias<Person> p = new Alias<>(Person.class);
            persons = queryPersons(p, () -> p.val.getName() == "Ondro");
        });
        then(() -> {
            isValidPersonByName(persons, 1);
        });
    }

    @Test
    public void canQueryPersonByNameEqualsFunc() {
        when(() -> {
            Alias<Person> p = new Alias<>(Person.class);
            persons = queryPersons(p, () -> JpaFunctions.equals("Ondro", p.val.getName()));
        });
        then(() -> {
            isValidPersonByName(persons, 1);
        });
    }

    private List<Person> queryPersons(Alias<Person> p, Condition whereCondition) {
        List<Person> persons = new LambdaQuery<Person>(getEM())
                .select(p)
                .from(p)
                .where(whereCondition)
                .getResultList();
        return persons;
    }

    @Test
    public void canQueryPersonByHairColorUsingLambdas() {
        Alias<Person> p = new Alias<>(Person.class);
        List<Person> persons = new LambdaQuery<Person>(getEM())
                .select(p)
                .from(p)
                .where(() -> p.val.getHairColor() == "blond")
                .getResultList();
        isValidPersonByBlondHair(persons);
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
            isValidPersonByName(persons, 1);
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
                    .groupBy(() -> le.val.getPlace())
                    .getResultList();
        });
        then(() -> {
            isValidPersonByNameGroupByHairColor(colors);
        });
    }

    private void isValidPersonByBlondHair(List<Person> persons) {
        assertThat("List of persons", persons, allOf(
                is(iterableWithSize(2)),
                everyItem(is(instanceOf(Person.class)))
        ));
    }

}
