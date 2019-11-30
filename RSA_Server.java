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
		    	
		    }
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
