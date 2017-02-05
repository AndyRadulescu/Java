package application;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
/***
 * 
 * Class that has the role of encrypting the password set by the client, using MD5 algorithm.
 *
 */
public class Encrypter {
	private String input;
	private String result;
	
	public Encrypter(String input){
		this.input=input;
	}
	public String encrypt(){
		this.result=null;
		try {
			MessageDigest md=MessageDigest.getInstance("MD5");
			md.update(this.input.getBytes());
			this.result=new BigInteger(1,md.digest()).toString(16);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return this.result;
	}
	public String getInput() {
		return input;
	}
	public void setInput(String input) {
		this.input = input;
	}
}
