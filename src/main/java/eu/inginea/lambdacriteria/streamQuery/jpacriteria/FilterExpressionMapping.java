package eu.inginea.lambdacriteria.streamQuery.jpacriteria;

import com.trigersoft.jaque.expression.ExpressionType;
import eu.inginea.lambdacriteria.streamQuery.QueryMapping;
import eu.inginea.lambdacriteria.streamQuery.ruleengine.*;
import java.lang.reflect.Method;
import java.util.*;

class FilterExpressionMapping implements QueryMapping {
    private final Map<Object,Literal> literalsMap = new HashMap<>();
    private final Collection<LiteralMapper> literalMappers = new ArrayList<>();
    
    {
        BinaryOperation opEqual = BinaryOperation.EQUAL;
        addLiteralsMapping(opEqual, ExpressionType.Equal);
        addLiteralMapper(new MethodLiteral(opEqual, "equals", Object.class));
    }

    @Override
    public Optional<Literal> getTermForExpression(Object expr) {
        Optional<Literal> literal = Optional.ofNullable(literalsMap.get(expr));
        if (!literal.isPresent()) {
            literal = literalMappers.stream()
                    .map(mapper -> mapper.getLiteral(expr))
                    .filter(l -> l != null)
                    .findAny();
        }
        return literal;
    }

    private void addLiteralsMapping(Literal value, Object key) {
        if (key != null) {
            literalsMap.put(key, value);
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
    

}
