package cytus.ext.deemo;

import com.alibaba.fastjson.*;
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

		JSONObject obj = JSON.parseObject(str);
		JSONArray arr = obj.getJSONArray("notes");
		for (Object subobj : arr) {
			JSONObject nobj = (JSONObject) subobj;
			int id = nobj.getIntValue("$id");
			double x = nobj.getDoubleValue("pos");
			double time = nobj.getDoubleValue("_time");
			double size = nobj.getDoubleValue("size");
			notes.add(new Note(id, x, size, time));
		}

		arr = obj.getJSONArray("links");
		for (Object subobj : arr) {
			JSONObject link = (JSONObject) subobj;
			JSONArray arr2 = link.getJSONArray("notes");

			Link l = new Link();
			for (Object tobj2 : arr2) {
				JSONObject note = (JSONObject) tobj2;
				int p = Integer.parseInt(note.getString("$ref"));
				l.add(notes.get(p - 1));
				notes.get(p - 1).isnode = true;
			}
			links.add(l);
		}
	}

	public void write(String file) throws Exception {
		JSONObject root = new JSONObject();
		root.put("speed", 10);
		JSONArray notesarr = new JSONArray();
		for (Note n : notes) {
			JSONObject noteobj = new JSONObject();
			noteobj.put("$id", n.id);
			noteobj.put("pos", n.x);
			noteobj.put("size", n.size);
			noteobj.put("_time", n.time);
			notesarr.add(noteobj);
		}
		root.put("notes", notesarr);
		JSONArray linksarr = new JSONArray();
		for (Link l : links) {
			JSONObject obj = new JSONObject();
			JSONArray nodesarr = new JSONArray();
			for (Note n : l.nodes) {
				JSONObject nodeobj = new JSONObject();
				nodeobj.put("$ref", n.id);
				nodesarr.add(nodeobj);
			}
			obj.put("notes", nodesarr);
			linksarr.add(obj);
		}
		root.put("links", linksarr);
		FileWriter writer = new FileWriter(file);
		writer.write(root.toString());
		writer.close();
	}
}