package ondrom.experiments.jpa;

import org.junit.Test;

/**
 *
 * @author ondro
 */
public class JPASimpleTest extends BaseJPATest {

    public JPASimpleTest() {
    }

    @Test
    public void JPAIsWorking() {
        getEM().getMetamodel();
    }

}
