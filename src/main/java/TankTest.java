import java.awt.*;
import java.awt.event.*;
import java.util.List;
import java.util.ArrayList;

public class TankTest extends Frame {
	public static final int GAME_WIDTH = 1024;
	public static final int GAME_HEIGHT = 768;

	//private static Random r = new Random();

	Image offScreenImage = null;
	Tank myTank = new Tank(500,500,true,Tank.Direction.STOP,this);
	List<Missile> missiles = new ArrayList<Missile>();
	List<Explode> explodes = new ArrayList<Explode>();
	List<Tank> tanks = new ArrayList<Tank>();

	Wall w1 = new Wall(300,200,20,150,this),w2 = new Wall(500,100,300,20,this);

	Blood b = new Blood();
	public void update(Graphics g){
		if(offScreenImage == null){
			offScreenImage = this.createImage(GAME_WIDTH, GAME_HEIGHT);
		}
		Graphics gOffScreen = offScreenImage.getGraphics();
		Color c = gOffScreen.getColor();
		gOffScreen.setColor(Color.GREEN);
		gOffScreen.fillRect(0,0,GAME_WIDTH, GAME_HEIGHT);
		gOffScreen.setColor(c);
		print(gOffScreen);
		g.drawImage(offScreenImage,0,0,null);

	}

	public void paint(Graphics g){
		g.drawString("missiles count: "+ missiles.size(),10,50);
		g.drawString("explodes count: "+ explodes.size(),10,70);
		g.drawString("tanks    count: "+ tanks.size(),10,90);
		g.drawString("tanks     life: "+ myTank.getLife(),10,110);
		for(int i =0; i<missiles.size();i++){
			Missile m =missiles.get(i);
			m.hitTanks(tanks);
			m.hitTank(myTank);
			m.hitWall(w1);
			m.hitWall(w2);

			m.draw(g);
		}
		for (int i = 0; i < explodes.size(); i++) {
			Explode e = explodes.get(i);
			e.draw(g);
		}

		myTank.draw(g);
		myTank.eat(b);

		if(tanks.size()<=0) {
			for (int i = 0; i < 5; i++) {
				tanks.add(new Tank(50 + 40 * (i + 1), 50, false, Tank.Direction.D, this));
			}
		}
		for (int i = 0; i < tanks.size(); i++) {
			Tank t = tanks.get(i);
			t.collideWithWall(w1);
			t.collideWithWall(w2);
			t.collideWithTanks(tanks);
			t.draw(g);
		}
		w1.draw(g);
		w2.draw(g);
		b.draw(g);
	}

	public void lauchFrame(){
		for (int i = 0; i < 10; i++) {
			tanks.add(new Tank(50 +40*(i+1),50,false,Tank.Direction.D,this));
		}
		this.setLocation(400, 300);
		this.setSize(GAME_WIDTH,GAME_HEIGHT);
		this.setTitle("TankWar");
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
		this.setResizable(false);
		setVisible(true);
		this.addKeyListener(new KeyMonitor());

		new Thread(new PaintThread()).start();

	}

	public static void main(String[] args) {
		TankTest ts = new TankTest();
		ts.lauchFrame();
	}

	private class PaintThread implements Runnable{
		public void run(){
			while (true){
				repaint();
				try {
					Thread.sleep(50);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}

	private class KeyMonitor extends KeyAdapter{
		public void keyPressed(KeyEvent e){
			myTank.keyPressed(e);
		}

		public void keyReleased(KeyEvent e) {
			myTank.keyReleased(e);
		}
	}
}
