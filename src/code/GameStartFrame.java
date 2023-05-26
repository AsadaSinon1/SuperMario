package src.code;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GameStartFrame extends JFrame {
    private JPanel panel1;
    private JLabel label0;
    private JLabel label1;
    private JLabel label2;
    private JLabel label3;
    private JLabel star1;
    private JLabel star2;
    private GameEnterPanel panel2;
    private LevelChosenPanel panel3;
    GameStartFrame(){
        setTitle("SuperMario");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        getContentPane().setBackground(new Color(0,0,0));
        setSize(800,640);
        setResizable(false);
        // 获取屏幕的宽度和高度
        int screenWidth = Toolkit.getDefaultToolkit().getScreenSize().width;
        int screenHeight = Toolkit.getDefaultToolkit().getScreenSize().height;

        // 计算窗体的坐标位置
        int x = (screenWidth - getWidth()) / 2;
        int y = (screenHeight - getHeight()) / 2;

        // 将窗体移动到计算出的坐标位置
        setLocation(x, y);
        setLayout(null); // 去掉默认的布局管理器
    }
    public void Loading(){
        panel1 = new JPanel();
        panel1.setBounds(0,0,800,640);
        panel1.setBackground(Color.BLACK);
        getContentPane().add(panel1);
        panel1.setLayout(null);

        ImageIcon icon0 = new ImageIcon("src/image/Logo1.jpg");
        label0 = new JLabel(icon0);
        label0.setBounds(80,50,600,200);
        label0.setVisible(true);
        panel1.add(label0);


        ImageIcon icon1 = new ImageIcon("src/image/marioRunRight2.png");
        label1 = new JLabel(icon1);
        label1.setBounds(170,320,50,100);
        label1.setVisible(false);
        panel1.add(label1);

        ImageIcon icon2 = new ImageIcon("src/image/marioRunRight3.png");
        label2 = new JLabel(icon2);
        label2.setBounds(370,320,50,100);
        label2.setVisible(false);
        panel1.add(label2);

        ImageIcon icon3 = new ImageIcon("src/image/marioRunRight4.png");
        label3 = new JLabel(icon3);
        label3.setBounds(570,320,50,100);
        label3.setVisible(false);
        panel1.add(label3);

        ImageIcon icon4 = new ImageIcon("src/image/star.png");
        star1 = new JLabel(icon4);
        star2 = new JLabel(icon4);
        star1.setBounds(250,320,80,100);
        star2.setBounds(430,320,100,100);
        star1.setVisible(false);
        star2.setVisible(false);
        panel1.add(star1);
        panel1.add(star2);


        label1.setVisible(true);
        try {
            Thread.sleep(800);
        }catch (Exception e){
            e.printStackTrace();
        }

        label1.setVisible(false);
        star1.setVisible(true);
        label2.setVisible(true);

        try {
            Thread.sleep(800);
        }catch (Exception e){
            e.printStackTrace();
        }

        label2.setVisible(false);
        star2.setVisible(true);
        label3.setVisible(true);

        try {
            Thread.sleep(800);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    void showEnterFrame(){
        panel2 = new GameEnterPanel();
        setContentPane(panel2);
        validate();
        panel2.addAction(
                new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        panel3 = new LevelChosenPanel();
                        setContentPane(panel3);
                        validate();
                    }
                });
    }
    public static void main(String[] args){
        GameStartFrame frame =new GameStartFrame();
        frame.setVisible(true);
        frame.Loading();
        frame.showEnterFrame();
    }
}