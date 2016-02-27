package eu.inginea.lambdaquery.jpacriteria.inmemorysupport;

import eu.inginea.lambdaquery.jpacriteria.JPAQueryExpressions;

class InMemoryJPAQueryExpressions implements JPAQueryExpressions {

    private final Object value;

    InMemoryJPAQueryExpressions(Object value) {
        this.value = value;
    }

    @Override
    public boolean like(String pattern) {
        return InMemoryFactory.getFunctions().like(value.toString(), pattern);
    }

}
