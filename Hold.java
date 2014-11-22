package cytus;

import java.awt.*;
import java.awt.image.*;
import cytus.animation.*;

public class Hold extends Note {
	public int y2 = 0;
	double judgetime1 = -1, judgetime2 = -1;
	BufferedImage bar = null;
	Sprite head = null, nact = null, shadow = null, light = null,
			nearadd = null;
	Sprite bshadow = null;
	Animation hold1 = null, hold2 = null, light2 = null;
	boolean playsound = false;

	public Hold(NoteChartPlayer p, int id, int x, int y, double time,
			double hold) {
		this.p = p;
		this.id = id;
		this.x = x;
		this.y = y;
		this.stime = time;
		this.etime = time + hold;
		y2 = p.calcY(etime);
		page = p.calcPage(time);

		head = new Sprite("beat_hold_active");
		head.moveTo(x, y);
		bshadow = new Sprite("beat_shadow");
		bshadow.moveTo(x, y);
		bshadow.addTransform(new Transform(Transform.ALPHA, Transform.LINEAR,
				stime, stime + 1 / 6.0, 1, 0).asLoopTransform());
		bshadow.addTransform(new Transform(Transform.SCALE, Transform.LINEAR,
				stime, stime + 1 / 6.0, 0, 3).asLoopTransform());
		hold1 = AnimationPreset.get("hold_pressing_1");
		hold2 = AnimationPreset.get("hold_pressing_2");
		hold1.moveTo(x, y);
		hold2.moveTo(x, y);
		hold1.setStartTime(stime);
		hold1.setEndTime(stime + 0.75);
		hold2.setStartTime(stime + 0.75);
		bar = SpriteLibrary.get("hold_track");
		light = new Sprite("light_add");
		light.brighten();
		light2 = AnimationPreset.get("light_add_2");
		light2.specialPaint(p.buf);
		light2.setAnchor(0.5, 0.3);
		light2.brighten();
		shadow = new Sprite("shadow");
		shadow.setAnchor(0.5, 0.1);
		nact = new Sprite("flash_01");
		nact.moveTo(x, y);
		nearadd = new Sprite("near_add");
		nearadd.moveTo(x, y);
		nearadd.specialPaint(p.buf);

		double ntime = time - p.beat;
		head.addTransform(new Transform(Transform.ALPHA, Transform.LINEAR,
				ntime, ntime + p.beat / 2, 0.5, 1));
		nact.addTransform(new Transform(Transform.ALPHA, Transform.LINEAR,
				ntime, ntime + p.beat / 2, 0.5, 1));

		if (page % 2 == 1) {
			head.flip();
			hold1.flip();
			hold2.flip();
			light.flip();
			light2.flip();
			shadow.flip();
		}
	}

	public void paint(Graphics2D g) {
		if (p.time > stime) {
			if ((judgetime1 != -1) && (!playsound)) {
				playsound = true;
				try {
					p.sound.start();
				} catch (Exception e) {
				}
			}

			g.drawImage(bar, x - bar.getWidth() / 2, y, x + bar.getWidth() / 2,
					y2, bar.getWidth(), bar.getHeight(), 0, 0, null);
			nearadd.paint(g, p.time);
			hold1.paint(g, p.time);
			hold2.paint(g, p.time);
			bshadow.paint(g, p.time);

			if (judgetime1 == -1) {
				if (p.time - stime > 0.3) {
					p.addCombo(-1); // Miss
					p.notes.remove(this);
					Animation vanish = AnimationPreset.get("beat_vanish");
					Animation miss = AnimationPreset.get("judge_miss");
					vanish.moveTo(x, y);
					miss.moveTo(x, y);
					vanish.play(p, p.time);
					miss.play(p, p.time);
				}
			} else {
				int liney = p.calcY(p.time);
				shadow.moveTo(x, liney);
				shadow.paint(g, p.time);
				light2.moveTo(x, liney);
				light2.paint(g, p.time);
				light.moveTo(x, liney);
				light.paint(g, p.time);
				if (p.time > etime)
					judge(etime);
				if ((judgetime2 != -1) && (p.time > judgetime2)) {
					p.addCombo(judgement);
					p.notes.remove(this);
				}
			}
		} else {
			double time = p.time + p.beat;
			if (time > etime)
				time = etime;

			int csy = (int) ((time - stime) / p.beat * 240) + 247;
			csy = (int) (csy * NoteChartPlayer.SIZE_FIX);
			int liney = p.calcY(time);
			g.drawImage(bar, x - bar.getWidth() / 2, y, x + bar.getWidth() / 2,
					liney, bar.getWidth(), bar.getHeight(), 0, 0, null);
			head.paint(g, p.time);
			// if (p.time + p.beat * 0.4 >= stime) nearadd.paint(g, p.time);

			/*
			 * if (p.page < page) nact.paint(g, p.time);
			 */
		}
		if (NoteChartPlayer.prefs.get("showid") == 1) {
			g.setColor(Color.GREEN);
			g.drawString(String.valueOf(id), x, y);
			g.setColor(Color.BLACK);
		}
	}

	public void judge(double time) {
		if (judgetime1 == -1) {
			if (time < stime)
				judgetime1 = stime;
			else
				judgetime1 = time;
		} else if (judgetime2 == -1) {
			judgetime2 = time;
			Animation expanim = null;
			Animation judgeanim = null;
			Animation vanish = AnimationPreset.get("beat_vanish");
			vanish.moveTo(x, y);
			vanish.play(p, time);

			double d = (time - judgetime1) / (etime - stime);
			if (d >= 0.9) {
				judgement = 0; // Perfect TP100
				expanim = AnimationPreset.get("critical_explosion");
				judgeanim = AnimationPreset.get("judge_perfect");
			}
			if ((d >= 0.75) && (d <= 0.9)) {
				judgement = 1; // Perfect TP70
				expanim = AnimationPreset.get("explosion");
				judgeanim = AnimationPreset.get("judge_perfect");
			}
			if ((d >= 0.5) && (d < 0.75)) {
				judgement = 2; // Good
				expanim = AnimationPreset.get("explosion");
				judgeanim = AnimationPreset.get("judge_good");
			}
			if (d < 0.5) {
				judgement = 3; // Bad
				judgeanim = AnimationPreset.get("judge_bad");
			}
			if (expanim != null) { // judgement!=3
				expanim.moveTo(x, y2);
				expanim.play(p, time);
			}
			judgeanim.moveTo(x, y2);
			judgeanim.play(p, time);
		}
	}
}