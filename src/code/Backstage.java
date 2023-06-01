package src.code;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;
import javax.swing.Timer;
public class Backstage extends JFrame implements Runnable, ActionListener {
    Thread thread = new Thread(this);

    Timer timer = new Timer(20,this);
    // 后台所属地图
    Map currMap;
    // 当前场景编号
    int plotId = 1;
    // 后台当前场景
    Map.Plot currPlot;
    Mario mario = new Mario();


    Backstage(Map currMap) {
        this.currMap = currMap;
        // 初始化Plot（第一个场景）
        changePlot();
        mario.HP=currMap.info.upperHP;
        mario.respawn(currPlot.info.rsbX, currPlot.info.rsbY);

        currPlot.setPreferredSize(new Dimension(800,640));

        this.pack();
        this.setResizable(false);
        this.setVisible(true);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);

        thread.setDaemon(true);
        thread.start();
    }

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
    @Override
    public void run() {
        prevTime = System.currentTimeMillis();
        timer.start();
    }

    long curTime,prevTime;

    int interval=0;
    @Override
    public void actionPerformed(ActionEvent ae) {

        curTime = System.currentTimeMillis();

        for(Enemy enemy:currPlot.enemyList)
            enemy.update(curTime - prevTime, curTime);

        try {
            mario.update(curTime - prevTime, curTime);
        } catch (MyException.Death e) {
            if(mario.HP > 0)
                mario.respawn(currPlot.info.rsbX,currPlot.info.rsbY);
            else{
                plotId = 0;
                changePlot();
                mario.HP=currMap.info.upperHP;
                mario.respawn(currPlot.info.rsbX, currPlot.info.rsbY);
            }
        } catch (MyException.NextMap e){
            plotId++;
            changePlot();
            mario.respawn(0, mario.y);
        }


        if(interval==0) currPlot.paintImmediately(0,0, currPlot.screenWidth,currPlot.screenHeight);
        interval = (interval+1)%2;
        prevTime = curTime;

    }
}
