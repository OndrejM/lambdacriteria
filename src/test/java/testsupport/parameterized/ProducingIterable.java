package testsupport.parameterized;

import java.util.*;
import java.util.function.Supplier;

public class ProducingIterable<PRODUCED_TYPE> implements Iterable<PRODUCED_TYPE> {

    private final Collection<Supplier<PRODUCED_TYPE>> suppliers;

    public ProducingIterable(Collection<Supplier<PRODUCED_TYPE>> suppliers) {
        this.suppliers = suppliers;
    }

    public static <EXPECTED_TYPE> ProducingIterable<EXPECTED_TYPE>
            from(Supplier<EXPECTED_TYPE>... suppliers) {
        return new ProducingIterable<>(Arrays.asList(suppliers));
    }

    @Override
    public Iterator<PRODUCED_TYPE> iterator() {
        final Iterator<Supplier<PRODUCED_TYPE>> itSuppliers = suppliers.iterator();
        return new Iterator<PRODUCED_TYPE>() {
            @Override
            public boolean hasNext() {
                return itSuppliers.hasNext();
            }

            @Override
            public PRODUCED_TYPE next() {
                return itSuppliers.next().get();
            }
        };
    }

}
