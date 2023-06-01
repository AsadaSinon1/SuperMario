package src.code;

import javax.swing.*;
import java.awt.*;

/**
 * 此类是与LevelChosenPanel衔接的类，即点击对应关卡后生成的类
 */
public class Game{

    private Map currMap;
    Game(int mapId){
        currMap = new Map(mapId);
        SwingUtilities.invokeLater(new Backstage(currMap));
    }
    public static void main(String[] args) {
        new Game(1);
    }
}
