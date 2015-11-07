package eu.inginea.lambdacriteria.alternative3;

import com.trigersoft.jaque.expression.LambdaExpression;
import java.util.*;
import javax.persistence.EntityManager;

public class LambdaQuery3<RESULT> {
    
    protected Selection<RESULT, ?> selectionSingleRoot;
    protected SelectionMultipleRoots<RESULT> selectionMultipleRoots;
    
    public LambdaQuery3(EntityManager em) {
    }
    
    public <P1> LambdaQuery3<RESULT> select(Class<P1> p1Type, Selection<RESULT, P1> selection) {
        this.selectionSingleRoot = selection;
        return this;
    }

    public LambdaQuery3<RESULT> select(SelectionMultipleRoots<RESULT> selection) {
        this.selectionMultipleRoots = selection;
        return this;
    }

    public List<RESULT> getResultList() {
        if (selectionSingleRoot != null) {
            LambdaExpression<Selection> parsed = LambdaExpression.parse(selectionSingleRoot);
        } else if (selectionMultipleRoots != null) {
            LambdaExpression<SelectionMultipleRoots> parsed = LambdaExpression.parse(selectionMultipleRoots);
        }
        
        return Collections.emptyList();
    }

}
