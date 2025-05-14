package snakegame;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ObstacleManager {
	private final List<Point> obstacles = new ArrayList<>();
	private final Random random = new Random();
	private int ticks = 0; // 첫 번째 update()에서 장애물 생성되도록 0으로 초기화

	public boolean isObstacle(Point point) {
		return obstacles.contains(point);
	}

	// 장애물 생성 주기를 조절하려면 이 메서드에서 로직을 변경합니다.
	public void update(List<Point> snakeBody, Point foodLocation) {
		ticks++;

		// 장애물 생성 주기 조정 (예: 17번의 ticks마다 새로운 장애물 생성)
		if (ticks % 17 == 0) {
			generateNewObstacle(snakeBody, foodLocation);
		}
	}

	private void generateNewObstacle(List<Point> snakeBody, Point foodLocation) {
		int x, y;
		Point newPoint;
		do {
			x = random.nextInt(25) + 5; // 5~29 범위
			y = random.nextInt(27) + 3; // 3~29 범위
			newPoint = new Point(x, y);
		} while (isOccupied(newPoint, snakeBody, foodLocation));

		obstacles.add(newPoint);
	}

	// 장애물이 있는지 확인
	private boolean isOccupied(Point point, List<Point> snakeBody, Point foodLocation) {
		return snakeBody.contains(point) || point.equals(foodLocation) || obstacles.contains(point);
	}

	public List<Point> getObstacles() {
		return obstacles;
	}

	public boolean checkCollisionWithSnake(List<Point> snakeBody) {
		for (Point obstacle : obstacles) {
			if (snakeBody.contains(obstacle)) {
				return true; // 장애물과 충돌하면 true
			}
		}
		return false;
	}

	// 장애물 초기화 메서드
	public void reset() {
		obstacles.clear();
		ticks = 0; // 첫 번째 update()에서 바로 장애물이 생성되도록 0으로 설정
	}
}