/*
 * Name: 		  Eshan Sharma
 * Last Addition: 2/23/18 @ 10:34 AM.
 * 
 * ----Readme----
 * Block.Java will create the object and the constructor will be storing the object while accepting two parameters.
 * The data itself, and a link to the previous hash value. The genesis which is the first block will not have a previous
 * hash value. It will be set to 0 in the main method. 
 * 
 * Eventually I need to add a way where this class or the HashGen class will check to see if a previous has value has been
 * changed or not. 
 * 
 * Note: The timestamp variable is currently calculates the milliseconds since 1/1/1970. Not a very efficient way to keep
 * track of time but I did not know how to do it and I just looked through the API and found something for now. Since
 * it counts from 1/1/1970 till now, the larger value will be newer if you look at the sequence of hashes.  
 * 
 */

import java.util.*;

public class Block 
{
	//Initializing Variables
	public String hash;
	public String prevHash;
	private String data;     //For Testing Purposes, This will just be a sentence or a word
	private long timeStamp;  //For tracking time periods
	private int non;
	
	//Constructor
	public Block(String data, String prevHash)
	{
		this.data = data;
		this.prevHash = prevHash;
		this.timeStamp = new Date().getTime(); //Generates the time and date
		this.hash = convertToHash();           //Method Call. Apparently This has to be called last. Apparently -_- 
	}
	
	//This method will actually take the input string and convert it to a hash
	public String convertToHash()
	{
		String h = HashGen.translate(prevHash + Long.toString(timeStamp) + Integer.toString(non) + data);
		
		return h;
	}
	
	//Mining Block Algorithm
	public void mineBlock(int difficulty)
	{
		//This creates a string with a 0 difficulty value
		String target = new String(new char[difficulty]).replace('\0', '0');
		
		while(!hash.substring(0, difficulty).equals(target))
		{
			non ++;
			hash = convertToHash();
		}
		
		//Output
		System.out.println("Mined Block: " + hash);
	}
	
}
