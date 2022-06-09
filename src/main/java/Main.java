import javax.swing.*;
import modele.Game;
import vue_controleur.Console2048;
import vue_controleur.MainMenu;

import static javax.swing.WindowConstants.EXIT_ON_CLOSE;
import java.awt.*;

import static vue_controleur.Swing2048.PIXEL_PER_SQUARE;

public class Main {

    public static void main(String[] args) {
        //mainConsole();
        System.setProperty("java2d.uiScale", "2.5");
        mainSwing();
    }

    public static void mainConsole() {
        Game jeu = Game.init(4);
        Console2048 vue = new Console2048(jeu);
        jeu.addObserver(vue);

        vue.start();

    }

    public static void mainSwing() {
        Game game = Game.init(4);

        /*Swing2048 vue = new Swing2048(jeu);
        vue.setResizable(false);
        jeu.addObserver(vue);

        vue.setVisible(true);*/
        JFrame mainFrame = new JFrame();
        mainFrame.setDefaultCloseOperation(EXIT_ON_CLOSE);
        mainFrame.setResizable(false);
        mainFrame.setSize(new Dimension(game.getSize() * PIXEL_PER_SQUARE, (int) ((game.getSize() + 0.5) * PIXEL_PER_SQUARE)));
        MainMenu menu = new MainMenu();
        menu.setVisible(true);
        mainFrame.setContentPane(menu);
        mainFrame.setVisible(true);
        mainFrame.pack();
    }


}
