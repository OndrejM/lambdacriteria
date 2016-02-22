package eu.inginea.lambdacriteria.streamQuery.memory;

import eu.inginea.lambdacriteria.streamQuery.api.QueryStream;
import java.util.stream.Stream;

public class InMemoryStreamQuery {
    public <ROOT_ENTITY> QueryStream<ROOT_ENTITY> from(Stream<ROOT_ENTITY> dataStream) {
        return new InMemoryQueryStream<>(dataStream);
    }


}
