package eu.inginea.lambdaquery.base;

import eu.inginea.lambdaquery.ruleengine.Literal;
import java.util.Optional;

/**
 * Definitions for lambda to query language literals
 */
public interface QueryMapping {

    Optional<Literal> getTermForExpression(Object expr);

}
