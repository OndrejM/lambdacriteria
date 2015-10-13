package eu.inginea.lambdacriteria;

import java.util.List;
import javax.persistence.EntityManager;
import ondrom.experiments.jpa.Person;

public class LambdaQuery {

    private EntityManager em;

    public LambdaQuery(EntityManager em) {
        this.em = em;
    }
    
    public LambdaQuery select(Alias<?>... a) {
        return this;
    }
    
    public LambdaQuery from(Alias<?>... a) {
        return this;
    }
    
    public LambdaQuery where(Expression e) {
        return this;
    }

    public List<Person> getResultList() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
