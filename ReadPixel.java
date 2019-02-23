import java.awt.Component;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.io.File;
import java.util.Arrays;

public class ReadPixel extends Component {

	public String[][] stringArray;
	public int[][] binaryArray;
	int imageWidth;
	int imageHeight;

	public static void main(String[] foo) {

		System.out.println(" ---------------------------------- ");
		System.out.println(" 	 STARTING IMAGE DETECTION       ");
		System.out.println(" ---------------------------------- ");

		new ReadPixel();
	}


	public ReadPixel() {
		try {

			// get the BufferedImage, using the ImageIO class
			BufferedImage image = ImageIO.read(this.getClass().getResource("in.png"));
			marchThroughImage(image);

		} catch (IOException e) {
			System.out.println(" ---------------------------------- ");
			System.out.println("    PROBLEMS DETECTED - SEE BELOW   ");
			System.out.println(" ---------------------------------- ");
			System.err.println(e.getMessage());
		}
	}


	// Traverse through image to binarize image data. This method
	// generates both a String array (for human visualization), 
	// and a binary array for machine use. 

	private void marchThroughImage(BufferedImage image) {

		imageWidth = image.getWidth();  //image width
		imageHeight = image.getHeight(); //image height

	
		stringArray = new String[imageWidth][imageHeight];
		binaryArray = new int[imageWidth][imageHeight];


		// Traverses through image, determines color of pixel and
		// assigns the value to both our String and Binary array. 

		for (int i = 0; i < imageHeight; i++) {
			for (int j = 0; j < imageWidth; j++) {
				int pixel = image.getRGB(j, i);
				stringArray[i][j] = getColor(pixel);
				binaryArray[i][j] = populateBinaryArray(pixel);
			}
		}
		
		//drawBorders(binaryArray);
		
		QArray qArray = new QArray(binaryArray);

		// 9 Quadrants of our image. 
		int[][] Q1 = qArray.getQuadrant(1);
		int[][] Q2 = qArray.getQuadrant(2);
		int[][] Q3 = qArray.getQuadrant(3);
		int[][] Q4 = qArray.getQuadrant(4);
		int[][] Q5 = qArray.getQuadrant(5);
		int[][] Q6 = qArray.getQuadrant(6);
		int[][] Q7 = qArray.getQuadrant(7);
		int[][] Q8 = qArray.getQuadrant(8);
		int[][] Q9 = qArray.getQuadrant(9);

		print(Q1, "Q1");
		indexOfFirstOne(Q1);
		print(Q2, "Q2");
		indexOfFirstOne(Q2);
		print(Q3, "Q3");
		indexOfFirstOne(Q3);
		print(Q4, "Q4");
		indexOfFirstOne(Q4);
		print(Q5, "Q5");
		indexOfFirstOne(Q5);
		print(Q6, "Q6");
		indexOfFirstOne(Q6);
		print(Q7, "Q7");
		indexOfFirstOne(Q7);
		print(Q8, "Q8");
		indexOfFirstOne(Q8);
		print(Q9, "Q9");
		indexOfFirstOne(Q9);

	}


