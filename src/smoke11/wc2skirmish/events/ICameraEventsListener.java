package smoke11.wc2skirmish.events;

import java.util.EventListener;

/**
 * Created with IntelliJ IDEA.
 * User: nao
 * Date: 21.04.13
 * Time: 17:22
 * To change this template use File | Settings | File Templates.
 */
public interface ICameraEventsListener extends EventListener {
    public enum PossibleActions
    {
        CAMERA_UP,
        CAMERA_DOWN,
        CAMERA_LEFT,
        CAMERA_RIGHT;
    }
    public void MoveCameraEvent(GeneralEvent e);
}
