package eu.inginea.lambdacriteria.streamQuery.jpacriteria;

import eu.inginea.lambdacriteria.streamQuery.LambdaTransformingStream;
import eu.inginea.lambdacriteria.streamQuery.LambdaVisitor;
import eu.inginea.lambdacriteria.streamQuery.QueryMapping;
import eu.inginea.lambdacriteria.streamQuery.api.QueryStream;
import eu.inginea.lambdacriteria.streamQuery.QueryTransformer;
import eu.inginea.lambdacriteria.streamQuery.StreamOperation;
import eu.inginea.lambdacriteria.streamQuery.TokenHandler;
import java.util.stream.Stream;
import javax.persistence.EntityManager;
import javax.persistence.criteria.*;

// TODO generalize to use any grammar, not depend on entity manager
public class JPAStreamQuery {

    private final EntityManager em;

    public JPAStreamQuery(EntityManager em) {
        this.em = em;
    }

    public <ROOT_ENTITY> QueryStream<ROOT_ENTITY> from(Class<ROOT_ENTITY> aClass) {
        String entityName = em.getMetamodel().entity(aClass).getName();
        return new LambdaTransformingStream<>(new JPATransformer<ROOT_ENTITY>(aClass, em));
    }
    
    private static class JPATransformer<ROOT_ENTITY> implements QueryTransformer<ROOT_ENTITY> {
        private QueryMapping expressionMapping = new FilterExpressionMapping();
        private Class<ROOT_ENTITY> rootClass;
        private EntityManager em;
        private CriteriaBuilder cb;
        private CriteriaQuery<ROOT_ENTITY> q;
        private final Root<ROOT_ENTITY> rootPath;
        private JPACriteriaFilterHandler<ROOT_ENTITY> filterHandler;

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
            return expressionMapping;
        }

        @Override
        public TokenHandler supplyQueryVisitor() {
            filterHandler = new JPACriteriaFilterHandler<>(rootPath, cb, q);
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
