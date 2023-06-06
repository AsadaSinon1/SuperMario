package src.code;

import java.awt.*;

public class Enemy {
    int width, height;//怪物的宽度和高度，目前只支持矩形怪物
    //怪物的长宽应保持为pixel的倍数，这里不用pixelwidth是为了和Mario保持一致
    Map.Plot currPlot;//怪物所在的场景
    int value;//杀死怪物可以得到的分数
    double positionX, positionY, speedX, speedY;// X坐标，Y坐标，X轴速度，Y轴速度
    double deathTime = -1;//死亡时间
    boolean isfall=false;//是否处于掉落状态
    LifeState lifeState = LifeState.ALIVE;//生命状态
    final double DELTA_T = 0.1;//以0.1ms为单位，更新位置和速度
    final double G = 0.003;//重力加速度
    long walkingTimer=0;//走路计时器
    Image[] img;//怪物的所有贴图
    Image defaultImg = Toolkit.getDefaultToolkit().getImage("src/image/totalAlpha.png");//完全透明图像 
    /**
     * @param x x坐标
     * @param y y坐标
     * @param curr 所在场景标号
     */
    Enemy(double x, double y, Map.Plot curr)
    {
        positionX = x;
        positionY = y;
        currPlot = curr;
    }
    public double getPositionX() {
        return positionX;
    }

    public double getPositionY() {
        return positionY;
    }
    
    public int getWidth() {
        return width;
    }
    public int getHeight() {
        return height;
    }
    /**
     * @return 怪物当前状态对应的贴图
     */
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
        currPlot.mario.grade += value;//得分
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
     * @param pixelx 转化为pixel后的x坐标（即positionX/pixel）
     * @param pixely 转化为pixel后的y坐标
     * @return 该坐标是不是实体(墙、水管等是实体;其他怪物、马里奥不算实体)
     * @throws Exception
     */
    boolean isEntity(int pixelx,int pixely) throws Exception
    {
        int type = currPlot.mario.map[pixelx][pixely];
        //地图块类型 目前：0为空气，1为墙，2为金币，3为死亡，待改，需要持续更新,更新完应改成enum类
        if (type == 1)
            return true;
        if (type == 0 || type == 2 || type == 3)
            return false;
        throw new Exception("isEntity:未知地图块类型" + type);
    }
    
