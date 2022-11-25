package tests;

import java.awt.image.BufferedImage;
import java.awt.image.DataBuffer;
import java.awt.image.RenderedImage;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
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
import org.apache.pdfbox.text.PDFTextStripper;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

public class PDFReaderTest {

	WebDriver driver;

	@BeforeTest
	public void setup() {
		//ChromeOptions co = new ChromeOptions();
		// co.setHeadless(true);
		// co.addArguments("--incognito");
		 driver = new ChromeDriver();
		driver.get("https://www.inkit.com/blog/pdf-the-best-digital-document-management");

	}

	@Test
	public void pdfReaderTest() throws Exception {

		//String url = driver.findElement(By.xpath("//a[normalize-space()='trillions of PDFs']")).getAttribute("href");
		driver.findElement(By.xpath("//a[normalize-space()='trillions of PDFs']")).click();
		Thread.sleep(5000);
		String url = driver.getCurrentUrl();
		URL pdfUrl = new URL(url);

		URLConnection urlConnection = pdfUrl.openConnection();
		urlConnection.addRequestProperty("User-Agent", "Mozilla");

		InputStream ip = urlConnection.getInputStream();
		BufferedInputStream bf = new BufferedInputStream(ip);
		PDDocument pdDocument = PDDocument.load(bf);
//
//		// page count:
//		int pageCount = pdDocument.getNumberOfPages();
//		System.out.println("pdf page: " + pageCount);
//		Assert.assertEquals(pageCount, 4);
//
//		System.out.println(pdDocument.getVersion());
//		System.out.println(pdDocument.getCurrentAccessPermission().isReadOnly());
//		System.out.println(pdDocument.getDocumentInformation().getAuthor());
//		System.out.println(pdDocument.getDocumentInformation().getTitle());
//		System.out.println(pdDocument.getDocumentInformation().getSubject());
//		System.out.println(pdDocument.getDocumentInformation().getCreationDate());
//		System.out.println(pdDocument.isEncrypted());
//
//		System.out.println("========================pdf content===============");
//		
		int imagesCount = getImagesFromPDF(pdDocument).size();
		System.out.println(imagesCount);
		testPDFBoxExtractImages(pdDocument);
		

		// full pdf page content/text:
		PDFTextStripper pdfStiper = new PDFTextStripper();
//		String pdfText = pdfStiper.getText(pdDocument);
//		System.out.println(pdfText);
//		Assert.assertTrue(pdfText.contains("PDF BOOKMARK SAMPLE"));
//		Assert.assertTrue(pdfText.contains("This sample consists of a simple form containing four distinct fields."));
//		Assert.assertTrue(pdfText.contains("ap_bookmark.IFD"));
//		Assert.assertTrue(pdfText.contains("Place ap_bookmark.mdf"));

		// set the page number and get the text:
//		pdfStiper.setStartPage(42);
//		String pdfText = pdfStiper.getText(pdDocument);
//		System.out.println(pdfText);
//		Assert.assertTrue(pdfText.contains("ap_bookmark.dat "));
		
		File exp = new File("./727080549996161.png");
		File act = new File("./727080549996162.png");
		imageCompare(exp, act);
	}

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
	
	public static void testPDFBoxExtractImages(PDDocument document) throws Exception {
	    PDPageTree list = document.getPages();
	    for (PDPage page : list) {
	        PDResources pdResources = page.getResources();
	        for (COSName c : pdResources.getXObjectNames()) {
	            PDXObject o = pdResources.getXObject(c);
	            if (o instanceof org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject) {
	                File file = new File("./" + System.nanoTime() + ".png");
	                ImageIO.write(((org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject)o).getImage(), "png", file);
	            }
	        }
	    }
	}
	
	
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
	
	
	
	
	public float compareImage(File fileA, File fileB) {

	    float percentage = 0;
	    try {
	        // take buffer data from both image files //
	        BufferedImage biA = ImageIO.read(fileA);
	        DataBuffer dbA = biA.getData().getDataBuffer();
	        int sizeA = dbA.getSize();
	        BufferedImage biB = ImageIO.read(fileB);
	        DataBuffer dbB = biB.getData().getDataBuffer();
	        int sizeB = dbB.getSize();
	        int count = 0;
	        // compare data-buffer objects //
	        if (sizeA == sizeB) {

	            for (int i = 0; i < sizeA; i++) {

	                if (dbA.getElem(i) == dbB.getElem(i)) {
	                    count = count + 1;
	                }

	            }
	            percentage = (count * 100) / sizeA;
	        } else {
	            System.out.println("Both the images are not of same size");
	        }

	    } catch (Exception e) {
	        System.out.println("Failed to compare image files ...");
	    }
	    return percentage;
	}

	@AfterTest
	public void tearDown() {
		// driver.quit();
	}

}
