package eu.inginea.lambdacriteria.alternative3;

import java.io.Serializable;

@FunctionalInterface
public interface Selection<RESULT, P1> extends Serializable {
    public RESULT select(P1 rootEntity);
}
