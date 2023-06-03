package src.code;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class LevelChosenPanel extends JPanel {
    private JLabel label1;
    private JLabel label2;
    private JLabel level1;
    private JLabel level2;
    private JLabel level3;
    private JLabel level4;
    private JButton button_end;
    private JLabel num1;
    private JLabel num2;
    private JLabel num3;
    private JLabel num4;
    private JLabel frame1;
    private JLabel frame2;
    private JLabel frame3;
    private JLabel frame4;
    private String filePath;
    LevelChosenPanel(String filePath){
        this.filePath = filePath;
        setBackground(new Color(90,150,250));
        setBounds(0,0,800,640);
        setLayout(null); // 去掉默认的布局管理器

        ImageIcon icon1 =new ImageIcon("src/image/train1.png");
        label1 = new JLabel(icon1);
        label1.setBounds(40,20,300,300);
        this.add(label1);

        ImageIcon icon2 =new ImageIcon("src/image/train2.png");
        label2 = new JLabel(icon2);
        label2.setBounds(440,200,300,300);
        this.add(label2);

        button_end = new JButton("退出游戏");
        button_end.setBounds(600,525,90,30);
        button_end.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.exit(0);//退出程序
            }
        });
        this.add(button_end);

        ImageIcon icon_num1 = new ImageIcon("src/image/num1.png");
        num1 = new JLabel(icon_num1);
        num1.setBounds(423,200,55,65);
        this.add(num1);

        ImageIcon icon_num2 = new ImageIcon("src/image/num2.png");
        num2 = new JLabel(icon_num2);
        num2.setBounds(638,200,55,65);
        this.add(num2);

        ImageIcon icon_num3 = new ImageIcon("src/image/num3.png");
        num3 = new JLabel(icon_num3);
        num3.setBounds(95,477,55,65);
        this.add(num3);

        ImageIcon icon_num4 = new ImageIcon("src/image/num4.png");
        num4 = new JLabel(icon_num4);
        num4.setBounds(310,477,55,65);
        this.add(num4);

        ImageIcon icon_frame = new ImageIcon("src/image/Frame.png");
        frame1 = new JLabel(icon_frame);
        frame1.setBounds(360,70,180,180);
        frame1.setVisible(false);
        this.add(frame1);

        frame2 = new JLabel(icon_frame);
        frame2.setBounds(573,70,180,180);
        frame2.setVisible(false);
        this.add(frame2);

        frame3 = new JLabel(icon_frame);
        frame3.setBounds(30,345,180,180);
        frame3.setVisible(false);
        this.add(frame3);

        frame4 = new JLabel(icon_frame);
        frame4.setBounds(243,345,180,180);
        frame4.setVisible(false);
        this.add(frame4);

        ImageIcon tmp =new ImageIcon("src/image/goldCoin.png");
        tmp = new ImageIcon(tmp.getImage().getScaledInstance(70,60, Image.SCALE_SMOOTH));

        final boolean level1Arrived = FileOperation.searchFile(this.filePath+"/level1","level1.mapinfo");
        // 到达才能进入
        level1 = level1Arrived?new JLabel(tmp):new JLabel();
        level1.setBounds(360,75,180,170);
        level1.addMouseListener(
                new MouseListener() {
                    public void mouseClicked(MouseEvent arg0) {
                        if(level1Arrived)
                        {
                            SwingUtilities.getWindowAncestor(level1).setVisible(false);
                            //进入关卡1
                            new Game(filePath,1);
                        }
                    }
                    public void mouseEntered(MouseEvent arg0) {
                        frame1.setVisible(true);
                    }
                    public void mouseExited(MouseEvent arg0) {
                        frame1.setVisible(false);
                    }
                    public void mousePressed(MouseEvent arg0) {}
                    public void mouseReleased(MouseEvent arg0) {}
                }
        );
        this.add(level1);

        final boolean level2Arrived = FileOperation.searchFile(this.filePath+"/level2","level2.mapinfo");
        // 到达才能进入
        level2 = level2Arrived?new JLabel(tmp):new JLabel();
        level2.setBounds(573,75,180,170);
        level2.addMouseListener(
                new MouseListener() {
                    public void mouseClicked(MouseEvent arg0) {
                        if(level2Arrived)
                        {
                            SwingUtilities.getWindowAncestor(level2).setVisible(false);
                            //进入关卡2
                            new Game(filePath, 2);
                        }
                    }
                    public void mouseEntered(MouseEvent arg0) {
                        frame2.setVisible(true);
                    }
                    public void mouseExited(MouseEvent arg0) {
                        frame2.setVisible(false);
                    }
                    public void mousePressed(MouseEvent arg0) {}
                    public void mouseReleased(MouseEvent arg0) {}
                }
        );
        this.add(level2);

        final boolean level3Arrived = FileOperation.searchFile(this.filePath+"/level3","level3.mapinfo");
        // 到达才能进入
        level3 = level3Arrived?new JLabel(tmp):new JLabel();
        level3.setBounds(30,350,180,170);
        level3.addMouseListener(
                new MouseListener() {
                    public void mouseClicked(MouseEvent arg0) {
                        if(level3Arrived){
                            SwingUtilities.getWindowAncestor(level3).setVisible(false);
                            //进入关卡3
                            new Game(filePath, 3);
                        }
                    }
                    public void mouseEntered(MouseEvent arg0) {
                        frame3.setVisible(true);
                    }
                    public void mouseExited(MouseEvent arg0) {
                        frame3.setVisible(false);
                    }
                    public void mousePressed(MouseEvent arg0) {}
                    public void mouseReleased(MouseEvent arg0) {}
                }
        );

        this.add(level3);

        final boolean level4Arrived = FileOperation.searchFile(this.filePath+"/level4","level4.mapinfo");
        // 到达才能进入
        level4 = level4Arrived?new JLabel(tmp):new JLabel();
        level4.setBounds(243,350,180,170);
        level4.addMouseListener(
                new MouseListener() {
                    public void mouseClicked(MouseEvent arg0) {
                        if(level4Arrived){
                            SwingUtilities.getWindowAncestor(level4).setVisible(false);
                            //进入关卡4
                            new Game(filePath, 4);
                        }
                    }
                    public void mouseEntered(MouseEvent arg0) {
                        frame4.setVisible(true);
                    }
                    public void mouseExited(MouseEvent arg0) {
                        frame4.setVisible(false);
                    }
                    public void mousePressed(MouseEvent arg0) {}
                    public void mouseReleased(MouseEvent arg0) {}
                }
        );
        this.add(level4);
    }
}
