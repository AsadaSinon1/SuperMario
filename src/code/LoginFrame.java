package src.code;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Objects;

public class LoginFrame extends JFrame {
    private JPanel panel1;
    private JPanel panel2;
    private JPanel panel3;

    LoginFrame(){
        setTitle("Login");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
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
        private JPasswordField passwordField;

        private passwordBtn btn;
        LogPanel2(){
            setBounds(0,0,400,500);
            setBackground(new Color(100, 149, 237));
            setLayout(null);

            ImageIcon icon1 = new ImageIcon("src/image/welcome.png");
            label1 = new JLabel(icon1);
            label1.setBounds(59,10,250,130);
            this.add(label1);

            ImageIcon icon2 = new ImageIcon("src/image/user.png");
            label2 = new JLabel(icon2);
            label2.setBounds(45,150,45,45);
            this.add(label2);

            textField1 = new JTextField();
            textField1.setBounds(110,150,180,35);
            textField1.setBorder(new LineBorder(Color.BLACK));
            Font font = new Font("Arial", Font.BOLD, 16);
            textField1.setFont(font);
            Insets insets = new Insets(0, 10, 0, 10);
            textField1.setMargin(insets);
            this.add(textField1);

            ImageIcon icon3 = new ImageIcon("src/image/password.png");
            label3 = new JLabel(icon3);
            label3.setBounds(45,210,45,45);
            this.add(label3);

            passwordField = new JPasswordField('*');
            passwordField.setBounds(110,210,180,35);
            passwordField.setBorder(new LineBorder(Color.BLACK));
            passwordField.setFont(font);
            passwordField.setMargin(insets);
            this.add(passwordField);

            btn = new passwordBtn();
            btn.setBounds(143,268,110,30);
            btn.addActionListener(
                    new ActionListener() {
                        public void actionPerformed(ActionEvent e) {
                            if(btn.show == false){
                                passwordField.setEchoChar((char)0);
                                btn.show = true;
                            }
                            else{
                                passwordField.setEchoChar('*');
                                btn.show = false;
                            }
                        }
                    }
            );
            this.add(btn);

            ImageIcon icon4 = new ImageIcon("src/image/continue.png");
            label4 = new JLabel(icon4);
            label4.setBounds(67,300,250,80);
            this.add(label4);

            ImageIcon icon5 = new ImageIcon("src/image/shadeSmall.png");
            label5 = new JLabel(icon5);
            label5.setBounds(106,316,170,60);
            label5.setVisible(false);
            this.add(label5);

            ImageIcon icon6 = new ImageIcon("src/image/back.png");
            label6 = new JLabel(icon6);
            label6.setBounds(67,370,250,80);
            this.add(label6);

            label7 = new JLabel(icon5);
            label7.setBounds(106,386,170,60);
            label7.setVisible(false);
            this.add(label7);

            label4.addMouseListener(
                    new MouseListener() {
                        public void mouseClicked(MouseEvent arg0) {
                            //在save中查找用户名命名的文件，看是否存在；存在，进入选关界面；不存在，弹窗
                            //告知用户输入的用户名不正确
                            boolean found = false;
                            String inputText = textField1.getText();
                            found = FileOperation.searchFile("src/save",inputText);
                            if(found){
                                boolean right = (SQLConnection.SearchPassword(inputText).equals(new String(passwordField.getPassword())));
//                                right = true;//TODO: delete this
                                if(right){
                                    dispose();
                                    GameStartFrame frame = new GameStartFrame();
                                    frame.add(new LevelChosenPanel("src/save/"+inputText));
                                    frame.setVisible(true);
                                }
                                else{
                                    SwingUtilities.invokeLater(new Runnable() {
                                        public void run() {
                                            new MessageFrame(4);
                                        }
                                    });
                                }
                            }
                            else{
                                //弹出错误弹窗
                                SwingUtilities.invokeLater(new Runnable() {
                                    public void run() {
                                        new MessageFrame(1);
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
        private JPasswordField passwordField1;
        private JPasswordField passwordField2;
        private passwordBtn btn1;
        private passwordBtn btn2;
        LogPanel3(){
            setBounds(0,0,400,500);
            setBackground(new Color(100, 149, 237));
            setLayout(null);

            ImageIcon icon1 = new ImageIcon("src/image/welcome.png");
            label1 = new JLabel(icon1);
            label1.setBounds(55,10,250,130);
            this.add(label1);

            ImageIcon icon2 = new ImageIcon("src/image/user.png");
            label2 = new JLabel(icon2);
            label2.setBounds(35,145,45,45);
            this.add(label2);

            textField1 = new JTextField();
            textField1.setBounds(100,145,180,35);
            textField1.setBorder(new LineBorder(Color.BLACK));
            Font font = new Font("Arial", Font.BOLD, 16);
            textField1.setFont(font);
            Insets insets = new Insets(0, 10, 0, 10);
            textField1.setMargin(insets);
            this.add(textField1);

            ImageIcon icon3 = new ImageIcon("src/image/password.png");
            label3 = new JLabel(icon3);
            label3.setBounds(35,205,45,45);
            this.add(label3);

            passwordField1 = new JPasswordField();
            passwordField1.setBounds(100,205,180,35);
            passwordField1.setBorder(new LineBorder(Color.BLACK));
            passwordField1.setFont(font);
            passwordField1.setMargin(insets);
            this.add(passwordField1);

            btn1 = new passwordBtn();
            btn1.setBounds(290,207,100,30);
            btn1.addActionListener(
                    new ActionListener() {
                        public void actionPerformed(ActionEvent e) {
                            if(btn1.show == false){
                                passwordField1.setEchoChar((char)0);
                                btn1.show = true;
                            }
                            else{
                                passwordField1.setEchoChar('*');
                                btn1.show = false;
                            }
                        }
                    }
            );
            this.add(btn1);

            passwordField2 = new JPasswordField();
            passwordField2.setBounds(100,260,180,35);
            passwordField2.setBorder(new LineBorder(Color.BLACK));
            passwordField2.setFont(font);
            passwordField2.setMargin(insets);
            this.add(passwordField2);

            btn2 = new passwordBtn();
            btn2.setBounds(290,262,100,30);
            btn2.addActionListener(
                    new ActionListener() {
                        public void actionPerformed(ActionEvent e) {
                            if(btn2.show == false){
                                passwordField2.setEchoChar((char)0);
                                btn2.show = true;
                            }
                            else{
                                passwordField2.setEchoChar('*');
                                btn2.show = false;
                            }
                        }
                    }
            );
            this.add(btn2);

            ImageIcon icon4 = new ImageIcon("src/image/continue.png");
            label4 = new JLabel(icon4);
            label4.setBounds(67,300,250,80);
            this.add(label4);

            ImageIcon icon5 = new ImageIcon("src/image/shadeSmall.png");
            label5 = new JLabel(icon5);
            label5.setBounds(106,316,170,60);
            label5.setVisible(false);
            this.add(label5);

            ImageIcon icon6 = new ImageIcon("src/image/back.png");
            label6 = new JLabel(icon6);
            label6.setBounds(67,370,250,80);
            this.add(label6);

            label7 = new JLabel(icon5);
            label7.setBounds(106,386,170,60);
            label7.setVisible(false);
            this.add(label7);

            label4.addMouseListener(
                    new MouseListener() {
                        public void mouseClicked(MouseEvent arg0) {
                            //在save中查找该以该用户名命名的文件夹是否存在，若存在，提醒用户改用户名已被使用
                            //不存在，创建用户名存入save
                            String inputText = textField1.getText();
                            boolean found = FileOperation.searchFile("src/save", inputText);
                            if(found){
                                SwingUtilities.invokeLater(() -> new MessageFrame(2));
                            }
                            else{
                                boolean isEqual = (new String(passwordField1.getPassword()).equals(new String(passwordField2.getPassword())));
                                if(isEqual){
                                    FileOperation.insertFile("src/save",inputText,new String(passwordField1.getPassword()));
                                    FileOperation.insertFile("src/save/"+inputText,"level1","");
                                    FileOperation.insertFile("src/save/"+inputText,"level2","");
                                    FileOperation.insertFile("src/save/"+inputText,"level3","");
                                    FileOperation.insertFile("src/save/"+inputText,"level4","");
                                    if(FileOperation.copyFile("src/map/level1","src/save/"+inputText+"/level1"))
                                        SwingUtilities.invokeLater(() -> new MessageFrame(3));
                                    else{
                                        JOptionPane.showMessageDialog(null, "Cannot open the file!!!", "Error", JOptionPane.INFORMATION_MESSAGE);
                                        System.exit(ERROR);
                                    }
                                }
                                else{
                                    SwingUtilities.invokeLater(() -> new MessageFrame(5));
                                }
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
    public static boolean searchFile(String path, String userName) {
        // 要查找的目录和文件名
        boolean found = false;
        File folder = new File(path); // 目标文件夹的路径
        if (folder.exists() && folder.isDirectory()) {
            // 文件夹存在，遍历目录，查找是否有同名文件夹
            File[] files = folder.listFiles();
            for (File file : files) {
                if (file.getName().equals(userName)) {
                    found = true;
                }
            }
        }
        return found;
    }

    public static boolean insertFile(String path, String fileName,String password) {
        //目录下存在已该用户名命名的文件，该用户无法创建
        File filedir = new File(path+'/'+fileName);
        if(searchFile(path,fileName))return false;
        filedir.mkdir();
        if(Objects.equals(path, "src/save"))
            //在数据库中创建用户信息
            SQLConnection.Update(fileName,password,0,true);
        return true;
    }

    public static boolean copyFile(String source, String target){
        Path sourceFolder = Paths.get(source);
        Path targetFolder = Paths.get(target);
        try {
            // 复制地图文件夹及其内容
            Files.walk(sourceFolder)
                    .forEach(src -> {
                        try {
                            Path tgt = targetFolder.resolve(sourceFolder.relativize(src));
                            Files.copy(src, tgt, StandardCopyOption.COPY_ATTRIBUTES);
                        } catch (IOException ignored) {}
                    });
            return true;
        } catch (IOException e) {
            return false;
        }
    }
}
class passwordBtn extends JButton{
    public boolean show;
    passwordBtn(){
        setText("show/hide");
        setBackground(Color.GRAY);
        show = false;
    }
}
