/*
 * Name: 		  Eshan Sharma
 * Last Addition: 4/5/18 @ 12:56 PM.  
 * 
 * ----Readme----
 * This is where all the magic happens. So I went with Sha256 crypto encryption as I found on Reddit and other soruces
 * that this is the easiest one to work with and its easy to implement. This class simply takes a string and converts
 * it into a hash using Sha256.
 * 
 */

import java.security.MessageDigest;

public class HashGen 
{
	//This method will apply Sha256 to an input string, int or word which is a cryptographic hash function
	public static String translate (String input)
	{
		try 
		{
			MessageDigest digest = MessageDigest.getInstance("SHA-256");
			
			//This where the conversation takes place
			byte[] hash = digest.digest(input.getBytes("UTF-8"));
			
			//Store the hash as a hexadecimal
			StringBuffer hexString = new StringBuffer(); 
			
			for (int i = 0; i < hash.length; i++)
			{
				String hex = Integer.toHexString(0xff & hash[i]);
				if(hex.length() == 1) hexString.append('0');
				hexString.append(hex);
			}
			
			return hexString.toString(); //Calling the build in java method. No use for a custom one right now.
		}
		catch(Exception n)
		{
			throw new RuntimeException(n);
		}
		
	}
	
}
