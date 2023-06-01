package src.code;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.*;

public class LoginFrame extends JFrame {
    private JPanel panel1;
    private JPanel panel2;
    private JPanel panel3;

    LoginFrame(){
        setTitle("Login");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        getContentPane().setBackground(new Color(100, 149, 237));
        setSize(400,500);
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
    class LogPanel1 extends JPanel{
        private JLabel label1;
        private JLabel label2;
        private JLabel label3;
        private JLabel label4;
        private JLabel label5;
        LogPanel1() {
            setBounds(0,0,400,500);
            setBackground(new Color(100, 149, 237));
            setLayout(null);

            ImageIcon icon1 = new ImageIcon("src/image/log.png");
            label1 = new JLabel(icon1);
            label1.setBounds(67,20,250,250);
            this.add(label1);

            ImageIcon icon2 = new ImageIcon("src/image/signIn.png");
            label2 = new JLabel(icon2);
            label2.setBounds(67,270,250,90);
            this.add(label2);

            ImageIcon icon3 = new ImageIcon("src/image/signUp.png");
            label3 = new JLabel(icon3);
            label3.setBounds(67,350,250,90);
            this.add(label3);

            ImageIcon icon4 = new ImageIcon("src/image/shadeBig.png");
            label4 = new JLabel(icon4);
            label4.setBounds(67,273,250,90);
            label4.setVisible(false);
            this.add(label4);

            label5 = new JLabel(icon4);
            label5.setBounds(67,353,250,90);
            label5.setVisible(false);
            this.add(label5);

            label2.addMouseListener(
                    new MouseListener() {
                        public void mouseClicked(MouseEvent arg0) {
                            //进入Sign In 界面
                            showPanel2();
                        }
                        public void mouseEntered(MouseEvent arg0) {
                            label4.setVisible(true);
                        }
                        public void mouseExited(MouseEvent arg0) {
                            label4.setVisible(false);
                        }
                        public void mousePressed(MouseEvent arg0) {}
                        public void mouseReleased(MouseEvent arg0) {}
                    }
            );

            label3.addMouseListener(
                    new MouseListener() {
                        public void mouseClicked(MouseEvent arg0) {
                            //进入Sign Up 界面
                            showPanel3();
                        }
                        public void mouseEntered(MouseEvent arg0) {
                            label5.setVisible(true);
                        }
                        public void mouseExited(MouseEvent arg0) {
                            label5.setVisible(false);
                        }
                        public void mousePressed(MouseEvent arg0) {}
                        public void mouseReleased(MouseEvent arg0) {}
                    }
            );

        }
    }
    //Sign In界面
    class LogPanel2 extends JPanel{
        private JLabel label1;
        private JLabel label2;
        private JLabel label3;
        private  JLabel label4;
        private JLabel label5;
        private JLabel label6;
        private JLabel label7;
        private JTextField textField1;
        private JLabel label8;
        LogPanel2(){
            setBounds(0,0,400,500);
            setBackground(new Color(100, 149, 237));
            setLayout(null);

            ImageIcon icon1 = new ImageIcon("src/image/welcome.png");
            label1 = new JLabel(icon1);
            label1.setBounds(55,30,250,130);
            this.add(label1);

            ImageIcon icon2 = new ImageIcon("src/image/user.png");
            label2 = new JLabel(icon2);
            label2.setBounds(45,170,45,45);
            this.add(label2);

            textField1 = new JTextField();
            textField1.setBounds(110,170,180,40);
            textField1.setBorder(new LineBorder(Color.BLACK));
            Font font = new Font("Arial", Font.BOLD, 16);
            textField1.setFont(font);
            Insets insets = new Insets(0, 10, 0, 10);
            textField1.setMargin(insets);
            this.add(textField1);

            ImageIcon icon3 = new ImageIcon("src/image/password.png");
            label3 = new JLabel(icon3);
            label3.setBounds(45,230,45,45);
            this.add(label3);

            label8 = new JLabel("No need for password.");
            label8.setFont(font);
            label8.setBounds(110,230,180,40);
            this.add(label8);

            ImageIcon icon4 = new ImageIcon("src/image/continue.png");
            label4 = new JLabel(icon4);
            label4.setBounds(67,280,250,80);
            this.add(label4);

            ImageIcon icon5 = new ImageIcon("src/image/shadeSmall.png");
            label5 = new JLabel(icon5);
            label5.setBounds(106,296,170,60);
            label5.setVisible(false);
            this.add(label5);

            ImageIcon icon6 = new ImageIcon("src/image/back.png");
            label6 = new JLabel(icon6);
            label6.setBounds(67,350,250,80);
            this.add(label6);

            label7 = new JLabel(icon5);
            label7.setBounds(106,366,170,60);
            label7.setVisible(false);
            this.add(label7);

            label4.addMouseListener(
                    new MouseListener() {
                        public void mouseClicked(MouseEvent arg0) {
                            //在save中查找用户名命名的文件，看是否存在；存在，进入选关界面；不存在，弹窗
                            //告知用户输入的用户名不正确
                            boolean found = false;
                            String inputText = textField1.getText();
                            found = FileOperation.searchFile(inputText);
                            if(found){
                                dispose();
                                GameStartFrame frame = new GameStartFrame();
                                frame.add(new LevelChosenPanel());
                                frame.setVisible(true);
                            }
                            else{
                                //弹出错误弹窗
                                SwingUtilities.invokeLater(() -> new MessageFrame(1));
                            }
                        }
                        public void mouseEntered(MouseEvent arg0) {
                            label5.setVisible(true);
                        }
                        public void mouseExited(MouseEvent arg0) {
                            label5.setVisible(false);
                        }
                        public void mousePressed(MouseEvent arg0) {}
                        public void mouseReleased(MouseEvent arg0) {}
                    }
            );

            label6.addMouseListener(
                    new MouseListener() {
                        public void mouseClicked(MouseEvent arg0) {
                            //放回上一个界面
                            showPanel1();
                        }
                        public void mouseEntered(MouseEvent arg0) {
                            label7.setVisible(true);
                        }
                        public void mouseExited(MouseEvent arg0) {
                            label7.setVisible(false);
                        }
                        public void mousePressed(MouseEvent arg0) {}
                        public void mouseReleased(MouseEvent arg0) {}
                    }
            );
        }
    }
    //Sign Up界面
    class LogPanel3 extends JPanel{
        private JLabel label1;
        private JLabel label2;
        private JLabel label3;
        private  JLabel label4;
        private JLabel label5;
        private JLabel label6;
        private JLabel label7;
        private JTextField textField1;
        private JLabel label8;
        LogPanel3(){
            setBounds(0,0,400,500);
            setBackground(new Color(100, 149, 237));
            setLayout(null);

            ImageIcon icon1 = new ImageIcon("src/image/welcome.png");
            label1 = new JLabel(icon1);
            label1.setBounds(55,30,250,130);
            this.add(label1);

            ImageIcon icon2 = new ImageIcon("src/image/user.png");
            label2 = new JLabel(icon2);
            label2.setBounds(45,170,45,45);
            this.add(label2);

            textField1 = new JTextField();
            textField1.setBounds(110,170,180,40);
            textField1.setBorder(new LineBorder(Color.BLACK));
            Font font = new Font("Arial", Font.BOLD, 16);
            textField1.setFont(font);
            textField1.setMargin(new Insets(0,10,0,10));
            this.add(textField1);

            ImageIcon icon3 = new ImageIcon("src/image/password.png");
            label3 = new JLabel(icon3);
            label3.setBounds(45,230,45,45);
            this.add(label3);

            label8 = new JLabel("No need for password.");
            label8.setFont(font);
            label8.setBounds(110,230,180,40);
            this.add(label8);

            ImageIcon icon4 = new ImageIcon("src/image/continue.png");
            label4 = new JLabel(icon4);
            label4.setBounds(67,280,250,80);
            this.add(label4);

            ImageIcon icon5 = new ImageIcon("src/image/shadeSmall.png");
            label5 = new JLabel(icon5);
            label5.setBounds(106,296,170,60);
            label5.setVisible(false);
            this.add(label5);

            ImageIcon icon6 = new ImageIcon("src/image/back.png");
            label6 = new JLabel(icon6);
            label6.setBounds(67,350,250,80);
            this.add(label6);

            label7 = new JLabel(icon5);
            label7.setBounds(106,366,170,60);
            label7.setVisible(false);
            this.add(label7);

            label4.addMouseListener(
                    new MouseListener() {
                        public void mouseClicked(MouseEvent arg0) {
                            //在save中查找该以该用户名命名的文件夹是否存在，若存在，提醒用户改用户名已被使用
                            //不存在，创建用户名存入save
                            String inputText = textField1.getText();
                            boolean created = FileOperation.insertFile(inputText);
                            if(created){
                                textField1.setText("");
                                SwingUtilities.invokeLater(new Runnable() {
                                    public void run() {
                                        new MessageFrame(3);
                                    }
                                });
                            }
                            else{
                                SwingUtilities.invokeLater(new Runnable() {
                                    public void run() {
                                        new MessageFrame(2);
                                    }
                                });
                            }
                        }
                        public void mouseEntered(MouseEvent arg0) {
                            label5.setVisible(true);
                        }
                        public void mouseExited(MouseEvent arg0) {
                            label5.setVisible(false);
                        }
                        public void mousePressed(MouseEvent arg0) {}
                        public void mouseReleased(MouseEvent arg0) {}
                    }
            );

            label6.addMouseListener(
                    new MouseListener() {
                        public void mouseClicked(MouseEvent arg0) {
                            //放回上一个界面
                            showPanel1();
                        }
                        public void mouseEntered(MouseEvent arg0) {
                            label7.setVisible(true);
                        }
                        public void mouseExited(MouseEvent arg0) {
                            label7.setVisible(false);
                        }
                        public void mousePressed(MouseEvent arg0) {}
                        public void mouseReleased(MouseEvent arg0) {}
                    }
            );
        }
    }
    public void showPanel1(){
        panel1 = new LogPanel1();
        setContentPane(panel1);
        validate();
    }
    public void showPanel2()
    {
        panel2 = new LogPanel2();
        setContentPane(panel2);
        validate();
    }
    public void showPanel3()
    {
        panel3 = new LogPanel3();
        setContentPane(panel3);
        validate();
    }
}
class FileOperation {
    public static boolean searchFile(String userName) {
        // 要查找的目录和文件名
        boolean found = false;
        File folder = new File("src/save"); // 目标文件夹的路径
        if (folder.exists() && folder.isDirectory()) {
            // 文件夹存在，遍历目录，查找是否有同名文件夹
            File[] files = folder.listFiles();
            for (File file : files) {
                if (file.isDirectory() && file.getName().equals(userName)) {
                    found = true;
                }
            }
        }
        return found;
    }

    public static boolean insertFile(String fileName) {
        //目录下存在已该用户名命名的文件，该用户无法创建
        File filedir = new File("src/save/"+fileName);
        if(searchFile(fileName))return false;
        filedir.mkdir();
        //在数据库中创建用户信息
        SQLConnection.Update(fileName,0,true);
        return true;
    }
}
