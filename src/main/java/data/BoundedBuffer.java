package data;

import extensions.Print;
import io.reactivex.Flowable;
import io.reactivex.processors.BehaviorProcessor;

public class BoundedBuffer {

    private int producerPivot = 0;
    private int consumerPivot = 0;

    private final EqualityAndFlag[] bufferArray;

    private final BehaviorProcessor<EqualityAndFlag[]> _buffers = BehaviorProcessor.create();
    private final Flowable<EqualityAndFlag[]> buffers = _buffers;

    public BoundedBuffer(int bufferSize) {
        bufferArray = new EqualityAndFlag[bufferSize];
        _buffers.offer(bufferArray);
    }

    public void writeEquality(Equality equality) {
        if (producerPivot >= bufferArray.length) producerPivot = 0;
        bufferArray[producerPivot++] = new EqualityAndFlag(equality, EqualityState.WRITE);
        Print.println("[WRITE]" + Thread.currentThread().getName());
        emitNewEquality();
    }

    public String readEquality() {
        int prevPivot;
        if (consumerPivot == 0) {
            prevPivot = bufferArray.length - 1;
        } else {
            prevPivot = consumerPivot - 1;
        }
        if (bufferArray[prevPivot] != null) {
            bufferArray[prevPivot].setState(EqualityState.NONE);
        }
        Print.println("[READ] " + Thread.currentThread().getName());
        emitNewEquality();
        if (consumerPivot >= bufferArray.length) consumerPivot = 0;
        bufferArray[consumerPivot].setState(EqualityState.READ);
        emitNewEquality();
        bufferArray[consumerPivot].calculate();
        return bufferArray[consumerPivot++].toString();
    }

    public boolean canReadEquality() throws InterruptedException {
        if (consumerPivot >= bufferArray.length) {
            consumerPivot = 0;
        }
        if (producerPivot >= bufferArray.length) {
            producerPivot = 0;
        }
        boolean isReady = (bufferArray[consumerPivot] != null && !bufferArray[consumerPivot].isCalculated());
        if (isReady) {
            bufferArray[consumerPivot].setState(EqualityState.RUNNING);
            emitNewEquality();
            Thread.sleep(10);
        }
        return consumerPivot < producerPivot || isReady;
    }

    public Flowable<EqualityAndFlag[]> getBuffers() {
        return buffers;
    }

    private void emitNewEquality() {
        _buffers.offer(bufferArray);
    }
}
