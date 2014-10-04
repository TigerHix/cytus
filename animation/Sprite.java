package cytus.animation;

import cytus.*;
import java.awt.*;
import java.awt.geom.*;
import java.awt.image.*;
import java.util.*;

public class Sprite {
	BufferedImage cimg = null;
	String srcname = null;
	int x = 0, y = 0;
	int stime = 0, etime = 0;
	double angle = 0, alpha = 1, sx = 1, sy = 1, ax = 0.5, ay = 0.5;
	LinkedList<Transform> trans = new LinkedList<Transform>();
	LinkedList<Sprite> childs = new LinkedList<Sprite>();
	boolean bright = false;

	protected Sprite() {
	}

	public Sprite(BufferedImage img) {
		this.cimg = img;
	}

	public Sprite(String srcname) {
		this.srcname = srcname;
		this.cimg = SpriteLibrary.get(srcname);
	}

	public void paint(Graphics2D g, double time) {
		for (Sprite s : childs)
			s.paint(g, time);

		for (Transform t : trans)
			t.adjust(time);

		AffineTransform t = new AffineTransform();
		t.scale(sx, sy);
		t.translate(x / sx - cimg.getWidth() * ax, y / sy - cimg.getHeight()
				* ay);

		if (angle != 0)
			t.rotate(angle, cimg.getWidth() * ax, cimg.getHeight() * ay);

		g.setComposite(AlphaComposite.SrcOver.derive((float) alpha));
		g.drawImage(cimg, t, null);
		g.setComposite(AlphaComposite.SrcOver);
	}

	public void scale(double sx, double sy) {
		this.sx = sx;
		this.sy = sy;
	}

	public void rotate(double angle) {
		this.angle = angle;
	}

	public void setAnchor(double ax, double ay) {
		this.ax = ax;
		this.ay = ay;
	}

	public void setAlpha(double alpha) {
		this.alpha = alpha;
	}

	public void moveTo(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public void moveTo(double x, double y) {
		moveTo((int) x, (int) y);
	}

	public void addTransform(Transform t) {
		trans.add(t);
		t.setSprite(this);
	}

	public void removeTransform(Transform t) {
		trans.remove(t);
	}

	public void clearTransforms() {
		trans.clear();
	}

	public void addChild(Sprite s) {
		childs.add(s);
	}

	public void removeChild(Sprite s) {
		childs.remove(s);
	}

	public void prescale(double s) {
		if (srcname != null) {
			cimg = SpriteLibrary.getScaledSprite(srcname, s);
			srcname += s;
		} else
			cimg = ImageUtil.scale(cimg, s);
	}

	public void brighten() {
		if (!bright) {
			bright = true;

			if (srcname != null) {
				cimg = SpriteLibrary.getBrighter(srcname);
				srcname = "bright_" + srcname;
			} else
				ImageUtil.brighter(cimg, 1.8);
		}
	}

	public void flip() {
		if (srcname != null) {
			cimg = SpriteLibrary.getFlip(srcname);
			srcname = "flip_" + srcname;
		} else
			cimg = ImageUtil.flip(cimg);

		ay = 1 - ay;
	}
}