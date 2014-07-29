package cytus.animation;

public class LinearAlphaTransform extends Transform {
    boolean bound=true;
    double stime=0,etime=0;
    double st=1,ed=1;
    public LinearAlphaTransform(double stime,double etime,double st,double ed) {
        this.stime=stime;
        this.etime=etime;
        this.st=st;
        this.ed=ed;
    }
    public LinearAlphaTransform(double stime,double etime,double st,double ed,boolean bound) {
        this.stime=stime;
        this.etime=etime;
        this.st=st;
        this.ed=ed;
        this.bound=bound;
    }
    public void adjust(double time) {
        if(time<stime) {
            if(bound) s.setAlpha(st);

            return;
        }

        if(time>etime) {
            if(bound) s.setAlpha(ed);

            return;
        }

        double pos=(time-stime)/(etime-stime);
        double alpha=st+(ed-st)*pos;
        s.setAlpha(alpha);
    }
}