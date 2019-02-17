import java.awt.Component;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.io.File;
import java.util.Arrays; 


public class ReadPixel extends Component {
	
	public String[][] stringArray;
	public int[][] binaryArray;

	public static void main(String[] foo) {
		new ReadPixel();
	}
	

	
	private void marchThroughImage(BufferedImage image) {
		
		int w = image.getWidth();
		int h = image.getHeight();
		
		stringArray = new String[w][h];
		binaryArray = new int[w][h];
		
		System.out.println("width, height: " + w + ", " + h);
		
		for(int i = 0; i < h; i++) {
			for(int j = 0; j < w; j++) {
				System.out.println("x, y: " + j + ", " + i);
				int pixel = image.getRGB(j, i);
				stringArray[i][j] = getColor(pixel);
				binaryArray[i][j] = populateBinaryArray(pixel);
			}		
		}
		
		// Print out Array in viewable format. 
		System.out.println(Arrays.deepToString(stringArray).replace("], ", "]\n"));
		System.out.println(Arrays.deepToString(binaryArray).replace("], ", "]\n"));

		drawBorders(binaryArray, w, h);

		System.out.println(Arrays.deepToString(binaryArray).replace("], ", "]\n"));


	}

	private void drawBorders(int[][] binaryArray, int w, int h) {

		// Check all 8 positions around each pixel. We call isValid to assure that we're in bounds and that the number we're checking was checked to be a border pixel 
		// If the white pixel shares a border with a black pixel, we provide this edge the number two to identify it. 
		for(int i = 0; i < h; i++) {
			for(int j = 0; j < w; j++) {
				if(isValid(binaryArray, i, j, w, h)) { 
					if(binaryArray[i-1][j-1] == 0 || binaryArray[i-1][j] == 0 || binaryArray[i-1][j+1] == 0 
												  || binaryArray[i][j-1] == 0 || binaryArray[i][j+1] == 0 
												  || binaryArray[i+1][j-1] == 0 || binaryArray[i+1][j] == 0 
												  || binaryArray[i+1][j+1] == 0) {
							binaryArray[i][j] = 2;
					   } 
				}
			}		
		}

		// In our second pass, we go through each pixel and check to see if it was identified as a border pixel. If so, we'll re-convert it back to binary. 
	    // Otherwise we'll set it to be a zero to make our life easier. 
		for(int i = 0; i < h; i++) {
			for(int j = 0; j < w; j++) {
				if(binaryArray[i][j] != 2) {
					binaryArray[i][j] = 0;
				} else {
					binaryArray[i][j] = 1;
				}
					
			}		
		}


	}

	// Check to see if we're in bounds, and if the number we have has been set to a 1. 
	boolean isValid(int maze[][], int x, int y, int w, int h) { 
        return (x >= 0 && x < w && y >= 0 && y < h && maze[x][y] == 1); 
    } 
  
	
	private String getColor(int pixel) {
		int alpha = (pixel >> 24) & 0xff;
		int red = (pixel >> 16) & 0xff;
		int green = (pixel >> 8) & 0xff;
		int blue = (pixel) & 0xff;
		
		String color = " ";
		
		if(red == 0 && green == 0 && blue == 0)
			color = "B";
		
		return color;
	}

	private int populateBinaryArray(int pixel) {
		int alpha = (pixel >> 24) & 0xff;
		int red = (pixel >> 16) & 0xff;
		int green = (pixel >> 8) & 0xff;
		int blue = (pixel) & 0xff;
		
		int color = 0;
		
		if(red == 0 && green == 0 && blue == 0) {
			color = 1;
		}
			
		return color;
	}
		
	public ReadPixel() {
		try {
			//get the BufferedImage, using the ImageIO class
			BufferedImage image = ImageIO.read(this.getClass().getResource("in.png"));
			marchThroughImage(image);
		} catch (IOException e) {
			System.err.println(e.getMessage());
		}
	}
}
	
	