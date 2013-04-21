package smoke11.wc2skirmish;

import org.newdawn.slick.Input;
import smoke11.wc2skirmish.events.GeneralEvent;
import smoke11.wc2skirmish.events.ICameraEventsListener;
import smoke11.wc2skirmish.events.IUnitEventsListener;
import smoke11.wc2skirmish.events.UnitEvent;
import smoke11.wc2skirmish.units.Unit;

import java.util.ArrayList;
import java.util.EventListener;
import java.util.List;

///////////////////
//Idea of this class is to parse input from state "what keys are down" to "what actions are needed to be processed?" (update method)
//After that, fireEvent is checking which type of events are needed to fired and fire them.
//And there, all listeners get type of actions, needed variables and processing it (or ignore it) acordingly.
//////////////////

public class ParseInput {
    private static List _listeners = new ArrayList(); //all listeners
    private static ArrayList<ICameraEventsListener> _cameraListeners = new ArrayList(); //listeners for camera events
    private static ArrayList<IUnitEventsListener> _unitListeners = new ArrayList(); //listeners for units events
    private static Unit selectedUnit;
    public static enum Controls
    {
        CAMERA_UP(Input.KEY_UP),
        CAMERA_DOWN(Input.KEY_DOWN),
        CAMERA_LEFT(Input.KEY_LEFT),
        CAMERA_RIGHT(Input.KEY_RIGHT),
        UNIT_MOVE(Input.MOUSE_RIGHT_BUTTON);

        private final int index;

        Controls(int index) {
            this.index = index;
        }

        public int index() {
            return index;
        }

    }
    public static void update(Input input, int delta)
    {
        ArrayList<String> actions = new ArrayList<String>();
        for(Controls control : Controls.values())
            if(input.isKeyDown(control.index()))
                actions.add(control.name());
        fireEvent(actions, delta);
    }
    private static void fireEvent(ArrayList<String> actions, int delta)
    {
        for (String action : actions)
        {
            if(action.contains("CAMERA"))
                fireCameraEvent(action, delta);
            else if(action.contains("UNIT"))
                fireUnitEvent(action,delta);
        }
    }
    private static void fireCameraEvent(String action, int delta)
    {
        for (ICameraEventsListener listener : _cameraListeners)
            listener.MoveCameraEvent(new GeneralEvent(action,delta));

    }
    private static void fireUnitEvent(String action, int delta)
    {
        if(action.equalsIgnoreCase(IUnitEventsListener.PossibleActions.UNIT_MOVE.name()))
            selectedUnit.MoveUnitEvent(new UnitEvent(action,delta,selectedUnit));
    }
    public static synchronized void addEventListener(EventListener listener)  {
        _listeners.add(listener);
        if(listener instanceof ICameraEventsListener)
            _cameraListeners.add((ICameraEventsListener)listener);
        else if(listener instanceof  IUnitEventsListener)
            _unitListeners.add((IUnitEventsListener)listener);
    }
    public static synchronized void removeEventListener(EventListener listener)   {
        _listeners.remove(listener);
        if(listener instanceof ICameraEventsListener)
            _cameraListeners.remove(listener);
        else if(listener instanceof IUnitEventsListener)
            _unitListeners.remove(listener);
    }
}
