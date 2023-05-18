package src.code;

import javax.swing.*;

public class TestMapBackground implements Background {
    ImageIcon icon;
    TestMapBackground(String filename){
        icon = new ImageIcon(filename);
    }

    public JLabel getBackground() {
        return new JLabel(icon);
    }
}
