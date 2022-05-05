package vue_controleur;

import javax.swing.*;
import javax.swing.border.Border;
import modele.Case;
import modele.Game;

import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Observable;
import java.util.Observer;

public class Swing2048 extends JFrame implements Observer {
    private static final int PIXEL_PER_SQUARE = 60;
    // tableau de cases : i, j -> case graphique
    private JLabel[][] tabC;
    private Game game;


    public Swing2048(Game _jeu) {
        game = _jeu;
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(game.getSize() * PIXEL_PER_SQUARE, game.getSize() * PIXEL_PER_SQUARE);
        tabC = new JLabel[game.getSize()][game.getSize()];


        JPanel contentPane = new JPanel(new GridLayout(game.getSize(), game.getSize()));

        for (int i = 0; i < game.getSize(); i++) {
            for (int j = 0; j < game.getSize(); j++) {
                Border border = BorderFactory.createLineBorder(Color.darkGray, 5);
                tabC[i][j] = new JLabel();
                tabC[i][j].setBorder(border);
                tabC[i][j].setHorizontalAlignment(SwingConstants.CENTER);


                contentPane.add(tabC[i][j]);

            }
        }
        setContentPane(contentPane);
        addKeyListener();
        refresh();

    }


    /**
     * Correspond à la fonctionnalité de Vue : affiche les données du modèle
     */
    private void refresh() {

        SwingUtilities.invokeLater(new Runnable() { // demande au processus graphique de réaliser le traitement
            @Override
            public void run() {
                for (int i = 0; i < game.getSize(); i++) {
                    for (int j = 0; j < game.getSize(); j++) {
                        Case c = game.getCase(j, i);

                        if (c == null) {

                            tabC[i][j].setText("");

                        } else {
                            tabC[i][j].setText(c.getValue() + "");
                        }


                    }
                }
            }
        });


    }

    /**
     * Correspond à la fonctionnalité de Contrôleur : écoute les évènements, et déclenche des traitements sur le modèle
     */
    private void addKeyListener() {
        addKeyListener(new KeyAdapter() { // new KeyAdapter() { ... } est une instance de classe anonyme, il s'agit d'un objet qui correspond au controleur dans MVC
            @Override
            public void keyPressed(KeyEvent e) {
                switch (e.getKeyCode()) {  // on regarde quelle touche a été pressée
                    case KeyEvent.VK_LEFT:
                        game.fillGrid();
                        break;
                    case KeyEvent.VK_RIGHT:
                        game.fillGrid();
                        break;
                    case KeyEvent.VK_DOWN:
                        game.fillGrid();
                        break;
                    case KeyEvent.VK_UP:
                        game.fillGrid();
                        break;
                }
            }
        });
    }


    @Override
    public void update(Observable o, Object arg) {
        refresh();
    }
}