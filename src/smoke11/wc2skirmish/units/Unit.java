package smoke11.wc2skirmish.units;

import org.newdawn.slick.geom.Vector2f;
import smoke11.wc2skirmish.events.*;

import java.util.ArrayList;
import java.util.EventListener;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: nao
 * Date: 21.04.13
 * Time: 16:16
 * To change this template use File | Settings | File Templates.
 */
public class Unit implements IUnitEventsListener {
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
    protected Types type;
    protected String race;
    protected int faction;

    public Vector2f getPosition() {
        return position;
    }
    public Vector2f getTilePosition() {
        return new Vector2f(position.x/32,position.y/32);
    }
    protected Vector2f position;
    protected Vector2f destination; //its for moving or attacking
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
        if(destination!=null)//if there is destination, then move unit to it
        {}
    }
    @Override
    public void MoveUnitEvent(UnitEvent e) {
        destination = e.destinationVector;

    }
    private void MoveUnit(int delta)
    {
        //TODO: http://stackoverflow.com/questions/9742039/a-pathfinding-java-slick2d-library
        for (IUnitUpdatesEventsListener listener : _listeners)
            listener.UnitMovedEvent(new UnitUpdatesEvent(IUnitUpdatesEventsListener.possibleActions.UNIT_MOVED.name(),this,position,destination));
    }

    public static synchronized void addEventListener(IUnitUpdatesEventsListener listener)  {
        _listeners.add(listener);

    }
    public static synchronized void removeEventListener(IUnitUpdatesEventsListener listener)   {
        _listeners.remove(listener);

    }
}
