package eu.inginea.lambdacriteria.streamQuery;

import eu.inginea.lambdacriteria.streamQuery.jpacriteria.JPACriteriaFilterHandler;
import eu.inginea.lambdacriteria.streamQuery.loggingtransfromer.LoggingTransformer;
import java.util.stream.Stream;
import javax.persistence.EntityManager;
import javax.persistence.criteria.*;

public class StreamQuery<ROOT_ENTITY> {

    private final EntityManager em;

    public StreamQuery(EntityManager em) {
        this.em = em;
    }

    public QueryStream<ROOT_ENTITY> from(Class<ROOT_ENTITY> aClass) {
        String entityName = em.getMetamodel().entity(aClass).getName();
        return new LambdaTransformingStream<>(new JPATransformer<ROOT_ENTITY>(aClass, em));
    }
    
    private static class JPATransformer<ROOT_ENTITY> implements QueryTransformer<ROOT_ENTITY> {
        private LoggingTransformer loggingTransformer = new LoggingTransformer();
        private Class<ROOT_ENTITY> rootClass;
        private EntityManager em;
        private CriteriaBuilder cb;
        private CriteriaQuery<ROOT_ENTITY> q;
        private final Root rootPath;
        private JPACriteriaFilterHandler filterHandler;

        public JPATransformer(Class<ROOT_ENTITY> rootClass, EntityManager em) {
            this.rootClass = rootClass;
            this.em = em;
            cb = em.getCriteriaBuilder();
            q = cb.createQuery(rootClass);
            rootPath = q.from(rootClass);
        }
        
        @Override
        public LambdaVisitor supplyLambdaVisitor(StreamOperation op) {
            return new LambdaVisitor(op);
        }

        @Override
        public QueryMapping supplyMapping() {
                // TODO put mapping to JPA specific class, or generalize
            return loggingTransformer;
        }

        @Override
        public TokenHandler supplyQueryVisitor() {
            filterHandler = new JPACriteriaFilterHandler(rootPath, cb, q);
            return filterHandler;
        }

        @Override
        public Stream<ROOT_ENTITY> getResults() {
            if (filterHandler != null) {
                q.where(filterHandler.getCriteriaQuery());
            }
            return em.createQuery(q).getResultList().stream();
        }
        
    }

}
