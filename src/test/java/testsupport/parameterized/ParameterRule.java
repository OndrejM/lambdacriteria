package testsupport.parameterized;

import java.util.*;
import org.junit.AssumptionViolatedException;
import org.junit.rules.*;
import org.junit.runner.Description;
import org.junit.runners.model.*;

/**
 * JUnit rule to execute parameterized tests. To make a test parameterized, just
 * call rule.getParameter() inside the test method. Test will be executed once
 * for each parameter. Rule is triggered only for tests which use the parameter
 * during their execution. When multiple parameter rules are used inside a
 * single test, the test will be executed for every combination of values. For
 * example usage, see ParameteriedRuleTest.
 * <p>
 * If parameters need to be created only after the test is started, provide
 * supplier for each parameter using ProducingIterable.
 * <p>
 * If all parameterized executions succeed, jUnit will report only one test as
 * passed. For each failing execution, there will be additional failed test in
 * report.
 * @param <PARAM_TYPE> Type of values passed as parameters via this rule
 */
public class ParameterRule<PARAM_TYPE> implements TestRule {

    private final Iterable<PARAM_TYPE> parameters;
    private PARAM_TYPE currentParam;
    private boolean paramUsedInTest;
    Iterator<PARAM_TYPE> itParams;

    public ParameterRule(Iterable<PARAM_TYPE> parameters) {
        this.parameters = parameters;
    }

    public PARAM_TYPE getParameter() {
        if (!paramUsedInTest) {
            paramUsedInTest = true;
            if (itParams.hasNext()) {
                currentParam = itParams.next();
            } else {
                throw new AssumptionViolatedException("Skipping this test as no parameter left in the list, probably the list of parameters is empty");
            }
        }
        return currentParam;
    }

    @Override
    public Statement apply(final Statement stmnt, Description d) {
        return new Statement() {

            @Override
            public void evaluate() throws Throwable {

                List<Throwable> errors = new ArrayList<>();
                itParams = parameters.iterator();
                int index = 0;

                paramUsedInTest = false;
                try {
                    stmnt.evaluate();
                } catch (Throwable ex) {
                    if (!paramUsedInTest) {
                        throw ex;
                    } else {
                        addError(errors, ex, index);
                    }
                }

                if (paramUsedInTest) {
                    while (itParams.hasNext()) {
                        index++;
                        paramUsedInTest = false;
                        try {
                            stmnt.evaluate();
                        } catch (Throwable ex) {
                            if (paramUsedInTest) {
                                addError(errors, ex, index);
                            }
                        }
                        if (!paramUsedInTest) {
                            throw new IllegalStateException("Parameterized test does not use parameter. You should not skip getting parameter for some tests");
                        }
                    }
                    MultipleFailureException.assertEmpty(errors);
                }

            }

            private boolean addError(List<Throwable> errors, Throwable ex, int index) {
                return errors.add(new AssertionError("(parameter " + index + ") "
                        + ex.getMessage(), ex));
            }
        };
    }

}
