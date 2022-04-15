package ui;


import data.BoundedBuffer;
import data.Consumer;
import data.Pair;
import data.Producer;
import io.reactivex.Flowable;
import io.reactivex.processors.BehaviorProcessor;
import io.reactivex.processors.PublishProcessor;

import javax.swing.*;
import java.util.Arrays;
import java.util.Objects;
import java.util.concurrent.Flow;
import java.util.concurrent.atomic.AtomicInteger;

public class MainViewModel {

    private final BehaviorProcessor<DefaultListModel<String>> _producerListData = BehaviorProcessor.create();
    private final Flowable<DefaultListModel<String>> producerListData = _producerListData;

    private final BehaviorProcessor<DefaultListModel<String>> _consumerListData = BehaviorProcessor.createDefault(new DefaultListModel<>());
    private final Flowable<DefaultListModel<String>> consumerListData = _consumerListData;

    private final BehaviorProcessor<DefaultListModel<String>> _boundedBufferListData = BehaviorProcessor.createDefault(new DefaultListModel<>());
    private final Flowable<DefaultListModel<String>> boundedBufferListData = _boundedBufferListData;

    private final BehaviorProcessor<Integer> _boundedBufferSize = BehaviorProcessor.create();
    private final Flowable<Integer> boundedBufferSize = _boundedBufferSize;

    private final BehaviorProcessor<Integer> _equalitySize = BehaviorProcessor.create();
    private final Flowable<Integer> equalitySize = _equalitySize;

    private final BehaviorProcessor<Producer> producer = BehaviorProcessor.create();
    private final BehaviorProcessor<Consumer> consumer = BehaviorProcessor.create();
    private final BehaviorProcessor<BoundedBuffer> boundedBuffer = BehaviorProcessor.create();

    private final PublishProcessor<String> alertMessageProcessor = PublishProcessor.create();
    private final Flowable<String> alertMessage = alertMessageProcessor;

    private final BehaviorProcessor<Boolean> isProcessing = BehaviorProcessor.createDefault(false);

    public DefaultListModel<String> _sample = new DefaultListModel<>();

    private int producerPivot = 0;
    private int consumerPivot = 0;

    public MainViewModel() {
        _boundedBufferSize.zipWith(_equalitySize, Pair::new)
                .subscribe(boundedBufferWithEqualitySize -> {
                    int boundedBufferSize = boundedBufferWithEqualitySize.first;
                    int equalitySize = boundedBufferWithEqualitySize.second;
                    DefaultListModel<String> initializedBufferModel = new DefaultListModel<>();
                    for (int i = 1; i <= boundedBufferSize; i++) {
                        initializedBufferModel.addElement("(" + i + ")");
                    }
                    DefaultListModel<String> initializedEqualityModel = new DefaultListModel<>();
                    for (int i = 1; i <= equalitySize; i++) {
                        initializedEqualityModel.addElement("(" + i + ")");
                    }
                    DefaultListModel<String> initializedEqualityModel2 = new DefaultListModel<>();
                    for (int i = 1; i <= equalitySize; i++) {
                        initializedEqualityModel2.addElement("(" + i + ")");
                        _sample.addElement("(" + i + ")");
                    }
                    producerPivot = 0;
                    consumerPivot = 0;
                    _producerListData.offer(initializedEqualityModel);
                    _consumerListData.offer(initializedEqualityModel2);
                    _boundedBufferListData.offer(initializedBufferModel);
                });
        producer.zipWith(consumer, Pair::new)
                .subscribe(producerAndConsumer -> {
                    Producer producer = producerAndConsumer.first;
                    Consumer consumer = producerAndConsumer.second;
                    producer.start();
                    consumer.start();
                });
    }

    public void init() throws InterruptedException {
        Objects.requireNonNull(producer.getValue()).interrupt();
        Objects.requireNonNull(consumer.getValue()).interrupt();
    }

    public void start() {
        if (isProcessing.getValue() != null && isProcessing.getValue()) {
            alertMessageProcessor.offer("이미 실행중입니다. 멈추고 다시 시도해주세요.");
            return;
        }
        isProcessing.offer(true);
        int bufferSize = Objects.requireNonNull(_boundedBufferSize.getValue());
        int equalitySize = Objects.requireNonNull(_equalitySize.getValue());
        producerPivot = 0;
        consumerPivot = 0;
        BoundedBuffer buffer = new BoundedBuffer(bufferSize);
        boundedBuffer.offer(buffer);
        buffer.getBuffers()
                .subscribe(buffers -> {
                    DefaultListModel<String> newBoundedBufferListModel = new DefaultListModel<>();
                    DefaultListModel<String> prev = _boundedBufferListData.getValue();
                    if (prev == null) return;
                    AtomicInteger currentBufferSize = new AtomicInteger();
                    Arrays.stream(buffers).forEach(equalityAndFlag -> {
                                if (equalityAndFlag == null) return;
                                newBoundedBufferListModel.addElement(equalityAndFlag.getState() + " " + equalityAndFlag.getOnlyEqualityString());
                                currentBufferSize.getAndIncrement();
                            }
                    );
                    for (int i = currentBufferSize.get(); i < bufferSize; i++) {
                        newBoundedBufferListModel.addElement(prev.get(i));
                    }
                    _boundedBufferListData.offer(newBoundedBufferListModel);
                });
        producer.offer(new Producer(equalitySize, buffer, equality -> {
            DefaultListModel<String> prev = _producerListData.getValue();
            if (prev == null) return null;
            prev.set(producerPivot++, equality);
            _producerListData.offer(prev);
            return null;
        }));
        consumer.offer(new Consumer(equalitySize, buffer, equality -> {
            DefaultListModel<String> prev = _consumerListData.getValue();
            if (prev == null) return null;
            prev.set(consumerPivot++, equality);
            _consumerListData.offer(prev);
            return null;
        }, it -> {
            isProcessing.offer(false);
            alertMessageProcessor.offer("실행이 모두 종료되었습니다.");
            return null;
        }));
    }

    public Flowable<Pair<DefaultListModel<String>, Integer>> getProducerListData() {
        return producerListData.map(list -> new Pair(list, producerPivot));
    }

    public Flowable<Pair<DefaultListModel<String>, Integer>> getConsumerListData() {
        return consumerListData.map(list -> new Pair(list, consumerPivot));
    }

    public Flowable<DefaultListModel<String>> getBoundedBufferListData() {
        return boundedBufferListData;
    }

    public void setBoundedBufferSize(int size) {
        if (isProcessing.getValue() != null && isProcessing.getValue()) {
            alertMessageProcessor.offer("이미 실행중입니다. 멈추고 다시 시도해주세요.");
            return;
        }
        _boundedBufferSize.offer(size);
    }

    public void setEqualitySize(int size) {
        _equalitySize.offer(size);
    }

    public Flowable<String> getAlertMessage() {
        return alertMessage;
    }
}
