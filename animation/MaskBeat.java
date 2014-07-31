package cytus.animation;

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
		// g.setComposite(AlphaComposite.SrcOver.derive(0.5f));
		g.drawImage(img, 0, 64, (int) (img.getWidth() * size), 640, null);
		g.drawImage(img, 960, 64, 960 - (int) (img.getWidth() * size), 640, 0,
				0, img.getWidth(), img.getHeight(), null);
		// g.setComposite(AlphaComposite.SrcOver);
	}
}