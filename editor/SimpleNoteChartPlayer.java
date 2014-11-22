package cytus.editor;

import cytus.*;
import java.awt.*;
import java.awt.image.*;
import java.text.*;
import javax.swing.*;
import javax.media.*;
import java.util.*;
import java.io.*;

public class SimpleNoteChartPlayer {
	NoteChart pdata = null;
	LinkedList<NoteChart.Note> selection = new LinkedList<NoteChart.Note>();
	BufferedImage buf = new BufferedImage(720, 480, BufferedImage.TYPE_INT_ARGB);
	Graphics2D g = (Graphics2D) buf.getGraphics();
	Player player = null; // MediaPlayer

	int WIDTH = 675, HEIGHT = 375, XOFF = 22, YOFF = 52;

	public SimpleNoteChartPlayer(NoteChart pdata, Player player) {
		this.pdata = pdata;
		this.player = player;
		g.setStroke(new BasicStroke(5));
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
	}

	public int calcPage(double time) {
		return (int) ((time + pdata.pshift) / pdata.beat);
	}

	public int calcX(double x) {
		return (int) (x * WIDTH + XOFF);
	}

	public int calcY(double time) {
		int cpage = calcPage(time);
		double y = (time + pdata.pshift) / pdata.beat - cpage;
		if (cpage % 2 == 0)
			y = 1 - y;
		return (int) (y * HEIGHT + YOFF);
	}

	public void paint() {
		double time = player.getMediaTime().getSeconds();
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, 720, 480);

		int CSIZE = 72, NSIZE = 36, TSIZE = 36;
		for (int i = 0; i < pdata.links.size(); i++) {
			NoteChart.Link l = pdata.links.get(i);
			if ((l.nodes.getFirst().time <= time + pdata.beat)
					&& (time <= l.nodes.getLast().time)) {
				int last = -1;
				for (int j = 0; j < l.n; j++) {
					NoteChart.Note node = l.nodes.get(j);
					int x = calcX(node.x);
					int y = calcY(node.time);
					if ((time + pdata.beat > node.time) && (time < node.time)) {
						if (selection.contains(node)) {
							g.setColor(new Color(255, 0, 0, 100));
							g.fillOval(x - 20, y - 20, 40, 40);
						}
						if (time <= node.time - pdata.beat / 2) {
							double alpha = (time - (node.time - pdata.beat))
									/ (pdata.beat / 2);
							g.setComposite(AlphaComposite.SrcOver
									.derive((float) alpha));
						} else
							g.setComposite(AlphaComposite.SrcOver);
						if (j < l.n - 1) {
							g.setColor(Color.BLACK);
							int nextx = calcX(l.nodes.get(j + 1).x);
							int nexty = calcY(l.nodes.get(j + 1).time);
							g.drawLine(x, y, nextx, nexty);
						}
						g.setColor(new Color(255, 255, 0, 100));
						g.fillOval(x - NSIZE / 2, y - NSIZE / 2, NSIZE, NSIZE);
						g.setColor(Color.RED);
						g.drawString(String.valueOf(node.id), x, y);
					}
					if (time >= node.time)
						last = j;
				}
				int cx = calcX(l.nodes.getFirst().x);
				int cy = calcY(l.nodes.getFirst().time);
				if (last != -1) {
					if (last == l.n - 1)
						last--;
					NoteChart.Note node = l.nodes.get(last);
					NoteChart.Note next = l.nodes.get(last + 1);
					int x = calcX(node.x);
					int y = calcY(node.time);
					int nextx = calcX(next.x);
					int nexty = calcY(next.time);
					double pos = (time - node.time) / (next.time - node.time);
					cx = (int) ((nextx - x) * pos + x);
					cy = (int) ((nexty - y) * pos + y);
				}
				g.setComposite(AlphaComposite.SrcOver);
				g.setColor(new Color(0, 255, 0, 100));
				g.fillOval(cx - CSIZE / 2, cy - CSIZE / 2, CSIZE, CSIZE);
				g.setColor(new Color(255, 255, 0, 100));
				g.drawOval(cx - CSIZE / 2, cy - CSIZE / 2, CSIZE, CSIZE);
			}
		}
		int start = -1, end = -1;
		for (int i = 0; i < pdata.notes.size(); i++) {
			NoteChart.Note note = pdata.notes.get(i);
			if ((note.time <= time + pdata.beat)
					&& (time <= note.time + note.holdtime)) {
				if (start == -1)
					start = i;
				end = i;
			}
		}
		if (end != -1) {
			for (int i = end; i >= start; i--) {
				NoteChart.Note note = pdata.notes.get(i);
				if (note.linkref != -1)
					continue;
				int x = calcX(note.x);
				int y = calcY(note.time);
				int y2 = 0;
				if (time + pdata.beat <= note.time + note.holdtime)
					y2 = calcY(time + pdata.beat);
				else
					y2 = calcY(note.time + note.holdtime);
				if (selection.contains(note)) {
					g.setColor(new Color(255, 0, 0, 100));
					g.fillOval(x - 40, y - 40, 80, 80);
				}
				if (time <= note.time - pdata.beat / 2) {
					double alpha = (time - (note.time - pdata.beat))
							/ (pdata.beat / 2);
					g.setComposite(AlphaComposite.SrcOver.derive((float) alpha));
				} else
					g.setComposite(AlphaComposite.SrcOver);
				if (note.holdtime == 0) { // Circle
					g.setColor(new Color(0, 0, 0, 100));
					g.fillOval(x - CSIZE / 2, y - CSIZE / 2, CSIZE, CSIZE);
					g.setColor(note.page % 2 == 0 ? Color.MAGENTA : Color.GREEN);
					g.drawOval(x - CSIZE / 2, y - CSIZE / 2, CSIZE, CSIZE);
				} else { // Hold
					g.setColor(Color.YELLOW);
					if (y < y2)
						g.fillRect(x - TSIZE / 2, y, TSIZE, y2 - y);
					else
						g.fillRect(x - TSIZE / 2, y2, TSIZE, y - y2);
					if (time > note.time) {
						int pos = (int) ((y2 - y) * (time - note.time) / note.holdtime)
								+ y;
						g.setColor(Color.CYAN);
						if (y < pos)
							g.fillRect(x - TSIZE / 2, y, TSIZE, pos - y);
						else
							g.fillRect(x - TSIZE / 2, pos, TSIZE, y - pos);
					}
					g.setColor(Color.YELLOW);
					g.fillOval(x - CSIZE / 2, y - CSIZE / 2, CSIZE, CSIZE);
					g.setColor(new Color(0, 0, 0, 100));
					g.drawOval(x - CSIZE / 2, y - CSIZE / 2, CSIZE, CSIZE);
				}
				g.setColor(Color.RED);
				g.drawString(String.valueOf(note.id), x, y);
			}
		}
		int liney = calcY(time);
		g.setComposite(AlphaComposite.SrcOver);
		g.setColor(Color.BLACK);
		g.drawLine(0, liney, 720, liney);

		g.drawString(
				"Time:" + new java.text.DecimalFormat("0.000").format(time),
				10, 20);
	}
}