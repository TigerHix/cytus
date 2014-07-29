package cytus.editor;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Graphics;

import javax.media.Time;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.event.MouseInputListener;

import java.awt.Dimension;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JSeparator;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.ButtonGroup;
import javax.swing.JSlider;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JToggleButton;

import cytus.*;
import cytus.animation.*;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.text.DecimalFormat;
import java.util.LinkedList;

import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;

public class Editor extends JFrame {

	private JPanel contentPane;
	private final ButtonGroup buttonGroup = new ButtonGroup();
	private final ButtonGroup buttonGroup_1 = new ButtonGroup();
	private final ButtonGroup buttonGroup_2 = new ButtonGroup();
	private JToggleButton select = null;
	private JSlider slider = null;
	private JLabel label = null;

	Pattern p = null;
	MainPanel mpanel = null;
	boolean playing = false;
	boolean grid = true;
	int cellsizex = 30;
	int timediv = 4;
	int tooltype = 0;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Editor frame = new Editor();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public Editor() {
		try {
			SpriteLibrary.load();
			AnimationPreset.load();
			FontLibrary.load();
			p = new Pattern("l2_a", "hard");
		} catch (Exception e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);

		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);

		JMenu menu = new JMenu("\u6587\u4EF6");
		menuBar.add(menu);

		JMenuItem menuItem = new JMenuItem("\u65B0\u5EFA");
		menu.add(menuItem);

		JMenuItem menuItem_1 = new JMenuItem("\u6253\u5F00");
		menu.add(menuItem_1);

		JMenuItem menuItem_2 = new JMenuItem("\u4FDD\u5B58");
		menu.add(menuItem_2);

		JMenuItem menuItem_3 = new JMenuItem("\u5173\u95ED");
		menu.add(menuItem_3);

		JMenu menu_1 = new JMenu("\u8BBE\u7F6E");
		menuBar.add(menu_1);

		JMenuItem menuItem_4 = new JMenuItem("\u5168\u5C40\u8BBE\u7F6E");
		menu_1.add(menuItem_4);

		JMenu menu_2 = new JMenu("\u7F51\u683C");
		menu_1.add(menu_2);

