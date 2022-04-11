package ui;

import extensions.JLabelUtils;

import javax.swing.*;
import java.awt.*;

public class MyLabel extends JLabel {
    private static final String DEFAULT_TEXT = "Default";
    private static final int BASE_FONT_SIZE = 14;

    public MyLabel(String text, int width, int height, int fontSize) {
        super(text);
        setMinimumSize(new Dimension(width, height));
        setPreferredSize(new Dimension(width, height));
        setMaximumSize(new Dimension(width, height));
        setSize(width, height);
        JLabelUtils.setAlignmentCenter(this);
        setFont(new Font("sans-serif", Font.BOLD, fontSize <= 0 ? BASE_FONT_SIZE : fontSize));
    }

    public static class Builder {

        private int width;
        private int height;
        private String text = DEFAULT_TEXT;
        private int fontSize;

        public Builder(String title) {
            this(title, 0, 0);
        }

        public Builder(String title, int width, int height) {
            this.text = title;
            this.width = width;
            this.height = height;
        }

        public Builder width(int width) {
            this.width = width;
            return this;
        }

        public Builder height(int height) {
            this.height = height;
            return this;
        }

        public Builder text(String text) {
            this.text = text;
            return this;
        }

        public Builder fontSize(int fontSize) {
            this.fontSize = fontSize;
            return this;
        }

        public JLabel build() {
            return new MyLabel(text, width, height, fontSize);
        }
    }
}
