package cytus.editor;

import cytus.*;
import cytus.animation.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.media.*;

public class EditorGUI extends JFrame implements ActionListener{
  Pattern p=null;
  boolean grid=true;
  double time=0;
  boolean playing=false;
  int dx=20,dy=16;
  int ntype=0;
  
  ControlPanel ctrls=null;
  ToolPanel tools=null;
  MainPanel mp=null;
  
  public EditorGUI(){
    super("谱面编辑器");
	setSize(720,564);
	setDefaultCloseOperation(EXIT_ON_CLOSE);
	
	try{
	  SpriteLibrary.load();
      AnimationPreset.load();
      FontLibrary.load();
	  p=new Pattern("l2_a","hard");
	}catch(Exception e){
	  System.out.println(e);
	}
	
	JMenuBar root=new JMenuBar();
	
	JMenu mf=new JMenu("文件");
	JMenuItem mf_new=new JMenuItem("新建");
	JMenuItem mf_open=new JMenuItem("打开");
	JMenuItem mf_save=new JMenuItem("保存");
	JMenuItem mf_exit=new JMenuItem("退出");
	mf_new.addActionListener(this);
	mf_open.addActionListener(this);
	mf_save.addActionListener(this);
	mf_exit.addActionListener(this);
	mf.add(mf_new);
	mf.add(mf_open);
	mf.add(mf_save);
	mf.add(mf_exit);
	root.add(mf);
	
	JMenu ms=new JMenu("设置");
	JMenuItem ms_global=new JMenuItem("全局设置");
	ms_global.addActionListener(this);
	
	JMenu ms_grid=new JMenu("网格");
	JCheckBoxMenuItem msg_show=new JCheckBoxMenuItem("显示网格",true);
	
	ButtonGroup group1=new ButtonGroup();
	JRadioButtonMenuItem msg_d1=new JRadioButtonMenuItem("间隔:1/50");
	JRadioButtonMenuItem msg_d2=new JRadioButtonMenuItem("间隔:1/20",true);
	JRadioButtonMenuItem msg_d3=new JRadioButtonMenuItem("间隔:1/10");
	JRadioButtonMenuItem msg_d4=new JRadioButtonMenuItem("间隔:1/5");
	group1.add(msg_d1);
	group1.add(msg_d2);
	group1.add(msg_d3);
	group1.add(msg_d4);
	
	msg_show.addActionListener(e->grid=!grid);
	msg_d1.addActionListener(e->dx=50);
	msg_d2.addActionListener(e->dx=20);
	msg_d3.addActionListener(e->dx=10);
	msg_d4.addActionListener(e->dx=5);
	ms_grid.add(msg_show);
	ms_grid.addSeparator();
	ms_grid.add(msg_d1);
	ms_grid.add(msg_d2);
	ms_grid.add(msg_d3);
	ms_grid.add(msg_d4);
	
	JMenu ms_tgap=new JMenu("时间间隔");
	ButtonGroup group2=new ButtonGroup();
	JRadioButtonMenuItem ms_t1=new JRadioButtonMenuItem("间隔:1/2");
	JRadioButtonMenuItem ms_t2=new JRadioButtonMenuItem("间隔:1/4");
	JRadioButtonMenuItem ms_t3=new JRadioButtonMenuItem("间隔:1/8");
	JRadioButtonMenuItem ms_t4=new JRadioButtonMenuItem("间隔:1/16",true);
	JRadioButtonMenuItem ms_t5=new JRadioButtonMenuItem("间隔:1/32");
    group2.add(ms_t1);
    group2.add(ms_t2);
    group2.add(ms_t3);
	group2.add(ms_t4);
	group2.add(ms_t5);
	
	ms_t1.addActionListener(e->dy=2);
	ms_t2.addActionListener(e->dy=4);
	ms_t3.addActionListener(e->dy=8);
	ms_t4.addActionListener(e->dy=16);
	ms_t5.addActionListener(e->dy=32);
	ms_tgap.add(ms_t1);
	ms_tgap.add(ms_t2);
	ms_tgap.add(ms_t3);
	ms_tgap.add(ms_t4);
	ms_tgap.add(ms_t5);
	
	ms.add(ms_global);
	ms.add(ms_grid);
	ms.add(ms_tgap);
	root.add(ms);
	
	Container pane=getContentPane();
	pane.setLayout(null);
	pane.add(root);
	root.setBounds(0,0,720,20);
	
	ctrls=new ControlPanel();
	pane.add(ctrls);
	ctrls.setBounds(0,30,480,54);
	
	tools=new ToolPanel();
	pane.add(tools);
	tools.setBounds(480,20,720,64);
	
	mp=new MainPanel();
	pane.add(mp);
	mp.setBounds(0,84,720,480);
	
	setVisible(true);
	
	new Thread(()->{
	  while(true){
	    while(playing){
	      time=p.player.getMediaTime().getSeconds();
		  repaint();
		  ctrls.updateStatus();
	      try{
	        Thread.sleep(10);
	      }catch(Exception e){}
		}
		while(!playing){
		  repaint();
		  try{
	        Thread.sleep(10);
	      }catch(Exception e){}
		}
	  }
	}).start();
  }
  public void actionPerformed(ActionEvent e){
    String str=e.getActionCommand();
  }
  class ControlPanel extends JPanel{
    JSlider s_time=new JSlider(0,100,0);
	JLabel l_time=new JLabel("0.00");
	double dur=p.player.getDuration().getSeconds();
	double last=0;
    public ControlPanel(){
	  JButton b_play=new JButton("播放");
	  JButton b_pause=new JButton("暂停");
	  JButton b_stop=new JButton("停止");
	  add(s_time);
	  add(l_time);
	  add(b_play);
	  add(b_pause);
	  add(b_stop);
	  s_time.addChangeListener(e->adjust());
	  b_play.addActionListener(e->start());
	  b_pause.addActionListener(e->pause());
	  b_stop.addActionListener(e->stop());
	}
	public void updateStatus(){
	  s_time.setValue((int)(time/dur*100));
	  l_time.setText(new java.text.DecimalFormat("0.00").format(time));
	}
	public void adjust(){
	  if(!playing){
	    time=s_time.getValue()/100.0*dur;
		if(time<last) p.restart();
		last=time;
	    setTime(time);
	    l_time.setText(new java.text.DecimalFormat("0.00").format(time));
	  }
	}
	public void start(){
      p.player.start();
	  playing=true;
    }
    public void pause(){
      p.player.stop();
      playing=false;
    }
    public void stop(){
	  playing=false;
      p.restart();
	  updateStatus();
    }
	public void setTime(double ntime){
      p.player.setMediaTime(new Time(ntime));
	  time=ntime;
	  updateStatus();
    }
  }
  class ToolPanel extends JPanel{
    public ToolPanel(){
	  setLayout(null);
	  JButton t_circle=new JButton(new ImageIcon(
	                               SpriteLibrary.get("red_active")
								   .getScaledInstance(64,64,Image.SCALE_SMOOTH)));
	  JButton t_hold=new JButton(new ImageIcon(
	                               SpriteLibrary.get("beat_hold_active")
								   .getScaledInstance(64,64,Image.SCALE_SMOOTH)));
	  JButton t_drag=new JButton(new ImageIcon(
	                               SpriteLibrary.get("drag_head_active")
								   .getScaledInstance(64,64,Image.SCALE_SMOOTH)));
	  add(t_circle);
	  add(t_hold);
	  add(t_drag);
	  t_circle.setBounds(0,0,64,64);
	  t_hold.setBounds(64,0,64,64);
	  t_drag.setBounds(128,0,64,64);
	  t_circle.addActionListener(e->ntype=1);
	  t_hold.addActionListener(e->ntype=2);
	  t_drag.addActionListener(e->ntype=3);
	}
  }
  class MainPanel extends JPanel implements MouseInputListener{
    public MainPanel(){
	  setFocusable(true);
	  addMouseListener(this);
	}
    public void paint(Graphics g){
	  try{
	    p.paint();
	  }catch(Exception e){}
	  g.drawImage(p.buf,0,0,null);
	  if(grid){
	    g.setColor(new Color(232,232,232,100));
	    for(int i=0;i<=600;i+=600/dx) g.drawLine(i+60,64,i+60,416);
		for(int i=0;i<=352;i+=352/dy) g.drawLine(60,i+64,660,i+64);
	  }
    }
	public void mouseClicked(MouseEvent e){
	  if(e.getButton()==MouseEvent.BUTTON3){
        ntype=0;
		return;
	  }
	  int x=e.getX(),y=e.getY();
	  int rx=(int)(x*dx/600)*(600/dx);
	  int ry=(int)(y*dy/352)*(352/dy);
	}
	public void mouseMoved(MouseEvent e){
	  System.out.println("move");
	  if(ntype!=0){
	    int x=e.getX(),y=e.getY();
	    int rx=(int)(x*dx/600)*(600/dx);
	    int ry=(int)(y*dy/352)*(352/dy);
		Graphics g=getGraphics();
		g.setColor(Color.RED);
		g.drawOval(rx-64,ry-64,128,128);
	  }
	}
	public void mouseDragged(MouseEvent e){}
	public void mouseEntered(MouseEvent e){}
	public void mouseExited(MouseEvent e){}
	public void mousePressed(MouseEvent e){}
	public void mouseReleased(MouseEvent e){}
  }
  class GlobalSettingsDialog extends JDialog{}
  public static void main(String args[]){
    new EditorGUI();
  }
}
	