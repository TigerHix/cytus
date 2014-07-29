package cytus;

import java.awt.*;

public abstract class Note {
	Pattern p = null;
	public int x = 0, y = 0;
	public double stime = 0, etime = 0, page = 0;

	public abstract void paint(Graphics2D g);

	public abstract void remove();

	public void recalc() {
		page = (int) ((stime + p.offset) / p.beat);
		y = (int) (((stime + p.offset) % p.beat / p.beat) * 352);
		if (page % 2 == 0)
			y = 352 - y;
		y += 64;
	}
}
