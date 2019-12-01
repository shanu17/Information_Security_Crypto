package X509_Certificates;

import java.io.*;
import java.net.*;
import java.security.KeyStore;
import java.security.cert.X509Certificate;
import java.security.interfaces.RSAPublicKey;
import java.util.Date;
import java.util.Scanner;

import javax.crypto.Cipher;

public class X509_Client {
	
	public static void main(String[] args) throws Exception
	{
		String host = "127.0.0.1";
		int port = 7999;
		Socket s = new Socket(host, port);
	    
		ObjectOutputStream os = new ObjectOutputStream(s.getOutputStream());
        InputStream inStream = new FileInputStream("keystore.jks");
        
        KeyStore ks=KeyStore.getInstance("JKS");
        ks.load(inStream,"password".toCharArray());
        X509Certificate cert=(X509Certificate) ks.getCertificate("selfsigned");
        System.out.println("The detail of the certificate file is:");
        System.out.println(cert.toString());
        Date date = cert.getNotAfter();
        if(date.after(new Date()))
        {
        	System.out.println("The certificate is current.");
        	System.out.println("It is valid from "+ cert.getNotBefore()+ " to "+cert.getNotAfter()) ;
        }
        else
        {     
        	System.out.println("The certificate is expired.");
        }
   
        try
        {
           cert.checkValidity();
    	   System.out.println("The certificate is signed using the private key that corresponds to the public key.");
        } catch (Exception e){
    	   System.out.println(e);   
        }
        
        System.out.println("Message to send Server:");
        Scanner input = new Scanner(System.in);
        String message= input.nextLine();
        System.out.println("The message that is being sent to the server before encryption is: " + message);
        RSAPublicKey eServer = (RSAPublicKey) cert.getPublicKey();
        
        //Encrypt : server's public key 
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cipher.init(Cipher.ENCRYPT_MODE, eServer);
        byte[] cipherText = cipher.doFinal(message.getBytes());
        System.out.println("The ciphertext is: " + cipherText);
        os.writeObject(cipherText);
		os.flush();
		os.close();
	    s.close();
	    input.close();
	}
	

}
