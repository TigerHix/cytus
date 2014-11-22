package cytus.ext.osu;

public class Spinner extends Note {
	double etime = 0;

	public Spinner(double stime, double etime) {
		super(256, 192, stime, true);
		this.etime = etime;
	}
}