package vue_controleur;

import javax.swing.*;
import modele.Game;

import static javax.imageio.ImageIO.read;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static java.awt.BorderLayout.PAGE_START;
import static vue_controleur.Swing2048.PIXEL_PER_SQUARE;

public class MainMenu extends JPanel {

    public static final String RESOURCE_PATH = "src/main/resources/";
    public static final int TITLE_WIDTH = 500;
    public static final int TITLE_HEIGHT = 85;
    public static final String TITLE = "2048 GAME";
    public static final int TITLE_SIZE = 100;
    public static final int MENU_ITEM_WIDTH = 474;
    public static final int MENU_ITEM_HEIGHT = 120;
    public static final int MENU_ITEM_SIZE = 32;
    private final Game game;
    private final Map<String, BufferedImage> menuImages = new HashMap<>();

    public MainMenu() {
        game = Game.init(4);
        setOpaque(false);
        setLayout(new GridLayout(4, 1));
        setPreferredSize(new Dimension(game.getSize() * PIXEL_PER_SQUARE, (int) ((game.getSize() + 0.5) * PIXEL_PER_SQUARE)));
        try {
            menuImages.put("base game hover", read(new File(RESOURCE_PATH + "base_game_hover.png")));
            menuImages.put("IA Mode hover", read(new File(RESOURCE_PATH + "IA_Mode_hover.png")));
            menuImages.put("Quit hover", read(new File(RESOURCE_PATH + "Quit_hover.png")));
        } catch (IOException e) {
            e.printStackTrace();
        }
        //setBackground(new Color(51, 51, 51));
        JLabel title = createJLabelImage(RESOURCE_PATH + "2048Title.png", TITLE_WIDTH, TITLE_HEIGHT, TITLE, Color.GREEN, TITLE_SIZE);
        title.setVerticalAlignment(JLabel.NORTH);
        title.setHorizontalAlignment(JLabel.CENTER);
        add(title, PAGE_START);

        JLabel baseGameClickableLabel = createJLabelImage(RESOURCE_PATH + "base_game.png", MENU_ITEM_WIDTH, MENU_ITEM_HEIGHT, "base game", Color.BLACK, MENU_ITEM_SIZE);
        baseGameClickableLabel.addMouseListener(createStandardMouseAdapter(baseGameClickableLabel, new Swing2048(game), "base game", "base game hover"));
        baseGameClickableLabel.setHorizontalAlignment(JLabel.CENTER);
        baseGameClickableLabel.setVerticalAlignment(JLabel.CENTER);
        add(baseGameClickableLabel);

        JLabel iaMode = createJLabelImage(RESOURCE_PATH + "IA_Mode.png", MENU_ITEM_WIDTH, MENU_ITEM_HEIGHT, "IA Mode", Color.GREEN, MENU_ITEM_SIZE);
        iaMode.addMouseListener(createStandardMouseAdapter(iaMode, null, "IA Mode", "IA Mode hover"));
        iaMode.setHorizontalAlignment(JLabel.CENTER);
        iaMode.setVerticalAlignment(JLabel.CENTER);
        add(iaMode);

        JLabel quit = createJLabelImage(RESOURCE_PATH + "Quit.png", MENU_ITEM_WIDTH, MENU_ITEM_HEIGHT, "Quit", Color.GREEN, MENU_ITEM_SIZE);
        quit.addMouseListener(createStandardMouseAdapter(quit, null, "Quit", "Quit hover"));
        quit.setHorizontalAlignment(JLabel.CENTER);
        quit.setVerticalAlignment(JLabel.CENTER);
        add(quit);

    }

    private MouseAdapter createStandardMouseAdapter(JLabel label, final Swing2048 vue, final String iconName, final String iconNameHover) {
        return new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (vue == null) {
                    System.exit(0);
                } else {
                    setLayout(new BorderLayout());
                    game.addObserver(vue);
                    vue.setVisible(true);
                    removeAll();
                    vue.setFocusable(true);
                    setFocusable(false);
                    add(vue);
                    vue.requestFocusInWindow();
                    updateUI();
                }
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                try {
                    label.setIcon(new ImageIcon(menuImages.get(iconNameHover).getScaledInstance(MENU_ITEM_WIDTH, MENU_ITEM_HEIGHT, Image.SCALE_SMOOTH)));
                } catch (NullPointerException ex) {
                    label.setForeground(Color.RED);
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                try {
                    label.setIcon(new ImageIcon(menuImages.get(iconName).getScaledInstance(MENU_ITEM_WIDTH, MENU_ITEM_HEIGHT, Image.SCALE_SMOOTH)));
                } catch (NullPointerException ex) {
                    label.setForeground(Color.BLACK);
                }
            }
        };
    }

    private JLabel createJLabelImage(String pathname, int width, int height, String title, Color color, int size) {
        JLabel label = new JLabel();
        try {
            BufferedImage icon = read(new File(pathname));
            menuImages.put(title, icon);
            label.setIcon(new ImageIcon((icon.getScaledInstance(width, height, Image.SCALE_SMOOTH))));
        } catch (Exception e) {
            label.setText(title);
            label.setFont(new Font("serif", Font.PLAIN, size));
            label.setForeground(color);
        }
        return label;
    }

    @Override
    public void paintComponent(Graphics g) {
        g.drawImage(new ImageIcon(MainMenu.RESOURCE_PATH + "bg.png").getImage(), 0, 0, null);
    }
}
