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
        private Class<?> rootClass;
        private CriteriaBuilder cb;
        private CriteriaQuery q;
        private final Root rootPath;

        public JPATransformer(Class<?> rootClass, EntityManager em) {
            this.rootClass = rootClass;
            cb = em.getCriteriaBuilder();
            q = cb.createQuery();
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
            return new JPACriteriaFilterHandler(rootPath, cb, q);
        }

        @Override
        public Stream<ROOT_ENTITY> getResults() {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }
        
    }

}
