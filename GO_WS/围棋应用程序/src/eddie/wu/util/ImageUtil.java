package eddie.wu.util;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.BufferedOutputStream;
import java.io.FileOutputStream;

//TODO: find replacement of IMAGE API.
//import com.sun.image.codec.jpeg.JPEGCodec;
//import com.sun.image.codec.jpeg.JPEGImageEncoder;

import eddie.wu.manual.StateLoader;

public class ImageUtil {
//	public ImageUtil(int boardSize){
//		columns = boarSize-1;
//		
//	}
	
	public ImageUtil(){
		//columns = 8;
	}
	
	int diameter = 28;// 交叉点之间的宽度
	int topLeft = diameter / 2 + 4; // 左上角的坐标18
	int columns;// = 8;
	int imageWidth;
	int imageHeight;

	public void textManualToImage(String filePath, String fileLocation) {
		byte[][] state = StateLoader.LoadStateFromTextFile(filePath);
		int boardSize = state.length-2;
		columns = boardSize;
		
		imageWidth = topLeft * 2 + diameter * (columns-1);
		imageHeight = imageWidth;
		BufferedImage image;
		image = new BufferedImage(imageWidth, imageHeight,
				BufferedImage.TYPE_INT_RGB);
		Graphics graphics = image.getGraphics();
		graphics.setColor(Color.ORANGE);
		graphics.fillRect(0, 0, imageWidth, imageHeight);
		graphics.setColor(Color.BLACK);
		
		
		BoardGraphic.drawBlankBoard(graphics, boardSize);
		BoardGraphic.drawPoints(graphics, state);
		// graphics.drawRect(X + 1 * columnWidth, columnHeight - h1,
		// columnWidth,
		// h1);
		// graphics.drawRect(X + 2 * columnWidth, columnHeight - h2,
		// columnWidth,
		// h2);
		// graphics.drawRect(X + 3 * columnWidth, columnHeight - h3,
		// columnWidth,
		// h3);
		// graphics.drawRect(X + 4 * columnWidth, columnHeight - h4,
		// columnWidth,
		// h4);
		// graphics.drawRect(X + 5 * columnWidth, columnHeight - h5,
		// columnWidth,
		// h5);
		// createImage("D://Temp//chart.jpg");

		try {
			FileOutputStream fos = new FileOutputStream(fileLocation);
			BufferedOutputStream bos = new BufferedOutputStream(fos);
			
//			JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(bos);
//			encoder.encode(image);
			bos.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public static void main(String[] args) {
		new ImageUtil().test();
		new ImageUtil().test2();
	}

	public void test() {
		String inname = "doc/围棋程序数据/大眼基本死活/角上曲四打劫状态.txt";
//		byte[][] state = StateLoader.LoadStateFromTextFile(inname);

		String fileLocation = "doc/围棋程序数据/大眼基本死活/角上曲四打劫状态.JPG";
		;
		this.textManualToImage(inname, fileLocation);
	}
	
	public void test2() {
		String inname = "doc/围棋程序数据/大眼基本死活/二手劫状态变化图.txt";
//		byte[][] state = StateLoader.LoadStateFromTextFile(inname);

		String fileLocation = "doc/围棋程序数据/大眼基本死活/二手劫状态变化图.JPG";
		;
		this.textManualToImage(inname, fileLocation);
	}
	

}
