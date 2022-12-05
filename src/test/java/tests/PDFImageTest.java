package tests;

import java.io.File;
import java.io.IOException;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.testng.annotations.Test;

public class PDFImageTest {
	
	
	@Test
	public void pdfImageTest() throws IOException {
		
		PDDocument document = null;
        String fileName = "/Users/naveenautomationlabs/Desktop/hdfc.pdf";
        try
        {
            document = PDDocument.load( new File(fileName) );
            PDFImageExtractor printer = new PDFImageExtractor();
            int pageNum = 0;
            for( PDPage page : document.getPages() )
            {
                pageNum++;
                System.out.println( "Processing page: " + pageNum );
                printer.processPage(page);
            }
        }
        finally
        {
            if( document != null )
            {
                document.close();
            }
        }
		
	}

}
