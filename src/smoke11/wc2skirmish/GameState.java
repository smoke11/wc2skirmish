package smoke11.wc2skirmish;

import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.*;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.util.pathfinding.AStarPathFinder;
import smoke11.DebugView;
import smoke11.wc2skirmish.events.*;
import smoke11.wc2skirmish.units.Unit;
import smoke11.wc2skirmish.units.UnitFactory;
import smoke11.wc2utils.Tile;


import java.util.ArrayList;
import java.util.HashMap;

////////////////////
//GameState is in charge of running actual game.
//init stage is for getting necessary data from InitilizeState //TODO: write some info about how events are working and dependencies
///////////////////
public class GameState extends BasicGameState implements ICameraEventsListener, IWorldEventsListener, IUnitUpdatesEventsListener{
    private static final int id = 2;
    private static ParseInput parseInput;
    private static Terrain terrain;
    private static AStarPathFinder pathFinder;
    /////////
    //units - all lists should be updated when something happens, its job for events
    private static ArrayList<Unit> allunitsList;  //no grouping
    private static ArrayList<Unit>[][] allUnitsByCoord;
    private static ArrayList<Unit> selectedUnits; //only selected (by player) units
    private static ArrayList<Unit> unitsNeededToBeUpdated; //for making less references
    /////////
    /////////
    //for rendering

    private static HashMap<String, SpriteSheet> terrainSpriteSheets;
    private static HashMap<String, Image[]> terrainSpriteTiles;
    private static HashMap<String, SpriteSheet> unitSpriteSheets;
    private static HashMap<String, HashMap<String,Image>> unitSpriteTiles;

    private static Vector2f cameraOffset = new Vector2f(0,0);

    private static Vector2f screenRes = new Vector2f(0,0);
    /////////

    public int getID() {
        return id;
    }

    @Override
    public void init(GameContainer gameContainer, StateBasedGame stateBasedGame) throws SlickException {
        ParseInput.addEventListener(this);
        parseInput = new ParseInput();
        Unit.addEventListener(this);

        gameContainer.setMinimumLogicUpdateInterval(17);
        gameContainer.setMaximumLogicUpdateInterval(18);
        screenRes = new Vector2f(gameContainer.getScreenWidth(),gameContainer.getScreenHeight());
        terrain = new Terrain(InitializeState.getMapTiles());
        pathFinder = new AStarPathFinder(terrain,99999,false);
        ArrayList<Tile>[][] unitTiles = InitializeState.getUnitTiles();
        terrainSpriteSheets = InitializeState.getTerrainSpriteSheets();
        terrainSpriteTiles =InitializeState.getTerrainSpriteTiles();
        unitSpriteSheets = InitializeState.getUnitSpriteSheets();
        unitSpriteTiles = InitializeState.getUnitSpriteTiles();

        //making starting lists of units
        allunitsList = new ArrayList<Unit>();
        allUnitsByCoord = new ArrayList[unitTiles.length][unitTiles[0].length];
        selectedUnits = new ArrayList<Unit>();
        unitsNeededToBeUpdated = new ArrayList<Unit>();
        Unit unit;
        for (int x=0; x<unitTiles.length;x++)
            for (int y=0; y<unitTiles[0].length;y++)
            {
                allUnitsByCoord[x][y] = new ArrayList<Unit>(); //for initializing all of fields from list
                if(unitTiles[x][y]!=null)
                {
                    for(Tile tile : unitTiles[x][y])
                    {
                        if(tile!=null)
                        {
                            unit=UnitFactory.createUnit(tile.Name, 0, new Vector2f(x, y));
                            if(unit!=null)
                            {
                                allunitsList.add(unit);
                                allUnitsByCoord[x][y].add(unit);
                            }
                        }
                    }
                }
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
        Tile[][] terrainTiles = terrain.getMapTiles();
        for (int x=0; x<terrainTiles.length;x++)
            for (int y=0; y<terrainTiles[0].length;y++)
                terrainSpriteTiles.get(terrainname)[terrainTiles[x][y].ID].drawEmbedded(x*32-cameraOffset.x,y*32-cameraOffset.y);
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
                img.drawEmbedded(unit.getPosition().x-cameraOffset.x,unit.getPosition().y-cameraOffset.y);
            ss.endUse();
        }
        //rendering tilebox for selected units
        for (Unit unit : selectedUnits)
        {
            graphics.setColor(Color.white);
            graphics.draw(new Rectangle(unit.getPosition().x-cameraOffset.x,unit.getPosition().y-cameraOffset.y,32,32)); //TODO: make proper sizes for units!
        }
    }

