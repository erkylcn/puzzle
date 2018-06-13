package com.eigames.games.gui;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

import com.eigames.games.puzzle.Game;
import com.eigames.games.puzzle.GameListener;
import com.eigames.games.puzzle.ImagePart;
import com.eigames.games.puzzle.Options;
import com.eigames.games.puzzle.Options.BoardSize;
import com.eigames.games.util.ImageUtil;

public class GameWindow extends JFrame implements ActionListener, GameListener {

	private static final long serialVersionUID = 1L;
	private Game game;
	private JButton[] gameButtons;
	private JPanel gamePanel;
	private Options options = new Options();
	private ButtonGroup sizeButtonGroup;

	public static void main(String[] args) throws IOException {

		new GameWindow();
	}

	public GameWindow() {
		init();
		setDefaultCloseOperation(EXIT_ON_CLOSE);
	}

	private void init() {
		initMenu();
		initBoard();
		// initGameBoard();
		setLayout(new GridLayout(1, 1));
		options.setSelectedBoardSize(BoardSize.X33);
		setSize(options.getSelectedBoardSize().getN() * 200, options.getSelectedBoardSize().getM() * 200);
		// pack();
		setVisible(true);

	}

	private void initBoard() {
		JPanel optionsPanel = new JPanel();
		optionsPanel.add(new JLabel("Board Size:"));
		optionsPanel.setLayout(new GridLayout(4, 4));
		sizeButtonGroup = new ButtonGroup();
		for (Options.BoardSize size : Options.BoardSize.values()) {
			JRadioButton newButton = new JRadioButton(size.toString());
			newButton.setActionCommand(size.name());
			sizeButtonGroup.add(newButton);
			optionsPanel.add(newButton);
		}
		sizeButtonGroup.setSelected(sizeButtonGroup.getElements().nextElement().getModel(), true);

		JLabel imagePath = new JLabel("Image Path:");
		JLabel path = new JLabel();
		JButton openFileChooserButton = new JButton("Choose");
		openFileChooserButton.setSize(40, 40);
		openFileChooserButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				JFileChooser fc = new JFileChooser();
				FileFilter filter = new FileNameExtensionFilter("Image File(jpeg, jgp, png)", "jpg", "png", "jpeg");
				fc.setFileFilter(filter);
				int returnVal = fc.showOpenDialog(GameWindow.this);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					String absolutePath = fc.getSelectedFile().getAbsolutePath();
					path.setText(absolutePath);
					options.setSelectedImageUrl(absolutePath);
					options.setFileName(fc.getSelectedFile().getName());
				}
			}
		});

		optionsPanel.add(imagePath);
		optionsPanel.add(path);
		optionsPanel.add(openFileChooserButton);

		JButton startButton = new JButton("Start");
		startButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent actionEvent) {

				// crop image file and save for next use
				options.setSelectedBoardSize(BoardSize.valueOf(sizeButtonGroup.getSelection().getActionCommand()));
				try {
					ImageUtil.crop(options.getSelectedBoardSize().getN(), options.getSelectedBoardSize().getM(), options.getSelectedImageUrl());
				} catch (IOException e) {
					e.printStackTrace();
				}
				getContentPane().removeAll();
				game = new Game(options);
				game.addListener(GameWindow.this);
				initGameBoard();
			}
		});
		optionsPanel.add(startButton);
		getContentPane().add(optionsPanel);
	}

	private void initMenu() {
		JMenuBar menuBar = new JMenuBar();

		JMenu file = new JMenu("File");
		file.setMnemonic(KeyEvent.VK_F);

		JMenuItem restart = new JMenuItem("Restart", KeyEvent.VK_T);
		restart.setMnemonic(KeyEvent.VK_R);
		restart.setToolTipText("Restart");
		restart.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

			}
		});
		file.add(restart);

		JMenuItem options = new JMenuItem("Options", KeyEvent.VK_O);
		options.setMnemonic(KeyEvent.VK_O);
		options.setToolTipText("Options");
		options.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

			}
		});
		file.add(options);

		JMenuItem exit = new JMenuItem("Exit", KeyEvent.VK_E);
		exit.setMnemonic(KeyEvent.VK_E);
		exit.setToolTipText("Exit");
		exit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		file.add(exit);

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
				try {
					BufferedImage image = ImageIO.read(new File("images/ei/" + imagePart.getPartId() + ".png"));
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
		setVisible(true);// daha mantikli
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
					image = ImageIO.read(new File("images/ei/" + imagePart.getPartId() + ".png"));
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
