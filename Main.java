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
		setSize(966, 648);
		setResizable(false);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
		setLocation((d.width - 966) / 2, (d.height - 648) / 2);
		setVisible(true);
		Graphics2D g = (Graphics2D) getContentPane().getGraphics();
		new Pattern(str, "hard").start(g);
	}

	public static void main(String args[]) throws Exception {
		new Main(args[0]);
	}
}