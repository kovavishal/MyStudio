/* *
 * 
 */
package com.studio.service;

/**
 * @author ezhilraja_k
 *
 */
import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import java.io.*;
import javax.persistence.Entity;
import javax.swing.*;

//import com.sun.mail.util.MailSSLSocketFactory;

//import com.sun.mail.util.MailSSLSocketFactory;

import java.util.*;
import javax.mail.*;
import java.io.*;
import javax.mail.internet.*;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;


public class EmailService
{
  private static final String SMTP_HOST_NAME="smtp.gmail.com";
  private static final String SMTP_AUTH_USER="svdppanruti@gmail.com";
  private static final String SMTP_AUTH_PWD ="Svdppanruti607106"; 
  private static final String SMTP_PORT = "465"; 

  JFrame mesfrm; 

  private static final String SSL_FACTORY = "javax.net.ssl.SSLSocketFactory";

  public void mailing(String emailMsgTxt, String emailSubjectTxt, String emailFromAddress, String[] emailList, String wher, String refno, String filename, boolean msg) throws Exception
  {
    postMail( emailList, emailSubjectTxt, emailMsgTxt, emailFromAddress, filename);
    System.out.println("Successfully Sent mail to All Users");
    if(msg)
       JOptionPane.showMessageDialog(mesfrm,"Mail Sent Successfully !");
    //--------------------------------------------------------------------//
    //--------------------------------------------------------------------//
  } 

  public void postMail(String recipients[], String subject, String message, String from, String filename) throws MessagingException 
  {
    boolean debug = true;
    Properties props = new Properties();
    props.put("mail.transport.protocol","smtp");
    props.put("mail.smtp.host", SMTP_HOST_NAME);
    props.put("mail.smtp.auth", "true");
    props.put("mail.debug", "true");
    props.put("mail.smtp.port", SMTP_PORT);  
    props.put("mail.smtp.socketFactory.port", SMTP_PORT);
    props.put("mail.smtp.socketFactory.class", SSL_FACTORY);
    props.put("mail.smtp.socketFactory.fallback", "false");

    props.put("mail.smtp.starttls.enable", "true");  
   /* SecurityManager security = System.getSecurityManager();
    System.out.println("Security Manager" + security);*/
    props.put("mail.smtp.ssl.trust", "smtp.gmail.com");    

   // MailSSLSocketFactory sf = new MailSSLSocketFactory();
   // sf.setTrustAllHosts(true); 
   // props.put("mail.imap.ssl.trust", "*");
   // props.put("mail.imap.ssl.socketFactory", sf); 
   
    
    Session session = Session.getInstance(props,new javax.mail.Authenticator() 
    {
      
	protected PasswordAuthentication getPasswordAuthentication() 
      {
        return new PasswordAuthentication(SMTP_AUTH_USER, SMTP_AUTH_PWD);
      }
    });
    session.setDebug(debug);
    Message  msg = new MimeMessage(session);
    InternetAddress addressFrom = new InternetAddress(from);
    msg.setFrom(addressFrom);
//    String filename = "message.pdf";


            //
            // Set the email message text.
            //
            MimeBodyPart messagePart = new MimeBodyPart();
            messagePart.setText(subject);

            //
            // Set the email attachment file
            //
            Multipart multipart = new MimeMultipart();
            if(!filename.equals("NOATTACHMENT"))
            {
              MimeBodyPart attachmentPart = new MimeBodyPart();
              FileDataSource fileDataSource = new FileDataSource(filename) {
                  @Override
                  public String getContentType() {
                      return "application/octet-stream";
                  }
              };
              attachmentPart.setDataHandler(new DataHandler(fileDataSource));
              attachmentPart.setFileName(filename);
              multipart.addBodyPart(attachmentPart);
            }
            multipart.addBodyPart(messagePart);
            msg.setContent(multipart);
            msg.setSubject(subject);
//            msg.addRecipients(messagePart.RecipientType.CC, "madhanraj@lignitediesel.com");


    InternetAddress[] addressTo = new InternetAddress[recipients.length];
    for (int i = 0; i < recipients.length; i++) {
    addressTo[i] = new InternetAddress(recipients[i]);
   }
   msg.setRecipients(Message.RecipientType.TO, addressTo);

//   msg.setContent(message, "text/plain");

   Transport.send(msg);
 }
//---------------------------------------------------------------
}



// TO SEND MAIL THE FOLLOWING CODE TO BE USED AND CAN BE CALLED FROM ANYWHERE
// JUST NEED TO IMPORT com.service.EmailService.class

/*
//...................................................
String emailSubjectTxt = "BANK RECEIPT UPDATION";
//...................................................
String emailMsgTxt = "\nAttn.M.D.\n";
emailMsgTxt = emailMsgTxt + "CBR" + "\n";
emailMsgTxt = emailMsgTxt + "by " + "\n"; 
emailMsgTxt = emailMsgTxt + "AMNT FROM : Rs.\n";
emailMsgTxt = emailMsgTxt + "DATE FROM : \n";
//...................................................
String[] emailList = {"ldinakaran@gmail.com","radisoftconsultancy@gmail.com"};
//...................................................
//String[] emailList = {"ldinakaran@gmail.com"};
//...................................................
try
{
//new EmailService().mailing(emailMsgTxt, emailSubjectTxt, "ldinakaran@gmail.com", emailList, "CASH RECEIPT UPDATION", "ABIKRISHNAA_TILEMART", "NOATTACHMENT", false);
//JOptionPane.showMessageDialog(null,"Mail Sent Successfully !");
}
catch(Exception re){; System.out.println(re); re.printStackTrace();  }
//#####################################################################################

*/