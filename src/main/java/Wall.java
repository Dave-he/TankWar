import java.awt.*;

public class Wall {
	int x,y,w,h;
	TankTest tc;

	public Wall(int x, int y, int w, int h, TankTest tc){
		this.x =x;
		this.y = y;
		this.w = w;
		this.h = h;
		this.tc = tc;
	}

	public void draw(Graphics g){
		g.fillRect(x,y,w,h);
	}

	public Rectangle getRect(){
		return new Rectangle(x,y,100,100);
	}
}
