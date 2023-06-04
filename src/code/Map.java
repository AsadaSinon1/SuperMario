package src.code;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;

import static java.awt.image.ImageObserver.ERROR;

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
        // 读取地图信息
        try {
            parseMap();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Cannot open the file!!!", "Error", JOptionPane.INFORMATION_MESSAGE);
            System.exit(ERROR);
        }
        // 将Plot按照顺序add进入plotPool
        for(int i = 0; i<this.info.plotNum; i++)
            plotPool.add(new Plot("src/map/level"+mapId+"/plots/plot"+ (i + 1) +".plotinfo"));
    }
    // 解析地图文件
    public void parseMap() throws IOException {
        FileInputStream fis = new FileInputStream("src/map/level"+mapId+"/level"+mapId+".mapinfo");
        // 读取文件内容
        byte[] buffer = new byte[(int) fis.available()];
        fis.read(buffer);
        // 将字节数组转换为字符串
        info = new MapInfo(new String(buffer));
        // 关闭FileInputStream
        fis.close();
    }

    /**
     * Map的内部类，Map文件信息
     */
    class MapInfo {
        //血量上限
        final int upperHP;// TODO
        // 当前成绩
        int grade;// TODO
        // 场景数量
        final int plotNum;// TODO
        //走路速度、起跳速度、重力加速度、摩擦系数(值越小效果越明显)
        final double walkSpeed = 0.3,jumpSpeed = 0.6,g = 0.003,mu = 0.1;
        // 人物宽度、人物高度、边缘容错度、冲刺时间
        final int width = 30,height = 40;
        final int delay = 100,dashDelay = 250;
        // 是否有冲刺、蹬墙跳、抓墙能力
        final boolean canDash = true,canWallJump = true,canGrabWall = true;

        MapInfo(String content){
            upperHP = 3;
            grade = 0;
            plotNum = Integer.parseInt(content);
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


            //yyt:这里不要用原图大小，要用我们期待的大小
            BufferedImage bufferedImage = new BufferedImage(PlotInfo.screenWidth, PlotInfo.screenHeight, BufferedImage.TYPE_INT_ARGB);

            // 以下均为测试
            // 加载两张照片
            Image image1 = new ImageIcon("src/image/bgMountainCloud4.jpeg").getImage();
            Image image2 = new ImageIcon("src/image/boxBrick.png").getImage();
            Image image3 = new ImageIcon("src/image/boxQuestion.png").getImage();
            Image image4 = new ImageIcon("src/image/goldCoin.png").getImage(); // TODO:我们需要heart

            // 获取缓冲图像的Graphics2D对象
            Graphics2D g2d = bufferedImage.createGraphics();
            // 绘制背景照d片
            g2d.drawImage(image1, 0, 0, PlotInfo.screenWidth, PlotInfo.screenHeight, null); // Changed by yyt.


            // 绘制血量
            for (int j = 0; j < mario.HP; j++) {
                g2d.drawImage(image4, j * 40 - 2, 0,
                        PlotInfo.blockSize, PlotInfo.blockSize + 4, null);// Test code by yyt. TODO: Delete this.
            }
            // 绘制砖块照片
            for (int i = 0; i < 20; i++) {
                if(info.death&&(i==6||i==7)) continue;
                for (int j = 1; j < 3; j++) {
                    g2d.drawImage(image2, i * 40 - 2, PlotInfo.screenHeight - j * PlotInfo.blockSize,
                            PlotInfo.blockSize + 2, PlotInfo.blockSize + 4, null);// Test code by yyt. TODO: Delete this.
                } // +2，+4，是为了消除间隙。此乃曲线救国也！
            }

            if(!info.end&&!info.death){
                for (int j = 3; j < 12; j++) {
                    g2d.drawImage(image2, -2, PlotInfo.screenHeight - j * PlotInfo.blockSize,
                            PlotInfo.blockSize + 2, PlotInfo.blockSize + 4, null);// Test code by yyt. TODO: Delete this.
                }
                for (int j = 3; j < 12; j++) {
                    g2d.drawImage(image2, 160 - 2, PlotInfo.screenHeight - j * PlotInfo.blockSize,
                            PlotInfo.blockSize + 2, PlotInfo.blockSize + 4, null);// Test code by yyt. TODO: Delete this.
                }
            }

            g2d.drawImage(image2, 600 - 2, 520, PlotInfo.blockSize + 2, PlotInfo.blockSize + 4, null);//画一块砖，Test code by yyt.TODO: Delete this.
            if(info.end)
                g2d.drawImage(image3, 560 - 2, 520, PlotInfo.blockSize + 2, PlotInfo.blockSize + 4, null);//画一个出口，Test code by yyt.TODO: Delete this.

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
            if(mario.isDeath())return;
            int code = e.getKeyCode();
            long curTime = System.currentTimeMillis();
            if (code==KeyEvent.VK_A){
                mario.setLeft(true);
                if(mario.isDash())return;
                if(mario.getVx()==0)mario.setVx(-mario.walkSpeed);
                if(!mario.isRight())mario.setFaceRight(false);
                if(mario.isJump()&&curTime<mario.getTimeJump()+mario.delay)mario.jumpLeft(curTime);
            }
            if (code==KeyEvent.VK_D){
                mario.setRight(true);
                if(mario.isDash())return;
                if(mario.getVx() ==0) mario.setVx(mario.walkSpeed);
                if(!mario.isLeft()) mario.setFaceRight(true);
                if(mario.isJump()&&curTime< mario.getTimeJump() +mario.delay)mario.jumpRight(curTime);
            }
            if (code==KeyEvent.VK_SPACE){
                if(mario.isDash())return;
                if((!mario.isJump()&&!mario.isFall())||(mario.isFall()&&curTime<mario.getTimeOnGround()+mario.delay))mario.setVy(mario.jumpSpeed);
                if(mario.isRight()&&!mario.isJump())mario.jumpRight(curTime);
                if(mario.isLeft()&&!mario.isJump())mario.jumpLeft(curTime);
                mario.setFall(true);
                mario.setJump(true);
                mario.setTimeJump(curTime);
            }
            if(code==KeyEvent.VK_L){
                if(!mario.isDashAble())return;
                mario.setDash(true);
                mario.setDashAble(false);
                mario.setTimeDash(curTime);
            }
        }
        @Override
        public void keyReleased(KeyEvent e) {
            if(mario.isDeath())return;
            if (e.getKeyCode() == KeyEvent.VK_A){
                mario.setLeft(false);
                if(mario.isDash())return;
                mario.setVx(mario.isRight() ? mario.walkSpeed : 0);
                if(mario.isRight()) mario.setFaceRight(true);
            }
            if (e.getKeyCode() == KeyEvent.VK_D){
                mario.setRight(false);
                if(mario.isDash())return;
                mario.setVx(mario.isLeft() ? -mario.walkSpeed : 0);
                if(mario.isLeft()) mario.setFaceRight(false);
            }
            if (e.getKeyCode() == KeyEvent.VK_SPACE){
                mario.setJump(false);
                mario.setWallLeftJump(false);
                mario.setWallRightJump(false);
            }
        }

        /**
         * 绘图函数
         */
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            // 加载背景图像
            Image backgroundImage = this.paintPlot();
            // 绘制背景地图
            g.drawImage(backgroundImage, 0, 0,
                    PlotInfo.screenWidth, PlotInfo.screenHeight, null);
            // 绘制enemy图像
            for(Enemy enemy:enemyList)
                g.drawImage(enemy.getCurImage(), Mario.pixelate(enemy.getPositionX()),
                        Mario.pixelate(enemy.getPositionY()), enemy.width, enemy.height, null);
            // 绘制Mario图像
            g.drawImage(Toolkit.getDefaultToolkit().getImage("src/image/mario"+mario.findDirection()+".png"), Mario.pixelate(mario.getX()), Mario.pixelate(mario.getY()), mario.width, mario.height,null);//        g.setColor(Color.BLACK);
        }


        /**
         * plot的内部类，plot的数据结构（很重要）
         */
        class PlotInfo {
            // 画图参数。请务必在这里更改，不要把数字写死在程序里！
            // 窗口宽度,窗口高度,方块的边长,像素大小
            final static int screenWidth = 800, screenHeight = 640, blockSize = 40, pixel = 5;
            int[][] digitalMap;
            int rsbX, rsbY;
            ArrayList<Enemy> enemyList = new ArrayList<>();

            boolean end,death;
            public PlotInfo(String content) {
                digitalMap = new int[screenWidth/pixel+10][screenHeight/pixel+10];
                // TODO:设置重生点
                rsbX = rsbY = 0;

                // TODO:设计敌人
                Mushroom mushroom1 = new Mushroom(500, 0, Plot.this);
                Mushroom mushroom2 = new Mushroom(70, 200, Plot.this);
                enemyList.add(mushroom1);
                enemyList.add(mushroom2);
                // TODO:设计地图
                end = content.equals("end");
                death = content.equals("death");
                for (int i = 0; i < 160; i++)
                    for (int j = 112; j < 128; j++)//yyt：这里应该是112 不是113？砖是40x40（8pixelx8pixel)
                        digitalMap[i][j] = 1;

                for (int i = 120; i < 128; i++)
                    for (int j = 104; j < 112;j++)
                        digitalMap[i][j] = 1;

                if(!end&&!death) {
                    for (int i = 0; i < 8; i++)
                        for (int j = 40; j < 112; j++)
                            digitalMap[i][j] = 1;
                    for (int i = 32; i < 40; i++)
                        for (int j = 40; j < 112; j++)
                            digitalMap[i][j] = 1;
                }

                if(end)
                    for(int i = 112;i<120;i++)
                        for (int j = 104;j<112;j++)
                            digitalMap[i][j] = 2;
                if(death)
                {
                    for(int i = 48;i<64;i++)
                        for (int j = 120;j<128;j++)
                            digitalMap[i][j] = 3;
                    for(int i = 48;i<64;i++)
                        for (int j = 112;j<120;j++)
                            digitalMap[i][j] = 0;
                }

            }
        }
    }
}
