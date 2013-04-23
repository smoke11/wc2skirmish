package smoke11.wc2skirmish.events;

import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.util.pathfinding.Path;
import smoke11.wc2skirmish.units.Unit;

/**
 * Created with IntelliJ IDEA.
 * User: nao
 * Date: 21.04.13
 * Time: 18:06
 * To change this template use File | Settings | File Templates.
 */
public class UnitEvent extends GeneralEvent {
    public Unit sourceUnit;
    public Unit targetUnit;
    public Path destinationPath;
    public UnitEvent(String action, int delta, Unit sourceUnit) {
        super(action,delta);
        this.sourceUnit=sourceUnit;
    }
    public UnitEvent(String action, int delta, Unit sourceUnit, Path destinationPath)
    {
        super(action, delta);
        this.sourceUnit=sourceUnit;
        this.destinationPath =destinationPath;
    }
}
