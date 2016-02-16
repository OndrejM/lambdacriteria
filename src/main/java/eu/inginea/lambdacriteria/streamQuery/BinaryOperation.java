package eu.inginea.lambdacriteria.streamQuery;

import com.trigersoft.jaque.expression.ExpressionType;
import eu.inginea.lambdacriteria.streamQuery.Literal;

public enum BinaryOperation implements Literal {

    EQUAL(ExpressionType.Equal);
    
    private int exprType;

    private BinaryOperation(int operation) {
        this.exprType = operation;
    }

    @Override
    public String toString() {
        return ExpressionType.toString(exprType);
    }
    
}
