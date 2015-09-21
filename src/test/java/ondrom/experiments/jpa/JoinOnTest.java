package ondrom.experiments.jpa;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import org.junit.Test;
import static org.junit.Assert.*;
import static ondrom.experiments.jpa.entitybuilders.PersonBuilder.*;
import static ondrom.experiments.jpa.entitybuilders.LifeEventBuilder.*;
import static org.hamcrest.Matchers.*;
import org.jglue.cdiunit.CdiRunner;
import org.jglue.cdiunit.deltaspike.SupportDeltaspikeData;
import org.junit.runner.RunWith;

@RunWith(CdiRunner.class)
@SupportDeltaspikeData
public class JoinOnTest extends BaseJPATest {

    @Inject
    private PersonRepository personRepo;
    
    @Test
    public void isQueryByEmbeddedObjectInMapWorkig() {
        final List<String> PLACES = Arrays.asList("Praha", "Nitra");
        final String PERSON_NAME = "Ondro";
        given(() -> {
            // fill DB data
            beginTx();
            Iterator<String> itPlaces = PLACES.iterator();
            aPerson()
                .withName(PERSON_NAME)
                .withLifeEvent(aLifeEvent()
                    .withPlace(itPlaces.next()))
                .withLifeEvent(aLifeEvent()
                    .withPlace(itPlaces.next()))
                .existsIn(getEM());
            commitTx();
            reopenEM();
        });
        then(() -> {
            List<Person> resultList = personRepo.findByNameFilterLifeEventsByPlace(PERSON_NAME, PLACES.get(0));
            assertThat(resultList, is(not(empty())));
            assertThat(resultList.get(0).getLifeEventList(), is(iterableWithSize( 1 )));
        });
        
    }
    
    @Produces
    @ApplicationScoped
    public EntityManager createEntityManager() {
        return getEM();
    }

}
