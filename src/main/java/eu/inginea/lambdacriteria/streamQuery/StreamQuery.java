package eu.inginea.lambdacriteria.streamQuery;

import eu.inginea.lambdacriteria.streamQuery.loggingtransfromer.LambdaQueryLoggingTransformer;
import javax.persistence.EntityManager;

public class StreamQuery<T> {

    private final EntityManager em;

    public StreamQuery(EntityManager em) {
        this.em = em;
    }

    public QueryStream<T> from(Class<T> aClass) {
        // stub implementation doing everything in memory
        String entityName = em.getMetamodel().entity(aClass).getName();
        LambdaQueryLoggingTransformer transformer = new LambdaQueryLoggingTransformer();
        return new LambdaTransformingStream<>((StreamOperation op) -> new LambdaVisitor(op),
                () -> transformer,
                () -> transformer,
                transformer::executeQuery
        );
    }

}
