package cytus.animation;

import cytus.*;
import java.io.*;
import javax.imageio.*;
import java.awt.*;
import java.awt.image.*;

public class ComboAnimation {
	double stime = 0, etime = 0;
	int x = 0, y = 0, textx = 0, texty = 0;
	BufferedImage textimg = null, img = null;

	public ComboAnimation(int combo, double time) {
		String str = String.valueOf(combo);
		int len = str.length();
		img = new BufferedImage((int) ((len + 1) * 128 * Pattern.SIZE_FIX),
				(int) (317 * Pattern.SIZE_FIX), BufferedImage.TYPE_INT_ARGB);
		Graphics g = img.getGraphics();

		for (int i = 0; i < len; i++) {
			BufferedImage num = FontLibrary
					.get("bcfont" + (str.charAt(i) - 48));
			g.drawImage(num, i * (int) (128 * Pattern.SIZE_FIX), 0, null);
		}

		g.dispose();

		stime = time;
		etime = time + 1;
		x = Pattern.WIDTH / 2 - img.getWidth() / 2;
		y = (int) (180 * Pattern.SIZE_FIX);
		textx = Pattern.WIDTH / 2 - (int) (150 * Pattern.SIZE_FIX);
		texty = (int) (150 * Pattern.SIZE_FIX);

		textimg = SpriteLibrary.get("combo_text");
	}

	public void paint(Graphics2D g, double time) {
		double delta = time - stime;
		double alpha = 0;
		double s = 1;

		if (delta <= 1 / 6.0)
			alpha = delta * 6;

		if ((delta > 1 / 6.0) && (delta <= 1 / 3.0))
			alpha = 1;

		if ((delta > 1 / 3.0) && (delta < 1 / 2.0))
			alpha = 3 - delta * 6;

		if (delta <= 1 / 2.0)
			s = 1 + delta;

		s *= 0.8;
		g.setComposite(AlphaComposite.SrcOver.derive((float) alpha));
		g.drawImage(textimg, textx, texty, (int) (textimg.getWidth() * s),
				(int) (textimg.getHeight() * s), null);
		g.drawImage(img, x, y, (int) (img.getWidth() * s),
				(int) (img.getHeight() * s), null);

		if (delta <= 1 / 3.0)
			alpha = delta * 3;

		if ((delta > 1 / 3.0) && (delta <= 2 / 3.0))
			alpha = 1;

		if ((delta > 2 / 3.0) && (delta < 1))
			alpha = 3 - delta * 3;

		g.setComposite(AlphaComposite.SrcOver.derive((float) alpha));
		g.drawImage(textimg, textx, texty, null);
		g.drawImage(img, x, y, null);
		g.setComposite(AlphaComposite.SrcOver);
	}
}
