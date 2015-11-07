package ondrom.experiments.jpa.lambdaquery;

import eu.inginea.lambdacriteria.alternative2.LambdaQuery2;
import eu.inginea.lambdacriteria.Alias;
import eu.inginea.lambdacriteria.alternative3.*;
import java.util.*;
import ondrom.experiments.jpa.*;
import org.junit.Test;
import org.junit.Before;

public class FeatureQueryWithLambdasAlternative3 extends QueryWithLambdasBase {

    List<Person> allPersons = null;
    List<Person> persons = null;
    List<String> colors = null;

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
    public void canQueryPersonByName() {
        when(() -> {
            persons = new LambdaQuery3<Person>(getEM())
                    .select(Person.class, p -> {
                        if ("Ondro".equals(p.getName())) {
                            return p;
                        }
                        return null;
                    })
                    .getResultList();
            // alternative using API for multiple roots
            persons = new LambdaQuery3<Person>(getEM())
                    .select(o -> {
                        if (o instanceof Person) {
                            Person p = (Person) o;
                            if ("Ondro".equals(p.getName())) {
                                return p;
                            }
                        }
                        return null;
                    })
                    .getResultList();
        });
        then(() -> {
            isValidPersonByName(persons, 1);
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
    
    public static <RESULT> Rows<RESULT> rows(RESULT x) {
        return new Rows<>();
    }

    @Test
    public void canQueryPersonByNameGroupByHairColor() {
        when(() -> {
            colors = new LambdaQuery3<String>(getEM())
                    .select(Person.class, p -> {
                        if ("Ondro".equals(p.getName())) {
                            return rows(p.getHairColor())
                                    .groupBy(p.getHairColor())
                                    .result();
                        }
                        return null;
                    })
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

    public <RESULT> RESULT join(Collection<RESULT> joinExpr) {
        return null;
    }
    
    @Test
    public void canQueryPersonByNameGroupByLifeEventPlace() {
        when(() -> {
            colors = new LambdaQuery3<String>(getEM())
                    .select(Person.class, p -> {
                        LifeEvent le = join(p.getLifeEventList());
                        if ("Ondro".equals(p.getName())) {
                            return rows(le.getPlace())
                                    .groupBy(le.getPlace())
                                    .result();
                        }
                        return null;
                    })
                    .getResultList();
        });
        then(() -> {
            isValidPersonByNameGroupByHairColor(colors);
        });
    }

    @Test
    public void canQueryPersonByNameGroupByLifeEventPlace2() {
        when(() -> {
            Alias<LifeEvent> le = new Alias<LifeEvent>();
            colors = new LambdaQuery2<String>(getEM())
                    .select(le, (LifeEvent lee) -> lee.getPlace())
                    // alternatively
                    .select(le, LifeEvent::getPlace)
                    .from(Person.class)
                    .where(pe -> "Ondro".equals(pe.getName()))
                    .join(le, Person pe -> pe.getLifeEventList()
            )
                .groupBy(le, (LifeEvent lee) -> lee.getPlace())
                    .getResultList();
        });
        then(() -> {
            isValidPersonByNameGroupByHairColor(colors);
        });
    }

}
