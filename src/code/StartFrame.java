package src.code;

import javax.swing.*;

public class StartFrame extends JFrame {
    StartFrame(){
        setTitle("SuperMario");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        add(new TestMapBackground("src/image/UI参考.jpeg").getBackground());
        setSize(800,640);
        setResizable(false);
    }
    public static void main(String[] args) {
        new StartFrame().setVisible(true);
    }
}
