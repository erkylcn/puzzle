package com.eigames.games.puzzle;

public class Board {
	private int n;
	private int m;

	private ImagePart[][] board;

	public Board(int n, int m) {
		this.n = n;
		this.m = m;
		board = new ImagePart[n][m];
	}

	public int getN() {
		return n;
	}

	public void setN(int x) {
		n = x;
	}

	public int getM() {
		return m;
	}

	public void setM(int y) {
		m = y;
	}

	public void setImagePart(int x, int y, ImagePart im) {
		board[x][y] = im;
	}

	public ImagePart getImagePart(int x, int y) {
		return board[x][y];
	}

	public void printBoard() {
		for (int x = 0; x < n; x++) {
			for (int y = 0; y < m; y++) {
				ImagePart imagePart = board[x][y];
				System.out.print(imagePart != null ? imagePart.getPartId() + " " : "X ");
			}
			System.out.println();
		}
	}
}
