package com.eigames.games.puzzle;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import com.eigames.games.gui.GameWindow;

public class GameController implements ActionListener {

	public GameController(GameWindow wiew, Game game) {
		GameWindow view = new GameWindow(new Game(3, 3));
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub

	}
}
