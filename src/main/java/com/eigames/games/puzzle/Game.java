package com.eigames.games.puzzle;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Game implements IGame {
	private Board board;
	private int stepCount;
	private ImagePart emptyImagePart;
	private Options gameOptions;
	private List<GameListener> listeners = new ArrayList<>();

	public Game(Options options) {
		board = new Board(options.getSelectedBoardSize().getN(), options.getSelectedBoardSize().getM());
		init();
	}

	public void addListener(GameListener listener) {
		listeners.add(listener);
	}

	public void init() {
		stepCount = 0;

		List<ImagePart> ipList = new ArrayList<>();
		for (int i = 0; i < board.getN() * board.getM(); i++) {
			ipList.add(new ImagePart("", i));
		}

		Collections.shuffle(ipList);

		for (int x = 0; x < board.getN(); x++) {
			for (int y = 0; y < board.getM(); y++) {
				board.setImagePart(x, y, ipList.get(x * board.getM() + y));
			}
		}
		emptyImagePart = board.getImagePart(0, 0);
		board.setImagePart(0, 0, new ImagePart("", -1));
		for (GameListener l : listeners) {
			l.gameStatusChanged();
		}
		System.out.println("Game Started");
	}

	@Override
	public void moveUp(int x, int y) {
		if (x != 0) {
			board.setImagePart(x - 1, y, board.getImagePart(x, y));
			board.setImagePart(x, y, null);
			stepCount++;
		}
	}

	@Override
	public void moveDown(int x, int y) {
		if (x != board.getN() - 1) {
			board.setImagePart(x + 1, y, board.getImagePart(x, y));
			board.setImagePart(x, y, null);
			stepCount++;
		}
	}

	@Override
	public void moveLeft(int x, int y) {
		if (y != 0) {
			board.setImagePart(x, y - 1, board.getImagePart(x, y));
			board.setImagePart(x, y, null);
			stepCount++;
		}
	}

	@Override
	public void moveRight(int x, int y) {
		if (y != board.getM() - 1) {
			board.setImagePart(x, y + 1, board.getImagePart(x, y));
			board.setImagePart(x, y, new ImagePart("", -1));
			stepCount++;
		}
	}

	@Override
	public boolean isGameFinished() {
		int emptyX = 0, emptyY = 0;
		for (int x = 0; x < board.getN(); x++) {
			for (int y = 0; y < board.getM(); y++) {
				ImagePart imagePart = board.getImagePart(x, y);
				if (!imagePart.isEmpty() && imagePart.getPartId() != board.getM() * x + y) {
					return false;
				}
				if (imagePart.isEmpty()) {
					emptyX = x;
					emptyY = y;
				}
			}
		}
		board.setImagePart(emptyX, emptyY, emptyImagePart);
		return true;
	}

	private void move(int x, int y) {
		ImagePart ip = board.getImagePart(x, y);

		boolean moved = true;
		if (x - 1 >= 0 && board.getImagePart(x - 1, y).isEmpty()) {
			board.setImagePart(x - 1, y, ip);
		}
		else if (y - 1 >= 0 && board.getImagePart(x, y - 1).isEmpty()) {
			board.setImagePart(x, y - 1, ip);
		}
		else if (x + 1 < board.getN() && board.getImagePart(x + 1, y).isEmpty()) {
			board.setImagePart(x + 1, y, ip);
		}
		else if (y + 1 < board.getM() && board.getImagePart(x, y + 1).isEmpty()) {
			board.setImagePart(x, y + 1, ip);
		}
		else {
			moved = false;
			System.out.println("Can't Move x=" + x + ", y=" + y);
		}

		if (moved) {
			System.out.println("Move x=" + x + ", y=" + y + ", stepcount=" + ++stepCount);
			board.setImagePart(x, y, new ImagePart("", -1));
			board.printBoard();
			isGameFinished();
			for (GameListener l : listeners) {
				l.gameStatusChanged();
			}
		}
	}

	@Override
	public void move(int partId) {
		for (int x = 0; x < board.getN(); x++) {
			for (int y = 0; y < board.getM(); y++) {
				ImagePart imagePart = board.getImagePart(x, y);
				if (partId == imagePart.getPartId()) {
					move(x, y);
					return;
				}
			}
		}
	}

	@Override
	public void restart() {

	}

	@Override
	public void start() {

	}

	@Override
	public int getStepCount() {
		return 0;
	}

	public void printBoard() {
		board.printBoard();
	}

	public int getN() {
		return board.getN();
	}

	public int getM() {
		return board.getM();
	}

	public ImagePart getImagePart(int x, int y) {
		return board.getImagePart(x, y);
	}
}
