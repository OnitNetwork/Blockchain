/*
 * Name: 		  Eshan Sharma
 * Last Addition: 4/23/18 @ 6:09 PM.  
 * 
 * ----Readme----
 * Creates IDs to accept transactions using public keys.
 * 
 */
public class TransactionInput 
{
	//Public Variables (Must be public)
	public String transactionOutputId; //Reference to TransactionOutputs -> transactionId
	public TransactionOutput UTXO; //Contains the Unspent transaction output
	
	//Method to create an in transaction
	public TransactionInput(String transactionOutputId) 
	{
		this.transactionOutputId = transactionOutputId;
	}
}
