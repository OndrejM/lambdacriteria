package eu.inginea.lambdacriteria;

import java.io.Serializable;
import java.util.concurrent.Callable;

public interface Condition extends Callable<Boolean>, Serializable {

}
