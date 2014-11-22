package cytus.ext.osu;

public class TimingPoint {
	double time = 0;
	double bpm = 0, mul = 1;

	public TimingPoint(double time, double bpm, double mul) {
		this.time = time;
		this.bpm = bpm;
		this.mul = mul;
	}

	public TimingPoint(String str) {
		String sub[] = str.split(",");
		time = Integer.parseInt(sub[0]);
		double t = Double.parseDouble(sub[1]);
		if (sub[6].equals("1"))
			bpm = 60000.0 / t;
		else
			mul = -100.0 / t;
	}
}