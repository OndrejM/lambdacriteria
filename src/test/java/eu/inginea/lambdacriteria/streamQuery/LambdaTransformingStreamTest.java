package eu.inginea.lambdacriteria.streamQuery;

import java.util.List;
import java.util.stream.Collectors;
import ondrom.experiments.jpa.*;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author ondro
 */
public class LambdaTransformingStreamTest implements BDDTestBase {
    
    public LambdaTransformingStreamTest() {
    }

    @Test
    public void canQueryPersonByNameUsingTransfromingStream() {
        List<Person> persons = from(Person.class)
                .filter((p) -> "Ondro".equals(p.getName()))
                .collect(Collectors.toList());
    }

    private static <ENTITY> LambdaTransformingStream<ENTITY> from(Class<ENTITY> entityClass) {
        return new LambdaTransformingStream<>((StreamOperation op) -> new LambdaVisitor(op));
    }

}
