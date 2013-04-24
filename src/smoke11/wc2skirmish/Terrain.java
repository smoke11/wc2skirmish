package smoke11.wc2skirmish;

import org.newdawn.slick.util.pathfinding.PathFindingContext;
import org.newdawn.slick.util.pathfinding.TileBasedMap;
import smoke11.wc2skirmish.units.Unit;
import smoke11.wc2utils.Tile;
import smoke11.wc2utils.Vector2;

/**
 * Created with IntelliJ IDEA.
 * User: nao
 * Date: 23.04.13
 * Time: 15:34
 * To change this template use File | Settings | File Templates.
 */
public class Terrain implements TileBasedMap {
    private Tile[][] mapTiles;
    private Vector2 mapBounds = new Vector2(0,0);
    private static String[] land_blocking_Elements = new String[] { "water","forest","wall","mountains"};
    private static String[] water_blocking_Elements = new String[]{ "coast", "ground", "forest", "mountains","wall"};


    public Terrain(Tile[][] terrainTiles)
    {
        mapTiles=terrainTiles;
        mapBounds = new Vector2(mapTiles.length*32,mapTiles[0].length*32);
    }
    public Tile[][] getMapTiles() {
        return mapTiles;
    }

    public Vector2 getMapBounds() {
        return mapBounds;
    }

    @Override
    public int getWidthInTiles() {
        return mapTiles.length;
    }

    @Override
    public int getHeightInTiles() {
        return mapTiles[0].length;
    }

    @Override
    public void pathFinderVisited(int arg0, int arg1) {

    }

    @Override
    public boolean blocked(PathFindingContext pathFindingContext, int x, int y) {
        Unit.Types moverType = ((Unit)pathFindingContext.getMover()).getType();
        if(moverType== Unit.Types.FLYING)
            return true;
        if(moverType==Unit.Types.MELEE||moverType== Unit.Types.RANGED)
        {
            for (String blocking : land_blocking_Elements)
                if(mapTiles[x][y].Name.contains(blocking))
                    return true;
            return false;
        }
        if(moverType== Unit.Types.SHIP)
        {
            for (String blocking : water_blocking_Elements)
                if(mapTiles[x][y].Name.contains(blocking))
                    return true;
            return false;
        }
        return false;
    }

    @Override
    public float getCost(PathFindingContext pathFindingContext, int x, int y) {
        return 1.0f;
    }


}
