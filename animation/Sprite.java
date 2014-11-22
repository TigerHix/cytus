package cytus.animation;

import cytus.*;
import java.awt.*;
import java.awt.geom.*;
import java.awt.image.*;
import java.util.*;

public class Sprite {
	public Sprite father = null;
	public BufferedImage img = null;
	public String name = null;
	public BufferedImage dst = null;
	public double x = 0, y = 0;
	public double angle = 0, alpha = 1, sx = 1, sy = 1, ax = 0.5, ay = 0.5;
	public boolean special = false;
	public LinkedList<Transform> trans = new LinkedList<Transform>();
	public LinkedList<Sprite> childs = new LinkedList<Sprite>();
	public boolean bright = false;
	public boolean independent = false;

	public Sprite() {
	}

	public Sprite(BufferedImage img) {
		this.img = img;
	}

	public Sprite(String name) {
		this.name = name;
		this.img = SpriteLibrary.get(name);
	}

	public void paint(Graphics2D g, double time) {
		for (Transform t : trans)
			t.adjust(this, time);
		for (Sprite s : childs)
			s.paint(g, time);

		if (img == null)
			return;
		if (alpha <= 0)
			return;

		AffineTransform tx = getAffineTransform();

		if (!special) {
			if (alpha >= 1)
				g.setComposite(AlphaComposite.SrcOver);
			else
				g.setComposite(AlphaComposite.SrcOver.derive((float) alpha));
			g.drawImage(img, tx, null);
			g.setComposite(AlphaComposite.SrcOver);
		} else
			ImageUtil.drawImageF(img, dst, tx, alpha);
	}

	public AffineTransform getAffineTransform() {
		AffineTransform t = new AffineTransform();
		if (img == null) {
			t.translate(x, y);
			t.rotate(angle);
			t.scale(sx, sy);
		} else {
			t.translate(x - img.getWidth() * sx * ax, y - img.getHeight() * sy
					* ay);
			t.rotate(angle, img.getWidth() * sx * ax, img.getHeight() * sy * ay);
			t.scale(sx, sy);
		}
		if ((!independent) && (father != null))
			t.preConcatenate(father.getAffineTransform());
		return t;
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

	public void setAnchor(String name) {
		switch (name) {
		case "Center":
			ax = 0.5;
			ay = 0.5;
			break;
		case "Top":
			ax = 0.5;
			ay = 0;
			break;
		case "Bottom":
			ax = 0.5;
			ay = 1;
			break;
		case "TopLeft":
			ax = 0;
			ay = 0;
			break;
		case "TopRight":
			ax = 1;
			ay = 0;
			break;
		case "Left":
			ax = 0;
			ay = 0.5;
			break;
		case "Right":
			ax = 1;
			ay = 0.5;
			break;
		case "BottomLeft":
			ax = 0;
			ay = 1;
			break;
		case "BottomRight":
			ax = 1;
			ay = 1;
			break;
		}
	}

	public void setAlpha(double alpha) {
		this.alpha = alpha;
	}

	public void moveTo(double x, double y) {
		this.x = x;
		this.y = y;
	}

	public void specialPaint(BufferedImage dst) {
		special = true;
		this.dst = dst;
	}

	public void addTransform(Transform t) {
		trans.add(t);
	}

	public void removeTransform(Transform t) {
		trans.remove(t);
	}

	public void clearTransforms() {
		trans.clear();
	}

	public void addChild(Sprite s, boolean independent) {
		s.father = this;
		if (!independent) {
			s.x = s.x - this.x;
			s.y = s.y - this.y;
		}
		s.independent = independent;
		childs.add(s);
	}

	public void removeChild(Sprite s) {
		s.father = null;
		childs.remove(s);
	}

	public void prescale(double s) {
		if (name != null) {
			img = SpriteLibrary.getScaledSprite(name, s);
			name += s;
		} else
			img = ImageUtil.scale(img, s);
	}

	public void brighten() {
		if (!bright) {
			bright = true;

			if (name != null) {
				img = SpriteLibrary.getBrighter(name);
				name = "bright_" + name;
			} else
				ImageUtil.brighter(img, 1.5);
		}
	}

	public void flip() {
		if (name != null) {
			img = SpriteLibrary.getFlip(name);
			name = "flip_" + name;
		} else
			img = ImageUtil.flipV(img);

		ay = 1 - ay;
	}
}