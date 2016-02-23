package eu.inginea.lambdaquery.base;

import eu.inginea.lambdaquery.ruleengine.Term;

/**
 * Visitor of stream lambda literals
 */
public interface TokenHandler {

    public void handleToken(Term literal);

}
