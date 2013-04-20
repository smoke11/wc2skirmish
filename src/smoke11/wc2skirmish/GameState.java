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
    private static Tile[][] unitTiles;
    private static HashMap<String, SpriteSheet> terrainSpriteSheets;
    private static HashMap<String, Image[]> terrainSpriteTiles;
    private static HashMap<String, SpriteSheet> unitSpriteSheets;
    private static HashMap<String, HashMap<Integer,Image>> unitSpriteTiles;
    private static Vector2f cameraOffset = new Vector2f(0,0);

    private static HashMap<String, Integer> controls = new HashMap<String, Integer>(); //TODO: move this to xml and read it from file

    public int getID() {
        return id;
    }

    @Override
    public void init(GameContainer gameContainer, StateBasedGame stateBasedGame) throws SlickException {
        mapTiles = InitializeState.getMapTiles();
        unitTiles = InitializeState.getUnitTiles();
        terrainSpriteSheets = InitializeState.getTerrainSpriteSheets();
        terrainSpriteTiles =InitializeState.getTerrainSpriteTiles();
        unitSpriteSheets = InitializeState.getUnitSpriteSheets();
        unitSpriteTiles = InitializeState.getUnitSpriteTiles();
    }
    /////////////////
    //for rendering terrain, tiles are order by mapTiles id
    //for rendering units, there is needed to get current unit, by name (by first key in hashmap), then get current tile by unittile id
    /////////////////
    @Override
    public void render(GameContainer gameContainer, StateBasedGame stateBasedGame, Graphics graphics) throws SlickException {
        //render terrain
        String terrainname= "summertiles";
        terrainSpriteSheets.get(terrainname).startUse();
        for (int x=0; x<mapTiles.length;x++)
            for (int y=0; y<mapTiles[0].length;y++)
                terrainSpriteTiles.get(terrainname)[mapTiles[x][y].ID].drawEmbedded(cameraOffset.x+x*32,cameraOffset.y+y*32);
        terrainSpriteSheets.get(terrainname).endUse();
        //render units (buildings are units too!)
        for(String ssname : unitSpriteSheets.keySet())
        {
            SpriteSheet ss = unitSpriteSheets.get(ssname);
            if(ss==null)
                continue;
            ss.startUse();
            for (int x=0; x<mapTiles.length;x++)
                for (int y=0; y<mapTiles[0].length;y++)
                {
                    if(unitTiles[x][y]==null)
                        continue;
                    Image img = unitSpriteTiles.get(ssname).get(unitTiles[x][y].ID);
                    if(img!=null)
                        img.drawEmbedded(cameraOffset.x+x*32,cameraOffset.y+y*32);
                }
            ss.endUse();
        }
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
