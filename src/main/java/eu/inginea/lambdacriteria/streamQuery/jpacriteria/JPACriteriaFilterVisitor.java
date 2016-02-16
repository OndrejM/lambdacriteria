package eu.inginea.lambdacriteria.streamQuery.jpacriteria;

import eu.inginea.lambdacriteria.streamQuery.*;
import eu.inginea.lambdacriteria.streamQuery.Path;
import java.util.*;
import static java.util.Arrays.asList;
import java.util.function.Function;
import java.util.stream.*;
import static java.util.stream.Collectors.toList;
import javax.persistence.criteria.*;

public class JPACriteriaFilterVisitor implements QueryVisitor {

    private static Map<List<Class<?>>, Function<List<?>, Expression>> rules = new HashMap<>();
    private Class<?> rootClass;
    private Deque<Object> expressionBuffer = new LinkedList<>();

    static {
        rules.put(asList(Constant.class), c -> {
            return new StubExpression(c.get(0));
        });
        rules.put(asList(Expression.class, BinaryOperation.class, Expression.class), JPACriteriaFilterVisitor::concatenate);
        rules.put(asList(Parameter.class, Path.class), JPACriteriaFilterVisitor::concatenatePath);
    }

    public static Expression concatenate(List<?> expList) {
        return new StubExpression(expList);
    }

    public static Expression concatenatePath(List<?> expList) {
        return new StubExpression(expList, " . ");
    }

    public JPACriteriaFilterVisitor(Class<?> rootClass) {
        this.rootClass = rootClass;
    }

    @Override
    public void visit(Term literal) {
        expressionBuffer.add(literal);
        while (matchSomeRule()) {
        }
    }

    private boolean matchSomeRule() {
        boolean ruleMatches = false;
        List<Class<?>> expTypesList = new LinkedList<>();
        Iterator<Object> itExp = expressionBuffer.descendingIterator();
        while (itExp.hasNext() && !ruleMatches) {
            expTypesList.add(0, itExp.next().getClass());
            Function<List<?>, Expression> rule = findFirstMatchingRule(expTypesList);
            if (rule != null) {
                ruleMatches = true;
                List<Object> expList = IntStream.range(0, expTypesList.size())
                        .mapToObj(i -> expressionBuffer.pollLast())
                        .collect(toList());
                Collections.reverse(expList);
                expressionBuffer.add(rule.apply(expList));
            }
        }
        return ruleMatches;
    }

    private static Function<List<?>, Expression> findFirstMatchingRule(List<Class<?>> expTypesList) {
        Optional<List<Class<?>>> matchingKey = rules.keySet().stream()
                .filter(typeList -> {
                    Iterator<Class<?>> itTypes = expTypesList.iterator();
                    return expTypesList.size() == typeList.size()
                            && typeList.stream().allMatch(t -> t.isAssignableFrom(itTypes.next()));
                })
                .findAny();
        if (matchingKey.isPresent()) {
            return rules.get(matchingKey.get());
        } else {
            return null;
        }
    }

    public Expression getCriteriaQuery() {
        return (Expression)expressionBuffer.getFirst();
    }

    private static class StubExpression implements Expression {

        private String value;

        public StubExpression(Object c) {
            value = c.toString();
        }

        public StubExpression(Collection<?> list) {
            this(list, Collectors.joining(" "));
        }
        public StubExpression(Collection<?> list, CharSequence delimiter) {
            this(list, Collectors.joining(delimiter));
            
        }
        private StubExpression(Collection<?> list, Collector<CharSequence, ?, String> collector) {
            value = list.stream()
                    .map(o -> o.toString())
                    .collect(collector);
        }

        @Override
        public String toString() {
            return "StringExpression{" + value + '}';
        }

        @Override
        public Predicate isNull() {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public Predicate isNotNull() {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public Predicate in(Object... os) {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public Predicate in(Expression... exprsns) {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public Predicate in(Collection clctn) {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public Predicate in(Expression exprsn) {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public Expression as(Class type) {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public Selection alias(String string) {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public boolean isCompoundSelection() {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public List getCompoundSelectionItems() {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public Class getJavaType() {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public String getAlias() {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }
    }

}
