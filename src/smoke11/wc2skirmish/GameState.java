package smoke11.wc2skirmish;

import org.lwjgl.util.vector.Vector2f;
import org.newdawn.slick.*;
import org.newdawn.slick.Input;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import smoke11.DebugView;
import smoke11.wc2utils.Tile;


import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created with IntelliJ IDEA.
 * User: nao
 * Date: 19.04.13
 * Time: 22:57
 * To change this template use File | Settings | File Templates.
 */
public class GameState extends BasicGameState {
    private static final int id = 2;
    private static Tile[][] mapTiles;
    private static HashMap<String, SpriteSheet> spriteSheets;
    private static HashMap<String, Image[]> imageSpriteTiles;
    private static Vector2f cameraOffset = new Vector2f(0,0);

    private static HashMap<String, Integer> controls = new HashMap<String, Integer>(); //TODO: move this to xml and read it from file

    public int getID() {
        return id;
    }

    @Override
    public void init(GameContainer gameContainer, StateBasedGame stateBasedGame) throws SlickException {
        mapTiles = InitializeState.getMapTiles();
        spriteSheets = InitializeState.getSpriteSheets();
        imageSpriteTiles=InitializeState.getImageSpriteTiles();

    }

    @Override
    public void render(GameContainer gameContainer, StateBasedGame stateBasedGame, Graphics graphics) throws SlickException {
        spriteSheets.get("summertiles").startUse();
        for (int x=0; x<mapTiles.length;x++)
            for (int y=0; y<mapTiles[0].length;y++)
                imageSpriteTiles.get("summertiles")[mapTiles[x][y].ID].drawEmbedded(cameraOffset.x+x*32,cameraOffset.y+y*32);
        spriteSheets.get("summertiles").endUse();
    }

    @Override
    public void update(GameContainer gameContainer, StateBasedGame stateBasedGame, int i) throws SlickException {

        processActions(gameContainer.getInput(),i);
    }
    public void processActions(Input input, int delta)
    {
        ArrayList<String> actions = ParseInput.update(input);     //get what actions needed to be processed
        for(String action : actions)
            DebugView.writeDebug(DebugView.DEBUGLVL_MOREINFO,"GameState","Proccesing action: "+action);
        int cameraspeed=1;
        if(actions.contains(ParseInput.Controls.CAMERA_UP.name()))
            cameraOffset.y+=cameraspeed*delta;
        if(actions.contains(ParseInput.Controls.CAMERA_DOWN.name()))
            cameraOffset.y-=cameraspeed*delta;
        if(actions.contains(ParseInput.Controls.CAMERA_LEFT.name()))
            cameraOffset.x+=cameraspeed*delta;
        if(actions.contains(ParseInput.Controls.CAMERA_RIGHT.name()))
            cameraOffset.x-=cameraspeed*delta;
    }


}
