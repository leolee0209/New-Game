package Main;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.util.ArrayList;
import java.util.Dictionary;
import javax.swing.JLabel;

import Manager.GameStateManager;
import Manager.LoadingManager;
import Manager.SavingManager;
import Manager.GameStateManager.GameState;

public class UserInterface extends JLabel {

    GamePanel gamePanel;
    UserInput userInput;

    MenuState menuState = MenuState.Main;

    int screenWidth = LoadingManager.screenWidth;
    int screenHeight = LoadingManager.screenHeight;

    Dictionary<String, Vector> selectionImagePos;

    int selectionAnchorX = LoadingManager.selectionAnchorX;
    int selectionAnchorY = LoadingManager.selectionAnchorY;
    int selectionButtonOffsetX = LoadingManager.selectionButtonOffsetX;
    int selectionButtonOffsetY = LoadingManager.selectionButtonOffsetY;
    int UItileSelectionImageSize = LoadingManager.UISelectionImageSize;

    int menuAnchorX = LoadingManager.menuAnchorX;
    int menuAnchorY = LoadingManager.menuAnchorY;
    int menuWidth = LoadingManager.menuWidth;
    int menuHeight = LoadingManager.menuHeight;
    int menuButtonOffsetX = LoadingManager.menuButtonOffsetX;
    int menuButtonOffsetY = LoadingManager.menuButtonOffsetY;
    int menuButtonWidth = LoadingManager.menuButtonWidth;
    int menuButtonHeight = LoadingManager.menuButtonHeight;

    boolean showSelectionUI = true;
    boolean showMenuUI = false;

    String[] menuMainOptions;
    ArrayList<Button> menuButtons = new ArrayList<>();

    public enum MenuState {
        Main,
        Save,
        Load,
        Settings
    }

    public UserInterface(GamePanel gamePanel) {

        this.gamePanel = gamePanel;

        this.screenWidth = LoadingManager.screenWidth;
        this.screenHeight = LoadingManager.screenHeight;

        selectionImagePos = LoadingManager.tileSelectionPos;
        menuMainOptions = LoadingManager.menuMainOptions;

        this.setFocusable(false);
        this.setVisible(true);

    }

    public void DrawUserInterface(Graphics2D g2) {

        if (showSelectionUI) {
            DrawSelectionArea(g2);
        }

        if (showMenuUI) {
            DrawMenu(g2);
        }
    }

    private void DrawSelectionArea(Graphics2D g2) {

        // draw the underlying plate that it's on
        g2.setColor(new Color(100, 80, 180, 180));
        g2.fillRect(selectionAnchorX, selectionAnchorY, screenWidth, screenHeight);

        if (GameStateManager.gameState == GameState.placingComponents) {
            // load through placable components to place them all
            int i = 0;
            for (String name : LoadingManager.componentNames) {

                // draw bottom plate for every component
                g2.setColor(Color.white);
                g2.fillRect(
                        selectionAnchorX + selectionButtonOffsetX + i * LoadingManager.UISelectionImageSize
                                + selectionButtonOffsetX * i,
                        selectionAnchorY + selectionButtonOffsetY, LoadingManager.UISelectionImageSize,
                        LoadingManager.UISelectionImageSize);

                // draw components' image
                g2.drawImage(LoadingManager.componentSpritesForUI.get(name),
                        selectionAnchorX + selectionButtonOffsetX + i * LoadingManager.UISelectionImageSize
                                + selectionButtonOffsetX * i,
                        selectionAnchorY + selectionButtonOffsetY, null);

                // draw text for those components
                g2.setColor(Color.black);
                g2.drawString(Integer.toString(i + 1),
                        (int) (selectionAnchorX + selectionButtonOffsetX * 1.2
                                + i * LoadingManager.UISelectionImageSize
                                + selectionButtonOffsetX * i),
                        (int) (selectionAnchorY + selectionButtonOffsetY * 1.8));

                i++;
            }

            if (userInput == null) {
                System.err.println(1111111123);
            }
            // draw the selection outline that shows you what your selecting
            if (userInput.currentComponent != null) {
                g2.setColor(Color.black);
                float thickness = 4;
                Stroke oldStroke = g2.getStroke();
                g2.setStroke(new BasicStroke(thickness));
                g2.drawRect(selectionImagePos.get(userInput.currentComponent.name).x,
                        selectionImagePos.get(userInput.currentComponent.name).y,
                        LoadingManager.UISelectionImageSize,
                        LoadingManager.UISelectionImageSize);
                g2.setStroke(oldStroke);

            }
        } else if (GameStateManager.gameState == GameState.placingEntities) {
            // load through placable components to place them all
            int i = 0;
            for (String name : LoadingManager.entityNames) {

                // draw bottom plate for every component
                g2.setColor(Color.white);
                g2.fillRect(
                        selectionAnchorX + selectionButtonOffsetX + i * LoadingManager.UISelectionImageSize
                                + selectionButtonOffsetX * i,
                        selectionAnchorY + selectionButtonOffsetY, LoadingManager.UISelectionImageSize,
                        LoadingManager.UISelectionImageSize);

                // draw entities' image
                g2.drawImage(LoadingManager.entitySpritesForUI.get(name),
                        selectionAnchorX + selectionButtonOffsetX + i * LoadingManager.UISelectionImageSize
                                + selectionButtonOffsetX * i,
                        selectionAnchorY + selectionButtonOffsetY, null);

                // draw text for those components
                g2.setColor(Color.black);
                g2.drawString(Integer.toString(i + 1),
                        (int) (selectionAnchorX + selectionButtonOffsetX * 1.2
                                + i * LoadingManager.UISelectionImageSize
                                + selectionButtonOffsetX * i),
                        (int) (selectionAnchorY + selectionButtonOffsetY * 1.8));

                i++;
            }

            // draw the selection outline that shows you what your selecting
            if (userInput.currentEntity != null) {
                g2.setColor(Color.black);
                float thickness = 4;
                Stroke oldStroke = g2.getStroke();
                g2.setStroke(new BasicStroke(thickness));
                g2.drawRect(selectionImagePos.get(userInput.currentEntity.name).x,
                        selectionImagePos.get(userInput.currentEntity.name).y,
                        LoadingManager.UISelectionImageSize,
                        LoadingManager.UISelectionImageSize);
                g2.setStroke(oldStroke);

            }
        }
    }