	private int[][] drawBorders(int[][] quadrantArray) {

		// Check all 8 positions around each pixel. We call isValid to assure that we're
		// in bounds and that the number we're checking was checked to be a border pixel
		// If the white pixel shares a border with a black pixel, we provide this edge
		// the number two to identify it.

		for (int i = 0; i < quadrantArray.length; i++) {
			for (int j = 0; j < quadrantArray[0].length; j++) {
				if(quadrantArray[i][j] == 1 && (i == 0 || j == 0 || i == quadrantArray.length - 1 || j == quadrantArray[i].length - 1)) {
					quadrantArray[i][j] = 2;
				}else if (isValid(quadrantArray, i, j)) {

					
					if (quadrantArray[i - 1][j - 1] == 0 || 
						quadrantArray[i - 1][j] == 0 || 
						quadrantArray[i - 1][j + 1] == 0 || 
						quadrantArray[i][j - 1] == 0 || 
						quadrantArray[i][j + 1] == 0 || 
						quadrantArray[i + 1][j - 1] == 0 || 
						quadrantArray[i + 1][j] == 0 || 
						quadrantArray[i + 1][j + 1] == 0) {

							quadrantArray[i][j] = 2;
							//print(quadrantArray);
					}
				} 	
			}
		}

		//System.out.println(" Binary Build, Determining Borders: \n");
		//System.out.println(
		//Arrays.deepToString(this.binaryArray).replace("], ", "]\n").replace("[[", "[").replace("]]", "]\n"));


		// In our second pass, we go through each pixel and check to see if it was
		// identified as a border pixel. If so, we'll re-convert it back to binary.
		// Otherwise we'll set it to be a zero to make our life easier.

		for (int i = 0; i < quadrantArray.length; i++) {
			for (int j = 0; j < quadrantArray[0].length; j++) {
				if (quadrantArray[i][j] != 2) {
					quadrantArray[i][j] = 0;
				} else {
					quadrantArray[i][j] = 1;
				}

			} 
		}


		System.out.println("Just an ol testerino");
		printArrays(quadrantArray);

		// In our third pass, we go through each pixel and check to see if the current
		// pixel is an 'dead' pixel. (AKA, a pixel that shares an unnecessary border)
		// This is determined by checking its' neighbors and seeing if they already 
		// have a defined border assigned. 

			for (int row = 0; row < 10; row++) {
				for (int col = 0; col < 10; col++) {
					try {
						
						if(isValid2(quadrantArray, row, col + 1) && isValid2(quadrantArray, row + 1, col)) {

							if(quadrantArray[row][col+1] == 1 /* E */ && quadrantArray[row+1][col] == 1 /* S */) {
								System.out.println(1);
								System.out.println("Removing pixel at: " + row + ", " + col);
								
								quadrantArray[row][col] = 0;
							}
						}

		

						if(isValid2(quadrantArray, row, col - 1) && isValid2(quadrantArray, row + 1, col)) {
							if(quadrantArray[row][col - 1] == 1  /* W */ && quadrantArray[row + 1][col] == 1 /* S */) {
								System.out.println(2);
								System.out.println("Removing pixel at: " + row + ", " + col);

								quadrantArray[row][col] = 0;
						}
					}



						if(isValid2(quadrantArray, row - 1, col) && isValid2(quadrantArray, row, col - 1)) {
							if(quadrantArray[row - 1][col] == 1 /* N */ && quadrantArray[row][col - 1] == 1 /*W */) {
								System.out.println(3);
								System.out.println("Removing pixel at: " + row + ", " + col);

							quadrantArray[row][col] = 0;
						}
					}

						if(isValid2(quadrantArray, row - 1, col) && isValid2(quadrantArray, row, col + 1)) {
							if(quadrantArray[row-1][col] == 1 /* N */ && quadrantArray[row][col+1] == 1 /* E */) {
							System.out.println(4);
							System.out.println("Removing pixel at: " + row + ", " + col);

							quadrantArray[row][col] = 0;
						}
					}
						
					
				} catch(Exception IndexOutOfBoundsException) {
					System.out.println("We're out of bounds.");
					continue;
				}
				}		
			}

			System.out.println("Just an ol testerino 2");
			printArrays(quadrantArray);

			return quadrantArray;
	}

	public int indexOfFirstOne(int[][] quadrantArray) {
		boolean pixelExists = false; 
		System.out.println("\n Determining Start Location...");

		outerloop: 
		for (int i = 0; i < quadrantArray.length; i++) {
			for (int j = 0; j < quadrantArray.length; j++) {
				// When first 1 is found.
				if (quadrantArray[i][j] == 1) {
					System.out.println(" Black pixel identified at: [" + i + ", " + j + "]");
					generateChainCode(i, j, quadrantArray);
					pixelExists = true;
					break outerloop;
				}
			}
		}

		// Edge case where the image is entirely white
		// or no black pixels are detected.
		if(!pixelExists)
			System.out.println("No pixels were found in this quadrant!");
			
		return -1;
	}


		// Check to see if we're in bounds, and if the number we have has been set to a 1.
	boolean isValid(int quadrantArray[][], int x, int y) { 
		return (x >= 0 && x < quadrantArray.length && y >= 0 && y < quadrantArray[0].length && quadrantArray[x][y] == 1); 
	} 

	boolean isValid2(int quadrantArray[][], int x, int y) { 
        return (x >= 0 && x < quadrantArray.length && y >= 0 && y < quadrantArray[0].length); 
    } 



	// Driver to calculate chain code. Calculates by looking in 
	// a clockwise rotation.

