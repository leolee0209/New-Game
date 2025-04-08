package Entities;

import Main.Vector;
import Manager.LoadingManager;
import Manager.MapManager.Cell;

public class Tower extends EntityClass {

    public Tower(String name) {
        this.name = name;
    }
    
    public Tower(Vector position, String name) {
        Init(position, name);
    }

    public void Init(Vector position, String name) {

        this.position.SetVector(position);

        this.name = name;
        this.health = LoadingManager.towerHealth;
    }

    public void Init(Cell cell) {
        this.position.SetVector(cell.absPos);
        this.name = cell.getName();
        this.health = LoadingManager.towerHealth;
    }

    @Override
    public EntityClass CreateInstance(Vector position, String name) {

        return new Tower(position, name);
    }

}
