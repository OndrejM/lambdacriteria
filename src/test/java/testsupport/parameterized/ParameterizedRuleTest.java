package testsupport.parameterized;

import java.util.*;
import static org.hamcrest.Matchers.*;
import org.junit.*;
import static org.junit.Assert.*;
import org.junit.rules.ExpectedException;

public class ParameterizedRuleTest {

    private static int testCounter = 0;

    private static final List<String> STRING_PARAMS = Arrays.asList("1", "2");
    private static final List<String> OTHER_STRING_PARAMS = Arrays.asList("s1", "s2", "s3");

    @Rule
    public ParameterRule<String> strings = new ParameterRule<>(STRING_PARAMS);

    @Rule
    public ParameterRule<String> otherStrings = new ParameterRule<>(OTHER_STRING_PARAMS);

    @Rule
    public ExpectedException expected = ExpectedException.none();

    @Before
    public void countTest() {
        testCounter++;
    }

    @Test
    public void canRunTestWithParameters() {
        String s = strings.getParameter();
        expected.expect(AssertionError.class);

        fail("Failing for " + s);
    }

    @Test
    public void canRunTestWithOtherParameters() {
        String s = otherStrings.getParameter();
        expected.expect(AssertionError.class);

        fail("Failing for " + s);
    }

    @Test
    public void canRunTestWithCombinationOfParameters() {
        String s = strings.getParameter();
        String otherS = otherStrings.getParameter();
        expected.expect(AssertionError.class);

        fail("Failing for s=" + s + ", other=" + otherS);
    }

    @Test
    public void canRunTestWithoutParameters() {
        expected.expect(AssertionError.class);

        fail("Failing some test");
    }

    @AfterClass
    public static void canRunAllTestsAsSupposed() {
        assertThat(testCounter, is(equalTo(
                STRING_PARAMS.size()
                + OTHER_STRING_PARAMS.size()
                + (STRING_PARAMS.size() * OTHER_STRING_PARAMS.size())
                + 1)));
    }

}
