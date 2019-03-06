package chaincodemachinelearning;

import java.io.File;
import java.io.IOException;

public class Main {
	
	public static void main(String[] foo) throws IOException {
		
		File eightFolder = new File("res/eight");
		File threeFolder = new File("res/three");

		System.out.println("STARTING PROGRAM");


		// Generating chaincode for all images in the threes folder.
		ListFiles three = new ListFiles(threeFolder);
		three.listFilesForFolder();
		
		//Generating chaincode for all images in the eights folder.
		ListFiles eight = new ListFiles(eightFolder);
		eight.listFilesForFolder();
		
	
	}
}
