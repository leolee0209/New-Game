package Entities;

import Main.Vector;
import Manager.MapManager;
import Manager.MapManager.Cell;

public class EntityClass {

    public String name;
    public Vector position = new Vector();
    public int health;

    public EntityClass() {
    }

    public void Init(Vector position, String string) {
    }

    public void Init(Cell cell) {
        
    }

    public static EntityClass GetEntities(String name) {

        switch (name) {
            case "tower":
                return new Tower(name);
        }

        return null;
    }
    
    public EntityClass CreateInstance(Vector position, String name) {

        return null;
    }

    public void Hit() {
        this.health--;
        if (this.health <= 0) {
            EntityClass.Destroy(this);
        }
    }

    public static void Destroy(EntityClass entity) {
        MapManager.GetCellFromAbsPos(entity.position, true).entity = null;
        entity = null;
    }
}
