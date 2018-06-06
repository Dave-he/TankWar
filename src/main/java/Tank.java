import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.List;
import java.util.Random;

public class Tank {
	public  static final int XSPEED = 5;
	public  static final int YSPEED = 5;

	public  static final int WIDTH = 30;
	public  static final int HEIGHT = 30;

	private int x,y;
	private int oldX,oldY;
	TankTest ts;
	private boolean bL = false,bU = false,bR = false,bD = false;
	private BloodBar bb = new BloodBar();

	public int getLife() {
		return life;
	}

	public void setLife(int life) {
		this.life = life;
	}

	private int life = 100;
	/**
	 * 随机数产生器产生方向
	 */
	private static Random r  = new Random();

	enum Direction{L,LU,U,RU,R,RD,D,LD,STOP}
	/**
	 * 炮筒方向和当前方向
	 */
	private Direction dir = Direction.STOP;
	private Direction ptDir = Direction.D;
	private int step = r.nextInt(12)+3;

	public boolean isGood() {
		return good;
	}

	public void setGood(boolean good) {
		this.good = good;
	}

	private boolean good;
	private boolean live = true;

	public boolean isLive() {
		return live;
	}

	public void setLive(boolean live) {
		this.live = live;
	}

	public Tank(int x, int y) {
		this.x = x;
		this.y = y;
		this.oldX =x;
		this.oldY = y;
	}

	public Tank(int x, int y, boolean good){
		this(x,y);
		this.good = good;
	}

	public Tank(int x, int y,boolean good,Direction dir,TankTest ts) {
		this(x,y,good);
		this.dir = dir;
		this.ts = ts;
	}

	public void draw(Graphics g){
		if(!live) return;

		Color c = g.getColor();
		if(good) g.setColor(Color.RED);
		else g.setColor(Color.BLUE);
		g.fillOval(x,y,30,30);
		g.setColor(c);
		if(good)
		bb.draw(g);
		switch(ptDir) {
			case L:
				g.drawLine(x + Tank.WIDTH/2, y + Tank.HEIGHT/2, x, y + Tank.HEIGHT/2);
				break;
			case LU:
				g.drawLine(x + Tank.WIDTH/2, y + Tank.HEIGHT/2, x, y);
				break;
			case U:
				g.drawLine(x + Tank.WIDTH/2, y + Tank.HEIGHT/2, x + Tank.WIDTH/2, y);
				break;
			case RU:
				g.drawLine(x + Tank.WIDTH/2, y + Tank.HEIGHT/2, x + Tank.WIDTH, y);
				break;
			case R:
				g.drawLine(x + Tank.WIDTH/2, y + Tank.HEIGHT/2, x + Tank.WIDTH, y + Tank.HEIGHT/2);
				break;
			case RD:
				g.drawLine(x + Tank.WIDTH/2, y + Tank.HEIGHT/2, x + Tank.WIDTH, y + Tank.HEIGHT);
				break;
			case D:
				g.drawLine(x + Tank.WIDTH/2, y + Tank.HEIGHT/2, x + Tank.WIDTH/2, y + Tank.HEIGHT);
				break;
			case LD:
				g.drawLine(x + Tank.WIDTH/2, y + Tank.HEIGHT/2, x, y + Tank.HEIGHT);
				break;
		}
		move();
	}

	/**
	 * 根据当前方向移动
	 */
	void move() {
		this.oldY = y;
		this.oldX = x;
		switch(dir) {
			//左
			case L:
				x -= XSPEED;
				break;
			//右
			case LU:
				x -= XSPEED;
				y -= YSPEED;
				break;
			//上
			case U:
				y -= YSPEED;
				break;
			case RU:
				x += XSPEED;
				y -= YSPEED;
				break;
			case R:
				x += XSPEED;
				break;
			case RD:
				x += XSPEED;
				y += YSPEED;
				break;
			case D:
				y += YSPEED;
				break;
			case LD:
				x -= XSPEED;
				y += YSPEED;
				break;
			case STOP:
				break;
		}
		if(this.dir != Direction.STOP){
			this.ptDir = dir;
		}
		if(x < 30 ) x = 30 ;
		if(y < 30 ) y = 30;
		if(x + Tank.WIDTH >TankTest.GAME_WIDTH) x = TankTest.GAME_WIDTH - Tank.WIDTH;
		if(y + Tank.HEIGHT > TankTest.GAME_HEIGHT) y = TankTest.GAME_HEIGHT  - Tank.HEIGHT;

		if(!good){
			Direction[] dirs = Direction.values();
			if(step == 0){
				step = r.nextInt(12)+3;

			int rn = r.nextInt(dirs.length);
			dir = dirs[rn];
			}
			step --;

			if(r.nextInt(40)>35)this.fire();
		}
		this.oldX = x;
		this.oldY = y;
	}

