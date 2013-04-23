package smoke11.wc2skirmish;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.*;
import org.newdawn.slick.state.GameState;
import smoke11.DebugView;

public class Main extends StateBasedGame {

    public static enum States
    {
        INITIALIZE (1),
        GAME (2);
        private final int index;

        States(int index) {
            this.index = index;
        }

        public int index() {
            return index;
        }
    }

    public Main() {
        super("WC2 Skirmish");
    }


    public void initStatesList(GameContainer container) {
        addState(new InitializeState());
        addState(new smoke11.wc2skirmish.GameState());
    }


    public static void main(String[] argv) {
        try {
            if(Main.class.getProtectionDomain().getCodeSource().getLocation().getPath().contains(".jar"))//if it is stand alone, make console window
                DebugView.createWindow(830, 0, 200, 400, DebugView.DEBUGLVL_LESSINFO);
            else
                DebugView.setDebugLevel(DebugView.DEBUGLVL_MOREINFO);
            //initialize main window
            AppGameContainer container = new AppGameContainer(new Main());
            container.setTargetFrameRate(60);
            container.setDisplayMode(1400,900,false);
            container.start();
        } catch (SlickException e) {
            e.printStackTrace();
        }
    }
}