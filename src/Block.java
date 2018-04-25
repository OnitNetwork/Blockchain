/*
 * Name: 		  Eshan Sharma
 * Last Addition: 4/23/18 @ 6:22 PM..
 * 
 * ----Readme----
 * Block.Java will create the object and the constructor will be storing the object while accepting two parameters.
 * The data itself, and a link to the previous hash value. The genesis which is the first block will not have a previous
 * hash value. It will be set to 0 in the main method. 
 * 
 * Modified again to handle transactions using UTXOs
 * 
 */

import java.util.*;

public class Block 
{
	//Initializing Variables
	public String hash;
	public String prevHash;
	private long timeStamp;  //For tracking time periods
	private int non;
	public String merkleRoot;
	
	//List of transactions. Still using a message for an example.
	public ArrayList<Transaction> transactions = new ArrayList<Transaction>(); 
	
	
	//Constructor
	public Block(String prevHash)
	{
		this.prevHash = prevHash;
		this.timeStamp = new Date().getTime(); //Generates the time and date
		this.hash = convertToHash();           //Method Call. Apparently This has to be called last. Apparently -_- 
	}
	
	//This method will actually take the input string and convert it to a hash
	public String convertToHash()
	{
		String h = HashGen.translate(prevHash + Long.toString(timeStamp) + Integer.toString(non) + merkleRoot);
		
		return h;
	}
	
	//Mining Block Algorithm
	public void mineBlock(int difficulty)
	{
		//This creates a string with a 0 difficulty value
		String target = new String(new char[difficulty]).replace('\0', '0');
		
		//Dat Merkle Root
		merkleRoot = HashGen.getMerkleRoot(transactions);
		
		while(!hash.substring(0, difficulty).equals(target))
		{
			non ++;
			hash = convertToHash();
		}
		
		//Output
		System.out.println("Mined Block: " + hash);
	}
	
	//Adding a blocks to a transaction
	public boolean addTransaction(Transaction transaction) 
	{
		//process transaction and check if valid, unless block is genesis block then ignore.
		if(transaction == null) 
		{
			return false;
		}
		
		if((prevHash != "0")) 
		{
			if((transaction.processTransaction() != true)) 
			{
				System.out.println("Transaction failed to process. Discarded.");
				
				return false;
			}
		}
		
		transactions.add(transaction);
		
		System.out.println("Transaction added to Block");
		
		return true;
	
	}
	
}
