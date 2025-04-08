package Manager;

import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.util.Dictionary;
import java.util.Hashtable;
import javax.imageio.ImageIO;

import Main.GamePanel;
import Main.Vector;

public class LoadingManager {

    public static Dictionary<String, Image> componentSprites = new Hashtable<>();
    public static Dictionary<String, Image> entitySprites = new Hashtable<>();
    public static Dictionary<String, Image> componentSpritesForUI = new Hashtable<>();
    public static Dictionary<String, Image> entitySpritesForUI = new Hashtable<>();
    public static Image minionSprite;
    public static Dictionary<String, Vector> tileSelectionPos = new Hashtable<>();

    static String currentDir;

    // display length, size
    public static int originalTileSize = 16;
    public static double scale = 2;
    public static int tileSize = (int) Math.round(originalTileSize * scale);
    public static int screenWidth = 1280;
    public static int screenHeight = 768;
    public static int maxScreenCol = screenWidth / tileSize;
    public static int maxScreenRow = screenHeight / tileSize;

    // UI display adjustments
    public static int selectionAnchorX = 0;
    public static int selectionAnchorY = screenHeight - 80;
    public static int selectionButtonOffsetX = 15;
    public static int selectionButtonOffsetY = 15;
    public static int UISelectionImageSize = 50;

    public static int menuAnchorX = screenWidth / 5;
    public static int menuAnchorY = screenHeight / 6;
    public static int menuWidth = screenWidth / 5 * 3;
    public static int menuHeight = screenHeight / 6 * 4;
    public static int menuButtonOffsetX = 60;
    public static int menuButtonOffsetY = 30;
    public static int menuButtonWidth = menuWidth / 5 * 4;
    public static int menuButtonHeight = 50;

    // KeyHandler
    public static int continuousTime = 300;

    public static int runSpeed = 5;

    // entities' stats
    public static int towerHealth = 20;

    public static int sectionSize = 16;

    public static String[] componentNames = { "spawner",
            "upArrow",
            "downArrow",
            "leftArrow",
            "rightArrow",
            "upStarter",
            "downStarter",
            "leftStarter",
            "rightStarter",
            "upPusher",
            "downPusher",
            "leftPusher",
            "rightPusher",
            "breaker"
    };

    public static String[] entityNames = {
            "tower"
    };

    public static String[] menuMainOptions = {
            "Save",
            "Load",
            "Place Components",
            "Place Entities",
            "Settings"
    };

    public static String[] menuSettingOptions = {
            "originalTileSize",
            "scale"
    };

    public static String minionName = "minion";

    public static void LoadEverything() throws IOException {

        // get the directory which is in front of every file here
        File currentDirFile = new File(".");
        String helper = currentDirFile.getAbsolutePath();
        currentDir = helper.substring(0, helper.length() - currentDirFile.getCanonicalPath().length());

        // load everything
        LoadTileSprites();
        LoadtileSelectionPos();
        LoadEntitySprite();
        LoadEntitySprites();

        //SavingManager.RefreshSaveFiles();
    }

    private static void ReloadSprites() throws IOException {
        componentSprites = new Hashtable<>();
        entitySprites = new Hashtable<>();
        componentSpritesForUI = new Hashtable<>();
        entitySpritesForUI = new Hashtable<>();
        minionSprite = null;

        LoadTileSprites();
        LoadEntitySprite();
        LoadEntitySprites();
    }

    private static void RefreshTileSize() {
        tileSize = (int) Math.round(originalTileSize * scale);
        maxScreenCol = screenWidth / tileSize;
        maxScreenRow = screenHeight / tileSize;
    }

    public static void LoadTileSprites() throws IOException {

        // iterate through all tileNames
        for (int i = 0; i < componentNames.length; i++) {

            // scale images to tileSize and put them into tileSprites dictionary
            Image scaledImage = ImageIO.read(new File(currentDir + "Sprites\\" + componentNames[i] + ".png"))
                    .getScaledInstance(tileSize, tileSize, Image.SCALE_FAST);
            componentSprites.put(componentNames[i], scaledImage);

            // scale images to UISelectionImageSize and put them into componentSpritesForUI
            // dictionary
            Image scaledImageForUI = ImageIO.read(new File(currentDir + "Sprites\\" + componentNames[i] + ".png"))
                    .getScaledInstance(UISelectionImageSize, UISelectionImageSize, Image.SCALE_FAST);
            componentSpritesForUI.put(componentNames[i], scaledImageForUI);
        }

    }

    public static void LoadEntitySprites() throws IOException {

        // iterate through all entityNames
        for (int i = 0; i < entityNames.length; i++) {

            // scale images to tileSize and put them into entityNames dictionary
            Image scaledImage = ImageIO.read(new File(currentDir + "Sprites\\" + entityNames[i] + ".png"))
                    .getScaledInstance(tileSize, tileSize, Image.SCALE_FAST);
            entitySprites.put(entityNames[i], scaledImage);

            // scale images to UISelectionImageSize and put them into entitySpritesForUI
            // dictionary
            Image scaledImageForUI = ImageIO.read(new File(currentDir + "Sprites\\" + entityNames[i] + ".png"))
                    .getScaledInstance(UISelectionImageSize, UISelectionImageSize, Image.SCALE_FAST);
            entitySpritesForUI.put(entityNames[i], scaledImageForUI);

        }

    }

    public static void LoadtileSelectionPos() {
        // iterate through every tilesNames
        int i = 0;
        for (String name : componentNames) {

            // put the position of where those UI tiles which you can click on into the
            // tileSelectionPos dictionary
            Vector pos = new Vector();
            pos.x = selectionAnchorX + selectionButtonOffsetX + i * UISelectionImageSize
                    + selectionButtonOffsetX * i;
            pos.y = selectionAnchorY + selectionButtonOffsetY;

            tileSelectionPos.put(name, pos);

            i++;
        }

        i = 0;
        for (String name : entityNames) {

            // put the position of where those UI tiles which you can click on into the
            // tileSelectionPos dictionary
            Vector pos = new Vector();
            pos.x = selectionAnchorX + selectionButtonOffsetX + i * UISelectionImageSize
                    + selectionButtonOffsetX * i;
            pos.y = selectionAnchorY + selectionButtonOffsetY;

            tileSelectionPos.put(name, pos);

            i++;
        }

    }

    public static void LoadEntitySprite() throws IOException {
        minionSprite = ImageIO.read(new File(currentDir + "Sprites\\" + minionName + ".png"))
                .getScaledInstance(tileSize, tileSize, Image.SCALE_FAST);

    }

    public static void ChangeMapScale(int num, GamePanel gamePanel) throws IOException {

        if (scale + num * 0.1 <= 0) {
            return;
        }

        scale += num * 0.1;

        RefreshTileSize();
        ReloadSprites();
        gamePanel.GetSettingsFromLoadingManager();
    }

    public static void ChangeMapOriginalTileSize(int num, GamePanel gamePanel) throws IOException {
        if (originalTileSize + num <= 0) {
            return;
        }
        originalTileSize += num;
        RefreshTileSize();
        ReloadSprites();
        gamePanel.GetSettingsFromLoadingManager();
    }

    public static boolean CheckIfNameInComponents(String name) {
        for (String tmp : componentNames) {
            if (name.equals(tmp)) {
                return true;
            }
        }
        return false;
    }

    public static boolean CheckIfNameInEntities(String name) {
        for (String tmp : entityNames) {
            if (name.equals(tmp)) {
                return true;
            }
        }
        return false;
    }

}
