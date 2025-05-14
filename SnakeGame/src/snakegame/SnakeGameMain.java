package snakegame;

import javax.swing.*;

public class SnakeGameMain {
	public static void main(String[] args) {
		JFrame frame = new JFrame("지렁이 게임");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setContentPane(new GamePanel1());
		frame.pack();
		frame.setVisible(true);
	}
}