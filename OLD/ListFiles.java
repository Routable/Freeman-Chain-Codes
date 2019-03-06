import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class ListFiles {

	File eightFolder = new File("C:\\Users\\steve\\OneDrive\\Desktop\\MachineLearning-kaysbranch\\images\\eight");
	File threeFolder = new File("C:\\Users\\steve\\OneDrive\\Desktop\\MachineLearning-kaysbranch\\images\\three");
	
	public void listFilesForFolder() throws IOException {
	    for (File fileEntry : eightFolder.listFiles()) {
	    		File img = new File(fileEntry.getName());
	    		BufferedImage buffImg = new BufferedImage(30, 30, BufferedImage.TYPE_INT_ARGB);
	            System.out.println("Currently Operating On: " + fileEntry.getName());
	            new ReadPixel(buffImg);
	    }
	}
}
