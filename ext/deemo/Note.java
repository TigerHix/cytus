package cytus.ext.deemo;

public class Note {
	int id = 0;
	double x = 0;
	double size = 0;
	double time = 0;
	boolean isnode = false;

	public Note(int id, double x, double size, double time) {
		this.id = id;
		this.x = x;
		this.size = size;
		this.time = time;
	}
}
