package Main;
import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.OverlayLayout;

import Manager.LoadingManager;

public class Main {
    public static void main(String[] args) throws Exception {

        
        LoadingManager.LoadEverything();
        
        JFrame window = new JFrame();

        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setResizable(false);
        window.setTitle("Game");
        GamePanel gamePanel = new GamePanel();
        UserInterface userInterface = new UserInterface(gamePanel);

        userInterface.setLayout(new OverlayLayout(userInterface));

        gamePanel.setLayout(new BorderLayout());

        window.add(userInterface);

        window.add(gamePanel);
        

        gamePanel.userInterface = userInterface;
        gamePanel.frame = window;



        window.pack();

        window.setLocationRelativeTo(null);
        window.setVisible(true);

        gamePanel.StartGameThread();

    }
}
