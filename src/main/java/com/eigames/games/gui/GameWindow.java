package com.eigames.games.gui;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;

import com.eigames.games.puzzle.Game;
import com.eigames.games.puzzle.GameListener;
import com.eigames.games.puzzle.ImagePart;
import com.eigames.games.util.ImageUtil;

public class GameWindow extends JFrame implements ActionListener, GameListener {

	private static final long serialVersionUID = 1L;
	private Game game;
	private JButton[] gameButtons;
	private JPanel gamePanel;

	public static void main(String[] args) throws IOException {
		new GameWindow(new Game(3, 3));
	}

	public GameWindow(Game game) {
		this.game = game;
		this.game.addListener(this);
		init();
		setDefaultCloseOperation(EXIT_ON_CLOSE);
	}

	private void init() {
		initMenu();
		initGameBoard();
		setLayout(new GridLayout(1, 1));
		setSize(game.getN() * 200, game.getM() * 200);
		setVisible(true);
	}

	private void initMenu() {
		JMenuBar menuBar = new JMenuBar();

		JMenu file = new JMenu("File");
		file.setMnemonic(KeyEvent.VK_F);

		JMenuItem menuItem = new JMenuItem("Load Image", KeyEvent.VK_T);
		menuItem.setMnemonic(KeyEvent.VK_L);
		menuItem.setToolTipText("Reload Image");
		menuItem.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				game.init();

			}
		});
		file.add(menuItem);

		menuBar.add(file);
		setJMenuBar(menuBar);
	}

	private void initGameBoard() {
		gamePanel = new JPanel();
		gamePanel.setLayout(new GridLayout(game.getN(), game.getM()));
		gameButtons = new JButton[game.getM() * game.getN()];
		for (int i = 0; i < game.getN(); i++) {
			for (int j = 0; j < game.getM(); j++) {
				ImagePart imagePart = game.getImagePart(i, j);
				JButton newButton = new JButton();
				newButton.setActionCommand(String.valueOf(imagePart.getPartId()));
				newButton.setHideActionText(true);
				// button.setSize(10, 10);
				try {
					BufferedImage image = ImageIO.read(getClass().getResource("/images/image1/" + imagePart.getPartId() + ".png"));
					newButton.setIcon(new ImageIcon(ImageUtil.resizeImage(image, 200, 200)));
				} catch (IOException e) {
					e.printStackTrace();
				}
				newButton.addActionListener(this);
				gamePanel.add(newButton);
				gameButtons[i * game.getM() + j] = newButton;
			}
		}
		getContentPane().add(gamePanel);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		game.move(Integer.valueOf(e.getActionCommand()).intValue());
	}

	private void repaintButtons() {
		for (int i = 0; i < game.getN(); i++) {
			for (int j = 0; j < game.getM(); j++) {
				ImagePart imagePart = game.getImagePart(i, j);
				int index = i * game.getM() + j;
				gameButtons[index].setActionCommand((String.valueOf(imagePart.getPartId())));
				BufferedImage image;
				try {
					image = ImageIO.read(getClass().getResource("/images/image1/" + imagePart.getPartId() + ".png"));
					gameButtons[index].setIcon(new ImageIcon(ImageUtil.resizeImage(image, 200, 200)));
				} catch (IOException e) {
					e.printStackTrace();
				}

			}
		}
	}

	@Override
	public void gameStatusChanged() {
		repaintButtons();
	}
}
