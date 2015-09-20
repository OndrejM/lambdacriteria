package ondrom.experiments.jpa;

import javax.persistence.TypedQuery;
import org.junit.Test;
import static org.junit.Assert.*;
import static ondrom.experiments.jpa.PersonBuilder.*;
import static ondrom.experiments.jpa.LifeEventBuilder.*;
import static org.hamcrest.Matchers.*;

public class QueryEmbeddedMapTest extends BaseJPATest {

    public QueryEmbeddedMapTest() {
    }

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
            TypedQuery<Person> q = getEM().createNamedQuery(QAttr.QName, Person.class);
            q.setParameter(QAttr.place, PLACE);
            assertThat(q.getResultList(), not(empty()));
        });
        
    }

}
