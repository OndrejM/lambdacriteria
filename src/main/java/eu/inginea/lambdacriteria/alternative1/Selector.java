package eu.inginea.lambdacriteria.alternative1;

import java.io.Serializable;
import java.util.concurrent.Callable;

public interface Selector<SELECTED> extends Callable<SELECTED>, Serializable {

}
