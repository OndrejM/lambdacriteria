package eu.inginea.lambdacriteria.streamQuery;

import com.trigersoft.jaque.expression.ExpressionType;
import java.util.*;

/**
 * Describes transformations from lambda expressions in query stream to final query like JPQL or Criteria
 */
class LambdaQueryLoggingTransformer implements QueryMapping, QueryVisitor {
    private Map<Object,Object> literalsMapping = new HashMap<>();
    
    {
        literalsMapping.put(ExpressionType.Equal, ExpressionType.Equal);
    }

    public Map<Object, Object> getLiteralsMapping() {
        return literalsMapping;
    }
    
    
    
}
