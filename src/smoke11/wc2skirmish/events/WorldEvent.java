package smoke11.wc2skirmish.events;



/**
 * Created with IntelliJ IDEA.
 * User: nao
 * Date: 21.04.13
 * Time: 18:06
 * To change this template use File | Settings | File Templates.
 */
public class WorldEvent extends GeneralEvent {
    public int[] selectRect; //4 arguments for rectangle, x1,x2,x3,x4
    public WorldEvent(String action, int delta, int[] selectRect) {
        super(action,delta);
        this.selectRect=selectRect;
    }

}
