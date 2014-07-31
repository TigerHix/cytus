package cytus.ext.osu;

public class Note {
	int x = 0, y = 0;
	double time = 0;
	boolean newcolor = false;

	public Note(int x, int y, double time, boolean newcolor) {
		this.x = x;
		this.y = y;
		this.time = time;
		this.newcolor = newcolor;
	}
}
