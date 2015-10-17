package eu.inginea.lambdacriteria.alternative1;

import eu.inginea.lambdacriteria.Alias;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import ondrom.experiments.jpa.Person;

public class LambdaInMemoryQuery<T> extends LambdaQuery<T> {

    protected Map<Alias, Stream> rootStreams = new HashMap<>();
    protected List<T> result = null;
    
    public LambdaInMemoryQuery() {
        super(null);
    }

    public LambdaInMemoryQuery withData(Alias<Person> p, Stream<Person> stream) {
        rootStreams.put(p, stream);
        return this;
    }

    @Override
    public LambdaInMemoryQuery<T> where(Condition e) {
        // todo support multiple roots
        Map.Entry<Alias, Stream> rootEntry = rootStreams.entrySet().iterator().next();
        Alias alias = rootEntry.getKey();
        Stream<T> data = rootEntry.getValue();
        result = (List<T>)data.filter((entity) -> {
            alias.val = entity;
            try {
                return e.call();
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        }).collect(Collectors.toList());
        rootEntry.setValue(result.stream()); // TODO make filtering lazy
        return this;
    }

    @Override
    public List<T> getResultList() {
        return result;
    }

    @Override
    public LambdaInMemoryQuery<T> from(Alias<?> root) {
        super.from(root);
        return this;
    }

    @Override
    public LambdaInMemoryQuery<T> select(Alias<?> a) {
        super.select(a);
        return this;
    }

    
}
