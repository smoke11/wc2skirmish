package smoke11.wc2skirmish;

import org.newdawn.slick.SpriteSheet;
import org.newdawn.slick.Image;
import smoke11.wc2utils.Tile;

import java.util.HashMap;


/**
 * Created with IntelliJ IDEA.
 * User: nao
 * Date: 09.03.13
 * Time: 12:18
 * To change this template use File | Settings | File Templates.
 */
public class SpritesheetParser {
    //slick images
    ////////////////
    //First two methods returning Array of Images, sorted by ids, designed for cutting terrain spriteshets
    //simple, if title[i1][i2] exists make sprite for tile, for id as index
    ///////////////
    public static Image[] cutSpriteSheet(SpriteSheet spritesheet,  Tile[][] tiles)         //http://www.slick2d.org/wiki/index.php/Performance - batching
    {
        Image[] sprites = new Image[tiles.length*tiles[0].length];
        int lastID=-1;
        for (int i1=0;i1<tiles.length;i1++)
        {
            for (int i2=0;i2<tiles[0].length;i2++)
            {
                if(tiles[i1][i2]!=null)
                {
                    if(lastID!=tiles[i1][i2].ID)
                    {
                        lastID=tiles[i1][i2].ID;
                        sprites[lastID]= spritesheet.getSubImage(tiles[i1][i2].OffsetX, tiles[i1][i2].OffsetY, tiles[i1][i2].SizeX, tiles[i1][i2].SizeY);


                    }
                }


            }

        }
        return sprites;
    }
    public static Image[] cutSpriteSheet(SpriteSheet spritesheet,  Tile[][][] tiles)
    {
        Image[] sprites = new Image[tiles.length*tiles[0].length*tiles[0][0].length];
        int lastID=-1;
        for (int i1=0;i1<tiles.length;i1++)
        {
            for (int i2=0;i2<tiles[0].length;i2++)
            {
                for (int i3=0;i3<tiles[0][0].length;i3++)
                {
                    if(tiles[i1][i2][i3]!=null)
                    {
                        if(lastID!=tiles[i1][i2][i3].ID)
                        {
                            lastID=tiles[i1][i2][i3].ID;
                            sprites[lastID]= spritesheet.getSubImage(tiles[i1][i2][i3].OffsetX, tiles[i1][i2][i3].OffsetY, tiles[i1][i2][i3].SizeX, tiles[i1][i2][i3].SizeY);

                        }
                    }
                }

            }

        }
        return sprites;
    }
    ////////////////////////////////
    //because i`m cutting only SpriteSheet at time (i.e. buildings or one unit with anim) at the time, there is need to return HashMap, as method to get image by id as index is no longer working
    //so for each tile (expressed by id) i`m getting image and put it in hashmap where key is id. and this hashmap with images put to hashmap where key is string with name of unit for which was spritesheet
    ///////////////////////////////
    public static HashMap<Integer,Image> cutSpriteSheet(SpriteSheet spritesheets, Tile[][] tiles, String ifThisContainsUseImage, String ifThisContainsIgnore)
    {
        HashMap<Integer,Image> images = new HashMap<Integer, Image>();
        int lastID=-1;
        for (int i1=0;i1<tiles.length;i1++)
        {
            for (int i2=0;i2<tiles[0].length;i2++)
            {
                if(tiles[i1][i2]!=null)
                {
                    if(lastID!=tiles[i1][i2].ID)
                    {
                        lastID=tiles[i1][i2].ID;
                            if(tiles[i1][i2].Name.contains(ifThisContainsUseImage))
                            {//using it for telling SpritesheetParser to take specific sprites from specific sheets. i.e. "Human" means take all tiles which hase human in name for this spritesheet (for this it will be all human buildings) Orc in ignore will mean that ignore all with Orc in name
                                if(ifThisContainsIgnore=="")
                                    images.put(lastID,spritesheets.getSubImage(tiles[i1][i2].OffsetX, tiles[i1][i2].OffsetY, tiles[i1][i2].SizeX, tiles[i1][i2].SizeY));
                                else //if there is more than one word to ignore
                                {
                                    int contains=0;   //TODO: Check if i need to know about how many ignored words are here
                                    String[] split = ifThisContainsIgnore.split(";");
                                    for (int z=0;z<split.length;z++)
                                    {
                                        if(tiles[i1][i2].Name.contains(split[z]))
                                            contains++;
                                    }
                                    if(contains==0)
                                        images.put(lastID,spritesheets.getSubImage(tiles[i1][i2].OffsetX, tiles[i1][i2].OffsetY, tiles[i1][i2].SizeX, tiles[i1][i2].SizeY));
                                }
                            }
                        }
                    }

                }
            }


        return images;
    }




}
