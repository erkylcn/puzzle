package com.eigames.games.util;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class ImageUtil implements ActionListener {
	public static void main(String... strings) throws IOException {
		ImageUtil.crop(3, 3, "/images/image1/bmw.jpg");
	}

	public static void crop(int n, int m, String url) throws IOException {
		BufferedImage image = ImageIO.read(ImageUtil.class.getResource(url));
		int height = image.getHeight();
		int width = image.getWidth();
		int croppedHeight = height / n;
		int croppedWidth = width / m;
		for (int y = 0; y < m; y++) {
			for (int x = 0; x < n; x++) {
				BufferedImage img = image.getSubimage(croppedWidth * y, croppedHeight * x, croppedWidth, croppedHeight);
				BufferedImage crop = new BufferedImage(croppedWidth, croppedHeight, BufferedImage.TYPE_INT_RGB);
				Graphics g = crop.createGraphics();
				g.drawImage(img, 0, 0, null);
				File outputfile = new File((x * m + y) + ".png");
				ImageIO.write(crop, "png", outputfile);
			}
		}
	}

	public static BufferedImage resizeImage(BufferedImage originalImage, int width, int height) {
		BufferedImage resizedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = resizedImage.createGraphics();
		g.drawImage(originalImage, 0, 0, width, height, null);
		g.dispose();
		return resizedImage;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		System.exit(0);
	}
}
