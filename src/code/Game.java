package src.code;

import javax.swing.*;
import java.awt.*;

/**
 * 此类是与LevelChosenPanel衔接的类，即点击对应关卡后生成的类
 */
public class Game extends JFrame{

    private Map currMap;
    private String user;
    Game(String user, int mapId) {
        this.user = user;
        currMap = new Map(mapId, this);
        SwingUtilities.invokeLater(new Backstage(currMap));
    }
    public void nextGame() {
        // TODO:save操作
        // 加载地图
        FileOperation.copyFile("src/map/level"+(currMap.mapId+1),this.user+"/level"+(currMap.mapId+1));
        // 进入选关界面
        LevelChosenPanel nextGame = new LevelChosenPanel(user);
        setContentPane(nextGame);
        setPreferredSize(new Dimension(800,640));
        pack();
        setLocation(368,112);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);
        setVisible(true);
    }
    public static void main(String[] args) {
        new Game("",1);
    }
}
