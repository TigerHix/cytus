package cytus.animation;
import net.sf.json.*;
import java.io.*;
import java.util.*;
public class AnimationPreset {
    static String flist[]=new String[] {"arrow_explode","arrow_flash","beat_flash","beat_vanish","critical_explosion","drag_light","explosion","hold_pressing_1","hold_pressing_2","judge_bad","judge_good","judge_miss","judge_perfect","light_add_2","node_explode","node_flash"};
    static HashMap<String,Double> map1=new HashMap<String,Double>();
    static HashMap<String,Boolean> map2=new HashMap<String,Boolean>();
    static HashMap<String,String[]> map3=new HashMap<String,String[]>();
    public static void load() throws Exception {

        for(int i=0; i<16; i++) {
            BufferedReader r=new BufferedReader(new FileReader("assets/ui/gameplay/"+flist[i]+".anim.json"));
            String s="";
            String str=r.readLine();

            while(str!=null) {
                s+=str;
                str=r.readLine();
            }

            r.close();
            JSONObject obj=JSONObject.fromObject(s);
            map1.put(flist[i],obj.getDouble("length"));
            map2.put(flist[i],obj.getString("mode").equals("once"));
            JSONArray arr=JSONArray.fromObject(obj.getString("frames"));
            Object f[]=arr.toArray();
            String frames[]=new String[f.length];

            for(int j=0; j<frames.length; j++) {
                JSONArray arr2=JSONArray.fromObject(f[j]);
                frames[j]=(String)arr2.get(1);
            }

            map3.put(flist[i],frames);
        }
    }
    public static Animation get(String str) {
        double len=map1.get(str);
        boolean mode=map2.get(str);
        String frames[]=map3.get(str);
        Animation anim=new Animation(frames.length,len,mode,frames);

        if(str.equals("light_add_2")) anim.setAnchor(0.5,0.3);

        return anim;
    }
}