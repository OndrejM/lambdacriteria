package eu.inginea.lambdacriteria.streamQuery.ruleengine;

import com.trigersoft.jaque.expression.ExpressionType;

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
