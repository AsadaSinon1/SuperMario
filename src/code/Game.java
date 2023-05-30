package src.code;

import javax.swing.*;
import java.awt.*;

public class Game extends JFrame {

    private Map currMap;
    Game(){
    }
    public static void main(String[] args) {
        SwingUtilities.invokeLater(Backstage::new);
    }
}
