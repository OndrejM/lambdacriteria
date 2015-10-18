package eu.inginea.lambdacriteria.base;

import java.util.Deque;
import java.util.LinkedList;
import javax.persistence.criteria.Expression;

public class ExpressionInfo {

    private final Deque<Expression<?>> jpaExpressions = new LinkedList<>();

    public Expression<?> getJpaExpression() {
        return jpaExpressions.stream().findFirst().orElse(null);
    }

    public void addJpaExpression(Expression expr) {
        if (!jpaExpressions.isEmpty()) {
            jpaExpressions.pop();
        }
        jpaExpressions.push(expr);
    }

    public void push() {
        jpaExpressions.push(null);
    }

    public Expression<?> pop() {
        return jpaExpressions.pop();
    }
}
