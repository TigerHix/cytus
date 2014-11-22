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
		img = new BufferedImage(
				(int) ((len + 1) * 128 * NoteChartPlayer.SIZE_FIX),
				(int) (317 * NoteChartPlayer.SIZE_FIX),
				BufferedImage.TYPE_INT_ARGB);
		Graphics g = img.getGraphics();
		for (int i = 0; i < len; i++) {
			BufferedImage num = FontLibrary
					.get("bcfont" + (str.charAt(i) - 48));
			g.drawImage(num, i * (int) (128 * NoteChartPlayer.SIZE_FIX), 0,
					null);
		}
		g.dispose();

		stime = time;
		etime = time + 1;
		x = NoteChartPlayer.WIDTH / 2 - img.getWidth() / 2;
		y = (int) (320 * NoteChartPlayer.SIZE_FIX);
		textx = NoteChartPlayer.WIDTH / 2;
		texty = (int) (180 * NoteChartPlayer.SIZE_FIX);

		textimg = SpriteLibrary.get("combo_text");
	}

	public void paint(Graphics2D g, double time) {
		double delta = time - stime;
		double alpha = 0;
		double s = 1;

		// Back
		if (delta <= 1 / 3.0)
			alpha = delta * 3;

		if ((delta > 1 / 3.0) && (delta <= 2 / 3.0))
			alpha = 1;

		if ((delta > 2 / 3.0) && (delta < 1))
			alpha = 3 - delta * 3;

		g.setComposite(AlphaComposite.SrcOver.derive((float) alpha));
		g.drawImage(textimg, textx - textimg.getWidth() / 2,
				texty - textimg.getHeight() / 2, null);
		g.drawImage(img, x, y - img.getHeight() / 2, null);
		g.setComposite(AlphaComposite.SrcOver);

		// Front
		alpha = 0;
		if (delta <= 1 / 6.0)
			alpha = delta * 6;
		if ((delta > 1 / 6.0) && (delta <= 1 / 3.0))
			alpha = 1;
		if ((delta > 1 / 3.0) && (delta < 1 / 2.0))
			alpha = 3 - delta * 6;
		if (delta <= 1 / 2.0)
			s = 1 + delta * 1.6 / 2;

		g.setComposite(AlphaComposite.SrcOver.derive((float) alpha));
		int textw = (int) (textimg.getWidth() * s), texth = (int) (textimg
				.getHeight() * s);
		int imgw = (int) (img.getWidth() * s), imgh = (int) (img.getHeight() * s);
		g.drawImage(textimg, textx - textw / 2, texty - texth / 2, textw,
				texth, null);
		g.drawImage(img, x, y - imgh / 2, imgw, imgh, null);
		g.setComposite(AlphaComposite.SrcOver);
	}
}
