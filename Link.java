package cytus;

import java.awt.*;
import java.awt.geom.*;
import java.awt.image.*;
import java.util.*;
import cytus.animation.*;

public class Link extends Note {
	int n = 0;
	public ArrayList<Node> nodes = new ArrayList<Node>();
	Sprite head = null, shadow = null, nearadd = null;
	Animation arrow = null, arrexp = null, dlight = null, blow = null;
	boolean endblow = false;

	public Link(NoteChartPlayer p) {
		this.p = p;
		head = new Sprite("drag_head_active");
		shadow = new Sprite("shadow");
		shadow.setAnchor(0.5, 0);
		nearadd = new Sprite("near_add");
		nearadd.specialPaint(p.buf);
		arrow = AnimationPreset.get("arrow_flash");
		dlight = AnimationPreset.get("drag_light");
		dlight.specialPaint(p.buf);
		dlight.setAnchor(0.5, 0.3);
		arrexp = AnimationPreset.get("arrow_explode");
	}

	public double calcAngle(int p, int q) {
		Node n1 = nodes.get(p);
		Node n2 = nodes.get(q);
		double angle = Math.atan((double) (n2.x - n1.x)
				/ (double) Math.abs(n2.y - n1.y));

		if (n1.y < n2.y)
			angle = Math.PI - angle;

		return angle;
	}

	public void paint(Graphics2D g) {
		int i = 0;
		double nodesize = 24 * NoteChartPlayer.SIZE_FIX;
		if (p.time >= stime) {
			for (i = n - 1; i > 0; i--)
				if (nodes.get(i).stime >= p.time) {
					double angle = calcAngle(i - 1, i);
					double x1 = nodes.get(i - 1).x + nodesize * Math.sin(angle);
					double y1 = nodes.get(i - 1).y - nodesize * Math.cos(angle);
					double x2 = nodes.get(i).x - nodesize * Math.sin(angle);
					double y2 = nodes.get(i).y + nodesize * Math.cos(angle);
					Line2D.Double line = new Line2D.Double(x1, y1, x2, y2);
					g.draw(line);
				}

			for (i = 0; i < n; i++)
				nodes.get(i).paint(g);
			for (i = 0; i < n; i++)
				if (p.time < nodes.get(i).stime)
					break;
			if (i == n)
				i = n - 1;

			if (p.time >= etime) {
				if (!endblow) {
					Node lastnode = nodes.get(n - 1);
					if (lastnode.judgement != -1) {
						dlight.moveTo(lastnode.x, lastnode.y);
						dlight.addTransform(new Transform(Transform.ROTATION,
								Transform.LINEAR, etime, etime + 1 / 3.0, 0,
								-Math.PI));
						blow.addChild(dlight, true);
						arrexp.play(p);
						blow.play(p);
					}
					endblow = true;
				}
				if (p.time > etime + 0.3)
					p.notes.remove(this);
				return;
			}
			double pos = (p.time - nodes.get(i - 1).stime)
					/ (nodes.get(i).stime - nodes.get(i - 1).stime);
			int cx = (int) ((nodes.get(i).x - nodes.get(i - 1).x) * pos + nodes
					.get(i - 1).x);
			int cy = (int) ((nodes.get(i).y - nodes.get(i - 1).y) * pos + nodes
					.get(i - 1).y);
			double angle = calcAngle(i - 1, i);
			shadow.moveTo(cx, cy);
			shadow.rotate(angle);
			shadow.paint(g, p.time);
			head.moveTo(cx, cy);
			head.paint(g, p.time);
			nearadd.moveTo(cx, cy);
			nearadd.paint(g, p.time);
			arrow.moveTo(cx, cy);
			arrow.rotate(angle);
			arrow.paint(g, p.time);
			dlight.moveTo(cx, cy);
			dlight.rotate(angle);
			dlight.paint(g, p.time);
		} else {
			int end = n;
			for (i = 1; i < n; i++)
				if (p.time + p.beat < nodes.get(i).stime) {
					end = i;
					break;
				}
			for (i = 0; i < end - 1; i++) {
				double angle = calcAngle(i, i + 1);
				double x1 = nodes.get(i).x + nodesize * Math.sin(angle);
				double y1 = nodes.get(i).y - nodesize * Math.cos(angle);
				double x2 = nodes.get(i + 1).x - nodesize * Math.sin(angle);
				double y2 = nodes.get(i + 1).y + nodesize * Math.cos(angle);
				Line2D.Double line = new Line2D.Double(x1, y1, x2, y2);
				g.draw(line);
			}
			for (i = 0; i < end; i++) {
				nodes.get(i).paint(g);
			}

			// x=nodes.get(0).x , y=nodes.get(0).y ,stime = nodes.get(0).stime
			head.moveTo(x, y);
			head.paint(g, p.time);
			nearadd.moveTo(x, y);
			if (p.time + p.beat * 0.4 >= stime)
				nearadd.paint(g, p.time);

			if (p.page == page) {
				arrow.moveTo(x, y);
				arrow.rotate(calcAngle(0, 1));
				arrow.paint(g, p.time);
			}
		}
	}

