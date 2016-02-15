package ondrom.experiments.jpa;

public interface BDDTestBase {

    default void given(Runnable whenRunnable) {
        whenRunnable.run();
    }
    
    default void when(Runnable whenRunnable) {
        whenRunnable.run();
    }
    
    default void then(Runnable whenRunnable) {
        whenRunnable.run();
    }
    
}
