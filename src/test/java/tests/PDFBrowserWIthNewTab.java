package tests;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Iterator;
import java.util.Set;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

public class PDFBrowserWIthNewTab {

	WebDriver driver;

	@BeforeTest
	public void setup() throws InterruptedException {

		driver = new ChromeDriver();
		driver.get("https://www.hdfcbank.com/personal/resources/rates");
		Thread.sleep(3000);

	}
	
	
	@Test
	public void readPdfInSameBrowserTest() throws InterruptedException, IOException {
		
		driver.findElement(By.xpath("//h3[contains(text(),'Fixed Deposit Interest Rate Greater Than Or Equal To 5 Cr')]")).click();
		Thread.sleep(2000);
		//driver.findElement(By.xpath("//p[contains(text(),'For rates greater than equal to Rs. 5 Cr please ')]/a")).click();
		//Thread.sleep(3000);
		
		String url =
				driver.findElement(By.xpath("//p[contains(text(),'For rates greater than equal to Rs. 5 Cr please ')]/a")).getAttribute("href");
		
		
//		Set<String> handles = driver.getWindowHandles();
//		Iterator<String> it = handles.iterator();
//		String PWID = it.next();
//		String CWID = it.next();
//		
//		driver.switchTo().window(CWID);
//		String url = driver.getCurrentUrl();
//		System.out.println("PDF tab url : " + url);
		
		URL pdfUrl = new URL(url);
		
		URLConnection urlConnection = pdfUrl.openConnection();
		urlConnection.addRequestProperty("User-Agent", "Mozilla");
		
		InputStream ip = urlConnection.getInputStream();
		
		BufferedInputStream bf = new BufferedInputStream(ip);
		
		//
		PDDocument pdDocument = PDDocument.load(bf);
		
		int pageCount = pdDocument.getNumberOfPages();
		System.out.println("pages count: " + pageCount);
		Assert.assertEquals(pageCount, 4);
		
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
		String pdfFullText = pdfStripper.getText(pdDocument);
		System.out.println(pdfFullText);
		Assert.assertTrue(pdfFullText.contains("DOMESTIC/NRE/NRO TERM DEPOSITS INTEREST RATES"));
		
//		pdfStripper.setStartPage(1);
//		String pdfPageText = pdfStripper.getText(pdDocument);
//		System.out.println(pdfPageText);
		
		pdDocument.close();
		
//		driver.close();//close the pdf tab window
//		driver.switchTo().window(PWID);
//		System.out.println("parent window title : " + driver.getTitle());
		
		
	}
	
	
	
	

	@AfterTest
	public void tearDown() {
		driver.quit();
	}

}
