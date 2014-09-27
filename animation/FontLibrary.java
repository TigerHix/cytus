package cytus.animation;

import cytus.ImageUtil;
import javax.imageio.*;
import java.io.*;
import java.awt.*;
import java.awt.image.*;
import java.util.*;
import java.util.regex.*;

public class FontLibrary {
	static HashMap<String, CFont> map = new HashMap<String, CFont>();
	static String fclass[] = new String[] { "bcfont", "lcfont", "sfont" };
	static String flist[] = new String[] { "combo", "ComboSmall", "BoltonBold" };

	static class CFont {
		BufferedImage img = null;
		int id = 0;
		int x = 0, y = 0, w = 0, h = 0;
		int xoff = 0, yoff = 0;
		int xadv = 0;
	}

	public static void load() throws Exception {
		// java.util.regex.Pattern
		Pattern p = Pattern.compile("(?<=\\=)\\-?\\d*(?=\\s)");

		for (int i = 0; i < 3; i++) {
			BufferedReader r = new BufferedReader(new FileReader(
					"assets/fonts/" + flist[i] + ".fnt"));
			r.readLine();
			r.readLine();
			String str = r.readLine();
			String fname = str.substring(str.indexOf("\"") + 1,
					str.length() - 1);
			BufferedImage img = ImageIO.read(new File("assets/fonts/" + fname));
			r.readLine();
			str = r.readLine();

			while (str.indexOf("char") != -1) {
				CFont font = new CFont();
				Matcher m = p.matcher(str);
				m.find();// char id
				font.id = Integer.parseInt(m.group()) - 48;
				m.find();// x
				font.x = Integer.parseInt(m.group());
				m.find();// y
				font.y = Integer.parseInt(m.group());
				m.find();// w
				font.w = Integer.parseInt(m.group());
				m.find();// h
				font.h = Integer.parseInt(m.group());
				m.find();// xoff
				font.xoff = Integer.parseInt(m.group());
				m.find();// yoff
				font.yoff = Integer.parseInt(m.group());
				m.find();// xadv
				font.xadv = Integer.parseInt(m.group());
				font.img = new BufferedImage(font.w, font.h,
						BufferedImage.TYPE_INT_ARGB);
				font.img.getGraphics().drawImage(img, 0, 0, font.w, font.h,
						font.x, font.y, font.x + font.w, font.y + font.h, null);
				if (i == 2) {
					ImageUtil.inverseColor(font.img);// special
					font.img = ImageUtil.scale(font.img, 1.5);
				}

				font.img = ImageUtil.scale(font.img, cytus.Pattern.SIZE_FIX);
				font.w = (int) (font.w * cytus.Pattern.SIZE_FIX);
				font.h = (int) (font.h * cytus.Pattern.SIZE_FIX);
				map.put(fclass[i] + font.id, font);
				str = r.readLine();
			}
		}
	}

	public static BufferedImage getComboSmall(int combo) {
		String s = String.valueOf(combo);
		CFont font[] = new CFont[s.length()];

		for (int i = 0; i < font.length; i++)
			font[i] = map.get("lcfont" + s.charAt(i));

		int len = font[0].w;
		int adv = (int) (8 * cytus.Pattern.SIZE_FIX);

		for (int i = 1; i < font.length; i++)
			len += font[i].w - adv; // xadv

		BufferedImage img = new BufferedImage(len,
				(int) (27 * cytus.Pattern.SIZE_FIX),
				BufferedImage.TYPE_INT_ARGB);
		Graphics g = img.getGraphics();
		g.drawImage(font[0].img, 0, 0, null);
		len = font[0].w;

		for (int i = 1; i < font.length; i++) {
			g.drawImage(font[i].img, len - adv, 0, null); // xadv
			len += font[i].w - adv;
		}

		g.dispose();
		return img;
	}

	public static BufferedImage getScore(double score) {
		String s = null;
		s = new java.text.DecimalFormat("0000000").format(score);
		CFont font[] = new CFont[7];

		for (int i = 0; i < 7; i++)
			font[i] = map.get("sfont" + s.charAt(i));

		int adv = (int) (16 * cytus.Pattern.SIZE_FIX);
		int len = font[0].w + adv;

		for (int i = 1; i < 7; i++)
			len += font[i].w + adv; // xadv

		BufferedImage img = new BufferedImage(len,
				(int) (64 * cytus.Pattern.SIZE_FIX),
				BufferedImage.TYPE_INT_ARGB);
		Graphics g = img.getGraphics();
		g.drawImage(font[0].img, 0, 0, null);
		len = font[0].w + adv;

		for (int i = 1; i < 7; i++) {
			g.drawImage(font[i].img, len, 0, null); // xadv
			len += font[i].w + adv;
		}

		g.dispose();
		return img;
	}

	public static BufferedImage get(String str) {
		return map.get(str).img;
	}
}