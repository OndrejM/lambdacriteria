package eu.inginea.lambdacriteria.base;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.criteria.Expression;

public class ExpressionInfo {
    private List<Expression<?>> jpaExpressions = new ArrayList<>();
    private int numberOfExpected = 1;
    
    public Expression<?> getJpaExpression() {
        return jpaExpressions.stream().findFirst().orElse(null);
    }

    public List<Expression<?>> getJpaExpressions() {
        return jpaExpressions;
    }
 
    public void addJpaExpression(Expression expr) {
        jpaExpressions.clear();
        jpaExpressions.add(expr);
    }
}
