package cytus.animation;
import java.awt.*;
import java.awt.image.*;
public class MaskBeat extends Sprite {
    BufferedImage img=null;
    double offset=0,beat=0;
    public MaskBeat(double offset,double beat) {
        img=SpriteLibrary.get("gameplay_bg_mask_3");
        this.offset=offset;
        this.beat=beat;
    }
    public void paint(Graphics2D g,double time) {
        //BeatTransform by hand
        double pos=((time+offset)%beat)/beat;
        double size=0.5+0.2*Math.max(Math.cos(pos*Math.PI*2),0);
        g.setComposite(AlphaComposite.SrcOver.derive(0.5f));
        g.drawImage(img,0,48,(int)(img.getWidth()*size),480,null);
        g.drawImage(img,720,48,720-(int)(img.getWidth()*size),480,
                    0,0,img.getWidth(),img.getHeight(),null);
        g.setComposite(AlphaComposite.SrcOver);
    }
}