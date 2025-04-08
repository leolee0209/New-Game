package Main;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.swing.SwingUtilities;

import Main.UserInterface.MenuState;
import Manager.GameStateManager;
import Manager.LoadingManager;
import Manager.MapManager;
import Manager.GameStateManager.GameState;

public class KeyHandler implements KeyListener, MouseListener, MouseMotionListener, MouseWheelListener {

    GamePanel gamePanel;
    UserInterface userInterface;

    public boolean spacePressed, upPressed, downPressed, leftPressed, rightPressed, deletePressed, hPressed,
            enterPressed, lPressed, escapePressed, sPressed;
    public boolean[] numberPressed = new boolean[LoadingManager.componentNames.length];

    public boolean scrollPressed = false;

    public boolean keyHold = false;

    public boolean cursorDragSelectMode = false;

    public boolean mousePressed;
    public Vector mousePressedScreenPos = new Vector();

    public long pressedTime = 0;

    Vector cursorPos = new Vector(0, 0);
    Vector cursorEndPos = new Vector(0, 0);

    Vector lastCursorPos = new Vector(0, 0);

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {

        int code = e.getKeyCode();

        if (!keyHold) {
            pressedTime = System.currentTimeMillis();
        }

        if (code == KeyEvent.VK_SPACE) {
            spacePressed = true;
            keyHold = true;
        }
        if (code == KeyEvent.VK_UP) {
            upPressed = true;
            keyHold = true;
        }
        if (code == KeyEvent.VK_DOWN) {
            downPressed = true;
            keyHold = true;
        }
        if (code == KeyEvent.VK_LEFT) {
            leftPressed = true;
            keyHold = true;
        }
        if (code == KeyEvent.VK_RIGHT) {
            rightPressed = true;
            keyHold = true;
        }
        if (code == KeyEvent.VK_1) {
            numberPressed[0] = true;
        }
        if (code == KeyEvent.VK_2) {
            numberPressed[1] = true;
        }
        if (code == KeyEvent.VK_3) {
            numberPressed[2] = true;
        }
        if (code == KeyEvent.VK_4) {
            numberPressed[3] = true;
        }
        if (code == KeyEvent.VK_5) {
            numberPressed[4] = true;
        }
        if (code == KeyEvent.VK_6) {
            numberPressed[5] = true;
        }
        if (code == KeyEvent.VK_7) {
            numberPressed[6] = true;
        }
        if (code == KeyEvent.VK_8) {
            numberPressed[7] = true;
        }
        if (code == KeyEvent.VK_9) {
            numberPressed[8] = true;
        }
        if (code == KeyEvent.VK_DELETE) {
            deletePressed = true;
        }
        if (code == KeyEvent.VK_H && !hPressed) {
            hPressed = true;
            gamePanel.userInterface.showSelectionUI = !gamePanel.userInterface.showSelectionUI;
        }
        if (code == KeyEvent.VK_ESCAPE && !escapePressed) {
            escapePressed = true;
            if (gamePanel.userInterface.menuState == MenuState.Main) {

                gamePanel.userInterface.showMenuUI = !gamePanel.userInterface.showMenuUI;
            } else {
                gamePanel.userInterface.menuState = MenuState.Main;
            }
        }
        if (code == KeyEvent.VK_ENTER && !enterPressed) {
            enterPressed = true;
            if (GameStateManager.gameState == GameState.gameRunning) {
                try {
                    gamePanel.StopRunningGame();
                } catch (FileNotFoundException e1) {
                    e1.printStackTrace();
                }
            } else {
                try {
                    gamePanel.StartToRunGame();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }

        ProcessKeyPressedEvent();

    }

    @Override
    public void keyReleased(KeyEvent e) {
        int code = e.getKeyCode();

        pressedTime = 0;

        if (code == KeyEvent.VK_SPACE) {
            spacePressed = false;
            keyHold = false;
        }
        if (code == KeyEvent.VK_UP) {
            upPressed = false;
            keyHold = false;
        }
        if (code == KeyEvent.VK_DOWN) {
            downPressed = false;
            keyHold = false;
        }
        if (code == KeyEvent.VK_LEFT) {
            leftPressed = false;
            keyHold = false;
        }
        if (code == KeyEvent.VK_RIGHT) {
            rightPressed = false;
            keyHold = false;
        }
        if (code == KeyEvent.VK_1) {
            numberPressed[0] = false;
        }
        if (code == KeyEvent.VK_2) {
            numberPressed[1] = false;
        }
        if (code == KeyEvent.VK_3) {
            numberPressed[2] = false;
        }
        if (code == KeyEvent.VK_4) {
            numberPressed[3] = false;
        }
        if (code == KeyEvent.VK_5) {
            numberPressed[4] = false;
        }
        if (code == KeyEvent.VK_6) {
            numberPressed[5] = false;
        }
        if (code == KeyEvent.VK_7) {
            numberPressed[6] = false;
        }
        if (code == KeyEvent.VK_8) {
            numberPressed[7] = false;
        }
        if (code == KeyEvent.VK_9) {
            numberPressed[8] = false;
        }
        if (code == KeyEvent.VK_DELETE) {
            deletePressed = false;
        }
        if (code == KeyEvent.VK_H) {
            hPressed = false;
        }
        if (code == KeyEvent.VK_ESCAPE) {
            escapePressed = false;
        }
        if (code == KeyEvent.VK_ENTER) {
            enterPressed = false;
        }

    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        mousePressed = false;
        if (SwingUtilities.isMiddleMouseButton(e)) {
            scrollPressed = false;
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (!mousePressed) {
            mousePressed = true;

            mousePressedScreenPos.SetVector(e.getX(), e.getY());

            if (userInterface.CheckIfClickingOnUI().equals("")) {
                cursorEndPos.x = e.getX() / LoadingManager.tileSize;
                cursorEndPos.y = e.getY() / LoadingManager.tileSize;
            }

            if (SwingUtilities.isLeftMouseButton(e)) {
                cursorDragSelectMode = true;
                try {
                    ProcessLeftMouseClickEvent();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
            if (SwingUtilities.isRightMouseButton(e)) {
                cursorDragSelectMode = true;
                ProcessRightMouseClickEvent();
            }
            if (SwingUtilities.isMiddleMouseButton(e)) {
                cursorDragSelectMode = false;
                cursorPos.SetVector(mousePressedScreenPos.Divide(LoadingManager.tileSize));
                scrollPressed = true;
                lastCursorPos.SetVector(cursorPos);
            }
        }
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    @Override
    public void mouseDragged(MouseEvent e) {

        cursorEndPos.SetVector(e.getX() / LoadingManager.tileSize, e.getY() / LoadingManager.tileSize);

        if (scrollPressed) {
            if (!lastCursorPos.equals(cursorEndPos)) {
                MapManager.MoveMap(lastCursorPos, cursorEndPos);
                lastCursorPos.SetVector(cursorEndPos);
            }
        }

    }

    @Override
    public void mouseMoved(MouseEvent e) {
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        int scrollUnit = e.getUnitsToScroll();
        try {
            LoadingManager.ChangeMapScale(scrollUnit / 3, gamePanel);
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }

    public void ProcessKeyPressedEvent() {
    }

    public void ProcessLeftMouseClickEvent() throws IOException {
    }

    public void ProcessRightMouseClickEvent() {
    }

}
