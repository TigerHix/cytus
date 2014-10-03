package cytus;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.event.*;

public class UserInput implements MouseInputListener, KeyListener {
	PatternPlayer p = null;
	int x = 0, y = 0, csize = (int) (90 * PatternPlayer.SIZE_FIX),
			nsize = (int) (68 * PatternPlayer.SIZE_FIX);
	boolean keydown = false;

	public UserInput(PatternPlayer p, Component c) {
		this.p = p;
		switch (PatternPlayer.prefs.get("inputmode")) {
		case 0:
			for (Note note : p.notes) {
				if (note instanceof Circle)
					note.judge(note.stime);
				if (note instanceof Hold) {
					note.judge(note.stime);
					note.judge(note.etime);
				}
				if (note instanceof Link) {
					Link l = (Link) note;
					for (Link.Node node : l.nodes)
						node.judge(node.stime);
				}
			}
			break;
		case 1:
			c.addMouseListener(this);
			c.addMouseMotionListener(this);
			c.setFocusable(true);
			c.addKeyListener(this);
			break;
		}
	}

	public void doEvent() {
		p.gg.setColor(Color.GREEN);
		p.gg.drawOval(x - 10, y - 10, 20, 20);
		p.gg.setColor(Color.BLACK);
		if (keydown) {
			click(x, y);
			press(x, y);
		} else
			release(x, y);
	}

	public LinkedList<Note> selectObject(int x, int y) {
		LinkedList<Note> selected = new LinkedList<Note>();
		for (Note note : p.notes) {
			if (note instanceof Link) {
				Link l = (Link) note;
				for (Link.Node node : l.nodes)
					if ((node.stime < p.time + p.beat) && (node.page == p.page)) {
						double dist = Math.hypot(x - node.x, y - node.y);
						if (dist <= nsize)
							selected.add(node);
					}
			}
			if ((note.stime < p.time + p.beat) && (note.page == p.page)) {
				double dist = Math.hypot(x - note.x, y - note.y);
				if (dist <= csize)
					selected.add(note);
			}
		}
		return selected;
	}

	public void click(int x, int y) {
		LinkedList<Note> selected = selectObject(x, y);
		for (Note note : selected)
			if (note instanceof Circle) {
				note.judge(p.time);
				return;
			}
	}

	public void press(int x, int y) {
		LinkedList<Note> selected = selectObject(x, y);
		for (Note note : selected) {
			if (note instanceof Link.Node) {
				note.judge(p.time);
			}
			if (note instanceof Hold) {
				Hold h = (Hold) note;
				if (h.judgetime1 == -1)
					h.judge(p.time);
				return;
			}
		}
	}

	public void release(int x, int y) {
		LinkedList<Note> selected = selectObject(x, y);
		for (Note note : selected) {
			if (note instanceof Hold) {
				Hold h = (Hold) note;
				if (h.judgetime1 != -1)
					h.judge(p.time);
				return;
			}
		}
	}

	public void mouseEntered(MouseEvent e) {
	}

	public void mouseExited(MouseEvent e) {
	}

	public void mouseClicked(MouseEvent e) {
	}

	public void mousePressed(MouseEvent e) {
		click(x, y);
		press(x, y);
	}

	public void mouseReleased(MouseEvent e) {
		release(x, y);
	}

	public void mouseMoved(MouseEvent e) {
		x = e.getX();
		y = e.getY();
	}

	public void mouseDragged(MouseEvent e) {
		x = e.getX();
		y = e.getY();
		press(x, y);
	}

	public void keyPressed(KeyEvent e) {
		keydown = true;
		// click(x, y);
		// press(x, y);
	}

	public void keyReleased(KeyEvent e) {
		keydown = false;
		// release(x, y);
	}

	public void keyTyped(KeyEvent e) {
	}
}