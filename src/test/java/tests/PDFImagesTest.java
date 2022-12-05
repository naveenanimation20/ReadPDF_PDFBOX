package tests;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.testng.annotations.Test;

public class PDFImagesTest {
	
	
	@Test
	public void pdfImageTest() throws Exception {
//		String url = "file:///Users/naveenautomationlabs/Desktop/1330_Johnson.pdf";
//
//		URL pdfUrl = new URL(url);
//		URLConnection urlConnection = pdfUrl.openConnection();
//		urlConnection.addRequestProperty("User-Agent", "Mozilla");
//
//		InputStream ip = urlConnection.getInputStream();
//		BufferedInputStream bf = new BufferedInputStream(ip);
//		PDDocument pdDocument = PDDocument.load(bf);
		
		PDFReader reader = new PDFReader();
//		int imageCount = reader.getPDFImagesCount(pdDocument);
//		System.out.println("total images = "+ imageCount);
//		
//		
//		reader.PDFBoxExtractImages(pdDocument);
		
		File fileA = new File("./newhdfc.png");
		File fileB = new File("./actual_image_hdfc_logo-modified.png");

		
		reader.imageCompare(fileA, fileB);
		
	}
	
	
	
	
	

}
