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

		for (int i = 0; i < h; i++) {
			for (int j = 0; j < w; j++) {
				//System.out.println("x, y: " + j + ", " + i);
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

		indexOfFirstOne(w, h);

	}

	private void drawBorders(int[][] binaryArray, int w, int h) {

		// Check all 8 positions around each pixel. We call isValid to assure that we're
		// in bounds and that the number we're checking was checked to be a border pixel
		// If the white pixel shares a border with a black pixel, we provide this edge
		// the number two to identify it.
		for (int i = 0; i < h; i++) {
			for (int j = 0; j < w; j++) {
				if (isValid(binaryArray, i, j, w, h)) {
					if (binaryArray[i - 1][j - 1] == 0 || binaryArray[i - 1][j] == 0 || binaryArray[i - 1][j + 1] == 0
							|| binaryArray[i][j - 1] == 0 || binaryArray[i][j + 1] == 0
							|| binaryArray[i + 1][j - 1] == 0 || binaryArray[i + 1][j] == 0
							|| binaryArray[i + 1][j + 1] == 0) {
						binaryArray[i][j] = 2;
					}
				}
			}
		}

		// In our second pass, we go through each pixel and check to see if it was
		// identified as a border pixel. If so, we'll re-convert it back to binary.
		// Otherwise we'll set it to be a zero to make our life easier.
		for (int i = 0; i < h; i++) {
			for (int j = 0; j < w; j++) {
				if (binaryArray[i][j] != 2) {
					binaryArray[i][j] = 0;
				} else {
					binaryArray[i][j] = 1;
				}
			}
		}
	}

	// Check to see if we're in bounds, and if the number we have has been set to a
	// 1.
	boolean isValid(int maze[][], int x, int y, int w, int h) {
		return (x >= 0 && x < w && y >= 0 && y < h && maze[x][y] == 1);
	}

	public int indexOfFirstOne(int w, int h) {
		System.out.println("Calling Index");
		// traverse the array from left to right
		outerloop:
		for (int i = 0; i < w; i++) {
			for (int j = 0; j < w; j++) {
				// When first 1 is found.
				if (binaryArray[i][j] == 1) {
					System.out.println("First pixel identified at: " + i + ", " + j);
					generateChainCode(i, j, w, h);
					break outerloop;
				}
			}
		}
		// 1's are not present in the array
		return -1;
	}

	// Driver to calculate chain code.
	// Calculate by looking in a clockwise rotation.
	public void generateChainCode(int row, int col, int w, int h) {
		System.out.println("Calculating Chaincode . . .");
		
		int startRow = row;
		int startCol = col;

		int temprow = row;
		int tempcol = col; 

		String chaincode = "";

		while(true) {
			System.out.println(Arrays.deepToString(binaryArray).replace("], ", "]\n"));
			System.out.println("Before: " + row + ", " + col);

				if (binaryArray[startRow][startCol + 1] == 1) { // direction 0 - NORTH
					System.out.println(0 + " North");
					binaryArray[startRow][startCol + 1] = 2;
					chaincode += 0;
					startCol += 1;
				} else if (binaryArray[startRow + 1][startCol + 1] == 1) { // direction 1 - NORTH EAST
					System.out.println(1 + " North East");
					binaryArray[startRow + 1][startCol + 1] = 2;
					chaincode += 1;
					startRow -= 1;
					startCol += 1;
				} else if (binaryArray[startRow - 1][startCol] == 1) { // direction 2 - EAST)
					System.out.println(2 + " East");
					binaryArray[startRow - 1][startCol] = 2;
					chaincode += 2;
					startRow -= 1;
				} else if (binaryArray[startRow - 1][startCol + 1] == 1) { // direction 3 - SOUTH EAST
					System.out.println(3 + " South East");
					binaryArray[startRow - 1][startCol + 1] = 2;
					chaincode += 3;
					startRow -= 1;
					startCol -= 1;
				} else if (binaryArray[startRow][startCol - 1] == 1) { // direction 4 - SOUTH
					System.out.println(4 + "South");
					binaryArray[startRow][startCol - 1] = 2;
					chaincode += 4;
					startCol -= 1; 
				} else if (binaryArray[startRow - 1][startCol - 1] == 1) { // direction 5 - SOUTH WEST
					System.out.println(5 + " South West");
					binaryArray[startRow - 1][startCol - 1] = 2;
					chaincode += 5;
					startRow += 1;
					startCol -= 1; 
				} else if (binaryArray[startRow + 1][startCol] == 1) { // direction 6 - WEST
					System.out.println(6 + "West");
					binaryArray[startRow + 1][startCol] = 2; 
					chaincode += 6;
					startRow += 1; 
				} else if (binaryArray[startRow + 1][startCol - 1] == 1) { // direction 7 - NORTH WEST
					System.out.println(7 + " North West");
					binaryArray[startRow + 1][startCol - 1] = 2;
					chaincode += 7;
					startRow += 1;
					startCol += 1;
				} else {
					System.out.println("OH SHIT");
				}

				System.out.println("\nAfter: " + row + ", " + col);
				System.out.println("Chaincode: " + chaincode);

				if (startRow == temprow && startCol == tempcol)
					break;
		}

				

		System.out.println(chaincode);

	}

	// Used to create a visual representation of the number in String format.
	// Not useful for the machine learning, but useful for human validation.
	private String getColor(int pixel) {
		int alpha = (pixel >> 24) & 0xff;
		int red = (pixel >> 16) & 0xff;
		int green = (pixel >> 8) & 0xff;
		int blue = (pixel) & 0xff;

		String color = " ";

		if (red == 0 && green == 0 && blue == 0)
			color = "B";

		return color;
	}

	// Used to populate a binary array of the image data for machine interpretation.
	private int populateBinaryArray(int pixel) {
		int alpha = (pixel >> 24) & 0xff;
		int red = (pixel >> 16) & 0xff;
		int green = (pixel >> 8) & 0xff;
		int blue = (pixel) & 0xff;

		int color = 0;

		if (red == 0 && green == 0 && blue == 0) {
			color = 1;
		}

		return color;
	}

	public ReadPixel() {
		try {
			// get the BufferedImage, using the ImageIO class
			BufferedImage image = ImageIO.read(this.getClass().getResource("in.png"));
			marchThroughImage(image);
		} catch (IOException e) {
			System.err.println(e.getMessage());
		}
	}
}
