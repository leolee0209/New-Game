package Components;

import Main.Vector;
import Manager.MapManager.Cell;

public class Starters extends ComponentClass {

    public Vector direction = new Vector();
    
    public Starters(String name) {
        this.name = name;
    }
    
    public Starters(Vector position, String name) {
        Init(position, name);
    }

    @Override
    public void Init(Vector position, String name) {
        this.position.SetVector(position);
        this.name = name;
        switch (name) {
            case "upStarter":
                this.direction = DirectionToInt("up");
                break;
            case "downStarter":
                this.direction = DirectionToInt("down");
                break;
            case "leftStarter":
                this.direction = DirectionToInt("left");
                break;
            case "rightStarter":
                this.direction = DirectionToInt("right");
                break;
        }
    }

    public void Init(Cell cell) {
        this.position.SetVector(cell.absPos);
        this.name = cell.getName();
        switch (this.name) {
            case "upStarter":
                this.direction = DirectionToInt("up");
                break;
            case "downStarter":
                this.direction = DirectionToInt("down");
                break;
            case "leftStarter":
                this.direction = DirectionToInt("left");
                break;
            case "rightStarter":
                this.direction = DirectionToInt("right");
                break;
        }
    }


    @Override
    public Starters CreateInstance(Vector position, String name) {
        
        Starters starter = new Starters(position, name);

        return starter;

    }

}
