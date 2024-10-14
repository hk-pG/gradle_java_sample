package gradle_java_sample.service;

import org.junit.jupiter.api.Test;

public class AsyncServiceWithThreadTest {

    @Test
    public void test_run() {
        final var instance = init(1);

        instance.start();
    }

    private AsyncServiceWithThread init(final Integer maxCount) {
        return new AsyncServiceWithThread(maxCount);
    }
}
