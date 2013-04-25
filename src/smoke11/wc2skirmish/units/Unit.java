package smoke11.wc2skirmish.units;

import org.newdawn.slick.util.pathfinding.Mover;
import org.newdawn.slick.util.pathfinding.Path;
import smoke11.DebugView;
import smoke11.wc2skirmish.SlickUtilities;
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
    protected Vector2 Size;

    public Types getType() {
        return type;
    }

    protected Types type;
    protected String race;
    protected int faction;
    protected void setPosition(Vector2 position) //USE THIS TO change value, it will update all variables that are depend on this var
    {
        this.position=position;
        tilePosition=Vector2.div(position,32);
    }
    public Vector2 getPosition() { return position; }
    public Vector2 getTilePosition() { return tilePosition; }
    public Vector2 getLastTilePosition(){ return lastTilePosition; }
    protected void setLastPosition(Vector2 position) //USE THIS TO change value, it will update all variables that are depend on this var
    {
        this.lastPosition=position;
        lastTilePosition=Vector2.div(lastPosition,32);
    }
    protected void setDestinationVector(Vector2 position) //USE THIS TO change value, it will update all variables that are depend on this var
    {
        this.destinationVector=position;
        destinationTileVector=Vector2.div(destinationVector,32);
    }
    private Vector2 position;
    private Vector2 tilePosition;
    private Vector2 lastPosition;
    private Vector2 lastTilePosition;
    private Vector2 destinationVector; //next step in destinationPath
    private Vector2 destinationTileVector;
    private Path destinationPath; //its for moving or attacking
    private int destinationPath_ActualIndex;
    private int destinationPath_Length;

    protected Unit()
    {
        this.Size = new Vector2(32,32); //TODO: initialize proper sizes for units
    }
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

    public boolean Update(int delta) //if there is something to update, return true, otherwise false. GameState will know that it`s not needed to update that unit anymore
    {
        if(destinationPath !=null)//if there is destinationPath, then move unit to it
        {
            MoveUnit(delta);
            return true;
        }
        return false;
    }
    @Override
    public void MoveUnitEvent(UnitEvent e) {
        DebugView.writeDebug(DebugView.DEBUGLVL_MOREINFO,"Unit","Moving event, preparing to move.");
        destinationPath_ActualIndex =1;
        destinationPath = e.destinationPath;
        DebugView.writeDebug(DebugView.DEBUGLVL_MOREINFO,"Unit","Destination path:"+ SlickUtilities.DestinationPathToString(destinationPath));
        setDestinationVector(position.copy());
        destinationPath_Length =destinationPath.getLength();
    }


    private void MoveUnit(int delta)
    {
        DebugView.writeDebug(DebugView.DEBUGLVL_MOREINFO,"Unit","[Moving]: next iteration");
        setLastPosition(position.copy());
        DebugView.writeDebug(DebugView.DEBUGLVL_MOREINFO, "Unit", "[Moving]: position: " + tilePosition + position);
        if(destinationVector.compareVector2(lastPosition)) //because unit 'moving' at one tile at time, if unit reach destination tile it is needed to get next one on path
        {
            if(destinationPath_ActualIndex < destinationPath_Length)
            {
                int x = destinationPath.getStep(destinationPath_ActualIndex).getX();
                int y = destinationPath.getStep(destinationPath_ActualIndex).getY();
                setDestinationVector(new Vector2(x*32,y*32));
                DebugView.writeDebug(DebugView.DEBUGLVL_MOREINFO,"Unit","[Moving]: Getting new step: "+destinationTileVector);
                destinationPath_ActualIndex++;
            }
            else  //if there is no next step, end
            {
                destinationPath =null;
                DebugView.writeDebug(DebugView.DEBUGLVL_MOREINFO,"Unit","[Moving]: Destination reached.");
                return;
            }

        }
        DebugView.writeDebug(DebugView.DEBUGLVL_MOREINFO,"Unit","[Moving]: destination (next step): "+destinationTileVector + destinationVector);
        Vector2 norm = Vector2.sub(destinationVector,position);
        norm.normalize();
        DebugView.writeDebug(DebugView.DEBUGLVL_MOREINFO,"Unit","[Moving]: directions: "+norm);
        Vector2 moveVec = new Vector2((delta/34f)*speed*norm.x,(delta/34f)*speed*norm.y); //for 60 fps, delta ==17
        if(checkIfNextMoveExceedDestPoint(destinationVector, moveVec, norm))      //this method is make sure that unit will not go too far. if unit exceed dest point moveunit method must lower moveVect to value
            moveVec= Vector2.sub(destinationVector,position);
        DebugView.writeDebug(DebugView.DEBUGLVL_MOREINFO,"Unit","[Moving]: moveVec: "+moveVec);
        setPosition(Vector2.add(position,moveVec));
        DebugView.writeDebug(DebugView.DEBUGLVL_MOREINFO,"Unit","[Moving]: new postion: "+getTilePosition()+position);
        for (IUnitUpdatesEventsListener listener : _listeners)
            listener.UnitMovedEvent(new UnitUpdatesEvent(IUnitUpdatesEventsListener.possibleActions.UNIT_MOVED.name(),this));
    }
    //this method is make sure that unit will not go too far. if unit exceed dest point moveunit method must lower moveVect to value
    private boolean checkIfNextMoveExceedDestPoint(Vector2 destination, Vector2 moveVect, Vector2 norm)//norm is determining side (0..3) (up,right,down,left)
    {
        Vector2 testPos = new Vector2(position);
        testPos.add(moveVect);
        int side=0;
        if(norm.x>0)
            side=1;
        else if(norm.y>0)
            side=2;
        else if(norm.x<0)
            side=3;
        switch (side)
        {
            case 0:
                if(testPos.y<destination.y)
                    return true;
                return false;
            case 1:
                if(testPos.x>destination.x)
                    return true;
                return false;
            case 2:
                if(testPos.y>destination.y)
                    return true;
                return false;
            case 3:
                if(testPos.x<destination.x)
                    return true;
                return false;
        }
        return true;
    }


    public static synchronized void addEventListener(IUnitUpdatesEventsListener listener)  {
        _listeners.add(listener);

    }
    public static synchronized void removeEventListener(IUnitUpdatesEventsListener listener)   {
        _listeners.remove(listener);

    }
}
