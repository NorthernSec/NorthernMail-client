package com.NorthernSec.NorthernMail.client;

import java.io.IOException;
import java.net.UnknownHostException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import org.json.simple.JSONObject;

import com.NorthernSec.NorthernMail.Exceptions.EncryptionException;
import com.NorthernSec.NorthernMail.connection.ConnectionHandler;

public class MailClient {
  private ConnectionHandler con;
  private KeyManagement keyMan;

  final private String VERSION="NorthernMail Client v0.0 beta";

  public MailClient(){
	con=new ConnectionHandler();
	keyMan=new KeyManagement();
  }
  
  public void connect(String host, int port) throws UnknownHostException, IOException{
	  con.connect(host, port);
  }
  
  public void close() throws IOException{
	  con.close();
  }
  
  public String fetchMail() throws InvalidKeyException, IOException, EncryptionException{
	  con.send(("Fetch::"+VERSION).getBytes());
	  return new String(con.receive());
  }
  
  public void sendMail(String mail,String key, Boolean sign) throws EncryptionException, InvalidKeyException, IOException, ClassNotFoundException{
	  try {
		  PublicKey pubKey =keyMan.getPublicKey(key);
		  final Cipher cipher = Cipher.getInstance("RSA");
		  cipher.init(Cipher.ENCRYPT_MODE, pubKey);
		  String data = "";
		  //TODO get json library
		  JSONObject json = new JSONObject();
		  //TODO: replace send with con.aesSend(cipher.doFinal(data.getBytes()));
		  //TODO: complete encryption
	} catch (NoSuchAlgorithmException | NoSuchPaddingException | IllegalBlockSizeException | BadPaddingException e) {
		throw new EncryptionException(e);
	}
  }
  
  public static void main(String[] args){
	  try{
		  MailClient mc = new MailClient();
		  mc.connect("127.0.0.1", 5002);
		  System.out.println(mc.fetchMail());
		  //keyMan.generateKeypair(4096, "testkey");
		  mc.sendMail("some test mail", "testkey" , true);
		  mc.close();
	  } catch (Exception e){
		  e.printStackTrace();
	  }
  }
}
