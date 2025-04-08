package Manager;

public class GameStateManager {

    public enum GameState {
        placingComponents,
        placingEntities,
        gameRunning
    }

    public static GameState gameState = GameState.placingComponents;



}
