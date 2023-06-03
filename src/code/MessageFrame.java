package src.code;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MessageFrame extends JFrame {
    private JLabel label;
    private JButton button;
    MessageFrame(int kind){
        setTitle("Error");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        getContentPane().setBackground(Color.GRAY);
        setSize(300,200);
        setResizable(false);
        // 获取屏幕的宽度和高度
        int screenWidth = Toolkit.getDefaultToolkit().getScreenSize().width;
        int screenHeight = Toolkit.getDefaultToolkit().getScreenSize().height;

        // 计算窗体的坐标位置
        int x = (screenWidth - getWidth()) / 2;
        int y = (screenHeight - getHeight()) / 2;

        // 将窗体移动到计算出的坐标位置
        setLocation(x, y);
        setLayout(null);

        Font font = new Font("Arial", Font.BOLD, 16);

        button = new JButton("close");
        button.setFont(font);
        button.setBounds(105,110,80,28);
        button.addActionListener(
                new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        dispose();
                    }
                }
        );
        getContentPane().add(button);

        //根据错误类型表示错误信息
        if(kind == 1){
            String labelText = "<html>The entered user name<br>does not exist.</html>";
            label = new JLabel(labelText);
            label.setBounds(40,20,200,100);
        }
        else if(kind == 2){
            label = new JLabel("<html>This user name is used.<br>Please choose another one!</html>");
            label.setBounds(40,20,200,100);
        }
        else if(kind == 3){
            label = new JLabel("<html>Registered successfully!<br>Go back to sign in!</html>");
            label.setBounds(40,20,200,100);
        }
        else if(kind == 4){
            label = new JLabel("Wrong password!");
            label.setBounds(40,20,200,100);
        }
        else{
            label = new JLabel("<html>The password entered<br>twice do not match!</html>");
            label.setBounds(40,20,200,100);
        }
        label.setFont(font);
        getContentPane().add(label);
        setVisible(true);
    }
}
