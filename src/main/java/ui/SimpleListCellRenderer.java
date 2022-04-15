package ui;

import javax.swing.*;
import java.awt.*;

public class SimpleListCellRenderer extends DefaultListCellRenderer {
    @Override
    public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        Component component = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
        if (component instanceof JLabel && value instanceof String) {
            String _value = (String) value;
            if(_value.startsWith("(")) {
                component.setBackground(Color.WHITE);
            } else {
                component.setBackground(Color.GREEN);
            }
            ((JLabel) component).setText(_value);
        }
        return component;
    }
}
