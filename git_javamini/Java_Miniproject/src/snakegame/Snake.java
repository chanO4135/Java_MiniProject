package snakegame;

import java.awt.Point;
import java.util.LinkedList;

public class Snake implements Movable {
	private LinkedList<Point> body; // 뱀의 몸통 좌표를 저장하는 리스트
	private char direction; // 현재 이동 방향 ('U':상, 'D':하, 'L':좌, 'R':우)
	private ObstacleManager obstacleManager; // 장애물 관리자 참조

	// 생성자 (장애물 관리자 전달 버전)
	public Snake(ObstacleManager obstacleManager) {
		this(); // 기본 생성자 호출
		this.obstacleManager = obstacleManager; // 장애물 관리자 설정
	}

	// 기본 생성자
	public Snake() {
		this.body = new LinkedList<>();
		this.body.add(new Point(5, 5)); // 시작 위치 (5,5)에 머리 생성
		this.direction = 'R'; // 초기 방향: 오른쪽
	}

	// 이동 메인 메서드
	@Override
	public void move() throws InvalidMoveException {
		Point newHead = calculateNewHeadPosition(); // 새 머리 위치 계산

		checkWallCollision(newHead); // 벽 충돌 검사
		checkBodyCollision(newHead); // 자기 몸 충돌 검사
		checkObstacleCollision(newHead); // 장애물 충돌 검사

		updateBodyPosition(newHead); // 충돌 없으면 위치 업데이트
	}

	// 새 머리 위치 계산
	private Point calculateNewHeadPosition() {
		Point newHead = new Point(getHead()); // 현재 머리 복사
		switch (direction) {
		case 'U':
			newHead.y--;
			break; // 위로 이동
		case 'D':
			newHead.y++;
			break; // 아래로 이동
		case 'L':
			newHead.x--;
			break; // 왼쪽으로 이동
		case 'R':
			newHead.x++;
			break; // 오른쪽으로 이동
		}
		return newHead;
	}

	// 벽 충돌 검사 (UI 영역 + 게임 경계)
	private void checkWallCollision(Point newHead) throws InvalidMoveException {
		// 기존 UI 영역 검사 코드 삭제 → 뱀은 UI 영역 통과 가능
		if (newHead.x < 0 || newHead.x >= 30 || newHead.y < 0 || newHead.y >= 30) {
			throw new InvalidMoveException("게임 경계를 벗어났습니다!");
		}
	}

	// 자기 몸 충돌 검사
	private void checkBodyCollision(Point newHead) throws InvalidMoveException {
		if (body.contains(newHead)) {
			throw new InvalidMoveException("뱀의 몸에 부딪혔습니다!");
		}
	}

	// 장애물 충돌 검사
	private void checkObstacleCollision(Point newHead) throws InvalidMoveException {
		if (obstacleManager != null && obstacleManager.isObstacle(newHead)) {
			throw new InvalidMoveException("장애물에 부딪혔습니다!");
		}
	}

	// 뱀 위치 업데이트
	private void updateBodyPosition(Point newHead) {
		body.addFirst(newHead); // 새 머리 추가
		body.removeLast(); // 꼬리 제거
	}

	// 뱀 성장 메서드 (먹이 먹었을 때)
	public void grow() {
		body.add(new Point(body.getLast())); // 꼬리 위치에 새 몸통 추가
	}

	// 게임 리셋 메서드
	public void reset() {
		body.clear(); // 몸통 초기화
		body.add(new Point(5, 5)); // 시작 위치로 재설정
		direction = 'R'; // 방향 초기화
	}

	// ===== 게터/세터 =====//
	public void setDirection(char dir) {
		direction = dir; // 이동 방향 설정
	}

	public LinkedList<Point> getBody() {
		return body; // 몸통 좌표 반환
	}

	public Point getHead() {
		return body.getFirst(); // 머리 좌표 반환
	}
}