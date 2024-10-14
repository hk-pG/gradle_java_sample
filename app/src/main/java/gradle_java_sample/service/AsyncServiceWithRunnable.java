package gradle_java_sample.service;

public class AsyncServiceWithRunnable implements Runnable {

    final Integer maxCount;

    public AsyncServiceWithRunnable(final Integer maxCount) {
        this.maxCount = maxCount;
    }

    @Override
    public void run() {
        try {
            for (int i = 0; i < maxCount; i++) {
                Thread.sleep(1000);
                System.out.println("count : " + i);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
