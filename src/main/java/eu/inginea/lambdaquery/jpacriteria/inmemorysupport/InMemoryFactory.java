package eu.inginea.lambdaquery.jpacriteria.inmemorysupport;

import eu.inginea.lambdaquery.jpacriteria.JPAQueryExpressions;

public class InMemoryFactory {

    private static final InMemoryFunctions FUNCTIONS = new InMemoryFunctions();

    public static InMemoryFunctions getFunctions() {
        return FUNCTIONS;
    }

    public static JPAQueryExpressions getExpressions(Object value) {
        return new InMemoryJPAQueryExpressions(value);
    }

}
