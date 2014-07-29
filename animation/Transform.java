package cytus.animation;
public abstract class Transform {
    Sprite s=null;
    public void setSprite(Sprite s) {
        this.s=s;
    }
    public abstract void adjust(double time);
}
