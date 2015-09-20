package ondrom.experiments.jpa;

import java.text.DateFormat;
import java.util.Locale;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

public abstract class BaseJPATest {

    private EntityManager em = null;
    
    protected static DateFormat dateTimeFormatter() {
        return DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT, new Locale("SK"));
    }

    protected synchronized EntityManager getEM() {
        if (em == null) {
            EntityManagerFactory emf = Persistence.createEntityManagerFactory("test");
            em = emf.createEntityManager();
        }
        return em;
    }

    protected void beginTx() {
        getEM().getTransaction().begin();
    }

    protected void commitTx() {
        getEM().getTransaction().commit();
    }

}
