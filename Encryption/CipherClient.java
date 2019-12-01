package Encryption;

import java.io.*;
import java.net.*;
import java.security.*;
import java.io.File;

import javax.crypto.*;

public class CipherClient implements Serializable
{
	private static final long serialVersionUID = 1L;
	public Key create_keyfile()
	{
		try {
			Key key = KeyGenerator.getInstance("DES").generateKey();
			File file = new File("KeyStore.txt");
			file.createNewFile();
			FileOutputStream fos = new FileOutputStream("KeyStore.txt");
			ObjectOutputStream objectOut = new ObjectOutputStream(fos);
			objectOut.writeObject(key);
			objectOut.close();
			fos.close();
			return key;
		}catch(Exception e)
		{
			System.out.println(e);
		}
		return null;
	}
	public void send_ciphertext(String message, OutputStream outStream, Key key)
	{
		try {
            Cipher ecipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
            ecipher.init(Cipher.ENCRYPT_MODE, key);
            byte [] CipherText = ecipher.doFinal(message.getBytes());
            
            DataOutputStream out = new DataOutputStream(outStream);
            out.write(CipherText);
            out.flush();
            out.close();
		}catch (Exception e) {
			
		}
	}
	public static void main(String[] args) throws Exception 
	{
		String message = "The quick brown fox jumps over the lazy dog.";
		String host = "127.0.0.1";
		int port = 7999;
		CipherClient alice = new CipherClient();
		Key key = alice.create_keyfile();
		Socket s = new Socket(host, port);
		alice.send_ciphertext(message,s.getOutputStream(), key);
		s.close();
	}
}