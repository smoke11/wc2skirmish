package smoke11.wc2skirmish;

import org.newdawn.slick.util.pathfinding.Path;

/**
 * Created with IntelliJ IDEA.
 * User: nao
 * Date: 25.04.13
 * Time: 09:05
 * To change this template use File | Settings | File Templates.
 */
public class SlickUtilities {
    public static String DestinationPathToString(Path path)
    {
        StringBuilder string = new StringBuilder();
        string.append("Path:");                 //TODO: make all buildings strings with StringBuilder
        Path.Step step;
        for (int i=0; i<path.getLength(); i++)
        {
            step=path.getStep(i);
            string.append(" ("+path.getX(i)+","+path.getY(i)+"),");
        }
        return string.toString();
    }
}
