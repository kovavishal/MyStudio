package com.studio.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.persistence.Entity;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public class DownloadFileServlet extends HttpServlet {

	
	private static final long serialVersionUID = 1L;
	//System.out.println("INSIDE DOWNLOAD FILESERVLET ");
	//System.out.println("INSIDE DOWNLOAD FILESERVLET ");
	//System.out.println("INSIDE DOWNLOAD FILESERVLET ");
	//System.out.println("INSIDE DOWNLOAD FILESERVLET ");
	

	@Override
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// reads input file from an absolute path
		//String filePath = "E:/Test/Download/MYPIC.JPG";
		//Order myorder = new Order();
		//String ord = ""+myorder.getOrderId(); 
			//	System.out.println(myorder.getOrderId());
		String ord = request.getParameter("orderid");
		System.out.println("ORD:" + ord);
				
				String filePath ="D:\\VAISHANVI_PDF\\"+ ord +".pdf";
				//String filePath ="/WEB-INF/downloads/pdf/"+ ord +".pdf";
		
		// get me the order objhect here
		
		//String filePath = "d:/VAISHNAVI_PDF/ORDER_1176.pdf";
		// SHOW THE CODE WHERE U R COLLECTING IVNOICE DATA
		
		File downloadFile = new File(filePath);
		FileInputStream inStream = new FileInputStream(downloadFile);
		
		// if you want to use a relative path to context root:
		String relativePath = getServletContext().getRealPath("");
		System.out.println("relativePath = " + relativePath);
		
		// obtains ServletContext
		ServletContext context = getServletContext();
		
		// gets MIME type of the file
		String mimeType = context.getMimeType(filePath);
		if (mimeType == null) {			
			// set to binary type if MIME mapping not found
			mimeType = "application/octet-stream";
		}
		System.out.println("MIME type: " + mimeType);
		
		// modifies response
		response.setContentType(mimeType);
		response.setContentLength((int) downloadFile.length());
		
		// forces download
		/*String headerKey = "Content-Disposition";
		String headerValue = String.format("attachment; filename=\"%s\"", downloadFile.getName());
		response.setHeader(headerKey, headerValue);*/
		
		// obtains response's output stream
		OutputStream outStream = response.getOutputStream();
		
		byte[] buffer = new byte[4096];
		int bytesRead = -1;
		
		while ((bytesRead = inStream.read(buffer)) != -1) {
			outStream.write(buffer, 0, bytesRead);
		}
		
		inStream.close();
		outStream.close();		
	}
}
