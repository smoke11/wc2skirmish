package smoke11.wc2skirmish.events;

import java.util.EventListener;

/**
 * Created with IntelliJ IDEA.
 * User: nao
 * Date: 21.04.13
 * Time: 17:22
 * To change this template use File | Settings | File Templates.
 */
public interface IUnitUpdatesEventsListener extends EventListener {
    public enum possibleActions
    {
        UNIT_MOVED,
    }
    public void UnitMovedEvent(UnitUpdatesEvent e);
}
