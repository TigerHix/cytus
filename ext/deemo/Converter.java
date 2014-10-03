package cytus.ext.deemo;

import java.io.*;

//Untested
public class Converter {
	public static Composition c2d(cytus.Pattern p) {
		Composition c = new Composition();
		for (cytus.Note note : p.notes) {
			if (note instanceof cytus.Circle) {
				double x = (note.x - cytus.Pattern.PXOFF)
						/ (double) cytus.Pattern.PWIDTH * 4 - 2;
				c.notes.add(new Note(note.id, x, 1, note.stime));
			}
			if (note instanceof cytus.Hold) {
				double x = (note.x - cytus.Pattern.PXOFF)
						/ (double) cytus.Pattern.PWIDTH * 4 - 2;
				c.notes.add(new Note(note.id, x, 1, note.stime));
			}
			if (note instanceof cytus.Link) {
				Link l = new Link();
				cytus.Link src = (cytus.Link) note;
				for (cytus.Link.Node node : src.nodes) {
					double x = (node.x - cytus.Pattern.PXOFF)
							/ (double) cytus.Pattern.PWIDTH * 4 - 2;
					l.add(new Note(node.id, x, 1, node.stime));
				}
				c.links.add(l);
			}
		}
		return c;
	}

	public static cytus.Pattern d2c(Composition c, double pagesize,
			double offset) {
		cytus.Pattern p = new cytus.Pattern();
		p.beat = pagesize;
		p.offset = offset;
		for (Note note : c.notes) {
			if (note.isnode)
				continue;
			int x = p.calcX(note.x / 4.0 + 0.5);
			int y = p.calcY(note.time);
			p.notes.add(new cytus.Circle(p, note.id, x, y, note.time));
		}
		for (Link link : c.links) {
			cytus.Link l = new cytus.Link(p);
			for (Note node : link.nodes) {
				int x = p.calcX(node.x / 4.0 + 0.5);
				int y = p.calcY(node.time);
				l.nodes.add(l.new Node(node.id, x, y, node.time));
			}
			l.recalc();
			p.notes.add(l);
		}
		return p;
	}
}