package ondrom.experiments.jpa;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author ondro
 */
public class JPASimpleTest {
    
    public JPASimpleTest() {
    }
    
     @Test
     public void hello() throws ClassNotFoundException, InstantiationException, IllegalAccessException {
        Class.forName("org.h2.Driver").newInstance();
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("test");
        EntityManager em = emf.createEntityManager();
        em.getMetamodel();
     }
}
