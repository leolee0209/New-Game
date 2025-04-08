package Main;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;

import Components.Minion;
import Manager.*;
import Manager.GameStateManager.GameState;
import Manager.MapManager.Cell;
import Manager.MapManager.Section;

public class GamePanel extends JPanel implements Runnable {

    int originalTileSize;
    double scale;

    int tileSize;
    int maxScreenCol;
    int maxScreenRow;
    int screenWidth;
    int screenHeight;

    UserInput userInput = new UserInput(this);
    Thread thread;
    JFrame frame;

    UserInterface userInterface;

    int FPS = 60;

    public GamePanel() {
        GetSettingsFromLoadingManager();

        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(new Color(237, 184, 190));
        this.setDoubleBuffered(true);
        this.addKeyListener(userInput);
        this.addMouseListener(userInput);
        this.addMouseMotionListener(userInput);
        this.addMouseWheelListener(userInput);
        this.setFocusable(true);

    }

    public void StartGameThread() {

        userInterface.userInput = this.userInput;
        userInput.userInterface = this.userInterface;
        SavingManager.userInput = this.userInput;
        GameRun.userInput = this.userInput;

        thread = new Thread(this);
        thread.start();
    }

    @Override
    public void run() {

        SavingManager.RefreshSaveFiles();
        try {
            SavingManager.LoadEverythingFromFile();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        double drawInterval = 1000000000 / FPS;
        double nextDrawTime = System.nanoTime() + drawInterval;
        double remainingTime;

        
        int runCount = 0;

        while (thread != null) {

            runCount++;

            try {
                Update(runCount);
            } catch (IOException e) {
                e.printStackTrace();
            }
            repaint();

            try {
                remainingTime = (nextDrawTime - System.nanoTime()) / 1000000;
                if (remainingTime < 0) {
                    remainingTime = 0;
                }

                Thread.sleep((long) remainingTime);

                nextDrawTime += drawInterval;

            } catch (InterruptedException e) {

                e.printStackTrace();
            }
        }
    }

    public void Update(int runCount) throws IOException {

        switch (GameStateManager.gameState) {
            case gameRunning:
                if (runCount % LoadingManager.runSpeed == 0) {
                    GameRun.RunTheGame();
                }
                break;
            case placingComponents:
                // userInput.ChangeComponentInHand();

                // userInput.PlaceAndDeleteComponentsOrEntities();
                break;
            case placingEntities:
                // userInput.ChangeEntityInHand();

                // userInput.PlaceAndDeleteComponentsOrEntities();
                break;
        }

    }

    public void paintComponent(Graphics g) {

        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        // draw background
        g2.setColor(Color.black);
        for (int x = 0; x < maxScreenCol; x++) {
            for (int y = 0; y < maxScreenRow; y++) {

                Vector startingPos = GameToScreenPos(x, y);
                g2.fillRect(startingPos.x, startingPos.y, tileSize - 1, tileSize - 1);
            }
        }
        // draw section border
        Vector topLeftCellPosition = MapManager.topLeftCellPosition;
        int sectionSize = LoadingManager.sectionSize;
        g2.setColor(Color.green);
        for (int x = 0; x < LoadingManager.maxScreenCol; x++) {
            if ((x + topLeftCellPosition.x) % sectionSize == 0) {
                int xPos = GameToScreenPos(x, 0).x - 1;
                g2.drawLine(xPos, 0, xPos, LoadingManager.screenHeight);
            }
        }
        for (int y = 0; y < LoadingManager.maxScreenRow; y++) {
            if ((y + topLeftCellPosition.y) % sectionSize == 0) {
                int yPos = GameToScreenPos(0, y).y - 1;
                g2.drawLine(0, yPos, LoadingManager.screenWidth, yPos);
            }
        }
        // write the section position
        for (int x = 0; x < LoadingManager.maxScreenCol; x++) {
            for (int y = 0; y < LoadingManager.maxScreenRow; y++) {
                if ((y + topLeftCellPosition.y) % sectionSize == 0 && (x + topLeftCellPosition.x) % sectionSize == 0) {
                    Vector sectionBeginPos = GameToScreenPos(x, y);
                    Vector sectionPos = MapManager.AbsPosToSectionPos(topLeftCellPosition.Add(new Vector(x, y)));

                    g2.drawString(sectionPos.FormatToBrackets(), sectionBeginPos.x, sectionBeginPos.y);
                }
            }
        }

        float thickness = 30;
        Stroke oldStroke = g2.getStroke();
        // draw components and entities
        for (Section targetSection : MapManager.loadedSections) {
            ArrayList<Cell> occupiedCells = targetSection.GetOccupiedCells();
            for (Cell targetCell : occupiedCells) {
                Vector targetCellScreenPos = new Vector();

                if (targetCell.entity != null) {
                    targetCellScreenPos = new Vector(
                            GameToScreenPos(targetCell.absPos.Minus(MapManager.topLeftCellPosition)));
                    g2.drawImage(LoadingManager.entitySprites.get(targetCell.entity.name),
                            targetCellScreenPos.x,
                            targetCellScreenPos.y, null);
                    g2.setColor(Color.blue);

                    g2.setStroke(new BasicStroke(thickness));
                    g2.drawString(Integer.toString(targetCell.entity.health),
                            targetCellScreenPos.x + tileSize / 2,
                            targetCellScreenPos.y + tileSize / 2);
                    g2.setStroke(oldStroke);
                } else if (targetCell.component != null) {

                    targetCellScreenPos = new Vector(
                            GameToScreenPos(targetCell.absPos.Minus(MapManager.topLeftCellPosition)));

                    g2.drawImage(LoadingManager.componentSprites.get(targetCell.component.name),
                            targetCellScreenPos.x,
                            targetCellScreenPos.y, null);

                }
            }
        }

        // draw selection cursor
        g2.setColor(new Color(255, 255, 255, 100));
        g2.fillRect(GameToScreenPos(userInput.cursorPos).x, GameToScreenPos(userInput.cursorPos).y, tileSize,
                tileSize);

        if ((userInput.cursorEndPos.x != userInput.cursorPos.x
                || userInput.cursorEndPos.y != userInput.cursorPos.y) && userInput.cursorDragSelectMode) {

            int cursorStartX;
            int cursorStartY;
            if (userInput.cursorEndPos.x > userInput.cursorPos.x) {
                cursorStartX = userInput.cursorPos.x * tileSize;
            } else {
                cursorStartX = userInput.cursorEndPos.x * tileSize;
            }
            if (userInput.cursorEndPos.y > userInput.cursorPos.y) {
                cursorStartY = userInput.cursorPos.y * tileSize;
            } else {
                cursorStartY = userInput.cursorEndPos.y * tileSize;
            }
            g2.fillRect(cursorStartX, cursorStartY,
                    Math.abs(userInput.cursorEndPos.x - userInput.cursorPos.x) * tileSize + tileSize,
                    Math.abs(userInput.cursorEndPos.y - userInput.cursorPos.y) * tileSize + tileSize);

        }

        // if gameRunning then draw minions
        if (GameStateManager.gameState == GameState.gameRunning) {

            for (Minion minion : GameRun.allMinions) {

                if (MapManager.CheckIfInScreen(minion.position)) {
                    Vector targetMinionScreenPos = new Vector(
                            GameToScreenPos(minion.position.Minus(MapManager.topLeftCellPosition)));

                    g2.drawImage(LoadingManager.minionSprite,
                            targetMinionScreenPos.x,
                            targetMinionScreenPos.y, null);
                }
            }

        }

        // draw UI
        userInterface.DrawUserInterface(g2);

        g2.dispose();

    }

    public static Vector GameToScreenPos(int x, int y) {
        Vector screenPos = new Vector(x * LoadingManager.tileSize, y * LoadingManager.tileSize);

        return screenPos;
    }

    public static Vector GameToScreenPos(Vector gamePos) {
        Vector screenPos = new Vector(gamePos.x * LoadingManager.tileSize, gamePos.y * LoadingManager.tileSize);

        return screenPos;
    }

    public void StartToRunGame() throws IOException {

        if (!GameRun.InitRun()) {
            return;
        }
        GameStateManager.gameState = GameState.gameRunning;
        System.out.println("Game start running.");
    }

    public void StopRunningGame() throws FileNotFoundException {
        GameRun.StopRun();
        GameStateManager.gameState = GameState.placingComponents;
        System.out.println("Game stop running.");
    }

    public void GetSettingsFromLoadingManager() {
        originalTileSize = LoadingManager.originalTileSize;
        scale = LoadingManager.scale;

        tileSize = LoadingManager.tileSize;
        maxScreenCol = LoadingManager.maxScreenCol;
        maxScreenRow = LoadingManager.maxScreenRow;
        screenWidth = LoadingManager.screenWidth;
        screenHeight = LoadingManager.screenHeight;
    }

}
