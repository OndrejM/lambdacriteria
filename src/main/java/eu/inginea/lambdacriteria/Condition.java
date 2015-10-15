package eu.inginea.lambdacriteria;

import java.io.Serializable;
import java.util.concurrent.Callable;

/**
 * Functional interface to pass lambda where queries
 */
public interface Condition extends Callable<Boolean>, Serializable {

}
