package cytus.editor;

import cytus.*;

public class NoteData implements Comparable<NoteData> {
	int num = 0;
	double x = 0;
	double time = 0;
	double holdtime = 0;
	int page = 0;
	Link linkref = null;

	public int compareTo(NoteData d) {
		if (time == d.time)
			return 0;
		if (time < d.time)
			return -1;
		else
			return 1;
	}
}