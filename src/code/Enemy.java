package src.code;

import java.awt.*;
import java.io.IOException;
import java.sql.Date;


public class Enemy {
    int width, height;//怪物的宽度和高度，目前只支持矩形怪物
    //怪物的长宽应保持为pixel的倍数，这里不用pixelwidth是为了和Mario保持一致
    int curMapId;//怪物所在的地图 待改：目前版本的怪物只能待在一张地图里
    int value;//杀死怪物可以得到的分数
    double positionX, positionY, speedX, speedY;// X坐标，Y坐标，X轴速度，Y轴速度
    double deathTime = -1;//死亡时间
    LifeState lifeState = LifeState.ALIVE;//生命状态
    Image[] img;//怪物的所有贴图
    Image defaultImg = Toolkit.getDefaultToolkit().getImage("src/image/num4.png");//待改 完全透明图像 
    /**
     * @param x x坐标
     * @param y y坐标
     * @param mapId 所在地图
     */
    Enemy(double x,double y,int mapId)
    {
        positionX = x;
        positionY = y;
        curMapId = mapId;
    }
    /**
     * @return 怪物当前状态对应的贴图
     */
    public double getPositionX() {
        return positionX;
    }
    public double getPositionY() {
        return positionY;
    }
    Image getCurImage() {
        return defaultImg;
    }
    /**
     * 更新敌人每一帧的状态
     */
    void update(double dt,long curTime) {}
    
    /**
     * 处理敌人死亡
     * 注意每个敌人只调用一次这个，不要对尸体调用death
     */
    void death() throws Exception
    {
        if (lifeState != LifeState.ALIVE)
            throw new Exception("对不是活着的怪物调用death（）");
        lifeState = LifeState.REMAIN;
        deathTime = System.currentTimeMillis();
        speedX = 0;//杀死怪物后，速度变为0
        speedY = 0;//后续可能待改，尸体保持速度掉落比较合理
        Mario.grade += value;//得分
        //待改，播放怪物死亡音乐
    }
    /**
     * 修正坐标，防止出界
     */
    void positionCorrection()
    {
        if (positionX < 0)
            positionX = 0;
        if (positionX + width > Mario.WIDTH)
            positionX = Mario.WIDTH - width;
        if (positionY < 0)
            positionY = 0;
        if (positionY + height > Mario.HEIGHT)
            positionY = Mario.HEIGHT - height;
    }
    /**
     * 是不是实体(墙、水管等是实体、金币;其他怪物不算实体、mario也不算，单独处理)
     * @return 
     */
    boolean isEntity(int pixelx,int pixely) throws Exception
    {
        int type=Mario.map[curMapId][pixelx][pixely];
        //地图块类型 目前：0为空气，1为墙，2为金币，3为死亡，待改，需要持续更新,更新完应改成enum类
        if(type==1)
            return true;
        if(type==0||type==2||type==3)
            return false;
        throw new Exception("isEntity:未知地图块类型" + type);
    }  
    /**
     * 检查实体碰撞
     * @param edge 检查哪个边缘 
     * @return
     * @throws Exception
     */
    boolean checkBounce(Edge edge) throws Exception
    {        
        positionCorrection();
        int pixelX = Mario.pixelate(positionX);//借用一下算像素的函数
        int pixelY = Mario.pixelate(positionY);
        int pixelXEnd=-1, pixelYEnd=-1, pixelXStart=-1, pixelYStart=-1;//检测范围
        int edgeLen = 1;//边缘的宽度    
        switch(edge)
        {
            case UPPER:
                if(positionY==0)
                    return true;
                pixelXEnd = pixelX + width / Mario.pixel;
                pixelYEnd = pixelY + edgeLen;
                pixelXStart = pixelX;
                pixelYStart = pixelY;
                break;
            case LOWER:
                if(positionY==Mario.HEIGHT-height)
                    return true;
                pixelXEnd = pixelX + width / Mario.pixel;
                pixelYEnd = pixelY + height;
                pixelXStart = pixelX;
                pixelYStart = pixelY + height/ Mario.pixel - edgeLen;
                break;
            case LEFT:
                if(positionX==0)
                    return true;
                pixelXEnd = pixelX + edgeLen;
                pixelYEnd = pixelY + height / Mario.pixel;
                pixelXStart = pixelX;
                pixelYStart = pixelY;
                break;
            case RIGHT:
                if(positionX==Mario.WIDTH-width)
                    return true;
                pixelXEnd = pixelX + width / Mario.pixel-edgeLen;
                pixelYEnd = pixelY + height / Mario.pixel;
                pixelXStart = pixelX+ width / Mario.pixel;
                pixelYStart = pixelY;
                break;           
            default:
                break;
        }
        for (int i = pixelXStart; i < pixelXEnd; i++)
            for (int j = pixelYStart; j < pixelYEnd; j++)
            {
                if (isEntity(i, j))
                    return true;
            }
        return false;
    }
};


/**
 * 怪物有三种状态，生存、遗留（有尸体）、死亡
 */
enum LifeState {
    ALIVE, REMAIN, DEAD;
}

/**
 * 四种边缘，上下左右
 */
enum Edge{
    UPPER, LOWER, LEFT, RIGHT;
}

class Mushroom extends Enemy {
    int step;//当前运动状态对应的贴图，0 or 1

    Mushroom(double x,double y,int mapId)
    {
        super(x, y, mapId);
        speedX = 0.2;
        speedY = 0;
    }
   {
       img = new Image[3];
       img[0] = Toolkit.getDefaultToolkit().getImage("src/image/num1.png");//待改 左脚迈步
       img[1] = Toolkit.getDefaultToolkit().getImage("src/image/num2.png");//待改 右脚迈步
       img[2]=Toolkit.getDefaultToolkit().getImage("src/image/num3.png");//待改 灰蘑菇
   }
    @Override
    public Image getCurImage() {
        if (lifeState == LifeState.DEAD)
            return defaultImg;
        if (lifeState == LifeState.REMAIN)
            return img[2];
        return img[step];
    }
    @Override
    void update(double dt, long curTime) {
        
        if (lifeState == LifeState.DEAD)
            return;
        if(lifeState==LifeState.REMAIN&&curTime-deathTime>=1000)//尸体停留超过1s了
        {
            lifeState = LifeState.DEAD;
            return;
        }
        //检查碰撞地图实体
        //修正速度：若左、右、上碰到实体，反弹，速度变为等大方向相反，若下碰到实体，Vy变为0，Vx不变
       //待完成

    }    
}
