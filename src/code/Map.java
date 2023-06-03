package src.code;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Map类，与地图文件相对应，读取地图文件信息，并且创建对应数量的Plot
 */
public class Map {
    // 地图编号
    int mapId;
    // 场景池
    ArrayList<Plot> plotPool = new ArrayList<>();
    // 地图信息
    MapInfo info;
    // 游戏控制器
    Game controller;
    Map(int mapId,Game controller){
        this.controller=controller;
        this.mapId = mapId;
        this.info = new MapInfo();
        // 将Plot按照顺序add进入plotPool
        for(int i = 0; i<this.info.plotNum; i++)
            plotPool.add(new Plot("src/map/level"+mapId+"/plots/plot"+ (i + 1) +".plotinfo"));
    }

    /**
     * Map的内部类，Map文件信息
     */
    class MapInfo {
        //血量上限
        int upperHP;
        // 当前成绩
        int grade;
        // 场景数量
        int plotNum;
        MapInfo(){
            upperHP = 3;
            grade = 0;
            plotNum = 2;
        }
    }

    /**
     * Map的内部类，Plot类
     * JPanel的子类，读取Plot文件信息，绘制Plot图像
     */
    class Plot extends JPanel implements KeyListener {
        final String filename;
        public PlotInfo info;
        Mario mario;
        // 敌人列表
        ArrayList<Enemy> enemyList = new ArrayList<>();

        // 画图参数。请务必在这里更改，不要把数字写死在程序里！
        public int screenHeight = 640; // 窗口高度
        public int screenWidth = 800; // 窗口宽度
        public int blockSize = 40; // 方块的边长

        public Plot(String filename) {
            this.filename = filename;
            addKeyListener(this); // 添加键盘监听器
        }

        /**
         * 读取*.plot文件
         * @throws IOException
         */
        public void parsePlot() throws IOException {
            FileInputStream fis = new FileInputStream(filename);
            // 读取文件内容
            byte[] buffer = new byte[(int) fis.available()];
            fis.read(buffer);
            // 将字节数组转换为字符串
            info = new PlotInfo(new String(buffer));
            // 关闭FileInputStream
            fis.close();

            enemyList = info.enemyList;
        }

