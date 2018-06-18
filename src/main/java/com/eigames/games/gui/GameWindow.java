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
	private JPanel optionsPanel;

	public static void main(String[] args) throws IOException {

		new GameWindow();
	}

	public GameWindow() {
		init();
		setDefaultCloseOperation(EXIT_ON_CLOSE);
	}

	private void init() {
		initMenu();
		initView();
		setSize(606, 658);
		// setSize(800, 658);
		setResizable(false);
		setVisible(true);
	}

	private void initView() {
		optionsPanel = new JPanel();
		optionsPanel.add(new JLabel("Board Size:"));
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
				options.setSelectedBoardSize(BoardSize.valueOf(sizeButtonGroup.getSelection().getActionCommand()));
				try {
					ImageUtil.crop(options.getSelectedBoardSize().getN(), options.getSelectedBoardSize().getM(), options.getSelectedImageUrl());
				} catch (IOException e) {
					e.printStackTrace();
				}
				GameWindow.this.startGame();
			}
		});
		optionsPanel.add(startButton);
		getContentPane().add(optionsPanel);

	}

	private void startGame() {
		game = new Game(options);
		game.addListener(GameWindow.this);
		initGameBoard();
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
				GameWindow.this.startGame();
			}
		});
		file.add(restart);

		JMenuItem options = new JMenuItem("Options", KeyEvent.VK_O);
		options.setMnemonic(KeyEvent.VK_O);
		options.setToolTipText("Options");
		options.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				getContentPane().removeAll();
				getContentPane().add(optionsPanel);
				getContentPane().repaint();

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
		gamePanel.setSize(600, 600);
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
					newButton.setIcon(new ImageIcon(ImageUtil.resizeImage(image, gamePanel.getWidth() / options.getSelectedBoardSize().getM(), gamePanel.getHeight() / options.getSelectedBoardSize().getN())));
				} catch (IOException e) {
					e.printStackTrace();
				}
				newButton.setSize(gamePanel.getWidth() / options.getSelectedBoardSize().getM(), gamePanel.getHeight() / options.getSelectedBoardSize().getN());
				newButton.addActionListener(this);
				gamePanel.add(newButton);
				gameButtons[i * game.getM() + j] = newButton;
			}
		}
		getContentPane().removeAll();
		getContentPane().add(gamePanel);
		getContentPane().repaint();
		setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		JButton button = (JButton) e.getSource();
		System.out.println("button size = " + button.getSize() + " window size = " + size() + " panel size = " + gamePanel.size());
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
					gameButtons[index].setIcon(new ImageIcon(ImageUtil.resizeImage(image, gamePanel.getWidth() / options.getSelectedBoardSize().getM(), gamePanel.getHeight() / options.getSelectedBoardSize().getN())));

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
