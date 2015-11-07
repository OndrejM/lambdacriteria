package eu.inginea.lambdacriteria.alternative3;

import java.io.Serializable;

@FunctionalInterface
public interface SelectionMultipleRoots<RESULT> extends Serializable {
    public RESULT select(Object rootEntities);
}
