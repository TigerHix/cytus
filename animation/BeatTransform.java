package cytus.animation;

public class BeatTransform extends Transform {
	double beat = 0, offset = 0;
	double stime = 0;

	public BeatTransform(double stime, double offset, double beat) {
		this.stime = stime;
		this.offset = offset;
		this.beat = beat;
	}

	public void adjust(double time) {
		if (time < stime)
			return;

		double pos = ((time + offset) % beat) / beat;
		double size = 1 + 0.05 * Math.max(Math.cos(pos * Math.PI * 2), 0);
		s.scale(size, size);
	}
}
