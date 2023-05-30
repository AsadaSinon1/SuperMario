package src.code;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.IOException;

public class Plot {
    private final String filename;
    public PlotInfo info;

    // 画图参数。请务必在这里更改，不要把数字写死在程序里！
    public int screenHeight = 640; // 窗口高度
    public int screenWidth = 800; // 窗口宽度
    public int blockSize = 40; // 方块的边长

    public Plot(String filename) {
        this.filename = filename;
    }

    public void parsePlot() throws IOException {
        FileInputStream fis = new FileInputStream(filename);
        // 读取文件内容
        byte[] buffer = new byte[(int) fis.available()];
        fis.read(buffer);
        // 将字节数组转换为字符串
        info = new PlotInfo(new String(buffer));
        // 关闭FileInputStream
        fis.close();
    }

    public Image paintPlot() {
        //读取info（待完成）

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
        g2d.drawImage(image2, 600-2, 520, blockSize + 2, blockSize + 4, null);//画一块砖，Test code by yyt.TODO: Delete this. 
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
        // 返回合并后的图像
        return bufferedImage;
    }

    /**
     * struct for plot
     */
    class PlotInfo {
        int [][][]digitalMap;
        public PlotInfo(String content) {

        }
    }
}