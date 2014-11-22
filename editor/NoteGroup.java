package cytus.editor;

import cytus.*;
import java.util.*;

public class NoteGroup {
	LinkedList<NoteChart.Note> notes = new LinkedList<NoteChart.Note>();
	String name = "";

	public NoteGroup(String name) {
		this.name = name;
	}
}