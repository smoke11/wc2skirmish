package smoke11.wc2skirmish.xml;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import smoke11.DebugView;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;

/**
 * Created with IntelliJ IDEA.
 * User: nobody_traveler
 * Date: 11/03/13
 * Time: 09:42
 * To change this template use File | Settings | File Templates.
 */
public class XMLSettingsCreator {

    public static void main(String argv[]) {

        try {

            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

            Document doc = docBuilder.newDocument();
            Element rootElement = doc.createElement("MainSettings");
            doc.appendChild(rootElement);
            String[] names = new String[]{"main_folder","unit_tiles","terrain_tiles","sprites","maps"};
            String[] dirs = new String[]{argv[0], argv[0]+"unit_tiles.xml", argv[0]+"terrain_tiles.xml", argv[0]+"sprites\\", argv[0]+"test maps\\"};
            String[] names2 = new String[]{
                    "summertiles",  //terrain - sumer
                    "humanbuildingssummer",   //buildings
                    "orcbuildingssummer",      //buildings
                    //units
                    //land
                    "footman",
                    "grunt",
                    "peasant",
                    "peon",
                    "ballista",
                    "catapult",
                    "knight",
                    "ogre",
                    "archer",
                    "axethrower",
                    "mage",
                    "deathknight",
                    "knight",
                    "ogre",
                    "dwarves",
                    "goblins",
                    //water
                    "humanoiltanker",
                    "orcoiltanker",
                    "humantransport",
                    "orctransport",
                    "elvendestroyer",
                    "trolldestroyer",
                    "battleship",
                    "juggernaught",
                    "gnomishsubmarine",
                    "giantturtle",
                    //air
                    "gnomishflyingmachine",
                    "goblinzeppelin",
                    "gryphonrider",
                    "dragon",
                    //misc
                    "misc",
                    "misc",
                    "critters"};


            String[] dirs2 = new String[]{
                    dirs[3]+"summertiles.png",    //terrain - summer
                    dirs[3]+ "human/humanbuildingssummer.png", //buildings
                    dirs[3]+ "orc/orcbuildingssummer.png",     //buildings
                    //units
                    //land
                    dirs[3]+ "human/footman.png",
                    dirs[3]+ "orc/grunt.png",
                    dirs[3]+ "human/peasant.png",
                    dirs[3]+ "orc/peon.png",
                    dirs[3]+ "human/ballista.png",
                    dirs[3]+ "orc/catapult.png",
                    dirs[3]+ "human/knight.png",
                    dirs[3]+ "orc/ogre.png",
                    dirs[3]+ "human/archer.png",
                    dirs[3]+ "orc/axethrower.png",
                    dirs[3]+ "human/mage.png",
                    dirs[3]+ "orc/deathknight.png",
                    dirs[3]+ "human/knight.png",
                    dirs[3]+ "orc/ogre.png",
                    dirs[3]+ "human/dwarves.png",
                    dirs[3]+ "orc/goblins.png",
                    //water
                    dirs[3]+ "human/humanoiltanker.png",
                    dirs[3]+ "orc/orcoiltanker.png",
                    dirs[3]+ "human/humantransport.png",
                    dirs[3]+ "orc/orctransport.png",
                    dirs[3]+ "human/elvendestroyer.png",
                    dirs[3]+ "orc/trolldestroyer.png",
                    dirs[3]+ "human/battleship.png",
                    dirs[3]+ "orc/juggernaught.png",
                    dirs[3]+ "human/gnomishsubmarine.png",
                    dirs[3]+ "orc/giantturtle.png",
                    //air
                    dirs[3]+ "human/gnomishflyingmachine.png",
                    dirs[3]+ "orc/goblinzeppelin.png",
                    dirs[3]+ "human/gryphonrider.png",
                    dirs[3]+ "orc/dragon.png",
                    //misc
                    dirs[3]+ "misc.png",
                    dirs[3]+ "misc.png",
                    dirs[3]+ "critters.png"};
            String[] recogniseWith = new String[]{  //using it for telling SpritesheetParser to take specific sprites from specific sheets. i.e. "Human" means take all tiles which hase human in name for this spritesheet (for this it will be all human buildings
                    "",//terrain
                    "Human",  //buildings
                    "Orc",   //buildings
                    //units
                    //land
                    "Footman",
                    "Grunt",
                    "Peasant",
                    "Peon",
                    "Ballista",
                    "Catapult",
                    "Knight",
                    "Ogre",
                    "Archer",
                    "Axethrower",
                    "Mage",
                    "Death Knight",
                    "Paladin",
                    "Ogre-Mage",
                    "Dwarves",
                    "Goblin Sapper",
                    //water
                    "Human Oil Tanker",
                    "Orc Oil Tanker",
                    "Human Transport",
                    "Orc Transport",
                    "Elven Destroyer",
                    "Troll Destroyer",
                    "Battleship",
                    "Juggernaught",
                    "Gnomish Submarine",
                    "Giant Turtle",
                    //air
                    "Gnomish Flying Machine",
                    "Goblin Zepplin",
                    "Gryphon Rider",
                    "Dragon",
                    //misc
                    "Gold Mine",
                    "Oil Patch",
                    "Critter"
            };
            String[] ignoreIfHave = new String[]{ //sometimes there is a need to ignore some word in name. i.e. there is orc unit - Ogre and there is building Orc Ogre Mound. so there its needed to ignore word Orc for unit because it will choose building or vice versa
                    "",//terrain
                    // //if empty, ignore this
                    "",
                    "",
                    "",
                    "",
                    "",
                    "",
                    "",
                    "",
                    "Death", //Human unit Knight ignore Death Knight
                    "Orc",   // Orc unit Ogre ignore orc bulding
                    "",
                    "",
                    "Tower", //human unit mage ignore human building
                    "",
                    "",
                    "Orc",  // Orc unit Ogre ignore orc bulding
                    "",
                    "Alchemist", // orc unit goblin sapper ignore orc building
                    "",
                    "",
                    "",
                    "",
                    "",
                    "",
                    "",
                    "",
                    "",
                    "",
                    "",
                    "",
                    "",
                    "Roost", //orc unit dragon ignore orc building
                    "",
                    "Tanker;Well", //misc building oil patch ignore orc and human units
                    ""
            };
            Element tile;
            Attr attr;
            for (int i=0;i<names.length;i++)
            {
            tile = doc.createElement("Path");
            rootElement.appendChild(tile);

            attr = doc.createAttribute("Name");
            attr.setValue(names[i]);
            tile.setAttributeNode(attr);

            attr = doc.createAttribute("Dir");
            attr.setValue(dirs[i]);
            tile.setAttributeNode(attr);

            }
            for (int i=0;i<names2.length;i++)
            {
                tile = doc.createElement("ImagePath");
                rootElement.appendChild(tile);

                attr = doc.createAttribute("Name");
                attr.setValue(names2[i]);
                tile.setAttributeNode(attr);

                attr = doc.createAttribute("Dir");
                attr.setValue(dirs2[i]);
                tile.setAttributeNode(attr);

                attr = doc.createAttribute("RecogniseWith");
                attr.setValue(recogniseWith[i]);
                tile.setAttributeNode(attr);

                attr = doc.createAttribute("IgnoreIfHave");
                attr.setValue(ignoreIfHave[i]);
                tile.setAttributeNode(attr);

            }
            // write the content into xml file
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);

                File f=new File(argv[0]+"settings.xml");
            if(f.exists())
            {
                f.delete();
            }
            StreamResult result;

                result = new StreamResult(new File(argv[0]+"settings.xml"));

            // Output to console for testing
            // StreamResult result = new StreamResult(System.out);

            transformer.transform(source, result);

            DebugView.writeDebug(DebugView.DEBUGLVL_LESSINFO, XMLSettingsCreator.class.getSimpleName(), "File saved!");

        } catch (ParserConfigurationException pce) {
            pce.printStackTrace();
            //return -1;
        } catch (TransformerException tfe) {
            tfe.printStackTrace();
            //return -1;
        }
    }
}
