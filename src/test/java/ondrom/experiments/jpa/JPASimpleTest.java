package ondrom.experiments.jpa;

import org.junit.Test;

/**
 *
 * @author ondro
 */
public class JPASimpleTest extends JPATestBase {

    public JPASimpleTest() {
    }

    @Test
    public void JPAIsWorking() {
        getEM().getMetamodel();
    }

}
