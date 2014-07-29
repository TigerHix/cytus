package cytus;

import cytus.animation.*;
import java.util.*;
import java.io.*;
import java.text.*;
import java.awt.*;
import java.awt.image.*;
import javax.imageio.*;
import javax.media.*;
import javazoom.jl.converter.*;

public class Pattern {
	final static String path = "assets/songs/";
	public static HashMap<String, Integer> prefs = new HashMap<String, Integer>();
	static {
		File f = new File("preferences.txt");
		if (!f.exists()) {
			prefs.put("note_effect", 1);
			prefs.put("quality", 1);
			prefs.put("bg", 1);
			prefs.put("mask", 1);
			prefs.put("popupmode", 2);
			prefs.put("convertmp3", 1);
			try {
				PrintWriter p = new PrintWriter(new FileWriter(f));
				for (Map.Entry<String, Integer> entry : prefs.entrySet())
					p.println(entry.getKey() + "=" + entry.getValue());
				p.close();
			} catch (Exception e) {
			}
		} else {
			try {
				BufferedReader in = new BufferedReader(new FileReader(f));
				String str = in.readLine();
				while (str != null) {
					String pair[] = str.split("=");
					prefs.put(pair[0], Integer.parseInt(pair[1]));
					str = in.readLine();
				}
				in.close();
			} catch (Exception e) {
			}
		}
		try{
		    cytus.animation.SpriteLibrary.load();
            cytus.animation.AnimationPreset.load();
            cytus.animation.FontLibrary.load();
		}catch(Exception e){}
	}

	public double offset = 0, beat = 0, time = 0;
	public int page = 0;
	int liney = 0, combo = 0, ncount = 0;
	double score = 0, tp = 0;

	public LinkedList<Note> notes = new LinkedList<Note>();
	public LinkedList<Note> copy = new LinkedList<Note>();
	LinkedList<Animation> animqueue = new LinkedList<Animation>();

	Graphics2D gg = null;
	Image bg = null, line = null;
	ComboSmallPop pop = null;
	MaskBeat mask = null;
	public BufferedImage buf = new BufferedImage(720, 480,
			BufferedImage.TYPE_INT_ARGB);

	public Player player = null;
	JSPlayer sound = null;
    
	public Pattern(){}
	public Pattern(String name, String diff) throws Exception {
		BufferedReader in = new BufferedReader(new FileReader(path + name + "/"
				+ name + "." + diff + ".txt"));

		if (in.readLine().equals("VERSION 2"))
			new Reader2(in);
		else
			new Reader1(in);

		in.close();

		Note t[] = new Note[notes.size()];
		notes.toArray(t);
		// Old:
		/*Arrays.sort(t, new Comparator<Note>() {
			public int compare(Note n1, Note n2) {
				return Double.compare(n1.stime, n2.stime);
			}
		});*/
		// New feature in JDK 1.8
		Arrays.sort(t,(a,b)->Double.compare(a.stime,b.stime));
		notes.clear();
		Collections.addAll(notes, t);
		copy.addAll(notes);

		if (prefs.get("convertmp3") == 1) {
			new Converter().convert(path + name + "/" + name + ".mp3",
					"temp.wav");
			player = Manager.createRealizedPlayer(new MediaLocator(new File(
					"temp.wav").toURI().toURL()));
		} else
			player = Manager.createRealizedPlayer(new MediaLocator(new File(
					path + name + "/" + name + ".mp3").toURI().toURL()));

		if (prefs.get("bg") == 1) {
			if (new File(path + name + "/" + name + "_bg.png").exists())
				bg = ImageIO
						.read(new File(path + name + "/" + name + "_bg.png"));
			else if (new File(path + name + "/bg.png").exists())
				bg = ImageIO.read(new File(path + name + "/bg.png"));
			else
				bg = ImageIO.read(new File(path + name + "/" + name + ".png"));

			bg = bg.getScaledInstance(720, 480, Image.SCALE_SMOOTH);
			bg = ImageUtil.transparent(bg, 0.8f);
		} else {
			bg = new BufferedImage(720, 480, BufferedImage.TYPE_INT_ARGB);
			Graphics g = bg.getGraphics();
			g.setColor(Color.WHITE);
			g.fillRect(0, 0, 720, 480);
			g.dispose();
		}

		BufferedImage title = SpriteLibrary.get("gameplay_title");
		BufferedImage mask1 = SpriteLibrary.get("gameplay_title_mask");
		BufferedImage mask2 = SpriteLibrary.get("gameplay_bg_mask_2");
		BufferedImage white = SpriteLibrary.get("gameplay_bg_mask");

		Graphics g = bg.getGraphics();
		g.drawImage(white, 0, 0, 720, 48, null);
		g.drawImage(mask1, 0, 0, 720, 48, null);
		g.drawImage(mask2, 190, 10, 340, 48, null);
		g.drawImage(title, 190, -10, null);
		line = SpriteLibrary.get("scanline");

		sound = new JSPlayer("assets/sounds/beat1.wav");

		if (prefs.get("note_effect") == 0)
			sound.mute();
		if (prefs.get("mask") == 1)
			mask = new MaskBeat(offset, beat / 2);

		gg = (Graphics2D) buf.getGraphics();
		gg.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);

