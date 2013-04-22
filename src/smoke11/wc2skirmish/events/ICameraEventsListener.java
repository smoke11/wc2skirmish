package smoke11.wc2skirmish.events;

import java.util.EventListener;


public interface ICameraEventsListener extends EventListener {
    public enum possibleActions
    {
        CAMERA_UP,
        CAMERA_DOWN,
        CAMERA_LEFT,
        CAMERA_RIGHT;
    }
    public void MoveCameraEvent(GeneralEvent e);
}
