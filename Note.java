package cytus;

import java.awt.*;

public abstract class Note implements Comparable<Note> {
	PatternPlayer p = null;
	public int id = 0;
	public int x = 0, y = 0;
	public double stime = 0, etime = 0, judgetime = -1;
	public int page = 0;
	public int judgement = -1;

	public abstract void paint(Graphics2D g);

	public abstract void judge(double time);

	public int compareTo(Note note) {
		return Double.compare(stime, note.stime);
	}
}
