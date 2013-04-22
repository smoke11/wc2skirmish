package smoke11.wc2skirmish.events;

import org.newdawn.slick.geom.Vector2f;
import smoke11.wc2skirmish.units.Unit;

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
    public Vector2f startingVector;
    public Vector2f destinationVector;
    public UnitUpdatesEvent(String action, Unit sourceUnit, Vector2f startingVector, Vector2f destinationVector)
    {
        super(action, -1);
        this.sourceUnit=sourceUnit;
        this.startingVector=startingVector;
        this.destinationVector=destinationVector;
    }
}
