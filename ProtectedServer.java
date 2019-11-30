import java.io.*;
import java.net.*;
import java.security.*;
import java.util.Arrays;

public class ProtectedServer
{
	public boolean authenticate(InputStream inStream) throws IOException, NoSuchAlgorithmException 
	{
		DataInputStream in = new DataInputStream(inStream);
		
		String username = in.readUTF();
		double rand1 = in.readDouble();
		long t1 = in.readLong();
		double rand2 = in.readDouble();
		long t2 = in.readLong();
		byte[] data = in.readAllBytes();
		
		String password = lookupPassword(username);
		
		byte[] first_server = Protection.makeDigest(username, password, t1, rand1);
		byte[] second_server = Protection.makeDigest(first_server, t2, rand2);
		if(Arrays.equals(data,second_server))
		{
			return true;
		}
		else {
			return false;
		}
		// IMPLEMENT THIS FUNCTION.
		
	}

	protected String lookupPassword(String user) { return "abc123"; }

	public static void main(String[] args) throws Exception 
	{
		int port = 7999;
		ServerSocket s = new ServerSocket(port);
		Socket client = s.accept();

		ProtectedServer server = new ProtectedServer();

		if (server.authenticate(client.getInputStream()))
		  System.out.println("Client logged in.");
		else
		  System.out.println("Client failed to log in.");

		s.close();
	}
}