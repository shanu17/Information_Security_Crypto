import java.io.*;
import java.net.*;
import java.security.KeyStore;
import java.security.PrivateKey;

import javax.crypto.Cipher;

public class X509_Server {

	public static void main(String[] args) {
		
		String aliasname="selfsigned";
        char[] password="password".toCharArray();
		
        int port = 7999;
        try {
    		ServerSocket server = new ServerSocket(port);
    		Socket s = server.accept();
    		ObjectInputStream is = new ObjectInputStream(s.getInputStream());
    	    
    		
            KeyStore ks = KeyStore.getInstance("JKS");
            ks.load(new FileInputStream("keystore.jks"), password);
            PrivateKey dServer = (PrivateKey)ks.getKey(aliasname, password);
           
            
            Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            byte[] in = (byte[]) is.readObject();
    		cipher.init(Cipher.DECRYPT_MODE, dServer);
    		byte[] plaintText = cipher.doFinal(in);
    		System.out.println("The message from the client is: " + new String(plaintText));
    		server.close();
        }catch(Exception e)
        {
        	
        }
	}

	

}
