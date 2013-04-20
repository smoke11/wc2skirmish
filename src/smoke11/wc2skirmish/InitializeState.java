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
    public static Tile[][] mapTiles;
    public static HashMap<String, SpriteSheet> spriteSheets = new HashMap<String, SpriteSheet>();
    public static HashMap<String, Image[]> imageSpriteTiles = new HashMap<String, Image[]>();
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
        if(!new File(mainDir+"settings.xml").exists())                 //if there is no xml we should make it
            XMLSettingsCreator.main(new String[]{mainDir});
        if(firstTimeOpen)
            readSettings();
        readImages();
        loadMap();
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
            unittiles = new File(XMLSettingsReader.Paths.get("unit_tiles"));
            terraintiles = new File(XMLSettingsReader.Paths.get("terrain_tiles"));
            if(unittiles.exists())
                XML_Units_SettingsCreatorIter.main(new String[]{XMLSettingsReader.Paths.get("unit_tiles")});
            if(terraintiles.exists())
                XML_Tiles_SettingsCreatorIter.main(new String[]{XMLSettingsReader.Paths.get("terrain_tiles")});
            XMLPudSettingsReader.main(new String[]{XMLSettingsReader.Paths.get("terrain_tiles"), XMLSettingsReader.Paths.get("unit_tiles")});
            firstTimeOpen=false;
        }

    }
    private void readImages()
    {
        String name;
        try {
            name = "summertiles";
            DebugView.writeDebug(DebugView.DEBUGLVL_LESSINFO,"InitializeState","Reading spritesheet: "+name);
            spriteSheets.put(name, new SpriteSheet(XMLSettingsReader.ImagePaths.get(name), 32, 32, 1));
            name = "humanbuildingssummer";
            DebugView.writeDebug(DebugView.DEBUGLVL_LESSINFO,"InitializeState","Reading spritesheet: "+name);
            spriteSheets.put(name, new SpriteSheet(XMLSettingsReader.ImagePaths.get(name), 32, 32, 1));
            name = "orcbuildingssummer";
            DebugView.writeDebug(DebugView.DEBUGLVL_LESSINFO,"InitializeState","Reading spritesheet: "+name);
            spriteSheets.put(name, new SpriteSheet(XMLSettingsReader.ImagePaths.get(name), 32, 32, 1));
        } catch (SlickException e) {
            DebugView.writeDebug(DebugView.DEBUGLVL_ERRORS,"InitializeState","While reading sprites: ");
            DebugView.writeDebug(DebugView.DEBUGLVL_ERRORS,"InitializeState",e.getMessage());
            DebugView.writeDebug(DebugView.DEBUGLVL_ERRORS,"InitializeState",e.getStackTrace().toString());

        }
            name = "summertiles";
            imageSpriteTiles.put(name, SpritesheetParser.cutSpriteSheet(spriteSheets.get(name), XMLPudSettingsReader.SortedTerrainTiles));
            name = "humanbuildingssummer";
            imageSpriteTiles.put(name, SpritesheetParser.cutSpriteSheet(spriteSheets.get(name), XMLPudSettingsReader.UnitTiles));  //TODO: make proper methods from spritesheetparser from pudutils
            name = "orcbuildingssummer";
            imageSpriteTiles.put(name, SpritesheetParser.cutSpriteSheet(spriteSheets.get(name), XMLPudSettingsReader.UnitTiles));   //TODO: make proper methods from spritesheetparser from pudutils
    }
    private void loadMap()
    {
        FileOpenPanel f = new FileOpenPanel();

        if(firstTimeOpen)
            readSettings();

        f.openMapFile(XMLSettingsReader.Paths.get("main_folder"));
        if(f.OpenedMapFile !=null)
        {

            PudParser p;
            p = new PudParser();
            p.getMapDataFromFile(f.OpenedMapFile);
            p.prepareTiles(XMLPudSettingsReader.SortedTerrainTiles, XMLPudSettingsReader.UnitTiles);
            mapTiles =p.mapTiles;


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
