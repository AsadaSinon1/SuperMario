package src.code;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.util.ArrayList;

public class Backstage extends JFrame implements Runnable {
    Thread thread = new Thread(this);
    Plot currPlot;
    ArrayList<Enemy> enemyList = new ArrayList<>();

    Mario mario = new Mario();
    Enemy e = new Enemy(0, 0, 1);

    
    Backstage() {
        Mushroom mushroom1 = new Mushroom(500, 0, 0);
        Mushroom mushroom2 = new Mushroom(50, 200, 0);
        enemyList.add(mushroom1);
        enemyList.add(mushroom2);
        currPlot = new Plot("src/map/level1.mariomap");
        try {
            currPlot.parsePlot();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Cannot open the file!!!", "Error", JOptionPane.INFORMATION_MESSAGE);
        }
        this.setContentPane(new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                // 加载背景图像
                //Image backgroundImage = currPlot.paintPlot().getScaledInstance(800, 640, Image.SCALE_SMOOTH);//调整大小
                Image backgroundImage = currPlot.paintPlot();
                // 绘制图像
                g.drawImage(backgroundImage, 0, 0,
                        currPlot.screenWidth, currPlot.screenHeight, null);
                for(Enemy enemy:enemyList)
                g.drawImage(enemy.getCurImage(), Mario.pixelate(enemy.getPositionX()),
                        Mario.pixelate(enemy.getPositionY()), enemy.width, enemy.height, null);
                        
                g.drawImage(Toolkit.getDefaultToolkit().getImage("src/image/mario"+mario.findDirection()+".png"), Mario.pixelate(mario.x), Mario.pixelate(mario.y), mario.width, mario.height,null);//        g.setColor(Color.BLACK);
            }
        });
        // mario.initMap(currPlot.info.digitalMap);
        //design the map
        for (int k = 0; k < 2; k++) {
            for (int i = 0; i < 160; i++)
                for (int j = 112; j < 128; j++)//yyt：这里应该是112 不是113？砖是40x40（8pixelx8pixel)
                    Mario.map[k][i][j] = 1;
        }
        for (int i = 120; i < 128; i++)
            for (int j = 104; j < 112;j++)
                Mario.map[0][i][j] = 1;
//        for(int k = 0;k<2;k++){
//            for(int i = 0;i<60;i++)
//                for(int j = 48;j<117;j++)
//                    mario.map[k][i][j] = 1;
//        }
//        for(int k = 0;k<2;k++){
//            for(int i = 80;i<102;i++)
//                for(int j = 48;j<117;j++)
//                    mario.map[k][i][j] = 1;
//        }

        this.getContentPane().setPreferredSize(new Dimension(800,640));
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.pack();
        this.setResizable(false);
        this.setVisible(true);
        this.getContentPane().requestFocus();
        //键盘事件
        this.getContentPane().addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if(mario.death)return;
                int code = e.getKeyCode();
                long curTime = System.currentTimeMillis();
                if (code==KeyEvent.VK_A){
                    mario.left = true;
                    if(mario.dash)return;
                    if(mario.vx==0)mario.vx = -mario.walkSpeed;
                    if(!mario.right)mario.faceRight = false;
                    if(mario.jump&&curTime<mario.timeJump+mario.delay&&curTime<mario.timeRight+mario.delay&&!mario.wallRightJump&&!mario.wallLeftJump){
                        mario.vy = mario.jumpSpeed*0.8;
                        mario.vx = -mario.walkSpeed;
                        mario.wallLeftJump = true;
                    }
                }
                if (code==KeyEvent.VK_D){
                    mario.right = true;
                    if(mario.dash)return;
                    if(mario.vx==0)mario.vx = mario.walkSpeed;
                    if(!mario.left)mario.faceRight = true;
                    if(mario.jump&&curTime<mario.timeJump+mario.delay&&curTime<mario.timeLeft+mario.delay&&!mario.wallRightJump&&!mario.wallLeftJump){
                        mario.vy = mario.jumpSpeed*0.8;
                        mario.vx = mario.walkSpeed;
                        mario.wallRightJump = true;
                    }
                }
                if (code==KeyEvent.VK_SPACE){
                    if(mario.dash)return;
                    if((!mario.jump&&!mario.fall)||(mario.fall&&curTime<mario.timeOnGround+mario.delay))mario.vy = mario.jumpSpeed;
                    if(mario.right&&curTime<mario.timeLeft+mario.delay&&!mario.jump&&!mario.wallRightJump&&!mario.wallLeftJump){
                        mario.vy = mario.jumpSpeed*0.8;
                        mario.vx = mario.walkSpeed;
                        mario.wallRightJump = true;
                    }
                    if(mario.left&&curTime<mario.timeRight+mario.delay&&!mario.jump&&!mario.wallLeftJump&&!mario.wallRightJump){
                        mario.vy = mario.jumpSpeed*0.8;
                        mario.vx = -mario.walkSpeed;
                        mario.wallLeftJump = true;
                    }
                    mario.fall = mario.jump = true;
                    mario.timeJump = curTime;
                }
                if(code==KeyEvent.VK_L){
                    if(!mario.dashAble)return;
                    mario.dash = true;
                    mario.dashAble = false;
                    mario.timeDash = curTime;
                }
            }
            public void keyReleased(KeyEvent e) {
                if(mario.death)return;
                if (e.getKeyCode() == KeyEvent.VK_A){
                    mario.left = false;
                    if(mario.dash)return;
                    mario.vx = mario.right?mario.walkSpeed:0;
                    if(mario.right)mario.faceRight = true;
                }
                if (e.getKeyCode() == KeyEvent.VK_D){
                    mario.right = false;
                    if(mario.dash)return;
                    mario.vx = mario.left?-mario.walkSpeed:0;
                    if(mario.left)mario.faceRight = false;
                }
                if (e.getKeyCode() == KeyEvent.VK_SPACE){
                    mario.jump = false;
                    mario.wallLeftJump = false;
                    mario.wallRightJump = false;
                }
            }
        });
        thread.setDaemon(true);
        thread.start();
        mario.respawn(0);
    }


    @Override
    public void run() {
        long curTime = 0,prevTime =System.currentTimeMillis();
        int interval=0;
        while(true){
            mySleep(20);
            curTime = System.currentTimeMillis();
            mario.update(curTime - prevTime, curTime);
            for(Enemy enemy:enemyList)
                enemy.update(curTime - prevTime, curTime);          
            if(interval==0) repaint();
            interval = (interval+1)%2;
            prevTime = curTime;
        }
    }
    void mySleep(long millis){
        try{
            Thread.sleep(millis);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