	public void judge(double time) {
	}

	public void recalc() {
		n = nodes.size();
		x = nodes.get(0).x;
		y = nodes.get(0).y;
		stime = nodes.get(0).stime;
		etime = nodes.get(n - 1).stime;
		page = p.calcPage(stime);

		double ntime = page * p.beat - p.pshift;
		head.clearTransforms();
		head.addTransform(new Transform(Transform.ALPHA, Transform.LINEAR,
				stime - p.beat, stime - p.beat / 2, 0, 1));

		switch (NoteChartPlayer.prefs.get("popupmode")) {
		case 0:// None
			break;
		case 1:// Default
			head.addTransform(new Transform(Transform.SCALE, Transform.LINEAR,
					stime - p.beat / 2, stime, 0.8, 1));
			nearadd.addTransform(new Transform(Transform.SCALE,
					Transform.LINEAR, stime - p.beat / 2, stime, 0.8, 1));
			break;

		case 2:// Grouped
			head.addTransform(new Transform(Transform.SCALE, Transform.LINEAR,
					ntime - p.beat / 2, ntime, 0.5, 1));
			nearadd.addTransform(new Transform(Transform.SCALE,
					Transform.LINEAR, ntime - p.beat / 2, ntime, 0.5, 1));
			break;
		}

		arrexp.moveTo(nodes.get(n - 1).x, nodes.get(n - 1).y);
		arrexp.rotate(calcAngle(n - 2, n - 1));
		blow = new Animation("drag_head_blow", etime, etime + 1 / 3.0);
		blow.moveTo(nodes.get(n - 1).x, nodes.get(n - 1).y);
		blow.clearTransforms();
		blow.addTransform(new Transform(Transform.ROTATION, Transform.LINEAR,
				etime, etime + 1 / 3.0, 0, -Math.PI * 1.5));
		blow.addTransform(new Transform(Transform.SCALE, Transform.LINEAR,
				etime, etime + 1 / 3.0, 1, 2));
		blow.addTransform(new Transform(Transform.ALPHA, Transform.LINEAR,
				etime, etime + 1 / 3.0, 1, 0.5));
	}

	public class Node extends Note {
		Animation nflash = null, nexp = null, perfect = null;
		Sprite ps = null, nps = null;
		boolean removed = false;

		public Node(int id, int x, int y, double time) {
			this.p = Link.this.p;
			this.id = id;
			this.x = x;
			this.y = y;
			this.page = p.calcPage(time);
			this.stime = time;
			this.etime = time;

			nflash = AnimationPreset.get("node_flash");
			nflash.moveTo(x, y);
			nflash.setStartTime(page * p.beat - p.pshift);
			nflash.setEndTime(nflash.getStartTime() + 1 / 3.0);

			ps = new Sprite("node_flash_04");
			ps.moveTo(x, y);
			ps.addTransform(new Transform(Transform.ALPHA, Transform.LINEAR,
					time - p.beat, time - p.beat / 2, 0, 1));

			nps = new Sprite("node_flash_01");
			nps.moveTo(x, y);
			nps.addTransform(new Transform(Transform.ALPHA, Transform.LINEAR,
					time - p.beat, time - p.beat / 2, 0, 1));
		}

		public void paint(Graphics2D g) {
			if (removed)
				return;
			if ((judgement != -1) && (p.time >= judgetime)) {
				p.addCombo(judgement);
				removed = true;
				return;
			}
			if (page == p.page) {
				if (nflash.ended(p.time))
					ps.paint(g, p.time);
				else
					nflash.paint(g, p.time);
			} else
				nps.paint(g, p.time);

			if (p.time > stime + 0.3) {
				p.addCombo(-1); // Miss
				Animation judgeanim = AnimationPreset.get("judge_miss");
				judgeanim.moveTo(x, y);
				ps.addTransform(new Transform(Transform.ALPHA,
						Transform.LINEAR, p.time, p.time + 1 / 3.0, 1, 0));
				judgeanim.addChild(ps, true);
				judgeanim.play(p, p.time);
				removed = true;
			}

			if (NoteChartPlayer.prefs.get("showid") == 1) {
				g.setColor(Color.GREEN);
				g.drawString(String.valueOf(id), x, y);
				g.setColor(Color.BLACK);
			}
		}

		public void judge(double time) {
			judgetime = time;
			Animation judgeanim = null;
			Animation expanim = null;
			double d = Math.abs(time - stime);
			if (d < 0.1) {
				judgement = 0; // Perfect TP100
				judgeanim = AnimationPreset.get("judge_perfect");
				expanim = AnimationPreset.get("critical_explosion");
				judgeanim.prescale(0.75);
			}
			if (d >= 0.1) {
				judgement = 1; // Perfect TP70;
				judgeanim = AnimationPreset.get("judge_perfect");
				if (time > stime)
					expanim = AnimationPreset.get("explosion");
				else
					expanim = AnimationPreset.get("node_explode");
			}
			expanim.moveTo(x, y);
			judgeanim.moveTo(x, y);
			expanim.play(p, time);
			judgeanim.play(p, time);
		}
	}
}