		JCheckBoxMenuItem checkBoxMenuItem = new JCheckBoxMenuItem(
				"\u663E\u793A\u7F51\u683C");
		checkBoxMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				grid = !grid;
			}
		});
		checkBoxMenuItem.setSelected(true);
		menu_2.add(checkBoxMenuItem);

		JSeparator separator = new JSeparator();
		menu_2.add(separator);

		JRadioButtonMenuItem rdbtnmntmLevel = new JRadioButtonMenuItem("Level1");
		rdbtnmntmLevel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				cellsizex = 60;
			}
		});
		buttonGroup.add(rdbtnmntmLevel);
		menu_2.add(rdbtnmntmLevel);

		JRadioButtonMenuItem rdbtnmntmLevel_1 = new JRadioButtonMenuItem(
				"Level2");
		rdbtnmntmLevel_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				cellsizex = 30;
			}
		});
		rdbtnmntmLevel_1.setSelected(true);
		buttonGroup.add(rdbtnmntmLevel_1);
		menu_2.add(rdbtnmntmLevel_1);

		JRadioButtonMenuItem rdbtnmntmLevel_2 = new JRadioButtonMenuItem(
				"Level3");
		rdbtnmntmLevel_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				cellsizex = 15;
			}
		});
		buttonGroup.add(rdbtnmntmLevel_2);
		menu_2.add(rdbtnmntmLevel_2);

		JMenu menu_3 = new JMenu("\u65F6\u95F4\u5355\u4F4D");
		menu_1.add(menu_3);

		JRadioButtonMenuItem radioButtonMenuItem = new JRadioButtonMenuItem(
				"1/2");
		radioButtonMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				timediv = 2;
			}
		});
		buttonGroup_1.add(radioButtonMenuItem);
		menu_3.add(radioButtonMenuItem);

		JRadioButtonMenuItem radioButtonMenuItem_1 = new JRadioButtonMenuItem(
				"1/4");
		radioButtonMenuItem_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				timediv = 4;
			}
		});
		radioButtonMenuItem_1.setSelected(true);
		buttonGroup_1.add(radioButtonMenuItem_1);
		menu_3.add(radioButtonMenuItem_1);

		JRadioButtonMenuItem radioButtonMenuItem_2 = new JRadioButtonMenuItem(
				"1/8");
		radioButtonMenuItem_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				timediv = 8;
			}
		});
		buttonGroup_1.add(radioButtonMenuItem_2);
		menu_3.add(radioButtonMenuItem_2);

		JRadioButtonMenuItem radioButtonMenuItem_3 = new JRadioButtonMenuItem(
				"1/16");
		radioButtonMenuItem_3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				timediv = 16;
			}
		});
		buttonGroup_1.add(radioButtonMenuItem_3);
		menu_3.add(radioButtonMenuItem_3);

		JRadioButtonMenuItem radioButtonMenuItem_4 = new JRadioButtonMenuItem(
				"1/32");
		radioButtonMenuItem_4.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				timediv = 32;
			}
		});
		buttonGroup_1.add(radioButtonMenuItem_4);
		menu_3.add(radioButtonMenuItem_4);

		contentPane = new JPanel();
		contentPane.setPreferredSize(new Dimension(720, 505));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		slider = new JSlider();
		slider.setFocusable(false);
		slider.setValue(0);
		slider.setBounds(10, 0, 169, 23);
		contentPane.add(slider);

		label = new JLabel("0.000");
		label.setBounds(189, 0, 48, 23);
		contentPane.add(label);

		JButton btnNewButton = new JButton("\u64AD\u653E");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				start();
			}
		});
		btnNewButton.setBounds(236, 0, 69, 23);
		contentPane.add(btnNewButton);

		JButton button = new JButton("\u6682\u505C");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				pause();
			}
		});
		button.setBounds(304, 0, 69, 23);
		contentPane.add(button);

		JButton btnNewButton_1 = new JButton("\u505C\u6B62");
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				stop();
			}
		});
		btnNewButton_1.setBounds(372, 0, 69, 23);
		contentPane.add(btnNewButton_1);

		select = new JToggleButton("\u9009\u62E9");
		select.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				tooltype = 0;
			}
		});
		select.setSelected(true);
		buttonGroup_2.add(select);
		select.setBounds(452, 0, 66, 23);
		contentPane.add(select);

		JToggleButton tglbtnNewToggleButton_1 = new JToggleButton("Click");
		tglbtnNewToggleButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				tooltype = 1;
			}
		});
		buttonGroup_2.add(tglbtnNewToggleButton_1);
		tglbtnNewToggleButton_1.setBounds(518, 0, 64, 23);
		contentPane.add(tglbtnNewToggleButton_1);

		JToggleButton tglbtnNewToggleButton_2 = new JToggleButton("Hold");
		tglbtnNewToggleButton_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				tooltype = 2;
			}
		});
		buttonGroup_2.add(tglbtnNewToggleButton_2);
		tglbtnNewToggleButton_2.setBounds(582, 0, 64, 23);
		contentPane.add(tglbtnNewToggleButton_2);

		JToggleButton tglbtnNewToggleButton_3 = new JToggleButton("Drag");
		tglbtnNewToggleButton_3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				tooltype = 3;
			}
		});
		buttonGroup_2.add(tglbtnNewToggleButton_3);
		tglbtnNewToggleButton_3.setBounds(646, 0, 64, 23);
		contentPane.add(tglbtnNewToggleButton_3);

		mpanel = new MainPanel();
		mpanel.setSize(new Dimension(720, 480));
		mpanel.setPreferredSize(new Dimension(720, 480));
		mpanel.setBounds(0, 25, 720, 480);
		contentPane.add(mpanel);

		pack();
	}

	public void start() {
		p.player.start();
		playing = true;
	}

	public void pause() {
		p.player.stop();
		playing = false;
	}

	public void stop() {
		p.player.stop();
		playing = false;
		setTime(0);
	}

	public void setTime(double ntime) {
		double time = p.player.getMediaTime().getSeconds();
		if (ntime < time)
			p.restart();
		p.player.stop();
		p.player.setMediaTime(new Time(ntime));
		if (playing)
			p.player.start();
	}

	public class MainPanel extends JPanel implements MouseInputListener {
		int curx = 0, cury = 0;
		LinkedList<NoteData> notes = new LinkedList<NoteData>();
		Note selectedObj = null;

		public MainPanel() {
			addMouseListener(this);
			addMouseMotionListener(this);
			new Thread(new Runnable() {
				public void run() {
					while (true) {
						repaint();
						double time = p.player.getMediaTime().getSeconds();
						double duration = p.player.getDuration().getSeconds();
						// slider.setValue((int) (time / duration * 100));
						label.setText(new DecimalFormat("0.000").format(time));
						try {
							Thread.sleep(16);
						} catch (InterruptedException e) {
							// TODO 自动生成的 catch 块
							e.printStackTrace();
						}
					}
				}
			}).start();
			setFocusable(true);
			addKeyListener(new KeyAdapter() {

				@Override
				public void keyPressed(KeyEvent e) {
					// TODO 自动生成的方法存根
					double time = p.player.getMediaTime().getSeconds();
					double dur = p.player.getDuration().getSeconds();
					switch (e.getKeyCode()) {
					case 32:
						if (playing) {
							p.player.stop();
							playing = false;
						} else {
							p.player.start();
							playing = true;
						}
						break;
					case 37:
						setTime(lastBeat(time, timediv));
						break;
					case 39:
						setTime(nextBeat(time, timediv));
						break;
					case 38:
						setTime(lastBeat(time, 1));
						break;
					case 40:
						setTime(nextBeat(time, 1));
						break;
					}
				}

			});
		}

		@Override
		public void paint(Graphics g) {
			// TODO 自动生成的方法存根
			try {
				p.paint();
			} catch (Exception e) {
				// TODO 自动生成的 catch 块
				e.printStackTrace();
			}
			g.drawImage(p.buf, 0, 0, null);
			if (grid) {
				g.setColor(new Color(240, 240, 240, 100));
				for (int i = 0; i <= 600; i += cellsizex)
					g.drawLine(i + 60, 64, i + 60, 416);

				g.setColor(new Color(100, 250, 250, 100));
				double time = p.player.getMediaTime().getSeconds();
				int page = (int) ((time + p.offset) / p.beat);
				double ntime = page * p.beat;
				while (page == p.page) {
					int y = getY(ntime);
					g.drawLine(60, y, 660, y);
					ntime += p.beat / timediv;
					page = (int) ((ntime + p.offset) / p.beat);
				}
			}
			if (selectedObj != null) {
				g.setColor(Color.GREEN);
				g.drawOval(selectedObj.x - 32, selectedObj.y - 32, 64, 64);
			}
			if (tooltype != 0) {
				if (tooltype == 3) {
					g.setColor(Color.BLUE);
					for (NoteData n : notes)
						g.drawOval(n.x - 16, n.y - 16, 32, 32);
					g.drawOval(curx - 16, cury - 16, 32, 32);
				} else {
					g.setColor(Color.RED);
					if (notes.size() > 0) {
						NoteData d = notes.get(0);
						g.drawOval(d.x - 32, d.y - 32, 64, 64);
					}
					g.drawOval(curx - 32, cury - 32, 64, 64);
				}
			}
		}

		class NoteData {
			int x = 0, y = 0;
			double time = 0;

			public NoteData(int x, int y, double time) {
				this.x = x;
				this.y = y;
				this.time = time;
			}
		}

		private double getTime(int y) {
			y -= 64;
			if (p.page % 2 == 0)
				y = 352 - y;
			double pos = y / 352.0;
			return (p.page + pos) * p.beat - p.offset;
		}

		private int getY(double time) {
			int page = (int) ((time + p.offset) / p.beat);
			int y = (int) (((time + p.offset) % p.beat / p.beat) * 352);
			if (page % 2 == 0)
				y = 352 - y;
			y += 64;
			return y;
		}

		private double lastBeat(double time, int div) {
			int count = (int) ((time + p.offset) / (p.beat / div));
			return count * (p.beat / div);
		}

		private double nextBeat(double time, int div) {
			return lastBeat(time, div) + (p.beat / div);
		}

		private Note selectObject(int x, int y) {
			double min = 1e10;
			Note sel = null;
			for (Note n : p.notes) {
				if (n.page == p.page) {
					if ((Math.abs(n.x - x) <= 32) && (Math.abs(n.y - y) <= 32)) {
						double d = (n.x - x) * (n.x - x) + (n.y - y)
								* (n.y - y);
						if (d < min) {
							min = d;
							sel = n;
						}
					}
				}
				if (n.page > p.page)
					break;
			}
			return sel;
		}

		@Override
		public void mouseClicked(MouseEvent e) {
			// TODO 自动生成的方法存根
			if (e.getButton() == MouseEvent.BUTTON1) {
				switch (tooltype) {
				case 0:
					selectedObj = selectObject(e.getX(), e.getY());
					break;
				case 1:
					p.copy.add(new Circle(p, curx, cury, getTime(cury)));
					p.restart();
					break;
				case 2:
					if (notes.size() == 0)
						notes.add(new NoteData(curx, cury, getTime(cury)));
					else {
						NoteData t = notes.get(0);
						if (t.time < getTime(cury)) {
							p.copy.add(new Hold(p, t.x, t.y, t.time,
									getTime(cury) - t.time));
							p.restart();
							notes.clear();
						}
					}
					break;
				case 3:
					if ((notes.size() == 0)
							|| ((notes.size() > 0) && (notes.getLast().time <= getTime(cury))))
						notes.add(new NoteData(curx, cury, getTime(cury)));
					break;
				}
			} else {
				if ((tooltype == 3) && (notes.size() > 1)) {
					Link link = new Link(p);
					for (NoteData d : notes) {
						link.nodes.add(link.new Node(d.x, d.y, d.time));
					}
					link.recalc();
					p.copy.add(link);
					p.restart();
				}
				notes.clear();
				tooltype = 0;
				select.setSelected(true);
			}
		}

		@Override
		public void mouseEntered(MouseEvent e) {
			// TODO 自动生成的方法存根

		}

		@Override
		public void mouseExited(MouseEvent e) {
			// TODO 自动生成的方法存根

		}

		@Override
		public void mousePressed(MouseEvent e) {
			// TODO 自动生成的方法存根

		}

		@Override
		public void mouseReleased(MouseEvent e) {
			// TODO 自动生成的方法存根

		}

		@Override
		public void mouseDragged(MouseEvent e) {
			// TODO 自动生成的方法存根

		}

		@Override
		public void mouseMoved(MouseEvent e) {
			// TODO 自动生成的方法存根
			int x = e.getX(), y = e.getY();
			if (grid && (tooltype != 0)) {
				curx = (int) (x / cellsizex) * cellsizex;
				double time = getTime(e.getY());
				cury = getY(lastBeat(time, timediv));
			} else {
				curx = x;
				cury = y;
			}
		}

	}
}
