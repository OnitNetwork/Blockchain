/*
 * Name: 		  Eshan Sharma
 * Last Addition: 4/23/18 @ 6:21 PM.  
 * 
 * ----Readme----
 * Uses IDs made in TransactionInput to accept transactions and store them in the wallet class. I dont really have a local
 * storage set up yet :) Also assigns a token and checks it.
 * 
 */
import java.security.*;

public class TransactionOutput 
{
	//Public Variables (Must be public)
	public String id;
	public String parentTransactionId; //Transaction ID
	public PublicKey reciepient; //The owner of the token
	public float value; //value of tokens
	
	//Constructor
	public TransactionOutput(PublicKey reciepient, float value, String parentTransactionId) 
	{
		this.reciepient = reciepient;
		this.value = value;
		this.parentTransactionId = parentTransactionId;
		this.id = HashGen.translate(HashGen.getStringFromKey(reciepient)+Float.toString(value)+parentTransactionId);
	}	
	
	//Method to verify Tokens
		public boolean isMine(PublicKey publicKey) 
		{
			return (publicKey == reciepient);
		}
}
