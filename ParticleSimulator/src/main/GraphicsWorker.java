package main;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.concurrent.Callable;

@SuppressWarnings("rawtypes")
public class GraphicsWorker implements Callable<BufferedImage>{

	public Color[][] colors;
	int size;
	int width;
	int height;
	int xOffset;
	int yOffset;
	public GraphicsWorker(Color[][] c, int size,int x ,int y ,int w, int h){
		colors = c;
		this.size = size;
		width = w;
		height = h;
		this.xOffset = x;
		this.yOffset = y;
	}
	
	@Override
	public BufferedImage call() throws Exception {
		BufferedImage b = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		Graphics2D g = (Graphics2D)b.getGraphics();
		for(int i = 0; i < width;i++){
			for(int j = 0; j < height;j++){
				g.setColor(colors[i + xOffset][j + yOffset]);
				g.fillRect(i, j, size, size);
			}
		}
		return b;
	}

}
