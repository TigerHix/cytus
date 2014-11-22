package cytus;

import java.awt.*;

import java.awt.image.*;
import java.awt.geom.*;

public class ImageUtil {
	public static BufferedImage copyImage(BufferedImage src) {
		int w = src.getWidth();
		int h = src.getHeight();
		BufferedImage img = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
		img.getGraphics().drawImage(src, 0, 0, null);
		return img;
	}

	public static BufferedImage scale(BufferedImage src, double scale) {
		if (scale == 1)
			return src;
		int w = (int) (src.getWidth() * scale);
		int h = (int) (src.getHeight() * scale);
		return scale(src, w, h);
	}

	public static BufferedImage scale(BufferedImage src, int width, int height) {
		if ((width == -1) && (height == -1))
			return src;
		int w = width, h = height;
		if (w == -1)
			w = src.getWidth() * (src.getHeight() / h);
		if (h == -1)
			h = src.getHeight() * (src.getWidth() / w);
		BufferedImage img = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = (Graphics2D) img.getGraphics();
		g.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
				RenderingHints.VALUE_INTERPOLATION_BICUBIC);
		g.drawImage(src, 0, 0, w, h, null);
		g.dispose();
		return img;
	}

	public static BufferedImage flipV(BufferedImage src) {
		int w = src.getWidth();
		int h = src.getHeight();
		BufferedImage img = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);

		for (int i = 0; i < w; i++)
			for (int j = 0; j < h; j++)
				img.setRGB(i, j, src.getRGB(i, h - j - 1));

		return img;
	}

	public static BufferedImage flipH(BufferedImage src) {
		int w = src.getWidth();
		int h = src.getHeight();
		BufferedImage img = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);

		for (int i = 0; i < w; i++)
			for (int j = 0; j < h; j++)
				img.setRGB(i, j, src.getRGB(w - i - 1, j));

		return img;
	}

	public static int filterRGB(int rgb1, int rgb2) {
		int a1 = (rgb1 >> 24) & 0xFF;
		int r1 = (rgb1 >> 16) & 0xFF;
		int g1 = (rgb1 >> 8) & 0xFF;
		int b1 = rgb1 & 0xFF;
		int a2 = (rgb2 >> 24) & 0xFF;
		int r2 = (rgb2 >> 16) & 0xFF;
		int g2 = (rgb2 >> 8) & 0xFF;
		int b2 = rgb2 & 0xFF;
		int a = 255 - (255 - a1) * (255 - a2) / 255;
		int r = 255 - (255 - r1) * (255 - r2) / 255;
		int g = 255 - (255 - g1) * (255 - g2) / 255;
		int b = 255 - (255 - b1) * (255 - b2) / 255;
		return (a << 24) | (r << 16) | (g << 8) | b;
	}

	public static int multiplyRGB(int rgb1, int rgb2) {
		int a1 = (rgb1 >> 24) & 0xFF;
		int r1 = (rgb1 >> 16) & 0xFF;
		int g1 = (rgb1 >> 8) & 0xFF;
		int b1 = rgb1 & 0xFF;
		int a2 = (rgb2 >> 24) & 0xFF;
		int r2 = (rgb2 >> 16) & 0xFF;
		int g2 = (rgb2 >> 8) & 0xFF;
		int b2 = rgb2 & 0xFF;
		int a = a1 * a2 / 255;
		int r = r1 * r2 / 255;
		int g = g1 * g2 / 255;
		int b = b1 * b2 / 255;
		return (a << 24) | (r << 16) | (g << 8) | b;
	}

	public static int brighterRGB(int rgb, double d) {
		int a = (rgb >> 24) & 0xFF;
		int r = (rgb >> 16) & 0xFF;
		int g = (rgb >> 8) & 0xFF;
		int b = rgb & 0xFF;
		r = (int) Math.min(r * d, 255);
		g = (int) Math.min(g * d, 255);
		b = (int) Math.min(b * d, 255);
		return (a << 24) | (r << 16) | (g << 8) | b;
	}

	public static int alphaPremultiply(int rgb, double alpha) {
		int r = (rgb >> 16) & 0xFF;
		int g = (rgb >> 8) & 0xFF;
		int b = rgb & 0xFF;
		r = (int) (r * alpha);
		g = (int) (g * alpha);
		b = (int) (b * alpha);
		return (255 << 24) | (r << 16) | (g << 8) | b;
	}

	public static void brighter(BufferedImage img, double d) {
		int w = img.getWidth(), h = img.getHeight();
		int data[] = (int[]) img.getRaster().getDataElements(0, 0, w, h, null);

		for (int i = 0; i < data.length; i++)
			data[i] = brighterRGB(data[i], d);

		img.getRaster().setDataElements(0, 0, w, h, data);
	}

	public static void fillColor(BufferedImage img, int color) {
		int w = img.getWidth(), h = img.getHeight();
		int data[] = (int[]) img.getRaster().getDataElements(0, 0, w, h, null);

		for (int i = 0; i < data.length; i++)
			data[i] = multiplyRGB(data[i], color);

		img.getRaster().setDataElements(0, 0, w, h, data);
	}

	public static void drawImageF(BufferedImage src, BufferedImage dst,
			AffineTransform t, double alpha) {
		if (NoteChartPlayer.prefs.get("nospecialpaint") == 1)
			return;

		AffineTransformOp op = new AffineTransformOp(t,
				AffineTransformOp.TYPE_BICUBIC);
		Rectangle2D.Float bounds = (Rectangle2D.Float) op.getBounds2D(src);
		AffineTransform t2 = AffineTransform.getTranslateInstance(-bounds.x,
				-bounds.y);
		t2.concatenate(t);
		op = new AffineTransformOp(t2, AffineTransformOp.TYPE_BICUBIC);
		BufferedImage tmpsrc = op.filter(src, null);
		Rectangle2D.Float screen = new Rectangle2D.Float(0, 0, dst.getWidth(),
				dst.getHeight());
		Rectangle2D.Float part = (Rectangle2D.Float) bounds
				.createIntersection(screen);
		int w = (int) part.width, h = (int) part.height;
		int x1 = (int) (part.x - bounds.x), y1 = (int) (part.y - bounds.y);
		int x2 = (int) part.x, y2 = (int) part.y;
		int data1[] = (int[]) tmpsrc.getRaster().getDataElements(x1, y1, w, h,
				null);
		int data2[] = (int[]) dst.getRaster().getDataElements(x2, y2, w, h,
				null);

		if (alpha == 1) {
			for (int i = 0; i < data1.length; i++)
				data2[i] = filterRGB(data1[i], data2[i]);
		} else {
			for (int i = 0; i < data1.length; i++)
				data2[i] = filterRGB(alphaPremultiply(data1[i], alpha),
						data2[i]);
		}

		dst.getRaster().setDataElements(x2, y2, w, h, data2);
	}
}