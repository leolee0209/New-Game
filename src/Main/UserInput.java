package Main;

import java.io.IOException;

import Components.*;
import Entities.EntityClass;
import Main.UserInterface.MenuState;
import Manager.*;
import Manager.GameStateManager.GameState;
import Manager.MapManager.Cell;

public class UserInput extends KeyHandler {

    ComponentClass currentComponent;

    EntityClass currentEntity;

    boolean lastHold = false;

    public Starters starter = null;

    int currentMouseX, currentMouseY;

    public UserInput(GamePanel gamePanel) {
        // get the gamePanel instance
        this.gamePanel = gamePanel;

    }

    @Override
    public void ProcessKeyPressedEvent() {
        PlaceAndDeleteComponentsOrEntities();

        if (GameStateManager.gameState == GameState.placingComponents) {
            ChangeComponentInHand();
        } else if (GameStateManager.gameState == GameState.placingEntities) {
            ChangeEntityInHand();
        }
    }

    public void PlaceAndDeleteComponentsOrEntities() {
        // place
        if (spacePressed) {

            Place();
        }
        // delete
        else if (deletePressed) {

            Delete();
        }
    }

    public void ChangeComponentInHand() {
        // set the initial tile that you're holding
        if (currentComponent == null) {
            currentComponent = ComponentClass.GetComponents("spawner");
        }

        // change with keyboard
        else {

            int i = 0;
            for (String name : LoadingManager.componentNames) {
                if (i < numberPressed.length) {
                    if (numberPressed[i] && currentComponent.name != name) {
                        currentComponent = ComponentClass.GetComponents(name);
                    }
                    i++;
                }
            }
        }
    }

    public void ChangeEntityInHand() {
        // set the initial tile that you're holding
        if (currentEntity == null) {
            currentEntity = EntityClass.GetEntities("tower");
        }

        // change with keyboard
        else {

            int i = 0;
            for (String name : LoadingManager.entityNames) {
                if (i < numberPressed.length) {
                    if (numberPressed[i] && currentEntity.name != name) {
                        currentEntity = EntityClass.GetEntities(name);
                    }
                    i++;
                }
            }
        }
    }

    public void ClickOnMenu() throws IOException {

        if (userInterface.showMenuUI) {
            switch (userInterface.menuState) {
                case Main:
                    for (Button button : userInterface.menuButtons) {
                        if (CheckIfPressOnButton(button)) {

                            switch (button.content) {
                                case "Save":
                                    SavingManager.RefreshSaveFiles();
                                    userInterface.menuState = MenuState.Save;
                                    break;
                                case "Load":
                                    SavingManager.RefreshSaveFiles();
                                    userInterface.menuState = MenuState.Load;
                                    break;
                                case "Place Components":
                                    GameStateManager.gameState = GameState.placingComponents;
                                    break;
                                case "Place Entities":
                                    GameStateManager.gameState = GameState.placingEntities;
                                    break;
                                case "Settings":
                                    userInterface.menuState = MenuState.Settings;
                                    break;

                            }
                            break;
                        }
                    }
                    break;
                case Save:
                    for (Button button : userInterface.menuButtons) {

                        if (CheckIfPressOnButton(button)) {

                            if (button.content.equals("new Save File")) {
                                SavingManager.CreateNewSaveFile();
                            } else {

                                SavingManager.SaveEverythingToFile(button.content);
                            }
                            break;
                        }
                    }

                    break;
                case Load:
                    for (Button button : userInterface.menuButtons) {

                        if (CheckIfPressOnButton(button)) {

                            SavingManager.SaveEverythingToFile(null);
                            SavingManager.ChangeCurrentFile(button.content);
                            SavingManager.LoadEverythingFromFile();
                            break;
                        }
                    }
                    break;
                case Settings:
                    for (Button button : userInterface.menuButtons) {

                        if (CheckIfPressOnButton(button)) {
                            if (button.buttonType == Button.Type.Plus) {
                                if (button.linkedContent == "originalTileSize") {
                                    LoadingManager.ChangeMapOriginalTileSize(1, gamePanel);
                                } else if (button.linkedContent == "scale") {
                                    LoadingManager.ChangeMapScale(1, gamePanel);
                                }
                            }
                            if (button.buttonType == Button.Type.Minus) {
                                if (button.linkedContent == "originalTileSize") {
                                    LoadingManager.ChangeMapOriginalTileSize(-1, gamePanel);
                                } else if (button.linkedContent == "scale") {
                                    LoadingManager.ChangeMapScale(-1, gamePanel);
                                }
                            }
                        }

                    }
                    break;
            }

        }
    }