    private void DrawMenu(Graphics2D g2) {

        g2.setColor(new Color(100, 80, 180, 230));
        g2.fillRect(menuAnchorX, menuAnchorY, menuWidth, menuHeight);

        menuButtons.clear();

        int i = 0;
        switch (menuState) {
            case Main:

                for (String content : menuMainOptions) {
                    menuButtons.add(new Button(menuAnchorX + menuButtonOffsetX,
                            menuAnchorY + menuButtonOffsetY + (menuButtonOffsetY + menuButtonHeight) * i,
                            menuButtonWidth, menuButtonHeight, content));

                    i++;
                }
                break;
            case Save:

                menuButtons.add(new Button(menuAnchorX + menuButtonOffsetX,
                        menuAnchorY + menuButtonOffsetY + (menuButtonOffsetY + menuButtonHeight) * i,
                        menuButtonWidth, menuButtonHeight, "new Save File"));
                i++;

                for (String content : SavingManager.allFileNames) {
                    menuButtons.add(new Button(menuAnchorX + menuButtonOffsetX,
                            menuAnchorY + menuButtonOffsetY + (menuButtonOffsetY + menuButtonHeight) * i,
                            menuButtonWidth, menuButtonHeight, content));

                    i++;
                }
                break;
            case Load:

                for (String content : SavingManager.allFileNames) {
                    menuButtons.add(new Button(menuAnchorX + menuButtonOffsetX,
                            menuAnchorY + menuButtonOffsetY + (menuButtonOffsetY + menuButtonHeight) * i,
                            menuButtonWidth, menuButtonHeight, content));

                    i++;
                }
                break;
            case Settings:

                for (String content : LoadingManager.menuSettingOptions) {

                    if (content.equals("originalTileSize")) {
                        menuButtons.add(new Button(menuAnchorX + menuButtonOffsetX + menuButtonWidth / 5,
                                menuAnchorY + menuButtonOffsetY + (menuButtonOffsetY + menuButtonHeight) * i,
                                menuButtonWidth / 5 * 3, menuButtonHeight, content + " : " + LoadingManager.tileSize));

                        menuButtons.add(new Button(menuAnchorX + menuButtonOffsetX,
                                menuAnchorY + menuButtonOffsetY + (menuButtonOffsetY + menuButtonHeight) * i,
                                menuButtonWidth / 5, menuButtonHeight, "-", Button.Type.Minus, content));

                        menuButtons.add(new Button(menuAnchorX + menuButtonOffsetX + menuButtonWidth / 5 * 4,
                                menuAnchorY + menuButtonOffsetY + (menuButtonOffsetY + menuButtonHeight) * i,
                                menuButtonWidth / 5, menuButtonHeight, "+", Button.Type.Plus, content));
                    } else if (content.equals("scale")) {
                        menuButtons.add(new Button(menuAnchorX + menuButtonOffsetX + menuButtonWidth / 5,
                                menuAnchorY + menuButtonOffsetY + (menuButtonOffsetY + menuButtonHeight) * i,
                                menuButtonWidth / 5 * 3, menuButtonHeight, content + " : x" + LoadingManager.scale));

                        menuButtons.add(new Button(menuAnchorX + menuButtonOffsetX,
                                menuAnchorY + menuButtonOffsetY + (menuButtonOffsetY + menuButtonHeight) * i,
                                menuButtonWidth / 5, menuButtonHeight, "-", Button.Type.Minus, content));

                        menuButtons.add(new Button(menuAnchorX + menuButtonOffsetX + menuButtonWidth / 5 * 4,
                                menuAnchorY + menuButtonOffsetY + (menuButtonOffsetY + menuButtonHeight) * i,
                                menuButtonWidth / 5, menuButtonHeight, "+", Button.Type.Plus, content));
                    }

                    i++;
                }
                break;
        }

        g2.setColor(Color.white);
        for (Button button : menuButtons) {
            button.enabled = true;

            g2.drawRect(button.x, button.y, button.width, button.height);
            g2.drawString(button.content, button.x + button.width / 2, button.y + button.height / 2);
        }

    }

    public String CheckIfClickingOnUI() {
        int mouseX = userInput.mousePressedScreenPos.x;
        int mouseY = userInput.mousePressedScreenPos.y;

        // menu
        if (mouseX > menuAnchorX && mouseX < menuAnchorX + menuWidth && mouseY > menuAnchorY
                && mouseY < menuAnchorY + menuHeight && showMenuUI) {
            return "menu";
        }

        // tile selection area
        if (mouseX > selectionAnchorX && mouseX < selectionAnchorX + screenWidth && mouseY > selectionAnchorY
                && mouseY < selectionAnchorY + screenHeight && showSelectionUI) {
            return "tileSelection";
        }

        return "";

    }

}
