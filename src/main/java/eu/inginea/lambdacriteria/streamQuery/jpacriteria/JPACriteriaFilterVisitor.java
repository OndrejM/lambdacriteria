package eu.inginea.lambdacriteria.streamQuery.jpacriteria;

import eu.inginea.lambdacriteria.streamQuery.*;
import eu.inginea.lambdacriteria.streamQuery.Path;
import eu.inginea.lambdacriteria.streamQuery.ruleengine.RuleEngine;
import java.util.*;
import static java.util.Arrays.asList;
import java.util.function.Function;
import java.util.stream.*;
import static java.util.stream.Collectors.toList;
import javax.persistence.criteria.*;

public class JPACriteriaFilterVisitor implements QueryVisitor {

    private static final RuleEngine engine = new RuleEngine();
    private Class<?> rootClass;

    static {
        engine.addRule(Constant.class, c -> new StubExpression(c));
        engine.addRule(asList(Expression.class, BinaryOperation.class, Expression.class), JPACriteriaFilterVisitor::concatenate);
        engine.addRule(asList(Parameter.class, Path.class), JPACriteriaFilterVisitor::concatenatePath);
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
        engine.addTerm(literal);
    }

    public Expression getCriteriaQuery() {
        return (Expression)engine.getExpression();
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