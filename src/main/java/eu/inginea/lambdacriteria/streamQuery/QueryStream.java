package eu.inginea.lambdacriteria.streamQuery;

import java.util.stream.Collector;

public interface QueryStream<T> {
    QueryStream<T> filter(QueryPredicate<? super T> predicate);
    <R, A> R collect(Collector<? super T, A, R> collector);
}
