package cytus.animation;

import cytus.*;
import net.sf.json.*;

import java.awt.*;
import java.awt.image.*;

import javax.imageio.*;

import java.io.*;
import java.util.*;

public class SpriteLibrary {
	static HashMap<String, BufferedImage> map = new HashMap<String, BufferedImage>();

	public static void load() throws Exception {
		String flist[] = new String[] { "animation_1", "animation_2", "common",
				"common_2", "common_add" };

		LinkedList<String> noteobj = new LinkedList<String>();
		BufferedReader r1 = new BufferedReader(new FileReader(
				"animation/noteobj.txt"));
		String str1 = r1.readLine();

		while (str1 != null) {
			noteobj.add(str1);
			str1 = r1.readLine();
		}

		for (int i = 0; i < 5; i++) {
			BufferedImage src = ImageIO.read(new File("assets/ui/GamePlay/"
					+ flist[i] + ".png"));
			BufferedReader r2 = new BufferedReader(new FileReader(
					"assets/ui/GamePlay/" + flist[i] + ".json"));

			String s = "";
			String str2 = r2.readLine();

			while (str2 != null) {
				s += str2;
				str2 = r2.readLine();
			}

			r2.close();

			JSONObject obj = JSONObject.fromObject(s);
			s = obj.getString("frames");
			obj = JSONObject.fromObject(s);

			Iterator t = obj.keys();

			while (t.hasNext()) {
				String key = (String) t.next();
				JSONObject frame = JSONObject.fromObject(obj.getString(key));
				JSONObject fpos = JSONObject.fromObject(frame
						.getString("frame"));
				int x = fpos.getInt("x");
				int y = fpos.getInt("y");
				int w = fpos.getInt("w");
				int h = fpos.getInt("h");

				if ((w == 0) || (h == 0)) {
					map.put(key, new BufferedImage(1, 1,
							BufferedImage.TYPE_INT_ARGB));
					continue;
				}

				JSONObject srcsize = JSONObject.fromObject(frame
						.getString("sourceSize"));
				int srcw = srcsize.getInt("w");
				int srch = srcsize.getInt("h");
				JSONObject sprsrcsize = JSONObject.fromObject(frame
						.getString("spriteSourceSize"));
				int spsx = sprsrcsize.getInt("x");
				int spsy = sprsrcsize.getInt("y");
				int spsw = sprsrcsize.getInt("w");
				int spsh = sprsrcsize.getInt("h");

				BufferedImage img = new BufferedImage(w, h,
						BufferedImage.TYPE_INT_ARGB);
				img.getGraphics().drawImage(src, 0, 0, w, h, x, y, x + w,
						y + h, null);

				BufferedImage out = new BufferedImage(srcw, srch,
						BufferedImage.TYPE_INT_ARGB);
				out.getGraphics().drawImage(img, spsx, spsy, spsw, spsh, null);
				if (noteobj.contains(key))
					out = ImageUtil.scale(out, 1.5);

				if (key.indexOf("light_add2") != -1) {
					out = ImageUtil.scale(out, 2);
					ImageUtil.brighter(out, 1.25);
				}

				map.put(key, out);
			}
		}

		BufferedImage scanline = new BufferedImage(960, 48,
				BufferedImage.TYPE_INT_ARGB);
		BufferedImage t = map.get("bar");

		for (int i = 0; i < 20; i++)
			scanline.getGraphics().drawImage(t, i * 48, 0, null);

		map.remove("bar");
		t = null;

		map.put("scanline", scanline);

	}

	public static BufferedImage get(String str) {
		return map.get(str);
	}

	public static BufferedImage getFlip(String str) {
		if (map.containsKey("flip_" + str))
			return map.get("flip_" + str);
		else {
			BufferedImage img = ImageUtil.flip(map.get(str));
			map.put("flip_" + str, img);
			return img;
		}
	}

	public static BufferedImage getBrighter(String str) {
		if (map.containsKey("bright_" + str))
			return map.get("bright_" + str);
		else {
			BufferedImage buf = ImageUtil.copyImage(map.get(str));
			ImageUtil.brighter(buf, 1.5);
			map.put("bright_" + str, buf);
			return buf;
		}
	}

	public static BufferedImage getScaledSprite(String str, double s) {
		if (map.containsKey(str + s))
			return map.get(str + s);
		else {
			BufferedImage buf = ImageUtil.scale(map.get(str), s);
			map.put(str + s, buf);
			return buf;
		}
	}
}