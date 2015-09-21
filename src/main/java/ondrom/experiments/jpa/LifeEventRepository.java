package ondrom.experiments.jpa;

import java.util.List;
import org.apache.deltaspike.data.api.EntityRepository;
import org.apache.deltaspike.data.api.Query;
import org.apache.deltaspike.data.api.Repository;

@Repository(forEntity = Person.class)
public interface LifeEventRepository extends EntityRepository<LifeEvent, Long> {
    @Query(named = Person.FindAllLeByName.QName)
    List<LifeEvent> findByPlaceAndHavingPerson(String place);
}
