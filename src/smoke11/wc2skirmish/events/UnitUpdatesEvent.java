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
    public Vector2 startingVector;
    public Vector2 destinationVector;
    public UnitUpdatesEvent(String action, Unit sourceUnit, Vector2 startingTileVector, Vector2 destinationTileVector)
    {
        super(action, -1);
        this.sourceUnit=sourceUnit;
        this.startingVector=startingVector;
        this.destinationVector=destinationVector;
    }
}
