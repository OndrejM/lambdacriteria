package eu.inginea.lambdacriteria.streamQuery;

import javax.persistence.EntityManager;

public class StreamQuery<T> {
    private final EntityManager em;

    public StreamQuery(EntityManager em) {
        this.em = em;
    }

    public QueryStream<T> from(Class<T> aClass) {
        // stub implementation doing everything in memory
        String entityName = em.getMetamodel().entity(aClass).getName();
        return new LambdaTransformingStream<>();
    }

}
