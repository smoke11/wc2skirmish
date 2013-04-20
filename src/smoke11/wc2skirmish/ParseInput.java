package smoke11.wc2skirmish;

import org.newdawn.slick.Input;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: nao
 * Date: 20.04.13
 * Time: 11:11
 * To change this template use File | Settings | File Templates.
 */
public class ParseInput {
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
    public static ArrayList<String> update(Input input)
    {
        ArrayList<String> actions = new ArrayList<String>();
        for(Controls control : Controls.values())
            if(input.isKeyDown(control.index()))
                actions.add(control.name());
        return actions;
    }

}
