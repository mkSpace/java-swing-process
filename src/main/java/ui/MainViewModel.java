package ui;


import io.reactivex.Flowable;
import io.reactivex.processors.BehaviorProcessor;

import javax.swing.*;

public class MainViewModel {

    private final BehaviorProcessor<DefaultListModel<String>> _producerListData = BehaviorProcessor.createDefault(new DefaultListModel<>());
    private final Flowable<DefaultListModel<String>> producerListData = _producerListData;

}
