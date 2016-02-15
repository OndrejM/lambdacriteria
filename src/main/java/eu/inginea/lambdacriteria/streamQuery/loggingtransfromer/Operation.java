package eu.inginea.lambdacriteria.streamQuery.loggingtransfromer;

import com.trigersoft.jaque.expression.ExpressionType;
import eu.inginea.lambdacriteria.streamQuery.Literal;

public enum Operation implements Literal {

    EQUAL(ExpressionType.Equal);
    
    private int exprType;

    private Operation(int operation) {
        this.exprType = operation;
    }

    @Override
    public String toString() {
        return ExpressionType.toString(exprType);
    }
    
}
