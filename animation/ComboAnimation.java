package cytus.animation;
import java.io.*;
import javax.imageio.*;
import java.awt.*;
import java.awt.image.*;
public class ComboAnimation extends Animation {
    Sprite text=null;
    public ComboAnimation(int combo,double time) {
        String str=String.valueOf(combo);
        int len=str.length();
        img=new BufferedImage[1];
        img[0]=new BufferedImage(len*128+127,317,BufferedImage.TYPE_INT_ARGB);
        Graphics g=img[0].getGraphics();

        for(int i=0; i<len; i++) {
            BufferedImage num=FontLibrary.get("bcfont"+(str.charAt(i)-48));
            g.drawImage(num,i*128,0,null);
        }

        g.dispose();
        stime=time;
        etime=time+1;
        x=360;
        y=240;
        text=new Sprite("combo_text");
        text.moveTo(350,120);
        text.scale(0.8,0.8);
    }
    public void paint(Graphics2D g,double time) {
        double delta=time-stime;
        alpha=0;

        if(delta<=1/6.0) alpha=delta*6;

        if((delta>1/6.0)&&(delta<=1/3.0)) alpha=1;

        if((delta>1/3.0)&&(delta<1/2.0)) alpha=3-delta*6;

        text.scale(0.8,0.8);

        if(delta<=1/2.0) {
            double s=(1+delta)*0.8;
            scale(s,s);
            text.scale(s,s);
        }

        text.setAlpha(alpha);
        text.paint(g,time);
        super.paint(g,time);

        if(delta<=1/3.0) alpha=delta*3;

        if((delta>1/3.0)&&(delta<=2/3.0)) alpha=1;

        if((delta>2/3.0)&&(delta<1)) alpha=3-delta*3;

        text.scale(0.8,0.8);
        text.setAlpha(alpha);
        text.paint(g,time);
        scale(0.8,0.8);
        super.paint(g,time);
    }
}