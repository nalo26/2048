import modele.Game;
import vue_controleur.Console2048;
import vue_controleur.MainMenu;

public class Main {

    public static void main(String[] args) {
        //mainConsole();
        mainSwing();

    }

    public static void mainConsole() {
        Game jeu = Game.init(4);
        Console2048 vue = new Console2048(jeu);
        jeu.addObserver(vue);

        vue.start();

    }

    public static void mainSwing() {
        /*Game jeu = Game.init(4);
        Swing2048 vue = new Swing2048(jeu);
        vue.setResizable(false);
        jeu.addObserver(vue);

        vue.setVisible(true);*/
        MainMenu menu = new MainMenu();
        menu.setVisible(true);
        menu.pack();


    }


}
