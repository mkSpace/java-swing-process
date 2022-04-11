package ui;

import javax.swing.*;
import java.awt.*;

import static java.awt.FlowLayout.CENTER;

public class MainView implements View {

    private final int MAIN_FRAME_WIDTH = 1280;
    private final int MAIN_FRAME_HEIGHT = 720;

    private final JFrame mainFrame;
    private final JPanel topPanel;
    private final JPanel middlePanel;
    private final JPanel bottomPanel;

    public MainView() {
        mainFrame = new JFrame("App");
        topPanel = new JPanel();
        middlePanel = new JPanel();
        bottomPanel = new JPanel();
    }

    @Override
    public void setupViews() {
//        mainFrame.setContentPane(mainPanel);
        setupTopPanel();
        setupMiddlePanel();
        setupBottomPanel();
        setupMainFrame();
    }

    @Override
    public void bindViewModels() {

    }

    private void setupMainFrame() {
        mainFrame.setTitle("2017112622_박재민_운영체제_과제_1");
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setSize(MAIN_FRAME_WIDTH, MAIN_FRAME_HEIGHT);
        mainFrame.setVisible(true);
        mainFrame.setLayout(new FlowLayout(CENTER, 16, 16));
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
        topPanel.add(producerLabel);
        topPanel.add(boundBufferLabel);
        topPanel.add(consumerLabel);
        topPanel.setBackground(Color.gray);
    }

    private void setupMiddlePanel() {
        middlePanel.setBackground(Color.gray);
        middlePanel.setSize(MAIN_FRAME_WIDTH, MAIN_FRAME_HEIGHT / 10 * 8);
    }

    private void setupBottomPanel() {
        bottomPanel.setBackground(Color.gray);
    }
}
