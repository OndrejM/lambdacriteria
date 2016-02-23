package eu.inginea.lambdaquery.base;

import java.util.stream.Stream;

public interface ExecuteQuery<RESULT> {
    Stream<RESULT> executeQuery();
}
