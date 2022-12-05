package tests;

import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageTree;
import org.apache.pdfbox.pdmodel.PDResources;
import org.apache.pdfbox.pdmodel.graphics.PDXObject;
import org.apache.pdfbox.pdmodel.graphics.form.PDFormXObject;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;

/**
 * 
 * @author naveenautomationlabs
 *
 */
public class PDFReader {
	
	
	public int getPDFImagesCount(PDDocument document) throws IOException {
		return getImagesFromPDF(document).size();
	}
	
	/*
	 * get the list of PDF images
	 */
	public List<RenderedImage> getImagesFromPDF(PDDocument document) throws IOException {
		List<RenderedImage> images = new ArrayList<>();
		for (PDPage page : document.getPages()) {
			images.addAll(getImagesFromResources(page.getResources()));
		}

		return images;
	}

	private List<RenderedImage> getImagesFromResources(PDResources resources) throws IOException {
		List<RenderedImage> images = new ArrayList<>();

		for (COSName xObjectName : resources.getXObjectNames()) {
			PDXObject xObject = resources.getXObject(xObjectName);

			if (xObject instanceof PDFormXObject) {
				images.addAll(getImagesFromResources(((PDFormXObject) xObject).getResources()));
			} else if (xObject instanceof PDImageXObject) {
				images.add(((PDImageXObject) xObject).getImage());
			}
		}
		
		return images;
	}
	
	/**
	 * extract images from PDF
	 * @param document
	 * @throws Exception
	 */
	public void PDFBoxExtractImages(PDDocument document) throws Exception {
	    PDPageTree list = document.getPages();
	    for (PDPage page : list) {
	        PDResources pdResources = page.getResources();
	        for (COSName c : pdResources.getXObjectNames()) {
	            PDXObject o = pdResources.getXObject(c);
	            if (o instanceof org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject) {
	                File file = new File("./pdfimages/" + System.nanoTime() + ".png");
	                ImageIO.write(((org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject)o).getImage(), "png", file);
	            }
	        }
	    }
	}
	
	
	/**
	 * Compare two images
	 * @param fileA
	 * @param fileB
	 */
	public void imageCompare(File fileA, File fileB) {
		// Initially assigning null
        BufferedImage imgA = null;
        BufferedImage imgB = null;
  
        // Try block to check for exception
        try {
  
            // Reading file from local directory by
            // creating object of File class
            
  
            // Reading files
            imgA = ImageIO.read(fileA);
            imgB = ImageIO.read(fileB);
        }
  
        // Catch block to check for exceptions
        catch (IOException e) {
            // Display the exceptions on console
            System.out.println(e);
        }
  
        // Assigning dimensions to image
        int width1 = imgA.getWidth();
        int width2 = imgB.getWidth();
        int height1 = imgA.getHeight();
        int height2 = imgB.getHeight();
  
        // Checking whether the images are of same size or
        // not
        if ((width1 != width2) || (height1 != height2))
  
            // Display message straightaway
            System.out.println("Error: Images dimensions"
                               + " mismatch");
        else {
  
            // By now, images are of same size
  
            long difference = 0;
  
            // treating images likely 2D matrix
  
            // Outer loop for rows(height)
            for (int y = 0; y < height1; y++) {
  
                // Inner loop for columns(width)
                for (int x = 0; x < width1; x++) {
  
                    int rgbA = imgA.getRGB(x, y);
                    int rgbB = imgB.getRGB(x, y);
                    int redA = (rgbA >> 16) & 0xff;
                    int greenA = (rgbA >> 8) & 0xff;
                    int blueA = (rgbA)&0xff;
                    int redB = (rgbB >> 16) & 0xff;
                    int greenB = (rgbB >> 8) & 0xff;
                    int blueB = (rgbB)&0xff;
  
                    difference += Math.abs(redA - redB);
                    difference += Math.abs(greenA - greenB);
                    difference += Math.abs(blueA - blueB);
                }
            }
  
            // Total number of red pixels = width * height
            // Total number of blue pixels = width * height
            // Total number of green pixels = width * height
            // So total number of pixels = width * height *
            // 3
            double total_pixels = width1 * height1 * 3;
  
            // Normalizing the value of different pixels
            // for accuracy
  
            // Note: Average pixels per color component
            double avg_different_pixels
                = difference / total_pixels;
  
            // There are 255 values of pixels in total
            double percentage
                = (avg_different_pixels / 255) * 100;
  
            // Lastly print the difference percentage
            System.out.println("Difference Percentage-->"
                               + percentage);
        }
	}

}
