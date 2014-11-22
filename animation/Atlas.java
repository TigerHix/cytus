package cytus.animation;

import com.alibaba.fastjson.*;
import java.io.*;
import java.util.*;
import java.awt.*;
import java.awt.image.*;
import javax.imageio.*;

public class Atlas {
	public HashMap<String, BufferedImage> map = new HashMap<String, BufferedImage>();

	public Atlas(String folder, String filename) throws Exception {
		BufferedImage src = ImageIO.read(new File(folder + filename + ".png"));
		JSONObject frames = JSON.parseObject(
				readFile(folder + filename + ".json")).getJSONObject("frames");

		for (Map.Entry<String, Object> entry : frames.entrySet()) {
			String name = entry.getKey();
			JSONObject part = (JSONObject) entry.getValue();

			JSONObject fpos = part.getJSONObject("frame");
			int x = fpos.getIntValue("x");
			int y = fpos.getIntValue("y");
			int w = fpos.getIntValue("w");
			int h = fpos.getIntValue("h");
			if ((w == 0) || (h == 0)) {
				map.put(name, new BufferedImage(1, 1,
						BufferedImage.TYPE_INT_ARGB));
				continue;
			}

			JSONObject srcsize = part.getJSONObject("sourceSize");
			int srcw = srcsize.getIntValue("w");
			int srch = srcsize.getIntValue("h");

			JSONObject sprsrcsize = part.getJSONObject("spriteSourceSize");
			int spsx = sprsrcsize.getIntValue("x");
			int spsy = sprsrcsize.getIntValue("y");
			int spsw = sprsrcsize.getIntValue("w");
			int spsh = sprsrcsize.getIntValue("h");

			BufferedImage tmp = new BufferedImage(w, h,
					BufferedImage.TYPE_INT_ARGB);
			Graphics2D g = (Graphics2D) tmp.getGraphics();
			g.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
					RenderingHints.VALUE_INTERPOLATION_BICUBIC);
			g.setRenderingHint(RenderingHints.KEY_RENDERING,
					RenderingHints.VALUE_RENDER_QUALITY);
			g.drawImage(src, 0, 0, w, h, x, y, x + w, y + h, null);
			g.dispose();

			BufferedImage img = new BufferedImage(srcw, srch,
					BufferedImage.TYPE_INT_ARGB);
			g = (Graphics2D) img.getGraphics();
			g.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
					RenderingHints.VALUE_INTERPOLATION_BICUBIC);
			g.setRenderingHint(RenderingHints.KEY_RENDERING,
					RenderingHints.VALUE_RENDER_QUALITY);
			g.drawImage(tmp, spsx, spsy, spsw, spsh, null);
			g.dispose();

			map.put(name, img);
		}
	}

	public BufferedImage get(String part) {
		return map.get(part);
	}

	private String readFile(String path) throws Exception {
		BufferedReader in = new BufferedReader(new FileReader(path));
		String str = "";
		String s = in.readLine();
		while (s != null) {
			str += s;
			s = in.readLine();
		}
		in.close();
		return str;
	}
}