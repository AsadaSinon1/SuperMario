package src.code;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import javax.swing.Timer;

/**
 * Backstage类，JFrame的子类，每张地图都由一个Backstage类实现后台操作，包括Plot的展示、定时更新与运行
 * 每个后台都控制着Mario的数据，并与当前Plot交互，死亡或进入新场景会更换Plot
 */
public class Backstage extends JFrame implements Runnable, ActionListener {
    // 后台进程
    Thread thread = new Thread(this);

    // 后台刷新计时器
    Timer timer = new Timer(20,this);
    // 后台所属地图
    Map currMap;
    // 当前场景编号
    int plotId = 1;
    // 后台当前场景
    Map.Plot currPlot;
    Mario mario;


    Backstage(Map currMap) {
        this.currMap = currMap;
        mario = new Mario(currMap.info);
        // 初始化Plot（第一个场景）
        changePlot();
        mario.respawn(currPlot.info.rsbX, currPlot.info.rsbY);

        currPlot.setPreferredSize(new Dimension(800,640));

        int showWidth = (Toolkit.getDefaultToolkit().getScreenSize().width - Map.Plot.PlotInfo.screenWidth)/2;
        int showHeight = (Toolkit.getDefaultToolkit().getScreenSize().height - Map.Plot.PlotInfo.screenHeight)/2;
        this.setLocation(showWidth,showHeight);

        this.pack();
        this.setResizable(false);
        this.setVisible(true);
        this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);

        thread.setDaemon(true);
        thread.start();
    }

    /**
     * 更换场景并且重新设置组件的聚焦点，初始化mario的数字地图
     */
    public void changePlot(){
        currPlot = currMap.plotPool.get(plotId-1);
        currPlot.mario=mario;
        try {
            currPlot.parsePlot();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Cannot open the file!!!", "Error", JOptionPane.INFORMATION_MESSAGE);
            System.exit(ERROR);
        }


        this.setContentPane(currPlot);
        this.setVisible(true);
        currPlot.requestFocus();
        mario.initMap(currPlot.info.digitalMap);
    }
    // 线程运行函数
    @Override
    public void run() {
        prevTime = System.currentTimeMillis();
        timer.start();
    }

    private long prevTime;

    // repaint时间间隔
    private int interval=0;
    // 计时器周期运行函数
    @Override
    public void actionPerformed(ActionEvent ae) {

        // 刷新起始时间
        long curTime = System.currentTimeMillis();


        //更新Enemy状态
        for (Enemy enemy : currPlot.enemyList)
            enemy.update(curTime - prevTime, curTime);

        //更新Mario状态
        try {
            mario.update(curTime - prevTime, curTime);
        } catch (MyException.Death e) {
            if (mario.HP > 0)
                mario.respawn(currPlot.info.rsbX, currPlot.info.rsbY);
            else {
                plotId = 1;
                changePlot();
                mario.HP = currMap.info.upperHP;
                mario.respawn(currPlot.info.rsbX, currPlot.info.rsbY);
            }
        } catch (MyException.NextMap e) {
            changeMap(false);
        } catch (MyException.NextPlot e) {
            plotId++;
            changePlot();
            mario.respawn(0, mario.getY());
        }

        // 刷新JPanel
        if (interval == 0)
            currPlot.paintImmediately(0, 0, Map.Plot.PlotInfo.screenWidth, Map.Plot.PlotInfo.screenHeight);
        interval = (interval + 1) % 2;
        prevTime = curTime;

        if(currPlot.backFlag)
        {
            int option = JOptionPane.showConfirmDialog(this, "确定要退出吗？", "退出提示", JOptionPane.YES_NO_OPTION);
            if(option == JOptionPane.YES_OPTION)
                changeMap(true);
            currPlot.backFlag = false;
            prevTime = System.currentTimeMillis();
        }

    }
    // 进入选关界面
    public void changeMap(boolean back) {
        thread.interrupt();
        timer.stop();
        //TODO: SQL操作

        // 退出
        dispose();
        currMap.controller.nextGame(back);
    }
}
