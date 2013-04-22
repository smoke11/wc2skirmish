package smoke11.wc2skirmish;


import org.newdawn.slick.Input;
import org.newdawn.slick.MouseListener;
import org.newdawn.slick.geom.Vector2f;
import smoke11.DebugView;
import smoke11.wc2skirmish.events.*;
import smoke11.wc2skirmish.units.Unit;

import java.util.ArrayList;
import java.util.EventListener;
import java.util.List;

///////////////////
//Idea of this class is to parse input from state "what keys are down" to "what actions are needed to be processed?" (InputUpdate method)
//After that, fireEvent is checking which type of events are needed to fired and fire them.
//And there, all listeners get type of actions, needed variables and processing it (or ignore it) acordingly.
//////////////////

public class ParseInput implements ISelectedUnitsEventListener, IGameState_MouseEventsListener {
    private static List _listeners = new ArrayList(); //all listeners
    private static ArrayList<ICameraEventsListener> _cameraListeners = new ArrayList<ICameraEventsListener>(); //listeners for camera events
    private static ArrayList<IUnitEventsListener> _unitListeners = new ArrayList<IUnitEventsListener>(); //listeners for units events
    private static ArrayList<IWorldEventsListener> _worldListeners = new ArrayList<IWorldEventsListener>(); //listener for world events
    private static List<Unit> selectedUnits;
    private static ArrayList<String> actions= new ArrayList<String>();
    private static int[] mouseRect;
    //////////
    //Parsing input
    //////////
    public static enum KeyboardControls
    {
        CAMERA_UP(Input.KEY_UP),
        CAMERA_DOWN(Input.KEY_DOWN),
        CAMERA_LEFT(Input.KEY_LEFT),
        CAMERA_RIGHT(Input.KEY_RIGHT);


        private final int index;

        KeyboardControls(int index) {
            this.index = index;
        }

        public int index() {
            return index;
        }

    }

    public static void InputUpdate(Input input, int delta)
    {
        for(KeyboardControls control : KeyboardControls.values())
            if(input.isKeyDown(control.index()))
                actions.add(control.name());
        fireEvent(actions, delta);
    }

    public enum MouseControls
    {
        WORLD_SELECTUNIT(Input.MOUSE_LEFT_BUTTON, 0), //first value is for Input.Key and second for number of button from events data
        UNIT_MOVE(Input.MOUSE_RIGHT_BUTTON, 1);

        private List<Integer> indexes;

        MouseControls(int index, int index2) {
            indexes.add(index);
            indexes.add(index2);
        }

        public List<Integer> indexes() {
            return this.indexes;
        }


    }
    @Override
    public void mouseDragged(int oldx, int oldy, int newx, int newy) {
        DebugView.writeDebug(DebugView.DEBUGLVL_MOREINFO,"ParseInput","Mouse dragged from: "+oldx+","+oldy+" to "+newx+","+newy);
    }

    @Override
    public void mouseClicked(int button, int x, int y, int clickCount) {
        DebugView.writeDebug(DebugView.DEBUGLVL_MOREINFO,"ParseInput","Mouse clicked: "+x+","+y);
        mouseRect = new int[]{x,y};
        if(button==0)
            actions.add(IWorldEventsListener.possibleActions.WORLD_SELECTUNIT.name());
        else if(button==1)
            actions.add(IUnitEventsListener.possibleActions.UNIT_MOVE.name());
    }
    //////////////
    //Events
    //////////////
    @Override
    public void UnitsSelectedEvent(ArrayList<Unit> units) {
        DebugView.writeDebug(DebugView.DEBUGLVL_MOREINFO,"ParseInput","Getting selected units from GameState");
        selectedUnits=units;
    }

    private static void fireEvent(ArrayList<String> actions, int delta)
    {
        for (String action : actions)
        {
            String[] split=action.split("_");
            if(split[0].contains("WORLD"))  //TODO: SOMETHING WRONG IS HERE!
                fireWorldEvent(action,delta);
            else if(split[0].contains("CAMERA"))
                fireCameraEvent(action, delta);
            else if(split[0].contains("UNIT"))
                fireUnitEvent(action,delta);
        }
        actions.removeAll(actions);
    }
    private static void fireCameraEvent(String action, int delta)
    {
        for (ICameraEventsListener listener : _cameraListeners)
            listener.MoveCameraEvent(new GeneralEvent(action,delta));

    }
    private static void fireUnitEvent(String action, int delta)
    {
        if(action.equalsIgnoreCase(IUnitEventsListener.possibleActions.UNIT_MOVE.name()))
            for (Unit unit : selectedUnits)
                unit.MoveUnitEvent(new UnitEvent(action,delta,unit, new Vector2f(mouseRect[0],mouseRect[1])));
    }
    private static void fireWorldEvent(String action, int delta)
    {
        if(action.equalsIgnoreCase(IWorldEventsListener.possibleActions.WORLD_SELECTUNIT.name()))
            for (IWorldEventsListener listener : _worldListeners)
                listener.SelectUnitEvent(new WorldEvent(action,delta,mouseRect));
    }
    public static synchronized void addEventListener(EventListener listener)  {
        _listeners.add(listener);
        if(listener instanceof ICameraEventsListener)
            _cameraListeners.add((ICameraEventsListener)listener);
        if(listener instanceof  IUnitEventsListener)
            _unitListeners.add((IUnitEventsListener)listener);
        if(listener instanceof  IWorldEventsListener)
            _worldListeners.add((IWorldEventsListener)listener);
    }
    public static synchronized void removeEventListener(EventListener listener)   {
        _listeners.remove(listener);
        if(listener instanceof ICameraEventsListener)
            _cameraListeners.remove(listener);
        if(listener instanceof IUnitEventsListener)
            _unitListeners.remove(listener);
        if(listener instanceof IWorldEventsListener)
            _worldListeners.remove(listener);
    }

}

interface ISelectedUnitsEventListener { void UnitsSelectedEvent(ArrayList<Unit> units);} //getting actual list of selected units from GameState class
interface IGameState_MouseEventsListener{   void mouseDragged(int oldx,int oldy,int newx,int newy); void mouseClicked(int button,int x,int y,int clickCount);} //getting mouse input from GameState class