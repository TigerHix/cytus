package cytus;

import java.awt.*;
import java.awt.image.*;
import cytus.animation.*;

public class Hold extends Note {
	public int y2 = 0, sy = 0;
	BufferedImage bar = null;
	Sprite head = null, nact = null, shadow = null, light = null;
	Sprite bshadow = null;
	Animation hold1 = null, hold2 = null, light2 = null;
	boolean playsound = false;

	public Hold(Pattern p, int x, int y, double time, double hold) {
		this.p = p;
		this.x = x;
		this.y = y;
		this.stime = time;
		this.etime = time + hold;
		page = p.calcPage(time);
		int page2 = p.calcPage(etime);
		if (page != page2)
			etime = (page + 1) * p.beat - p.offset - 1e-3;
		y2 = p.calcY(etime);

		sy = (int) ((hold / p.beat * 300) + 309);
		sy = (int) (sy * Pattern.SIZE_FIX);

		head = new Sprite("beat_hold_active");
		head.moveTo(x, y);
		bshadow = new Sprite("beat_shadow");
		bshadow.moveTo(x, y);
		bshadow.addTransform(new LoopAlphaTransform(stime, 1 / 6.0, 1, 0));
		bshadow.addTransform(new LoopScaleTransform(stime, 1 / 6.0, 0, 3));
		hold1 = AnimationPreset.get("hold_pressing_1");
		hold2 = AnimationPreset.get("hold_pressing_2");
		hold1.moveTo(x, y);
		hold2.moveTo(x, y);
		hold1.setStartTime(stime);
		hold1.setEndTime(stime + 0.75);
		hold2.setStartTime(stime + 0.75);
		bar = SpriteLibrary.get("hold_track");
		light = new Sprite("light_add");
		light2 = AnimationPreset.get("light_add_2");
		light2.specialPaint(p.buf);
		light2.setAnchor(0.5, 0.3);
		shadow = new Sprite("shadow");
		shadow.setAnchor(0.5, 0.1);
		nact = new Sprite("flash_01");
		nact.moveTo(x, y);

		double ntime = time - p.beat;
		nact.addTransform(new LinearAlphaTransform(ntime, ntime + p.beat / 2,
				0.5, 1));

		if (page % 2 == 1) {
			head.flip();
			hold1.flip();
			hold2.flip();
			light2.flip();
			light.flip();
			shadow.flip();
		}
	}

	public void paint(Graphics2D g) {
		if (p.time > stime) {
			if (!playsound) {
				playsound = true;

				try {
					p.sound.start();
				} catch (Exception e) {
				}
			}

			g.drawImage(bar, x - bar.getWidth() / 2, y, x + bar.getWidth() / 2,
					y2, 0, sy, bar.getWidth(), 0, null);
			hold1.paint(g, p.time);
			hold2.paint(g, p.time);
			bshadow.paint(g, p.time);

			int liney = p.calcY(p.time);
			shadow.moveTo(x, liney);
			shadow.paint(g, p.time);
			light2.moveTo(x, liney);
			light2.paint(g, p.time);
			light.moveTo(x, liney);
			light.paint(g, p.time);
		} else {
			double time = p.time + p.beat;

			if (time > etime)
				time = etime;

			int csy = (int) ((time - stime) / p.beat * 300) + 309;
			csy = (int) (csy * Pattern.SIZE_FIX);
			int liney = p.calcY(time);
			g.drawImage(bar, x - bar.getWidth() / 2, y, x + bar.getWidth() / 2,
					liney, 0, csy, bar.getWidth(), 0, null);
			head.paint(g, p.time);

			if (p.page < page)
				nact.paint(g, p.time);
		}
	}

	public void remove() {
		p.addCombo();

		Animation a1 = AnimationPreset.get("beat_vanish");
		a1.moveTo(x, y);
		Animation a2 = AnimationPreset.get("critical_explosion");
		a2.moveTo(x, y2);
		Animation a3 = AnimationPreset.get("judge_perfect");
		a3.moveTo(x, y2);
		a1.play(p, etime);
		a2.play(p, etime);
		a3.play(p, etime);
	}

	public void recalc() {
		super.recalc();
		int page2 = p.calcPage(etime);
		if (page != page2)
			etime = (page + 1) * p.beat - p.offset - 1e-3;
		y2 = p.calcY(etime);
	}
}