    @Override
    public void update(GameContainer gameContainer, StateBasedGame stateBasedGame, int i) throws SlickException {

        ParseInput.InputUpdate(gameContainer.getInput(), i);
        for (Unit unit: unitsNeededToBeUpdated)
            unit.Update(i);
    }

    //////////////////
    //Events
    //////////////////

    @Override
    public void MoveCameraEvent(GeneralEvent e) {   //keep in mind that camera offset is calculated contrariwise, for rendering
        String whichSide = e.action;
        DebugView.writeDebug(DebugView.DEBUGLVL_MOREINFO,"GameState","Moving Camera to "+whichSide);
        int cameraspeed=1;  //TODO: move this to some settings actions, preferably readed from xml
        int delta=cameraspeed*e.delta;
        if(whichSide.contains(ICameraEventsListener.possibleActions.CAMERA_UP.name()))
            if(cameraOffset.y-delta>0)
                cameraOffset.y-=delta;
        if(whichSide.contains(ICameraEventsListener.possibleActions.CAMERA_DOWN.name()))
            if(cameraOffset.y+delta<terrain.getMapBounds().y-screenRes.y)
                cameraOffset.y+=delta;
        if(whichSide.contains(ICameraEventsListener.possibleActions.CAMERA_LEFT.name()))
            if(cameraOffset.x-delta>0)
                cameraOffset.x-=delta;
        if(whichSide.contains(ICameraEventsListener.possibleActions.CAMERA_RIGHT.name()))
            if(cameraOffset.x+delta<terrain.getMapBounds().x-screenRes.x)
                cameraOffset.x+=delta;
    }

    //getting from ParseInput class how area was selected by mouse, getting units that are inside it and return list of units to ParseInput class
    @Override
    public void SelectUnitEvent(WorldEvent e) {
        DebugView.writeDebug(DebugView.DEBUGLVL_MOREINFO,"GameState","Selecting unit(s)");
        selectedUnits = new ArrayList<Unit>();
        if(e.selectRect.length==2) //selecting only one unit, by mouse click
            for (Unit unit : allUnitsByCoord[e.selectRect[0]/32][e.selectRect[1]/32])
                selectedUnits.add(unit);
        DebugView.writeDebug(DebugView.DEBUGLVL_MOREINFO,"GameState","Selected "+selectedUnits.size()+" units.");
        parseInput.UnitsSelectedEvent(selectedUnits);
    }

    @Override
    public void MoveUnitEvent(WorldEvent e) {
        DebugView.writeDebug(DebugView.DEBUGLVL_MOREINFO,"GameState","Selecting units.");
        for (Unit unit : selectedUnits)
        {
            unitsNeededToBeUpdated.add(unit);
            unit.MoveUnitEvent(new UnitEvent(Unit.possibleActions.UNIT_MOVE.name(),0,unit,pathFinder.findPath(unit,(int)unit.getTilePosition().x,(int)unit.getTilePosition().y,e.selectRect[0]/32,e.selectRect[1]/32)));

        }
    }


    //////////////
    //Mouse Input
    //////////////
    @Override
    public void mouseClicked(int button,int x,int y,int clickCount)
    {
         parseInput.mouseClicked(button,(int)(x+cameraOffset.x),(int)(y+cameraOffset.y),clickCount);
    }
    @Override
    public void mouseDragged(int oldx,int oldy,int newx,int newy)
    {
        parseInput.mouseDragged(oldx,oldy,newx,newy);
    }
    ////////////
    //Unit updates - using this for keeping data of units updates.
    //i.e. unit moves, so it`s needed to update array of lists of units sorted by coordinates
    ////////////
    @Override
    public void UnitMovedEvent(UnitUpdatesEvent e) { //update postion of unit in array sorted by coordinats
        if(e.startingVector!=null&&e.destinationVector!=null)
        {
        allUnitsByCoord[(int)e.startingVector.x][(int)e.startingVector.y].remove(e.sourceUnit);
        allUnitsByCoord[(int)e.destinationVector.x][(int)e.destinationVector.y].add(e.sourceUnit);
        }
    }
}
