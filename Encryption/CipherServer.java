import java.io.*;
import java.net.*;
import java.security.*;
import javax.crypto.*;

public class CipherServer
{
	public void decrypt(Key key, InputStream inStream)
	{
		try {
			Cipher dcipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
			dcipher.init(Cipher.DECRYPT_MODE, key);
			DataInputStream in = new DataInputStream(inStream);
			byte[] ciphertext = in.readAllBytes();
			byte[] plain = dcipher.doFinal(ciphertext);
			String plaintext = new String(plain);
			System.out.println("The decrypted plaintext is:\t" + plaintext);
		}catch(Exception e)
		{
			System.out.println("In decrypt" + e);
		}
	}
	public static void main(String[] args) throws Exception 
	{
		int port = 7999;
		ServerSocket server = new ServerSocket(port);
		Socket s = server.accept();
		try {
            FileInputStream fileIn = new FileInputStream("KeyStore.txt");
            ObjectInputStream objectIn = new ObjectInputStream(fileIn);
            Key key = (Key) objectIn.readObject();
			CipherServer bob = new CipherServer();
			bob.decrypt(key, s.getInputStream());
			objectIn.close();
		}catch(Exception e)
		{
			System.out.println("Something is wrong" + e);
		}
		server.close();
	}
}