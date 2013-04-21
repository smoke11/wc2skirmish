package smoke11.wc2skirmish.events;

import java.util.EventListener;

/**
 * Created with IntelliJ IDEA.
 * User: nao
 * Date: 21.04.13
 * Time: 17:22
 * To change this template use File | Settings | File Templates.
 */
public interface IUnitEventsListener extends EventListener {
    public enum PossibleActions
    {
        UNIT_MOVE,
        UNIT_STOP,
        UNIT_ATTACK,
        UNIT_HARVEST,
        UNIT_BUILD,
        UNIT_REPAIR,
        UNIT_USESKILL
    }
    public void MoveUnitEvent(UnitEvent e);
}
