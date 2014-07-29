package cytus.ext.osu;

import cytus.*;
import java.io.*;

public class Converter{
  public static Beatmap patternToBeatmap(Pattern p) throws Exception{
    Beatmap b=new Beatmap();
	b.timing.add(new TimingPoint(p.offset,120/p.beat,1));
	for(cytus.Note n:p.notes){
	  int x=(int)((n.x-60)*512.0/600.0);
	  int y=(int)((n.y-64)*384.0/352.0);
	  if(n instanceof cytus.Hold){
	    cytus.Hold hold=(cytus.Hold)n;
		int y2=(int)((hold.y2-64)*384.0/352.0);
		b.notes.add(new Slider(x,y,n.stime,1,n.etime-n.stime,
		new int[][]{{x,y},{x,y2}}));
      }
	  else
	    if(n instanceof cytus.Link){
		  cytus.Link link=(cytus.Link)n;
		  for(cytus.Link.Node node:link.nodes){
		    int nx=(int)((node.x-60)*512.0/600.0);
		    b.notes.add(new Circle(nx,node.y,node.stime));
		  }
		}
	    else b.notes.add(new Circle(x,y,n.stime));
	}
	return b;
  }
  public static Pattern beatmapToPattern(Beatmap b,int offset) throws Exception{
    Pattern p=new Pattern();
	p.beat=120.0/b.timing.getFirst().bpm;
	p.offset=offset;
	for(Note n:b.notes){
	  int x=(int)(n.x*600.0/512.0)+60;
	  int y=(int)((n.time+offset)%p.beat/p.beat*352);
	  int page=(int)((n.time+offset)/p.beat);
	  if(page%2==0) y=352-y;
	  y+=64;
	  if(n instanceof Slider){
	    Slider s=(Slider)n;
	    double delta=s.length/s.repeat;
		for(int i=0;i<=s.repeat;i++){
		  int t=i%2==0?0:s.nodes.length-1;
		  int nx=(int)(s.nodes[t][0]*600.0/512.0)+60;
		  int ny=(int)((n.time+i*delta+offset)%p.beat/p.beat*352);
		  int npage=(int)((n.time+offset)/p.beat);
	      if(npage%2==0) ny=352-ny;
	      ny+=64;
		  p.notes.add(new cytus.Circle(p,nx,ny,n.time+i*delta));
		}
	  }
	  else
	    p.notes.add(new cytus.Circle(p,x,y,n.time));
	}
	return p;
  }
  public static Beatmap formatBeatmap(Beatmap b) throws Exception{
    return patternToBeatmap(beatmapToPattern(b,0));
  }
  public static void main(String args[]) throws Exception{
    Beatmap b=new Beatmap(new File(args[0]));
    //formatBeatmap(b).write(new File(args[1]));
	beatmapToPattern(b,0).new Writer(new PrintWriter(new File(args[1])));
	System.exit(0);
  }
}