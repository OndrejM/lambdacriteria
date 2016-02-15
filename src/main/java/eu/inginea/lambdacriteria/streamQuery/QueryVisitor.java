package eu.inginea.lambdacriteria.streamQuery;

/**
 * Visitor of stream lambda literals
 */
public interface QueryVisitor {

    public void visit(Object literal);

}
