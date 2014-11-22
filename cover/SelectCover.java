package cytus.cover;

import com.alibaba.fastjson.*;
import cytus.*;
import cytus.animation.*;
import java.io.*;
import java.util.*;
import java.awt.*;
import java.awt.image.*;
import javax.swing.*;
import javax.imageio.*;

public class SelectCover extends Sprite {
	LinkedHashMap<String, Sprite> elements = new LinkedHashMap<String, Sprite>();
	HashMap<String, Atlas> rsrc = new HashMap<String, Atlas>();
	String songtitle = null;
	String folder = null;
	int WIDTH = 1024, HEIGHT = 683;
	BufferedImage img = new BufferedImage(WIDTH, HEIGHT,
			BufferedImage.TYPE_INT_RGB);

	public SelectCover(String songtitle) throws Exception {
		this.songtitle = songtitle;
		folder = "assets/songs/" + songtitle + "/";
		initSprite(this,
				JSON.parseObject(readFile(folder + "select_cover.prefab.json")));

		double endtime = MorphingAnimation.read(folder + songtitle
				+ "_enter.anim.json", elements, false, 0);
		MorphingAnimation.read(folder + songtitle + "_loop.anim.json",
				elements, true, endtime);

		/*
		 * JWindow window = new JWindow(); window.setSize(WIDTH, HEIGHT);
		 * window.setVisible(true); Graphics g = window.getGraphics();
		 * Graphics2D gg = (Graphics2D) img.getGraphics();
		 * 
		 * gg.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
		 * RenderingHints.VALUE_INTERPOLATION_BICUBIC);
		 * 
		 * double time = 0; while (true) { gg.setColor(Color.WHITE);
		 * gg.fillRect(0, 0, WIDTH, HEIGHT); paint(gg, time);
		 * gg.setColor(Color.BLACK); gg.drawString("Time:"+time,10,20);
		 * g.drawImage(img, 0, 0, null);
		 * 
		 * time += 0.01; Thread.sleep(10); }
		 */
	}

	private void initSprite(Sprite s, JSONObject obj) throws Exception {
		s.name = obj.getString("Name");
		s.x = obj.getDoubleValue("X");
		s.y = -obj.getDoubleValue("Y");
		if (s.father == null) {
			s.x += WIDTH / 2;
			s.y += HEIGHT / 2;
		}
		if (obj.containsKey("Texture"))
			s.img = ImageUtil.copyImage(ImageIO.read(new File(folder
					+ obj.getString("Texture"))));
		if (obj.containsKey("Atlas")) {
			String filename = obj.getString("Atlas");
			String part = obj.getString("Part");
			if (!rsrc.containsKey(filename))
				rsrc.put(filename, new Atlas(folder, filename.split("\\.")[0]));
			s.img = rsrc.get(filename).get(part);
		}
		int width = obj.getIntValue("Width");
		int height = obj.getIntValue("Height");
		if ((width != 0) && (height != 0))
			s.img = ImageUtil.scale(s.img, width, height);
		if (obj.containsKey("Alpha"))
			s.alpha = obj.getDoubleValue("Alpha");
		if (obj.containsKey("Rotation"))
			s.angle = -Math.toRadians(obj.getDoubleValue("Rotation"));
		if (obj.containsKey("ScaleX"))
			s.sx = obj.getDoubleValue("ScaleX");
		if (obj.containsKey("ScaleY"))
			s.sy = obj.getDoubleValue("ScaleY");
		if (obj.containsKey("Anchor"))
			s.setAnchor(obj.getString("Anchor"));
		if (obj.containsKey("FlipU"))
			s.img = ImageUtil.flipH(s.img);
		if (obj.containsKey("Blending"))
			s.specialPaint(img);
		if (obj.containsKey("Color")) {
			JSONArray carr = obj.getJSONArray("Color");
			int r = (int) (carr.getDoubleValue(0) * 255);
			int g = (int) (carr.getDoubleValue(1) * 255);
			int b = (int) (carr.getDoubleValue(2) * 255);
			int rgb = 0xFF000000 | (r << 16) | (b << 8) | b;
			ImageUtil.fillColor(s.img, rgb);
		}
		elements.put(s.name, s);
		if (obj.containsKey("Children")) {
			JSONArray childsarr = obj.getJSONArray("Children");
			for (Object subobj : childsarr) {
				JSONObject child = (JSONObject) subobj;
				Sprite cs = new Sprite();
				s.addChild(cs, false);
				initSprite(cs, child);
			}
		}
	}

	private String readFile(String path) throws Exception {
		BufferedReader in = new BufferedReader(new FileReader(path));
		String str = "";
		String s = in.readLine();
		while (s != null) {
			str += s;
			s = in.readLine();
		}
		in.close();
		return str;
	}

	public static void main(String args[]) throws Exception {
		new SelectCover(args[0]);
	}
}