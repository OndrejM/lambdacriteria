package eu.inginea.lambdacriteria.streamQuery;

import com.trigersoft.jaque.expression.ExpressionType;
import java.lang.reflect.Method;
import java.util.*;

/**
 * Describes transformations from lambda expressions in query stream to final query like JPQL or Criteria
 */
class LambdaQueryLoggingTransformer implements QueryMapping, QueryVisitor {
    private Map<Object,Literal> literalsMapping = new HashMap<>();
    private Collection<LiteralMapper> literalMappers = new ArrayList<>();
    
    {
        OperationLiteral opEqual = new OperationLiteral(ExpressionType.Equal);
        addLiteralsMapping(opEqual, ExpressionType.Equal);
        addLiteralMapper(new MethodLiteral(opEqual, "equals", Object.class));
    }

    @Override
    public Optional<Literal> getLiteralForExpression(Object expr) {
        Optional<Literal> literal = Optional.ofNullable(literalsMapping.get(expr));
        if (!literal.isPresent()) {
            literal = literalMappers.stream()
                    .map(mapper -> mapper.getLiteral(expr))
                    .filter(l -> l != null)
                    .findAny();
        }
        return literal;
    }

    @Override
    public void visit(Object literal) {
        System.out.println("Log: " + literal);
    }

    private void addLiteralsMapping(Literal value, Object key) {
        if (key != null) {
            literalsMapping.put(key, value);
        }
    }

    private void addLiteralMapper(LiteralMapper mapper) {
        literalMappers.add(mapper);
    }

    private interface LiteralMapper {
        public Literal getLiteral(Object key);
    }
    
    private class MethodLiteral implements LiteralMapper {
        private final String methodName;
        private final List<Class<?>> parameterTypes;
        private final Literal targetLiteral;

        public MethodLiteral(Literal targetLiteral, String methodName, Class<?>... parameterTypes) {
            this.methodName = methodName;
            this.parameterTypes = Arrays.asList(parameterTypes);
            this.targetLiteral = targetLiteral;
        }
        
        @Override
        public Literal getLiteral(Object o) {
            if (o instanceof Method) {
                Method m = (Method)o;
                if (m.getName().equals(methodName) 
                        && Arrays.asList(m.getParameterTypes()).equals(parameterTypes)) {
                    return targetLiteral;
                }
            }
            return null;
        }
    }
    
    private class OperationLiteral implements Literal {
        int operation;

        public OperationLiteral(int operation) {
            this.operation = operation;
        }
        
        @Override
        public String toString() {
            return ExpressionType.toString(operation);
        }
    }
    
}
