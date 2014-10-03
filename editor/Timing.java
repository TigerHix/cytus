package cytus.editor;

public class Timing {
	double beat = 0, offset = 0;

	public Timing(double beat, double offset) {
		this.beat = beat;
		this.offset = offset;
	}

	public double getBeat(double time, int div) {
		return (int) ((time - offset) / (beat / div)) * (beat / div) + offset;
	}

	public double getNearestBeat(double time, int div) {
		return Math.round((time - offset) / (beat / div)) * (beat / div)
				+ offset;
	}

	public boolean isBeat(double time, int div) {
		return isZero(Math.IEEEremainder(time - offset, beat / div));
	}

	public int getBeatType(double time, int div) {
		div = (int) log2(div);
		if (!isBeat(time, 1 << div))
			return -1;
		while (div > 0) {
			if (!isBeat(time, 1 << (div - 1)))
				return div;
			div--;
		}
		return 0;
	}

	public boolean equals(double d1, double d2) {
		return isZero(d1 - d2);
	}

	private boolean isZero(double d) {
		return Math.abs(d) < 1e-3;
	}

	private double log2(double x) {
		return Math.log(x) / Math.log(2);
	}
}