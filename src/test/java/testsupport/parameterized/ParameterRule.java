package testsupport.parameterized;

import java.util.*;
import org.junit.rules.*;
import org.junit.runner.Description;
import org.junit.runners.model.*;

public class ParameterRule<PARAM_TYPE> implements TestRule {

    private final Iterable<PARAM_TYPE> parameters;
    private PARAM_TYPE currentParam;

    public ParameterRule(Iterable<PARAM_TYPE> parameters) {
        this.parameters = parameters;
    }

    public PARAM_TYPE getParameter() {
        return currentParam;
    }

    @Override
    public Statement apply(final Statement stmnt, Description d) {
        return new Statement() {

            @Override
            public void evaluate() throws Throwable {

                List<Throwable> errors = new ArrayList<Throwable>();
                Iterator<PARAM_TYPE> itParams = parameters.iterator();
                int index = 0;

                while (itParams.hasNext()) {
                    currentParam = itParams.next();
                    try {
                        stmnt.evaluate();
                    } catch (Throwable ex) {
                        addError(errors, ex, index);
                    }
                    index++;
                }

                MultipleFailureException.assertEmpty(errors);
            }

            private boolean addError(List<Throwable> errors, Throwable ex, int index) {
                return errors.add(new AssertionError("(parameter " + index + ") " 
                        + ex.getMessage(), ex));
            }
        };
    }

}
