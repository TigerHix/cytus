package cytus.ext.osu;

import cytus.*;
import java.io.*;

public class Converter {
	public static Beatmap noteChartToBeatmap(NoteChart p) throws Exception {
		Beatmap b = new Beatmap();
		b.timing.add(new TimingPoint(p.pshift, 120 / p.beat, 1));
		int lastpage = 32767;
		for (cytus.NoteChart.Note n : p.notes) {
			if (n.linkref != -1)
				continue;
			int x = (int) (n.x * 512.0);
			int y = (int) (n.calcY(n.time) * 384.0);
			int page = n.calcPage(n.time);
			if (n.holdtime != 0) {
				int y2 = (int) (n.calcY(n.time + n.holdtime) * 384.0);
				b.notes.add(new Slider(x, y, n.time, 1, n.holdtime,
						new int[][] { { x, y }, { x, y2 } }));
			} else {
				if (lastpage < page) {
					lastpage = page;
					b.notes.add(new Circle(x, y, n.time, true));
				} else
					b.notes.add(new Circle(x, y, n.time, false));
			}
		}
		for (cytus.NoteChart.Link l : p.links) {
			for (cytus.NoteChart.Note node : l.nodes) {
				int x = (int) (node.x * 512.0);
				int y = (int) (node.calcY(node.time) * 384.0);
				b.notes.add(new Circle(x, y, node.time, false));
			}
		}
		return b;
	}

	public static NoteChart beatmapToNoteChart(Beatmap b, int pshift)
			throws Exception {
		NoteChart p = new NoteChart(120.0 / b.timing.getFirst().bpm, pshift);
		int count = 0;
		for (Note n : b.notes) {
			double x = n.x / 512.0;
			if (n instanceof Slider) {
				Slider s = (Slider) n;
				double delta = s.length / s.repeat;
				for (int i = 0; i <= s.repeat; i++) {
					int t = i % 2 == 0 ? 0 : s.nodes.length - 1;
					x = s.nodes[t][0] / 512.0;
					p.notes.add(p.new Note(count++, n.time + i * delta, x, 0));
				}
			} else
				p.notes.add(p.new Note(count++, n.time, x, 0));
		}
		return p;
	}

	public static Beatmap formatBeatmap(Beatmap b) throws Exception {
		return noteChartToBeatmap(beatmapToNoteChart(b, 0));
	}
}