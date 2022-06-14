package ia;

import modele.Direction;
import modele.Game;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class MonteCarloAI implements AI {

    private Game game;

    public MonteCarloAI(Game game) {
        this.game = game;
    }

    @Override
    public Direction play() {
        Direction best = null;
        double best_score_ratio = 0;
        int games_per_move = 500;
        for (Direction direction : Direction.values()) {
            int score_sum = 0;
            Game copy;
            try {
                copy = (Game) game.clone();
            } catch (CloneNotSupportedException e) {
                continue;
            }
            copy.move(direction);
            for (int i = 0; i < games_per_move; i++) {   
                score_sum += randomGame(copy);
            }

            double ratio = (double) score_sum / games_per_move;
            if (ratio > best_score_ratio || best == null) {
                best = direction;
                best_score_ratio = ratio;
            }
        }
        return best;
    }

    private int randomGame(Game game) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Future<Integer> future = executor.submit(() -> {
            while (!game.isGameWon() && !game.isGameOver()) {
                game.move(Direction.randomDirection());
            }
            return game.getScore();
        });
        try {
            return future.get();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException("Cannot get The value");
        } finally {
            executor.shutdown();
        }
    }

}