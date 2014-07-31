package cytus.editor;

import cytus.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.*;
import java.util.*;
import java.text.*;

public class PatternData extends DefaultTableModel {
	LinkedList<NoteData> notedata = new LinkedList<NoteData>();
	LinkedList<Link> linkdata = new LinkedList<Link>();
	Pattern p = null;
	DecimalFormat df = new DecimalFormat("0.000000");

	public PatternData(Pattern p) {
		super();
		this.p = p;
		int n = 0;
		addColumn("Note");
		addColumn("time");
		addColumn("page");
		addColumn("x_pos");
		addColumn("hold_time");
		addColumn("linkref");
		for (Note note : p.notes) {
			if (note instanceof Link) {
				Link l = (Link) note;
				linkdata.add(l);
				for (Link.Node node : l.nodes) {
					NoteData d = new NoteData();
					d.num = n++;
					d.x = (node.x - 60) / 600.0;
					d.time = node.stime;
					d.holdtime = 0;
					d.page = node.page;
					d.linkref = l;
					notedata.add(d);
					addRow(toStringArray(d));
				}
			} else {
				NoteData d = new NoteData();
				d.num = n++;
				d.x = (note.x - 60) / 600.0;
				d.time = note.stime;
				d.holdtime = note.etime - note.stime;
				d.page = note.page;
				d.linkref = null;
				notedata.add(d);
				addRow(toStringArray(d));
			}
		}
	}

	private String[] toStringArray(NoteData d) {
		String s[] = new String[6];
		s[0] = String.valueOf(d.num);
		s[1] = df.format(d.time);
		s[2] = String.valueOf(d.page);
		s[3] = df.format(d.x);
		s[4] = df.format(d.holdtime);
		if (d.linkref == null)
			s[5] = "-1";
		else
			s[5] = String.valueOf(linkdata.indexOf(d.linkref));
		return s;
	}

	public boolean isCellEditable(int row, int col) {
		return (col != 0) && (col != 2);// Note,page
	}

	public void setValueAt(Object data, int row, int col) {
		String str = (String) data;
		NoteData d = notedata.get(row);
		switch (col) {
		case 1:// time
			d.time = Double.parseDouble(str);
			d.page = (int) ((d.time + p.offset) / p.beat);
			Collections.sort(notedata);
			setRowCount(0);
			for (int i = 0; i < notedata.size(); i++) {
				notedata.get(i).num = i;
				addRow(toStringArray(notedata.get(i)));
			}
			break;
		case 3:// x_pos
			d.x = Double.parseDouble(str);
			break;
		case 4:// hold_time
			d.holdtime = Double.parseDouble(str);
			break;
		}
		fireTableCellUpdated(row, col);
	}
}