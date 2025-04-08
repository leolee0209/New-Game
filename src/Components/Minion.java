package Components;

import Entities.*;
import Main.Vector;
import Manager.LoadingManager;
import Manager.MapManager;
import Manager.MapManager.Cell;

public class Minion {
    public Vector direction = new Vector();
    public Vector position = new Vector();

    public Spawner tmpReturnSpawner = null;
    public Breaker tmpReturnBreaker = null;
    public Pushers tmpReturnPushers = null;
    public Tower tmpReturnTower = null;

    public Minion(String direction, Vector position) {
        this.direction = ComponentClass.DirectionToInt(direction);
        this.position = position;
    }

    public Minion(Vector direction, Vector position) {
        this.direction = direction;
        this.position = position;
    }

    public void Move() {

        Cell currentCell = MapManager.GetCellFromAbsPos(position, true);

        ComponentClass currentComponent = currentCell.component;
        EntityClass currentEntity = currentCell.entity;

        tmpReturnSpawner = null;
        tmpReturnBreaker = null;
        tmpReturnPushers = null;

        if (currentComponent != null) {

            if (currentComponent instanceof Arrows) {
                this.direction = ((Arrows) currentComponent).direction;
            }
            if (currentComponent instanceof Spawner) {
                tmpReturnSpawner = (Spawner) currentComponent;
            }
            if (currentComponent instanceof Breaker) {
                tmpReturnBreaker = (Breaker) currentComponent;
            }
            if (currentComponent instanceof Pushers) {
                tmpReturnPushers = (Pushers) currentComponent;
            }
        } else if (currentEntity != null) {

            if (currentEntity instanceof Tower) {
                tmpReturnTower = (Tower) currentEntity;
            }
        }

        if (direction.x > 0 && position.x < LoadingManager.maxScreenCol - 1 ||
                direction.x < 0 && position.x > 0 ||
                direction.y > 0 && position.y < LoadingManager.maxScreenRow - 1 ||
                direction.y < 0 && position.y > 0) {

            position.x += direction.x;
            position.y += direction.y;
        }
    }

}
