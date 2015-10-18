package eu.inginea.lambdacriteria.base;

import com.trigersoft.jaque.expression.ExpressionType;
import java.util.HashMap;
import java.util.Map;

public enum JpaOperationType {
    EQUALS;
    
    private static Map<Integer, JpaOperationType> fromJaQueMapping = new HashMap<>();
    
    static {
        fromJaQueMapping.put(ExpressionType.Equal, EQUALS);
    }
    
    public static JpaOperationType valueFromJaQueOperationType(int exprType) {
        return fromJaQueMapping.get(exprType);
    }
}
