package cytus;

import java.awt.*;
import java.awt.geom.*;
import java.awt.image.*;
import java.util.*;
import cytus.animation.*;

public class Circle extends Note {
	Sprite circle = null, nact = null, nearadd = null;;
	Animation shadow = null;

	public Circle(PatternPlayer p, int id, int x, int y, double time) {
		this.p = p;
		this.id = id;
		this.x = x;
		this.y = y;
		this.stime = time;
		this.etime = time;
		page = p.calcPage(time);
		circle = page % 2 == 0 ? new Sprite("red_active") : new Sprite(
				"yellow_active");
		circle.moveTo(x, y);
		nact = new Sprite("flash_01");
		nact.moveTo(x, y);
		nearadd = new Sprite("near_add");
		nearadd.moveTo(x, y);
		nearadd.specialPaint(p.buf);

		double ntime1 = time - p.beat;
		double ntime2 = page * p.beat - p.pshift;

		switch (PatternPlayer.prefs.get("popupmode")) {
		case 1:// Default
			circle.addTransform(new LinearAlphaTransform(ntime1, ntime1
					+ p.beat / 2, 0, 1));
			circle.addTransform(new LinearScaleTransform(ntime1, time, 0.5, 1));
			nearadd.addTransform(new LinearScaleTransform(ntime1, time, 0.5, 1));
			nact.addTransform(new LinearAlphaTransform(ntime1, ntime1 + p.beat
					/ 2, 0, 0.8));
			nact.addTransform(new LinearScaleTransform(ntime1, time, 0.5, 1));
			break;

		case 2:// Grouped
			circle.addTransform(new LinearAlphaTransform(ntime1, ntime1
					+ p.beat / 2, 0, 1));
			circle.addTransform(new LinearScaleTransform(ntime2 - p.beat / 2,
					ntime2, 0.5, 1));
			circle.addTransform(new BeatTransform(ntime2, p.pshift, p.beat / 2));
			nearadd.addTransform(new LinearScaleTransform(ntime2 - p.beat / 2,
					ntime2, 0.5, 1));
			nearadd.addTransform(new BeatTransform(ntime2, p.pshift, p.beat / 2));
			nact.addTransform(new LinearAlphaTransform(ntime1, ntime1 + p.beat
					/ 2, 0, 0.8));
			nact.addTransform(new LinearScaleTransform(ntime2 - p.beat / 2,
					ntime2, 0.5, 1));
			break;

		default: // None
			circle.addTransform(new LinearAlphaTransform(ntime1, ntime1
					+ p.beat / 2, 0, 0.8));
			circle.addTransform(new BeatTransform(ntime2, p.pshift, p.beat / 2));
			nearadd.addTransform(new BeatTransform(ntime2, p.pshift, p.beat / 2));
			nact.addTransform(new LinearAlphaTransform(ntime1, ntime1 + p.beat
					/ 2, 0, 0.8));
		}

		shadow = new Animation("beat_shadow", time - p.beat * 0.4,
				time + 1 / 3.0);
		shadow.moveTo(x, y);
		shadow.addTransform(new LinearScaleTransform(time - p.beat * 0.4, time,
				1, 2, false));
		shadow.addTransform(new LinearAlphaTransform(time - p.beat * 0.4, time,
				0.8, 0, false));
		shadow.play(p);
	}

	public void paint(Graphics2D g) {
		if ((judgement != -1) && (p.time >= judgetime)) {
			p.addCombo(judgement);
			p.notes.remove(this);
			return;
		}
		if (p.time > stime + 0.3) {
			p.addCombo(-1); // Miss
			Animation judgeanim = AnimationPreset.get("judge_miss");
			judgeanim.moveTo(x, y);

			circle.addTransform(new LinearAlphaTransform(p.time,
					p.time + 1 / 3.0, 1, 0));
			judgeanim.addChild(circle);

			judgeanim.play(p, p.time);
			p.notes.remove(this);
		}
		circle.paint(g, p.time);
		if (p.time + p.beat * 0.4 >= stime)
			nearadd.paint(g, p.time);
		if (p.page < page)
			nact.paint(g, p.time);
		if (PatternPlayer.prefs.get("showid") == 1) {
			g.setColor(Color.GREEN);
			g.drawString(String.valueOf(id), x, y);
			g.setColor(Color.BLACK);
		}
	}

	public void judge(double time) {
		judgetime = time;
		Animation expanim = null;
		Animation judgeanim = null;
		Animation blow = new Animation(page % 2 == 0 ? "red_blow"
				: "yellow_blow", time, time + 1 / 6.0);
		blow.addTransform(new RotateTransform(time, time + 1 / 6.0, 0, -Math.PI));
		blow.addTransform(new LinearScaleTransform(time, time + 1 / 6.0, 1, 3));
		blow.addTransform(new LinearAlphaTransform(time, time + 1 / 6.0, 1, 0));
		blow.moveTo(x, y);

		double d = Math.abs(time - stime);
		if (d <= 0.075) {
			judgement = 0; // Perfect TP100
			expanim = AnimationPreset.get("critical_explosion");
			judgeanim = AnimationPreset.get("judge_perfect");
			blow.play(p);
		}
		if ((d > 0.075) && (d <= 0.150)) {
			judgement = 1; // Perfect TP70
			expanim = AnimationPreset.get("explosion");
			judgeanim = AnimationPreset.get("judge_perfect");
			blow.play(p);
		}
		if ((d > 0.150) && (d <= 0.225)) {
			judgement = 2; // Good
			expanim = AnimationPreset.get("explosion");
			judgeanim = AnimationPreset.get("judge_good");
			blow.play(p);
		}
		if (d > 0.225) {
			judgement = 3; // Bad
			judgeanim = AnimationPreset.get("judge_bad");
			circle.addTransform(new LinearAlphaTransform(time, time + 1 / 3.0,
					1, 0));
			judgeanim.addChild(circle);
		}
		if (expanim != null) { // judgement!=3
			expanim.moveTo(x, y);
			expanim.play(p, time);
			// shadow.clearTransforms();
			shadow.addTransform(new LinearScaleTransform(time, time + 1 / 3.0,
					1.6, 1.6, false));
			shadow.addTransform(new LinearAlphaTransform(time, time + 1 / 3.0,
					0.8, 0.3, false));
		}
		judgeanim.moveTo(x, y);
		judgeanim.play(p, time);
	}
}