package smoke11.wc2skirmish;

import org.newdawn.slick.SpriteSheet;
import org.newdawn.slick.Image;
import smoke11.wc2utils.Tile;




/**
 * Created with IntelliJ IDEA.
 * User: nao
 * Date: 09.03.13
 * Time: 12:18
 * To change this template use File | Settings | File Templates.
 */
public class SpritesheetParser {
    //slick images
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






}
