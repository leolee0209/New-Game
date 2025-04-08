package Components;

import Main.Vector;
import Manager.MapManager.Cell;

public class Arrows extends ComponentClass {

    public Vector direction = new Vector();

    public Arrows(String name) {
        this.name = name;
    }
    
    public Arrows(Vector position, String name) {
        Init(position, name);
    }

    @Override
    public void Init(Vector position, String name) {
        this.position.SetVector(position);
        this.name = name;
        switch (name) {
            case "upArrow":
                this.direction = DirectionToInt("up");
                break;
            case "downArrow":
                this.direction = DirectionToInt("down");
                break;
            case "leftArrow":
                this.direction = DirectionToInt("left");
                break;
            case "rightArrow":
                this.direction = DirectionToInt("right");
                break;
        }
    }

    public void Init(Cell cell) {
        this.position.SetVector(cell.absPos);
        this.name = cell.getName();
        switch (this.name) {
            case "upArrow":
                this.direction = DirectionToInt("up");
                break;
            case "downArrow":
                this.direction = DirectionToInt("down");
                break;
            case "leftArrow":
                this.direction = DirectionToInt("left");
                break;
            case "rightArrow":
                this.direction = DirectionToInt("right");
                break;
        }
    }


    @Override
    public Arrows CreateInstance(Vector position, String name) {

        Arrows arrow = new Arrows(position, name);

        return arrow;

    }
    
    

}
