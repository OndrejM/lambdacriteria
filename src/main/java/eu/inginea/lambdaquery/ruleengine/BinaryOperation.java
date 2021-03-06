package eu.inginea.lambdaquery.ruleengine;

import com.trigersoft.jaque.expression.ExpressionType;

public enum BinaryOperation implements Literal {

    EQUAL(ExpressionType.Equal),
    LIKE;
    
    private int exprType;
    private String operationWithoutExpression = null;

    private BinaryOperation(int operation) {
        this.exprType = operation;
    }

    private BinaryOperation() {
        this.operationWithoutExpression = this.getClass().getSimpleName() + "." + this.name();
    }

    @Override
    public String toString() {
        return operationWithoutExpression != null 
                ? operationWithoutExpression 
                : ExpressionType.toString(exprType);
    }
    
}
