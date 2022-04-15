package data;

import extensions.Print;

import java.util.function.Function;

public class Consumer extends Thread {

    private final int equalitySize;
    private final BoundedBuffer boundedBuffer;
    private final Function<String, Void> doOnRead;
    private final Function<Void, Void> doOnDispose;

    public Consumer(int equalitySize, BoundedBuffer boundedBuffer, Function<String, Void> doOnRead, Function<Void, Void> doOnDispose) {
        this.equalitySize = equalitySize;
        this.boundedBuffer = boundedBuffer;
        this.doOnRead = doOnRead;
        this.doOnDispose = doOnDispose;
    }

    @Override
    public void run() {
        for (int i = 0; i < equalitySize; i++) {
            try {
                if (boundedBuffer.canReadEquality()) {
                    doOnRead.apply(boundedBuffer.readEquality());
                } else {
                    i--;
                    sleep(50);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
                break;
            }
        }
        doOnDispose.apply(null);
    }
}
