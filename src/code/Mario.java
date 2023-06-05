package src.code;

public class Mario{
    // 屏幕宽度、屏幕高度、像素大小
    final static int WIDTH = Map.Plot.PlotInfo.screenWidth, HEIGHT = Map.Plot.PlotInfo.screenHeight, pixel = Map.Plot.PlotInfo.pixel;
    // 血量、分数
    int HP, grade;
    //走路速度、起跳速度、重力加速度、摩擦系数(值越小效果越明显)
    final double walkSpeed, jumpSpeed, g, mu;
    // 人物宽度、人物高度、边缘容错度、冲刺时间
    final int width, height;
    final int delay, dashDelay;
    // 是否有冲刺、蹬墙跳、抓墙能力
    final boolean canDash, canWallJump, canGrabWall;
    // 地图（0为空气，1为墙，2为出口，3为死亡）
    int[][] map = new int[WIDTH/pixel+10][HEIGHT/pixel+10];

    //人物x,y坐标、水平速度、垂直速度
    private double x,y,vx,vy;
    //上一帧跑步状态（1，2，3，4）
    private int lastState;
    //是否按A、是否按D、是否离地、是否按space、是否死亡、是否面朝右、是否能冲刺、是否在冲刺、是否踩头
    private boolean left,right,fall,jump,death,faceRight,dashAble,dash,step;
    //左（右）侧是否有墙、是否触发左（右）蹬墙跳
    private boolean wallLeft,wallRight,wallLeftJump,wallRightJump;
    //粘在左（右）墙的时刻、跳跃时刻、在地面时刻、冲刺时刻
    private long timeLeft,timeRight,timeJump,timeOnGround,timeDash;

    Mario(Map.MapInfo info){
        HP = info.upperHP;
        grade = info.grade;
        walkSpeed = info.walkSpeed;
        jumpSpeed = info.jumpSpeed;
        g = info.g;
        mu = info.mu;
        width = info.width;
        height = info.height;
        delay = info.delay;
        dashDelay = info.dashDelay;
        canDash = info.canDash;
        canGrabWall = info.canGrabWall;
        canWallJump = info.canWallJump;
    }

    void killed(){
        HP--;
        death = true;
    }
    //设计地图函数，可修改
    void initMap(int [][]map){
        this.map = map;
    }

    // 查找模型朝向
    public String findDirection(){
        if(fall&&wallLeft&&left&&canGrabWall){
            lastState=1;
            return "SlideLeft";
        }
        if(fall&&wallRight&&right&&canGrabWall){
            lastState=1;
            return "SlideRight";
        }
        if(fall&&(jump||step)&&vx>0){
            lastState=1;
            return "JumpRight";
        }
        if(fall&&(jump||step)&&vx<0){
            lastState=1;
            return "JumpLeft";
        }
        if(fall&&(jump||step)&&vx==0){
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
        death = dash = left = right = jump = step = false;
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
    boolean check(double curx,double cury,int num) throws MyException.NextMap {
        int px = pixelate(curx)/pixel,py = pixelate(cury)/pixel;
        if(px<0)px = 0;
        if(py<0)py = 0;
        for(int i = 0;i<width/pixel;i++)
            for(int j = 0;j<height/pixel;j++){
                int val = map[px+i][py+j];
                if(val!=num)continue;
                if(num==2) throw new MyException.NextMap();
                return true;
            }
        return false;
    }
    //更新每一帧所有状态（尽量不要修改）
    void update(double dt,long curTime) throws MyException.Death, MyException.NextPlot, MyException.NextMap {
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
        if(vy<=0)step = false;
        //通关、死亡检测
        check(x,y,2);
        if(check(x,y,3)) killed();
        if(death) throw new MyException.Death();
        //地图边缘检测
        if(x<0)x = 0;
        if(y<0)y = 0;
        // 进入下一场景
        if(x>WIDTH-width) throw new MyException.NextPlot();
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
            double rate = onWall&&canGrabWall?mu:1.0;
            if(vy>0)vy-=dt*g*(jump?0.5:(step?1.0:3.0))*(onWall?1.5:1.0);
            else vy-=dt*g*rate;
            if(vy<-jumpSpeed*rate)vy = -jumpSpeed*rate;
        }else{
            timeOnGround = curTime;
            if(canDash) dashAble = true;
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
//        if(step)System.out.println(Math.random());
    }


    public double getY() {
        return y;
    }
    public void setLeft(boolean left) {
        this.left = left;
    }
    public double getVx() {
        return vx;
    }
    public void setVx(double vx) {
        this.vx = vx;
    }
    public void setRight(boolean right) {
        this.right = right;
    }
    public void setFaceRight(boolean faceRight) {
        this.faceRight = faceRight;
    }
    public boolean isJump() {
        return jump;
    }
    public void setJump(boolean jump) {
        this.jump = jump;
    }
    public long getTimeJump() {
        return timeJump;
    }
    public void setTimeJump(long timeJump) {
        this.timeJump = timeJump;
    }
    public boolean isStep(){
        return this.step;
    }
    public void setStep(boolean step){
        this.step = step;
    }
    public boolean isFall() {
        return fall;
    }
    public void setFall(boolean fall) {
        this.fall = fall;
    }
    public long getTimeOnGround() {
        return timeOnGround;
    }
    public void setVy(double vy) {
        this.vy = vy;
    }
    public boolean isDashAble() {
        return dashAble;
    }
    public void setDashAble(boolean dashAble) {
        this.dashAble = dashAble;
    }
    public void setDash(boolean dash) {
        this.dash = dash;
    }
    public boolean isDash() {
        return dash;
    }
    public void setTimeDash(long timeDash) {
        this.timeDash = timeDash;
    }
    public boolean isRight() {
        return right;
    }
    public boolean isLeft() {
        return left;
    }
    public void setWallLeftJump(boolean wallLeftJump) {
        this.wallLeftJump = wallLeftJump;
    }
    public void setWallRightJump(boolean wallRightJump) {
        this.wallRightJump = wallRightJump;
    }
    public double getX() {
        return x;
    }

    public boolean isDeath() {
        return death;
    }

    public double getVy() {
        return vy;
    }
}
