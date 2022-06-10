package ia;

import modele.Direction;
import modele.Game;

import java.util.concurrent.*;

public class MonteCarloAI implements AI {

    private Game game;

    public MonteCarloAI(Game game) {
        this.game = game;
    }

    @Override
    public Direction play() {
        Direction best = null;
        double best_score_ratio = 0;
        int games_per_move = 100;
        for (Direction direction : Direction.values()) {
            int score_sum = 0;
            Game copy;
            for (int i = 0; i < games_per_move; i++) {
                try {
                    copy = (Game) game.clone();
                } catch (CloneNotSupportedException e) {
                    continue;
                }
                copy.move(direction);
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
        Callable<Integer> callable = new Callable<Integer>() {
            @Override
            public Integer call() throws Exception {
                while (!game.isGameWon() && !game.isGameOver()) {
                    game.move(Direction.randomDirection());
                }
                return game.getScore();
            }
        };
        Future<Integer> future = executor.submit(callable);
        try {
            return future.get();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException("Cannot get The value");
        } finally {
            executor.shutdown();
        }
    }

}