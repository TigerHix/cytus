package cytus.ext.deemo;

import java.util.*;

public class Link {
	LinkedList<Note> nodes = new LinkedList<Note>();

	public void add(Note n) {
		nodes.add(n);
	}
}