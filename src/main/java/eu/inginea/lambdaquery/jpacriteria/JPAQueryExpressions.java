package eu.inginea.lambdaquery.jpacriteria;

import eu.inginea.lambdaquery.QueryExpressions;
import eu.inginea.lambdaquery.jpacriteria.inmemorysupport.*;

public interface JPAQueryExpressions extends QueryExpressions {

    boolean like(String pattern);

    static boolean like(String value, String expr) {
        return InMemoryFactory.getFunctions().like(value, expr);
    }

    static JPAQueryExpressions is(Object value) {
        return InMemoryFactory.getExpressions(value);
    }

}
