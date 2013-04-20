package smoke11.wc2skirmish;

import org.newdawn.slick.*;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import smoke11.wc2utils.Tile;

import java.util.HashMap;

/**
 * Created with IntelliJ IDEA.
 * User: nao
 * Date: 19.04.13
 * Time: 22:57
 * To change this template use File | Settings | File Templates.
 */
public class GameState extends BasicGameState{
    private static final int id = 2;
    private static Tile[][] mapTiles;
    private static HashMap<String, SpriteSheet> spriteSheets;
    private static HashMap<String, Image[]> imageSpriteTiles;
    public int getID() {
        return id;
    }

    @Override
    public void init(GameContainer gameContainer, StateBasedGame stateBasedGame) throws SlickException {
        mapTiles = InitializeState.mapTiles;
        spriteSheets = InitializeState.spriteSheets;
        imageSpriteTiles=InitializeState.imageSpriteTiles;
    }

    @Override
    public void render(GameContainer gameContainer, StateBasedGame stateBasedGame, Graphics graphics) throws SlickException {
        spriteSheets.get("summertiles").startUse();
        for (int x=0; x<mapTiles.length;x++)
            for (int y=0; y<mapTiles[0].length;y++)
                imageSpriteTiles.get("summertiles")[mapTiles[x][y].ID].drawEmbedded(x*32,y*32);
        spriteSheets.get("summertiles").endUse();
    }

    @Override
    public void update(GameContainer gameContainer, StateBasedGame stateBasedGame, int i) throws SlickException {
        //To change body of implemented methods use File | Settings | File Templates.
    }
    //TODO: move here rendering logic from initializestate
}
