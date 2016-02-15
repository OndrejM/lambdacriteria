package eu.inginea.lambdacriteria.streamQuery;

import java.util.Optional;

/**
 * Definitions for lambda to query language literals
 */
public interface QueryMapping {

    Optional<Literal> getTermForExpression(Object expr);

}
