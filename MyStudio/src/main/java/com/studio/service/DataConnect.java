/**
 * 
 */
package com.studio.service;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import javax.persistence.Entity;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import com.lowagie.text.pdf.AcroFields;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.PdfStamper;


public class DataConnect
{
 
    public static boolean downloadFile(File file, File tofile) {
        System.out.println("1. File exists : " + file.exists());
        boolean success = false;
        
        String saveAsFileName = tofile.toString();
        System.out.println("saveAsFileName : " + saveAsFileName);
        System.out.println("file.toPath() : " + file.toPath());
        
        try
        {
            //File file = new File(BOOKINGPDFFILE);
            HttpHeaders respHeaders = new HttpHeaders();
            MediaType mediaType = MediaType.parseMediaType("application/pdf");
            respHeaders.setContentType(mediaType);
            respHeaders.setContentLength(file.length());
            respHeaders.setContentDispositionFormData("attachment", file.getName());
            InputStreamResource isr = new InputStreamResource(new FileInputStream(file));
            
            //return new ResponseEntity<InputStreamResource>(isr, respHeaders, HttpStatus.OK);
            //return new ResponseEntity<InputStreamResource>(isr, respHeaders, HttpStatus.OK);
        }
        catch (Exception e)
        {
            String message = "Errore nel download del file .csv; "+e.getMessage();
            //logger.error(message, e);
            //return new ResponseEntity<InputStreamResource>(HttpStatus.INTERNAL_SERVER_ERROR);
        }   
        
        System.out.println("file.toPath(,,,) : " + file.toPath());
        System.out.println("file.toPath(,,,) : " + file.toPath());
        System.out.println("file.toPath(,,,) : " + file.toPath());
        System.out.println("file.toPath(,,,) : " + file.toPath());
        System.out.println("file.toPath(,,,) : " + file.toPath());
        System.out.println("file.toPath(,,,) : " + file.toPath());
        System.out.println("file.toPath(,,,) : " + file.toPath());
        System.out.println("file.toPath(,,,) : " + file.toPath());
        System.out.println("file.toPath(,,,) : " + file.toPath());
        System.out.println("file.toPath(,,,) : " + file.toPath());
        System.out.println("file.toPath(,,,) : " + file.toPath());
        System.out.println("file.toPath(,,,) : " + file.toPath());
        System.out.println("file.toPath(,,,) : " + file.toPath());
        System.out.println("file.toPath(,,,) : " + file.toPath());
        
        

        /*InputStream is = null;
        OutputStream os = null;
        try {
            is = new FileInputStream(file);
            os = new FileOutputStream(tofile);
            byte[] buffer = new byte[1024];
            int length;
            while ((length = is.read(buffer)) > 0) {
                os.write(buffer, 0, length);
            }
        } catch(Exception re ) {
            //is.close();
            //os.close();
        	
        }*/
        
        
       String pDoc = "d://THALATHALTHALA.pdf";
       try
        {
    	  PdfReader reader   = new PdfReader("d://ADITYA_INN_ADV.pdf");
          PdfStamper stamper = new PdfStamper(reader, new FileOutputStream(pDoc));
          AcroFields form    = stamper.getAcroFields();
          form.setField("HEADING", "HEADING PRINT BY PASSING PARAMETER");
          form.setField("RCPIN", "PARAMETER PASSED");

          form.setField("MSG1", "Kindly handover the room key to the Reception,");
          form.setField("MSG2", "Before leaving the Hotel Premises.");
          stamper.close();
        }
        catch (Exception e)
        {
          e.printStackTrace();
        }
        
        
        
        //new PrintServices().PrintFile("d://VAISHNAVI_INV.pdf");
        
        
        
        

        System.out.println("Return SUCCESS : " + success);
        return success;
    }

	 
	
}
