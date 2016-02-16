package eu.inginea.lambdacriteria.streamQuery;

import java.util.stream.Stream;

public interface ExecuteQuery<RESULT> {
    Stream<RESULT> executeQuery();
}
