package ui;


import io.reactivex.Flowable;
import io.reactivex.processors.BehaviorProcessor;

import javax.swing.*;

public class MainViewModel {

    private final BehaviorProcessor<DefaultListModel<String>> _producerListData = BehaviorProcessor.createDefault(new DefaultListModel<>());
    private final Flowable<DefaultListModel<String>> producerListData = _producerListData;

    private final BehaviorProcessor<Integer> _boundedBufferSize = BehaviorProcessor.createDefault(0);
    private final Flowable<Integer> boundedBufferSize = _boundedBufferSize;

    private final BehaviorProcessor<Integer> _equalitySize = BehaviorProcessor.createDefault(0);
    private final Flowable<Integer> equalitySize = _equalitySize;

    public Flowable<DefaultListModel<String>> getProducerListData() {
        return producerListData;
    }

    public Flowable<Integer> getBoundedBufferSize() {
        return boundedBufferSize;
    }

    public void setBoundedBufferSize(int size) {
        _boundedBufferSize.offer(size);
    }

    public Flowable<Integer> getEqualitySize() {
        return equalitySize;
    }

    public void setEqualitySize(int size) {
        _equalitySize.offer(size);
    }

}
