package cytus.animation;

public class LoopAlphaTransform extends Transform {
	double stime = 0, dur = 0;
	double st = 0, ed = 0;

	public LoopAlphaTransform(double stime, double dur, double st, double ed) {
		this.stime = stime;
		this.dur = dur;
		this.st = st;
		this.ed = ed;
	}

	public void adjust(double time) {
		if (time < stime)
			return;

		double pos = (time - stime) % dur / dur;
		double alpha = st + (ed - st) * pos;
		s.setAlpha(alpha);
	}
}