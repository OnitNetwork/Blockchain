/*
 * Name: 		  Eshan Sharma
 * Last Addition: 4/23/18 @ 6:04 PM.
 * 
 * ----Readme----
 * Prepare for the paragraphs in the comment section. The main method will be executing all the code. The main method will now mine as many blocks as we please.
 * Currently I have initialized 4 blocks with a 0 difficulty for the sake of time as on school computers for testing, even a difficulty of 1 takes so long. This uses
 * proof of work to acquire the token and presents you with the Hash at first and then outputs the block chain at the end. 
 *
 * NOTE: This does require that special library. More info on that More Information about the library can be found at https://github.com/google/gson.
 * NOTE: You now also need Bouncy castle for this too: https://www.bouncycastle.org/latest_releases.html
 * 
 * The driver was edited to do transactions and it currently is supposed to do one transaction per block. This is literally a copy and paste from how bitcoin does transactions
 * just converted to Java code. Additionally, it will test out some simple plus and minus transactions between to wallets.
 */

import java.util.*;
import java.security.Security;
import java.util.Base64;

//This user library can be found from https://repo1.maven.org/maven2/com/google/code/gson/gson/2.6.2/gson-2.6.2.jar
import com.google.gson.GsonBuilder;


public class BlockChainMain 
{
	//Creates the "Chain" of the block chain. This will link all of the hashes together.
	//Being a static method, any changes made to a hash will result in this whole chain breaking. Which is intended!
	//I have to still add a 
	public static ArrayList<Block> blockchain = new ArrayList<Block>();
	
	//I set a static difficulty of 1 as it was recommended by the person who taught me how to write this. You can change the difficulty as you please to check the times.
	public static int difficulty = 3;
	
	//Creating those wallets
	public static Wallet wallet1;
	public static Wallet wallet2;
	
	//HashMap for UTXOs
	public static HashMap<String,TransactionOutput> UTXOs = new HashMap<String,TransactionOutput>();
	
	//Setting Min Transaction
	public static float minimumTransaction = 0.1f;
	
	//OG Transaction
	public static Transaction genesisTransaction;
	
	public static void main(String[] args) {	
		//add our blocks to the blockchain ArrayList:
		Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider()); //Setup Bouncey castle as a Security Provider
		
		//Create wallets:
		wallet1 = new Wallet();
		wallet2 = new Wallet();		
		Wallet coinbase = new Wallet();
		
		//create genesis transaction, which sends 100 Tokens to wallet1: 
		genesisTransaction = new Transaction(coinbase.publicKey, wallet1.publicKey, 100f, null);
		genesisTransaction.generateSignature(coinbase.privateKey);	 //manually sign the genesis transaction	
		genesisTransaction.transactionId = "0"; //manually set the transaction id
		genesisTransaction.outputs.add(new TransactionOutput(genesisTransaction.reciepient, genesisTransaction.value, genesisTransaction.transactionId)); //manually add the Transactions Output
		UTXOs.put(genesisTransaction.outputs.get(0).id, genesisTransaction.outputs.get(0)); //its important to store our first transaction in the UTXOs list.
		
		//OG Output
		System.out.println("Creating and Mining Genesis block... ");
		Block genesis = new Block("0");
		genesis.addTransaction(genesisTransaction);
		addBlock(genesis);
		
		//Testing
		Block block1 = new Block(genesis.hash);
		System.out.println("\nwallet1's balance is: " + wallet1.getBalance());
		System.out.println("\nwallet1 is Attempting to send funds (40) to wallet2...");
		block1.addTransaction(wallet1.sendFunds(wallet2.publicKey, 40f));
		addBlock(block1);
		System.out.println("\nwallet1's balance is: " + wallet1.getBalance());
		System.out.println("wallet2's balance is: " + wallet2.getBalance());
		
