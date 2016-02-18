package integration.jpa.lambdaquery;

import eu.inginea.lambdacriteria.streamQuery.*;
import java.util.List;
import java.util.stream.Collectors;
import integration.jpa.model.Person;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;
import org.junit.Before;

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
            assertThat("List of persons matching criteria", persons, is(iterableWithSize( 1 )));
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
            assertThat("List of persons matching criteria", persons, is(iterableWithSize( 1 )));
        });
    }
    
}
