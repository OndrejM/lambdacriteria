package eu.inginea.lambdacriteria.streamQuery;

import eu.inginea.lambdacriteria.streamQuery.ruleengine.Term;

/**
 * Visitor of stream lambda literals
 */
public interface QueryVisitor {

    public void visit(Term literal);

}
