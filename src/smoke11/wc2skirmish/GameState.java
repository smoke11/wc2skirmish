package smoke11.wc2skirmish;

import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.*;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import smoke11.DebugView;
import smoke11.wc2skirmish.events.GeneralEvent;
import smoke11.wc2skirmish.events.ICameraEventsListener;
import smoke11.wc2skirmish.events.IWorldEventsListener;
import smoke11.wc2skirmish.events.WorldEvent;
import smoke11.wc2skirmish.units.Unit;
import smoke11.wc2skirmish.units.UnitFactory;
import smoke11.wc2utils.Tile;


import java.util.ArrayList;
import java.util.HashMap;


public class GameState extends BasicGameState implements ICameraEventsListener, IWorldEventsListener {
    private static final int id = 2;
    private static ParseInput parseInput;
    /////////
    //for rendering
    private static Tile[][] mapTiles;
    private static Tile[][] unitTiles; //needed for making list of units
    private static ArrayList<Unit> allunitsList;
    private static ArrayList<Unit> selectedUnits;

    private static HashMap<String, SpriteSheet> terrainSpriteSheets;
    private static HashMap<String, Image[]> terrainSpriteTiles;
    private static HashMap<String, SpriteSheet> unitSpriteSheets;
    private static HashMap<String, HashMap<String,Image>> unitSpriteTiles;

    private static Vector2f cameraOffset = new Vector2f(0,0);
    /////////

    private static HashMap<String, Integer> controls = new HashMap<String, Integer>(); //TODO: move this to xml and read it from file
    public int getID() {
        return id;
    }

    @Override
    public void init(GameContainer gameContainer, StateBasedGame stateBasedGame) throws SlickException {
        ParseInput.addEventListener(this);
        parseInput = new ParseInput();

        mapTiles = InitializeState.getMapTiles();
        unitTiles = InitializeState.getUnitTiles();
        terrainSpriteSheets = InitializeState.getTerrainSpriteSheets();
        terrainSpriteTiles =InitializeState.getTerrainSpriteTiles();
        unitSpriteSheets = InitializeState.getUnitSpriteSheets();
        unitSpriteTiles = InitializeState.getUnitSpriteTiles();

        //making starting list of units
        allunitsList = new ArrayList<Unit>();
        selectedUnits = new ArrayList<Unit>();
        Unit unit;
        for (int x=0; x<unitTiles.length;x++)
            for (int y=0; y<unitTiles[0].length;y++)
                if(unitTiles[x][y]!=null)
                {
                    unit=UnitFactory.createUnit(unitTiles[x][y].Name, 0, new Vector2f(x * 32, y * 32));
                    if(unit!=null)
                        allunitsList.add(unit);
                }
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
        String ssname;
        for (Unit unit : allunitsList)
        {
            ssname=unit.getName().toLowerCase();
            SpriteSheet ss = unitSpriteSheets.get(ssname);
            if(ss==null)
                continue;
            ss.startUse();
            Image img = unitSpriteTiles.get(ssname).get(unit.getPudID());
            if(img!=null)
                img.drawEmbedded(cameraOffset.x+unit.getPosition().x,cameraOffset.y+unit.getPosition().y);
            ss.endUse();
        }
        //rendering tilebox for selected units
        for (Unit unit : selectedUnits)
        {
            graphics.setColor(Color.white);
            graphics.draw(new Rectangle(unit.getPosition().x,unit.getPosition().y,32,32)); //TODO: make proper sizes for units!
        }
    }

    @Override
    public void update(GameContainer gameContainer, StateBasedGame stateBasedGame, int i) throws SlickException {

        ParseInput.keyboardInputUpdate(gameContainer.getInput(), i);

    }



    @Override
    public void MoveCameraEvent(GeneralEvent e) {
        String whichSide = e.action;
        DebugView.writeDebug(DebugView.DEBUGLVL_MOREINFO,"GameState","Moving Camera to "+whichSide);
        int cameraspeed=1;  //TODO: move this to some settings actions, preferably readed from xml
        if(whichSide.contains(ICameraEventsListener.possibleActions.CAMERA_UP.name()))
            cameraOffset.y+=cameraspeed*e.delta;
        if(whichSide.contains(ICameraEventsListener.possibleActions.CAMERA_DOWN.name()))
            cameraOffset.y-=cameraspeed*e.delta;
        if(whichSide.contains(ICameraEventsListener.possibleActions.CAMERA_LEFT.name()))
            cameraOffset.x+=cameraspeed*e.delta;
        if(whichSide.contains(ICameraEventsListener.possibleActions.CAMERA_RIGHT.name()))
            cameraOffset.x-=cameraspeed*e.delta;
    }

    //getting from ParseInput class how area was selected by mouse, getting units that are inside it and return list of units to ParseInput class
    @Override
    public void SelectUnitEvent(WorldEvent e) {
        DebugView.writeDebug(DebugView.DEBUGLVL_MOREINFO,"GameState","Getting mouse selection from ParseInput and passing selected units to ParseInput");
        selectedUnits = new ArrayList<Unit>();
        Rectangle rect = new Rectangle(e.selectRect[0]/32,e.selectRect[1]/32,(e.selectRect[2]/32-e.selectRect[0]/32),(e.selectRect[3]/32-e.selectRect[1]/32));
        for (Unit unit : allunitsList) //TODO: make efficient solution to this, it works now because there are few units on map. maybe getting from array unitTiles (but it`s not updated so think about that too)
            if(rect.contains(unit.getTilePosition().x,unit.getTilePosition().y))
                selectedUnits.add(unit);
        DebugView.writeDebug(DebugView.DEBUGLVL_MOREINFO,"GameState","Selected "+selectedUnits.size()+" units.");
        parseInput.UnitsSelectedEvent(selectedUnits);
    }
    //// /////////
    //Mouse Input
    //////////////
    @Override
    public void mouseClicked(int button,int x,int y,int clickCount)
    {
         parseInput.mouseClicked(button,x,y,clickCount);
    }
    @Override
    public void mouseDragged(int oldx,int oldy,int newx,int newy)
    {
        parseInput.mouseDragged(oldx,oldy,newx,newy);
    }
}