        /**
         * 根据文件内容，绘制背景图并且返回
         */
        public Image paintPlot() {
            // TODO:读取info

            // 以下均为测试
            // 加载两张照片
            Image image1 = new ImageIcon("src/image/bgMountainCloud4.jpeg").getImage();
            Image image2 = new ImageIcon("src/image/boxBrick.png").getImage();
            // 创建一个缓冲图像
            // BufferedImage bufferedImage = new BufferedImage(image1.getWidth(null), image1.getHeight(null),
            //         BufferedImage.TYPE_INT_ARGB);
            //yyt:这里不要用原图大小，要用我们期待的大小
            BufferedImage bufferedImage = new BufferedImage(this.screenWidth, this.screenHeight, BufferedImage.TYPE_INT_ARGB);

            // 获取缓冲图像的Graphics2D对象
            Graphics2D g2d = bufferedImage.createGraphics();
            // 绘制背景照片
            g2d.drawImage(image1, 0, 0, this.screenWidth, this.screenHeight, null); // Changed by yyt.

            // 绘制砖块照片
            for (int i = 0; i < 20; i++) {
                for (int j = 1; j < 3; j++) {
                    g2d.drawImage(image2, i * 40 - 2, this.screenHeight - j * this.blockSize,
                            this.blockSize + 2, this.blockSize + 4, null);// Test code by yyt. TODO: Delete this.
                } // +2，+4，是为了消除间隙。此乃曲线救国也！
            }
            g2d.drawImage(image2, 600 - 2, 520, blockSize + 2, blockSize + 4, null);//画一块砖，Test code by yyt.TODO: Delete this.
            // for (int i = 0; i < 11; i++) {
            //     for (int j = 2; j < 15; j++) {
            //         g2d.drawImage(image2, i * 25-10, 548 - j * 24, null);
            //     }
            // }

            // for (int i = 14; i < 18; i++) {
            //     for (int j = 2; j < 15; j++) {
            //         g2d.drawImage(image2, i * 25, 548 - j * 24, null);
            //     }
            // }

            // 完成绘制
            g2d.dispose();
            // 测试到此为止
            // 返回合并后的图像
            return bufferedImage;
        }
        @Override
        public void keyTyped(KeyEvent e) {
        }
        @Override
        public void keyPressed(KeyEvent e) {
            if(mario.death)return;
            int code = e.getKeyCode();
            long curTime = System.currentTimeMillis();
            if (code==KeyEvent.VK_A){
                mario.left = true;
                if(mario.dash)return;
                if(mario.vx==0)mario.vx = -mario.walkSpeed;
                if(!mario.right)mario.faceRight = false;
                if(mario.jump&&curTime<mario.timeJump+mario.delay)mario.jumpLeft(curTime);
            }
            if (code==KeyEvent.VK_D){
                mario.right = true;
                if(mario.dash)return;
                if(mario.vx==0)mario.vx = mario.walkSpeed;
                if(!mario.left)mario.faceRight = true;
                if(mario.jump&&curTime<mario.timeJump+mario.delay)mario.jumpRight(curTime);
            }
            if (code==KeyEvent.VK_SPACE){
                if(mario.dash)return;
                if((!mario.jump&&!mario.fall)||(mario.fall&&curTime<mario.timeOnGround+mario.delay))mario.vy = mario.jumpSpeed;
                if(mario.right&&!mario.jump)mario.jumpRight(curTime);
                if(mario.left&&!mario.jump)mario.jumpLeft(curTime);
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
        @Override
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

        /**
         * 绘图函数
         */
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            // 加载背景图像
            //Image backgroundImage = currPlot.paintPlot().getScaledInstance(800, 640, Image.SCALE_SMOOTH);//调整大小
            Image backgroundImage = this.paintPlot();
            // 绘制图像
            g.drawImage(backgroundImage, 0, 0,
                    this.screenWidth, this.screenHeight, null);
            for(Enemy enemy:enemyList)
                g.drawImage(enemy.getCurImage(), Mario.pixelate(enemy.getPositionX()),
                        Mario.pixelate(enemy.getPositionY()), enemy.width, enemy.height, null);

            g.drawImage(Toolkit.getDefaultToolkit().getImage("src/image/mario"+mario.findDirection()+".png"), mario.pixelate(mario.x), mario.pixelate(mario.y), mario.width, mario.height,null);//        g.setColor(Color.BLACK);
        }


        /**
         * plot的内部类，plot的数据结构（很重要）
         */
        class PlotInfo {
            int WIDTH,HEIGHT,pixel;
            int[][] digitalMap;
            int rsbX, rsbY;
            ArrayList<Enemy> enemyList = new ArrayList<>();
            public PlotInfo(String content) {
                WIDTH = 800;
                HEIGHT = 640;
                pixel = 5;
                digitalMap = new int[WIDTH/pixel+10][HEIGHT/pixel+10];;
                // 设计敌人
                Mushroom mushroom1 = new Mushroom(500, 0, 0);
                Mushroom mushroom2 = new Mushroom(50, 200, 0);
                enemyList.add(mushroom1);
                enemyList.add(mushroom2);
                // 设计地图
                for (int i = 0; i < 160; i++)
                    for (int j = 112; j < 128; j++)//yyt：这里应该是112 不是113？砖是40x40（8pixelx8pixel)
                        digitalMap[i][j] = 1;

                for (int i = 120; i < 128; i++)
                    for (int j = 104; j < 112;j++)
                        digitalMap[i][j] = 1;
                if(content.equals("end"))
                    for(int i = 100;i<150;i++)
                        for (int j = 104;j<112;j++)
                            digitalMap[i][j] = 2;
                // 设置重生点
                rsbX = rsbY = 0;
            }
        }
    }
}
