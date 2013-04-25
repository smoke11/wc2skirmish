package smoke11.wc2skirmish.events;

import smoke11.wc2skirmish.units.Unit;
import smoke11.wc2utils.Vector2;

/**
 * Created with IntelliJ IDEA.
 * User: nao
 * Date: 21.04.13
 * Time: 18:06
 * To change this template use File | Settings | File Templates.
 */
public class UnitUpdatesEvent extends GeneralEvent {
    public Unit sourceUnit;
    public Unit targetUnit;
    public UnitUpdatesEvent(String action, Unit sourceUnit)
    {
        super(action, -1);
        this.sourceUnit=sourceUnit;
    }
}
