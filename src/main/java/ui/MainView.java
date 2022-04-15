package ui;

import di.Injection;
import io.reactivex.disposables.CompositeDisposable;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import static java.awt.FlowLayout.CENTER;

public class MainView implements View {

    private static final int MAIN_FRAME_WIDTH = 1280;
    private static final int MAIN_FRAME_HEIGHT = 720;
    private static final int WIDGET_MARGIN = 12;

    private final MainViewModel viewModel = Injection.provideMainViewModel();

    private final CompositeDisposable disposables = new CompositeDisposable();

    private final JFrame mainFrame;
    private final JPanel topPanel;
    private final JPanel middlePanel;
    private final JPanel bottomPanel;

    private final JButton startButton = new JButton("Start");
    private final JButton initializationButton = new JButton("Initialization");
    private final JButton settingButton = new JButton("Setting");

    private final JDialog dialog;

    private JList<String> producerList;
    private DefaultListModel<String> producerCache = new DefaultListModel<>();
    private DefaultListModel<String> consumerCache = new DefaultListModel<>();
    private JList<String> consumerList;
    private JList<String> boundedBufferList;


    public MainView() {
        mainFrame = new JFrame("App");
        topPanel = new JPanel();
        middlePanel = new JPanel();
        bottomPanel = new JPanel();
        dialog = new SettingDialog(mainFrame, "Setting");
        mainFrame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                super.windowClosed(e);
                disposables.clear();
                disposables.dispose();
            }
        });
    }

    @Override
    public void setupViews() {
        setupButtons();
        setupTopPanel();
        setupMiddlePanel();
        setupBottomPanel();
        setupMainFrame();
        bindViewModels();
    }

    @Override
    public void bindViewModels() {
        disposables.add(
                viewModel.getProducerListData()
                        .subscribe(listWithPivot -> {
                            DefaultListModel<String> list = listWithPivot.first;
                            int pivot = listWithPivot.second;
                            if (producerCache.isEmpty() || pivot == 0) {
                                producerCache = list;
                                producerList.setModel(producerCache);
                            } else if (!list.isEmpty() && pivot < list.size()) {
                                producerCache.set(pivot, list.get(pivot));
                            }
                        })
        );

        disposables.add(
                viewModel.getConsumerListData()
                        .subscribe(listWithPivot -> {
                            DefaultListModel<String> list = listWithPivot.first;
                            int pivot = listWithPivot.second;
                            if (consumerCache.isEmpty() || pivot == 0) {
                                consumerCache = list;
                                consumerList.setModel(consumerCache);
                            } else if (!list.isEmpty()&& pivot < list.size()) {
                                consumerCache.set(pivot, list.get(pivot));
                            }
                        })
        );

        disposables.add(
                viewModel.getBoundedBufferListData()
                        .subscribe(list -> {
                            if (list != null) boundedBufferList.setModel(list);
                        })
        );

        disposables.add(
                viewModel.getAlertMessage()
                        .subscribe(message -> JOptionPane.showMessageDialog(null, message, "알림", JOptionPane.INFORMATION_MESSAGE))
        );
    }

    private void setupMainFrame() {
        mainFrame.setTitle("2017112622_박재민_운영체제_과제_1");
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setSize(MAIN_FRAME_WIDTH + WIDGET_MARGIN * 3, MAIN_FRAME_HEIGHT + WIDGET_MARGIN * 3);
        mainFrame.setVisible(true);
        mainFrame.setLayout(new FlowLayout(CENTER, 0, 0));
        mainFrame.setLocationRelativeTo(null);
        mainFrame.setBackground(Color.gray);
        mainFrame.add(topPanel);
        mainFrame.add(middlePanel);
        mainFrame.add(bottomPanel);
        mainFrame.revalidate();
    }

    private void setupTopPanel() {
        topPanel.setSize(MAIN_FRAME_WIDTH, MAIN_FRAME_HEIGHT / 10);
        int labelWidth = MAIN_FRAME_WIDTH / 3;
        int labelHeight = topPanel.getHeight();
        JLabel producerLabel = new MyLabel.Builder("Producer")
                .width(labelWidth)
                .height(labelHeight)
                .fontSize(20)
                .build();
        JLabel boundBufferLabel = new MyLabel.Builder("Bounded Buffer")
                .width(labelWidth)
                .height(labelHeight)
                .fontSize(20)
                .build();
        JLabel consumerLabel = new MyLabel.Builder("Consumer")
                .width(labelWidth)
                .height(labelHeight)
                .fontSize(20)
                .build();
        topPanel.setLayout(new GridLayout(1, 3));
        topPanel.add(producerLabel);
        topPanel.add(boundBufferLabel);
        topPanel.add(consumerLabel);
        topPanel.setBackground(Color.gray);
    }

    private void setupMiddlePanel() {
        int middlePanelHeight = MAIN_FRAME_HEIGHT / 10 * 8;
        middlePanel.setBackground(Color.gray);
        middlePanel.setSize(MAIN_FRAME_WIDTH, middlePanelHeight);
        middlePanel.setLayout(new GridLayout(1, 3));
        producerList = new JList<>();
        Font font = new Font("sans-serif", Font.PLAIN, 16);
        producerList.setFont(font);
        consumerList = new JList<>();
        consumerList.setFont(font);
        boundedBufferList = new JList<>();
        boundedBufferList.setFont(font);
        Dimension listDimension = new Dimension(MAIN_FRAME_WIDTH / 3, middlePanelHeight);
        producerList.setFocusable(false);
        producerList.setCellRenderer(new SimpleListCellRenderer());
        consumerList.setCellRenderer(new SimpleListCellRenderer());
        boundedBufferList.setCellRenderer(new BoundedBufferListCellRenderer());
        producerList.setDragEnabled(false);
        consumerList.setDragEnabled(false);
        consumerList.setDragEnabled(false);
        boundedBufferList.setDragEnabled(false);
        boundedBufferList.setDragEnabled(false);
        JScrollPane producerScrollPane = new JScrollPane(producerList);
        JScrollPane bufferScrollPane = new JScrollPane(boundedBufferList);
        JScrollPane consumerScrollPane = new JScrollPane(consumerList);
        producerScrollPane.setPreferredSize(listDimension);
        bufferScrollPane.setPreferredSize(listDimension);
        consumerScrollPane.setPreferredSize(listDimension);
        middlePanel.add(producerScrollPane);
        middlePanel.add(bufferScrollPane);
        middlePanel.add(consumerScrollPane);
    }

    private void setupBottomPanel() {
        bottomPanel.setBackground(Color.gray);
        bottomPanel.setSize(MAIN_FRAME_WIDTH, MAIN_FRAME_HEIGHT / 10);
        bottomPanel.setLayout(new GridLayout(1, 3));
        bottomPanel.add(startButton);
        bottomPanel.add(initializationButton);
        bottomPanel.add(settingButton);
    }

    private void setupButtons() {
        int buttonWidth = MAIN_FRAME_WIDTH / 3;
        int buttonHeight = MAIN_FRAME_HEIGHT / 10;
        Dimension buttonDimension = new Dimension(buttonWidth, buttonHeight);
        startButton.setPreferredSize(buttonDimension);
        initializationButton.setPreferredSize(buttonDimension);
        settingButton.setPreferredSize(buttonDimension);
        startButton.addActionListener(e -> viewModel.start());
        initializationButton.addActionListener(e -> {
            try {
                viewModel.init();
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        });
        settingButton.addActionListener(e -> dialog.setVisible(true));
    }
}