		if (prefs.get("quality") > 1)
			gg.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
					RenderingHints.VALUE_INTERPOLATION_BICUBIC);

		if (prefs.get("quality") > 2)
			gg.setRenderingHint(RenderingHints.KEY_RENDERING,
					RenderingHints.VALUE_RENDER_QUALITY);

		gg.setStroke(new BasicStroke(5));
		gg.setColor(Color.BLACK);
	}

	public void addAnimation(Animation a) {
		animqueue.add(a);
	}

	public void removeAnimation(Animation a) {
		animqueue.remove(a);
	}

	public void addCombo() {
	    if (ncount == 1) {
		    score = 1000000;
			tp = 100;
			return;
		}
		score += 900000.0 / ncount + combo * 200000.0 / ncount / (ncount - 1);
		tp += 100.0 / ncount;
		combo++;
		pop = new ComboSmallPop(time);

		try {
			sound.start();
		} catch (Exception e) {
		}

		if (combo % 25 == 0) {
			ComboAnimation anim = new ComboAnimation(combo, time);
			addAnimation(anim);
		}
	}

	public void addCombo(int add) {
		for (int i = 0; i < add; i++)
			addCombo();
	}

	public void start(Graphics2D g) throws Exception {
		player.start();
		while (true) {
			int drawtime = paint();
			g.drawImage(buf, 0, 0, null);
			if (drawtime <= 16e6)
				Thread.sleep((int) (16 - drawtime / 1e6));
		}
	}

	public void restart() {
		notes.clear();
		animqueue.clear();
		Collections.sort(copy,(a,b)->Double.compare(a.stime,b.stime));
		notes.addAll(copy);
		ncount = 0;
		for (Note n : notes) {
			n.recalc();
			if (n instanceof Link)
				ncount += ((Link) n).n;
			else
				ncount++;
		}
		combo = 0;
		score = 0;
		tp = 0;
	}

	public int paint() throws Exception {
		time = player.getMediaTime().getSeconds();

		long mtime1 = System.nanoTime();
		gg.drawImage(bg, 0, 0, null);

		if (mask != null)
			mask.paint(gg, time);

		page = (int) ((time + offset) / beat);
		liney = (int) (((time + offset) % beat / beat) * 352);

		if (page % 2 == 0)
			liney = 352 - liney;

		liney += 64;

		LinkedList<Animation> del = new LinkedList<Animation>();

		for (Animation anim : animqueue) {
			if (anim.getEndTime() < time)
				del.add(anim);
			else if (anim.getStartTime() <= time)
				anim.paint(gg, time);
		}

		animqueue.removeAll(del);

		LinkedList<Note> del2 = new LinkedList<Note>();

		for (Note n : notes) {
			if (n.etime < time) {
				n.remove();
				del2.add(n);
			}
		}

		notes.removeAll(del2);

		if (notes.size() > 0) {
			int end = -1; // current page's end
			int npstart = -1, npend = -1; // Next page's start&end
			int i = 0;

			for (i = 0; i < notes.size(); i++) {
				Note n = notes.get(i);

				if (n.stime > time + beat)
					break;

				if ((n.page > page) && (npstart == -1))
					npstart = i;

				if (n.page == page)
					end = i;
				else
					npend = i;
			}

			if (npstart != -1) {
				for (i = npend; i >= npstart; i--) {
					Note n = notes.get(i);
					n.paint(gg);
				}
			}

			for (i = end; i >= 0; i--) {
				Note n = notes.get(i);
				n.paint(gg);
			}
		}

		gg.drawImage(line, 0, liney - 24, null);

		if (combo > 1) {
			BufferedImage text = SpriteLibrary.get("combo_small_text");
			BufferedImage bg = SpriteLibrary.get("combo_small_bg");
			double scale = pop != null ? pop.scale(time) : 1;
			int w1 = (int) (250 * pop.scale(time));
			gg.drawImage(bg, 360 - w1 / 2, 28, w1, 28, null);
			gg.drawImage(text, 270, 30, 87, 25, null);
			BufferedImage cp = FontLibrary.getComboSmall(combo);
			int w2 = (int) (cp.getWidth() * scale), h2 = (int) (cp.getHeight() * scale);
			gg.drawImage(cp, 410 - w2 / 2, 41 - h2 / 2, w2, h2, null);
		}

		BufferedImage sp = FontLibrary.getScore(score);
		gg.drawImage(sp, 720 - sp.getWidth(), 4, null);

		long mtime2 = System.nanoTime();
		double fps = 1e9 / (mtime2 - mtime1);
		String info = "Time:"
				+ new java.text.DecimalFormat("0.000").format(time);
		info += " fps:" + (int) fps;
		info += " TP:" + new java.text.DecimalFormat("00.00").format(tp);

		gg.drawString(info, 10, 20);
		return (int) (mtime2 - mtime1);
	}

	public class Reader1 {
		public Reader1(BufferedReader in) throws Exception {
			double data[][] = new double[1600][4];
			boolean v[] = new boolean[1600];
			Arrays.fill(v, true);

			String str = in.readLine();

			while (str.indexOf("SECTION") == -1) {
				// SHIFT X.XXXXXX
				if (str.startsWith("SHIFT\t"))
					offset = Double.parseDouble(str.substring(6));

				str = in.readLine();
			}

			// SECTION X.XXXXXX X.XXXXXX X
			beat = Double.parseDouble(str.split("\\t|\\s")[2]);

			while (str.indexOf("SECTION") != -1)
				str = in.readLine();

			while (str != null) {
				str = str.substring(5);
				Scanner s = new Scanner(str);
				s.nextInt();
				s.nextInt();
				double time = s.nextDouble();
				int y = (int) ((time + offset) % beat / beat * 352);
				s.nextInt();
				int x = (int) (s.nextDouble() * 600);
				int page = (int) ((time + offset) / beat);
				int next = s.nextInt();
				double hold = s.nextDouble();
				s.close();

				if (page % 2 == 0)
					y = 352 - y;

				x += 60;
				y += 64;

				if ((next == -1) && (hold != 0)) {
					notes.add(new Hold(Pattern.this, x, y, time, hold));
					v[ncount] = false;
				} else {
					data[ncount][0] = x;
					data[ncount][1] = y;
					data[ncount][2] = time;
					data[ncount][3] = next;
				}

				ncount++;
				str = in.readLine();
			}

			for (int i = 0; i < ncount; i++)
				if (v[i] && (data[i][3] != -1)) {
				    Link link=new Link(Pattern.this);
					int p = i;

					while (data[p][3] != -1) {
						v[p] = false;
						link.nodes.add(link.new Node((int)data[p][0],(int)data[p][1],data[p][2]));
						p = (int) data[p][3];
					}

					v[p] = false;
					link.nodes.add(link.new Node((int)data[p][0],(int)data[p][1],data[p][2]));
					link.recalc();
					notes.add(link);
				}

			for (int i = 0; i < ncount; i++)
				if (v[i])
					notes.add(new Circle(Pattern.this, (int)data[i][0], (int)data[i][1],
							data[i][2]));
		}
	}

	public class Reader2 {
		public Reader2(BufferedReader in) throws Exception {
			double data[][] = new double[1600][3];
			boolean v[] = new boolean[1600];
			Arrays.fill(v, true);

			in.readLine();
			String off = in.readLine();
			off = off.substring(11);
			offset = Double.parseDouble(off);
			String size = in.readLine();
			size = size.substring(10);
			beat = Double.parseDouble(size);

			String str = in.readLine();
			int count = 0;

			while (str != null) {
				if (str.indexOf("LINK") != -1) {
					str = str.substring(5);
					Scanner s = new Scanner(str);
					Link link=new Link(Pattern.this);

					while (s.hasNext()) {
						int p = s.nextInt();
						v[p] = false;
						link.nodes.add(link.new Node((int)data[p][0],(int)data[p][1],data[p][2]));
					}

					link.recalc();
					notes.add(link);
					s.close();
					str = in.readLine();
					continue;
				}

				ncount++;
				str = str.substring(5);
				Scanner s = new Scanner(str);
				s.nextInt();
				double time = s.nextDouble();
				int page = (int) ((time + offset) / beat);
				int y = (int) ((time + offset) % beat / beat * 352);
				int x = (int) (s.nextDouble() * 600);
				double hold = s.nextDouble();
				s.close();

				if (page % 2 == 0)
					y = 352 - y;

				x += 60;
				y += 64;

				if (hold != 0) {
					notes.add(new Hold(Pattern.this, x, y, time, hold));
					v[count] = false;
				}

				data[count][0] = x;
				data[count][1] = y;
				data[count][2] = time;
				count++;
				str = in.readLine();
			}

			for (int i = 0; i < count; i++)
				if (v[i])
					notes.add(new Circle(Pattern.this, (int)data[i][0], (int)data[i][1],
							data[i][2]));
		}
	}

	public class Writer {
		public Writer(PrintWriter out) throws Exception {
			DecimalFormat df = new DecimalFormat("0.000000");

			// Header
			out.println("VERSION 2");
			out.println("BPM " + df.format(240.0 / beat));
			out.println("PAGE_SHIFT " + df.format(offset));
			out.println("PAGE_SIZE " + df.format(beat));

			int count = 0, lcount = 0;
			int range[][] = new int[200][2];
			for (Note n : notes) {
				if (n instanceof Link) {
					Link l = (Link) n;

					range[lcount][0] = count;
					range[lcount][1] = count + l.n;
					lcount++;

					for (Link.Node node:l.nodes){
						out.print("NOTE\t");
						out.print((count++) + "\t");
						out.print(df.format(node.stime) + "\t");
						out.print(df.format((node.x - 60) / 600.0)
								+ "\t");
						out.println("0.000000");
					}
				} else {
					out.print("NOTE\t");
					out.print((count++) + "\t");
					out.print(df.format(n.stime) + "\t");
					out.print(df.format((n.x - 60) / 600.0) + "\t");
					out.println(df.format((n.etime - n.stime)));
				}
			}

			for (int i = 0; i < lcount; i++) {
				out.print("LINK");
				for (int j = range[i][0]; j < range[i][1]; j++)
					out.print(" " + j);
				out.println();
			}

			out.close();
		}
	}
}