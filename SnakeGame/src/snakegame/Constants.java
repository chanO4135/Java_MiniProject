package snakegame;

import java.awt.Color;

public class Constants {
	public static final int CELL_SIZE = 20;

	public static final Color BACKGROUND_COLOR = new Color(30, 30, 30); // 다크톤
	public static final Color GRID_COLOR = new Color(60, 60, 60); // 은은한 격자

	public static final Color SNAKE_HEAD_COLOR = new Color(0, 255, 127); // 연한 초록
	public static final Color SNAKE_BODY_COLOR = new Color(46, 139, 87); // 딥 그린

	public static final Color FOOD_COLOR = new Color(255, 99, 71); // 연한 붉은색
	public static final Color OBSTACLE_COLOR = new Color(100, 100, 255); // 부드러운 파란색

	public static final Color TEXT_COLOR = Color.WHITE;
	public static final int UI_HEIGHT = 3; // 상단 UI 영역의 셀 개수 (3줄)
	public static final int UI_AREA_X1 = 0; // UI 영역 x 시작
	public static final int UI_AREA_X2 = 4; // UI 영역 x 끝
	public static final int UI_AREA_Y1 = 0; // UI 영역 y 시작
	public static final int UI_AREA_Y2 = 2; // UI 영역 y 끝
}