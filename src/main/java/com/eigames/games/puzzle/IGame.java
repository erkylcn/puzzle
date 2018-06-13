package com.eigames.games.puzzle;

interface IGame {
	
	void moveUp(int x, int y);
	
	void moveLeft(int x, int y);
	
	void moveDown(int x, int y);
	
	void moveRight(int x, int y);
	
	void restart();
	
	void start();
	
	int getStepCount();
	
	boolean isGameFinished();

	void move(int partId);
}
