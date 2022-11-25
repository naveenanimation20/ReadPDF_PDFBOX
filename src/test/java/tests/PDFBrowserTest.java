package tests;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

public class PDFBrowserTest {

	WebDriver driver;

	@BeforeTest
	public void setup() {

		driver = new ChromeDriver();
		driver.get("https://www.inkit.com/blog/pdf-the-best-digital-document-management");

	}
	
	
	@Test
	public void readPdfInSameBrowserTest() throws InterruptedException, IOException {
		
		driver.findElement(By.linkText("trillions of PDFs")).click();
		Thread.sleep(3000);
		
		String url = driver.getCurrentUrl();
		
		URL pdfUrl = new URL(url);
		
		URLConnection urlConnection = pdfUrl.openConnection();
		urlConnection.addRequestProperty("User-Agent", "Mozilla");
		
		InputStream ip = urlConnection.getInputStream();
		
		BufferedInputStream bf = new BufferedInputStream(ip);
		
		//
		PDDocument pdDocument = PDDocument.load(bf);
		
		int pageCount = pdDocument.getNumberOfPages();
		System.out.println("pages count: " + pageCount);
		Assert.assertEquals(pageCount, 43);
		
		//
		System.out.println("------meta data of pdf-------");
		System.out.println(pdDocument.getVersion());
		System.out.println(pdDocument.getCurrentAccessPermission().canPrint());
		System.out.println(pdDocument.getCurrentAccessPermission().isReadOnly());
		System.out.println(pdDocument.getCurrentAccessPermission().isOwnerPermission());
		
		System.out.println(pdDocument.getDocumentInformation().getSubject());
		System.out.println(pdDocument.getDocumentInformation().getTitle());
		System.out.println(pdDocument.getDocumentInformation().getCreator());
		System.out.println(pdDocument.getDocumentInformation().getCreationDate());
		
		System.out.println(pdDocument.isEncrypted());
		System.out.println(pdDocument.getDocumentId());

		//read full pdf text:
		PDFTextStripper pdfStripper = new PDFTextStripper();
//		String pdfFullText = pdfStripper.getText(pdDocument);
//		System.out.println(pdfFullText);
//		Assert.assertTrue(pdfFullText.contains("Recognized worldwide"));
		
		pdfStripper.setStartPage(43);
		String pdfPageText = pdfStripper.getText(pdDocument);
		System.out.println(pdfPageText);
		
		pdDocument.close();
		
	}
	
	
	
	

	@AfterTest
	public void tearDown() {
		driver.quit();
	}

}
