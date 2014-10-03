package cytus.ext.deemo;

import net.sf.json.*;
import java.util.*;
import java.io.*;

public class Composition {
	LinkedList<Note> notes = new LinkedList<Note>();
	LinkedList<Link> links = new LinkedList<Link>();

	public Composition() {
	}

	public Composition(String file) throws Exception {
		BufferedReader r = new BufferedReader(new FileReader(file));
		String str = "";
		String s = r.readLine();
		while (s != null) {
			str += s;
			s = r.readLine();
		}
		r.close();

		JSONObject obj = JSONObject.fromObject(str);
		JSONArray arr = JSONArray.fromObject(obj.get("notes"));
		for (Object tobj : arr) {
			JSONObject nobj = JSONObject.fromObject(tobj);
			int id = nobj.getInt("$id");
			double x = nobj.optDouble("pos", 0);
			double time = nobj.getDouble("_time");
			double size = nobj.getDouble("size");
			notes.add(new Note(id, x, size, time));
		}

		arr = JSONArray.fromObject(obj.get("links"));
		for (Object tobj : arr) {
			JSONObject link = JSONObject.fromObject(tobj);
			JSONArray arr2 = JSONArray.fromObject(link.get("notes"));

			Link l = new Link();
			for (Object tobj2 : arr2) {
				JSONObject note = JSONObject.fromObject(tobj2);
				int p = Integer.parseInt(note.getString("$ref"));
				l.add(notes.get(p - 1));
				notes.get(p - 1).isnode = true;
			}
			links.add(l);
		}
	}

	public void write(String file) throws Exception {
		JSONObject root = new JSONObject();
		root.element("speed", 10);
		JSONArray notesarr = new JSONArray();
		for (Note n : notes) {
			JSONObject noteobj = new JSONObject();
			noteobj.element("$id", n.id);
			noteobj.element("pos", n.x);
			noteobj.element("size", n.size);
			noteobj.element("_time", n.time);
			notesarr.add(noteobj);
		}
		root.element("notes", notesarr);
		JSONArray linksarr = new JSONArray();
		for (Link l : links) {
			JSONObject obj = new JSONObject();
			JSONArray nodesarr = new JSONArray();
			for (Note n : l.nodes) {
				JSONObject nodeobj = new JSONObject();
				nodeobj.element("$ref", n.id);
				nodesarr.add(nodeobj);
			}
			obj.element("notes", nodesarr);
			linksarr.add(obj);
		}
		root.element("links", linksarr);
		FileWriter writer = new FileWriter(file);
		writer.write(root.toString());
		writer.close();
	}
}