	public void generateChainCode(int row, int col, int[][] quadrantArray) {
		System.out.println(" Calculating Chaincode ... \n");

		int startRow = row;
		int startCol = col;
		String chaincode = "";

		quadrantArray = drawBorders(quadrantArray);


		while (true) {

			if (isValid2(quadrantArray, startRow, startCol + 1) && quadrantArray[startRow][startCol + 1] == 1) { 			// direction 0 - EAST)
				System.out.println("Moving East. Adding [0] to Chaincode.");
				quadrantArray[startRow][startCol + 1] = 2;
				chaincode += 0;
				startCol += 1; 
			} else if (isValid2(quadrantArray, startRow - 1, startCol + 1) && quadrantArray[startRow - 1][startCol + 1] == 1) {  // direction 1 - NORTH EAST
				System.out.println("Moving North East. Adding [1] to Chaincode.");
				quadrantArray[startRow - 1][startCol + 1] = 2;
				chaincode += 1;
				startRow -= 1;
				startCol += 1;
			} else if (isValid2(quadrantArray, startRow - 1, startCol) && quadrantArray[startRow - 1][startCol] == 1) { 	   // direction 2 - NORTH
				System.out.println("Moving North. Adding [2] to Chaincode.");
				quadrantArray[startRow - 1][startCol] = 2;
				chaincode += 2;
				startRow -= 1;
			} else if (isValid2(quadrantArray, startRow - 1, startCol - 1) && quadrantArray[startRow - 1][startCol - 1] == 1) { // direction 3 - NORTH WEST
				System.out.println("Moving North West. Adding [3] to Chaincode.");
				quadrantArray[startRow - 1][startCol - 1] = 2;
				chaincode += 3;
				startRow -= 1;
				startCol -= 1;
			} else if (isValid2(quadrantArray, startRow, startCol - 1) && quadrantArray[startRow][startCol - 1] == 1) { 	   // direction 4 - WEST
				System.out.println("Moving West. Adding [4] to Chaincode.");
				quadrantArray[startRow][startCol - 1] = 2;
				chaincode += 4;
				startCol -= 1;
			} else if (isValid2(quadrantArray, startRow + 1, startCol - 1) && quadrantArray[startRow + 1][startCol - 1] == 1) { // direction 5 - SOUTH WEST
				System.out.println("Moving South West. Adding [5] to Chaincode.");
				quadrantArray[startRow + 1][startCol - 1] = 2;
				chaincode += 5;
				startRow += 1;
				startCol -= 1;
			} else if (isValid2(quadrantArray, startRow + 1, startCol) && quadrantArray[startRow + 1][startCol] == 1) { 	   // direction 6 - SOUTH
				System.out.println("Moving South. Adding [6] to Chaincode.");
				quadrantArray[startRow + 1][startCol] = 2;
				chaincode += 6;
				startRow += 1;
			} else if (isValid2(quadrantArray, startRow + 1, startCol + 1) && quadrantArray[startRow + 1][startCol + 1] == 1) { // direction 7 - SOUTH EAST
				System.out.println("Moving South East. Adding [7] to Chaincode.");
				quadrantArray[startRow + 1][startCol + 1] = 2;
				chaincode += 7;
				startRow += 1;
				startCol += 1;
			} else {
				System.out.println("Error: Unable to determine next pixel hop location. Stopping image detection and returning chain code.");
				break;
			}

			System.out.println("Current Chaincode: " + chaincode + "\n");

			if (startRow == row && startCol == col) {
				System.out.println("\n ------------------------------------------------------");
				System.out.println(" Reached starting pixel. Breaking Chaincode generation.");
				System.out.println(" ------------------------------------------------------");
				break;
			}
		}

		System.out.println(" Final Image Chaincode: " + chaincode);
	}


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

	private int populateBinaryArray(int pixel) {

		int alpha = (pixel >> 24) & 0xff;
		int red = (pixel >> 16) & 0xff;
		int green = (pixel >> 8) & 0xff;
		int blue = (pixel) & 0xff;

		if (red == 0 && green == 0 && blue == 0) {
			return 1;
		}

		return 0;
	}


	private void printArrays(int[][] array) {
		System.out.println(Arrays.deepToString(array).replace("], ", "]\n").replace("[[", "[").replace("]]", "]\n"));

		//System.out.println(" String Build: \n");
		//System.out.println(Arrays.deepToString(stringArray).replace("], ", "]\n").replace("[[", "[").replace("]]", "]\n"));
		//System.out.println(" Binary Build: \n");
		//System.out.println(Arrays.deepToString(binaryArray).replace("], ", "]\n").replace("[[", "[").replace("]]", "]\n"));
		//drawBorders(binaryArray);
		//System.out.println(" Binary Build, Proper Borders Only: \n");
		//System.out.println(Arrays.deepToString(binaryArray).replace("], ", "]\n").replace("[[", "[").replace("]]", "]\n"));
	}

	private void print(int[][] arr, String quadrant) {
		System.out.println("\n PRINTING QUADRANT" + quadrant);
		for(int i = 0; i < arr.length; i++) {
			for(int j = 0; j < arr.length; j++) {
				System.out.print(arr[i][j]);
			}
			System.out.println("");
		}
	}

	

}