		Block block2 = new Block(block1.hash);
		System.out.println("\nwallet1 Attempting to send more funds (1000) than it has...");
		block2.addTransaction(wallet1.sendFunds(wallet2.publicKey, 1000f));
		addBlock(block2);
		System.out.println("\nwallet1's balance is: " + wallet1.getBalance());
		System.out.println("wallet2's balance is: " + wallet2.getBalance());
		
		Block block3 = new Block(block2.hash);
		System.out.println("\nwallet2 is Attempting to send funds (20) to wallet1...");
		block3.addTransaction(wallet2.sendFunds( wallet1.publicKey, 20));
		System.out.println("\nwallet1's balance is: " + wallet1.getBalance());
		System.out.println("wallet2's balance is: " + wallet2.getBalance());
		
		//New Line
		System.out.print("\n");
		
		//Checks the block chain validity
		isChainValid();
		
	}
	
	//This is all verification for the block chain
	public static Boolean isChainValid() 
	{
		Block currentBlock; 
		Block previousBlock;
		
		String hashTarget = new String(new char[difficulty]).replace('\0', '0');
		HashMap<String,TransactionOutput> tempUTXOs = new HashMap<String,TransactionOutput>(); //a temporary working list of unspent transactions at a given block state.
		tempUTXOs.put(genesisTransaction.outputs.get(0).id, genesisTransaction.outputs.get(0));
		
		//loop through blockchain to check hashes:
		for(int i=1; i < blockchain.size(); i++) 
		{
			
			currentBlock = blockchain.get(i);
			previousBlock = blockchain.get(i-1);
			//compare registered hash and calculated hash:
			if(!currentBlock.hash.equals(currentBlock.convertToHash()) )
			{
				System.out.println("#Current Hashes not equal");
				return false;
			}
			//compare previous hash and registered previous hash
			if(!previousBlock.hash.equals(currentBlock.prevHash) ) 
			{
				System.out.println("#Previous Hashes not equal");
				return false;
			}
			//check if hash is solved
			if(!currentBlock.hash.substring( 0, difficulty).equals(hashTarget)) 
			{
				System.out.println("#This block hasn't been mined");
				return false;
			}
			
			//loop through blockchains transactions:
			TransactionOutput tempOutput;
			
			for(int t=0; t <currentBlock.transactions.size(); t++)
			{
				Transaction currentTransaction = currentBlock.transactions.get(t);
				
				if(!currentTransaction.verifiySignature()) 
				{
					System.out.println("#Signature on Transaction(" + t + ") is Invalid");
					
					return false; 
				}
				if(currentTransaction.getInputsValue() != currentTransaction.getOutputsValue()) 
				{
					System.out.println("#Inputs are note equal to outputs on Transaction(" + t + ")");
					
					return false; 
				}
				
				for(TransactionInput input: currentTransaction.inputs) 
				{	
					tempOutput = tempUTXOs.get(input.transactionOutputId);
					
					if(tempOutput == null) 
					{
						System.out.println("#Referenced input on Transaction(" + t + ") is Missing");
						
						return false;
					}
					
					if(input.UTXO.value != tempOutput.value) 
					{
						System.out.println("#Referenced input Transaction(" + t + ") value is Invalid");
						
						return false;
					}
					
					tempUTXOs.remove(input.transactionOutputId);
				}
				
				for(TransactionOutput output: currentTransaction.outputs) 
				{
					tempUTXOs.put(output.id, output);
				}
				
				if( currentTransaction.outputs.get(0).reciepient != currentTransaction.reciepient) 
				{
					System.out.println("#Transaction(" + t + ") output reciepient is not who it should be");
					
					return false;
				}
				if( currentTransaction.outputs.get(1).reciepient != currentTransaction.sender) 
				{
					System.out.println("#Transaction(" + t + ") output 'change' is not sender.");
					
					return false;
				}
				
			}
			
		}
		
		System.out.println("Blockchain is valid");
		
		return true;
	}
	
	public static void addBlock(Block newBlock) 
	{
		newBlock.mineBlock(difficulty);
		
		blockchain.add(newBlock);
	}
}