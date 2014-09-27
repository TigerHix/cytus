package cytus.animation;

import cytus.*;
import java.awt.*;
import java.awt.image.*;

public class MaskBeat extends Sprite {
	BufferedImage img = null;
	double offset = 0, beat = 0;

	public MaskBeat(double offset, double beat) {
		img = SpriteLibrary.get("gameplay_bg_mask_3");
		this.offset = offset;
		this.beat = beat;
	}

	public void paint(Graphics2D g, double time) {
		double pos = ((time + offset) % beat) / beat;
		double size = 0.7 + 0.3 * Math.max(Math.cos(pos * Math.PI * 2), 0);
		g.drawImage(img, 0, Pattern.HEIGHT / 10, (int) (img.getWidth() * size),
				Pattern.HEIGHT * 9 / 10, null);
		g.drawImage(img, Pattern.WIDTH, Pattern.HEIGHT / 10, Pattern.WIDTH
				- (int) (img.getWidth() * size), Pattern.HEIGHT, 0, 0,
				img.getWidth(), img.getHeight(), null);
	}
}