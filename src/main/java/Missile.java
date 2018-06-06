
import java.awt.*;
import java.util.List;

public class Missile {
	private static final int XSPEED = 10;
	private static final int YSPEED = 10;

	public  static final int WIDTH = 10;
	public  static final int HEIGHT = 10;
	private boolean good = true;

	private TankTest ts;

	int x,y;
	Tank.Direction dir;

	private boolean live = true;
	public boolean isLive() {
		return live;
	}

	public void setLive(boolean live) {
		this.live = live;
	}

	public Missile(int x, int y, Tank.Direction dir) {
		this.x = x;
		this.y = y;
		this.dir = dir;
	}
	public Missile(int x, int y,boolean good, Tank.Direction dir, TankTest tc) {
		this(x,y,dir);
		this.ts = tc;
		this.good = good;
	}

	public void draw(Graphics g){
		if(!live) {
			ts.missiles.remove(this);
			return;
		};
		Color c = g.getColor();
		g.setColor(Color.BLACK);
		g.fillOval(x,y,WIDTH,HEIGHT);
		g.setColor(c);

		move();
	}

	private void move() {
		switch(dir) {
			case L:
				x -= XSPEED;
				break;
			case LU:
				x -= XSPEED;
				y -= YSPEED;
				break;
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

		/**
		 * 出界
		 */
		if(x < 0 || y < 0 || x > TankTest.GAME_WIDTH || y > TankTest.GAME_HEIGHT){
			live = false;
			ts.missiles.remove(this);
		}
	}

	public Rectangle getRect(){
		return new Rectangle(x, y, WIDTH, HEIGHT);
	}

	public boolean hitTank(Tank t){
		if(this.getRect().intersects(t.getRect())&&t.isLive() && this.good != t.isGood()){
			if(t.isGood()){
				t.setLife(t.getLife() -20);
				if(t.getLife()<=0)t.setLive(false);
			}else{
				t.setLive(false);
			}
			this.live = false;
			Explode e = new Explode(x,y,ts);
			ts.explodes.add(e);
			return true;
		}
		return false;
	}

	public boolean hitTanks(List<Tank> tanks){
		for (int i = 0; i < tanks.size(); i++) {
			if(hitTank(tanks.get(i))){
				return true;
			}
		}
		return false;
	}

	public boolean hitWall(Wall w){
		if(this.live && this.getRect().intersects(w.getRect())){
			this.live = false;
			return true;
		}
		return false;
	}



}
