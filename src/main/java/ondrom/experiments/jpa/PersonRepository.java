package ondrom.experiments.jpa;

import java.util.List;
import org.apache.deltaspike.data.api.EntityRepository;
import org.apache.deltaspike.data.api.Query;
import org.apache.deltaspike.data.api.Repository;

@Repository(forEntity = Person.class)
public interface PersonRepository extends EntityRepository<Person, Long>{
    @Query("select p from Person p left join fetch p.lifeEventList le on le.place = ?2 where p.name = ?1")
    List<Person> findByNameFilterLifeEventsByPlace(String name, String lePlace);
}
