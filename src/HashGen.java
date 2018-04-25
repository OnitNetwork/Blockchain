/*
 * Name: 		  Eshan Sharma
 * Last Addition: 4/23/18 @ 5:29 PM.  
 * 
 * ----Readme----
 * This is where all the magic happens. So I went with Sha256 crypto encryption as I found on Reddit and other soruces
 * that this is the easiest one to work with and its easy to implement. This class simply takes a string and converts
 * it into a hash using Sha256. This now also contains methods that create ESDCA signatures and verifies them. Also has
 * getter method for the signiture as well (encoded).
 * 
 */

import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Base64;
import java.security.*;

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
	
	//Applies ECDSA Signature and returns a result (as bytes) for the main transaction signature
	public static byte[] applyECDSASig(PrivateKey privateKey, String input)
	{
		Signature dsa;
		
		byte[] output = new byte[0];
		
		try 
		{
			dsa = Signature.getInstance("ECDSA", "BC");
			dsa.initSign(privateKey);
			byte[] strByte = input.getBytes();
			dsa.update(strByte);
			byte[] realSig = dsa.sign();
			output = realSig;
		}
		catch(Exception y)
		{
			throw new RuntimeException(y);
		}
		
		return output;	
	}
	
	//This will verify the signature after every transaction
	public static boolean verifyECDSASig(PublicKey publicKey, String data, byte[] signature)
	{
		try 
		{
			Signature ecdsaVerify = Signature.getInstance("ECDSA", "BC");
			ecdsaVerify.initVerify(publicKey);
			ecdsaVerify.update(data.getBytes());
			
			return ecdsaVerify.verify(signature);
		}
		catch(Exception a)
		{
			throw new RuntimeException(a);
		}
	}
	
	//Returns a Merkle Root (I dont know what this does but we need it. Ask Jonathan for explanation)
	public static String getMerkleRoot(ArrayList<Transaction> transactions) 
	{
		int count = transactions.size();
		ArrayList<String> previousTreeLayer = new ArrayList<String>();
		
		for(Transaction transaction : transactions) 
		{
			previousTreeLayer.add(transaction.transactionId);
		}
		
		ArrayList<String> treeLayer = previousTreeLayer;
		
		while(count > 1) 
		{
			treeLayer = new ArrayList<String>();
			
			for(int i=1; i < previousTreeLayer.size(); i++) 
			{
				treeLayer.add(translate(previousTreeLayer.get(i-1) + previousTreeLayer.get(i)));
			}
			
			count = treeLayer.size();
			previousTreeLayer = treeLayer;
		}
		
		String merkleRoot = (treeLayer.size() == 1) ? treeLayer.get(0) : "";
		
		return merkleRoot;
	}
	
	//Getter Method
	public static String getStringFromKey(Key key) 
	{
		return Base64.getEncoder().encodeToString(key.getEncoded());
	}
	
	
	
}
