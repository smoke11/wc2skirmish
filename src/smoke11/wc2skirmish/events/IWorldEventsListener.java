package smoke11.wc2skirmish.events;

import java.util.EventListener;


public interface IWorldEventsListener extends EventListener {
    public enum possibleActions
    {
        WORLD_SELECTUNIT,
        WORLD_MOVEUNIT;
    }
    public void SelectUnitEvent(WorldEvent e);
    public void MoveUnitEvent(WorldEvent e);
}
