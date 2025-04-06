package Main;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import Components.*;
import Entities.*;
import Manager.SavingManager;

public class GameRun {

    public static UserInput userInput;

    public static ArrayList<Minion> allMinions = new ArrayList<>();

    static Starters starter;
    static Vector starterPos = new Vector();

    static boolean starterExisting = false;

    public static boolean InitRun() {

        starter = userInput.starter;
        if (starter != null) {
            starterExisting = true;
            starterPos.SetVector(starter.position);
        }

        if (starterExisting) {

            allMinions.add(new Minion(starter.direction, starterPos));

        } else {
            System.err.println("Starter not found.");
            return false;
        }

        try {
            SavingManager.SaveEverythingToFile(SavingManager.gameRunFileName);
            SavingManager.ChangeCurrentFile(SavingManager.gameRunFileName);
        } catch (IOException e) {
            System.err.println("Can't save and change to " + SavingManager.gameRunFileName);
            e.printStackTrace();
            return false;
        }
        
        return true;
    }

    public static void StopRun() throws FileNotFoundException {
        allMinions.clear();
        starter = null;
        starterPos = new Vector();
        starterExisting = false;
        SavingManager.ChangeCurrentFile(SavingManager.currentSaveName);
        SavingManager.LoadEverythingFromFile();
    }

    public static void RunTheGame() {

        ArrayList<Spawner> activatedSpawners = new ArrayList<>();
        ArrayList<Minion> minionsToDelete = new ArrayList<>();
        ArrayList<Pushers> activatedPushers = new ArrayList<>();
        ArrayList<Tower> occuredTowers = new ArrayList<>();

        for (Minion minion : allMinions) {
            minion.Move();

            if (minion.tmpReturnSpawner != null && !activatedSpawners.contains(minion.tmpReturnSpawner)) {
                activatedSpawners.add(minion.tmpReturnSpawner);
                minionsToDelete.add(minion);
            } else if (minion.tmpReturnBreaker != null) {
                minionsToDelete.add(minion);
            } else if (minion.tmpReturnPushers != null) {
                activatedPushers.add(minion.tmpReturnPushers);
                minionsToDelete.add(minion);
            } else if (minion.tmpReturnTower != null) {
                occuredTowers.add(minion.tmpReturnTower);
                minionsToDelete.add(minion);
            }

        }

        for (Spawner spawner : activatedSpawners) {
            spawner.Spawn();
        }
        for (Minion minion : minionsToDelete) {
            allMinions.remove(minion);
            minion = null;
        }
        for (Pushers pusher : activatedPushers) {
            pusher.Push();
        }
        for (Tower tower : occuredTowers) {
            tower.Hit();
        }

    }

}
