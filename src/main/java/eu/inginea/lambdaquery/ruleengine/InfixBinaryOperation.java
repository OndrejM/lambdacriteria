package eu.inginea.lambdaquery.ruleengine;

import com.trigersoft.jaque.expression.ExpressionType;

/**
 *
 * @author ondro
 */
public enum InfixBinaryOperation implements Literal {
    LIKE;

    private int exprType;
    private String operationWithoutExpression = null;

    private InfixBinaryOperation(int operation) {
        this.exprType = operation;
    }

    private InfixBinaryOperation() {
        this.operationWithoutExpression = this.getClass().getSimpleName() + "." + this.name();
    }

    @Override
    public String toString() {
        return operationWithoutExpression != null
                ? operationWithoutExpression
                : ExpressionType.toString(exprType);
    }

}
