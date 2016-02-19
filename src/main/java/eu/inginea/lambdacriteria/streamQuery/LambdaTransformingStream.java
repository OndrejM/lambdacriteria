package eu.inginea.lambdacriteria.streamQuery;

import eu.inginea.lambdacriteria.streamQuery.api.QueryStream;
import com.trigersoft.jaque.expression.LambdaExpression;
import java.util.function.*;
import java.util.stream.*;

public class LambdaTransformingStream<ROOT> implements QueryStream<ROOT> {

    private final Function<StreamOperation, ? extends LambdaVisitor> lambdaVisitorSupplier;
    private final Supplier<? extends QueryMapping> queryMappingSupplier;
    private final Supplier<? extends TokenHandler> queryVisitorSupplier;
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

}
