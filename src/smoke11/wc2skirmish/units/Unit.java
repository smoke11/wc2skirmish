package smoke11.wc2skirmish.units;

import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.util.pathfinding.Mover;
import org.newdawn.slick.util.pathfinding.Path;
import smoke11.DebugView;
import smoke11.wc2skirmish.events.*;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: nao
 * Date: 21.04.13
 * Time: 16:16
 * To change this template use File | Settings | File Templates.
 */
public class Unit implements IUnitEventsListener, Mover{
    private static ArrayList<IUnitUpdatesEventsListener> _listeners = new ArrayList(); //all listeners that is needed to be informed that unit do something
    public static enum Types{
        MELEE,
        RANGED,
        FLYING,
        SHIP
    }
    protected static String nameOfUnit; //TODO: make this and next one as final
    protected static String PudID;
    protected int id;
    protected int health;
    protected int mana;
    protected int level;
    protected int armor;
    protected Vector2f damage; //from min to max
    protected int range;
    protected int sight;
    protected int speed;

    public Types getType() {
        return type;
    }

    protected Types type;
    protected String race;
    protected int faction;

    public Vector2f getPosition() {
        return position;
    }
    public Vector2f getTilePosition() {
        return new Vector2f((int)position.x/32,(int)position.y/32);
    }
    public Vector2f getLastTilePosition() {
        return new Vector2f((int)lastPosition.x/32,(int)lastPosition.y/32);
    }
    public Vector2f getdestinationTileVector()
    {
        return new Vector2f((int)destinationVector.x/32,(int)destinationVector.y/32);
    }
    protected Vector2f position;
    protected Vector2f lastPosition;
    protected Vector2f destinationVector; //next step in destinationPath
    protected Path destinationPath; //its for moving or attacking
    protected int destIndex;
    protected Unit()
    {}
    protected Unit(String nameOfUnit, int id, int health, int mana, int level, int armor, Vector2f damage, int range, int sight, int speed,Types type, String race, int faction, Vector2f postion)
    {
        this.nameOfUnit =nameOfUnit;
        this.health=health;
        this.mana=mana;
        this.level=level;
        this.armor=armor;
        this.damage=damage;
        this.range=range;
        this.sight=sight;
        this.speed=speed;
        this.type=type;
        this.race=race;
        this.faction=faction;
        this.position=position;
    }
/*    private static Unit createUnit(String nameOfUnit, int id, int health, int mana, int level, int armor, Vector2f damage, int range, int sight, int speed,Types type, String race, int faction, Vector2f position)
    {
        return new Unit(nameOfUnit, id, health, mana, level, armor, damage, range, sight, speed,type, race, faction, position);
    }*/
    public String getName()
    {
        return nameOfUnit;
    }
    public String getPudID() {
        return PudID;
    }

    public void Update(int delta)
    {
        if(destinationPath !=null)//if there is destinationPath, then move unit to it
            MoveUnit(delta);
    }
    @Override
    public void MoveUnitEvent(UnitEvent e) {
        DebugView.writeDebug(DebugView.DEBUGLVL_MOREINFO,"Unit","Moving event, preparing to move.");
        destIndex=1;
        destinationPath = e.destinationPath;
        destinationVector=position.copy();

    }
    private void MoveUnit(int delta)
    {

        if(destIndex< destinationPath.getLength())  //if unit isn`t in destination point (if it exist), get next one
        {
            lastPosition=position.copy();
            Vector2f dest = getdestinationTileVector();
            Vector2f pos = getTilePosition();
            if(compareVector2f(getdestinationTileVector(),getTilePosition())) //because unit 'moving' at one tile at time, if unit reach destination tile it is needed to get next one on path
            {
                int x = destinationPath.getStep(destIndex).getX();
                int y = destinationPath.getStep(destIndex).getY();
                destinationVector = new Vector2f(x*32,y*32);
                destIndex++;
            }
            Vector2f sub = destinationVector.copy();  //TODO: move from slick Vector2f class to some Vector2 class (for int), to much work with this class
            sub.sub(position);
            Vector2f norm = sub.copy().normalise();
            Vector2f moveVec = new Vector2f((delta/17f)*speed*norm.x,(delta/17f)*speed*norm.y);
            position.add(moveVec);
            Vector2f lastpos=getLastTilePosition();
            pos = getTilePosition();
        for (IUnitUpdatesEventsListener listener : _listeners)
            listener.UnitMovedEvent(new UnitUpdatesEvent(IUnitUpdatesEventsListener.possibleActions.UNIT_MOVED.name(),this,lastpos,pos));
        }
        else
            destinationPath =null;
    }

    public static synchronized void addEventListener(IUnitUpdatesEventsListener listener)  {
        _listeners.add(listener);

    }
    public static synchronized void removeEventListener(IUnitUpdatesEventsListener listener)   {
        _listeners.remove(listener);

    }
    private static boolean compareVector2f(Vector2f vec1, Vector2f vec2) { return (vec1.x==vec2.x&&vec1.y==vec2.y);}
}
