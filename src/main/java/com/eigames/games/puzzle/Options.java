package com.eigames.games.puzzle;

public class Options {

	private BoardSize selectedBoardSize;
	private String selectedImageUrl;
	private String fileName;

	public BoardSize getSelectedBoardSize() {
		return selectedBoardSize;
	}

	public void setSelectedBoardSize(BoardSize selectedBoardSize) {
		this.selectedBoardSize = selectedBoardSize;
	}

	public String getSelectedImageUrl() {
		return selectedImageUrl;
	}

	public void setSelectedImageUrl(String selectedImageUrl) {
		this.selectedImageUrl = selectedImageUrl;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public enum BoardSize {
		X33(3, 3, "3X3"), X34(3, 4, "3X4"), X44(4, 4, "4X4"), X45(4, 5, "4X5"), X55(5, 5, "5X5"), X2020(20, 20, "20X20");

		int n;
		int m;
		String text;

		private BoardSize(int n, int m, String text) {
			this.n = n;
			this.m = m;
			this.text = text;
		}

		public int getN() {
			return n;
		}

		public int getM() {
			return m;
		}

		@Override
		public String toString() {
			return text;
		}
	}
}
