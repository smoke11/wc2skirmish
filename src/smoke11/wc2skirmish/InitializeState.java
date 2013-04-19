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
    private HashMap<String, SpriteSheet> SpriteSheets = new HashMap<String, SpriteSheet>();
    private static SpriteSheet terrainSpriteSheet;
    private static Image[] terrainSpriteTiles;
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
        File XMLfile = new File(mainDir+"settings.xml");
        if(XMLfile.exists())                 //if there is no xml we should make it
        {
            XMLSettingsCreator.main(new String[]{mainDir});
        }

        if(firstTimeOpen)
            readSettings();
        readImages();
        loadMap();

        if(mapTiles==null)
            DebugView.writeDebug(DebugView.DEBUGLVL_ERRORS,"InitializeState", "Something went wrong, important variables are null!");

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
        try {
            for (String name : XMLSettingsReader.ImagePaths.keySet())
            {
                DebugView.writeDebug(DebugView.DEBUGLVL_LESSINFO,"InitializeState","reading "+name);
                SpriteSheets.put(name, new SpriteSheet(XMLSettingsReader.ImagePaths.get("summertiles"),32,32,1));
            }
            //DebugView.writeDebug(DebugView.DEBUGLVL_LESSINFO,"InitializeState","reading summertiles.png");
            //terrainSpriteSheet = new SpriteSheet(XMLSettingsReader.Paths.get("sprites") + "summertiles.png",32,32,1);  //TODO: Make this huge list as hashmap to iterate on it
        } catch (SlickException e) {
            DebugView.writeDebug(DebugView.DEBUGLVL_ERRORS,"InitializeState","While reading sprites: ");
            DebugView.writeDebug(DebugView.DEBUGLVL_ERRORS,"InitializeState",e.getMessage());
            DebugView.writeDebug(DebugView.DEBUGLVL_ERRORS,"InitializeState",e.getStackTrace().toString());

        }
            terrainSpriteTiles=SpritesheetParser.cutSpriteSheet(SpriteSheets.get("summertiles"),XMLPudSettingsReader.SortedTerrainTiles);
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


    @Override
    public void render(GameContainer gameContainer, StateBasedGame stateBasedGame, Graphics graphics) throws SlickException {
           SpriteSheets.get("summertiles").startUse();
            for (int x=0; x<mapTiles.length;x++)
                for (int y=0; y<mapTiles[0].length;y++)
                      terrainSpriteTiles[mapTiles[x][y].ID].drawEmbedded(x*32,y*32);
                    //graphics.drawImage(terrainSpriteSheet.getSubImage(mapTiles[x][y].OffsetX,mapTiles[x][y].OffsetY,32,32),x*32,y*32);
            SpriteSheets.get("summertiles").endUse();

    }

    @Override
    public void update(GameContainer gameContainer, StateBasedGame stateBasedGame, int i) throws SlickException {
        //To change body of implemented methods use File | Settings | File Templates.
    }

}
