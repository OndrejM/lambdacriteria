package eu.inginea.lambdaquery.ruleengine;

import com.trigersoft.jaque.expression.ExpressionType;

public enum UnaryOperation implements Literal {

    IS;

    private int exprType;
    private String operationWithoutExpression = null;

    private UnaryOperation(int operation) {
        this.exprType = operation;
    }

    private UnaryOperation() {
        this.operationWithoutExpression = this.getClass().getSimpleName() + "." + this.name();
    }

    @Override
    public String toString() {
        return operationWithoutExpression != null
                ? operationWithoutExpression
                : ExpressionType.toString(exprType);
    }

}
