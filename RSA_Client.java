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

public class RSA_Client {
	public static void main(String[] args) {
		String host = "127.0.0.1";
		int port = 7999;
		try {
			Socket s = new Socket(host, port);
			Scanner keyboard = new Scanner(System.in);
			int input = keyboard.nextInt();
			
			ObjectOutputStream Send_to_Server = new ObjectOutputStream(s.getOutputStream());   // Stream to send information to server
			ObjectInputStream Get_from_Server = new ObjectInputStream(s.getInputStream());     // Stream to get information from server
			KeyPairGenerator genKeyPair = KeyPairGenerator.getInstance("RSA");  // Key Pair Gen for RSA
			genKeyPair.initialize(1024 ,new SecureRandom()); 
			KeyPair keyPair = genKeyPair.genKeyPair(); 
		    RSAPublicKey Public_Key_Client = (RSAPublicKey) keyPair.getPublic();	// Client's Public Key
		    RSAPrivateKey Private_Key_Client = (RSAPrivateKey)keyPair.getPrivate();	// Client's Private Key
		    System.out.println("Client Public Key: " + Public_Key_Client);
		    System.out.println("Client Private Key: " + Private_Key_Client);
			System.out.println("1. RSA with only Confidentiality");
			System.out.println("2. RSA with only Integrity");
			System.out.println("3. RSA with both Confidentiality and Integrity");
			System.out.println("4. Exit");
		    Send_to_Server.writeObject(Public_Key_Client);		// Sending Public Key of Client to Server
		    RSAPublicKey Public_Key_Server = (RSAPublicKey) Get_from_Server.readObject();	// Receiving the Public Key of the Server
		    Cipher cipher = Cipher.getInstance("RSA/NONE/NoPadding");
		    Signature signature = Signature.getInstance("SHA256withRSA");		// Signature to sign
			switch(input)
			{
			case 1: 
				System.out.println("Enter the text that you want to send to the Server: ");
				String plaintext = keyboard.nextLine();
				cipher.init(Cipher.ENCRYPT_MODE, Public_Key_Server);
				byte[] ciphertext = cipher.doFinal(plaintext.getBytes("UTF_8"));
				System.out.println("The ciphertext generated is: " + ciphertext.toString());
				signature.initSign(Private_Key_Client, new SecureRandom());
				signature.update(plaintext.getBytes("UTF_8"));
				byte[] sig_ciphertext = signature.sign();
				Send_to_Server.write(1);
				Send_to_Server.write(ciphertext);
				Send_to_Server.write(sig_ciphertext);
			case 2:
				System.out.println("Enter the text that you want to send to the Server: ");
				plaintext = keyboard.nextLine();
				cipher.init(Cipher.ENCRYPT_MODE, Private_Key_Client);
				ciphertext = cipher.doFinal(plaintext.getBytes());
				System.out.println("The ciphertext generated is: " + ciphertext.toString());
				signature.initSign(Private_Key_Client, new SecureRandom());
				signature.update(ciphertext);
				sig_ciphertext = signature.sign();
				Send_to_Server.write(2);
				Send_to_Server.write(ciphertext);
				Send_to_Server.write(sig_ciphertext);
			case 3:
				System.out.println("Enter the text that you want to send to the Server: ");
				plaintext = keyboard.nextLine();
				cipher.init(Cipher.ENCRYPT_MODE, Public_Key_Server);
				byte[] ciphertext1 = cipher.doFinal(plaintext.getBytes("UTF_8"));
				cipher.init(Cipher.ENCRYPT_MODE, Private_Key_Client);
				byte[] ciphertext2 = cipher.doFinal(ciphertext1);
				System.out.println("The ciphertext generated is: " + ciphertext2.toString());
				signature.initSign(Private_Key_Client, new SecureRandom());
				signature.update(plaintext.getBytes("UTF_8"));
				sig_ciphertext = signature.sign();
				Send_to_Server.write(3);
				Send_to_Server.write(ciphertext2);
				Send_to_Server.write(sig_ciphertext);
			case 4:
				System.exit(0);
			default:
				System.out.println("Sorry you have entered a wrong option as a penalty you need to re-run the program :P");
				System.exit(0);
			}
		}catch(Exception e)
		{
			
		}

	}

}
