import java.io.IOException;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.Signature;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

import javax.crypto.Cipher;

public class RSA_Server {

	public static void main(String[] args) {
		int port = 7999;
		try {
			ServerSocket server = new ServerSocket(port);
			Socket s = server.accept();
			
			ObjectInputStream Get_from_Client = new ObjectInputStream(s.getInputStream());
			ObjectOutputStream Send_to_Client = new ObjectOutputStream(s.getOutputStream());
			KeyPairGenerator genKeyPair = KeyPairGenerator.getInstance("RSA");  // Key Pair Gen for RSA
			genKeyPair.initialize(1024 ,new SecureRandom()); 
			KeyPair keyPair = genKeyPair.genKeyPair(); 
		    RSAPublicKey Public_Key_Server = (RSAPublicKey) keyPair.getPublic();	// Server's Public Key
		    RSAPrivateKey Private_Key_Server = (RSAPrivateKey)keyPair.getPrivate();	// Server's Private Key
		    System.out.println("Server's Public Key: " + Public_Key_Server);
		    System.out.println("Server's Private Key: " + Private_Key_Server);
		    RSAPublicKey Public_Key_Client = (RSAPublicKey) Get_from_Client.readObject();	// Read Client's Public Key
		    Send_to_Client.writeObject(Private_Key_Server);
		    int input = Get_from_Client.readInt();
		    Cipher cipher = Cipher.getInstance("RSA/NONE/NoPadding");
		    Signature signature = Signature.getInstance("SHA256withRSA");
		    switch(input)
		    {
		    case 1:
		    	byte[] ciphertext = Get_from_Client.readAllBytes();
		    	byte[] sig_ciphertext = Get_from_Client.readAllBytes();
		    	cipher.init(Cipher.DECRYPT_MODE, Private_Key_Server);
		    	String plaintext = new String(cipher.doFinal(ciphertext));
		    	  
			    signature.initVerify(Public_Key_Client);
			    signature.update(plaintext.getBytes("UTF_8"));
			    if(signature.verify(sig_ciphertext))
			    {
			    	System.out.println("The decrypted cipher text is  :  " + plaintext);
			    	System.out.println("THe signature is correct");
			    }
			    else
			    {
			    	System.out.println("THe signature is not correct");
			    }
		    case 2:
		    	ciphertext = Get_from_Client.readAllBytes();
		    	sig_ciphertext = Get_from_Client.readAllBytes();
		    	cipher.init(Cipher.DECRYPT_MODE, Public_Key_Client);
		    	plaintext = new String(cipher.doFinal(ciphertext));
		    	  
			    signature.initVerify(Public_Key_Client);
			    signature.update(plaintext.getBytes("UTF_8"));
			    if(signature.verify(sig_ciphertext))
			    {
			    	System.out.println("The decrypted cipher text is  :  " + plaintext);
			    	System.out.println("THe signature is correct");
			    }
			    else
			    {
			    	System.out.println("THe signature is not correct");
			    }
		    }
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
