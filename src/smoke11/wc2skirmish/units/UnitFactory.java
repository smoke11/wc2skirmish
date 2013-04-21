package smoke11.wc2skirmish.units;

import org.newdawn.slick.geom.Vector2f;

import java.util.HashMap;

/**
 * Created with IntelliJ IDEA.
 * User: nao
 * Date: 21.04.13
 * Time: 18:44
 * To change this template use File | Settings | File Templates.
 */
public class UnitFactory
{

    private static boolean initialized = false;
    public static Unit createUnit(String nameOfUnit, int id, int faction, Vector2f position)
    {
        if(nameOfUnit=="Peon")
            return new Peon(id, faction, position);
        return null;
    }

}
//TODO: move data to xml or lua
class Peon extends Unit {
    public static final String PUDID="03";
    public Peon(int id, int faction, Vector2f position)
    {
        super();
        this.nameOfUnit ="Peon";
        this.health = 30;
        this.mana = 0;
        this.level = 1;
        this.armor=1;
        this.damage = new Vector2f(1,5);
        this.range = 1;
        this.sight = 4;
        this.speed = 10;
        this.type = Types.MELEE;
        this.race = "Orc";
        this.id = id;
        this.faction = faction;
        this.position = position;

    }

}