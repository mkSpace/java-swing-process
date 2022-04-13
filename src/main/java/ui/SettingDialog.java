package ui;

import di.Injection;
import extensions.Print;

import javax.swing.*;
import java.awt.*;

public class SettingDialog extends JDialog {

    private static final int DIALOG_WIDTH = 300;
    private static final int DIALOG_HEIGHT = 125;

    private final JPanel boundedBufferPanel = new JPanel(new FlowLayout());
    private final JPanel numberOfEquationPanel = new JPanel(new FlowLayout());
    private final JButton okButton = new JButton("확인");

    private final MainViewModel viewModel = Injection.provideMainViewModel();

    public SettingDialog(JFrame owner, String title) {
        super(owner, title);
        setLayout(new GridLayout(3, 1));
        setupViews();
        setSize(new Dimension(DIALOG_WIDTH, DIALOG_HEIGHT));
        setLocationRelativeTo(null);
        Print.println("[SettingDialog] MainViewModel : " + viewModel.hashCode());
    }

    private void setupViews() {
        JLabel boundedBufferInputText = new JLabel("Bounded Buffer 크기 : ");
        JTextField boundedBufferTextField = new JTextField(5);
        boundedBufferPanel.add(boundedBufferInputText);
        boundedBufferPanel.add(boundedBufferTextField);

        JLabel numberOfEquationInputText = new JLabel("Equation 발생 횟수 : ");
        JTextField numberOfEquationTextField = new JTextField(5);
        numberOfEquationPanel.add(numberOfEquationInputText);
        numberOfEquationPanel.add(numberOfEquationTextField);

        okButton.addActionListener(e -> {
            int boundedBufferSize = 0;
            int equalitySize = 0;
            try {
                boundedBufferSize = Integer.parseInt(boundedBufferTextField.getText());
                equalitySize = Integer.parseInt(numberOfEquationTextField.getText());
            } catch (NumberFormatException error) {
                error.printStackTrace();
            }
            viewModel.setBoundedBufferSize(boundedBufferSize);
            viewModel.setEqualitySize(equalitySize);
            setVisible(false);
        });

        add(boundedBufferPanel);
        add(numberOfEquationPanel);
        add(okButton);
    }
}
