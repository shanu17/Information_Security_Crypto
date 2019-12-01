package Public_Key_System;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.SecureRandom;
import java.security.Signature;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.Scanner;

import javax.crypto.Cipher;

public class RSA_client {
	public static void main(String[] args) {
		String host = "127.0.0.1";
		int port = 7999;
		Envelope msg = new Envelope();
		try {
			Socket s = new Socket(host, port);
			Scanner keyboard = new Scanner(System.in);
			ObjectOutputStream Send_to_Server = new ObjectOutputStream(s.getOutputStream());   // Stream to send information to server
			ObjectInputStream Get_from_Server = new ObjectInputStream(s.getInputStream());     // Stream to get information from server
			KeyPairGenerator genKeyPair = KeyPairGenerator.getInstance("RSA");  // Key Pair Gen for RSA
			genKeyPair.initialize(1024 ,new SecureRandom()); 
			KeyPair keyPair = genKeyPair.genKeyPair(); 
		    RSAPublicKey Public_Key_Client = (RSAPublicKey) keyPair.getPublic();	// Client's Public Key
		    RSAPrivateKey Private_Key_Client = (RSAPrivateKey)keyPair.getPrivate();
		    RSAPublicKey Public_Key_Server = (RSAPublicKey) Get_from_Server.readObject();
		    System.out.println("Client Public Key: " + Public_Key_Client);
		    System.out.println("Client Private Key: " + Private_Key_Client);
		    Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
		    Signature signature = Signature.getInstance("SHA256withRSA");
			System.out.println("1. RSA with only Confidentiality");
			System.out.println("2. RSA with only Integrity");
			System.out.println("3. RSA with both Confidentiality and Integrity");
			System.out.println("4. Exit");
			System.out.println("Enter the text that you want to send to the Server: ");
			String plaintext = keyboard.nextLine();
			System.out.print("Enter your choice: ");
			int input = keyboard.nextInt();
			keyboard.nextLine();
			
			if(input == 1)
			{
			    cipher.init(Cipher.ENCRYPT_MODE, Public_Key_Server);
				byte[] ciphertext = cipher.doFinal(plaintext.getBytes());
				System.out.println("The ciphertext generated is: " + ciphertext.toString());
				signature.initSign(Private_Key_Client, new SecureRandom());
				signature.update(plaintext.getBytes());
				byte[] sig_ciphertext = signature.sign();
				System.out.println("The signature generated is: " + sig_ciphertext.toString());
				msg.addObject(Public_Key_Client);
				msg.addObject(ciphertext);
				msg.addObject(sig_ciphertext);
				msg.addObject(input);
				Send_to_Server.writeObject(msg);
				Send_to_Server.flush();
				s.close();
				keyboard.close();
			}
			else if(input==2)
			{
			    cipher.init(Cipher.ENCRYPT_MODE, Private_Key_Client);
				byte[] ciphertext = cipher.doFinal(plaintext.getBytes());
				System.out.println("The ciphertext generated is: " + ciphertext.toString());
				signature.initSign(Private_Key_Client, new SecureRandom());
				signature.update(plaintext.getBytes());
				byte[] sig_ciphertext = signature.sign();
				System.out.println("The signature generated is: " + sig_ciphertext.toString());
				msg.addObject(Public_Key_Client);
				msg.addObject(ciphertext);
				msg.addObject(sig_ciphertext);
				msg.addObject(input);
				Send_to_Server.writeObject(msg);
				Send_to_Server.flush();
				s.close();
				keyboard.close();
			}
			else if(input==3)
			{
			    cipher.init(Cipher.ENCRYPT_MODE, Public_Key_Server);
				byte[] ciphertext = cipher.doFinal(plaintext.getBytes());
				System.out.println("The ciphertext generated is: " + ciphertext.toString());
				signature.initSign(Private_Key_Client, new SecureRandom());
				signature.update(plaintext.getBytes());
				byte[] sig_ciphertext = signature.sign();
				System.out.println("The signature generated is: " + sig_ciphertext.toString());
				msg.addObject(Public_Key_Client);
				msg.addObject(ciphertext);
				msg.addObject(sig_ciphertext);
				msg.addObject(input);
				Send_to_Server.writeObject(msg);
				Send_to_Server.flush();
				s.close();
				keyboard.close();
			}
		}catch(Exception e)
		{
			
		}

	}

}
