package testbase;

import java.text.DateFormat;
import java.util.Locale;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public abstract class JPATestBase implements BDDTestBase {

    private static final String PU_NAME = "test";
    
    private EntityManager em = null;
    
    protected static DateFormat dateTimeFormatter() {
        return DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT, new Locale("SK"));
    }

    private EntityManagerFactory emf = Persistence.createEntityManagerFactory(PU_NAME);
    
    protected synchronized EntityManager getEM() {
        if (em == null) {
            emf.getCache().evictAll();
            em = emf.createEntityManager();
        }
        return em;
    }
    
    protected synchronized void reopenEM() {
        if (em != null) {
            em.close();
        }
        em = null;
    }

    protected void beginTx() {
        getEM().getTransaction().begin();
    }

    protected void commitTx() {
        getEM().getTransaction().commit();
    }

}
