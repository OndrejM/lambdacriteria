package eu.inginea.lambdaquery.memory;

import eu.inginea.lambdaquery.base.QueryPredicate;
import eu.inginea.lambdaquery.QueryStream;
import java.util.stream.*;

class InMemoryQueryStream<ROOT_ENTITY> implements QueryStream<ROOT_ENTITY> {
    private Stream<ROOT_ENTITY> dataStream;

    public InMemoryQueryStream(Stream<ROOT_ENTITY> dataStream) {
        this.dataStream = dataStream;
    }
    
    @Override
    public QueryStream<ROOT_ENTITY> filter(QueryPredicate<? super ROOT_ENTITY> predicate) {
        // TODO figure out why compiler refuses dataStream.filter(predicate)
        dataStream = dataStream.filter(p -> {
            return predicate.select(p);
                });
        return this;
    }

    @Override
    public <R, A> R collect(Collector<? super ROOT_ENTITY, A, R> collector) {
        return dataStream.collect(collector);
    }

}
