package ui;

import extensions.Print;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;

public class BoundedBufferListCellRenderer extends DefaultListCellRenderer {

    @Override
    public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        Component component = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
        if (component instanceof JLabel) {
            String cellValue = (String) value;
            if (cellValue.startsWith("(")) {
                component.setBackground(Color.WHITE);
                ((JLabel) component).setText(cellValue);
            } else {
                String realValue = cellValue;
                switch (Arrays.stream(cellValue.split(" ")).findFirst().orElse("NONE")) {
                    case "RUNNING":
                        component.setBackground(Color.BLUE);
                        realValue = realValue.replace("RUNNING", "");
                        break;
                    case "WRITE":
                        component.setBackground(Color.GREEN);
                        realValue = realValue.replace("WRITE", "");
                        break;
                    case "READ":
                        component.setBackground(Color.RED);
                        realValue = realValue.replace("READ", "");
                        break;
                    case "NONE":
                        component.setBackground(Color.WHITE);
                        realValue = realValue.replace("NONE", "");
                        break;
                }
                ((JLabel) component).setText(realValue);
            }
        }
        return component;
    }
}
