package smoke11.wc2skirmish.units;

import org.newdawn.slick.geom.Vector2f;

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
    private static int id=0; //to access this, use getID because its autoicremented
    private static int getId()
    { id++; return id-1;}
    public static Unit createUnit(String nameOfUnit, int faction, Vector2f position)
    {
        if(nameOfUnit.equalsIgnoreCase("Peon"))
            return new Peon(getId(), faction, position);
        if(nameOfUnit.equalsIgnoreCase("Peasant"))
            return new Peasant(getId(), faction, position);
        return null;
    }

}
//TODO: move data to xml or lua
class Peon extends Unit {

    public Peon(int id, int faction, Vector2f position)
    {
        super();
        this.PudID ="03";
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
class Peasant extends Unit {

    public Peasant(int id, int faction, Vector2f position)
    {
        super();
        this.PudID ="02";
        this.nameOfUnit ="Peasant";
        this.health = 30;
        this.mana = 0;
        this.level = 1;
        this.armor=1;
        this.damage = new Vector2f(1,5);
        this.range = 1;
        this.sight = 4;
        this.speed = 10;
        this.type = Types.MELEE;
        this.race = "Human";
        this.id = id;
        this.faction = faction;
        this.position = position;

    }

}