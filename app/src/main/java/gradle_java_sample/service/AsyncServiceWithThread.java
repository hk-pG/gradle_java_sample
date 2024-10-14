package gradle_java_sample.service;

public class AsyncServiceWithThread extends Thread {

    final Integer countForDown;

    public AsyncServiceWithThread(final Integer maxCount) {
        this.countForDown = maxCount;
    }

    @Override
    public void run() {
        try {
            for (int i = 0; i < countForDown; i++) {
                sleep(1000);
                System.out.println("count : " + i);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
