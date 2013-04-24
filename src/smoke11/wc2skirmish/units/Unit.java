package smoke11.wc2skirmish.units;

import org.newdawn.slick.util.pathfinding.Mover;
import org.newdawn.slick.util.pathfinding.Path;
import smoke11.DebugView;
import smoke11.wc2skirmish.events.*;
import smoke11.wc2utils.Vector2;

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
    protected Vector2 damage; //from min to max
    protected int range;
    protected int sight;
    protected int speed;

    public Types getType() {
        return type;
    }

    protected Types type;
    protected String race;
    protected int faction;

    public Vector2 getPosition() {
        return position;
    }
    public Vector2 getTilePosition() {
        return new Vector2((int)position.x/32,(int)position.y/32);
    }
    public Vector2 getLastTilePosition() {
        return new Vector2((int)lastPosition.x/32,(int)lastPosition.y/32);
    }
    public Vector2 getdestinationTileVector()
    {
        return new Vector2((int)destinationVector.x/32,(int)destinationVector.y/32);
    }
    protected Vector2 position;
    protected Vector2 lastPosition;
    protected Vector2 destinationVector; //next step in destinationPath
    protected Path destinationPath; //its for moving or attacking
    protected int destIndex;
    protected Unit()
    {}
    protected Unit(String nameOfUnit, int id, int health, int mana, int level, int armor, Vector2 damage, int range, int sight, int speed,Types type, String race, int faction, Vector2 postion)
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
/*    private static Unit createUnit(String nameOfUnit, int id, int health, int mana, int level, int armor, Vector2 damage, int range, int sight, int speed,Types type, String race, int faction, Vector2 position)
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
        destinationVector=new Vector2(position);

    }
    private void getNextStep()
    {

    }
    private void MoveUnit(int delta)
    {

            DebugView.writeDebug(DebugView.DEBUGLVL_MOREINFO,"Unit","[Moving]: next iteration");
            lastPosition=new Vector2(position);
            DebugView.writeDebug(DebugView.DEBUGLVL_MOREINFO,"Unit","[Moving]: position: "+getLastTilePosition()+lastPosition);
            if(getdestinationTileVector().compareVector2f(getTilePosition())) //because unit 'moving' at one tile at time, if unit reach destination tile it is needed to get next one on path
            {
                if(destIndex<destinationPath.getLength())
                {
                    int x = destinationPath.getStep(destIndex).getX();
                    int y = destinationPath.getStep(destIndex).getY();
                    destinationVector = new Vector2(x*32,y*32);
                    DebugView.writeDebug(DebugView.DEBUGLVL_MOREINFO,"Unit","[Moving]: Getting new step: "+getdestinationTileVector());
                    destIndex++;
                }
                else
                {
                    destinationPath =null;
                    DebugView.writeDebug(DebugView.DEBUGLVL_MOREINFO,"Unit","[Moving]: Destination reached.");
                    return;
                }

            }
            Vector2 sub = new Vector2(destinationVector);
            DebugView.writeDebug(DebugView.DEBUGLVL_MOREINFO,"Unit","[Moving]: sub: "+sub);
            sub.sub(position);
            Vector2 norm = new Vector2(sub);
            norm.normalize();
            Vector2 moveVec = new Vector2((delta/34f)*speed*norm.x,(delta/34f)*speed*norm.y); //for 60 fps, delta ==17
            DebugView.writeDebug(DebugView.DEBUGLVL_MOREINFO,"Unit","[Moving]: moveVec: "+moveVec);
            position.add(moveVec);
            DebugView.writeDebug(DebugView.DEBUGLVL_MOREINFO,"Unit","[Moving]: new postion: "+getTilePosition()+position);
            Vector2 lastpos=getLastTilePosition();
            Vector2 pos = getTilePosition();
        for (IUnitUpdatesEventsListener listener : _listeners)
            listener.UnitMovedEvent(new UnitUpdatesEvent(IUnitUpdatesEventsListener.possibleActions.UNIT_MOVED.name(),this,lastpos,pos));
        }



    public static synchronized void addEventListener(IUnitUpdatesEventsListener listener)  {
        _listeners.add(listener);

    }
    public static synchronized void removeEventListener(IUnitUpdatesEventsListener listener)   {
        _listeners.remove(listener);

    }
}
