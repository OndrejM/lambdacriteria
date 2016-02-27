package eu.inginea.lambdaquery.jpacriteria.inmemorysupport;

import java.util.Arrays;
import java.util.regex.Pattern;
import static java.util.stream.Collectors.joining;

/*
 * Functions that mimic JPQL operators when query is executed as a normal 
 * Java stream. This object is global and unsynchronized, therefore stateless.
 */
public class InMemoryFunctions {

    private final Pattern anyCharsSQLPattern = Pattern.compile(Pattern.quote("%"));
    private final Pattern singleCharSQLPattern = Pattern.compile(Pattern.quote("_"));

    InMemoryFunctions() {
    }

    public boolean like(String value, String expr) {
        String regex = Arrays.stream(anyCharsSQLPattern.split(expr, -1))
                .map(tokenAny -> singleCharSQLPattern.split(tokenAny, -1))
                .map(arrayTokenSingle -> Arrays.stream(arrayTokenSingle)
                        .map(tokenSingle -> Pattern.quote(tokenSingle))
                        .collect(joining(".")))
                .collect(joining(".*"));
        
        return Pattern.matches(regex, value);
    }
}
