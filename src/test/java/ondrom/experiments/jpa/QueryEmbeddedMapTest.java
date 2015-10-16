package ondrom.experiments.jpa;

import java.util.List;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
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
public class QueryEmbeddedMapTest extends JPATestBase {

    @Inject
    private LifeEventRepository lifeEventRepo;
    
    @Test
    public void isQueryByEmbeddedObjectInMapWorkig() {
        final String PLACE = "Praha";
        final String KEY = "BIRTH";
        given(() -> {
            // fill DB data
            beginTx();
            aPerson()
                .withLifeEvent(KEY, aLifeEvent()
                    .withPlace(PLACE))
                .existsIn(getEM());
            commitTx();
        });
        then(() -> {
            Person.FindAllLeByName QAttr = null;
            List<LifeEvent> resultList = lifeEventRepo.findByPlaceAndHavingPerson(PLACE);
            assertThat(resultList, not(empty()));
        });
        
    }
    
    @Produces
    @ApplicationScoped
    public EntityManager createEntityManager() {
        return getEM();
    }

}
