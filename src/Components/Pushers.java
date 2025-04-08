package Components;

import java.util.ArrayList;

import Main.Vector;
import Manager.MapManager;
import Manager.MapManager.Cell;

public class Pushers extends ComponentClass {

    public Vector direction = new Vector();

    public Pushers(String name) {
        this.name = name;
    }

    public Pushers(Vector position, String name) {
        Init(position, name);
    }

    @Override
    public void Init(Vector position, String name) {
        this.position.SetVector(position);
        this.name = name;
        switch (name) {
            case "upPusher":
                this.direction = DirectionToInt("up");
                break;
            case "downPusher":
                this.direction = DirectionToInt("down");
                break;
            case "leftPusher":
                this.direction = DirectionToInt("left");
                break;
            case "rightPusher":
                this.direction = DirectionToInt("right");
                break;
        }
    }

    public void Init(Cell cell) {
        this.position.SetVector(cell.absPos);
        this.name = cell.getName();

        switch (this.name) {
            case "upPusher":
                this.direction = DirectionToInt("up");
                break;
            case "downPusher":
                this.direction = DirectionToInt("down");
                break;
            case "leftPusher":
                this.direction = DirectionToInt("left");
                break;
            case "rightPusher":
                this.direction = DirectionToInt("right");
                break;
        }
    }

    @Override
    public Pushers CreateInstance(Vector position, String name) {

        Pushers pusher = new Pushers(position, name);

        return pusher;

    }

    public void Push() {

        ArrayList<ComponentClass> tileToPush = new ArrayList<>();

        int i = 1;
        while (true) {

            Vector pushTarget = new Vector(position.Add(direction.Multiply(i)));

            if (MapManager.GetCellFromAbsPos(pushTarget, true).component != null) {
                tileToPush.add(MapManager.GetCellFromAbsPos(pushTarget, true).component);
            } else {
                break;
            }

            i++;
        }

        for (int j = tileToPush.size() - 1; j >= 0; j--) {

            ComponentClass checkingComponent = tileToPush.get(j);

            MapManager.GetCellFromAbsPos(checkingComponent.position.Add(direction), true).component = checkingComponent;
            MapManager.GetCellFromAbsPos(checkingComponent.position, true).component = null;

            checkingComponent.position.SetVector(checkingComponent.position.Add(direction));

        }


        if (MapManager.GetCellFromAbsPos(position.Add(direction), true).component == null) {
            MapManager.GetCellFromAbsPos(position.Add(direction), true).component = this;
            MapManager.GetCellFromAbsPos(this.position, true).component = null;

            position.SetVector(position.Add(direction));
        }


    }

}
