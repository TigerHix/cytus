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
import javax.sound.sampled.*;

public class Kamcord {
	static final int MAX_NUM_OF_THREADS = 4; // Change if needed
	static final String path = "D:\\pic\\"; // Change the path
	static final int FPS = 60;
	int NUM_OF_THREADS = 0;
	boolean first = true;

	public Kamcord(String songtitle) throws Exception {
		System.setProperty("sun.java2d.opengl", "true");
		System.out.println("Loading...");
		NoteChartPlayer p = new NoteChartPlayer(songtitle, "hard");

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

			// Show status if (framecount % FPS == 0)
			double usedtime = (System.currentTimeMillis() - start) / 1000.0;
			int fps = (int) (framecount / usedtime);
			System.out.println("Frame:" + framecount + " Time:" + usedtime
					+ " fps:" + fps);
		}
		while (NUM_OF_THREADS > 0)
			wait();

		byte sample[] = readAudioData("./assets/sounds/beat1.wav");
		byte bgm[] = readAudioData("temp.wav");
		AudioInputStream ain = AudioSystem
				.getAudioInputStream(new ByteArrayInputStream(bgm));
		AudioFormat format = ain.getFormat();

		for (Note note : p.notes) {
			if (note instanceof Circle)
				mixAudioData(bgm, sample, note.stime);
			if (note instanceof Hold) {
				mixAudioData(bgm, sample, note.stime);
				mixAudioData(bgm, sample, note.etime);
			}
			if (note instanceof Link) {
				Link link = (Link) note;
				for (Link.Node node : link.nodes)
					mixAudioData(bgm, sample, node.stime);
			}
		}
		ByteArrayInputStream processed = new ByteArrayInputStream(bgm);
		AudioInputStream in = new AudioInputStream(processed, format,
				ain.getFrameLength());
		AudioSystem.write(in, AudioFileFormat.Type.WAVE, new File("out.wav"));

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

	private byte[] readAudioData(String file) throws Exception {
		ByteArrayOutputStream buf = new ByteArrayOutputStream();
		FileInputStream in = new FileInputStream(file);
		byte data[] = new byte[1024];
		int len = in.read(data, 0, 1024);
		while (len != -1) {
			buf.write(data, 0, len);
			len = in.read(data, 0, 1024);
		}
		in.close();
		return buf.toByteArray();
	}

	private void mixAudioData(byte data1[], byte data2[], double time) {
		int pos = (int) (44100 * time * 4);
		double f1 = 1, f2 = 1;
		for (int i = 0; i < data2.length / 4; i++) {
			int leftsample1 = (data1[pos + i * 4 + 1] << 8)
					| (data1[pos + i * 4] & 0xFF);
			int leftsample2 = (data2[i * 4 + 1] << 8) | (data2[i * 4] & 0xFF);
			int rightsample1 = (data1[pos + i * 4 + 3] << 8)
					| (data1[pos + i * 4 + 2] & 0xFF);
			int rightsample2 = (data2[i * 4 + 3] << 8)
					| (data2[i * 4 + 2] & 0xFF);

			int leftmix = (int) ((leftsample1 + leftsample2) * f1);
			if (leftmix > 32767) {
				f1 = 32767.0 / leftmix;
				leftmix = 32767;
			}
			if (leftmix < -32768) {
				f1 = -32768.0 / leftmix;
				leftmix = -32768;
			}
			if (f1 < 1)
				f1 += (1 - f1) / 16;
			int rightmix = (int) ((rightsample1 + rightsample2) * f2);
			if (rightmix > 32767) {
				f2 = 32767.0 / rightmix;
				rightmix = 32767;
			}
			if (rightmix < -32768) {
				f2 = -32767.0 / rightmix;
				rightmix = -32767;
			}
			if (f2 < 1)
				f2 += (1 - f2) / 16;
			data1[pos + i * 4] = (byte) (leftmix & 0xFF);
			data1[pos + i * 4 + 1] = (byte) ((leftmix >> 8) & 0xFF);
			data1[pos + i * 4 + 2] = (byte) (rightmix & 0xFF);
			data1[pos + i * 4 + 3] = (byte) ((rightmix >> 8) & 0xFF);
		}
	}

	public static void main(String args[]) throws Exception {
		new Kamcord(args[0]);
	}
}