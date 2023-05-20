
import java.awt.*;
import java.util.*;
import javax.swing.*;
import java.awt.event.*;

public class Mario extends JFrame implements Runnable{
    Thread thread = new Thread(this);
    final double walkSpeed = 0.2,jumpSpeed = 0.7,g = 0.002;
    final int WIDTH = 800,HEIGHT = 640,pixel = 5,width = 30,height = 40;
    double x = 100, y = 100;
    double vx,vy;
    boolean left,right,fall = true,jump;
    int[][] map = new int[WIDTH/pixel+50][HEIGHT/pixel+50];//0:air 1:wall

    static public void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new Mario();
        });
    }

    public Mario() {
        this.setSize(WIDTH, HEIGHT);
        this.setResizable(false);
        this.setVisible(true);
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        this.getContentPane().requestFocus();
        this.getContentPane().addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode()==KeyEvent.VK_A){
                    if(vx==0)vx = -walkSpeed;
                    left = true;
                }
                if (e.getKeyCode()==KeyEvent.VK_D){
                    if(vx==0)vx = walkSpeed;
                    right = true;
                }
                if (e.getKeyCode()==KeyEvent.VK_SPACE){
                    if(!jump&&!fall)vy = jumpSpeed;
                    fall = jump = true;
                }
            }
            public void keyReleased(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_A){
                    left = false;
                    vx = right?walkSpeed:0;
                }
                if (e.getKeyCode() == KeyEvent.VK_D){
                    right = false;
                    vx = left?-walkSpeed:0;
                }
                if (e.getKeyCode() == KeyEvent.VK_SPACE){
                    jump = false;
                }
            }
        });
        thread.setDaemon(true);
        thread.start();
        //design the map
        for(int i = 0;i<60;i++)
            for(int j = 80;j<100;j++)
                map[i][j] = 1;
        for(int i = 0;i<160;i++)
            for(int j = 119;j<150;j++)
                map[i][j] = 1;
        for(int i = 80;i<160;i++)
            for(int j = 100;j<120;j++)
                map[i][j] = 1;
    }

    @Override
    public void paint(Graphics g) {
        g.setColor(Color.GRAY);
        g.fillRect(0, 0, WIDTH, HEIGHT);
        g.setColor(Color.WHITE);
        g.fillRect(pixelate(x),pixelate(y),width,height);
        g.setColor(Color.BLACK);
        g.fillRect(0,595,800,5);
        g.fillRect(400,500,400,100);
        g.fillRect(0,400,300,100);
    }

    int pixelate(double x){
        int t = (int)(x/pixel)*pixel;
        if(2*(x-t)>pixel)return t+pixel;
        return t;
    }
    //mario collapse check
    boolean check(double curx,double cury){
        int px = pixelate(curx)/pixel,py = pixelate(cury)/pixel;
        for(int i = 0;i<width/pixel;i++)
            for(int j = 0;j<height/pixel;j++)
                if(map[px+i][py+j]==1)return true;
        return false;
    }

    void update(double dt){
        double newx = x+dt*vx,newy = y-dt*vy,t = 0;
        if(check(newx,newy)){//collapse check and adjust
            for(t = 0;t<=dt;t+=0.1,x+=0.1*vx)if(check(x,y))break;
            x-=0.1*vx;
            for(t = 0;t<=dt;t+=0.1,y-=0.1*vy)if(check(x,y))break;
            y+=0.1*vy;
        }else{
            x = newx;
            y = newy;
        }
        //area bound check
        if(x<0)x = 0;
        if(y<0)y = 0;
        if(x>WIDTH-width)x = WIDTH-width;
        //fall check
        int px = pixelate(x)/pixel,py = pixelate(y)/pixel;
        boolean flag = false;
        for(int i = 0;i<width/pixel;i++)if(map[px+i][py+height/pixel]==1)flag = true;
        if(flag){
            if(vy<0)vy = 0;
            fall = false;
        }else fall = true;
        //head check
        if(vy>0&&py>0){
            flag = false;
            for(int i = 0;i<width/pixel;i++)if(map[px+i][py-1]==1)flag = true;
            if(flag)vy = -0.5*vy;
        }
        //velocity update
        if(fall){
            vy-=dt*g;
            if(vy<-jumpSpeed)vy = -jumpSpeed;
        }
    }

    @Override
    public void run(){
        long curTime = 0,prevTime = new Date().getTime();
        while(true){
            mySleep(15);
            curTime = new Date().getTime();
            update(curTime-prevTime);
            repaint();
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
