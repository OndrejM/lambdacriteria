package eu.inginea.lambdacriteria.streamQuery.jpacriteria;

import eu.inginea.lambdacriteria.streamQuery.*;
import javax.persistence.criteria.Expression;

public class JPACriteriaFilterVisitor implements QueryVisitor {

    private Class<?> rootClass;

    public JPACriteriaFilterVisitor(Class<?> rootClass) {
        this.rootClass = rootClass;
    }
    
    @Override
    public void visit(Term literal) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    public Expression getCriteriaQuery() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
