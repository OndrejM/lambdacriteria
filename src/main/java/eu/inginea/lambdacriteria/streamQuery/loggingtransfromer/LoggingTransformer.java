package eu.inginea.lambdacriteria.streamQuery.loggingtransfromer;

import com.trigersoft.jaque.expression.ExpressionType;
import eu.inginea.lambdacriteria.streamQuery.*;
import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Stream;

/**
 * Describes transformations from lambda expressions in query stream to final query like JPQL or Criteria
 */
public class LoggingTransformer implements QueryMapping, QueryVisitor, ExecuteQuery {
    private final Map<Object,Literal> literalsMap = new HashMap<>();
    private final Collection<LiteralMapper> literalMappers = new ArrayList<>();
    
    {
        Operation opEqual = Operation.EQUAL;
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

    @Override
    public void visit(Term literal) {
        System.out.println("Log: " + literal);
    }

    private void addLiteralsMapping(Literal value, Object key) {
        if (key != null) {
            literalsMap.put(key, value);
        }
    }

    private void addLiteralMapper(LiteralMapper mapper) {
        literalMappers.add(mapper);
    }

    @Override
    public Stream executeQuery() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
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
