package smoke11.wc2skirmish;

import org.lwjgl.util.vector.Vector4f;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.*;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.util.pathfinding.AStarPathFinder;
import org.newdawn.slick.util.pathfinding.Path;
import smoke11.DebugView;
import smoke11.wc2skirmish.events.*;
import smoke11.wc2skirmish.units.Unit;
import smoke11.wc2skirmish.units.UnitFactory;
import smoke11.wc2utils.Tile;
import smoke11.wc2utils.Vector2;


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
    private final static ArrayList<Unit> allunitsList = new ArrayList<Unit>();  //no grouping
    private static ArrayList<Unit>[][] allUnitsByCoord;
    private final static ArrayList<Unit> selectedUnits = new ArrayList<Unit>(); //only selected (by player) units
    private final static ArrayList<Unit> unitsNeededToBeUpdated = new ArrayList<Unit>(); //for making less references
    private final static ArrayList<Unit> unitsToRemoveFromUpdateList = new ArrayList<Unit>();
    /////////
    /////////
    //for rendering
    private static boolean drawingGrid = true;
    private static HashMap<String, SpriteSheet> terrainSpriteSheets;      //TODO: make this as final!
    private static HashMap<String, Image[]> terrainSpriteTiles;
    private static HashMap<String, SpriteSheet> unitSpriteSheets;
    private static HashMap<String, HashMap<String,Image>> unitSpriteTiles;

    private static Vector2 cameraOffset = new Vector2(0,0);

    private static Vector2 screenRes = new Vector2(0,0);
    private static float alpha=0f;
    /////////

    public static Rectangle getScreenRect()//for getting rectangle of visibly, to reduce drawing
    {return new Rectangle(cameraOffset.x, cameraOffset.y,screenRes.x,screenRes.y);}
    public static Rectangle getScreenTileRect()//for getting rectangle of visibly tiles to reduce drawing
    {return new Rectangle(cameraOffset.x/32, cameraOffset.y/32,screenRes.x/32,screenRes.y/32);}
    public int getID() {
        return id;
    }

    @Override
    public void init(GameContainer gameContainer, StateBasedGame stateBasedGame) throws SlickException {
        ParseInput.addEventListener(this);
        parseInput = new ParseInput();
        Unit.addEventListener(this);

        screenRes = new Vector2(gameContainer.getScreenWidth(),gameContainer.getScreenHeight());
        ArrayList<Tile>[][] unitTiles = InitializeState.getUnitTiles();
        terrainSpriteSheets = InitializeState.getTerrainSpriteSheets();
        terrainSpriteTiles =InitializeState.getTerrainSpriteTiles();
        unitSpriteSheets = InitializeState.getUnitSpriteSheets();
        unitSpriteTiles = InitializeState.getUnitSpriteTiles();
        gameContainer.setMinimumLogicUpdateInterval(17);
        gameContainer.setMaximumLogicUpdateInterval(18);
        //making starting lists of units
        allUnitsByCoord = new ArrayList[unitTiles.length][unitTiles[0].length];

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
        terrain = new Terrain(InitializeState.getMapTiles(),allUnitsByCoord);
        pathFinder = new AStarPathFinder(terrain,99999,false);
    }

    /////////////////
    //for rendering terrain, tiles are order by mapTiles id
    //for rendering units, there is needed to get current unit, by name (by first key in hashmap), then get current tile by unittile id
    /////////////////
    @Override
    public void render(GameContainer gameContainer, StateBasedGame stateBasedGame, Graphics graphics) throws SlickException {

        //render terrain  TODO: draw all the things only on screen rect
        Rectangle screenRectangleTiles = getScreenTileRect();
        String terrainname= "summertiles";
        terrainSpriteSheets.get(terrainname).startUse();
        Tile[][] terrainTiles = terrain.getMapTiles();
        for (int x=0; x<terrainTiles.length;x++)
            for (int y=0; y<terrainTiles[0].length;y++)
                terrainSpriteTiles.get(terrainname)[terrainTiles[x][y].ID].drawEmbedded(x*32-cameraOffset.x,y*32-cameraOffset.y);
        terrainSpriteSheets.get(terrainname).endUse();
        //render grid
        if(drawingGrid)
        {
            for (int x=(int)screenRectangleTiles.getMinX();x<(int)screenRectangleTiles.getMaxX();x++)
                for (int y=(int)screenRectangleTiles.getMinY();y<(int)screenRectangleTiles.getMaxY();y++)
                    graphics.drawRect(x*32-cameraOffset.x,y*32-cameraOffset.y,32,32);
        }
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
        pause_resumeGameIfFocusChanged(gameContainer,gameContainer.getGraphics());
        ParseInput.InputUpdate(gameContainer.getInput(), i);
        for (Unit unit: unitsNeededToBeUpdated)
            if(!unit.Update(i)) //if there is no need in updateing unit (return false) remove it from updatelist
                unitsToRemoveFromUpdateList.add(unit);
        unitsNeededToBeUpdated.removeAll(unitsToRemoveFromUpdateList);
        unitsToRemoveFromUpdateList.clear();

    }
    public void pause_resumeGameIfFocusChanged(GameContainer gameContainer, Graphics gr)//when player alt tab or smthing similar game pauses, if get back to game, game will run again
    {
         if(!gameContainer.hasFocus())
         {
             Rectangle rect = new Rectangle (0, 0, screenRes.x, screenRes.y);
             gr.setColor(new Color (0.2f, 0.2f, 0.2f));
             gr.fill(rect);
             gameContainer.pause();
             this.pauseUpdate();
             this.pauseRender();
         }
         else if(gameContainer.isPaused())
             gameContainer.resume();
             this.unpauseUpdate();
            this.unpauseRender();
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
        selectedUnits.clear();
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
            if(!unitsNeededToBeUpdated.contains(unit))
                unitsNeededToBeUpdated.add(unit);
            Path path = pathFinder.findPath(unit,(int)unit.getTilePosition().x,(int)unit.getTilePosition().y,e.selectRect[0]/32,e.selectRect[1]/32);
            if(path!=null)
                unit.MoveUnitEvent(new UnitEvent(Unit.possibleActions.UNIT_MOVE.name(),0,unit,path));

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
            allUnitsByCoord[e.sourceUnit.getLastTilePosition().x][e.sourceUnit.getLastTilePosition().y].remove(e.sourceUnit);
            allUnitsByCoord[e.sourceUnit.getTilePosition().x][e.sourceUnit.getTilePosition().y].add(e.sourceUnit);

    }
}
