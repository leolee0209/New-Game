package Components;

import Main.Vector;
import Manager.MapManager.Cell;

public class ComponentClass {
    public Vector position = new Vector();
    public String name;

    public ComponentClass() {
    }

    public static ComponentClass GetComponents(String name) {
        switch (name) {
            case "upArrow":
            case "downArrow":
            case "leftArrow":
            case "rightArrow":
                return new Arrows(name);
            case "spawner":
                return new Spawner();
            case "upStarter":
            case "downStarter":
            case "leftStarter":
            case "rightStarter":
                return new Starters(name);
            case "upPusher":
            case "downPusher":
            case "leftPusher":
            case "rightPusher":
                return new Pushers(name);
            case "breaker":
                return new Breaker(name);
            default:
                System.err.println("TileClass.Init() failed to find tile name : " + name);
                break;
        }
        return null;
    }

    public ComponentClass CreateInstance(Vector position, String name) {

        return null;
    }

    public void Init(Vector position, String name) {

    }

    public void Init(Cell cell) {
    
    }

    public static Vector DirectionToInt(String direction) {

        Vector dirInt = new Vector();

        switch (direction) {
            case "up":
                dirInt.SetVector(0, -1);
                break;
            case "down":
            dirInt.SetVector(0, 1);
                break;
            case "left":
            dirInt.SetVector(-1, 0);
                break;
            case "right":
            dirInt.SetVector(1, 0);
                break;
        }
        return dirInt;

    }
   
}
