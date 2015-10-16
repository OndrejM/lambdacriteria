package ondrom.experiments.jpa.lambdaquery;

import eu.inginea.lambdacriteria.alternative2.LambdaQuery2;
import eu.inginea.lambdacriteria.Alias;
import java.util.List;
import ondrom.experiments.jpa.Person;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;
import org.junit.Before;

public class FeatureQueryWithLambdasAlternative2 extends QueryWithLambdasBase {

    List<Person> allPersons = null;
    List<Person> persons = null;
    List<String> colors = null;

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
    public void canQueryPersonByName() {
        when(() -> {
        Alias<Person> p = new Alias<>(Person.class);
            persons = new LambdaQuery2<Person>(getEM())
                .select(p)
                .from(p)
                .where(p, (pe) -> "Ondro".equals(pe.getName()))
                .getResultList();
        });
        then(() -> {
            isValidPersonByName(persons);
        });
    }

    @Test
    @Override
    public void canQueryPersonByNameGroupByHairColorUsingJPQL() {
        super.canQueryPersonByNameGroupByHairColorUsingJPQL();
    }

    @Test
    @Override
    public void canQueryPersonByNameGroupByHairColorUsingCriteria() {
        super.canQueryPersonByNameGroupByHairColorUsingCriteria();
    }
    
    @Test
    public void canQueryPersonByNameGroupByHairColor() {
        when(() -> {
        Alias<String> p = new Alias<>(Person.class);
            colors = new LambdaQuery2<String>(getEM())
                .select(p, pe -> pe.getHairColor())
                .from(p)
                .where(p, pe -> "Ondro".equals(pe.getName()))
                .groupBy(p, pe -> pe.getHairColor())
                .getResultList();
        });
        then(() -> {
            isValidPersonByNameGroupByHairColor(colors);
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
    
    
    
    
    
}
