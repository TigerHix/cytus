package cytus.ext.osu;

import java.awt.*;
import java.awt.geom.*;

public class Slider extends Note{
  int repeat=0;
  double length=0;
  Path2D.Double path=new Path2D.Double();
  int nodes[][]=null;
  public Slider(int x,int y,double time,int repeat,double length,int nodes[][]){
    super(x,y,time);
	this.repeat=repeat;
	this.length=length;
	this.nodes=new int[nodes.length][2];
	System.arraycopy(nodes,0,this.nodes,0,nodes.length);
	path.moveTo(nodes[0][0],nodes[1][1]);
	for(int i=1;i<nodes.length;i++)
	  path.lineTo(nodes[i][0],nodes[i][1]);
  }
}