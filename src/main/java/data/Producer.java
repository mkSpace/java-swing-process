package data;

import java.util.function.Function;

public class Producer extends Thread{

    private final int equalitySize;
    private final BoundedBuffer boundedBuffer;
    private final Function<String, Void> doOnWrite;

    public Producer(int equalitySize, BoundedBuffer boundedBuffer, Function<String, Void> doOnWrite) {
        this.equalitySize = equalitySize;
        this.boundedBuffer = boundedBuffer;
        this.doOnWrite = doOnWrite;
    }

    @Override
    public void run() {
        for(int i = 0; i < equalitySize; i++) {
            Equality equality = Equality.create();
            doOnWrite.apply(equality.toString());
            boundedBuffer.writeEquality(equality);
            try {
                sleep(80);
            } catch (InterruptedException e) {
                e.printStackTrace();
                break;
            }
        }
    }
}
