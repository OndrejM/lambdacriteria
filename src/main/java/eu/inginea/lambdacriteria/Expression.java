package eu.inginea.lambdacriteria;

import java.io.Serializable;
import java.util.concurrent.Callable;

public interface Expression extends Callable<Object>, Serializable {

}
