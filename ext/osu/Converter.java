package cytus.ext.osu;

import cytus.*;
import java.io.*;

public class Converter {
	public static Beatmap patternToBeatmap(Pattern p) throws Exception {
		Beatmap b = new Beatmap();
		b.timing.add(new TimingPoint(p.offset, 120 / p.beat, 1));
		int lastpage = 32767;
		for (cytus.Note n : p.notes) {
			int x = (int) ((n.x - Pattern.PXOFF) * 512.0 / (double) Pattern.PWIDTH);
			int y = (int) ((n.y - Pattern.PYOFF) * 384.0 / (double) Pattern.PHEIGHT);
			int page = (int) ((n.stime + p.offset) / p.beat);
			if (n instanceof cytus.Hold) {
				cytus.Hold hold = (cytus.Hold) n;
				int y2 = (int) ((hold.y2 - Pattern.PYOFF) * 384.0 / (double) Pattern.PHEIGHT);
				b.notes.add(new Slider(x, y, n.stime, 1, n.etime - n.stime,
						new int[][] { { x, y }, { x, y2 } }));
			} else if (n instanceof cytus.Link) {
				cytus.Link link = (cytus.Link) n;
				for (cytus.Link.Node node : link.nodes) {
					int nx = (int) ((node.x - Pattern.PXOFF) * 512.0 / (double) Pattern.PWIDTH);
					b.notes.add(new Circle(nx, node.y, node.stime, false));
				}
			} else {
				if (lastpage < page) {
					lastpage = page;
					b.notes.add(new Circle(x, y, n.stime, true));
				} else
					b.notes.add(new Circle(x, y, n.stime, false));
			}
		}
		return b;
	}

	public static Pattern beatmapToPattern(Beatmap b, int offset)
			throws Exception {
		Pattern p = new Pattern();
		p.beat = 120.0 / b.timing.getFirst().bpm;
		p.offset = offset;
		for (Note n : b.notes) {
			int x = p.calcX(n.x / 512.0);
			int y = p.calcY(n.time);
			if (n instanceof Slider) {
				Slider s = (Slider) n;
				double delta = s.length / s.repeat;
				for (int i = 0; i <= s.repeat; i++) {
					int t = i % 2 == 0 ? 0 : s.nodes.length - 1;
					int nx = p.calcX(s.nodes[t][0] / 512.0);
					int ny = p.calcY(n.time + i * delta);
					p.notes.add(new cytus.Circle(p, nx, ny, n.time + i * delta));
				}
			} else
				p.notes.add(new cytus.Circle(p, x, y, n.time));
		}
		return p;
	}

	public static Beatmap formatBeatmap(Beatmap b) throws Exception {
		return patternToBeatmap(beatmapToPattern(b, 0));
	}

	public static void main(String args[]) throws Exception {
		if (args[2].equals("0")) {
			Pattern p = new Pattern(args[0], "hard");
			patternToBeatmap(p).write(new File(args[1]));
		} else {
			Beatmap b = new Beatmap(new File(args[0]));
			beatmapToPattern(b, 0).new Writer(
					new PrintWriter(new File(args[1])));
		}
		System.exit(0);
	}
}