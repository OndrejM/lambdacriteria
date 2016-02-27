package testsupport.parameterized;

import java.util.Arrays;
import org.junit.*;
import static org.junit.Assert.fail;
import org.junit.rules.ExpectedException;

public class ParameterizedRuleTest {
    
    @Rule
    public ParameterRule<String> strings = new ParameterRule<>(Arrays.asList("1", "2"));
    
    @Rule
    public ExpectedException expected = ExpectedException.none();
    
    @Test
    public void canRunTestWithParameters() {
        String s = strings.getParameter();
        expected.expect(AssertionError.class);
        
        fail("Failing for " + s);
    }

}
