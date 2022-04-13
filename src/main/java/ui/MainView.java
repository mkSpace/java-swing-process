package ui;

import di.Injection;
import extensions.Print;
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
                viewModel.getBoundedBufferSize()
                        .subscribe(integer -> Print.println("[bindViewModels] boundedBufferSize: " + integer))
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
        DefaultListModel<String> sample = new DefaultListModel<>();
        sample.addElement("Jaemin");
        sample.addElement("Jaemin2");
        sample.addElement("Jaemin3");
        JList<DefaultListModel<String>> list1 = new JList(sample);
        JScrollPane scrollPane = new JScrollPane();
        Font font = new Font("sans-serif", Font.PLAIN, 16);
        list1.setFont(font);
        JList list2 = new JList(sample);
        list2.setFont(font);
        JScrollPane scrollPane2 = new JScrollPane();
        JList list3 = new JList(sample);
        list3.setFont(font);
        JScrollPane scrollPane3 = new JScrollPane();
        Dimension listDimension = new Dimension(MAIN_FRAME_WIDTH / 3, middlePanelHeight);
        list1.setPreferredSize(listDimension);
        list2.setPreferredSize(listDimension);
        list3.setPreferredSize(listDimension);
        list1.setFocusable(false);
        list1.setDragEnabled(false);
        scrollPane.getViewport().add(list1);
        scrollPane2.getViewport().add(list2);
        scrollPane3.getViewport().add(list3);
        scrollPane.setPreferredSize(listDimension);
        scrollPane2.setPreferredSize(listDimension);
        scrollPane3.setPreferredSize(listDimension);
        middlePanel.add(scrollPane);
        middlePanel.add(scrollPane2);
        middlePanel.add(scrollPane3);
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
        settingButton.addActionListener(e -> dialog.setVisible(true));
    }
}