    @Override
    public void ProcessLeftMouseClickEvent() throws IOException {

        switch (userInterface.CheckIfClickingOnUI()) {

            case "menu":
                ClickOnMenu();
                cursorDragSelectMode = false;
                break;
            case "tileSelection":
                // loop through all tilesNames
                for (String name : LoadingManager.componentNames) {
                    // check if click on the image
                    if (mousePressedScreenPos.x >= LoadingManager.tileSelectionPos.get(name).x &&
                            mousePressedScreenPos.x <= LoadingManager.tileSelectionPos.get(name).x
                                    + LoadingManager.UISelectionImageSize
                            &&
                            mousePressedScreenPos.y >= LoadingManager.tileSelectionPos.get(name).y &&
                            mousePressedScreenPos.y <= LoadingManager.tileSelectionPos.get(name).y
                                    + LoadingManager.UISelectionImageSize) {

                        if (currentComponent == null) {
                            currentComponent = ComponentClass.GetComponents(name);
                        } else if (currentComponent.name != name) {
                            currentComponent = ComponentClass.GetComponents(name);
                        }
                        break;
                    }
                }
                cursorDragSelectMode = false;
                break;
            default:
                int mouseTilePosX = mousePressedScreenPos.x / LoadingManager.tileSize;
                int mouseTilePosY = mousePressedScreenPos.y / LoadingManager.tileSize;

                cursorPos.x = mouseTilePosX;
                cursorPos.y = mouseTilePosY;

                break;

        }
    }

    @Override
    public void ProcessRightMouseClickEvent() {

        if (userInterface.CheckIfClickingOnUI() == "") {

            int mouseTilePosX = mousePressedScreenPos.x / LoadingManager.tileSize;
            int mouseTilePosY = mousePressedScreenPos.y / LoadingManager.tileSize;

            cursorPos.x = mouseTilePosX;
            cursorPos.y = mouseTilePosY;

            Place();

        }
    }

    public void Place() {
        Vector targetAbsPos = cursorPos.Add(MapManager.topLeftCellPosition);

        Cell targetCell = MapManager.GetCellFromAbsPos(targetAbsPos, true);

        // if the selecting position is null and current holding tile is not null and no
        // entity is there
        if (targetCell.isEmpty() && currentComponent != null) {

            // if placingComponents
            if (GameStateManager.gameState == GameState.placingComponents) {
                // if holding Starters and there's already a starter then return
                if (starter != null && currentComponent instanceof Starters) {
                    return;
                }

                // place the tile
                ComponentClass componentPlacing = currentComponent.CreateInstance(targetCell.absPos,
                        currentComponent.name);

                if (componentPlacing instanceof Starters) {
                    starter = (Starters) componentPlacing;
                }

                targetCell.component = componentPlacing;

            }
            // if placingEntities
            else if (GameStateManager.gameState == GameState.placingEntities) {

                // place the tile
                EntityClass entityPlacing = currentEntity.CreateInstance(targetCell.absPos,
                        currentEntity.name);

                targetCell.entity = entityPlacing;
            }

        }
    }

    public void Delete() {

        if (cursorDragSelectMode) {
            Vector[] beginPos_Size = GetDragSelectRange();

            if (beginPos_Size != null) {
                Cell[][] cellsToDelete = MapManager
                        .GetArrayOfCells(beginPos_Size[0].Add(MapManager.topLeftCellPosition), beginPos_Size[1]);

                for (int i = 0; i < cellsToDelete.length; i++) {
                    for (int j = 0; j < cellsToDelete[i].length; j++) {
                        if (cellsToDelete[i][j].isEmpty()) {
                            continue;
                        }
                        if (cellsToDelete[i][j].component != null) {
                            if (cellsToDelete[i][j].component instanceof Starters) {
                                starter = null;
                            }
                        }

                        cellsToDelete[i][j].component = null;
                        cellsToDelete[i][j].entity = null;
                    }
                }
                return;
            }
        }

        Cell targetCell = MapManager.GetCellFromAbsPos(cursorPos, true);

        if (!targetCell.isEmpty()) {

            if (targetCell.component != null) {
                if (targetCell.component instanceof Starters) {
                    starter = null;
                }
            }

            targetCell.component = null;
            targetCell.entity = null;
        }
    }

    public boolean CheckIfPressOnButton(Button button) {
        if (mousePressedScreenPos.x > button.x && mousePressedScreenPos.x < button.x + button.width
                && mousePressedScreenPos.y > button.y
                && mousePressedScreenPos.y < button.y + button.height) {
            return true;
        }
        return false;
    }

    public Vector[] GetDragSelectRange() {
        if ((cursorEndPos.x != cursorPos.x
                || cursorEndPos.y != cursorPos.y) && cursorDragSelectMode) {

            Vector beginPos = new Vector();
            Vector size = new Vector();
            if (cursorEndPos.x > cursorPos.x) {
                beginPos.x = cursorPos.x;
                size.x = cursorEndPos.x - cursorPos.x + 1;
            } else {
                beginPos.x = cursorEndPos.x;
                size.x = cursorPos.x - cursorEndPos.x + 1;
            }
            if (cursorEndPos.y > cursorPos.y) {
                beginPos.y = cursorPos.y;
                size.y = cursorEndPos.y - cursorPos.y + 1;
            } else {
                beginPos.y = cursorEndPos.y;
                size.y = cursorPos.y - cursorEndPos.y + 1;
            }
            return new Vector[] { beginPos, size };
        }
        return null;
    }
}
