// GameManager.java
package snakegame;

import java.awt.Point;
import java.util.List;

public class GameManager {
	private final Snake snake;
	private final Food food;
	private final ObstacleManager obstacleManager;
	private int score = 0;
	private int lives = 3;
	private int speed = 200;

	public GameManager(Snake snake, Food food, ObstacleManager obstacleManager) {
		this.snake = snake;
		this.food = food;
		this.obstacleManager = obstacleManager;
	}

	public boolean updateGame() throws InvalidMoveException, GameWinException {
		snake.move();

		if (snake.getBody().getFirst().equals(food.getLocation())) {
			snake.grow();
			food.generateNew(snake.getBody(), obstacleManager.getObstacles());
			score++;
			speed = Math.max(speed - 10, 50);

			// 게임 클리어 조건: 점수 10 이상이면 예외 발생
			if (score >= 10) {
				throw new GameWinException("축하합니다! 게임을 클리어했습니다!");
			}

			return true; // 음식 먹었을 때만 true 반환
		}

		return false; // 아무것도 안 먹었을 때 false
	}

	public int getScore() {
		return score;
	}

	public int getLives() {
		return lives;
	}

	public void loseLife() {
		lives--;
	}

	public void resetGame() {
		snake.reset();
		food.generateNew(snake.getBody(), obstacleManager.getObstacles());
		score = 0;
		lives = 3;
		speed = 200;
	}

	public int getSpeed() {
		return speed;
	}
}