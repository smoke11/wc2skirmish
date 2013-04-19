package smoke11.wc2skirmish;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;
import smoke11.DebugView;

public class Main extends StateBasedGame {



    public Main() {
        super("WC2 Skirmish");
    }


    public void initStatesList(GameContainer container) {
        addState(new InitializeState());

    }


    public static void main(String[] argv) {
        try {
            if(Main.class.getProtectionDomain().getCodeSource().getLocation().getPath().contains(".jar"))//if it is stand alone, make console window
                DebugView.createWindow(830, 0, 200, 400, DebugView.DEBUGLVL_LESSINFO);
            else
                DebugView.setDebugLevel(DebugView.DEBUGLVL_LESSINFO);
            //initialize main window
            AppGameContainer container = new AppGameContainer(new Main());
            container.setDisplayMode(800,600,false);
            container.start();
        } catch (SlickException e) {
            e.printStackTrace();
        }
    }
}