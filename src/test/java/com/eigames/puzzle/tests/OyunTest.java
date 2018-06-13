package com.eigames.puzzle.tests;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

import com.eigames.games.puzzle.Game;

public class OyunTest {

	@Test
	public void test() {

		Game game = new Game(3, 4);
		assertThat(game.isGameFinished()).isEqualTo(true);
		game.printBoard();

		// assertThat(game.isGameFinished()).isEqualTo(true);

		// assertThat(game.isGameFinished()).isEqualTo(true);

	}
}
