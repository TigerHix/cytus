package cytus;

import java.awt.*;
import java.awt.geom.*;
import java.awt.image.*;
import java.util.*;
import cytus.animation.*;

public class Circle extends Note {
	Sprite circle = null, nact = null;
	Animation shadow = null;

	public Circle(Pattern p, int x, int y, double time) {
		this.p = p;
		this.x = x;
		this.y = y;
		this.stime = time;
		this.etime = time;
		page = (int) ((time + p.offset) / p.beat);
		circle = page % 2 == 0 ? new Sprite("red_active") : new Sprite(
				"yellow_active");
		circle.moveTo(x, y);
		nact = new Sprite("flash_01");
		nact.moveTo(x, y);

		double ntime1 = time - p.beat;
		double ntime2 = page * p.beat - p.offset;

		switch (Pattern.prefs.get("popupmode")) {
		case 1:// Default
			circle.addTransform(new LinearAlphaTransform(ntime1, ntime1
					+ p.beat / 2, 0, 1));
			circle.addTransform(new LinearScaleTransform(ntime1, time, 0.5, 1));
			nact.addTransform(new LinearAlphaTransform(ntime1, ntime1 + p.beat
					/ 2, 0, 0.8));
			nact.addTransform(new LinearScaleTransform(ntime1, time, 0.5, 1));
			break;

		case 2:// Grouped
			circle.addTransform(new LinearAlphaTransform(ntime1, ntime1
					+ p.beat / 2, 0, 1));
			circle.addTransform(new LinearScaleTransform(ntime2 - p.beat / 2,
					ntime2, 0.5, 1));
			nact.addTransform(new LinearAlphaTransform(ntime1, ntime1 + p.beat
					/ 2, 0, 0.8));
			nact.addTransform(new LinearScaleTransform(ntime2 - p.beat / 2,
					ntime2, 0.5, 1));
			circle.addTransform(new BeatTransform(ntime2, p.offset, p.beat / 2));
			break;

		default: // None
			circle.addTransform(new LinearAlphaTransform(ntime1, ntime1
					+ p.beat / 2, 0, 1));
			nact.addTransform(new LinearAlphaTransform(ntime1, ntime1 + p.beat
					/ 2, 0, 0.8));
			circle.addTransform(new BeatTransform(ntime2, p.offset, p.beat / 2));
		}

		shadow = new Animation("beat_shadow", time - p.beat * 0.4,
				time + 1 / 3.0);
		shadow.moveTo(x, y);
		shadow.addTransform(new LinearScaleTransform(time - p.beat * 0.4, time,
				1, 2, false));
		shadow.addTransform(new LinearAlphaTransform(time - p.beat * 0.4, time,
				0.8, 0, false));
		shadow.addTransform(new LinearScaleTransform(time, time + 1 / 3.0, 1.6,
				1.6, false));
		shadow.addTransform(new LinearAlphaTransform(time, time + 1 / 3.0, 0.8,
				0.3, false));
		shadow.play(p);
	}

	public void paint(Graphics2D g) {
		if (p.time + p.beat / 2 >= stime)
			circle.bright();

		circle.paint(g, p.time);

		if (p.page < page)
			nact.paint(g, p.time);
	}

	public void remove() {
		p.addCombo();

		Animation a1 = AnimationPreset.get("critical_explosion");
		a1.moveTo(x, y);
		a1.scale(0.6, 0.6);
		Animation a2 = AnimationPreset.get("judge_perfect");
		a2.moveTo(x, y);
		Animation a3 = new Animation(
				page % 2 == 0 ? "red_blow" : "yellow_blow", stime,
				stime + 1 / 6.0);
		a3.addTransform(new RotateTransform(stime, stime + 1 / 6.0, 0, -Math.PI));
		a3.addTransform(new LinearScaleTransform(stime, stime + 1 / 6.0, 1, 3));
		a3.addTransform(new LinearAlphaTransform(stime, stime + 1 / 6.0, 1, 0));
		a3.moveTo(x, y);
		a1.play(p, stime);
		a2.play(p, stime);
		a3.play(p);
	}
}