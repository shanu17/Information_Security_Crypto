import java.io.*;
import java.net.*;
import java.security.*;
import java.util.Date;

public class ProtectedClient
{
	//@SuppressWarnings("static-access")
	public void sendAuthentication(String user, String password, OutputStream outStream) throws IOException, NoSuchAlgorithmException, NoSuchProviderException 
	{
		DataOutputStream out = new DataOutputStream(outStream);
		Date date = new Date();
		SecureRandom secureRandomGenerator = SecureRandom.getInstance("SHA1PRNG", "SUN");
		double rand1 = secureRandomGenerator.nextDouble();
		byte[] first = Protection.makeDigest(user, password, date.getTime(), rand1);
		double rand2 = secureRandomGenerator.nextDouble();
		byte[] second = Protection.makeDigest(first, date.getTime(), rand2);
		out.write(second);
		out.flush();
	}

	public static void main(String[] args) throws Exception 
	{
		String host = "paradox.sis.pitt.edu";
		int port = 7999;
		String user = "George";
		String password = "abc123";
		Socket s = new Socket(host, port);

		ProtectedClient client = new ProtectedClient();
		client.sendAuthentication(user, password, s.getOutputStream());

		s.close();
	}
}