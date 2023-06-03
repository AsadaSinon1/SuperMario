package src.code;
import java.awt.*;
import java.util.*;
import javax.swing.*;
import java.awt.event.*;

public class Mario{
    // 血量
    int HP;
    //走路速度、起跳速度、重力加速度
    final double walkSpeed = 0.3,jumpSpeed = 0.6,g = 0.003;
    //屏幕宽度、屏幕高度、像素大小、人物宽度、人物高度、地图数量、边缘容错度
    static final int WIDTH = 800, HEIGHT = 640, pixel = 5;
    final int width = 30,height = 40;
    static final int delay = 100,dashDelay = 250;
    //人物x,y坐标、水平速度、垂直速度
    double x,y,vx,vy;
    //上一帧跑步状态（1，2，3，4）
    int lastState;
    boolean canDash = true,canWallJump = true;
    //左方向是否按下、右方向是否按下、是否离地、跳跃键是否按下、是否死亡
    boolean left,right,fall,jump,death,faceRight,dashAble,dash;
    //左侧是否有墙、右侧是否有墙、是否触发左蹬墙跳、是否触发右蹬墙跳
    boolean wallLeft,wallRight,wallLeftJump,wallRightJump;
    //分数、地图（0为空气，1为墙，2为出口，3为死亡）
    static int grade;
    static int[][] map = new int[WIDTH/pixel+10][HEIGHT/pixel+10];
    //上一次粘在左（右）墙的时刻（用来调整蹬墙容错）、上一次跳跃的时刻、上一次在地面的时刻
    long timeLeft,timeRight,timeJump,timeOnGround,timeDash;

    //设计地图函数，可修改
    void initMap(int [][]map){
        this.map = map;
    }

    // 查找模型朝向
    public String findDirection(){
        if(fall&&wallLeft&&left){
            lastState=1;
            return "SlideLeft";
        }
        if(fall&&wallRight&&right){
            lastState=1;
            return "SlideRight";
        }
        if(fall&&jump&&vx>0){
            lastState=1;
            return "JumpRight";
        }
        if(fall&&jump&&vx<0){
            lastState=1;
            return "JumpLeft";
        }
        if(fall&&jump&&vx==0){
            lastState=1;
            return faceRight?"JumpRight":"JumpLeft";
        }
        if(vx==0){
            lastState = 1;
            return (faceRight?"RunRight":"RunLeft")+ lastState;
        }
        if(faceRight&&vx>0){
            lastState = lastState==1?2:(lastState-2+1)%3+2;
            return "RunRight"+ lastState;
        }
        if(faceRight&&vx<0){
            lastState = 2;
            return "RunLeft"+ lastState;
        }
        if(!faceRight&&vx>0){
            lastState = 2;
            return "RunRight"+ lastState;
        }
        if(!faceRight&&vx<0){
            lastState = lastState==1?2:(lastState-2+1)%3+2;
            return "RunLeft"+ lastState;
        }
        return "RunRight1";
    }

    //重生（参数为重生坐标）
    void respawn(double rsbX,double rsbY){
        x = rsbX;y = rsbY;
        vx = vy = 0;
        dashAble = canDash;
        fall = faceRight = true;
        death = dash = left = right = jump = false;
        wallLeft = wallRight = false;
        wallLeftJump = wallRightJump = false;
        lastState = 1;
    }
    void jumpLeft(long curTime){
        if(canWallJump&&curTime<timeRight+delay&&!wallRightJump&&!wallLeftJump){
            vy = jumpSpeed*0.8;
            vx = -walkSpeed;
            wallLeftJump = true;
        }
    }
    void jumpRight(long curTime){
        if(canWallJump&&curTime<timeLeft+delay&&!wallRightJump&&!wallLeftJump){
            vy = jumpSpeed*0.8;
            vx = walkSpeed;
            wallRightJump = true;
        }
    }
    //像素化坐标
    static int pixelate(double x){
        int t = (int)(x/pixel)*pixel;
        if(2*(x-t)>pixel)return t+pixel;
        return t;
    }
    //碰撞检测
    boolean check(double curx,double cury,int num) throws MyException.Exit {
        int px = pixelate(curx)/pixel,py = pixelate(cury)/pixel;
        if(px<0)px = 0;
        for(int i = 0;i<width/pixel;i++)
            for(int j = 0;j<height/pixel;j++){
                int val = map[px+i][py+j];
                if(val!=num)continue;
                if(num==2) throw new MyException.Exit();
                return true;
            }
        return false;
    }
    //更新每一帧所有状态（尽量不要修改）
    void update(double dt,long curTime) throws MyException.Death, MyException.NextMap, MyException.Exit {
        double newx = x+dt*vx,newy = dash?y:y-dt*vy,t = 0;
        //碰撞检测+调节
        if(check(newx,newy,1)){
            for(t = 0;t<=dt;t+=0.1,x+=0.1*vx)if(check(x,y,1))break;
            x-=0.1*vx;
            for(t = 0;t<=dt;t+=0.1,y-=0.1*vy)if(check(x,y,1))break;
            y+=0.1*vy;
        }else{
            x = newx;
            y = newy;
        }
        //通关、死亡检测
        check(x,y,2);
        if(check(x,y,3)){
            death = true;
            HP--;
            throw new MyException.Death();
        }
        //地图边缘检测
        if(x<0)x = 0;
        if(y<0)y = 0;
        if(x>WIDTH-width){
            throw new MyException.NextMap();
        }
        //离地检测
        int px = pixelate(x)/pixel,py = pixelate(y)/pixel;
        boolean flag = false;
        for(int i = 0;i<width/pixel;i++)if(map[px+i][py+height/pixel]==1)flag = true;
        if(flag){
            if(vy<0)vy = 0;
            fall = false;
        }else fall = true;
        //头顶检测
        if(vy>0&&py>0){
            flag = false;
            for(int i = 0;i<width/pixel;i++)if(map[px+i][py-1]==1)flag = true;
            if(flag)vy = -0.5*vy;
        }
        //速度更新+左右墙体检测（too hard）
        if(fall){
            wallLeft = wallRight = false;
            for(int i = 0;i<height/pixel;i++)if(px>0&&map[px-1][py+i]==1)wallLeft = true;
            for(int i = 0;i<height/pixel;i++)if(map[px+width/pixel][py+i]==1)wallRight = true;
            if(wallLeft)timeLeft = curTime;
            if(wallRight)timeRight = curTime;
            if(wallLeft||wallRight){
                if(dash)vx = 0;
                dash = false;
            }
            boolean onWall = (wallLeft&&left)||(wallRight&&right);
            double rate = onWall?0.3:1.0;
            if(vy>0)vy-=dt*g*(jump?0.5:3.0)*(onWall?1.5:1.0);
            else vy-=dt*g*rate;
            if(vy<-jumpSpeed*rate)vy = -jumpSpeed*rate;
        }else{
            timeOnGround = curTime;
            dashAble = true;
        }
        if(dash){
            long deltaTime = curTime-timeDash;
            if(deltaTime<dashDelay){
                vx = (faceRight?1:-1)*walkSpeed*(1+2*Math.sin(deltaTime*Math.PI/dashDelay));
            }else{
                dash = false;
                if(left&&right)vx = (faceRight?1:-1)*walkSpeed;
                else if(left){
                    vx = -walkSpeed;
                    faceRight = false;
                }else if(right){
                    vx = walkSpeed;
                    faceRight = true;
                }else vx = 0;
            }
        }
    }


}
