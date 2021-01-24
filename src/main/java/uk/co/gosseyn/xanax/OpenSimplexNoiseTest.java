package uk.co.gosseyn.xanax;

import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.*;

public class OpenSimplexNoiseTest
{
	private static final int WIDTH = 512;
	private static final int HEIGHT = 512;
	private static final double FEATURE_SIZE = 15;

	public static void main(String[] args)
		throws IOException {
		uk.co.gosseyn.xanax.OpenSimplexNoise noise =
				new uk.co.gosseyn.xanax.OpenSimplexNoise();
		BufferedImage image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
		double max = Double.NEGATIVE_INFINITY;
		double min = Double.POSITIVE_INFINITY;
		for (int y = 0; y < HEIGHT; y++)
		{
			for (int x = 0; x < WIDTH; x++)
			{
				double value = noise.eval(x / FEATURE_SIZE, y / FEATURE_SIZE, 0.0);
				int levels = 100;
				value = (value + 1) / 2; // make value fit in 0 to 1
				value = (int)(value * (levels + 1));
				value = value / levels * 2 - 1;
				if(value > max) {
					max = value;
				}
				if(value < min) {
					min = value;
				}
				if(value < 0) {
					//water level
					value = -1;
				}
				int rgb = 0x010101 * (int) ((value + 1) * 127.5);
				image.setRGB(x, y, rgb);
			}
		}
		System.out.println("min: "+min+" max: "+max);
		ImageIO.write(image, "png", new File("noise.png"));
	}
}