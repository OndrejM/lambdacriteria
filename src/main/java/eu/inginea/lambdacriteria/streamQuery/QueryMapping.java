package eu.inginea.lambdacriteria.streamQuery;

import eu.inginea.lambdacriteria.streamQuery.ruleengine.Literal;
import java.util.Optional;

/**
 * Definitions for lambda to query language literals
 */
public interface QueryMapping {

    Optional<Literal> getTermForExpression(Object expr);

}
