package cytus.animation;

public class RotateTransform extends Transform {
	double stime = 0, etime = 0;
	double st = 0, ed = 0;

	public RotateTransform(double stime, double etime, double st, double ed) {
		this.stime = stime;
		this.etime = etime;
		this.st = st;
		this.ed = ed;
	}

	public void adjust(double time) {
		if (time < stime) {
			s.rotate(st);
			return;
		}

		if (time > etime) {
			s.rotate(ed);
			return;
		}

		double pos = (time - stime) / (etime - stime);
		double angle = st + (ed - st) * pos;
		s.rotate(angle);
	}
}