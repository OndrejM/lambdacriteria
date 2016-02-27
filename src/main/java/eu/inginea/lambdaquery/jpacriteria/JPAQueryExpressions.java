package eu.inginea.lambdaquery.jpacriteria;

import eu.inginea.lambdaquery.QueryExpressions;

public interface JPAQueryExpressions extends QueryExpressions {

    static boolean like(String value, String expr) {
        // TODO implement proper JPA logic
        return false;
    }

    static JPAQueryExpressions is(Object value) {
        // TODO return in memory implementation
        return null;
    }

    boolean like(String pattern);
}
