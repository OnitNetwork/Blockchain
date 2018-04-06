/*
 * Name: 		  Eshan Sharma
 * Last Addition: 4/5/18 @ 12:56 PM.  
 * 
 * ----Readme----
 * This class uses Elliptic-curve cryptography to generate a pair of private and public keys.
 * 
 * You are going to have to download and import Bouncy Castle for this. You to import this:
 * https://www.bouncycastle.org/latest_releases.html to make this class work. 
 * 
 */

import java.security.*;
import java.security.spec.ECGenParameterSpec;

public class Wallet 
{
	//Generating Object for Keys
	public PrivateKey privateKey;
	public PublicKey publicKey;
	
	//Constructor
	public Wallet()
	{
		generateKeyPair();
	}
	
	//Method to generate a key pair and then separate the keys
	public void generateKeyPair()
	{
		try 
		{
			KeyPairGenerator keyGen = KeyPairGenerator.getInstance("ECDSA", "BC");
			SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
			ECGenParameterSpec ecSpec = new ECGenParameterSpec("prime192v1");
			
			//Starts the key generator to make a new pair
			keyGen.initialize(ecSpec, random);
			KeyPair keyPair = keyGen.generateKeyPair();
			
			//Separates and assigns the private and public keys
			privateKey = keyPair.getPrivate();
			publicKey = keyPair.getPublic();
		}
		catch(Exception x)
		{
			throw new RuntimeException(x);
		}
		
	}
	
}
