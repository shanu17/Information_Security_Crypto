package Message_Digest;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;


public class message_digest {
	
	public static String bytesToHex(byte[] in) {
	    final StringBuilder builder = new StringBuilder();
	    for(byte b : in) {
	        builder.append(String.format("%02x", b));
	    }
	    return builder.toString();
	}

	public static void main(String[] args) {
		
		System.out.println("Enter string to be hashed:");
		Scanner input = new Scanner (System.in);
		String message = input.nextLine();
		input.close();
		MessageDigest messageDigest;
		
		
		try {
			byte[] message_b = message.getBytes("UTF-8");
			messageDigest = MessageDigest.getInstance("SHA-256");
			byte[] digest = messageDigest.digest(message_b);
			System.out.print("Hash SHA-256(In Hex):\t");
			System.out.println(bytesToHex(digest));
			messageDigest = MessageDigest.getInstance("MD5");
			digest = messageDigest.digest(message_b);
			System.out.print("Hash MD5(In Hex):\t");
			System.out.println(bytesToHex(digest));
		} catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}
	

}
