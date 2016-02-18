package eu.inginea.lambdacriteria.streamQuery;

import com.trigersoft.jaque.expression.LambdaExpression;
import java.util.*;
import java.util.function.*;
import java.util.stream.*;

class LambdaTransformingStream<ROOT> implements QueryStream<ROOT> {

    private final Function<StreamOperation, ? extends LambdaVisitor> lambdaVisitorSupplier;
    private final Supplier<? extends QueryMapping> queryMappingSupplier;
    private final Supplier<? extends QueryVisitor> queryVisitorSupplier;
    private Supplier<Stream<ROOT>> executeQuery;

    public LambdaTransformingStream(QueryTransformer<ROOT> transformer) {
        this.lambdaVisitorSupplier = transformer::supplyLambdaVisitor;
        this.queryMappingSupplier = transformer::supplyMapping;
        this.queryVisitorSupplier = transformer::supplyQueryVisitor;
        this.executeQuery = transformer::getResults;
    }

    @Override
    public QueryStream<ROOT> filter(QueryPredicate<? super ROOT> predicate) {
        LambdaVisitor lambdaVisitor = lambdaVisitorSupplier.apply(StreamOperation.FILTER);
        lambdaVisitor.setQueryMapping(queryMappingSupplier.get());
        lambdaVisitor.setQueryVisitor(queryVisitorSupplier.get());
        LambdaExpression.parse(predicate).accept(lambdaVisitor);
        return this;
    }

    @Override
    public <COLLECTED, ACCUMULATION> COLLECTED collect(Collector<? super ROOT, ACCUMULATION, COLLECTED> collector) {
        Stream<ROOT> inputStream = executeQuery.get();
        return inputStream.collect(collector);
    }

    public <MAPPED_TO> Stream<MAPPED_TO> map(Function<? super ROOT, ? extends MAPPED_TO> mapper) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public IntStream mapToInt(ToIntFunction<? super ROOT> mapper) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public LongStream mapToLong(ToLongFunction<? super ROOT> mapper) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public DoubleStream mapToDouble(ToDoubleFunction<? super ROOT> mapper) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public <R> Stream<R> flatMap(Function<? super ROOT, ? extends Stream<? extends R>> mapper) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public IntStream flatMapToInt(Function<? super ROOT, ? extends IntStream> mapper) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public LongStream flatMapToLong(Function<? super ROOT, ? extends LongStream> mapper) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public DoubleStream flatMapToDouble(Function<? super ROOT, ? extends DoubleStream> mapper) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public Stream<ROOT> distinct() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public Stream<ROOT> sorted() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public Stream<ROOT> sorted(Comparator<? super ROOT> comparator) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public Stream<ROOT> peek(Consumer<? super ROOT> action) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public Stream<ROOT> limit(long maxSize) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public Stream<ROOT> skip(long n) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void forEach(Consumer<? super ROOT> action) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void forEachOrdered(Consumer<? super ROOT> action) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public Object[] toArray() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public <A> A[] toArray(IntFunction<A[]> generator) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public ROOT reduce(ROOT identity, BinaryOperator<ROOT> accumulator) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public Optional<ROOT> reduce(BinaryOperator<ROOT> accumulator) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public <U> U reduce(U identity, BiFunction<U, ? super ROOT, U> accumulator, BinaryOperator<U> combiner) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public <R> R collect(Supplier<R> supplier, BiConsumer<R, ? super ROOT> accumulator, BiConsumer<R, R> combiner) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public Optional<ROOT> min(Comparator<? super ROOT> comparator) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public Optional<ROOT> max(Comparator<? super ROOT> comparator) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public long count() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public boolean anyMatch(Predicate<? super ROOT> predicate) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public boolean allMatch(Predicate<? super ROOT> predicate) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public boolean noneMatch(Predicate<? super ROOT> predicate) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public Optional<ROOT> findFirst() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public Optional<ROOT> findAny() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public Iterator<ROOT> iterator() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public Spliterator<ROOT> spliterator() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public boolean isParallel() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public Stream<ROOT> sequential() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public Stream<ROOT> parallel() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public Stream<ROOT> unordered() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public Stream<ROOT> onClose(Runnable closeHandler) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void close() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
