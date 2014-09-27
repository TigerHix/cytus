package cytus;

import javax.swing.*;
import java.awt.*;
import java.awt.image.*;
import java.util.*;
import java.io.*;

public class Main extends JFrame {
	public Main(String str) throws Exception {
		super("Cytus");
		System.setProperty("sun.java2d.opengl", "true");
		Pattern p = new Pattern(str, "hard");
		setSize(Pattern.WIDTH + 6, Pattern.HEIGHT + 8);
		setResizable(false);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
		setLocation((d.width - getWidth()) / 2, (d.height - getHeight()) / 2);
		setVisible(true);
		Graphics2D g = (Graphics2D) getContentPane().getGraphics();
		p.start(g);
	}

	public static void main(String args[]) throws Exception {
		new Main(args[0]);
	}
}