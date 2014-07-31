package cytus.editor;

import cytus.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.table.*;

public class Editor extends JFrame implements ActionListener,
		TableModelListener {
	Pattern p = null;
	PatternData d = null;
	JTable table = null;

	public Editor() throws Exception {
		super("Editor");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setSize(800, 600);
		setVisible(true);

		JMenuBar root = new JMenuBar();

		JMenu m_file = new JMenu("File");
		JMenuItem m_fopen = new JMenuItem("Open");
		JMenuItem m_fsave = new JMenuItem("Save");
		m_file.add(m_fopen);
		m_file.add(m_fsave);
		m_fopen.addActionListener(this);
		m_fsave.addActionListener(this);
		root.add(m_file);

		JMenu m_settings = new JMenu("Settings");
		JMenuItem m_sglobal = new JMenuItem("Global settings");
		m_settings.add(m_sglobal);
		m_sglobal.addActionListener(this);
		root.add(m_settings);

		JMenu m_data = new JMenu("Data");
		JMenuItem m_drefresh = new JMenuItem("Refresh");
		ButtonGroup group = new ButtonGroup();
		JMenuItem m_dall = new JCheckBoxMenuItem("Show all notes", true);
		JMenuItem m_dcircles = new JCheckBoxMenuItem("Show circles");
		JMenuItem m_dholds = new JCheckBoxMenuItem("Show holds");
		JMenuItem m_dlinks = new JCheckBoxMenuItem("Show links");
		group.add(m_dall);
		group.add(m_dcircles);
		group.add(m_dholds);
		group.add(m_dlinks);
		m_data.add(m_drefresh);
		m_data.add(new JSeparator());
		m_data.add(m_dall);
		m_data.add(m_dcircles);
		m_data.add(m_dholds);
		m_data.add(m_dlinks);
		m_drefresh.addActionListener(this);
		m_dall.addActionListener(this);
		m_dcircles.addActionListener(this);
		m_dholds.addActionListener(this);
		m_dlinks.addActionListener(this);
		root.add(m_data);

		/*
		 * JMenu m_preview = new JMenu("Preview"); JMenuItem m_pupdate = new
		 * JMenuItem("Update preview window");
		 * m_pupdate.addActionListener(this); m_preview.add(m_pupdate);
		 * root.add(m_preview);
		 */

		add(root, "North");

		String songtitle = JOptionPane.showInputDialog(this, "Song title");
		setTitle("Loading...Please wait");
		p = new Pattern(songtitle, "hard");
		setTitle("Editor");
		d = new PatternData(p);
		d.addTableModelListener(this);
		table = new JTable(d);
		JScrollPane scroll = new JScrollPane(table);
		add(scroll, "Center");

		validate();
		new PreviewWindow();
	}

	public void actionPerformed(ActionEvent e) {
		System.out.println(e.getActionCommand());
	}

	public void tableChanged(TableEvent e) {

	}

	class PreviewWindow extends JFrame {
		Graphics2D gg = null;

		public PreviewWindow() {
			super("Preview Window");
			System.setProperty("sun.java2d.opengl", "true");
			setSize(966, 648);
			setResizable(false);
			setVisible(true);
			gg = (Graphics2D) getContentPane().getGraphics();
			new Thread(new Runnable() {
				public void run() {
					while (true) {
						try {
							Thread.sleep(500);
						} catch (Exception e) {
						}
						repaint();
					}
				}
			}).start();
		}

		public void paint(Graphics g) {
			try {
				p.paint();
			} catch (Exception e) {
			}
			gg.drawImage(p.buf, 0, 0, null);
		}
	}

	public static void main(String args[]) throws Exception {
		new Editor();
	}
}