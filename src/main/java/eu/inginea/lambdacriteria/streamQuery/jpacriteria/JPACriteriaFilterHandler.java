package eu.inginea.lambdacriteria.streamQuery.jpacriteria;

import eu.inginea.lambdacriteria.base.JpaOperationType;
import eu.inginea.lambdacriteria.streamQuery.ruleengine.Path;
import eu.inginea.lambdacriteria.streamQuery.ruleengine.*;
import static java.util.Arrays.asList;
import java.util.*;
import java.util.stream.*;
import javax.persistence.criteria.*;
import eu.inginea.lambdacriteria.streamQuery.TokenHandler;

public class JPACriteriaFilterHandler implements TokenHandler {

    private final RuleEngine engine = new RuleEngine();
    private javax.persistence.criteria.Path<?> rootPath;
    private CriteriaBuilder cb;
    private CriteriaQuery q;

    {
        engine.addRule(Constant.class, this::constantToExpression);
        engine.addRule(asList(Expression.class, BinaryOperation.class, Expression.class), this::binaryOperation);
        engine.addRule(asList(Parameter.class, Path.class), this::concatenatePath);
    }

    public JPACriteriaFilterHandler(javax.persistence.criteria.Path<?> rootPath, CriteriaBuilder cb, CriteriaQuery q) {
        this.rootPath = rootPath;
        this.cb = cb;
        this.q = q;
    }

    public Expression constantToExpression(Constant c) {
        return cb.literal(c.getValue());
    }

    public Expression binaryOperation(List<?> expList) {
        int i = 0;
        Expression expLeft = (Expression) expList.get(i++);
        BinaryOperation op = (BinaryOperation) expList.get(i++);
        Expression expRight = (Expression) expList.get(i++);

        switch (op) {
            case EQUAL:
                return cb.equal(expLeft, expRight);
            default:
                throw new UnsupportedOperationException("" + op);
        }
    }

    public Expression concatenatePath(List<?> expList) {
        int i = 0;
        Parameter param = (Parameter) expList.get(i++);
        Path pathLiteral = (Path) expList.get(i++);

        return rootPath.get(pathLiteral.getPath());
    }

    @Override
    public void handleToken(Term literal) {
        engine.addTerm(literal);
    }

    public Expression<?> getCriteriaQuery() {
        return (Expression) engine.getExpression();
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
            return "Wrapped{" + value + '}';
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
