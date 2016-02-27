package eu.inginea.lambdaquery.jpacriteria;

import com.trigersoft.jaque.expression.ExpressionType;
import eu.inginea.lambdaquery.base.QueryMapping;
import eu.inginea.lambdaquery.ruleengine.*;
import static eu.inginea.lambdaquery.ruleengine.BinaryOperation.*;
import static eu.inginea.lambdaquery.ruleengine.InfixBinaryOperation.*;
import java.lang.reflect.Method;
import java.util.*;

class FilterExpressionMapping implements QueryMapping {
    private final Map<Object,Literal> literalsMap = new HashMap<>();
    private final Collection<LiteralMapper> literalMappers = new ArrayList<>();
    
    {
        addLiteralsMapping(EQUAL, ExpressionType.Equal);
        addLiteralMapper(new MethodLiteral(EQUAL, "equals", Object.class));
        addLiteralMapper(new MethodLiteral(LIKE, "like", String.class, String.class));
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
