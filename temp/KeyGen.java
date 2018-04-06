package test;

import java.io.IOException;
import java.security.*;
import java.security.spec.ECGenParameterSpec;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.util.encoders.Base64;


import entity.HashGen;

public class KeyGen {
	
	public static  PrivateKey privateKey;
	public static PublicKey publicKey;
	
	public KeyGen(){
		generateKeyPair();	
	}
		
	public static void generateKeyPair() {
		
		Security.addProvider(new BouncyCastleProvider());
		
		try {
			KeyPairGenerator keyGen = KeyPairGenerator.getInstance("ECDSA", "BC");
			SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
			ECGenParameterSpec ecSpec = new ECGenParameterSpec("prime192v1");
			// Initialize the key generator and generate a KeyPair
			keyGen.initialize(ecSpec, random);   //256 bytes provides an acceptable security level
	        	KeyPair keyPair = keyGen.generateKeyPair();
	        	// Set the public and private keys from the keyPair
	        	privateKey = keyPair.getPrivate();
	        	publicKey = keyPair.getPublic();
		}catch(Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public static void main(String [] args) throws IOException {
		
		
		
		generateKeyPair(); 
		System.out.println(privateKey);
		System.out.println(publicKey);
		
		
		/**** BELOW IS WHAT NEEDS TO GET STORED AND DISPLAYED ON THE WEBSITE ******/
		
		//Private key
		System.out.println(new String(Base64.encode(privateKey.getEncoded())));
		//Public Key
		System.out.println(new String(Base64.encode(publicKey.getEncoded())));
		//Public address
		System.out.println(HashGen.translate(new String(Base64.encode(publicKey.getEncoded()))));
		
        
	}
	
}
