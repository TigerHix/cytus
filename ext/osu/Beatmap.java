package cytus.ext.osu;

import java.io.*;
import java.util.*;

public class Beatmap {
	public LinkedList<Note> notes = new LinkedList<Note>();
	public LinkedList<TimingPoint> timing = new LinkedList<TimingPoint>();
	int hp = 0, cs = 0, od = 0, ar = 0;
	double bpm = 120, sv = 1.4;

	public Beatmap() {
	}

	public Beatmap(File f) throws Exception {
		BufferedReader in = new BufferedReader(new FileReader(f));
		String str = in.readLine();
		while (!str.equals("[Difficulty]"))
			str = in.readLine();
		while (!str.equals("[Events]")) {
			str = in.readLine();
			String t[] = str.split(":");
			switch (t[0]) {
			case "HPDrainRate":
				hp = Integer.parseInt(t[1]);
				break;
			case "CircleSize":
				cs = Integer.parseInt(t[1]);
				break;
			case "OverallDifficulty":
				od = Integer.parseInt(t[1]);
				break;
			case "ApproachRate":
				ar = Integer.parseInt(t[1]);
				break;
			case "SliderMultiplier":
				sv = Double.parseDouble(t[1]);
				break;
			}
		}
		while (!str.equals("[TimingPoints]"))
			str = in.readLine();
		str = in.readLine();
		while (!str.equals("")) {
			TimingPoint tp = new TimingPoint(str);
			if (timing.size() > 0)
				if (tp.bpm == 0)
					tp.bpm = timing.getLast().bpm;
			timing.add(tp);
			str = in.readLine();
		}
		while (!str.equals("[HitObjects]"))
			str = in.readLine();
		str = in.readLine();
		while (str != null) {
			String t[] = str.split(",");
			int x = Integer.parseInt(t[0]);
			int y = Integer.parseInt(t[1]);
			double time = Integer.parseInt(t[2]) / 1000.0;
			int type = Integer.parseInt(t[3]);
			if ((type == 5) || (type == 6) || (type == 12))
				type -= 4;
			switch (type) {
			case 1:
				notes.add(new Circle(x, y, time, false));
				break;
			case 2:
				int repeat = Integer.parseInt(t[6]);
				double slen = Double.parseDouble(t[7]);
				TimingPoint tp = getTimingPoint(time);
				double length = (slen / sv / 100.0)
						* (60000.0 / tp.bpm / tp.mul) * repeat / 1000.0;
				t[5] = t[5].substring(t[5].lastIndexOf("|") + 1);
				String cord[] = t[5].split(":");
				int cx = Integer.parseInt(cord[0]),
				cy = Integer.parseInt(cord[1]);
				notes.add(new Slider(x, y, time, repeat, length, new int[][] {
						{ x, y }, { cx, cy } }));
				break;
			case 8:
				double etime = Integer.parseInt(t[5]) / 1000.0;
				notes.add(new Spinner(time, etime));
				break;
			}
			str = in.readLine();
		}
	}

	public TimingPoint getTimingPoint(double time) {
		if (time > timing.getLast().time)
			return timing.getLast();
		for (TimingPoint tp : timing)
			if (time > tp.time)
				return tp;
		return timing.getLast();
	}

	public void write(File f) throws Exception {
		PrintWriter out = new PrintWriter(f);
		for (Note n : notes) {
			out.print(n.x + "," + n.y + "," + (int) (n.time * 1000));
			if (n instanceof Slider) {
				Slider s = (Slider) n;
				out.print(",2,0,B");
				for (int i = 0; i < s.nodes.length; i++)
					out.print("|" + s.nodes[i][0] + ":" + s.nodes[i][1]);
				out.print("," + s.repeat + ",");
				TimingPoint tp = getTimingPoint(s.time);
				double slen = (s.length * 1000.0) / s.repeat
						/ (60000.0 / tp.bpm / tp.mul) * sv * 100.0;
				out.print(slen + ",2");
				for (int i = 1; i < s.nodes.length; i++)
					out.print("|0");
			} else
				out.print("," + (n.newcolor ? 5 : 1) + ",0");
			out.println();
		}
		out.close();
	}

	public static void main(String args[]) throws Exception {
		new Beatmap(new File(args[0]));
	}
}