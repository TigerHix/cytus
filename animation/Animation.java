package cytus.animation;

import cytus.*;
import java.awt.*;
import java.awt.image.*;

public class Animation extends Sprite {
	int n = 1;
	double stime = 0, etime = 0;
	double len = 1;
	boolean once = false;
	String frames[] = null;
	BufferedImage img[] = null;

	protected Animation() {
	}

	public Animation(String str, double stime, double etime) {
		this(1, etime - stime, true, new String[] { str });
		this.stime = stime;
		this.etime = etime;
	}

	public Animation(int n, double len, boolean once, String frames[]) {
		this.n = n;
		this.len = len / n;
		this.once = once;
		this.frames = frames;
		img = new BufferedImage[n];

		for (int i = 0; i < n; i++)
			img[i] = SpriteLibrary.get(frames[i]);
	}

	public void paint(Graphics2D g, double time) {
		if (time < stime)
			return;

		if (once && (time >= etime))
			return;

		int current = (int) ((time - stime) / len);
		current %= n;
		cimg = img[current];
		super.paint(g, time);
	}

	public void play(Pattern p) {
		play(p, stime);
	}

	public void play(Pattern p, double stime) {
		this.stime = stime;
		etime = stime + len * n;
		p.addAnimation(this);
	}

	public void setStartTime(double stime) {
		this.stime = stime;
	}

	public double getStartTime() {
		return stime;
	}

	public void setEndTime(double etime) {
		this.etime = etime;
	}

	public double getEndTime() {
		return etime;
	}

	public boolean started(double time) {
		return time >= stime;
	}

	public boolean ended(double time) {
		return time > etime;
	}

	public void flip() {
		for (int i = 0; i < n; i++) {
			img[i] = SpriteLibrary.getFlip(frames[i]);
			frames[i] = "flip_" + frames[i];
		}

		ay = 1 - ay;
	}

	public void brighten() {
		for (int i = 0; i < n; i++) {
			img[i] = SpriteLibrary.getBrighter(frames[i]);
			frames[i] = "bright_" + frames[i];
		}
	}

	public void prescale(double s) {
		for (int i = 0; i < n; i++) {
			img[i] = SpriteLibrary.getScaledSprite(frames[i], s);
			frames[i] += s;
		}
	}
}