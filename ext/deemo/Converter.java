package cytus.ext.deemo;

import java.io.*;

//Untested
public class Converter {
	public static Composition c2d(cytus.NoteChart p) {
		Composition c = new Composition();
		for (cytus.NoteChart.Note note : p.notes) {
			if ((note.holdtime == 0) && (note.linkref == -1)) { // Circle
				double x = (note.x - 0.5) * 2;
				c.notes.add(new Note(note.id, x, 1, note.time));
			}
			if (note.holdtime != 0) { // Hold
				double x = (note.x - 0.5) * 2;
				c.notes.add(new Note(note.id, x, 1, note.time));
			}
		}
		for (cytus.NoteChart.Link link : p.links) {
			Link l = new Link();
			for (cytus.NoteChart.Note node : link.nodes) {
				double x = (node.x - 0.5) * 2;
				l.add(new Note(node.id, x, 1, node.time));
			}
			c.links.add(l);
		}
		return c;
	}

	public static cytus.NoteChart d2c(Composition c, double pagesize,
			double pshift) {
		cytus.NoteChart p = new cytus.NoteChart(pagesize, pshift);
		int ncount = 0;
		for (Note note : c.notes) {
			if (note.isnode)
				continue;
			double x = note.x / 4.0 + 0.5;
			p.notes.add(p.new Note(ncount++, note.time, x, 0));
		}
		int lcount = 0;
		for (Link link : c.links) {
			cytus.NoteChart.Link l = p.new Link(lcount++);
			for (Note node : link.nodes) {
				double x = node.x / 4.0 + 0.5;
				p.notes.add(p.new Note(ncount, node.time, x, 0));
				l.add(ncount);
				ncount++;
			}
			p.links.add(l);
		}
		p.modified();
		return p;
	}
}