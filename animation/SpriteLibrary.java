package cytus.animation;

import cytus.*;
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

		HashMap<String, Double> objlist = new HashMap<String, Double>();
		BufferedReader r1 = new BufferedReader(new FileReader(
				"animation/objlist.txt"));
		String str1 = r1.readLine();

		while (str1 != null) {
			String pair[] = str1.split("\\t");
			objlist.put(pair[0], Double.parseDouble(pair[1])
					* NoteChartPlayer.SIZE_FIX);
			str1 = r1.readLine();
		}
		r1.close();

		for (int i = 0; i < 5; i++) {
			Atlas atlas = new Atlas("assets/ui/GamePlay/", flist[i]);
			map.putAll(atlas.map);
		}
		map.keySet().retainAll(objlist.keySet());
		for (Map.Entry<String, Double> entry : objlist.entrySet()) {
			BufferedImage img = map.get(entry.getKey());
			img = ImageUtil.scale(img, entry.getValue());
			map.put(entry.getKey(), img);
		}

		BufferedImage t = map.get("bar");
		map.put("scanline",
				ImageUtil.scale(t, NoteChartPlayer.WIDTH, t.getHeight()));

	}

	public static BufferedImage get(String str) {
		return map.get(str);
	}

	public static BufferedImage getFlip(String str) {
		if (map.containsKey("flip_" + str))
			return map.get("flip_" + str);
		else {
			BufferedImage img = ImageUtil.flipV(map.get(str));
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