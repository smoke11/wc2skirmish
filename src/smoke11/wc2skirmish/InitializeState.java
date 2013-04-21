package smoke11.wc2skirmish;

import org.newdawn.slick.*;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import smoke11.DebugView;
import smoke11.wc2skirmish.xml.XMLSettingsCreator;
import smoke11.wc2skirmish.xml.XMLSettingsReader;
import smoke11.wc2utils.FileOpenPanel;
import smoke11.wc2utils.PudParser;
import smoke11.wc2utils.Tile;
import smoke11.wc2utils.xml.XMLPudSettingsReader;
import smoke11.wc2utils.xml.XML_Tiles_SettingsCreatorIter;
import smoke11.wc2utils.xml.XML_Units_SettingsCreatorIter;

import java.io.File;
import java.util.HashMap;


public class InitializeState extends BasicGameState{
    /** The ID given to this state */
    private static final int id = 1;
    private static String mainDir="D:\\datafiles\\"; //use this to change path to files of this program
    private static boolean firstTimeOpen=true;
    private static Tile[][] mapTiles;
    public static Tile[][] getUnitTiles() {
        return unitTiles;
    }

    private static Tile[][] unitTiles;
    private static HashMap<String, SpriteSheet> terrainSpriteSheets = new HashMap<String, SpriteSheet>();
    private static HashMap<String, Image[]> terrainSpriteTiles = new HashMap<String, Image[]>();

    public static HashMap<String, SpriteSheet> getUnitSpriteSheets() {
        return unitSpriteSheets;
    }

    public static HashMap<String, HashMap<Integer,Image>> getUnitSpriteTiles() {
        return unitSpriteTiles;
    }

    private static HashMap<String, SpriteSheet> unitSpriteSheets = new HashMap<String, SpriteSheet>();
    private static HashMap<String, HashMap<Integer,Image>> unitSpriteTiles = new HashMap<String, HashMap<Integer,Image>>();
    public static HashMap<String, SpriteSheet> getTerrainSpriteSheets() {
        return terrainSpriteSheets;
    }

    public static HashMap<String, Image[]> getTerrainSpriteTiles() {
        return terrainSpriteTiles;
    }


    public static Tile[][] getMapTiles() {
        return mapTiles;
    }

    @Override
    public int getID() {
        return id;
}

    @Override
    public void init(GameContainer gameContainer, StateBasedGame stateBasedGame) throws SlickException {
        if(mainDir=="")  //if there is no path to xmls, we need to find it
        {
            FileOpenPanel f = new FileOpenPanel();
            mainDir = f.getPathToJar();
        }
        if(new File(mainDir+"settings.xml").exists())                 //if there is no xml we should make it
            XMLSettingsCreator.main(new String[]{mainDir});
        if(firstTimeOpen)
            readSettings();
        readImages();
        readMapData();
        startGame(stateBasedGame);


    }

    private void readSettings()
    {
        FileOpenPanel f = new FileOpenPanel();
        File XMLfile = new File(mainDir+"settings.xml");
        if(!XMLfile.exists())
        {
            f.openXMLFile(mainDir);
            XMLfile = f.OpenedXMLFile;
        }
        if(XMLfile !=null)
        {
            File terraintiles, unittiles;
            XMLSettingsReader.main(new String[]{XMLfile.getPath()});
            unittiles = new File(XMLSettingsReader.paths.get("unit_tiles"));
            terraintiles = new File(XMLSettingsReader.paths.get("terrain_tiles"));
            if(unittiles.exists())
                XML_Units_SettingsCreatorIter.main(new String[]{XMLSettingsReader.paths.get("unit_tiles")});
            if(terraintiles.exists())
                XML_Tiles_SettingsCreatorIter.main(new String[]{XMLSettingsReader.paths.get("terrain_tiles")});
            XMLPudSettingsReader.main(new String[]{XMLSettingsReader.paths.get("terrain_tiles"), XMLSettingsReader.paths.get("unit_tiles")});
            firstTimeOpen=false;
        }

    }
    ////////////////////
    //First, we read and cut terrain sprites. It`s simple - (we kill the Batman) we pass spritesheet and list of tiles to spritesheetparser
    //For units, we must pass spritesheet for unit, unit data (for all units) and string info about unit (for recognise and for ignore other units)
    ////////////////////
    private void readImages()
    {
        String terrainname;
        try {
            terrainname = "summertiles";
            DebugView.writeDebug(DebugView.DEBUGLVL_LESSINFO,"InitializeState","Reading spritesheet: "+terrainname);
            terrainSpriteSheets.put(terrainname, new SpriteSheet(XMLSettingsReader.imagePaths.get(terrainname), 32, 32, 1));
            for(String name : XMLSettingsReader.imagePaths.keySet())
            {
                if(terrainname.contentEquals(name))
                    continue;
                DebugView.writeDebug(DebugView.DEBUGLVL_LESSINFO,"InitializeState","Reading spritesheet: "+name);
                unitSpriteSheets.put(name, new SpriteSheet(XMLSettingsReader.imagePaths.get(name), 32, 32, 1));
            }

        } catch (SlickException e) {
            DebugView.writeDebug(DebugView.DEBUGLVL_ERRORS,"InitializeState","While reading sprites: ");
            DebugView.writeDebug(DebugView.DEBUGLVL_ERRORS,"InitializeState",e.getMessage());
            DebugView.writeDebug(DebugView.DEBUGLVL_ERRORS,"InitializeState",e.getStackTrace().toString());

        }
            terrainname = "summertiles";
            terrainSpriteTiles.put(terrainname, SpritesheetParser.cutSpriteSheet(terrainSpriteSheets.get(terrainname), XMLPudSettingsReader.SortedTerrainTiles));
            for(String name : unitSpriteSheets.keySet())
                unitSpriteTiles.put(name, SpritesheetParser.cutSpriteSheet(unitSpriteSheets.get(name), XMLPudSettingsReader.UnitTiles, XMLSettingsReader.recogniseWith.get(name), XMLSettingsReader.ignoreIfHave.get(name)));
    }
    private void readMapData()
    {
        FileOpenPanel f = new FileOpenPanel();

        if(firstTimeOpen)
            readSettings();

        f.openMapFile(XMLSettingsReader.paths.get("main_folder"));
        if(f.OpenedMapFile !=null)
        {

            PudParser p;
            p = new PudParser();
            p.getMapDataFromFile(f.OpenedMapFile);
            p.prepareTiles(XMLPudSettingsReader.SortedTerrainTiles, XMLPudSettingsReader.UnitTiles);
            mapTiles =p.mapTiles;
            unitTiles=p.unitTiles;
            //TODO: need to get faction from map data for units and buildings
        }
    }
    private void startGame(StateBasedGame stateBasedGame)
    {
        DebugView.writeDebug(DebugView.DEBUGLVL_LESSINFO,"InitializeState","Moving to state: GameState");
        stateBasedGame.enterState(Main.States.GAME.index());

    }

    @Override
    public void render(GameContainer gameContainer, StateBasedGame stateBasedGame, Graphics graphics) throws SlickException {

    }

    @Override
    public void update(GameContainer gameContainer, StateBasedGame stateBasedGame, int i) throws SlickException {
        //To change body of implemented methods use File | Settings | File Templates.
    }

}
