package cytus.editor;

import cytus.*;
import java.util.*;

public class NoteGroup {
	LinkedList<Pattern.Note> notes = new LinkedList<Pattern.Note>();
	String name = "";

	public NoteGroup(String name) {
		this.name = name;
	}
}