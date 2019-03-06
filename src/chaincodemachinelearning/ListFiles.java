package chaincodemachinelearning;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import javax.imageio.ImageIO;

public class ListFiles {
	File folder;
	
	public ListFiles(File folder) {
		this.folder = folder;
	}

	public void listFilesForFolder() throws IOException {

		File[] directoryListing = folder.listFiles();
		
		 if (directoryListing != null) {
			    for (File child : directoryListing) {
			    	File img = new File(child.getName());
			    	String filename = child.getName();
			        System.out.println("CALCULATING CHAINCODE FOR: " + filename);
			       
					BufferedImage image = ImageIO.read(child);
					
			        new ReadPixel(image);
			    }
			  } else {
				  System.out.println("Error: A problem with the file or directory has been detected. Check to see if the folder exists, and contains images");
			  }
			 

		
		
	}
}
