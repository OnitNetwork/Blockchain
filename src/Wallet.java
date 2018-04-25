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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Wallet 
{
	//Generating Object for Keys
	public PrivateKey privateKey;
	public PublicKey publicKey;
	
	//HashMap Again
	public HashMap<String,TransactionOutput> UTXOs = new HashMap<String,TransactionOutput>(); 
	
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
	
	//Returns UTXO Balance Method
	public float getBalance() 
	{
		float total = 0;	
		
        for (Map.Entry<String, TransactionOutput> item:BlockChainMain.UTXOs.entrySet())
        {
        	TransactionOutput UTXO = item.getValue();
        	
            if(UTXO.isMine(publicKey)) 
            {
            	UTXOs.put(UTXO.id,UTXO); //add it to our list of unspent transactions.
            	
            	total += UTXO.value; 
            }
        }  
        
		return total;
	}
	
	//New Transaction Method
	public Transaction sendFunds(PublicKey _recipient,float value ) 
	{
		if(getBalance() < value) 
		{ 
			System.out.println("#Not Enough funds to complete transaction.");
			
			return null;
		}
		
    //Creating inputs
		ArrayList<TransactionInput> inputs = new ArrayList<TransactionInput>();
    
		float total = 0;
		for (Map.Entry<String, TransactionOutput> item: UTXOs.entrySet())
		{
			TransactionOutput UTXO = item.getValue();
			total = total + UTXO.value;
			inputs.add(new TransactionInput(UTXO.id));
			
			if(total > value) 
			{
				break;
			}
		}
		
		Transaction newTransaction = new Transaction(publicKey, _recipient , value, inputs);
		newTransaction.generateSignature(privateKey);
		
		for(TransactionInput input: inputs)
		{
			UTXOs.remove(input.transactionOutputId);
		}
		
		return newTransaction;
	}		
	
	
}
