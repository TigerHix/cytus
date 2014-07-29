package cytus;

import java.awt.*;
import java.awt.geom.*;
import java.awt.image.*;
import java.util.*;
import cytus.animation.*;

public class Link extends Note {
	int n = 0, last = 0;
	public ArrayList<Node> nodes = new ArrayList<Node>();
	Sprite head = null, shadow = null;
	Animation arrow = null, arrexp = null, dlight = null, blow = null;

	public Link(Pattern p) {
		this.p = p;
		head = new Sprite("drag_head_active");
		shadow = new Sprite("shadow");
		shadow.setAnchor(0.5, 0);
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

		if (p.time >= stime) {
			for (i = n - 1; i > 0; i--)
				if (nodes.get(i).stime >= p.time) {
					double angle = calcAngle(i - 1, i);
					double x1 = nodes.get(i - 1).x + 24 * Math.sin(angle);
					double y1 = nodes.get(i - 1).y - 24 * Math.cos(angle);
					double x2 = nodes.get(i).x - 24 * Math.sin(angle);
					double y2 = nodes.get(i).y + 24 * Math.cos(angle);
					Line2D.Double line = new Line2D.Double(x1, y1, x2, y2);
					g.draw(line);

					nodes.get(i).paint(g);
				}

			for (i = 0; i < n; i++)
				if (p.time < nodes.get(i).stime)
					break;

			if (last != i) {
				p.addCombo(i - last);
				last = i;
			}

			if (i == n)
				i = n - 1;

			double pos = (double) (p.time - nodes.get(i - 1).stime)
					/ (double) (nodes.get(i).stime - nodes.get(i - 1).stime);
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
			arrow.moveTo(cx, cy);
			arrow.rotate(angle);
			arrow.paint(g, p.time);
			dlight.moveTo(cx, cy);
			dlight.rotate(angle);
			dlight.paint(g, p.time);
		} else {
			if (p.time + p.beat / 2 >= nodes.get(0).stime)
				head.bright();

			int end = n;

			for (i = 1; i < n; i++)
				if (p.time + p.beat < nodes.get(i).stime) {
					end = i;
					break;
				}

			for (i = 0; i < end - 1; i++) {
				double angle = calcAngle(i, i + 1);
				double x1 = nodes.get(i).x + 24 * Math.sin(angle);
				double y1 = nodes.get(i).y - 24 * Math.cos(angle);
				double x2 = nodes.get(i + 1).x - 24 * Math.sin(angle);
				double y2 = nodes.get(i + 1).y + 24 * Math.cos(angle);
				Line2D.Double line = new Line2D.Double(x1, y1, x2, y2);
				g.draw(line);
			}

			for (i = 0; i < end; i++) {
				nodes.get(i).paint(g);
			}

			head.moveTo(nodes.get(0).x, nodes.get(0).y);
			head.paint(g, p.time);

			if (p.page == page) {
				arrow.moveTo(nodes.get(0).x, nodes.get(0).y);
				arrow.rotate(calcAngle(0, 1));
				arrow.paint(g, p.time);
			}
		}
	}

	public void remove() {
		p.addCombo(n - last);

		blow.addChild(dlight);
		dlight.moveTo(nodes.get(n - 1).x, nodes.get(n - 1).y);
		dlight.addTransform(new RotateTransform(etime, etime + 1 / 3.0, 0,
				-Math.PI));
	}

	public void recalc() {
		n = nodes.size();
		x = nodes.get(0).x;
		y = nodes.get(0).y;
		stime = nodes.get(0).stime;
		etime = nodes.get(n - 1).stime;
		page = (int) ((stime + p.offset) / p.beat);

		double ntime = page * p.beat - p.offset;
		head.clearTransforms();
		head.addTransform(new LinearAlphaTransform(stime - p.beat, stime
				- p.beat / 2, 0, 1));

		switch (Pattern.prefs.get("popupmode")) {
		case 1:// Default
			head.addTransform(new LinearScaleTransform(stime - p.beat / 2,
					stime, 0.8, 1));
			break;

		case 2:// Grouped
			head.addTransform(new LinearScaleTransform(ntime - p.beat / 2,
					ntime, 0.5, 1));
			break;
		}

		arrexp.moveTo(nodes.get(n - 1).x, nodes.get(n - 1).y);
		arrexp.rotate(calcAngle(n - 2, n - 1));
		arrexp.play(p, etime);

		blow = new Animation("drag_head_blow", etime, etime + 1 / 3.0);
		blow.moveTo(nodes.get(n - 1).x, nodes.get(n - 1).y);
		blow.clearTransforms();
		blow.addTransform(new RotateTransform(etime, etime + 1 / 3.0, 0,
				-Math.PI * 1.5));
		blow.addTransform(new LinearScaleTransform(etime, stime + 1 / 3.0, 1, 2));
		blow.addTransform(new LinearAlphaTransform(etime, etime + 1 / 3.0, 1, 0));
		blow.play(p, etime);

		for (Node node : nodes)
			node.recalc();
	}

	public class Node extends Note {
		Animation nflash = null, nexp = null, perfect = null;
		Sprite ps = null, nps = null;

		public Node(int x, int y, double time) {
			this.p = Link.this.p;
			this.x = x;
			this.y = y;
			this.stime = time;
			this.etime = time;

			nflash = AnimationPreset.get("node_flash");
			nflash.moveTo(x, y);
			nflash.setStartTime(page * p.beat - p.offset);
			nflash.setEndTime(time);

			nexp = AnimationPreset.get("critical_explosion");
			nexp.moveTo(x, y);
			nexp.play(p, time);
			nexp.scale(0.6, 0.6);

			perfect = AnimationPreset.get("judge_perfect");
			perfect.moveTo(x, y);
			perfect.play(p, time);
			perfect.scale(0.6, 0.6);

			ps = new Sprite("node_flash_05");
			ps.moveTo(x, y);
			ps.addTransform(new LinearAlphaTransform(time - p.beat, time
					- p.beat / 2, 0, 1));

			nps = new Sprite("node_flash_01");
			nps.moveTo(x, y);
			nps.addTransform(new LinearAlphaTransform(time - p.beat, time
					- p.beat / 2, 0, 1));
		}

		public void paint(Graphics2D g) {
			if (page == p.page) {
				if (nflash.ended(p.time + p.beat))
					ps.paint(g, p.time);
				else
					nflash.paint(g, p.time);
			} else
				nps.paint(g, p.time);
		}
		
		public void remove(){
		}
	}
}