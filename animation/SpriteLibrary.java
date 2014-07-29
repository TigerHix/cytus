package cytus.animation;

import cytus.*;
import net.sf.json.*;

import java.awt.*;
import java.awt.image.*;

import javax.imageio.*;

import java.io.*;
import java.util.*;

public class SpriteLibrary {
    static HashMap<String,BufferedImage> map=new HashMap<String,BufferedImage>();
    public static void load() throws Exception {
        String flist[]=new String[] {"animation_1","animation_2","common","common_2","common_add"};

        for(int i=0; i<5; i++) {
            BufferedImage src=ImageIO.read(new File("assets/ui/gameplay/"+flist[i]+".png"));
            BufferedReader r=new BufferedReader(new FileReader("assets/ui/gameplay/"+flist[i]+".json"));

            String s="";
            String str=r.readLine();

            while(str!=null) {
                s+=str;
                str=r.readLine();
            }

            r.close();

            JSONObject obj=JSONObject.fromObject(s);
            s=obj.getString("frames");
            obj=JSONObject.fromObject(s);

            Iterator t=obj.keys();

            while(t.hasNext()) {
                String key=(String)t.next();
                JSONObject frame=JSONObject.fromObject(obj.getString(key));
                JSONObject fpos=JSONObject.fromObject(frame.getString("frame"));
                int x=fpos.getInt("x");
                int y=fpos.getInt("y");
                int w=fpos.getInt("w");
                int h=fpos.getInt("h");

                if((w==0)||(h==0)) {
                    map.put(key,new BufferedImage(1,1,BufferedImage.TYPE_INT_ARGB));
                    continue;
                }

                JSONObject srcsize=JSONObject.fromObject(frame.getString("sourceSize"));
                int srcw=srcsize.getInt("w");
                int srch=srcsize.getInt("h");
                JSONObject sprsrcsize=JSONObject.fromObject(frame.getString("spriteSourceSize"));
                int spsx=sprsrcsize.getInt("x");
                int spsy=sprsrcsize.getInt("y");
                int spsw=sprsrcsize.getInt("w");
                int spsh=sprsrcsize.getInt("h");

                BufferedImage img=new BufferedImage(w,h,BufferedImage.TYPE_INT_ARGB);
                img.getGraphics().drawImage(src,0,0,w,h,x,y,x+w,y+h,null);
                BufferedImage out=null;

                if(key.indexOf("light_add2")!=-1) {
                    out=new BufferedImage(srcw*2,srch*2,BufferedImage.TYPE_INT_ARGB);
                    out.getGraphics().drawImage(img,spsx*2,spsy*2,spsw*2,spsh*2,null);
                    ImageUtil.brighter(out,1.25);
                } else {
                    out=new BufferedImage(srcw,srch,BufferedImage.TYPE_INT_ARGB);
                    out.getGraphics().drawImage(img,spsx,spsy,spsw,spsh,null);
                }

                map.put(key,out);
            }
        }

        BufferedImage scanline=new BufferedImage(720,48,BufferedImage.TYPE_INT_ARGB);
        BufferedImage t=map.get("bar");

        for(int i=0; i<15; i++)
            scanline.getGraphics().drawImage(t,i*48,0,null);

        map.remove("bar");
        t=null;

        map.put("scanline",scanline);
    }
    public static BufferedImage get(String str) {
        return map.get(str);
    }
    public static BufferedImage getFlip(String str) {
        if(map.containsKey("flip_"+str)) return map.get("flip_"+str);
        else {
            BufferedImage img=ImageUtil.flip(map.get(str));
            map.put("flip_"+str,img);
            return img;
        }
    }
    public static BufferedImage getBrighter(String str) {
        if(map.containsKey("bright_"+str)) return map.get("bright_"+str);
        else {
            BufferedImage buf=ImageUtil.copyImage(map.get(str));
            ImageUtil.brighter(buf,1.5);
            map.put("bright_"+str,buf);
            return buf;
        }
    }
    public static void main(String args[]) throws Exception {
        load();
    }
}