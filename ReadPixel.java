import java.awt.Component;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.io.File;
import java.util.Arrays;
import java.util.*;
import java.sql.*;
import java.text.NumberFormat;

public class ReadPixel extends Component {

	public String[][] stringArray;
	public int[][] binaryArray;
	public ArrayList<String> chainList = new ArrayList<String>();
	public ArrayList<String> perimeterList = new ArrayList<String>();
	public ArrayList<String> directionList = new ArrayList<String>();
	int imageWidth;
	int imageHeight;

	public static void main(String[] foo) {

		System.out.println(" \n ---------------------------------- ");
		System.out.println(" 	   STARTING IMAGE DETECTION     ");
		System.out.println(" ---------------------------------- ");

		final File eightFolder = new File("C:/Users/Kaptain/Desktop/MachineLearning/images/eight");
		final File threeFolder = new File("C:/Users/Kaptain/Desktop/MachineLearning/images/three");
		listFilesForFolder(eightFolder); 
		listFilesForFolder(threeFolder);
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
	
	public static void listFilesForFolder(final File folder) {
	    for (final File fileEntry : folder.listFiles()) {
	        if (fileEntry.isDirectory()) {
	            listFilesForFolder(fileEntry);
	        } else {
	            System.out.println(fileEntry.getName());
	        }
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


		System.out.println("-----------------------------------");
		System.out.println(" Calculating Quadrant 1 Chaincode: ");
		System.out.println("-----------------------------------");
		indexOfFirstOne(Q1);

		System.out.println("-----------------------------------");
		System.out.println(" Calculating Quadrant 2 Chaincode: ");
		System.out.println("-----------------------------------");
		indexOfFirstOne(Q2);

		System.out.println("-----------------------------------");
		System.out.println(" Calculating Quadrant 3 Chaincode: ");
		System.out.println("-----------------------------------");
		indexOfFirstOne(Q3);

		System.out.println("-----------------------------------");
		System.out.println(" Calculating Quadrant 4 Chaincode: ");
		System.out.println("-----------------------------------");
		indexOfFirstOne(Q4);

		System.out.println("-----------------------------------");
		System.out.println(" Calculating Quadrant 5 Chaincode: ");
		System.out.println("-----------------------------------");
		indexOfFirstOne(Q5);

		System.out.println("-----------------------------------");
		System.out.println(" Calculating Quadrant 6 Chaincode: ");
		System.out.println("-----------------------------------");
		indexOfFirstOne(Q6);

		System.out.println("-----------------------------------");
		System.out.println(" Calculating Quadrant 7 Chaincode: ");
		System.out.println("-----------------------------------");
		indexOfFirstOne(Q7);

		System.out.println("-----------------------------------");
		System.out.println(" Calculating Quadrant 8 Chaincode: ");
		System.out.println("-----------------------------------");
		indexOfFirstOne(Q8);

		System.out.println("-----------------------------------");
		System.out.println(" Calculating Quadrant 9 Chaincode: ");
		System.out.println("-----------------------------------");
		indexOfFirstOne(Q9);

		System.out.println("-----------------------------------------------");
		System.out.println(" CALCULATIONS COMPLETED: SEE OUTPUT FOR DETAILS");
		System.out.println("-----------------------------------------------");

		
		//Getting Strings for DB insertion (FCC Code, Perimeter count, Direction Count)
		String q1code = chainList.get(0);
		String q2code = chainList.get(1);
		String q3code = chainList.get(2);
		String q4code = chainList.get(3);		
		String q5code = chainList.get(4);		
		String q6code = chainList.get(5);
		String q7code = chainList.get(6);
		String q8code = chainList.get(7);
		String q9code = chainList.get(8);
		
		String q1Pcount = perimeterList.get(0);
		String q2Pcount = perimeterList.get(1);
		String q3Pcount = perimeterList.get(2);
		String q4Pcount = perimeterList.get(3);
		String q5Pcount = perimeterList.get(4);
		String q6Pcount = perimeterList.get(5);
		String q7Pcount = perimeterList.get(6);
		String q8Pcount = perimeterList.get(7);
		String q9Pcount = perimeterList.get(8);
		
		String q1Dcount = directionList.get(0);
		String q2Dcount = directionList.get(1);
		String q3Dcount = directionList.get(2);
		String q4Dcount = directionList.get(3);
		String q5Dcount = directionList.get(4);
		String q6Dcount = directionList.get(5);
		String q7Dcount = directionList.get(6);
		String q8Dcount = directionList.get(7);
		String q9Dcount = directionList.get(8);
		
		
		//Connection to database for insertion
		Connection con=null;
		Statement stmt=null;
		try{
			Class.forName("oracle.jdbc.driver.OracleDriver");  
			con=DriverManager.getConnection(  
			"jdbc:oracle:thin:DM_USER13/password@10.1.144.85:1521:COSC436");  
			stmt=con.createStatement();
		}catch(Exception e){ System.out.println(e);}
        // get key coordinates
		ResultSet rs = null;
		ResultSet rs2 = null;
		try{
			//stmt.executeUpdate("INSERT INTO DIGIT_TEST (CONCAT(SEC," +quadrant+", _FCC_CODE) " + "VALUES ("+final_fcc2+")" + "WHERE CLS_LBL = 2");
			stmt.executeUpdate("INSERT INTO DIGIT_TRAINING (CLS_LBL, SEC1_FCC_CODE, SEC1_PERIMETER, SEC1_FCC_CNT, SEC2_FCC_CODE, SEC2_PERIMETER, SEC2_FCC_CNT, SEC3_FCC_CODE, SEC3_PERIMETER, SEC3_FCC_CNT, "
					+ "SEC4_FCC_CODE, SEC4_PERIMETER, SEC4_FCC_CNT, SEC5_FCC_CODE, SEC5_PERIMETER, SEC5_FCC_CNT, SEC6_FCC_CODE, SEC6_PERIMETER, SEC6_FCC_CNT, SEC7_FCC_CODE, SEC7_PERIMETER, SEC7_FCC_CNT, "
					+ "SEC8_FCC_CODE, SEC8_PERIMETER, SEC8_FCC_CNT, SEC9_FCC_CODE, SEC9_PERIMETER, SEC9_FCC_CNT) "
					+ "" + "VALUES ( '5', "+q1code+", "+q1Pcount+", "+q1Dcount+", "+q2code+", "+q2Pcount+", "+q2Dcount+", "+q3code+", "+q3Pcount+", "+q3Dcount+", "+q4code+", "+q4Pcount+", "+q4Dcount+", "
							+ ""+q5code+", "+q5Pcount+", "+q5Dcount+", "+q6code+", "+q6Pcount+", "+q6Dcount+", "+q7code+", "+q7Pcount+", "+q7Dcount+","
							+ " "+q8code+", "+q8Pcount+", "+q8Dcount+", "+q9code+", "+q9Pcount+", "+q9Dcount+")");  // + "WHERE CLS_LBL = 2");
		    //stmt.close();
			String query = "SELECT * FROM DIGIT_TRAINING ";
			String query2 = "SELECT * FROM DIGIT_TEST";
			rs = stmt.executeQuery(query);
			//rs2 = stmt.executeQuery(query2);
			
			while(rs.next())
		      {
		        String fcc1 = rs.getString("SEC1_FCC_CODE");
		        int cls_lbl = rs.getInt("CLS_LBL");
		        // print the results
		        System.out.println("FCC: " + fcc1 + " Class label: " + cls_lbl);
		      }
		}catch(Exception e){ System.out.println(e);}	
		
		
		Iterator itr = chainList.iterator();			
		int quadrant = 1;
		String final_fcc = "";
		String final_fcc2 = "";	

  		while(itr.hasNext()){ 
			   Object element = itr.next(); 
			   final_fcc2 = element.toString();
			   System.out.println("Quadrant " + quadrant + " Chain Code: " + final_fcc2);
				final_fcc += final_fcc2;
			   quadrant++;
  		}
		  System.out.println("Final Chain Code: " + final_fcc); 
		  System.out.println("q1 and q9 pcount: " + q1Pcount + ", " + q9Pcount);
		  System.out.println("q1 and q9 dcount: " + q1Dcount + ", " + q9Dcount);
		  
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


//printArrays(quadrantArray);

		// In our third pass, we go through each pixel and check to see if the current
		// pixel is an 'dead' pixel. (AKA, a pixel that shares an unnecessary border)
		// This is determined by checking its' neighbors and seeing if they already 
		// have a defined border assigned. 

			for (int row = 0; row < 10; row++) {
				for (int col = 0; col < 10; col++) {
					try {
						
						if(isValid2(quadrantArray, row, col + 1) && isValid2(quadrantArray, row + 1, col)) {

							if(quadrantArray[row][col+1] == 1 /* E */ && quadrantArray[row+1][col] == 1 /* S */) {
								quadrantArray[row][col] = 0;
							}
						}

						if(isValid2(quadrantArray, row, col - 1) && isValid2(quadrantArray, row + 1, col)) {
							if(quadrantArray[row][col - 1] == 1  /* W */ && quadrantArray[row + 1][col] == 1 /* S */) {
								quadrantArray[row][col] = 0;
							}	
						}

						if(isValid2(quadrantArray, row - 1, col) && isValid2(quadrantArray, row, col - 1)) {
							if(quadrantArray[row - 1][col] == 1 /* N */ && quadrantArray[row][col - 1] == 1 /*W */) {
							quadrantArray[row][col] = 0;
							}
						}

						if(isValid2(quadrantArray, row - 1, col) && isValid2(quadrantArray, row, col + 1)) {
							if(quadrantArray[row-1][col] == 1 /* N */ && quadrantArray[row][col+1] == 1 /* E */) {
							quadrantArray[row][col] = 0;
							}
						}
		
				} catch(Exception IndexOutOfBoundsException) {
					continue;
				}
			}		
		}

		//printArrays(quadrantArray);

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
		
		//Direction move counters
		int east = 0;
		int n_east = 0;
		int north = 0;
		int n_west = 0;
		int west = 0;
		int s_west = 0;
		int south = 0;
		int s_east = 0;

		quadrantArray = drawBorders(quadrantArray);


		while (true) {

			if (isValid2(quadrantArray, startRow, startCol + 1) && quadrantArray[startRow][startCol + 1] == 1) { 			// direction 0 - EAST)
				//System.out.println("Moving East. Adding [0] to Chaincode.");
				quadrantArray[startRow][startCol + 1] = 2;
				chaincode += 0;
				startCol += 1;
				east += 1; 
			} else if (isValid2(quadrantArray, startRow - 1, startCol + 1) && quadrantArray[startRow - 1][startCol + 1] == 1) {  // direction 1 - NORTH EAST
				//System.out.println("Moving North East. Adding [1] to Chaincode.");
				quadrantArray[startRow - 1][startCol + 1] = 2;
				chaincode += 1;
				startRow -= 1;
				startCol += 1;
				n_east += 1;
			} else if (isValid2(quadrantArray, startRow - 1, startCol) && quadrantArray[startRow - 1][startCol] == 1) { 	   // direction 2 - NORTH
				//System.out.println("Moving North. Adding [2] to Chaincode.");
				quadrantArray[startRow - 1][startCol] = 2;
				chaincode += 2;
				startRow -= 1;
				north += 1;
			} else if (isValid2(quadrantArray, startRow - 1, startCol - 1) && quadrantArray[startRow - 1][startCol - 1] == 1) { // direction 3 - NORTH WEST
				//System.out.println("Moving North West. Adding [3] to Chaincode.");
				quadrantArray[startRow - 1][startCol - 1] = 2;
				chaincode += 3;
				startRow -= 1;
				startCol -= 1;
				n_west +=1;
			} else if (isValid2(quadrantArray, startRow, startCol - 1) && quadrantArray[startRow][startCol - 1] == 1) { 	   // direction 4 - WEST
				//System.out.println("Moving West. Adding [4] to Chaincode.");
				quadrantArray[startRow][startCol - 1] = 2;
				chaincode += 4;
				startCol -= 1;
				west +=1;
			} else if (isValid2(quadrantArray, startRow + 1, startCol - 1) && quadrantArray[startRow + 1][startCol - 1] == 1) { // direction 5 - SOUTH WEST
				//System.out.println("Moving South West. Adding [5] to Chaincode.");
				quadrantArray[startRow + 1][startCol - 1] = 2;
				chaincode += 5;
				startRow += 1;
				startCol -= 1;
				s_west +=1;
			} else if (isValid2(quadrantArray, startRow + 1, startCol) && quadrantArray[startRow + 1][startCol] == 1) { 	   // direction 6 - SOUTH
				//System.out.println("Moving South. Adding [6] to Chaincode.");
				quadrantArray[startRow + 1][startCol] = 2;
				chaincode += 6;
				startRow += 1;
				south +=1;
			} else if (isValid2(quadrantArray, startRow + 1, startCol + 1) && quadrantArray[startRow + 1][startCol + 1] == 1) { // direction 7 - SOUTH EAST
				//System.out.println("Moving South East. Adding [7] to Chaincode.");
				quadrantArray[startRow + 1][startCol + 1] = 2;
				chaincode += 7;
				startRow += 1;
				startCol += 1;
				s_east += 1;
			} else {
				//System.out.println("Error: Unable to determine next pixel hop location. Stopping image detection and returning chain code.");
				break;
			}

			//System.out.println("Current Chaincode: " + chaincode + "\n");

			if (startRow == row && startCol == col) {
				System.out.println("\n ------------------------------------------------------");
				System.out.println(" Reached starting pixel. Breaking Chaincode generation.");
				System.out.println(" ------------------------------------------------------");
				
				break;
			}
		}

		//array for fcc counts converted to string for table insertion
		int[] fcc_counts = new int[]{east, n_east, north, n_west, west, s_west, south, s_east};
		String fcc_count = "";

		for(int i = 0; i < fcc_counts.length; i++){
			fcc_count += fcc_counts[i];

		}
		//Long chain = Long.parseLong(chaincode);
		//String formattedCode = String.format("%020d", chain);
		System.out.println(" Final Image Chaincode: " + chaincode);
		System.out.println(" FCC Code Count - East: " + east + ", North East: " + n_east + ", North: " + north + 
		", North West: " + n_west + ", West: " + west + ", South West: " + s_west + ", South: " + south + ", South East: "  + s_east);
		System.out.println(" FCC Count String for table insertion: " + fcc_count);
		System.out.println(" Perimeter count: " + chaincode.length());
		chainList.add(chaincode);
		directionList.add(fcc_count);
		int pLength = chaincode.length();
		String pCount = Integer.toString(chaincode.length());
		perimeterList.add(pCount);
	
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
