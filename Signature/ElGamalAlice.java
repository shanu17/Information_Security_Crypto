package Signature;

import java.io.*;
import java.net.*;
import java.security.*;
import java.math.BigInteger;

public class ElGamalAlice
{
	private static BigInteger computeY(BigInteger p, BigInteger g, BigInteger d)
	{
		return g.modPow(d, p); // g^d mod p
	}

	private static BigInteger computeK(BigInteger p)
	{
		SecureRandom rand = new SecureRandom();
		int bits = 1024;
		BigInteger k = new BigInteger(bits,rand); // length of 1024 bits
		while(!k.gcd(p.subtract(BigInteger.ONE)).equals(BigInteger.ONE))
		{
			k = new BigInteger(bits,rand);
		}
		return k;
	}
	
	private static BigInteger computeA(BigInteger p, BigInteger g, BigInteger k)
	{
		return g.modPow(k, p); // g^k mod p
	}

	private static BigInteger computeB(String message, BigInteger d, BigInteger a, BigInteger k, BigInteger p)
	{
		BigInteger message_int = new BigInteger(message.getBytes());
		BigInteger p_sub1 = p.subtract(BigInteger.ONE);
		BigInteger p1 = p_sub1;
		BigInteger x0 = BigInteger.ZERO;//0
		BigInteger x1 = BigInteger.ONE;//1
		BigInteger x2 = k;
		BigInteger z, z2, z3;
		while(!x2.equals(BigInteger.ZERO))
		{
			z = p_sub1.divide(x2);      //p1/x2
			z2 = p_sub1.subtract(x2.multiply(z));         //p1-x2*z
			p_sub1 = x2;
			x2 = z2;
			z3 = x0.subtract(x1.multiply(z));	//x0-x1*z
			x0 = x1;
			x1 = z3;
		}
		
		BigInteger b = x0.multiply(message_int.subtract(d.multiply(a))).mod(p1);
		//b = ((m-da)/k) mod (p-1) 
		
		return b;
	}

	public static void main(String[] args) throws Exception 
	{
		String message = "The quick brown fox jumps over the lazy dog.";

		String host = "127.0.0.1";
		int port = 7999;
		Socket s = new Socket(host, port);
		ObjectOutputStream os = new ObjectOutputStream(s.getOutputStream());

		// You should consult BigInteger class in Java API documentation to find out what it is.
		BigInteger y, g, p; // public key
		BigInteger d; // private key

		int mStrength = 1024; // key bit length
		SecureRandom mSecureRandom = new SecureRandom(); // a cryptographically strong pseudo-random number

		// Create a BigInterger with mStrength bit length that is highly likely to be prime.
		// (The '16' determines the probability that p is prime. Refer to BigInteger documentation.)
		p = new BigInteger(mStrength, 16, mSecureRandom);
		
		// Create a randomly generated BigInteger of length mStrength-1
		g = new BigInteger(mStrength-1, mSecureRandom);
		d = new BigInteger(mStrength-1, mSecureRandom);

		y = computeY(p, g, d);

		// At this point, you have both the public key and the private key. Now compute the signature.

		BigInteger k = computeK(p);
		BigInteger a = computeA(p, g, k);
		BigInteger b = computeB(message, d, a, k, p);

		// send public key
		os.writeObject(y);
		os.writeObject(g);
		os.writeObject(p);

		// send message
		os.writeObject(message);
		
		// send signature
		os.writeObject(a);
		os.writeObject(b);
		
		s.close();
	}
}