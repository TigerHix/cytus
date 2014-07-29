package cytus.animation;
public class ComboSmallPop {
    //Not inherited from Transform, but a utility
    double start=0;
    public ComboSmallPop(double start) {
        this.start=start;
    }
    public double scale(double time) {
        double delta=time-start;

        if(delta>=1/6.0) return 1;

        double pos=Math.sin(delta*6*Math.PI);
        return 1+0.1*pos;
    }
}