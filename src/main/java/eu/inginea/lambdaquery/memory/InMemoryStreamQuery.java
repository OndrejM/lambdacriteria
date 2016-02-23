package eu.inginea.lambdaquery.memory;

import eu.inginea.lambdaquery.QueryStream;
import java.util.stream.Stream;

public class InMemoryStreamQuery {
    public <ROOT_ENTITY> QueryStream<ROOT_ENTITY> from(Stream<ROOT_ENTITY> dataStream) {
        return new InMemoryQueryStream<>(dataStream);
    }


}
