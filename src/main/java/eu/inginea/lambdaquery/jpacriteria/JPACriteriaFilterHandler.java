package eu.inginea.lambdaquery.jpacriteria;

import static java.util.Arrays.asList;
import java.util.*;
import java.util.stream.*;
import javax.persistence.criteria.*;
import eu.inginea.lambdaquery.base.TokenHandler;
import eu.inginea.lambdaquery.ruleengine.Path;
import eu.inginea.lambdaquery.ruleengine.*;
import static eu.inginea.lambdaquery.ruleengine.BinaryOperation.EQUAL;

class JPACriteriaFilterHandler<ROOT_ENTITY> implements TokenHandler {

    private final RuleEngine engine = new RuleEngine();
    private javax.persistence.criteria.Path<?> rootPath;
    private CriteriaBuilder cb;
    private CriteriaQuery<ROOT_ENTITY> q;

    {
        engine.addRule(Constant.class, this::constantToExpression);
        engine.addRule(asList(Expression.class, BinaryOperation.class, Expression.class), this::binaryOperation);
        engine.addRule(asList(InfixBinaryOperation.class, Expression.class, Expression.class), this::infixBinaryOperation);
        engine.addRule(asList(Parameter.class, Path.class), this::concatenatePath);
        engine.addRule(asList(javax.persistence.criteria.Path.class, Path.class), this::concatenatePath);
    }

    public JPACriteriaFilterHandler(javax.persistence.criteria.Path<ROOT_ENTITY> rootPath, CriteriaBuilder cb, CriteriaQuery<ROOT_ENTITY> q) {
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

    public Expression infixBinaryOperation(List<?> expList) {
        int i = 0;
        InfixBinaryOperation op = (InfixBinaryOperation) expList.get(i++);
        Expression expLeft = (Expression) expList.get(i++);
        Expression expRight = (Expression) expList.get(i++);

        switch (op) {
            case LIKE:
                return cb.like(expLeft, expRight);
            default:
                throw new UnsupportedOperationException("" + op);
        }
    }

    public Expression concatenatePath(List<?> expList) {
        int i = 0;
        Object left = expList.get(i++);
        Path pathLiteral = (Path) expList.get(i++);

        javax.persistence.criteria.Path<?> jpaPath = null;
        if (left instanceof Parameter) {
            jpaPath = rootPath;
        } else if (left instanceof javax.persistence.criteria.Path) {
            jpaPath = (javax.persistence.criteria.Path) left;
        }

        return jpaPath.get(pathLiteral.getPath());
    }

    @Override
    public void handleToken(Term literal) {
        engine.addTerm(literal);
    }

    public Expression<Boolean> getCriteriaQuery() {
        Expression expr = (Expression) engine.getExpression();
        if (!Boolean.class.equals(expr.getJavaType())) {
            throw new IllegalStateException("The final expression for filter must be of Boolean type, it is of type "
                    + expr.getJavaType().getSimpleName() + " instead.");
        }
        return expr;
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
