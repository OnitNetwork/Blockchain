/*
 * Name: 		  Eshan Sharma
 * Last Addition: 2/23/18 @ 3:42 PM.
 * 
 * ----Readme----
 * Prepare for the paragraphs in the comment section. The main method will be executing all the code (Duh). The main method will now mine as many blocks as we please.
 * Currently I have initialized 4 blocks with a 0 difficulty for the sake of time as on school computers for testing, even a difficulty of 1 takes so long. This uses
 * proof of work to acquire the token and presents you with the Hash at first and then outputs the block chain at the end. 
 *
 * 
 * NOTE: This does require that special library. More info on that More Information about the library can be found at https://github.com/google/gson.
 */

import java.util.*;

//This user library can be found from https://repo1.maven.org/maven2/com/google/code/gson/gson/2.6.2/gson-2.6.2.jar
//It can be used for peer2peer things (haven't figured that part out yet but we can use whatever we want. I just found this online on reddit as users
//stated this would be the best one for creating a blockchain. If you have anything better, let me know.
import com.google.gson.GsonBuilder;


public class BlockChainMain 
{
	//Creates the "Chain" of the block chain. This will link all of the hashes together.
	//Being a static method, any changes made to a hash will result in this whole chain breaking. Which is intended!
	//I have to still add a 
	public static ArrayList<Block> twoChainz = new ArrayList<Block>();
	
	//I set a static difficulty of 1 as it was recommended by the person who taught me how to write this. You can change the difficulty as you please to check the times.
	public static int difficulty = 5;
	
	public static void main (String[] args)
	{
		//Mine one block - First
		twoChainz.add(new Block ("First Block: ", "0")); //Genesis Block
		System.out.println("*Mining First Block*");
		twoChainz.get(0).mineBlock(difficulty);
		System.out.println("\n");
		
		//Mine two blocks - Second
		twoChainz.add(new Block("Second Block: ",twoChainz.get(twoChainz.size()-1).hash));
		System.out.println("*Mining Second Block*");
		twoChainz.get(1).mineBlock(difficulty);
		System.out.println("\n");
		
		//Mine three blocks - Third
		twoChainz.add(new Block("Third Block: ",twoChainz.get(twoChainz.size()-1).hash));
		System.out.println("*Mining Third Block*");
		twoChainz.get(2).mineBlock(difficulty);
		System.out.println("\n");
		
		//Mine four blocks - Forth
		twoChainz.add(new Block("Forth Block: ",twoChainz.get(twoChainz.size()-1).hash));
		System.out.println("*Mining Forth Block*");
		twoChainz.get(3).mineBlock(difficulty);
		System.out.println("\n");
		
		
		//Check Validity of the blockchain
		System.out.println("\nBlockchain is Valid: " + isChainValid());
		
		//Output
		String twoChainzJson = new GsonBuilder().setPrettyPrinting().create().toJson(twoChainz);
		
		System.out.println("\n\nThe Block Chain Currently ****************************************************************************************************************************************************************************************** ");
		
		System.out.println(twoChainzJson);
		
	}
	
	//Changes made to this!
	public static Boolean isChainValid() 
	{
		Block currentBlock; 
		Block previousBlock;
		String hashTarget = new String(new char[difficulty]).replace('\0', '0');
		
		//loop through blockchain to check hashes:
		for(int i=1; i < twoChainz.size(); i++) 
		{
			currentBlock = twoChainz.get(i);
			previousBlock = twoChainz.get(i-1);
			
			//compare registered hash and calculated hash:
			if(!currentBlock.hash.equals(currentBlock.convertToHash()))
			{
				System.out.println("Current Hashes not equal");			
				
				return false;
			}
			//compare previous hash and registered previous hash
			if(!previousBlock.hash.equals(currentBlock.prevHash)) 
			{
				System.out.println("Previous Hashes not equal");
				
				return false;
			}
				
			//check if hash is solved
			if(!currentBlock.hash.substring( 0, difficulty).equals(hashTarget)) 
			{
				System.out.println("This block hasn't been mined");
					
				return false;
			}
		}
		return true;
	}
}
