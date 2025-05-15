// GamePanel1.java
package snakegame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Path2D;

public class GamePanel1 extends JPanel implements ActionListener, KeyListener, MouseListener {
	private Snake snake;
	private final Food food;
	private final GameManager manager;
	private final Timer timer;
	private boolean gameOver = false;
	private boolean gameStarted = false;
	private boolean gameWon = false;
	private Point lastCollisionPoint = null;
	private boolean isBlinking = false;
	private long blinkStartTime = 0;
	private final int BLINK_DURATION = 2000;
	private final int BLINK_INTERVAL = 200;

	private ObstacleManager obstacleManager;

	public GamePanel1() {
		this.setFocusable(true);
		this.setPreferredSize(new Dimension(600, 600));
		this.addKeyListener(this);
		this.addMouseListener(this);

		snake = new Snake();
		food = new Food();

		obstacleManager = new ObstacleManager();
		snake = new Snake(obstacleManager);
		manager = new GameManager(snake, food, obstacleManager);
		timer = new Timer(200, this);
	}

	private void drawCenteredString(Graphics2D g2d, String text, int x, int y) {
		FontMetrics metrics = g2d.getFontMetrics();
		int textWidth = metrics.stringWidth(text);
		int textHeight = metrics.getHeight();
		g2d.drawString(text, x - textWidth / 2, y + textHeight / 2);
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;

		g2d.setColor(Constants.BACKGROUND_COLOR);
		g2d.fillRect(0, 0, getWidth(), getHeight());

		g2d.setColor(Constants.GRID_COLOR);
		for (int i = 0; i < getWidth(); i += Constants.CELL_SIZE) {
			for (int j = 0; j < getHeight(); j += Constants.CELL_SIZE) {
				g2d.drawRect(i, j, Constants.CELL_SIZE, Constants.CELL_SIZE);
			}
		}

		if (!gameStarted) {
			drawStartScreen(g2d);
		} else if (gameOver) {
			drawGameOverScreen(g2d);
		} else if (gameWon) {
			drawWinScreen(g2d);
		} else {
			drawGameElements(g2d);

		}
	}

	private void drawStartScreen(Graphics2D g2d) {
		g2d.setColor(Constants.TEXT_COLOR);
		int centerX = getWidth() / 2;
		int startY = getHeight() / 2 - 100;

		g2d.setFont(new Font("Dialog", Font.BOLD, 36));
		drawCenteredString(g2d, "🐍 Snake Game 🐍", centerX, startY);

		g2d.setFont(new Font("Dialog", Font.PLAIN, 18));
		drawCenteredString(g2d, "음식을 먹고 뱀을 키우세요!", centerX, startY + 40);
		drawCenteredString(g2d, "장애물을 피하고 벽에 부딪히지 마세요!", centerX, startY + 70);
		drawCenteredString(g2d, "방향키 ↑ ↓ ← → 로 조작하세요", centerX, startY + 100);
		drawCenteredString(g2d, "목숨은 하트로 표시됩니다. 모두 잃으면 게임 오버!", centerX, startY + 130);

		g2d.setFont(new Font("Dialog", Font.ITALIC, 16));
		drawCenteredString(g2d, "클릭해서 게임을 시작하세요", centerX, startY + 180);
	}

	private void drawGameOverScreen(Graphics2D g2d) {
		g2d.setColor(Constants.TEXT_COLOR);
		g2d.setFont(new Font("Dialog", Font.BOLD, 30));
		String msg1 = "Game Over!";
		String msg2 = "최종 점수: " + manager.getScore();
		String msg3 = "다시 시작하려면 클릭하세요";
		int centerY = getHeight() / 2;
		drawCenteredString(g2d, msg1, getWidth() / 2, centerY - 40);
		drawCenteredString(g2d, msg2, getWidth() / 2, centerY);
		drawCenteredString(g2d, msg3, getWidth() / 2, centerY + 40);
	}

