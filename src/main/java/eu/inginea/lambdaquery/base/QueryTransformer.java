package eu.inginea.lambdaquery.base;

import java.util.stream.Stream;

public interface QueryTransformer<ROOT_ENTITY> {

    LambdaVisitor supplyLambdaVisitor(StreamOperation op);

    QueryMapping supplyMapping();

    TokenHandler supplyQueryVisitor();
    
    Stream<ROOT_ENTITY> getResults();
}
