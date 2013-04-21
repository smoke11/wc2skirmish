package smoke11.wc2skirmish.events;

/**
 * Created with IntelliJ IDEA.
 * User: nao
 * Date: 21.04.13
 * Time: 18:01
 * To change this template use File | Settings | File Templates.
 */
public class GeneralEvent {
    public int delta;
    public String action;

    public GeneralEvent(String action,int delta)
    {
        this.delta=delta;
        this.action=action;
    }
}