    /**
     * @param pixelx 转化为pixel后的x坐标（即positionX/pixel）
     * @param pixely 转化为pixel后的y坐标
     * @return 马里奥当前位置是不是覆盖该坐标
     */
    boolean isMario(int pixelx,int pixely)
    {
        int xStart = Mario.pixelate(currPlot.mario.getX()) / Mario.pixel;
        int xEnd = xStart + currPlot.mario.width/ Mario.pixel;
        int yStart = Mario.pixelate(currPlot.mario.getY()) / Mario.pixel;
        int yEnd = yStart + currPlot.mario.height/ Mario.pixel;
        return (xStart <= pixelx && pixelx < xEnd && yStart <= pixely && pixely < yEnd);
    }

    
    /**
     * @param edge 检测哪个边缘
     * @return 是否碰撞地图实体、地图边界
     */
    boolean checkBounce(Edge edge)
    {
        positionCorrection();

        int pixelX = Mario.pixelate(positionX) / Mario.pixel;//借用一下算像素的函数
        int pixelY = Mario.pixelate(positionY) / Mario.pixel;
        int pixelH = height / Mario.pixel;
        int pixelW = width / Mario.pixel;
        int pixelXEnd = -1, pixelYEnd = -1, pixelXStart = -1, pixelYStart = -1;//检测范围
        int edgeLen = 1;//边缘的宽度    
        switch (edge)//尽量不要改这里的代码
        {
            case UPPER:
                if (pixelY == 0)//触碰地图边界
                    return true;
                pixelXEnd = pixelX + pixelW;
                pixelYEnd = pixelY;
                pixelXStart = pixelX;
                pixelYStart = pixelY-edgeLen;
                break;
            case LOWER:
                if (pixelY >= Mario.HEIGHT / Mario.pixel - pixelH)
                    return true;
                pixelXEnd = pixelX + pixelW;
                pixelYEnd = pixelY + pixelH + edgeLen;
                pixelXStart = pixelX;
                pixelYStart = pixelY + pixelH;
                break;
            case LEFT:
                if (pixelX == 0)
                    return true;
                pixelXEnd = pixelX;
                pixelYEnd = pixelY + pixelH;
                pixelXStart = pixelX - edgeLen;
                pixelYStart = pixelY;
                break;
            case RIGHT:
                if (pixelX >= Mario.WIDTH / Mario.pixel - pixelW)
                    return true;
                pixelXEnd = pixelX + pixelW + edgeLen;
                pixelYEnd = pixelY + pixelH;
                pixelXStart = pixelX + pixelW;
                pixelYStart = pixelY;
                break;
            default:
                break;
        }
        for (int i = pixelXStart; i < pixelXEnd; i++)
            for (int j = pixelYStart; j < pixelYEnd; j++) {

                try {
                    if (isEntity(i, j)) {
                        return true;
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        return false;
    }

    /**
     * @param edge 检测哪个边缘
     * @return 是否碰撞马里奥
     */
    boolean checkBounceMario(Edge edge)
    {
        positionCorrection();
        int pixelX = Mario.pixelate(positionX) / Mario.pixel;//借用一下算像素的函数
        int pixelY = Mario.pixelate(positionY) / Mario.pixel;
        int pixelH = height / Mario.pixel;
        int pixelW = width / Mario.pixel;
        int pixelXEnd = -1, pixelYEnd = -1, pixelXStart = -1, pixelYStart = -1;//检测范围
        int edgeLen = 1;//边缘的宽度    
        switch (edge)//尽量不要改这里的代码
        {
            case UPPER:            
                pixelXEnd = pixelX + pixelW;
                pixelYEnd = pixelY;
                pixelXStart = pixelX;
                pixelYStart = pixelY - edgeLen;
                break;
            case LOWER:               
                pixelXEnd = pixelX + pixelW;
                pixelYEnd = pixelY + pixelH + edgeLen;
                pixelXStart = pixelX;
                pixelYStart = pixelY + pixelH;
                break;
            case LEFT:        
                pixelXEnd = pixelX;
                pixelYEnd = pixelY + pixelH;
                pixelXStart = pixelX - edgeLen;
                pixelYStart = pixelY;
                break;
            case RIGHT:
                pixelXEnd = pixelX + pixelW + edgeLen;
                pixelYEnd = pixelY + pixelH;
                pixelXStart = pixelX + pixelW;
                pixelYStart = pixelY;
                break;
            default:
                break;
        }
        for (int i = pixelXStart; i < pixelXEnd; i++)
            for (int j = pixelYStart; j < pixelYEnd; j++) {

                try {
                    if (isMario(i, j)) {
                        return true;
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

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
    final double MUSHROOM_SPEED = 0.2;//蘑菇的走路速度
    final int MUSHROOM_VALUE = 1000;//蘑菇的价值分数
    final int REMAIN_TIME = 2000;//蘑菇的尸体保留时间
    final int STEP_TIME = 500;//走一步需要的时间，即贴图切换时间
    final int MUSHROOM_HEIGHT = 30;//蘑菇的高度
    final int MUSHROOM_WIDTH = 30;//蘑菇的宽度
    
    Mushroom(double x, double y, Map.Plot curr)
    {
        super(x, y, curr);
        speedX = MUSHROOM_SPEED;
        speedY = 0;
        value = MUSHROOM_VALUE;
        height = MUSHROOM_HEIGHT;
        width = MUSHROOM_WIDTH;
        img = new Image[3];
        img[0] = Toolkit.getDefaultToolkit().getImage("src/image/mushroom0.png");//左脚迈步
        img[1] = Toolkit.getDefaultToolkit().getImage("src/image/mushroom1.png");//右脚迈步
        img[2] = Toolkit.getDefaultToolkit().getImage("src/image/mushroomRemain.png");//灰蘑菇
        step = 0;
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
        
        //判断状态
        if (lifeState == LifeState.DEAD)
            return;
        if(lifeState==LifeState.REMAIN)//尸体停留超过2s了
        {
            if (curTime - deathTime >= REMAIN_TIME)
            {
                lifeState = LifeState.DEAD;
            }          
            return;
        }

        boolean killMario = false;
        //若存活
        //检查碰撞地图实体
        //修正速度：若左、右、上碰到实体，反弹，速度变为等大方向相反，若下碰到实体，Vy变为0，Vx不变       
        for (double t = 0; t < dt; t += DELTA_T)
        {
            if (checkBounceMario(Edge.UPPER)&&currPlot.mario.getVy()<0&&!currPlot.mario.isDash())//上碰撞马里奥（有向下速度的），enemy死亡
            {
                try {
                    death();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                currPlot.mario.setStep(true);
                //Mario应该被反弹起来，不然看着很奇怪
                currPlot.mario.setVy(currPlot.mario.jumpSpeed);//注意这里是正的，正速度才是向上跳
                return;
            }

            //与马里奥碰撞
            if (checkBounceMario(Edge.LEFT)||checkBounceMario(Edge.LOWER)||checkBounceMario(Edge.RIGHT))
                killMario = true;
            if (checkBounce(Edge.LOWER))//下碰撞
            {
                speedY = 0;
                isfall = false;
            } else {
                speedY += G * DELTA_T;
                isfall = true;
            }
            
            if (speedY<0&&checkBounce(Edge.UPPER))//上碰撞
            {
                speedY = -speedY;
            }
            positionY = positionY + DELTA_T * speedY;

            if (((speedX<0)&&checkBounce(Edge.LEFT)) || ((speedX>0)&&checkBounce(Edge.RIGHT)))//左右碰撞
            {
                speedX = -speedX;
            }
            positionX = positionX + DELTA_T * speedX;
        }

        if (killMario)
            currPlot.mario.killed();
        //更新step 
        if (walkingTimer >= STEP_TIME)
            step = 1;
        else
            step = 0;
        walkingTimer = (walkingTimer + (long)dt) % (STEP_TIME*2);//这里强制类型转换没问题但是不美观
        //dt改成long类型会好一些，这里为了和Mario保持一致
    }    
}

class Turtle extends Enemy {
    int step;//当前运动状态对应的贴图顺序，0 or 1
    final double TURTLE_RIGHT_SPEED = 0.15;//乌龟向右走路速度
    final double TURTLE_LEFT_SPEED = -0.35;//乌龟向左走路速度
    final int TURTLE_VALUE = 2000;//乌龟的价值分数
    final int REMAIN_TIME = 3000;//乌龟的尸体保留时间
    final int STEP_TIME = 500;//走一步需要的时间，即贴图切换时间
    final int TURTLE_HEIGHT = 50;//乌龟的高度
    final int TURTLE_WIDTH = 30;//乌龟的宽度
    
    Turtle(double x, double y, Map.Plot curr)
    {
        super(x, y, curr);
        speedX = TURTLE_RIGHT_SPEED;
        speedY = 0;
        value = TURTLE_VALUE;
        height = TURTLE_HEIGHT;
        width = TURTLE_WIDTH;
        img = new Image[5];
        img[0] = Toolkit.getDefaultToolkit().getImage("src/image/turtleLeft0.png");
        img[1] = Toolkit.getDefaultToolkit().getImage("src/image/turtleLeft1.png");
        img[2] = Toolkit.getDefaultToolkit().getImage("src/image/turtleRight0.png");
        img[3] = Toolkit.getDefaultToolkit().getImage("src/image/turtleRight1.png");
        img[4] = Toolkit.getDefaultToolkit().getImage("src/image/turtleRemain.png");//Remain状态 龟壳
        step = 0;
    }
    @Override
    public Image getCurImage() {
        if (lifeState == LifeState.DEAD)
            return defaultImg;
        if (lifeState == LifeState.REMAIN)
            return img[4];
        int index = step;
        if(speedX>0)//朝右边
            index += 2;
        return img[index];
    }
    @Override
    void update(double dt, long curTime) {
        
        //判断状态
        if (lifeState == LifeState.DEAD)
            return;
        if(lifeState==LifeState.REMAIN)//尸体停留超过时间了
        {
            if (curTime - deathTime >= REMAIN_TIME)
            {
                lifeState = LifeState.DEAD;
            }          
            return;
        }

        boolean killMario = false;
        //若存活
        //检查碰撞地图实体
        //修正速度：若左、右、上碰到实体，反弹，速度变为等大方向相反，若下碰到实体，Vy变为0，Vx不变       
        for (double t = 0; t < dt; t += DELTA_T)
        {
            if (checkBounceMario(Edge.UPPER)&&currPlot.mario.getVy()<0&&!currPlot.mario.isDash())//上碰撞马里奥（有向下速度的），enemy死亡
            {
                try {
                    death();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                currPlot.mario.setStep(true);
                //Mario应该被反弹起来，不然看着很奇怪
                currPlot.mario.setVy(currPlot.mario.jumpSpeed);//注意这里是正的，正速度才是向上跳
                return;
            }

            //与马里奥碰撞
            if (checkBounceMario(Edge.LEFT)||checkBounceMario(Edge.LOWER)||checkBounceMario(Edge.RIGHT))
                killMario = true;
            if (checkBounce(Edge.LOWER))//下碰撞
            {
                speedY = 0;
                isfall = false;
            } else {
                speedY += G * DELTA_T;
                isfall = true;
            }
            
            if (speedY<0&&checkBounce(Edge.UPPER))//上碰撞
            {
                speedY = -speedY;
            }
            positionY = positionY + DELTA_T * speedY;

            if ((speedX<0)&&checkBounce(Edge.LEFT))//左碰撞
            {
                speedX = TURTLE_RIGHT_SPEED;//往右低速
            }
            if ((speedX > 0) && checkBounce(Edge.RIGHT))//右碰撞
            {
                speedX = TURTLE_LEFT_SPEED;//往左高速
            }
            positionX = positionX + DELTA_T * speedX;
        }

        if (killMario)
            currPlot.mario.killed();
        //更新step 
        if (walkingTimer >= STEP_TIME)
            step = 1;
        else
            step = 0;
        walkingTimer = (walkingTimer + (long)dt) % (STEP_TIME*2);//这里强制类型转换没问题但是不美观
        //dt改成long类型会好一些，这里为了和Mario保持一致
    }    
}