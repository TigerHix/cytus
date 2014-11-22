package cytus.animation;

import cytus.*;
import java.awt.*;
import java.awt.image.*;

public class MaskBeat extends Sprite {
	BufferedImage img = null;
	double pshift = 0, beat = 0;

	public MaskBeat(double pshift, double beat) {
		img = SpriteLibrary.get("gameplay_bg_mask_3");
		this.pshift = pshift;
		this.beat = beat;
	}

	public void paint(Graphics2D g, double time) {
		double pos = ((time + pshift) % beat) / beat;
		double size = 0.7 + 0.3 * Math.max(Math.cos(pos * Math.PI * 2), 0);
		g.drawImage(img, 0, NoteChartPlayer.HEIGHT / 10,
				(int) (img.getWidth() * size), NoteChartPlayer.HEIGHT * 9 / 10,
				null);
		g.drawImage(img, NoteChartPlayer.WIDTH, NoteChartPlayer.HEIGHT / 10,
				NoteChartPlayer.WIDTH - (int) (img.getWidth() * size),
				NoteChartPlayer.HEIGHT, 0, 0, img.getWidth(), img.getHeight(),
				null);
	}
}