	private void drawWinScreen(Graphics2D g2d) {
		g2d.setColor(Constants.TEXT_COLOR);
		g2d.setFont(new Font("Dialog", Font.BOLD, 30));

		int centerX = getWidth() / 2;
		int centerY = getHeight() / 2;

		// 텍스트 표시
		drawCenteredString(g2d, "🎉 You Win! 🎉", centerX, centerY - 80);
		drawCenteredString(g2d, "점수: " + manager.getScore(), centerX, centerY - 40);
		drawCenteredString(g2d, "최고 기록을 달성했어요!", centerX, centerY - 150); // 추가 메시지

		// 다시하기 버튼
		g2d.setColor(new Color(100, 255, 100));
		g2d.fillRoundRect(centerX - 120, centerY + 10, 100, 40, 10, 10);
		g2d.setColor(Color.BLACK);
		g2d.setFont(new Font("Dialog", Font.BOLD, 16));
		drawCenteredString(g2d, "다시하기", centerX - 70, centerY + 27);

		// 종료 버튼
		g2d.setColor(new Color(255, 100, 100));
		g2d.fillRoundRect(centerX + 20, centerY + 10, 100, 40, 10, 10);
		g2d.setColor(Color.BLACK);
		drawCenteredString(g2d, "종료", centerX + 70, centerY + 27);

		// 추가 연출 이펙트 (반짝이 효과 등 텍스트)
		g2d.setColor(new Color(255, 215, 0));
		g2d.setFont(new Font("Dialog", Font.PLAIN, 18));
		drawCenteredString(g2d, "✨ 멋진 플레이였어요! ✨", centerX, centerY + 90);
	}

	private void drawGameElements(Graphics2D g2d) {
		long currentTime = System.currentTimeMillis();
		boolean showSnake = true;

		if (isBlinking) {
			long elapsed = currentTime - blinkStartTime;
			if (elapsed >= BLINK_DURATION) {
				isBlinking = false;
			} else {
				showSnake = (elapsed / BLINK_INTERVAL) % 2 == 0; // 깜빡임 토글
			}
		}

		// 뱀 그리기
		if (showSnake) {
			for (int i = 0; i < snake.getBody().size(); i++) {
				Point p = snake.getBody().get(i);
				g2d.setColor(i == 0 ? Constants.SNAKE_HEAD_COLOR : Constants.SNAKE_BODY_COLOR);
				g2d.fillRoundRect(p.x * Constants.CELL_SIZE, p.y * Constants.CELL_SIZE, Constants.CELL_SIZE,
						Constants.CELL_SIZE, 8, 8);
			}
		}

		// 음식 그리기
		g2d.setColor(Constants.FOOD_COLOR);
		Point foodLoc = food.getLocation();
		g2d.fillOval(foodLoc.x * Constants.CELL_SIZE, foodLoc.y * Constants.CELL_SIZE, Constants.CELL_SIZE,
				Constants.CELL_SIZE);

		// 장애물 그리기
		g2d.setColor(Constants.OBSTACLE_COLOR);
		for (Point p : obstacleManager.getObstacles()) {
			g2d.fill3DRect(p.x * Constants.CELL_SIZE, p.y * Constants.CELL_SIZE, Constants.CELL_SIZE,
					Constants.CELL_SIZE, true);
		}

		// 점수
		g2d.setColor(Constants.TEXT_COLOR);
		g2d.setFont(new Font("Dialog", Font.PLAIN, 15));
		g2d.drawString("점수: " + manager.getScore(), 10, 20);

		// 목숨 (하트)
		int heartSize = 20;
		int heartXStart = 10;
		int heartYStart = 40;
		g2d.setColor(Color.PINK);
		for (int i = 0; i < manager.getLives(); i++) {
			drawHeart(g2d, heartXStart + (i * (heartSize + 5)), heartYStart, heartSize);
		}
	}

