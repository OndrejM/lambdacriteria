package integration.jpa.lambdaquery;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Root;
import testbase.JPATestBase;
import integration.jpa.model.Person;
import static integration.jpa.entitybuilders.AddressBuilder.anAddress;
import static integration.jpa.entitybuilders.LifeEventBuilder.*;
import static integration.jpa.entitybuilders.PersonBuilder.aPerson;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

public class QueryWithLambdasBase extends JPATestBase {

    protected void preparePersons() {
        final List<String> PERSON_NAMES = Arrays.asList("Ondro", "Janka", "Julia");
        final List<String> PERSON_CITIES = Arrays.asList("Nitra", "Praha", "Brno");
        final List<String> PERSON_HAIR_COLORS = Arrays.asList("black", "blond", "blond");
        final List<String> PERSON_EVENT_PLACE = Arrays.asList("Praha", "Madrid", "Madrid");

        given(() -> {
            reopenEM();
            beginTx();
            // clear DB data
            getEM().createQuery("DELETE FROM Person")
                    .executeUpdate();
            // fill DB data
            Iterator<String> itNames = PERSON_NAMES.iterator();
            Iterator<String> itCities = PERSON_CITIES.iterator();
            Iterator<String> itColors = PERSON_HAIR_COLORS.iterator();
            Iterator<String> itPlaces = PERSON_EVENT_PLACE.iterator();
            while (itNames.hasNext() 
                    && itCities.hasNext()
                    && itColors.hasNext()
                    && itPlaces.hasNext()) {
                aPerson()
                    .withName(itNames.next())
                    .withAddress(anAddress().withCity(itCities.next()))
                    .withHairColor(itColors.next())
                    .withLifeEvent(aLifeEvent().withPlace(itPlaces.next()))
                    .existsIn(getEM());
            }
            commitTx();
            reopenEM();
        });

    }

    protected void canQueryPersonByNameUsingJPQL() {
        List<Person> persons = getEM().createQuery("select p from Person p where p.name = :name", Person.class)
                .setParameter("name", "Ondro")
                .getResultList();
        isValidPersonByName(persons, 1);
    }
    
    protected void canQueryPersonByCityUsingJPQL() {
        List<Person> persons = getEM().createQuery("select p from Person p join p.address a where a.city = :city", Person.class)
                .setParameter("city", "Nitra")
                .getResultList();
        isValidPersonByName(persons, 1);
    }
    
    protected void canQueryPersonByNameUsingCriteria() {
        CriteriaBuilder cb = getEM().getCriteriaBuilder();
        CriteriaQuery<Person> q = cb.createQuery(Person.class);
        Root<Person> p = q.from(Person.class);
        q.select(p).where(cb.equal(p.get("name"), cb.parameter(String.class, "name")));
        List<Person> persons = getEM().createQuery(q)
                .setParameter("name", "Ondro")
                .getResultList();
        isValidPersonByName(persons, 1);
    }

    protected void isValidPersonByName(List<Person> persons, int size) {
        assertThat("List of persons", persons, allOf(is(iterableWithSize(size)), 
                everyItem(is(instanceOf(Person.class)))
        ));
    }
    
    protected void canQueryPersonByNameGroupByHairColorUsingJPQL() {
        List<String> colors = getEM().createQuery("select p.hairColor from Person p where p.name like :name group by p.hairColor", 
                String.class)
                .setParameter("name", "J%")
                .getResultList();
        isValidPersonByNameGroupByHairColor(colors);
    }
    
    protected void canQueryPersonByNameGroupByHairColorUsingCriteria() {
        CriteriaBuilder cb = getEM().getCriteriaBuilder();
        CriteriaQuery<String> q = cb.createQuery(String.class);
        Root<Person> p = q.from(Person.class);
        q.select(p.get("hairColor"))
                .where(cb.like(p.get("name"), cb.parameter(String.class, "name")))
                .groupBy(p.get("hairColor"));
        List<String> colors = getEM().createQuery(q)
                .setParameter("name", "J%")
                .getResultList();
        isValidPersonByNameGroupByHairColor(colors);
    }

    protected void isValidPersonByNameGroupByHairColor(List<String> colors) {
        assertThat("List of hairColors", colors, allOf(
                is(iterableWithSize( 1 )), everyItem(is(instanceOf(String.class)))
        ));
    }
    
    protected void canQueryPersonByNameGroupByLifeEventPlaceUsingJPQL() {
        List<String> places = getEM().createQuery("select le.place from Person p join p.lifeEventList le where p.name like :name group by le.place", 
                String.class)
                .setParameter("name", "J%")
                .getResultList();
        isValidPersonByNameGroupByLifeEventPlace(places);
    }

    protected void canQueryPersonByNameGroupByLifeEventPlaceUsingCriteria() {
        CriteriaBuilder cb = getEM().getCriteriaBuilder();
        CriteriaQuery<String> q = cb.createQuery(String.class);
        Root<Person> p = q.from(Person.class);
        Join<Object, Object> le = p.join("lifeEventList");
        q.select(le.get("place"))
                .where(cb.like(p.get("name"), cb.parameter(String.class, "name")))
                .groupBy(le.get("place"));
        List<String> places = getEM().createQuery(q)
                .setParameter("name", "J%")
                .getResultList();
        isValidPersonByNameGroupByHairColor(places);
    }

    protected void isValidPersonByNameGroupByLifeEventPlace(List<String> places) {
        assertThat("List of places matching criteria", places, is(iterableWithSize( 1 )));
    }

}