	private void stay(){
		x = oldX;
		y = oldY;
	}

	public void keyPressed(KeyEvent e) {
		int key = e.getKeyCode();
		switch(key) {
			case KeyEvent.VK_F2:
				if(!this.live){
					this.live = true;
					this.life = 100;
				}
				break;
			case KeyEvent.VK_CONTROL:
				fire();
				break;
			case KeyEvent.VK_LEFT :
				bL = true;
				break;
			case KeyEvent.VK_UP :
				bU = true;
				break;
			case KeyEvent.VK_RIGHT :
				bR = true;
				break;
			case KeyEvent.VK_DOWN :
				bD = true;
				break;
			case KeyEvent.VK_A:
				superFire();
				break;
		}
		locateDirection();
	}

	public void keyReleased(KeyEvent e) {
		int key = e.getKeyCode();
		switch(key) {
			case KeyEvent.VK_LEFT :
				bL = false;
				break;
			case KeyEvent.VK_UP :
				bU = false;
				break;
			case KeyEvent.VK_RIGHT :
				bR = false;
				break;
			case KeyEvent.VK_DOWN :
				bD = false;
				break;

		}
		locateDirection();
	}
	void locateDirection() {
		if(bL && !bU && !bR && !bD) dir = Direction.L;
		else if(bL && bU && !bR && !bD) dir = Direction.LU;
		else if(!bL && bU && !bR && !bD) dir = Direction.U;
		else if(!bL && bU && bR && !bD) dir = Direction.RU;
		else if(!bL && !bU && bR && !bD) dir = Direction.R;
		else if(!bL && !bU && bR && bD) dir = Direction.RD;
		else if(!bL && !bU && !bR && bD) dir = Direction.D;
		else if(bL && !bU && !bR && bD) dir = Direction.LD;
		else if(!bL && !bU && !bR && !bD) dir = Direction.STOP;
	}

	public Missile fire(){
		if(!live)return null;
		int x = this.x + Tank.WIDTH/2 - Missile.WIDTH/2;
		int y = this.y + Tank.HEIGHT/2 - Missile.HEIGHT/2;
		Missile m = new Missile(x,y,good,ptDir,this.ts);
		ts.missiles.add(m);
		return m;
	}
	public Missile fire(Direction dir){
		if(!live)return null;
		int x = this.x + Tank.WIDTH/2 - Missile.WIDTH/2;
		int y = this.y + Tank.HEIGHT/2 - Missile.HEIGHT/2;
		Missile m = new Missile(x,y,good,dir,this.ts);
		ts.missiles.add(m);
		return m;
	}

	public Rectangle getRect(){
		return new Rectangle(x, y, WIDTH, HEIGHT);
	}

	public boolean collideWithWall(Wall w){
		if(this.live && this.getRect().intersects(w.getRect())){
			this.stay();
			return true;
		}
		return false;
	}

	public boolean collideWithTanks(List<Tank> tanks) {
		for (int i = 0; i < tanks.size(); i++) {
			Tank t = tanks.get(i);
			if(this.live && this.getRect().intersects(t.getRect())){
				this.stay();
				t.stay();
				return true;
			}
		}
		return false;
	}

	private void superFire(){
		Direction[] dirs =  Direction.values();
		for (int i = 0; i < 8; i++) {
			fire(dirs[i]);
		}
	}

	private class BloodBar{
		public void draw(Graphics g){
			Color c = g.getColor();
			g.setColor(Color.RED);
			g.drawRect(x,y-10,WIDTH,10);
			int w = WIDTH * life/100;
			g.fillRect(x, y-10,w ,10);
			g.setColor(c);

		}
	}

	public boolean eat(Blood b){
		if(this.live && b.isLive()&& this.getRect().intersects(b.getRect())){
			this.life = 100;
			b.setLive(false);
			return true;
		}
		return false;
	}


}
