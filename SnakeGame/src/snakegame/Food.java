package snakegame;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Food {
	private Point location;
	private final Random rand = new Random();

	public Food() {
		generateNew();
	}

	public void generateNew() {
		int x = rand.nextInt(30);
		int y = rand.nextInt(30);
		location = new Point(x, y);
	}

	// 뱀과 장애물이 있는 자리를 제외한 위치에서 아이템 생성
	public void generateNew(List<Point> snakeBody, List<Point> obstacles) {
		List<Point> validPoints = new ArrayList<>();

		// x=5~29, y=3~29 영역에서만 생성
		for (int x = 5; x < 30; x++) {
			for (int y = 3; y < 30; y++) {
				Point candidate = new Point(x, y);
				if (!snakeBody.contains(candidate) && !obstacles.contains(candidate)) {
					validPoints.add(candidate);
				}
			}
		}

		if (validPoints.isEmpty()) {
			System.out.println("생성 가능한 위치가 없습니다!");
			return;
		}

		int randomIndex = rand.nextInt(validPoints.size());
		location = validPoints.get(randomIndex);
	}

	public Point getLocation() {
		return location;
	}
}