	private void drawHeart(Graphics2D g2d, int x, int y, int size) {
		int r = size / 2;
		g2d.setColor(new Color(255, 105, 180));
		g2d.fillArc(x, y, r, r, 0, 180);
		g2d.fillArc(x + r, y, r, r, 0, 180);
		int[] xPoints = { x, x + size, x + size / 2 };
		int[] yPoints = { y + r, y + r, y + size };
		g2d.fillPolygon(xPoints, yPoints, 3);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (!gameOver && !gameWon && gameStarted) {
			boolean ateFood = false;

			try {
				ateFood = manager.updateGame(); // 여기서 예외 발생 가능
				timer.setDelay(manager.getSpeed());

				obstacleManager.update(snake.getBody(), food.getLocation());
				Point head = snake.getHead();

				// 충돌 감지
				if (!ateFood) {
					if (obstacleManager.isObstacle(head)) {
						if (lastCollisionPoint == null || !lastCollisionPoint.equals(head)) {
							manager.loseLife();

							lastCollisionPoint = new Point(head);

							// 🟡 깜빡임 시작
							isBlinking = true;
							blinkStartTime = System.currentTimeMillis();

							// 🔽 깜빡임 반영을 위해 즉시 그리기
							repaint();
							paintImmediately(0, 0, getWidth(), getHeight());

							if (manager.getLives() <= 10) {
								gameOver = true;
							}
						}
					} else {
						lastCollisionPoint = null;
					}
				} else {
					lastCollisionPoint = null;
				}

			} catch (InvalidMoveException ex) {
				if (lastCollisionPoint == null || !lastCollisionPoint.equals(snake.getHead())) {
					manager.loseLife();

					lastCollisionPoint = new Point(snake.getHead());

					// 🟡 깜빡임 시작
					isBlinking = true;
					blinkStartTime = System.currentTimeMillis();

					System.out.println("목숨-1");
					if (manager.getLives() <= 0) {
						gameOver = true;
						System.out.println("죽음");
					}
				}
			} catch (GameWinException gwe) {
				gameWon = true;
				timer.stop();
				System.out.println("축하합니다! 게임을 클리어했습니다!!");
			}

			repaint();

		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		if (!gameStarted) {
			gameStarted = true;
			gameOver = false;
			gameWon = false;
			manager.resetGame();
			obstacleManager.reset();
			timer.start();
			repaint();
		} else if (gameOver) {
			gameStarted = true;
			gameOver = false;
			gameWon = false;
			manager.resetGame();
			obstacleManager.reset();
			timer.start();
			repaint();
		} else if (gameWon) {
			// 승리 화면에서 버튼 클릭 처리
			int x = e.getX();
			int y = e.getY();
			int centerY = getHeight() / 2;

			// 다시하기 버튼 영역
			if (x >= getWidth() / 2 - 120 && x <= getWidth() / 2 - 20 && y >= centerY + 10 && y <= centerY + 50) {
				gameStarted = true;
				gameOver = false;
				gameWon = false;
				manager.resetGame();
				obstacleManager.reset();
				timer.start();
				repaint();
			}
			// 종료 버튼 영역
			else if (x >= getWidth() / 2 + 20 && x <= getWidth() / 2 + 120 && y >= centerY + 10 && y <= centerY + 50) {
				System.exit(0);
			}
		}
	}

	// 나머지 메서드들은 동일하게 유지...
	@Override
	public void keyPressed(KeyEvent e) {
		if (!gameOver && !gameWon && gameStarted) {
			switch (e.getKeyCode()) {
			case KeyEvent.VK_UP:
				snake.setDirection('U');
				break;
			case KeyEvent.VK_DOWN:
				snake.setDirection('D');
				break;
			case KeyEvent.VK_LEFT:
				snake.setDirection('L');
				break;
			case KeyEvent.VK_RIGHT:
				snake.setDirection('R');
				break;
			}
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
	}

	@Override
	public void keyTyped(KeyEvent e) {
	}

	@Override
	public void mousePressed(MouseEvent e) {
	}

	@Override
	public void mouseReleased(MouseEvent e) {
	}

	@Override
	public void mouseEntered(MouseEvent e) {
	}

	@Override
	public void mouseExited(MouseEvent e) {
	}
}