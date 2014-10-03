/*
 *  Kamcord.java
 *  An experimental tool for recording videos
 *  This takes a lot of time and it generates thousands of png files, 
 *  so please make sure that you have enough free disk space
 *  For instance, 10000 frames(720x480) use approximately 4GB
 *  Change rendering quality in preferences.txt
 *  When finished, use tools such as FFmpeg to convert picture sequence into a video file
 *  Example: ffmpeg -f image2 -framerate 60 -i pic/%05d.png -qscale 0 out.avi
 *  Wait until it is ready
 */
package cytus.ext;

import cytus.*;
import java.io.*;
import java.awt.*;
import java.awt.image.*;
import javax.imageio.*;
import java.text.*;

public class Kamcord {
	static final int MAX_NUM_OF_THREADS = 4; // Change if needed
	static final int FPS = 60;
	int NUM_OF_THREADS = 0;

	public Kamcord(String songtitle) throws Exception {
		System.setProperty("sun.java2d.opengl", "true");
		System.out.println("Loading...");
		Pattern p = new Pattern(songtitle, "hard");

		DecimalFormat df = new DecimalFormat("00000");
		double dur = p.player.getDuration().getSeconds();
		int framelength = (int) (dur * FPS) + 1;
		System.out.println("Duration:" + dur + " Frames:" + framelength);

		p.time = 0;
		int framecount = 0;
		long start = System.currentTimeMillis();
		while (p.time <= dur) {
			p.paint();
			push(p.buf, df.format(framecount++) + ".png");
			p.time += 1.0 / (double) FPS;

			// Show status
			if (framecount % FPS == 0) {
				double usedtime = (System.currentTimeMillis() - start) / 1000.0;
				int fps = (int) (framecount / usedtime);
				System.out.println("Frame:" + framecount + " Time:" + usedtime
						+ " fps:" + fps);
			}
		}

		System.out.println("Done.");
		System.exit(0);
	}

	public void push(BufferedImage img, String filename) {
		BufferedImage copy = ImageUtil.copyImage(img);
		if (NUM_OF_THREADS >= MAX_NUM_OF_THREADS) {
			try {
				wait();
			} catch (Exception e) {
			}
		}
		new OutputThread(copy, filename).start();
	}

	public synchronized void add() {
		NUM_OF_THREADS++;
	}

	public synchronized void release() {
		NUM_OF_THREADS--;
		notify();
	}

	class OutputThread extends Thread {
		static final String path = "G:\\pic\\"; // Change the path
		BufferedImage img = null;
		String filename = null;

		public OutputThread(BufferedImage img, String filename) {
			this.img = img;
			this.filename = filename;
		}

		public void run() {
			Kamcord.this.add();
			try {
				ImageIO.write(img, "png", new File(path + filename));
				img = null;
			} catch (Exception e) {
				e.printStackTrace();
				System.exit(1);
			}
			Kamcord.this.release();
		}
	}

	public static void main(String args[]) throws Exception {
		new Kamcord(args[0]);
	}
}