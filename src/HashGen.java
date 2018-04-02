import java.security.MessageDigest;

/*
 * Name: 		  Eshan Sharma
 * Last Addition: 2/23/18 @ 2/23/18 PM.  
 * 
 * ----Readme----
 * This is where all the magic happens. So I went with Sha256 crypto encryption as I found on Reddit and other soruces
 * that this is the easiest one to work with and its easy to implement. This class simply takes a string and converts
 * it into a hash using Sha256.
 * 
 * Note: Originally, I did not have a try and catch block, but when I did not have it in that exact way, nothing would
 * compile and throw exceptions. I got the try and catch block as someone pointed me to someone who had a VERY similar
 * conversation method (since they were based of the videos in our project proposal) the try and catch was his solution
 * to the compiling issue. I personally could not figure out a different way so I used his for now till we can improve
 * on it. Also, turns out that for a lot of people starting out, the method I wrote ends up being similar to many others
 * as a lot of tutorial videos and books on the topic have a similar explanation on how it works at a basic level. Regardless
 * at the most basic level, it does what we need it to do for now. Any improvements are welcome.
 * 
 */

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
