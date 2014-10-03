package cytus;

import java.util.*;
import cytus.editor.*;

public class Pattern implements Cloneable {
	public double beat = 0, offset = 0, pshift = 0;
	public LinkedList<Pattern.Note> notes = new LinkedList<Pattern.Note>();
	public LinkedList<Pattern.Link> links = new LinkedList<Pattern.Link>();

	public Pattern(double beat, double pshift) {
		this.beat = beat;
		this.pshift = pshift;
	}

	public void modified() {
		Collections.sort(notes);
		for (int i = 0; i < notes.size(); i++)
			notes.get(i).id = i;
		LinkedList<Link> dellist = new LinkedList<Link>();
		for (Link link : links) {
			LinkedList<Note> ndel = new LinkedList<Note>();
			for (Note node : link.nodes)
				if (!notes.contains(node)) {
					ndel.add(node);
					link.n--;
				}
			link.nodes.removeAll(ndel);

			if (link.n <= 1)
				dellist.add(link);
			else
				Collections.sort(link.nodes);
		}
		links.removeAll(dellist);
		Collections.sort(links);
		for (int i = 0; i < links.size(); i++)
			links.get(i).setID(i);
	}

	public class Note implements Comparable<Note> {
		public int id = 0, page = 0;
		public double x = 0;
		public double time = 0, holdtime = 0;
		public int linkref = -1;

		public Note() {
		}

		public Note(int id, double time, double x, double holdtime) {
			this.id = id;
			this.time = time;
			this.x = x;
			this.page = calcPage(time);
			this.holdtime = holdtime;
		}

		public int calcPage(double time) {
			return (int) ((time + pshift) / beat);
		}

		public double calcY(double time) {
			int cpage = calcPage(time);
			double y = (time + pshift) / beat - cpage;
			if (cpage % 2 == 0)
				y = 1 - y;
			return y;
		}

		public int compareTo(Note n) {
			return Double.compare(time, n.time);
		}
	}

	public class Link implements Comparable<Link> {
		public int n = 0, id = 0;
		public LinkedList<Note> nodes = new LinkedList<Note>();

		public Link(int id) {
			this.id = id;
		}

		public void add(Note note) {
			note.linkref = id;
			nodes.add(note);
			n++;
		}

		public void remove(Note note) {
			note.linkref = -1;
			nodes.remove(note);
			n--;
			if (n == 1)
				nodes.get(0).linkref = -1;
		}

		public void removeAll() {
			for (Note note : nodes)
				note.linkref = -1;
			nodes.clear();
			n = 0;
		}

		public void add(int p) {
			add(notes.get(p));
		}

		public void remove(int p) {
			remove(notes.get(p));
		}

		public void setID(int id) {
			this.id = id;
			for (Note note : nodes)
				note.linkref = id;
		}

		public int compareTo(Link link) {
			return Double.compare(nodes.get(0).time, link.nodes.get(0).time);
		}
	}

	public void errorCheck() {
		modified();
		Timing timing = new Timing(beat, offset);
		int count = 0;
		for (Note note : notes) {
			if ((note.x < 0) || (note.x > 1)) {
				System.out.println("NOTE " + note.id + " 错误: note.x=" + note.x
						+ " ,x必须在[0,1]");
				count++;
			}
			if (!timing.isBeat(note.time, 16)) {
				System.out.println("NOTE " + note.id + " 警告: note.time="
						+ note.time + " 未对齐");
				count++;
			}
			if (note.holdtime != 0) {
				int page2 = note.calcPage(note.time + note.holdtime);
				if (page2 != note.page) {
					System.out.println("NOTE " + note.id + " 警告: Hold跨屏 page1="
							+ note.page + " page2=" + page2);
					count++;
				}
			}
			if (count >= 100)
				break;
		}
		if (count >= 100) {
			System.out.println("共有" + count + "个问题");
			return;
		}
		for (Link link : links) {
			int page = link.nodes.get(0).page;
			for (int i = 1; i < link.nodes.size(); i++)
				if (link.nodes.get(i).page != page) {
					System.out.println("LINK " + link.id + " 警告: Link跨屏 page1="
							+ page + " page2=" + link.nodes.get(i).page);
					count++;
					break;
				}
			if (count >= 100)
				break;
		}
		if (count != 0)
			System.out.println("共有" + count + "个问题");
		else
			System.out.println("检测完毕");
	}

	public Object clone() {
		Pattern copy = new Pattern(beat, pshift);
		copy.offset = offset;
		for (Note n : this.notes)
			copy.notes.add(copy.new Note(n.id, n.time, n.x, n.holdtime));
		for (Link l : this.links) {
			Link nlink = copy.new Link(l.id);
			for (int i = 0; i < l.n; i++)
				nlink.add(l.nodes.get(i).id);
			copy.links.add(nlink);
		}
		return copy;
	}
}