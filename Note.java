package cytus;

import java.awt.*;

public abstract class Note {
	Pattern p = null;
	public int id = 0;
	public int x = 0, y = 0;
	public double stime = 0, etime = 0;
	public int page = 0;

	public abstract void paint(Graphics2D g);

	public abstract void remove();

	public void recalc() {
		page = p.calcPage(stime);
		y = p.calcY(stime);
	}
}
