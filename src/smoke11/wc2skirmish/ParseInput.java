package smoke11.wc2skirmish;

import org.newdawn.slick.Input;

import java.util.ArrayList;
import java.util.EventListener;
import java.util.HashMap;
import java.util.List;

///////////////////
//Idea of this class is to parse input from state "what keys are down" to "what actions are needed to be processed?" (update method)
//After that, fireEvent is checking which type of events are needed to fired and fire them.
//And there, all listeners get type of actions, needed variables and processing it (or ignore it) acordingly.
//////////////////

public class ParseInput {
    private static List _listeners = new ArrayList(); //all listeners
    private static ArrayList<ICameraEventsListener> _cameraListeners = new ArrayList(); //listeners for camera events
    public static enum Controls
    {
        CAMERA_UP(Input.KEY_UP),
        CAMERA_DOWN(Input.KEY_DOWN),
        CAMERA_LEFT(Input.KEY_LEFT),
        CAMERA_RIGHT(Input.KEY_RIGHT);
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
    public static void fireEvent(ArrayList<String> actions, int delta)
    {
        for (String action : actions)
        {
            if(action.contains("CAMERA"))
                fireCameraEvent(action, delta);
        }
    }
    public static void fireCameraEvent(String action, int delta)
    {
        for (ICameraEventsListener listener : _cameraListeners)
            listener.MoveCamera(action,delta);

    }
    public static synchronized void addEventListener(EventListener listener)  {
        _listeners.add(listener);
        if(listener instanceof ICameraEventsListener)
            _cameraListeners.add((ICameraEventsListener)listener);
    }
    public static synchronized void removeEventListener(EventListener listener)   {
        _listeners.remove(listener);
        if(listener instanceof ICameraEventsListener)
            _cameraListeners.remove(listener);
    }
}
