import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.SecureRandom;
import java.security.Signature;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

import javax.crypto.Cipher;

public class RSA_server {

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
		    Send_to_Client.writeObject(Public_Key_Server); // Send public key to Client
		    Envelope msg = (Envelope) Get_from_Client.readObject();
		    RSAPublicKey Public_Key_Client = (RSAPublicKey) msg.getObjContents().get(0);
		    byte[] ciphertext = (byte[]) msg.getObjContents().get(1);
		    byte[] sig_ciphertext = (byte[]) msg.getObjContents().get(2);
		    int input = (int) msg.getObjContents().get(3);
		    Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
		    Signature signature = Signature.getInstance("SHA256withRSA");
		    if(input == 1)
		    {
		    	cipher.init(Cipher.DECRYPT_MODE, Private_Key_Server);
		    	String plaintext = new String(cipher.doFinal(ciphertext));
			    signature.initVerify(Public_Key_Client);
			    signature.update(plaintext.getBytes());
			    if(signature.verify(sig_ciphertext))
			    {
			    	System.out.println("The decrypted cipher text is  :  " + plaintext);
			    	System.out.println("THe signature is correct");
			    }
			    else
			    {
			    	System.out.println("THe signature is not correct but the message is correct");
			    }
		    }
		    else if(input==2)
		    {
		    	cipher.init(Cipher.DECRYPT_MODE, Public_Key_Client);
		    	String plaintext = new String(cipher.doFinal(ciphertext));
			    signature.initVerify(Public_Key_Client);
			    signature.update(plaintext.getBytes());
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
		    else if(input ==3)
		    {
		    	cipher.init(Cipher.DECRYPT_MODE, Private_Key_Server);
		    	String plaintext = new String(cipher.doFinal(ciphertext));
			    signature.initVerify(Public_Key_Client);
			    signature.update(plaintext.getBytes());
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
