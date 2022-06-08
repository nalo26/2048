package ia;

import modele.Direction;
import modele.Game;

public class RandomAI implements AI {
    
    private Game game;

    public RandomAI(Game game) {
        this.game = game;
    }

    public Direction play() {
        int random = Game.RANDOM.nextInt(4);
        return Direction.values()[random];
    }
}
