package Components;

import java.util.ArrayList;

import Main.GameRun;
import Main.Vector;
import Manager.MapManager;
import Manager.MapManager.Cell;

public class Spawner extends ComponentClass {

    public static int spawnSize = 3;

    Vector direction;

    private ArrayList<Vector> spawnPos = new ArrayList<>();
    ArrayList<Vector> spawnDirection = new ArrayList<>();

    public Spawner() {
        this.name = "spawner";
    }

    public Spawner(Vector position) {
        Init(position, "");
    }

    @Override
    public void Init(Vector position, String name) {
        this.position.SetVector(position);
        this.name = "spawner";
    }

    public void Init(Cell cell) {
        this.position.SetVector(cell.absPos);
        this.name = "spawner";
    }

    @Override
    public Spawner CreateInstance(Vector position, String name) {

        Spawner spawner = new Spawner(position);

        return spawner;
    }

    public void CheckSpawnPosAndDirection() {

        Cell[][] nearbyComponents = MapManager.GetArrayOfCells(this.position.Add(Vector.negone),
                Vector.one.Multiply(spawnSize));

        spawnPos.clear();
        spawnDirection.clear();

        for (int i = 0; i < spawnSize; i++) {
            for (int j = 0; j < spawnSize; j++) {
                if (nearbyComponents[i][j].component instanceof Arrows) {
                    AddSpawnPosAndDirectionToArrayList((Arrows) nearbyComponents[i][j].component,
                            nearbyComponents[i][j].component.position);
                }
            }
        }

    }

    public void AddSpawnPosAndDirectionToArrayList(Arrows arrow, Vector position) {
        spawnPos.add(new Vector(position));
        spawnDirection.add(new Vector(arrow.direction));
    }

    public void Spawn() {
        CheckSpawnPosAndDirection();
        for (int i = 0; i < spawnPos.size(); i++) {
            Minion minion = new Minion(spawnDirection.get(i), spawnPos.get(i));
            GameRun.allMinions.add(minion);
        }
    }

}
