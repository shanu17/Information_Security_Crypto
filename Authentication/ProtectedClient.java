package Authentication;

import java.io.*;
import java.net.*;
import java.security.*;
import java.util.Date;

public class ProtectedClient
{
	public void sendAuthentication(String user, String password, OutputStream outStream) throws IOException, NoSuchAlgorithmException, NoSuchProviderException 
	{
		DataOutputStream out = new DataOutputStream(outStream);
		Date date = new Date();
		SecureRandom secureRandomGenerator = SecureRandom.getInstance("SHA1PRNG", "SUN");
		double rand1 = secureRandomGenerator.nextDouble(); // Random number 1
		long t1 = date.getTime(); // TimeStamp1
		byte[] first = Protection.makeDigest(user, password, t1, rand1); // Digest 1
		double rand2 = secureRandomGenerator.nextDouble(); // Random number 2
		long t2 = date.getTime(); // TimeStamp 2
		byte[] second = Protection.makeDigest(first, t2, rand2); // Digest 2
		out.writeUTF(user);
		out.writeDouble(rand1);
		out.writeLong(t1);
		out.writeDouble(rand2);
		out.writeLong(t2);
		out.write(second);
		
		out.flush();
	}

	public static void main(String[] args) throws Exception 
	{
		String host = "127.0.0.1";
		int port = 7999;
		String user = "George";
		String password = "abc123";
		Socket s = new Socket(host, port);

		ProtectedClient client = new ProtectedClient();
		client.sendAuthentication(user, password, s.getOutputStream());

		s.close();
	}
}